package org.unicode.cldr.icu;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.unicode.cldr.ant.CLDRConverterTool;
import org.unicode.cldr.draft.FileUtilities;
import org.unicode.cldr.icu.ResourceSplitter.SplitInfo;
import org.unicode.cldr.tool.Option;
import org.unicode.cldr.tool.Option.Options;
import org.unicode.cldr.util.CLDRFile.DraftStatus;
import org.unicode.cldr.util.Factory;
import org.unicode.cldr.util.SupplementalDataInfo;

/**
 * Simpler mechanism for converting CLDR data to ICU Resource Bundles, intended
 * to replace LDML2ICUConverter. The format is almost entirely data-driven
 * instead of having lots of special-case code.
 * 
 * The flags used to specify the data to be generated are copied directly from
 * LDML2ICUConverter.
 * 
 * Unlike the instructions in CLDRConverterTool, this converter does not invoke
 * computeConvertibleXPaths to check if each xpath is convertible because the
 * xpaths that are convertible have already been filtered out by the regex lookups.
 * It may make more sense down the road to refactor CLDRConverterTool such that
 * this class doesn't inherit unnecessary functionality.
 * 
 * A rough overview of the new converter is available at
 * https://sites.google.com/site/cldr/development/coding-cldr-tools/newldml2icuconverter
 * 
 * @author jchye
 */
public class NewLdml2IcuConverter extends CLDRConverterTool {
    static final boolean DEBUG = true;

    static final Pattern SEMI = Pattern.compile("\\s*+;\\s*+");

    /*
     * The type of file to be converted.
     */
    enum Type {
        locales,
        dayPeriods,
        genderList, likelySubtags,
        metadata, metaZones,
        numberingSystems,
        plurals,
        postalCodeData,
        supplementalData,
        windowsZones,
        keyTypeData,
        brkitr,
        collation,
        rbnf;
    }

    private static final Options options = new Options(
        "Usage: LDML2ICUConverter [OPTIONS] [FILES]\n" +
            "This program is used to convert LDML files to ICU data text files.\n" +
            "Please refer to the following options. Options are not case sensitive.\n" +
            "\texample: org.unicode.cldr.icu.Ldml2IcuConverter -s xxx -d yyy en")
        .add("sourcedir", ".*", "Source directory for CLDR files")
        .add("destdir", ".*", ".", "Destination directory for output files, defaults to the current directory")
        .add("specialsdir", 'p', ".*", null, "Source directory for files containing special data, if any")
        .add("supplementaldir", 'm', ".*", null, "The supplemental data directory")
        .add("keeptogether", 'k', null, null,
            "Write locale data to one file instead of splitting into separate directories. For debugging")
        .add("type", 't', "\\w+", null, "The type of file to be generated")
        .add("xpath", 'x', ".*", null, "An optional xpath to debug the regexes with")
        .add("cldrVersion", 'c', ".*", null, "The version of the CLDR data (DEPRECATED).")
        .add("filter", 'f', null, null, "Perform filtering on the locale data to be converted.")
        .add("organization", 'o', ".*", null, "The organization to filter the data for");

    private static final String LOCALES_DIR = "locales";

    private boolean keepTogether = false;
    private Map<String, String> dirMapping;
    private Set<String> allDirs;
    private String sourceDir;
    private String destinationDir;
    private IcuDataSplitter splitter;
    // Either localeArg or locales will be non-null, but not both.
    // Used to convert files in main and collation.
    private String localeArg;
    private Set<String> locales;

    /**
     * Maps ICU paths to the directories they should end up in.
     */
    private Map<String, String> getDirMapping() {
        if (dirMapping == null) {
            dirMapping = loadMapFromFile("ldml2icu_dir_mapping.txt");
            allDirs = new HashSet<String>(dirMapping.values());
            allDirs.remove("*");
            allDirs.add(LOCALES_DIR);
        }
        return dirMapping;
    }

    private static Map<String, String> loadMapFromFile(String filename) {
        Map<String, String> map = new HashMap<String, String>();
        BufferedReader reader = FileUtilities.openFile(NewLdml2IcuConverter.class, filename);
        String line;
        try {
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0 || line.startsWith("#")) continue;
                String[] content = line.split(SEMI.toString());
                if (content.length != 2) {
                    throw new IllegalArgumentException("Invalid syntax of " + filename + " at line " + lineNum);
                }
                map.put(content[0], content[1]);
                lineNum++;
            }
        } catch (IOException e) {
            System.err.println("Failed to read fallback file.");
            e.printStackTrace();
        }
        return map;
    }

    private List<SplitInfo> loadSplitInfoFromFile() {
        Map<String, String> dirMapping = getDirMapping();
        List<SplitInfo> splitInfos = new ArrayList<SplitInfo>();
        for (Entry<String, String> entry : dirMapping.entrySet()) {
            SplitInfo splitInfo = new SplitInfo(entry.getKey(), entry.getValue());
            splitInfos.add(splitInfo);
        }
        return splitInfos;
    }

    @Override
    public void processArgs(String[] args) {
        Set<String> extraArgs = options.parse(args, true);
        // For supplemental output files, the supplemental directory is specified
        // as the source directory and the supplemental directory argument is
        // not required.
        if (!options.get("sourcedir").doesOccur()) {
            throw new IllegalArgumentException("Source directory must be specified.");
        }
        sourceDir = options.get("sourcedir").getValue();

        destinationDir = options.get("destdir").getValue();
        if (!options.get("type").doesOccur()) {
            throw new IllegalArgumentException("Type not specified");
        }
        Type type = Type.valueOf(options.get("type").getValue());
        keepTogether = options.get("keeptogether").doesOccur();
        if (!keepTogether && type == Type.supplementalData || type == Type.locales) {
            if (splitInfos == null) {
                splitInfos = loadSplitInfoFromFile();
            }
            splitter = IcuDataSplitter.make(destinationDir, splitInfos);
        }

        String debugXPath = options.get("xpath").getValue();
        // Quotes are stripped out at the command line so add them back in.
        if (debugXPath != null) {
            debugXPath = debugXPath.replaceAll("=([^\\]\"]++)\\]", "=\"$1\"\\]");
        }

        Factory specialFactory = null;
        File specialsDir = null;
        Option option = options.get("specialsdir");
        if (option.doesOccur()) {
            if (type == Type.rbnf) {
                specialsDir = new File(option.getValue());
            } else {
                specialFactory = Factory.make(option.getValue(), ".*");
            }
        } else if (type == Type.brkitr) {
            specialFactory = Factory.make(sourceDir, ".*");
        }

        // Get list of locales if defined.
        if (getLocalesMap() != null && getLocalesMap().size() > 0) {
            locales = new HashSet<String>();
            for (String filename : getLocalesMap().keySet()) {
                // Remove ".xml" from the end.
                locales.add(filename.substring(0, filename.length() - 4));
            }
        }

        if (extraArgs.size() > 0) {
            localeArg = extraArgs.iterator().next();
        }

        // Process files.
        switch (type) {
        case locales:
            // Generate locale data.
            SupplementalDataInfo supplementalDataInfo = null;
            option = options.get("supplementaldir");
            if (option.doesOccur()) {
                supplementalDataInfo = SupplementalDataInfo.getInstance(options.get("supplementaldir").getValue());
            } else {
                throw new IllegalArgumentException("Supplemental directory must be specified.");
            }

            // LocalesMap passed in from ant
            String[] localeList;
            Factory factory = null;
            if (locales != null) {
                factory = Factory.make(sourceDir, ".*", DraftStatus.contributed);
                localeList = new String[locales.size()];
                locales.toArray(localeList);
                Arrays.sort(localeList);
            } else if (localeArg != null) {
                factory = Factory.make(sourceDir, localeArg, DraftStatus.contributed);
                localeList = new String[factory.getAvailable().size()];
                factory.getAvailable().toArray(localeList);
            } else {
                throw new IllegalArgumentException("No files specified!");
            }

            String organization = options.get("organization").getValue();
            LocaleMapper mapper = new LocaleMapper(factory, specialFactory,
                supplementalDataInfo, options.get("filter").doesOccur(), organization);
            mapper.setDebugXPath(debugXPath);
            processLocales(mapper, localeList);
            break;
        case keyTypeData:
            processBcp47Data();
            break;
        case brkitr:
            BreakIteratorMapper brkMapper = new BreakIteratorMapper(specialFactory);
            for (String locale : specialFactory.getAvailable()) {
                IcuData data = brkMapper.fillFromCldr(locale);
                writeIcuData(data, destinationDir);
            }
            break;
        case collation:
            CollationMapper colMapper = new CollationMapper(sourceDir, specialFactory);
            processCollation(colMapper);
            break;
        case rbnf:
            RbnfMapper rbnfMapper = new RbnfMapper(new File(sourceDir), specialsDir);
            for (String locale : getFilteredLocales()) {
                IcuData data = rbnfMapper.fillFromCldr(locale);
                writeIcuData(data, destinationDir);
            }
            break;
        default: // supplemental data
            processSupplemental(type, debugXPath);
        }
    }

    private void processBcp47Data() {
        Bcp47Mapper mapper = new Bcp47Mapper(sourceDir);
        IcuData[] icuData = mapper.fillFromCldr();
        for (IcuData data : icuData) {
            writeIcuData(data, destinationDir);
        }
    }

    private void processSupplemental(Type type, String debugXPath) {
        IcuData icuData;
        if (type == Type.plurals) {
            PluralsMapper mapper = new PluralsMapper(sourceDir);
            icuData = mapper.fillFromCldr();
        } else if (type == Type.dayPeriods) {
            DayPeriodsMapper mapper = new DayPeriodsMapper(sourceDir);
            icuData = mapper.fillFromCldr();
        } else {
            SupplementalMapper mapper = SupplementalMapper.create(sourceDir);
            if (debugXPath != null) {
                mapper.setDebugXPath(debugXPath);
            }
            icuData = mapper.fillFromCldr(type.toString());
        }
        writeIcuData(icuData, destinationDir);
    }

    /**
     * Writes the given IcuData object to file.
     * 
     * @param icuData
     *            the IcuData object to be written
     * @param outputDir
     *            the destination directory of the output file
     */
    private void writeIcuData(IcuData icuData, String outputDir) {
        if (icuData.keySet().size() == 0) {
            throw new RuntimeException(icuData.getName() + " was not written because no data was generated.");
        }
        try {
            // Split data into different directories if necessary.
            // splitInfos is filled from the <remap> element in ICU's build.xml.
            if (splitter == null) {
                IcuTextWriter.writeToFile(icuData, outputDir);
            } else {
                String fallbackDir = new File(outputDir).getName();
                Map<String, IcuData> splitData = splitter.split(icuData, fallbackDir);
                for (String dir : splitData.keySet()) {
                    IcuTextWriter.writeToFile(splitData.get(dir), outputDir + "/../" + dir);
                }
            }
        } catch (IOException e) {
            System.err.println("Error while converting " + icuData.getSourceFile());
            e.printStackTrace();
        }
    }

    private void processLocales(LocaleMapper mapper, String[] locales) {
        for (String locale : locales) {
            long time = System.currentTimeMillis();
            IcuData icuData = mapper.fillFromCLDR(locale);
            writeIcuData(icuData, destinationDir);
            System.out.println("Converted " + locale + ".xml in " +
                (System.currentTimeMillis() - time) + "ms");
        }
    }

    private void processCollation(CollationMapper mapper) {
        List<String> locales = getFilteredLocales();
        for (String locale : locales) {
            if (!localeMatches(locale)) continue;
            System.out.println("Converting " + locale + "...");
            List<IcuData> subLocales = new ArrayList<IcuData>();
            IcuData icuData = mapper.fillFromCldr(locale, subLocales);
            writeIcuData(icuData, destinationDir);
            for (IcuData data : subLocales) {
              // Sub locales that don't match the filter may be in the list
              // even if their parent matches.
              if (!localeMatches(data.getName())) continue;
              writeIcuData(data, destinationDir);
            }
        }
    }

    private boolean localeMatches(String locale) {
        if (locales != null) {  // if running from ICU build
            return locales.contains(locale);
        } else if (localeArg != null) {  // if running converter directly
            return locale.matches(localeArg);
        } else {
            throw new IllegalArgumentException(
                    "Missing locale list. Please provide a list of locales or a regex.");
        }
    }

    /**
     * Returns the list of locale files in the source directory that match the
     * set of locales required for conversion.
     */
    public List<String> getFilteredLocales() {
        List<String> locales = new ArrayList<String>();
        for (String filename : new File(sourceDir).list()) {
            if (!filename.endsWith(".xml")) continue;
            String locale = filename.substring(0, filename.length() - 4);
            if (!localeMatches(locale)) continue;
            locales.add(locale);
        }
        return locales;
    }

    /**
     * TODO: call this method when we switch over to writing aliased files from
     * the LDML2ICUConverter. aliasList = aliasDeprecates.aliasList.
     * 
     * @param mapper
     * @param aliasList
     */
    // private void writeAliasedFiles(LocaleMapper mapper, List<Alias> aliasList) {
    // for (Alias alias : aliasList) {
    // IcuData icuData = mapper.fillFromCldr(alias);
    // if (icuData != null) {
    // writeIcuData(icuData, destinationDir);
    // }
    // }
    // }

    /**
     * In this prototype, just convert one file.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        long totalTime = System.currentTimeMillis();
        NewLdml2IcuConverter converter = new NewLdml2IcuConverter();
        converter.processArgs(args);
        System.out.println("Total time taken: " + (System.currentTimeMillis() - totalTime) + "ms");
    }

}