# UTS #35 Decimal Number Formatting Specification — Test Data Coverage Verification

This document traces each sentence of the number formatting specification for plain numbers (decimal, percent, scientific, and compact) in **UTS #35: Unicode LDML, Part 3: Numbers** ([`docs/ldml/tr35-numbers.md`](../../../docs/ldml/tr35-numbers.md)) to the test data dimension values that exercise it. The currency sections are covered in [`../currency/tr35_currency_test_coverage.md`](../currency/tr35_currency_test_coverage.md).

For each deep-linked section in UTS #35:
1. We link directly to the specification anchor in [`docs/ldml/tr35-numbers.md`](../../../docs/ldml/tr35-numbers.md), with its line range at the pinned commit.
2. We quote the exact normative snippet.
3. We break down every normative sentence/clause in the snippet into the exact **`(dimension = value)` combinations** required to exercise that behavior, with the expected result derived from the CLDR data.
4. We check those combinations against the dimensions of [`GenerateDecimalFormatTestData.java`](../../../tools/cldr-code/src/main/java/org/unicode/cldr/tool/GenerateDecimalFormatTestData.java): first whether the generator has each dimension, then whether each `(dimension = value)` is one of the CORE values of that dimension, and if not, whether it is one of its extended values.

### Pinned Inputs

| Input | Version |
| :--- | :--- |
| Specification text (`docs/ldml/tr35-numbers.md`) | `c33251cf82` (PR [#6191](https://github.com/unicode-org/cldr/pull/6191)) |
| CLDR data (`common/main`, `common/supplemental`) | `c33251cf82` |
| Generator and TSV files (`GenerateDecimalFormatTestData.java`, [`common/testData/decimal/`](.)) | `c33251cf82` (identical to `main` at `016a645a70`) |
| ICU4J that produced the TSV `expected` values | `79.0.1-20260723.162400-4` (pinned in `tools/pom.xml` at `c33251cf82`) |

Expected values in the breakdown tables are derived from the CLDR data and the specification, and cross-checked with the same ICU4J version.

### Coverage Status

| Status | Meaning |
| :--- | :--- |
| ✅ **Covered** | At least one combination of CORE dimension values represents the clause |
| 🟡 **Missing: *dimension* in CORE** | The needed value is only among the extended values of the dimension, so it is not combined with the other CORE values |
| 🟡 **Missing: *dimension*** | The needed value is in neither the CORE nor the extended values of the dimension |
| 🟡 **Missing: new dimension** | The generator does not have the dimension; the dimensions table below marks it **Needs to be added** |
| ❓ **Spec unclear** | The specification does not determine the expected value; see the linked ticket |
| ⚪ **Out of scope** | Not testable with formatting test data |

*dimension* is the name of a dimension, for example **Missing: `input` in CORE** or **Missing: `locale`**. This document checks coverage only: whether the `expected` values in the TSV files match the specification is out of scope.

---

## Current Test Data Dimensions in `GenerateDecimalFormatTestData.java`

Values at `c33251cf82`. `decimals.tsv` combines all CORE values with each other (225 rows). `decimals_modern_locales.tsv` combines each extended locale with all five styles and `CORE_NUMBERS` (2,400 rows), and `decimals_extended_numbers.tsv` combines the extended numbers with `CORE_LOCALES` and all five styles (6,300 rows).

| Dimension | Description | CORE values | Extended values |
| :--- | :--- | :--- | :--- |
| **`locale`** | CLDR locale identifier | `CORE_LOCALES`: `ar`, `ar_EG`, `bn`, `de`, `de_CH`, `en`, `ja`, `pt_PT`, `ru` | The other 96 locales that CLDR targets at `modern` coverage (`getExtendedModernLocales()`; `decimals_modern_locales.tsv`) |
| **`number_format`** | Kind of format | `"decimal"`, `"percent"`, `"scientific"` | — |
| **`format_length`** | Compact length | `""` (non-compact), `"short"`, `"long"` (compact) | — |
| **`input`** | Number to format | `CORE_NUMBERS`: `0.0`, `1.2`, `0.00831765`, `1234565.0`, `-1230.05` | 10ⁱ, 1.5 × 10ⁱ, and 5 × 10ⁱ for −6 ≤ i ≤ 12; `12`, `123`, `1234.56`, `1234567`, `0.000123`, `0.5`, `2.5`, `3.5`, `0.125`, `0.135`, `999.9`, `999999.9`; the negatives of all positive values, including the CORE ones; and `-0.0` (`getExtendedNumbers()` minus `CORE_NUMBERS`; `decimals_extended_numbers.tsv`) |
| **`numbering_system`** | The `nu` key of the Unicode locale identifier (`-u-nu-…`) | **Needs to be added** (Section 1): `"latn"`, `"native"`, `"traditio"`, `"finance"`. The current rows use no `nu` key, that is, the locale's default numbering system. | — |
| **`sign_display`** | When a sign is shown | **Needs to be added** (Sections 2 and 15): `"always"` (the `plusSign` for positive numbers), `"approximately"` (the `approximatelySign`), and `"approximately, always"` (both, as in `~+1.2`). The current rows show a sign only for negative numbers. | — |
| **`exponent_style`** | How the exponent of `"scientific"` is written | **Needs to be added** (Section 2): `"superscript"` (`superscriptingExponent`, as in `1.234565×10⁶`). The current rows use the `exponential` symbol (`1.234565E6`). | — |
| **`precision`** | Rounding of the number | **Needs to be added** (Sections 4, 6, and 11–13): `"significant: 3"` (minimum and maximum 3 significant digits) and `"fraction: 1"` (minimum and maximum 1 fraction digit), the two settings of the specification's compact example; `"fraction: 4-5"` (minimum 4 and maximum 5 fraction digits, as in the pattern `###0.0000#`); `"significant: 2-4"` (minimum 2 and maximum 4 significant digits, as in the pattern `@@##`); `"increment: 50"` and `"increment: 0.65"` (rounding increments). The current rows use the default precision. | — |
| **`grouping`** | Whether grouping separators are shown | **Needs to be added** (Section 6): `"off"` (as in the pattern `###0.#####`). The current rows use the locale's grouping. | — |
| **`integer_width`** | Minimum and maximum number of integer digits | **Needs to be added** (Sections 6 and 10): `"min: 5"` (as in the pattern `00000.0000`) and `"max: 2"`. The current rows use a minimum of 1 and no maximum. | — |
| **`misc_pattern`** | A pattern from `miscPatterns` applied to the formatted number | **Needs to be added** (Section 14): `"atLeast"`, `"atMost"`. The current rows use none of these patterns. | — |
| **`upper_input`** | Upper bound of a number range | **Needs to be added** (Section 15): `5000000.0`, `-1.2`, `1.2`, `1234567.0`, each paired with one `input` (`1234565.0`, `-1230.05`, `1.2`, `1234565.0`). The current rows format single numbers. | — |

The generator produces 5 of the 9 combinations of `number_format` and `format_length`: `"decimal"` with each length, and `"percent"` and `"scientific"` with `""` only.

A section that needs a dimension the generator does not have adds it to this table, marked **Needs to be added**.

---

## Section 1: Numbering Systems (`#defaultNumberingSystem`, `#otherNumberingSystems`)

* **TR35 Specification Link**: [`tr35-numbers.md#defaultNumberingSystem`](../../../docs/ldml/tr35-numbers.md#defaultNumberingSystem) and [`tr35-numbers.md#otherNumberingSystems`](../../../docs/ldml/tr35-numbers.md#otherNumberingSystems) (UTS #35 Part 3, Sections 2.1 and 2.2; L164–L199 at `c33251cf82`)
* **Related specification text**: L117–L154 ([Numbering Systems](../../../docs/ldml/tr35-numbers.md#Numbering_Systems): numeric and algorithmic systems), L303–L306 and L370–L374 (the `numberSystem` attribute of `<symbols>` and of the number formats), and [Numbering System Data](../../../docs/ldml/tr35.md#Numbering%20System%20Data) in Part 1 (the `nu` identifiers)

### 1.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> ### <a name="defaultNumberingSystem" href="../../../docs/ldml/tr35-numbers.md#defaultNumberingSystem">Default Numbering System</a>
>
> ```dtd
> <!ELEMENT defaultNumberingSystem ( #PCDATA )>
> ```
>
> This element indicates which numbering system should be used for presentation of numeric quantities in the given locale.
>
> ### <a name="otherNumberingSystems" href="../../../docs/ldml/tr35-numbers.md#otherNumberingSystems">Other Numbering Systems</a>
>
> ```dtd
> <!ELEMENT otherNumberingSystems ( alias | ( native*, traditional*, finance*)) >
> ```
>
> This element defines general categories of numbering systems that are sometimes used in the given locale for formatting numeric quantities. These additional numbering systems are often used in very specific contexts, such as in calendars or for financial purposes. There are currently three defined categories, as follows:
>
> **native**
>
> > Defines the numbering system used for the native digits, usually defined as a part of the script used to write the language. The native numbering system can only be a numeric positional decimal-digit numbering system, using digits with General_Category=Decimal_Number. Note: In locales where the native numbering system is the default, it is assumed that the numbering system "latn" (Western digits 0-9) is always acceptable, and can be selected using the -nu keyword as part of a Unicode locale identifier.
>
> **traditional**
>
> > Defines the traditional numerals for a locale. This numbering system may be numeric or algorithmic. If the traditional numbering system is not defined, applications should use the native numbering system as a fallback.
>
> **finance**
>
> > Defines the numbering system used for financial quantities. This numbering system may be numeric or algorithmic. This is often used for ideographic languages such as Chinese, where it would be easy to alter an amount represented in the default numbering system simply by adding additional strokes. If the financial numbering system is not specified, applications should use the default numbering system as a fallback.
>
> The categories defined for other numbering systems can be used in a Unicode locale identifier to select the proper numbering system without having to know the specific numbering system by name. For example:
>
> *   To select Hindi language using the native digits for numeric formatting, use locale ID: "hi-IN-u-nu-native".
> *   To select Chinese language using the appropriate financial numerals, use locale ID: "zh-u-nu-finance".
> *   To select Tamil language using the traditional Tamil numerals, use locale ID: "ta-u-nu-traditio".
> *   To select Arabic language using western digits 0-9, use locale ID: "ar-u-nu-latn".
>
> For more information on numbering systems and their definitions, see _[Section 1: Numbering Systems](../../../docs/ldml/tr35-numbers.md#Numbering_Systems)_.

---

### 1.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** Resolved values of the three elements. `↑↑↑` and missing values inherit from the parent locale, and finally from `root`.

| Locale | `defaultNumberingSystem` | `native` | `traditional` | `finance` |
| :--- | :--- | :--- | :--- | :--- |
| `root` | `latn` | `latn` | — | — |
| `en`, `de`, `de_CH`, `pt_PT`, `ru` | `latn` (inherited) | `latn` (inherited) | — | — |
| `ar` | `latn` (inherited; `ar` also has `<defaultNumberingSystem alt="latn">latn</defaultNumberingSystem>`) | `arab` | — | — |
| `ar_EG` | `arab` | `arab` (inherited from `ar`) | — | — |
| `bn` | `beng` | `beng` | — | — |
| `ja` | `latn` (inherited) | `latn` (inherited) | `jpan` (algorithmic) | `jpanfin` (algorithmic) |
| `hi` (extended) | `latn` (inherited) | `deva` | — | — |
| `ta` (extended) | `latn` (inherited) | `tamldec` | `taml` (algorithmic) | — |
| `zh` (extended) | `latn` (inherited) | `hanidec` | `hans` (algorithmic) | `hansfin` (algorithmic) |

Across `common/main`, 53 locales have a numeric default other than `latn` (21 `ar_*` locales with `arab`, `fa` with `arabext`, `mr` with `deva`, …), and 57 locales have a `native` value, all numeric. All 18 `traditional` values (`am`, `el`, `he`, `ja`, `ta`, `zh`, …) and all 5 `finance` values (`ja`, `yue`, `yue_Hans`, `zh`, `zh_Hant`) are algorithmic. The extended locales with a default other than `latn` are `as`, `fa`, `mr`, `my`, `ne`, `ps`, and `sd`.

Digits (`common/supplemental/numberingSystems.xml`): `arab` `٠١٢٣٤٥٦٧٨٩` (U+0660–U+0669), `beng` `০১২৩৪৫৬৭৮৯` (U+09E6–U+09EF), `deva` `०१२३४५६७८९` (U+0966–U+096F). The symbols come from `<symbols numberSystem="…">` of the selected numbering system (L305): for `arab`, `root` has `٫` (U+066B) as `decimal` and `٬` (U+066C) as `group`; `beng` uses the `latn` symbols.

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S1.1** | **`defaultNumberingSystem`** — *"This element indicates which numbering system should be used for presentation of numeric quantities in the given locale."* | • **S1.1a**: `locale` whose default is `latn` (e.g. `"en"`, `"de"`, `"ar"`), any `number_format`, any `input`<br>• **S1.1b**: `locale` whose default is a numeric system other than `latn` (`"ar_EG"`: `arab`; `"bn"`: `beng`), any `number_format` and `format_length`, any `input` | • S1.1a: `en` 1234565.0 → `1,234,565`; `ar` 1234565.0 → `1,234,565` (`ar` inherits `latn` from `root`).<br>• S1.1b: `ar_EG` 1234565.0 → `١٬٢٣٤٬٥٦٥`; `ar_EG` −1230.05 → `؜-١٬٢٣٠٫٠٥` / `"\u061C-\u0661\u066C\u0662\u0663\u0660\u066B\u0660\u0665"`; `bn` 1234565.0 → `১২,৩৪,৫৬৫` (`beng` digits, `latn` symbols, and the `bn` pattern `#,##,##0.###`). |
| **S1.2** | **`otherNumberingSystems`** — *"This element defines general categories of numbering systems that are sometimes used in the given locale for formatting numeric quantities. [...] There are currently three defined categories, as follows:"* | — (introduces S1.3–S1.7) | — |
| **S1.3** | **`native`** — *"Defines the numbering system used for the native digits, usually defined as a part of the script used to write the language."* | • **S1.3a**: `numbering_system = "native"` × `locale` whose `native` differs from its default (`"ar"`), any `input`<br>• **S1.3b**: `numbering_system = "native"` × `locale` whose `native` is its default (`"bn"`, `"en"`) | • S1.3a: `ar-u-nu-native` 1234565.0 → `١٬٢٣٤٬٥٦٥` (`arab`, not the default `latn`). Spec example: `hi-IN-u-nu-native` 1234565.0 → `१२,३४,५६५`.<br>• S1.3b: unchanged: `bn-u-nu-native` 1234565.0 → `১২,৩৪,৫৬৫`; `en-u-nu-native` → `1,234,565`. |
| **S1.4** | **`native`** — *"The native numbering system can only be a numeric positional decimal-digit numbering system, using digits with General_Category=Decimal_Number."* | — | A constraint on the data: all 57 `native` values are numeric systems. |
| **S1.5** | **`native`** — *"Note: In locales where the native numbering system is the default, it is assumed that the numbering system \"latn\" (Western digits 0-9) is always acceptable, and can be selected using the -nu keyword as part of a Unicode locale identifier."* | • `numbering_system = "latn"` × `locale` whose default is its `native` system (`"ar_EG"`, `"bn"`), any `input` | `ar-EG-u-nu-latn` 1234565.0 → `1,234,565`; −1230.05 → `‎-1,230.05` / `"\u200E-1,230.05"` (the `latn` symbols of `ar`); `bn-u-nu-latn` 1234565.0 → `12,34,565`. |
| **S1.6** | **`traditional`** — *"Defines the traditional numerals for a locale. This numbering system may be numeric or algorithmic. If the traditional numbering system is not defined, applications should use the native numbering system as a fallback."* | • **S1.6a**: `numbering_system = "traditio"` × `locale` with a `traditional` value (`"ja"`: `jpan`)<br>• **S1.6b**: `numbering_system = "traditio"` × `locale` without `traditional` whose `native` differs from its default (`"ar"`), any `input` | • S1.6a: `jpan` is algorithmic (`rules="ja/SpelloutRules/spellout-cardinal"`), so the number is formatted with rule-based number formatting, not with a pattern. No CLDR locale has a numeric `traditional` value.<br>• S1.6b: `ar-u-nu-traditio` 1234565.0 → `١٬٢٣٤٬٥٦٥` (the `native` `arab`, not the default `latn`). |
| **S1.7** | **`finance`** — *"Defines the numbering system used for financial quantities. This numbering system may be numeric or algorithmic. [...] If the financial numbering system is not specified, applications should use the default numbering system as a fallback."* | • **S1.7a**: `numbering_system = "finance"` × `locale` with a `finance` value (`"ja"`: `jpanfin`)<br>• **S1.7b**: `numbering_system = "finance"` × `locale` without `finance` whose default differs from its `native` (`"ar"`), any `input` | • S1.7a: `jpanfin` is algorithmic (`ja/SpelloutRules/spellout-cardinal-financial`), as are all 5 `finance` values.<br>• S1.7b: `ar-u-nu-finance` 1234565.0 → `1,234,565` (the default `latn`, not the `native` `arab`). |
| **S1.8** | *"The categories defined for other numbering systems can be used in a Unicode locale identifier to select the proper numbering system without having to know the specific numbering system by name."* | • `numbering_system` = `"native"`, `"traditio"`, or `"finance"` (S1.3, S1.6, S1.7) | The four examples: `hi-IN-u-nu-native` (S1.3a); `zh-u-nu-finance` (`hansfin`) and `ta-u-nu-traditio` (`taml`), both algorithmic (S1.6a, S1.7a); `ar-u-nu-latn`, a numbering system selected by name (S1.5). |

---

### 1.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S1.1a** | `locale` with default `latn` × any `number_format` × any `input` | ✅ **Covered** | CORE values: `en`, `de`, `de_CH`, `ja`, `pt_PT`, `ru`, and `ar` in `CORE_LOCALES`, with all five styles and `CORE_NUMBERS`. |
| **S1.1b** | `locale` with a numeric default other than `latn` × any `number_format` and `format_length` × any `input` | ✅ **Covered** | CORE values: `ar_EG` (`arab`) and `bn` (`beng`) in `CORE_LOCALES`, with all five styles and `CORE_NUMBERS`. The extended locales `as`, `fa`, `mr`, `my`, `ne`, `ps`, and `sd` add `arabext`, `deva`, and `mymr`. |
| **S1.3a–b**, **S1.5**, **S1.6b**, **S1.7b**, **S1.8** | `numbering_system` = `"native"`, `"latn"`, `"traditio"`, or `"finance"` × the locales above | 🟡 **Missing: new dimension** | The generator formats each locale identifier as is (`new ULocale(localeStr)`), and no CORE or extended locale identifier has a `-u-nu-` key. The locales are CORE values (`ar`, `ar_EG`, `bn`, `en`). **Action**: add the `numbering_system` dimension (see the Summary). |
| **S1.4** | — | ⚪ **Out of scope** | A constraint on the CLDR data, not on formatting. |
| **S1.6a**, **S1.7a** | `numbering_system` = `"traditio"` or `"finance"` × a locale whose value is algorithmic (`ja`) | ⚪ **Out of scope** | Algorithmic numbering systems are formatted with rule-based number formatting ([Rule-Based Number Formatting](../../../docs/ldml/tr35-numbers.md#Rule-Based_Number_Formatting)), which this generator does not cover. |

### 1.4 Notes

* `ar`, `hnj`, and `mww` have a `<defaultNumberingSystem alt="latn">`. The specification text does not describe an `alt` value for `defaultNumberingSystem`.
* The spec example `ar-u-nu-latn` gives the same result as `ar`: in the CLDR data, `ar` already defaults to `latn`, and only `ar_EG` and 20 other `ar_*` locales default to `arab`. `ar-EG-u-nu-latn` (S1.5) shows the switch.
* S1.6b and S1.7b need a locale whose `native` differs from its default, such as `ar`: in `bn` or `en` the fallbacks to `native` and to the default give the same result.

---

## Section 2: Number Symbols (`#Number_Symbols`)

* **TR35 Specification Link**: [`tr35-numbers.md#Number_Symbols`](../../../docs/ldml/tr35-numbers.md#Number_Symbols) (UTS #35 Part 3, Section 2.3: *Number Symbols*; L201–L306 at `c33251cf82`). The quote below has L207–L265, the general text and the symbols used for plain numbers, and L302–L305, the `numberSystem` attribute. The other symbols are covered elsewhere: `currencyDecimal` and `currencyGroup` (L267–L273) in Section 1 of the [currency document](../currency/tr35_currency_test_coverage.md), and `timeSeparator` (L275–L279) belongs to date and time formats.
* **Related specification text**: L679–L691 ([Number Pattern Character Definitions](../../../docs/ldml/tr35-numbers.md#Number_Pattern_Character_Definitions): the pattern characters `.`, `-`, `,`, `E`, `+`, `%`, and `‰`, and the symbols that replace them), L755–L761 ([Explicit Plus Signs](../../../docs/ldml/tr35-numbers.md#Explicit_Plus)), L775–L779 ([Special Values](../../../docs/ldml/tr35-numbers.md#special-values): NaN and infinity), and L781 onward ([Scientific Notation](../../../docs/ldml/tr35-numbers.md#sci))

### 2.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> Number symbols define the localized symbols that are commonly used when formatting numbers in a given locale. These symbols can be referenced using a number formatting pattern as defined in _[Section 3: Number Format Patterns](../../../docs/ldml/tr35-numbers.md#Number_Format_Patterns)_.
>
> The available number symbols are as follows:
>
> **decimal**
>
> > separates the integer and fractional part of the number.
>
> **group**
>
> > separates clusters of integer digits to make large numbers more legible; commonly used for thousands (grouping size 3, e.g. "100,000,000") or in some locales, ten-thousands (grouping size 4, e.g. "1,0000,0000"). There may be two different grouping sizes: The _primary grouping size_ used for the least significant integer group, and the _secondary grouping size_ used for more significant groups; these are not the same in all locales (e.g. "12,34,56,789"). If a pattern contains multiple grouping separators, the interval between the last one and the end of the integer defines the primary grouping size, and the interval between the last two defines the secondary grouping size. All others are ignored, so "#,##,###,####" == "###,###,####" == "##,#,###,####".
>
> **list**
>
> > symbol used to separate numbers in a list intended to represent structured data such as an array; must be different from the **decimal** value. This list separator is for “non-linguistic” usage as opposed to the listPatterns for “linguistic” lists (e.g. “Bob, Carol, and Ted”) described in Part 2, _[List Patterns](../../../docs/ldml/tr35-general.md#ListPatterns)_.
>
> **percentSign**
>
> > symbol used to indicate a percentage (1/100th) amount. (If present, the value is also multiplied by 100 before formatting. That way 1.23 → 123%)
>
> ~~**nativeZeroDigit**~~
>
> > Deprecated - do not use.
>
> ~~**patternDigit**~~
>
> > Deprecated. This was formerly used to provide the localized pattern character corresponding to '#', but localization of the pattern characters themselves has been deprecated for some time (determining the locale-specific _replacements_ for pattern characters is of course not deprecated and is part of normal number formatting).
>
> **minusSign**
>
> > Symbol used to denote negative value.
>
> **plusSign**
>
> > Symbol used to denote positive value.  It can be used to produce modified patterns, so that 3.12 is formatted as "+3.12", for example. The standard number patterns (except for type="accounting") will contain the minusSign, explicitly or implicitly. In the explicit pattern, the value of the plusSign can be substituted for the value of the minusSign to produce a pattern that has an explicit plus sign.
>
> **approximatelySign**
>
> > Symbol used to denote a value that is approximate but not exact. The symbol is substituted in place of the minusSign using the same semantics as plusSign substitution.
>
> **exponential**
>
> > Symbol separating the mantissa and exponent values.
>
> **superscriptingExponent**
>
> > (Programmers are used to the fallback exponent style “1.23E4”, but that should not be shown to end-users. Instead, the exponential notation superscriptingExponent should be used to show a format like “1.23 × 10<sup>4</sup>”. ) The superscripting can use markup, such as `<sup>4</sup>` in HTML, or for the special case of Latin digits, use the superscript characters: U+207B ( ⁻ ), U+2070 ( ⁰ ), U+00B9 ( ¹ ), U+00B2 ( ² ), U+00B3 ( ³ ), U+2074 ( ⁴ ) .. U+2079 ( ⁹ ).
>
> **perMille**
>
> > symbol used to indicate a per-mille (1/1000th) amount. (If present, the value is also multiplied by 1000 before formatting. That way 1.23 → 1230 [1/000])
>
> **infinity**
>
> > The infinity sign. Corresponds to the IEEE infinity bit pattern.
>
> **nan - Not a number**
>
> > The NaN sign. Corresponds to the IEEE NaN bit pattern.
>
> […]
>
> ```dtd
> <!ATTLIST symbols numberSystem CDATA #IMPLIED >
> ```
> The `numberSystem` attribute is used to specify that the given number symbols are to be used when the given numbering system is active. Number symbols can only be defined for numbering systems of the "numeric" type, since any special symbols required for an algorithmic numbering system should be specified by the RBNF formatting rules used for that numbering system. The `numberSystem` attribute will always be present in CLDR 49 and beyond. The DTD does not require it, so that older versions of CLDR can be read with as before.  Locales that specify a numbering system other than "latn" as the default should also specify number formatting symbols that are appropriate for use within the context of the given numbering system. For example, a locale that uses the Arabic-Indic digits as its default would likely use an Arabic comma for the grouping separator rather than the ASCII comma.

---

### 2.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** Resolved symbols of the default numbering system of each CORE locale (`<symbols numberSystem="…">`, following `↑↑↑` and missing values to the parent locale and to `root`; `root` makes the `beng` symbols an alias of the `latn` ones).

| Locale (numbering system) | `decimal` | `group` | `percentSign` | `minusSign` | `plusSign` | `approximatelySign` | `exponential` | `superscriptingExponent` | `perMille` | `nan` |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `en`, `ja` (`latn`) | `.` | `,` | `%` | `-` | `+` | `~` (`ja`: `約`) | `E` | `×` | `‰` | `NaN` |
| `de` (`latn`) | `,` | `.` | `%` | `-` | `+` | `≈` | `E` | `·` | `‰` | `NaN` |
| `de_CH` (`latn`) | `.` | `'` (U+0027) | `%` | `-` | `+` | `≈` | `E` | `·` | `‰` | `NaN` |
| `pt_PT`, `ru` (`latn`) | `,` | `"\u00A0"` | `%` | `-` | `+` | `~` (`ru`: `≈`) | `E` | `×` | `‰` | `NaN` (`ru`: `не число` / `"\u043D\u0435\u00A0\u0447\u0438\u0441\u043B\u043E"`) |
| `bn` (`beng`) | `.` | `,` | `%` | `-` | `+` | `~` | `E` | `×` | `‰` | `NaN` |
| `ar` (`latn`) | `.` | `,` | `"\u200E%\u200E"` | `"\u200E-"` | `"\u200E+"` | `~` | `E` | `×` | `‰` | `ليس رقمًا` / `"\u0644\u064A\u0633\u00A0\u0631\u0642\u0645\u064B\u0627"` |
| `ar_EG` (`arab`) | `٫` (U+066B) | `٬` (U+066C) | `"\u066A\u061C"` | `"\u061C-"` | `"\u061C+"` | `~` | `أس` | `×` | `؉` (U+0609) | as `ar` |

`infinity` is `∞` (U+221E) in all of them. Patterns (default numbering system): `#,##0.###` for decimal (`bn`: `#,##,##0.###`), `#,##0%` for percent (`de` and `ru`: `"#,##0\u00A0%"`), and `#E0` for scientific.

Scans of `common/main` (all locales): no decimal, percent, scientific, or currency pattern has a primary grouping size of 4 or more than two grouping separators, and no pattern contains `‰`. 26 locales have a secondary grouping size different from the primary one (`bn`, `hi`, `en_IN`, …). 15 locales have U+2212 MINUS SIGN as `minusSign`; among the extended locales, `et`, `eu`, `fa`, `fi`, `hr`, `lt`, `no`, `sl`, and `sv`.

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S2.1** | *"Number symbols define the localized symbols that are commonly used when formatting numbers in a given locale. These symbols can be referenced using a number formatting pattern [...]"* | — (introduces S2.2–S2.14) | — |
| **S2.2** | **`decimal`** — *"separates the integer and fractional part of the number."* | • `locale` whose `decimal` is not `.` (e.g. `"de"`, `"ar_EG"`) and one whose `decimal` is `.` (`"en"`)<br>• `input` with a fractional part (e.g. `1.2`) | `de` 1.2 → `1,2`; `ar_EG` → `١٫٢`; `en` → `1.2`. |
| **S2.3** | **`group`** — *"separates clusters of integer digits to make large numbers more legible; commonly used for thousands (grouping size 3, e.g. "100,000,000") or in some locales, ten-thousands (grouping size 4, e.g. "1,0000,0000")."* | • **S2.3a**: grouping size 3: any `locale` with a primary grouping size 3, `input` of magnitude ≥ 1000 (e.g. `1234565.0`), with several `group` values (`"en"`, `"de"`, `"de_CH"`, `"ru"`, `"ar_EG"`)<br>• **S2.3b**: grouping size 4 | • S2.3a: `en` 1234565.0 → `1,234,565`; `de` → `1.234.565`; `de_CH` → `1'234'565`; `ru` → `1 234 565` / `"1\u00A0234\u00A0565"`; `ar_EG` → `١٬٢٣٤٬٥٦٥`.<br>• S2.3b: no CLDR pattern has a primary grouping size of 4. |
| **S2.4** | **`group`** — *"There may be two different grouping sizes: The primary grouping size used for the least significant integer group, and the secondary grouping size used for more significant groups; these are not the same in all locales (e.g. "12,34,56,789"). If a pattern contains multiple grouping separators, the interval between the last one and the end of the integer defines the primary grouping size, and the interval between the last two defines the secondary grouping size. All others are ignored, so "#,##,###,####" == "###,###,####" == "##,#,###,####"."* | • **S2.4a**: `locale` with different primary and secondary grouping sizes (`"bn"`: `#,##,##0.###`), `input` with at least 6 integer digits (e.g. `1234565.0`)<br>• **S2.4b**: a pattern with more than two grouping separators (*"All others are ignored"*) | • S2.4a: `bn` 1234565.0 → `১২,৩৪,৫৬৫` (primary 3, secondary 2).<br>• S2.4b: no CLDR pattern has more than two grouping separators. |
| **S2.5** | **`list`** — *"symbol used to separate numbers in a list intended to represent structured data such as an array; must be different from the **decimal** value. [...]"* | — | Number formatting does not use `list`. That it differs from `decimal` is a constraint on the data. |
| **S2.6** | **`percentSign`** — *"symbol used to indicate a percentage (1/100th) amount. (If present, the value is also multiplied by 100 before formatting. That way 1.23 → 123%)"* | • `number_format = "percent"` × several `locale` values (`"en"`, `"de"`, `"ar"`, `"ar_EG"`), any `input` | `en` 1.2 → `120%`; `de` → `120 %` / `"120\u00A0%"`; `ar` → `120‎%‎` / `"120\u200E%\u200E"`; `ar_EG` → `١٢٠٪؜` / `"\u0661\u0662\u0660\u066A\u061C"`. |
| **S2.7** | **`nativeZeroDigit`**, **`patternDigit`** — *"Deprecated - do not use."*, *"Deprecated. [...]"* | — | Deprecated; not used in formatting. |
| **S2.8** | **`minusSign`** — *"Symbol used to denote negative value."* | • negative `input` (e.g. `-1230.05`) × several `locale` values (`"en"`, `"ar"`, `"ar_EG"`) | `en` −1230.05 → `-1,230.05`; `ar` → `‎-1,230.05` / `"\u200E-1,230.05"`; `ar_EG` → `؜-١٬٢٣٠٫٠٥` / `"\u061C-\u0661\u066C\u0662\u0663\u0660\u066B\u0660\u0665"`. |
| **S2.9** | **`plusSign`** — *"Symbol used to denote positive value. It can be used to produce modified patterns, so that 3.12 is formatted as "+3.12", for example. The standard number patterns (except for type="accounting") will contain the minusSign, explicitly or implicitly. In the explicit pattern, the value of the plusSign can be substituted for the value of the minusSign to produce a pattern that has an explicit plus sign."* | • `sign_display = "always"` × positive `input` (e.g. `1.2`) × several `locale` values (`"en"`, `"ar"`, `"ar_EG"`) | `en` 1.2 → `+1.2`; `ar` → `‎+1.2` / `"\u200E+1.2"`; `ar_EG` → `؜+١٫٢` / `"\u061C+\u0661\u066B\u0662"`. |
| **S2.10** | **`approximatelySign`** — *"Symbol used to denote a value that is approximate but not exact. The symbol is substituted in place of the minusSign using the same semantics as plusSign substitution."* | • `sign_display = "approximately"` × positive `input` × several `locale` values (`"en"`, `"de"`, `"ja"`) | `en` 1.2 → `~1.2`; `de` → `≈1,2`; `ja` → `約1.2`. |
| **S2.11** | **`exponential`** — *"Symbol separating the mantissa and exponent values."* | • `number_format = "scientific"` × several `locale` values (`"en"`, `"ar_EG"`), any `input` | `en` 1234565.0 → `1.234565E6`; `ar_EG` → `١٫٢٣٤٥٦٥أس٦`. |
| **S2.12** | **`superscriptingExponent`** — *"(Programmers are used to the fallback exponent style “1.23E4”, but that should not be shown to end-users. Instead, the exponential notation superscriptingExponent should be used to show a format like “1.23 × 10<sup>4</sup>”. ) The superscripting can use markup, such as `<sup>4</sup>` in HTML, or for the special case of Latin digits, use the superscript characters: [...]"* | • `number_format = "scientific"` × `exponent_style = "superscript"` × `locale` with Latin digits and different `superscriptingExponent` values (`"en"`: `×`; `"de"`: `·`), any `input` | `en` 1234565.0 → `1.234565×10⁶`; `de` → `1,234565·10⁶` (see the notes on spacing). |
| **S2.13** | **`perMille`** — *"symbol used to indicate a per-mille (1/1000th) amount. (If present, the value is also multiplied by 1000 before formatting. That way 1.23 → 1230 [1/000])"* | — | No CLDR pattern contains `‰`. Per-mille amounts are formatted as the unit `concentr-permille`, outside this generator. |
| **S2.14** | **`infinity`**, **`nan`** — *"The infinity sign. Corresponds to the IEEE infinity bit pattern."*, *"The NaN sign. Corresponds to the IEEE NaN bit pattern."* | • `input` = `Infinity`, `-Infinity`, `NaN` × several `locale` values (`"en"`, `"ar_EG"`, `"ru"`) | `en` → `∞`, `-∞`, `NaN`; `ar_EG` −∞ → `؜-∞` / `"\u061C-\u221E"`; `ru` NaN → `не число` / `"\u043D\u0435\u00A0\u0447\u0438\u0441\u043B\u043E"`. |
| **S2.15** | **`numberSystem`** — *"The `numberSystem` attribute is used to specify that the given number symbols are to be used when the given numbering system is active. [...] Locales that specify a numbering system other than "latn" as the default should also specify number formatting symbols that are appropriate for use within the context of the given numbering system. For example, a locale that uses the Arabic-Indic digits as its default would likely use an Arabic comma for the grouping separator rather than the ASCII comma."* | • **S2.15a**: `locale` whose default is not `latn` (`"ar_EG"`) and the same language with `latn` (`"ar"`), `input` of magnitude ≥ 1000<br>• **S2.15b**: *"Number symbols can only be defined for numbering systems of the "numeric" type [...]"* and *"The `numberSystem` attribute will always be present in CLDR 49 and beyond."* | • S2.15a: `ar_EG` 1234565.0 → `١٬٢٣٤٬٥٦٥` (group `٬` U+066C, ARABIC THOUSANDS SEPARATOR) vs. `ar` → `1,234,565`. With Section 1's `numbering_system`, `ar-u-nu-native` also uses the `arab` symbols.<br>• S2.15b: constraints on the data. |

---

### 2.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S2.2** | `locale` with `decimal` `,` / `٫` / `.` × fractional `input` | ✅ **Covered** | CORE values: `de`, `ar_EG`, and `en` in `CORE_LOCALES`; `1.2` and `-1230.05` in `CORE_NUMBERS`. |
| **S2.3a** | grouping size 3 × several `group` values × `input` ≥ 1000 | ✅ **Covered** | CORE values: `en`, `de`, `de_CH`, `ru`, `pt_PT`, and `ar_EG`; `1234565.0` and `-1230.05` in `CORE_NUMBERS`. |
| **S2.3b**, **S2.4b** | a pattern with grouping size 4, or with more than two grouping separators | ⚪ **Out of scope** | No CLDR data. |
| **S2.4a** | `locale = "bn"` × `input` with ≥ 6 integer digits | ✅ **Covered** | CORE values: `bn` in `CORE_LOCALES`; `1234565.0` in `CORE_NUMBERS`. |
| **S2.5**, **S2.7** | — | ⚪ **Out of scope** | Not used in number formatting (`list`), or deprecated. |
| **S2.6** | `number_format = "percent"` × several `locale` values | ✅ **Covered** | CORE values: `"percent"` with `format_length = ""`, all `CORE_LOCALES`, and `CORE_NUMBERS`. |
| **S2.8** | negative `input` × several `locale` values | ✅ **Covered** | CORE values: `-1230.05` in `CORE_NUMBERS`, all `CORE_LOCALES`. U+2212 MINUS SIGN is only in extended locales (`fi`, `sv`, …: `decimals_modern_locales.tsv`). |
| **S2.9**, **S2.10** | `sign_display = "always"` or `"approximately"` × positive `input` | 🟡 **Missing: new dimension** | The generator does not set a sign display, so it shows a sign only for negative numbers. **Action**: add the `sign_display` dimension (see the Summary). |
| **S2.11** | `number_format = "scientific"` × several `locale` values | ✅ **Covered** | CORE values: `"scientific"` with `format_length = ""`, all `CORE_LOCALES`, and `CORE_NUMBERS`. |
| **S2.12** | `exponent_style = "superscript"` × `number_format = "scientific"` | 🟡 **Missing: new dimension** | The generator's scientific rows use the `exponential` symbol (`E`) only. **Action**: add the `exponent_style` dimension (see the Summary). |
| **S2.13** | — | ⚪ **Out of scope** | No CLDR pattern uses `‰`; per-mille amounts are unit formatting. |
| **S2.14** | `input` = `Infinity`, `-Infinity`, `NaN` | 🟡 **Missing: `input`** | None of these is a CORE or extended number value. **Action**: see the Summary. |
| **S2.15a** | `locale = "ar_EG"` and `"ar"` × `input` ≥ 1000 | ✅ **Covered** | CORE values: `ar_EG` and `ar` in `CORE_LOCALES`; `1234565.0` in `CORE_NUMBERS`. |
| **S2.15b** | — | ⚪ **Out of scope** | Constraints on the data. |

### 2.4 Notes

* S2.10: the quoted text does not say how the approximately sign combines with a negative number, since both replace the same minus sign; [Approximate Number Formatting](../../../docs/ldml/tr35-numbers.md#Approximate_Number_Formatting) (L1878) prepends it to the minus sign (`~-5`). The Summary adds `"approximately"` rows for non-negative inputs here and for a negative input in Section 15.
* S2.12: the example “1.23 × 10<sup>4</sup>” has spaces around `×`, but no CLDR `superscriptingExponent` value has spaces (`en`: `×`), and the text does not say where the `10` comes from or how it is localized (for example with `arab` digits, which have no superscript characters). The expected values above follow the data: `1.234565×10⁶`.
* S2.14: [Special Values](../../../docs/ldml/tr35-numbers.md#special-values) (L777) says that NaN is shown without the prefixes and suffixes of the pattern; Section 10 checks the other styles.

---

## Section 3: Number Formats (`#Number_Formats`)

* **TR35 Specification Link**: [`tr35-numbers.md#Number_Formats`](../../../docs/ldml/tr35-numbers.md#Number_Formats) (UTS #35 Part 3, Section 2.4: *Number Formats*, with its subsections [`decimalFormats`](../../../docs/ldml/tr35-numbers.md#decimalformats), [`percentFormats`](../../../docs/ldml/tr35-numbers.md#percentformats), and [`scientificFormats`](../../../docs/ldml/tr35-numbers.md#scientificformats); L308–L374 at `c33251cf82`). The quote below has L310–L374. Compact number formats (L376 onward) are in Section 4.
* **Related specification text**: L645 onward ([Number Format Patterns](../../../docs/ldml/tr35-numbers.md#Number_Format_Patterns): the pattern syntax) and Section 1 of this document (the numbering systems that the `numberSystem` attribute refers to)

### 3.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> ```dtd
> <!ELEMENT decimalFormats (alias | (default*, decimalFormatLength*, special*)) >
> <!ELEMENT decimalFormatLength (alias | (default*, decimalFormat*, special*)) >
> <!ATTLIST decimalFormatLength type ( full | long | medium | short ) #IMPLIED >
> <!ELEMENT decimalFormat (alias | (pattern*, special*)) >
> ```
>
> (scientificFormats, percentFormats have the same structure)
>
> Number formats are used to define the rules for formatting numeric quantities using the pattern syntax described in _[Section 3: Number Format Patterns](../../../docs/ldml/tr35-numbers.md#Number_Format_Patterns)_.
>
> Different formats are provided for different contexts, as follows:
>
> #### decimalFormats
>
> > The normal locale specific way to write a base 10 number. Variations of the decimalFormat pattern are provided that allow compact number formatting.
>
> #### percentFormats
>
> > Pattern for use with percentage formatting
>
> #### scientificFormats
>
> > Pattern for use with scientific (exponent) formatting.
>
> Example:
>
> ```xml
> <decimalFormats numberSystem="latn">
>   <decimalFormatLength type="long">
>     <decimalFormat>
>       <pattern>#,##0.###</pattern>
>     </decimalFormat>
>   </decimalFormatLength>
> </decimalFormats>
>
> <scientificFormats numberSystem="latn">
>   <default type="long"/>
>   <scientificFormatLength type="long">
>     <scientificFormat>
>       <pattern>0.000###E+00</pattern>
>     </scientificFormat>
>   </scientificFormatLength>
>   <scientificFormatLength type="medium">
>     <scientificFormat>
>       <pattern>0.00##E+00</pattern>
>     </scientificFormat>
>   </scientificFormatLength>
> </scientificFormats>
>
> <percentFormats numberSystem="latn">
>   <percentFormatLength type="long">
>     <percentFormat>
>       <pattern>#,##0%</pattern>
>     </percentFormat>
>   </percentFormatLength>
> </percentFormats>
> ```
>
> ```dtd
> <!ATTLIST symbols numberSystem CDATA #IMPLIED >
> ```
>
> The `numberSystem` attribute is used to specify that the given number formatting pattern(s) are to be used when the given numbering system is active. By default, number formatting patterns without a specific `numberSystem` attribute are assumed to be used for the "latn" numbering system, which is western (ASCII) digits; however, number formatting patterns without a specific `numberSystem` attribute should not be used and will be deprecated in CLDR v48. Locales that specify a numbering system other than "latn" as the default should also specify number formatting patterns that are appropriate for use within the context of the given numbering system.
> For more information on numbering systems and their definitions, see _[Section 1: Numbering Systems](../../../docs/ldml/tr35-numbers.md#Numbering_Systems)_.

---

### 3.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** Number format patterns of the CORE locales, resolved for each locale's default numbering system (`root` makes the `arab` and `beng` decimal and scientific formats an alias of the `latn` ones).

| `number_format` | Element | Lengths with data | Patterns of the CORE locales |
| :--- | :--- | :--- | :--- |
| `"decimal"` | `decimalFormats` | no `type` (the non-compact pattern); `long` and `short` hold only compact patterns (Section 4) | `#,##0.###` (`bn`: `#,##,##0.###`) |
| `"percent"` | `percentFormats` | no `type` only | `#,##0%`; `de`, `ru`: `#,##0 %` / `"#,##0\u00A0%"`; `bn`: `#,##0%` for `beng` and `#,##,##0%` for `latn` |
| `"scientific"` | `scientificFormats` | no `type` only | `#E0` |

Scans of `common/main` (all locales): no locale has a `full` or `medium` length, a `<default>` element, or a `percentFormatLength` or `scientificFormatLength` with a `type`. Every pattern has a `numberSystem` attribute. Four locales have a pattern that differs between their numbering systems: `bn`, `ccp`, and `gu` (percent) and `te` (decimal); `bn` is the only CORE locale among them.

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S3.1** | DTD: `<!ATTLIST decimalFormatLength type ( full \| long \| medium \| short ) #IMPLIED >`, *"(scientificFormats, percentFormats have the same structure)"* | • **S3.1a**: each `number_format` with no length (`format_length = ""`)<br>• **S3.1b**: `number_format = "decimal"` × `format_length = "short"` and `"long"`<br>• **S3.1c**: the lengths `full` and `medium`, and `"percent"` or `"scientific"` with a length | • S3.1a, S3.1b: see S3.3–S3.5.<br>• S3.1c: no CLDR data. |
| **S3.2** | *"Number formats are used to define the rules for formatting numeric quantities using the pattern syntax described in Section 3: Number Format Patterns."* | — (the pattern syntax is checked in Sections 6 and 7) | — |
| **S3.3** | **`decimalFormats`** — *"The normal locale specific way to write a base 10 number. Variations of the decimalFormat pattern are provided that allow compact number formatting."* | • **S3.3a**: `number_format = "decimal"` × `format_length = ""` × several `locale` values (`"en"`, `"de"`, `"bn"`)<br>• **S3.3b**: `number_format = "decimal"` × `format_length = "short"` and `"long"` | • S3.3a: `en` 1234565.0 → `1,234,565`; `de` → `1.234.565`; `bn` → `১২,৩৪,৫৬৫`.<br>• S3.3b: `en` 1234565.0 → `1.2M` (short), `1.2 million` (long); see Section 4. |
| **S3.4** | **`percentFormats`** — *"Pattern for use with percentage formatting"* | • `number_format = "percent"` × several `locale` values (`"en"`, `"de"`) | `en` 1.2 → `120%`; `de` → `120 %` / `"120\u00A0%"`. |
| **S3.5** | **`scientificFormats`** — *"Pattern for use with scientific (exponent) formatting."* | • `number_format = "scientific"` × several `locale` values (`"en"`, `"de"`) | `en` 1234565.0 → `1.234565E6`; `de` → `1,234565E6`. |
| **S3.6** | The example: a `long` `decimalFormatLength` with `#,##0.###`, a `scientificFormats` with `<default type="long"/>` and `long` and `medium` lengths, and a `long` `percentFormatLength` | — | No CLDR data (see the notes). |
| **S3.7** | **`numberSystem`** — *"The `numberSystem` attribute is used to specify that the given number formatting pattern(s) are to be used when the given numbering system is active. By default, number formatting patterns without a specific `numberSystem` attribute are assumed to be used for the "latn" numbering system [...] Locales that specify a numbering system other than "latn" as the default should also specify number formatting patterns that are appropriate for use within the context of the given numbering system."* | • **S3.7a**: a `locale` whose pattern differs between two numbering systems (`"bn"`, percent) × `numbering_system = "latn"` (Section 1) and the default × `number_format = "percent"` × `input` with at least 6 integer digits after scaling (e.g. `1234565.0`)<br>• **S3.7b**: patterns without a `numberSystem` attribute<br>• **S3.7c**: a `locale` whose default is not `latn` (`"ar_EG"`) × each `number_format` | • S3.7a: `bn` 1234565.0 → `১২৩,৪৫৬,৫০০%` (`beng`: `#,##0%`) vs. `bn-u-nu-latn` → `12,34,56,500%` (`latn`: `#,##,##0%`).<br>• S3.7b: no CLDR data.<br>• S3.7c: `ar_EG` 1234565.0 → `١٬٢٣٤٬٥٦٥` (decimal), `١٫٢٣٤٥٦٥أس٦` (scientific); 1.2 → `١٢٠٪؜` / `"\u0661\u0662\u0660\u066A\u061C"` (percent). |

---

### 3.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S3.1a**, **S3.3a**, **S3.4**, **S3.5** | each `number_format` × `format_length = ""` × several `locale` values | ✅ **Covered** | CORE values: `"decimal"`, `"percent"`, and `"scientific"` with `format_length = ""`, all `CORE_LOCALES`, and `CORE_NUMBERS`. |
| **S3.1b**, **S3.3b** | `"decimal"` × `"short"`, `"long"` | ✅ **Covered** | CORE values: `"decimal"` with `"short"` and `"long"`. |
| **S3.1c**, **S3.6**, **S3.7b** | lengths and elements that CLDR does not use | ⚪ **Out of scope** | No CLDR data. |
| **S3.7a** | `locale = "bn"` × `numbering_system = "latn"` × `"percent"` × `1234565.0` | 🟡 **Missing: new dimension** | `numbering_system` is added in Section 1, but only with `"decimal"`, and the `bn` decimal pattern is the same for `beng` and `latn`. **Action**: add `numbering_system` rows for `"percent"` (see the Summary). |
| **S3.7c** | `locale = "ar_EG"` × each `number_format` | ✅ **Covered** | CORE values: `ar_EG` in `CORE_LOCALES`, with all five styles. |

### 3.4 Notes

* S3.6: in CLDR data a `decimalFormatLength` with a `type` holds only compact patterns, and percent and scientific formats have no lengths. The example's `long` decimal pattern `#,##0.###`, its scientific lengths, and its `<default>` element do not occur in the data.
* S3.7: the DTD line above the paragraph is `<!ATTLIST symbols numberSystem CDATA #IMPLIED >`, the attribute of `<symbols>` (Section 2), not of the number format elements. The paragraph also says that patterns without a `numberSystem` attribute *"will be deprecated in CLDR v48"*, while the pinned data is CLDR 49 and every pattern has the attribute.
* S3.7a: the other three locales with patterns that differ between numbering systems (`ccp`, `gu`, `te`) are not CORE locales; `gu` and `te` are extended locales.

---

## Section 4: Compact Number Formats (`#Compact_Number_Formats`)

* **TR35 Specification Link**: [`tr35-numbers.md#Compact_Number_Formats`](../../../docs/ldml/tr35-numbers.md#Compact_Number_Formats) (UTS #35 Part 3, Section 2.4.1: *Compact Number Formats*; L376–L491 at `c33251cf82`). The quote below has L378–L476, the patterns and the formatting steps. The `"0"` pattern (L479–L487) and the width guidance (L489–L490) are checked in Section 5.
* **Related specification text**: L742 ([`minimumGroupingDigits`](../../../docs/ldml/tr35-numbers.md#Examples_of_minimumGroupingDigits)), L811 onward ([Significant Digits](../../../docs/ldml/tr35-numbers.md#sigdig)), L840 onward ([Rounding](../../../docs/ldml/tr35-numbers.md#Rounding)), L1168 onward ([Language Plural Rules](../../../docs/ldml/tr35-numbers.md#Language_Plural_Rules): the plural categories of step 8), and Section 2 of the [currency document](../currency/tr35_currency_test_coverage.md) (step 4, `alt="alphaNextToNumber"`)

### 4.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> A pattern `type` attribute is used for _compact number formats_, such as the following:
>
> ```xml
> <decimalFormatLength type="long">
> 	<decimalFormat>
> 		<pattern type="1000" count="one">0 thousand</pattern>
> 		<pattern type="1000" count="other">0 thousand</pattern>
> 		<pattern type="10000" count="one">00 thousand</pattern>
> 		<pattern type="10000" count="other">00 thousand</pattern>
> 		<pattern type="100000" count="one">000 thousand</pattern>
> 		<pattern type="100000" count="other">000 thousand</pattern>
> 		<pattern type="1000000" count="one">0 million</pattern>
> 		<pattern type="1000000" count="other">0 million</pattern>
> 		<pattern type="10000000" count="one">00 million</pattern>
> 		<pattern type="10000000" count="other">00 million</pattern>
> …
> 	</decimalFormat>
> </decimalFormatLength>
> <decimalFormatLength type="short">
> 	<decimalFormat>
> 		<pattern type="1000" count="one">0K</pattern>
> 		<pattern type="1000" count="other">0K</pattern>
> 		<pattern type="10000" count="one">00K</pattern>
> 		<pattern type="10000" count="other">00K</pattern>
> 		<pattern type="100000" count="one">000K</pattern>
> 		<pattern type="100000" count="other">000K</pattern>
> 		<pattern type="1000000" count="one">0M</pattern>
> 		<pattern type="1000000" count="other">0M</pattern>
> 		<pattern type="10000000" count="one">00M</pattern>
> 		<pattern type="10000000" count="other">00M</pattern>
> …
> 	</decimalFormat>
> </decimalFormatLength>
> …
> <currencyFormatLength type="short">
>     <currencyFormat type="standard">
> 		<pattern type="1000" count="one">¤0K</pattern>
> 		<pattern type="1000" count="one" alt="alphaNextToNumber">¤ 0K</pattern>
> 		<pattern type="1000" count="other">¤0K</pattern>
> 		<pattern type="1000" count="other" alt="alphaNextToNumber">¤ 0K</pattern>
> 		<pattern type="10000" count="one">¤00K</pattern>
> 		<pattern type="10000" count="one" alt="alphaNextToNumber">¤ 00K</pattern>
> 		<pattern type="10000" count="other">¤00K</pattern>
> 		<pattern type="10000" count="other" alt="alphaNextToNumber">¤ 00K</pattern>
> 		<pattern type="100000" count="one">¤000K</pattern>
> 		<pattern type="100000" count="one" alt="alphaNextToNumber">¤ 000K</pattern>
> 		<pattern type="100000" count="other">¤000K</pattern>
> 		<pattern type="100000" count="other" alt="alphaNextToNumber">¤ 000K</pattern>
> 		<pattern type="1000000" count="one">¤0M</pattern>
> 		<pattern type="1000000" count="one" alt="alphaNextToNumber">¤ 0M</pattern>
> 		<pattern type="1000000" count="other">¤0M</pattern>
> 		<pattern type="1000000" count="other" alt="alphaNextToNumber">¤ 0M</pattern>
> 		<pattern type="10000000" count="one">¤00M</pattern>
> 		<pattern type="10000000" count="one" alt="alphaNextToNumber">¤ 00M</pattern>
> 		<pattern type="10000000" count="other">¤00M</pattern>
> 		<pattern type="10000000" count="other" alt="alphaNextToNumber">¤ 00M</pattern>        …
>     </currencyFormat>
> </currencyFormatLength>
> ```
>
> Formats can be supplied for numbers (as above) or for currencies or other units. They can also be used with ranges of numbers, resulting in formatting strings like “$10K” or “$3–7M”.
>
> To format a number N, use the following steps:
>
> Notes:
> - A _letter grapheme cluster_ is a grapheme cluster that starts with a letter and then 0 or more combining marks.
> For example, each of the following are are _letter grapheme clusters_: \<q>, \<q, _combining ring above_>, \<q, _combining ring above_, _acute accent_>.
> - All of the pattern elements with the same type must have the same number of zeros in the pattern element value.
> - The examples use N = 123456, the currency = CAD, and the currency symbol string = "$CA"
>
> 1. Let P be the pattern element with greatest type less than or equal to N, and any count value.
>     * P = `<pattern type="100000" count="**one**">¤000K</pattern>`
> 2. Let V be the pattern element value.
>     * V = "¤000K"
> 3. If the element value of P is "0", then use the corresponding non-compact number formatting instead, and skip the rest of these steps — but adjust the precision as described below.
>     * For example, instead of `currencyFormat` `<pattern type="10000" count="one">¤00K</pattern>`, use `<pattern>¤#,##0.00</pattern>`.
> 4. If P is a currency format, look at the currency symbol string, and the position of the currency symbol ¤ in the pattern element value.
> If ¤ is immediately to the left of a 0 and the currency string ends with a _letter grapheme cluster_ (eg, "$CA"),
> or to the right and the currency starts with a letter (eg, "CA$"),
> then switch to the `alt=alphaNextToNumber` pattern, if there is one.
>     * P = `<pattern type="100000" count="**one**" alt="alphaNextToNumber">¤ 000K</pattern>` // with the currency symbol "CA$"
>     * V = "¤ 000K"
> 5. Let Z be the number of 0 characters in V, minus 1.
>     * Z = 2
> 6. Let T be the numeric value of the `type` attribute value, after removing the final Z zeros.
>     * "100000" removing "00" = "1000"
>     * T  = 1000
> 7. Let N' be N / T
>     * N = 123.456
> 8. Determine the plural category of N, based on the numeric precision settings (the min/max number of significant or fraction digits), and switch  the value of V if necessary.
>     * In this case, the plural category of 123.456 in English with any precision is "other", so the
>     * P = `<pattern type="100000" count="**other**" alt="alphaNextToNumber">¤ 000K</pattern>`
>     * V = "¤ 000K"
>     * For the short compact formats, it doesn't make a difference for English, but may for other locales!
> 9. Let V' be the same as V, but replacing that sequence of zeros by "{0}".
>     * V' = "¤ {0}K"
> 10. Let F be N' formatted according to V' and the numeric precision settings.
>     * F = "$CA 123K"   // where the precision is min = max = 3 significant digits
>     * F = "$CA 123.4K" // where the precision is min = max = 1 fraction digit

---

### 4.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** Compact decimal patterns of the CORE locales for the types that the CORE inputs reach (1000 for −1230.05, 1000000 for 1234565.0). The short patterns of `de`, `ru`, `pt_PT`, `bn`, `ar`, and `ar_EG` have U+00A0 between the number and the abbreviation, for example `0 Mio'.'` / `"0\u00A0Mio'.'"`.

| Locale | `short` 1000 | `short` 1000000 | `long` 1000 | `long` 1000000 |
| :--- | :--- | :--- | :--- | :--- |
| `en` | `0K` | `0M` | `0 thousand` | `0 million` |
| `de`, `de_CH` | `0` | `0 Mio'.'` | `0 Tausend` | `0 Million` (one), `0 Millionen` (other) |
| `ru` | `0 тыс'.'` | `0 млн` | `0 тысяча` (one), `0 тысячи` (few, other), `0 тысяч` (many) | `0 миллион` (one), `0 миллиона` (few, other), `0 миллионов` (many) |
| `pt_PT` | `0 mil` | `0 M` | `0 mil` | `0 milhão` (one), `0 milhões` (other) |
| `ja` | `0` | `000万` (10000: `0万`) | as `short` | as `short` |
| `bn` | `0 হা` | `00 লা` (100000: `0 লা`) | `0 হাজার` | `00 লাখ` (100000: `0 লাখ`) |
| `ar`, `ar_EG` | `0 ألف` (few: `0 آلاف`) | `0 مليون` | `0 ألف` (few: `0 آلاف`) | `0 مليون` (few: `0 ملايين`) |

`ja` has no `long` patterns of its own; `root` makes `long` an alias of `short`. The `ar_EG` patterns are those of `ar` (`root` makes `arab` an alias of `latn`), formatted with `arab` digits and symbols.

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S4.1** | *"A pattern `type` attribute is used for _compact number formats_"*; *"Formats can be supplied for numbers (as above) or for currencies or other units. They can also be used with ranges of numbers [...]"* | • **S4.1a**: `number_format = "decimal"` × `format_length = "short"` and `"long"`<br>• **S4.1b**: currencies and units<br>• **S4.1c**: ranges | • S4.1a: see S4.3–S4.7.<br>• S4.1b: currency formats are in the currency document; units are outside this generator.<br>• S4.1c: see Section 15. |
| **S4.2** | Notes: *"A _letter grapheme cluster_ is a grapheme cluster that starts with a letter and then 0 or more combining marks."*; *"All of the pattern elements with the same type must have the same number of zeros in the pattern element value."* | — | The first note is used only by step 4 (currencies). The second is a constraint on the data. |
| **S4.3** | Step 1: *"Let P be the pattern element with greatest type less than or equal to N, and any count value."* | • **S4.3a**: `format_length = "short"`, `"long"` × `input` values that reach different types (`1234565.0`: 1000000; `-1230.05`: 1000)<br>• **S4.3b**: `input` equal to a type (e.g. `1000.0`, `1000000.0`)<br>• **S4.3c**: `input` below the smallest type (`0.0`, `1.2`, `0.00831765`) | • S4.3a: `en` short 1234565.0 → `1.2M`; −1230.05 → `-1.2K`.<br>• S4.3b: `en` short 1000.0 → `1K`; 1000000.0 → `1M`.<br>• S4.3c: no pattern applies; the number is formatted with the `"0"` pattern (Section 5): `en` short 1.2 → `1.2`. |
| **S4.4** | Steps 2–3: *"Let V be the pattern element value."*; *"If the element value of P is "0", then use the corresponding non-compact number formatting instead, and skip the rest of these steps — but adjust the precision as described below."* | • explicit `"0"` patterns (`"de"`, `"ja"` type 1000) | See Section 5. |
| **S4.5** | Step 4: *"If P is a currency format, look at the currency symbol string, and the position of the currency symbol ¤ in the pattern element value."* [...] | — | Currencies only; see Section 2 of the currency document. |
| **S4.6** | Steps 5–7: *"Let Z be the number of 0 characters in V, minus 1."*; *"Let T be the numeric value of the `type` attribute value, after removing the final Z zeros."*; *"Let N' be N / T"* | • **S4.6a**: Z = 0 (`"en"`, `"de"` × `1234565.0`)<br>• **S4.6b**: Z > 0, which for the CORE inputs needs a `locale` with patterns of 2 or more zeros at 1000 or 1000000 (`"ja"`: `000万`, Z = 2, T = 10000; `"bn"`: `00 লা`, Z = 1, T = 100000) | • S4.6a: `de` long 1234565.0 → N′ = 1.234565 → `1,2 Millionen`.<br>• S4.6b: `ja` short 1234565.0 → N′ = 123.4565 → `123万`; `bn` short → N′ = 12.34565 → `১২ লা` / `"\u09E7\u09E8\u00A0\u09B2\u09BE"`. |
| **S4.7** | Step 8: *"Determine the plural category of N, based on the numeric precision settings (the min/max number of significant or fraction digits), and switch the value of V if necessary."* [...] *"For the short compact formats, it doesn't make a difference for English, but may for other locales!"* | • **S4.7a**: `format_length = "long"` × a `locale` with different patterns per count × `input` in the `other` category (`1234565.0`)<br>• **S4.7b**: the same × `input` in another category: one (`1000000.0`), many (`5000000.0` in `"ru"`), few (`5000000.0` in `"ar"`)<br>• **S4.7c**: `format_length = "short"` × a `locale` with different short patterns per count (`"ar"` few) × `5000.0`<br>• **S4.7d**: an `input` whose category changes with the precision (`1040000.0`: `1` with the default precision, `1,0` with one fraction digit) | • S4.7a: `de` long 1234565.0 → `1,2 Millionen`; `ru` → `1,2 миллиона`.<br>• S4.7b: `de` long 1000000.0 → `1 Million`; `ru` 5000000.0 → `5 миллионов`; `ar` → `5 ملايين`.<br>• S4.7c: `ar` short 5000.0 → `5 آلاف` / `"5\u00A0\u0622\u0644\u0627\u0641"`.<br>• S4.7d: `de` long 1040000.0 → `1 Million` (one) by default; `1,0 Millionen` (other) with one fraction digit. |
| **S4.8** | Steps 9–10: *"Let V' be the same as V, but replacing that sequence of zeros by "{0}"."*; *"Let F be N' formatted according to V' and the numeric precision settings."*, with the examples min = max = 3 significant digits and min = max = 1 fraction digit | • **S4.8a**: the default precision × `"short"`, `"long"` × the CORE inputs (S4.3a)<br>• **S4.8b**: `precision` = 3 significant digits and 1 fraction digit (new dimension) × `"short"`, `"long"` × `1234565.0`, `-1230.05` | • S4.8a: see S4.3a and S4.6.<br>• S4.8b: `en` short 1234565.0 → `1.23M` (3 significant digits), `1.2M` (1 fraction digit); −1230.05 → `-1.23K`, `-1.2K`; `ja` short 1234565.0 → `123万`, `123.5万`. |
| **S4.9** | Steps 1 and 7 for a number N where the rounded N′ reaches the next type | • `input` that rounds up to a type (`999999.9`) | ❓ See the notes: `en` short 999999.9 → step 1 gives type 100000 and N′ = 999.9999. |

---

### 4.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S4.1a**, **S4.3a**, **S4.6a**, **S4.7a**, **S4.8a** | `"decimal"` × `"short"`, `"long"` × `1234565.0`, `-1230.05` × `en`, `de`, `ru` | ✅ **Covered** | CORE values: `"decimal"` with `"short"` and `"long"`, all `CORE_LOCALES`, and `CORE_NUMBERS`. |
| **S4.1b**, **S4.2**, **S4.5** | — | ⚪ **Out of scope** | Currencies and units, or a constraint on the data. |
| **S4.1c**, **S4.4** | — | — | Checked in Sections 5 (the `"0"` pattern) and 15 (number ranges). |
| **S4.3b** | `input` = `1000.0`, `1000000.0` | 🟡 **Missing: `input` in CORE** | Both are extended values (10³, 10⁶); `decimals_extended_numbers.tsv` has them with all `CORE_LOCALES` and all five styles. No action needed. |
| **S4.3c** | `input` below 1000 | ✅ **Covered** | CORE values: `0.0`, `1.2`, `0.00831765` with `"short"` and `"long"`. |
| **S4.6b** | `locale = "ja"`, `"bn"` × `1234565.0` | ✅ **Covered** | CORE values: `ja` and `bn` in `CORE_LOCALES`; `1234565.0` in `CORE_NUMBERS`. |
| **S4.7b**, **S4.7c** | `input` = `1000000.0`, `5000000.0`, `5000.0` × `"long"` / `"short"` | 🟡 **Missing: `input` in CORE** | Extended values (10⁶, 5 × 10⁶, 5 × 10³), in `decimals_extended_numbers.tsv` with all `CORE_LOCALES` and styles. No action needed. |
| **S4.7d** | `input = 1040000.0` × `"long"` × `de`, `ru`, `pt_PT` | 🟡 **Missing: `input`** | Not a CORE or extended value. **Action**: add it to the extended numbers, and to the `precision` rows (see the Summary). |
| **S4.8b** | `precision` × `"short"`, `"long"` | 🟡 **Missing: new dimension** | The generator uses the default precision of ICU's compact notation only. **Action**: add the `precision` dimension (see the Summary). |
| **S4.9** | `input = 999999.9` × `"short"`, `"long"` | 🟡 **Missing: `input` in CORE** | Extended value, in `decimals_extended_numbers.tsv` with all `CORE_LOCALES` and styles. The expected value is open (see the notes). |

### 4.4 Notes

* S4.3: step 1 compares the types with N, so a negative N has no pattern with a type less than or equal to it. The expected values above use the absolute value of N (`-1.2K`), as the examples elsewhere in the specification imply; the text does not say so.
* S4.7: the step says *"the plural category of N"*, but the example (*"the plural category of 123.456"*) and the step's reference to precision show that the category of the formatted N′ is meant. Step 7 also writes *"N = 123.456"* for N′.
* S4.8: the specification does not define a default precision for compact formats; it describes one only for the `"0"` pattern (*"typically to 2 or 3 digits"*, L483). The expected values for the default precision follow ICU's (2 significant digits below 100, otherwise integers), which matches the examples in the CLDR data and the TSV files. The example *"F = "$CA 123.4K" // where the precision is min = max = 1 fraction digit"* rounds 123.456 down; with any of the rounding modes that round to nearest, the result is `123.5K` (`en` short 123456 → `123.5K`).
* S4.9: after N′ = 999.9999 is rounded to `1000`, the steps give `1000K`, while ICU formats `1M` by choosing the type again. The specification does not say which is intended.
* S4.2: the text in the notes has a typo (*"are are"*), and the currency example of step 4 writes `CA$` for the symbol that the notes call `$CA`.

---

## Section 5: The Compact "0" Pattern (`#Compact_Number_Formats`)

* **TR35 Specification Link**: [`tr35-numbers.md#Compact_Number_Formats`](../../../docs/ldml/tr35-numbers.md#Compact_Number_Formats) (UTS #35 Part 3, Section 2.4.1: *Compact Number Formats*; L376–L491 at `c33251cf82`). The quote below has L479–L490, the end of the section: the `"0"` pattern and the width guidance. The formatting steps before it are in Section 4.
* **Related specification text**: L742 ([`minimumGroupingDigits`](../../../docs/ldml/tr35-numbers.md#Examples_of_minimumGroupingDigits), Section 8), L811 onward ([Significant Digits](../../../docs/ldml/tr35-numbers.md#sigdig)), and Section 2 of the [currency document](../currency/tr35_currency_test_coverage.md) (the `"0"` pattern of compact currency formats)

### 5.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> The default pattern for any type that is not supplied is the special value “0”, as in the following. The value “0” must be used when a child locale overrides a parent locale to drop the compact pattern for that type and use the default pattern.
>
>  `<pattern type="1" count="one">0</pattern>`
>
> If the value is precisely “0”, either explicit or defaulted, then the normal number format pattern for that sort of object is supplied — either `<decimalFormat>` or `<currencyFormat type="standard">` — with the normal formatting for the locale (such as the grouping separators). However, for the “0” case by default the significant digits are adjusted for consistency, typically to 2 or 3 digits, and the maximum fractional digits are set to 0 (for both currencies and plain decimal). Thus the output would be $12, not $12.01. APIs may, however, allow these default behaviors to be overridden.
>
> With the data above, N=12345 matches `<pattern type="10000" count="other">00 K</pattern>`. N is divided by 1000 (obtained from 10000 after removing "00" and restoring one "0"). The result is formatted according to the normal decimal pattern. With no fractional digits, that yields "12 K".
>
> Formatting 1200 in USD would result in “1.2 K $”, while 990 implicitly maps to the special value “0”, which maps to `<currencyFormat type="standard"><pattern>#,##0.00 ¤</pattern>`, and would result in simply “990 $”.
>
> The short non-currency format is designed for UI environments where space is at a premium, and should ideally result in a formatted string no more than about 6 em wide (with no fractional digits).
> The short currency format will include currency symbols, and should ideally be no more than 8 em in width.

---

### 5.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** No CLDR pattern has `type="1"`. The smallest `type` of the CORE locales is 1000, so the CORE inputs below 1000 (0.0, 1.2, 0.00831765) use the defaulted `"0"`. Explicit `"0"` patterns, and the parent patterns they override:

| Locale | `short` 1000 | `long` 1000 |
| :--- | :--- | :--- |
| `root` | `0K` | as `short` (alias) |
| `en` | `0K` | `0 thousand` |
| `de`, `de_CH` | `0` (also at 10000 and 100000) | `0 Tausend` |
| `ja` | `0` (10000: `0万`) | as `short` (`root` alias) |

The non-compact pattern of these locales is `#,##0.###` (`group`: `de` `.`, `de_CH` `'`, `ja` `,`), and their `minimumGroupingDigits` is 1.

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S5.1** | *"The default pattern for any type that is not supplied is the special value “0”, as in the following."*, with the example `<pattern type="1" count="one">0</pattern>` | • `format_length = "short"`, `"long"` × `input` below the smallest type (`0.0`, `1.2`, `0.00831765`) | `en` short 1.2 → `1.2`; 0.00831765 → `0.0083` (see S5.3b). |
| **S5.2** | *"The value “0” must be used when a child locale overrides a parent locale to drop the compact pattern for that type and use the default pattern."* | • a `locale` whose `"0"` overrides a parent pattern (`"de"`, `"de_CH"`, `"ja"` over `root` `0K`) × `format_length = "short"` × `input` that reaches type 1000 (`-1230.05`) | `de` short −1230.05 → `-1.230` (S5.3a), not `-1,2K`; `en` (no override) → `-1.2K`. |
| **S5.3** | *"If the value is precisely “0”, either explicit or defaulted, then the normal number format pattern for that sort of object is supplied — either `<decimalFormat>` or `<currencyFormat type="standard">` — with the normal formatting for the locale (such as the grouping separators). However, for the “0” case by default the significant digits are adjusted for consistency, typically to 2 or 3 digits, and the maximum fractional digits are set to 0 (for both currencies and plain decimal)."* | • **S5.3a**: an explicit `"0"` × `input` ≥ 1000 with fraction digits (`-1230.05`) × `"short"` (`"de"`, `"de_CH"`, `"ja"`) and `"long"` (`"ja"`)<br>• **S5.3b**: the defaulted `"0"` × `input` below 100 with fraction digits (`1.2`, `0.00831765`) | • S5.3a: `de` short −1230.05 → `-1.230` (3 significant digits) or `-1.200` (2), grouped and with no fraction digits; `de_CH` → `-1'230` / `-1'200`; `ja` short and long → `-1,230` / `-1,200`.<br>• S5.3b: ❓ `en` short 1.2 → `1.2` (2 significant digits) or `1` (maximum 0 fraction digits); 0.00831765 → `0.0083` or `0`. See the notes. |
| **S5.4** | *"APIs may, however, allow these default behaviors to be overridden."* | • `precision` (Section 4) × an explicit `"0"` × `-1230.05` | `de` short −1230.05 → `-1.230` (`"significant: 3"`), `-1.230,0` (`"fraction: 1"`). |
| **S5.5** | *"With the data above, N=12345 matches `<pattern type="10000" count="other">00 K</pattern>`. [...] The result is formatted according to the normal decimal pattern. With no fractional digits, that yields "12 K"."* | • a pattern with more than one `0` (Section 4, S4.6b) | `ja` short 1234565.0 → `123万` (S4.6b). |
| **S5.6** | The currency example: 1200 and 990 in USD, with a `"0"` pattern for 990 | — | Currencies; see Section 2 of the currency document. |
| **S5.7** | *"The short non-currency format is designed for UI environments where space is at a premium, and should ideally result in a formatted string no more than about 6 em wide (with no fractional digits)."*, and the 8 em guidance for currency formats | — | Guidance for the data; formatting tests don't measure width. |

---

### 5.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S5.1**, **S5.3b** | `"short"`, `"long"` × `input` below 1000 | ✅ **Covered** | CORE values: `0.0`, `1.2`, `0.00831765` with `"short"` and `"long"`. The expected value of S5.3b is open (see the notes). |
| **S5.2**, **S5.3a** | `locale = "de"`, `"de_CH"`, `"ja"` × `"short"`, `"long"` × `-1230.05` | ✅ **Covered** | CORE values: `de`, `de_CH`, and `ja` in `CORE_LOCALES`; `-1230.05` in `CORE_NUMBERS`. |
| **S5.4** | `precision` × an explicit `"0"` × `-1230.05` | 🟡 **Missing: new dimension** | **Action**: Section 4's `precision` rows, which combine `"short"` and `"long"` with all `CORE_LOCALES` and `-1230.05` (see the Summary). |
| **S5.5** | a pattern with more than one `0` | ✅ **Covered** | As S4.6b. |
| **S5.6**, **S5.7** | — | ⚪ **Out of scope** | Currencies, or guidance for the data. |

### 5.4 Notes

* S5.3: the text gives no exact precision (*"typically to 2 or 3 digits"*). For inputs of 100 or more, both settings give integers. Below 100, the significant digits conflict with the maximum of 0 fraction digits: 2 significant digits give `1.2` and `0.0083`, while 0 fraction digits give `1` and `0`. S4.3c's expected value (`en` short 1.2 → `1.2`) follows the significant digits.
* S5.5: *"the data above"* is the sample of L380 onward, whose `short` type 10000 pattern is `00K`, with no space, so the result there is `12K`.

---

## Section 6: Number Patterns (`#Number_Patterns`)

* **TR35 Specification Link**: [`tr35-numbers.md#Number_Patterns`](../../../docs/ldml/tr35-numbers.md#Number_Patterns) (UTS #35 Part 3, Section 3.1: *Number Patterns*, with the table [Number Pattern Examples](../../../docs/ldml/tr35-numbers.md#Number_Pattern_Examples); L647–L667 at `c33251cf82`). The quote below has L649–L667.
* **Related specification text**: L669 onward ([Special Pattern Characters](../../../docs/ldml/tr35-numbers.md#Special_Pattern_Characters), Section 7), L765 onward ([Formatting](../../../docs/ldml/tr35-numbers.md#Formatting): the integer and fraction digit counts), and Sections 7 and 12 of the [currency document](../currency/tr35_currency_test_coverage.md) (the `¤` placeholder and the currency fraction digits)

### 6.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> Number patterns affect how numbers are interpreted in a localized context. Here are some examples, based on the French locale. The "." shows where the decimal point should go. The "," shows where the thousands separator should go. A "0" indicates zero-padding: if the number is too short, a zero (in the locale's numeric set) will go there. A "#" indicates no padding: if the number is too short, nothing goes there. A "¤" shows where the currency sign will go. The following illustrates the effects of different patterns for the French locale, with the number "1234.567". Notice how the pattern characters ',' and '.' are replaced by the characters appropriate for the locale.
>
> ###### Table: <a name="Number_Pattern_Examples" href="../../../docs/ldml/tr35-numbers.md#Number_Pattern_Examples">Number Pattern Examples</a>
>
> | Pattern    | Currency | Text       |
> |------------|----------|------------|
> | #,##0.##   | _n/a_    | 1 234,57   |
> | #,##0.###  | _n/a_    | 1 234,567  |
> | ###0.##### | _n/a_    | 1234,567   |
> | ###0.0000# | _n/a_    | 1234,5670  |
> | 00000.0000 | _n/a_    | 01234,5670 |
> | #,##0.00 ¤ | EUR      | 1 234,57 € |
> |            | JPY      | 1 235 ¥JP  |
>
> The number of # placeholder characters before the decimal does not matter, since no limit is placed on the maximum number of digits. There should, however, be at least one zero someplace in the pattern. In currency formats, the number of digits after the decimal also does not matter, since the information in the supplemental data (see _[Supplemental Currency Data](../../../docs/ldml/tr35-numbers.md#Supplemental_Currency_Data))_ is used to override the number of decimal places — and the rounding — according to the currency that is being formatted. That can be seen in the above chart, with the difference between Yen and Euro formatting.
>
> To ensure correct layout, especially in currency patterns in which a variety of symbols may be used, number patterns may contain (invisible) bidirectional text format characters such as LRM, RLM, and ALM.
>
> _When parsing using a pattern, a lenient parse should be used; see [Lenient Parsing](../../../docs/ldml/tr35.md#Lenient_Parsing)._ As noted there, lenient parsing should ignore bidi format characters.

---

### 6.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** `fr` (an extended locale) has the decimal pattern `#,##0.###`, the `decimal` `,`, and the `group` U+202F NARROW NO-BREAK SPACE. No CLDR decimal, percent, or scientific pattern contains a bidi format character. Settings of the table's patterns:

| Pattern | Minimum integer digits | Fraction digits | Grouping |
| :--- | :--- | :--- | :--- |
| `#,##0.##` | 1 | 0–2 | yes |
| `#,##0.###` | 1 | 0–3 | yes (the decimal pattern of the CORE locales; `bn`: `#,##,##0.###`) |
| `###0.#####` | 1 | 0–5 | no |
| `###0.0000#` | 1 | 4–5 | no |
| `00000.0000` | 5 | 4 | no |

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S6.1** | *"The "." shows where the decimal point should go. The "," shows where the thousands separator should go."* [...] *"Notice how the pattern characters ',' and '.' are replaced by the characters appropriate for the locale."* | • the same pattern (`#,##0.###`) × `locale` values with different `decimal` and `group` (`"en"`, `"de"`, `"de_CH"`, `"ar_EG"`) × `-1230.05` | `en` −1230.05 → `-1,230.05`; `de` → `-1.230,05`; `de_CH` → `-1'230.05`; `ar_EG` → `؜-١٬٢٣٠٫٠٥` / `"\u061C-\u0661\u066C\u0662\u0663\u0660\u066B\u0660\u0665"`. |
| **S6.2** | *"A "0" indicates zero-padding: if the number is too short, a zero (in the locale's numeric set) will go there. A "#" indicates no padding: if the number is too short, nothing goes there."* | • **S6.2a**: one `0` before the decimal point (`#,##0.###`) × `input` below 1 (`0.00831765`) × `locale` values with different digits (`"en"`, `"ar_EG"`, `"bn"`)<br>• **S6.2b**: `#` × `input` with fewer digits than the pattern (`1.2`)<br>• **S6.2c**: more than one `0` | • S6.2a: `en` 0.00831765 → `0.008`; `ar_EG` → `٠٫٠٠٨` / `"\u0660\u066B\u0660\u0660\u0668"`; `bn` → `০.০০৮` / `"\u09E6.\u09E6\u09E6\u09EE"`.<br>• S6.2b: `en` 1.2 → `1.2`.<br>• S6.2c: see S6.4d and S6.4e. |
| **S6.3** | *"A "¤" shows where the currency sign will go."* | — | Currencies; see the currency document. |
| **S6.4** | The table [Number Pattern Examples](../../../docs/ldml/tr35-numbers.md#Number_Pattern_Examples) (`fr`, 1234.567) | • **S6.4a**: `#,##0.###` (the CORE decimal pattern) × several `locale` values<br>• **S6.4b**: `#,##0.##` (at most 2 fraction digits)<br>• **S6.4c**: `###0.#####` (no grouping)<br>• **S6.4d**: `###0.0000#` (4–5 fraction digits)<br>• **S6.4e**: `00000.0000` (at least 5 integer digits, no grouping)<br>• **S6.4f**: the currency rows | • S6.4a: see S6.1; `fr` −1230.05 → `-1 230,05` / `"-1\u202F230,05"`.<br>• S6.4b: the same rounding as S6.4a (`en` 0.00831765 → `0.008`), with at most 3 fraction digits instead of 2.<br>• S6.4c: `grouping = "off"`: `en` 1234565.0 → `1234565`; −1230.05 → `-1230.05`.<br>• S6.4d: `precision = "fraction: 4-5"`: `en` 1.2 → `1.2000`; 0.00831765 → `0.00832`; −1230.05 → `-1,230.0500`.<br>• S6.4e: `integer_width = "min: 5"` with `grouping = "off"`: `en` 1.2 → `00001.2`; −1230.05 → `-01230.05`; `ar_EG` 1.2 → `٠٠٠٠١٫٢` / `"\u0660\u0660\u0660\u0660\u0661\u066B\u0662"`.<br>• S6.4f: see S6.6. |
| **S6.5** | *"The number of # placeholder characters before the decimal does not matter, since no limit is placed on the maximum number of digits. There should, however, be at least one zero someplace in the pattern."* | • **S6.5a**: `input` with more integer digits than the pattern (`1234565.0` × `#,##0.###`)<br>• **S6.5b**: the requirement of a zero | • S6.5a: `en` 1234565.0 → `1,234,565`.<br>• S6.5b: a constraint on the data. |
| **S6.6** | *"In currency formats, the number of digits after the decimal also does not matter, since the information in the supplemental data [...] is used to override the number of decimal places — and the rounding — according to the currency that is being formatted."* | — | Currencies; see Section 12 of the currency document. |
| **S6.7** | *"To ensure correct layout, especially in currency patterns in which a variety of symbols may be used, number patterns may contain (invisible) bidirectional text format characters such as LRM, RLM, and ALM."* | • a pattern with a bidi format character | No CLDR decimal, percent, or scientific pattern has one. The bidi marks in the CORE results come from the symbols (`ar` `minusSign` `"\u200E-"`, Section 2). |
| **S6.8** | *"When parsing using a pattern, a lenient parse should be used"* [...] | — | Parsing. |

---

### 6.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S6.1**, **S6.2a**, **S6.2b**, **S6.4a**, **S6.4b**, **S6.5a** | the CORE decimal pattern × several `locale` values × `CORE_NUMBERS` | ✅ **Covered** | CORE values: `"decimal"` with `format_length = ""`, all `CORE_LOCALES`, and `CORE_NUMBERS`. `fr` is an extended locale (`decimals_modern_locales.tsv`). |
| **S6.2c**, **S6.4c**, **S6.4d**, **S6.4e** | `grouping = "off"`, `precision = "fraction: 4-5"`, `integer_width = "min: 5"` | 🟡 **Missing: new dimension** | No pattern of a CORE locale has these settings, and the generator does not set them through the API. **Action**: add the `grouping` and `integer_width` dimensions and the `precision` value `"fraction: 4-5"` (see the Summary). |
| **S6.3**, **S6.4f**, **S6.6** | — | ⚪ **Out of scope** | Currencies; see the currency document. |
| **S6.5b**, **S6.7** | — | ⚪ **Out of scope** | A constraint on the data, or no CLDR data. |
| **S6.8** | — | ⚪ **Out of scope** | Parsing. |

### 6.4 Notes

* S6.4: the table's `Text` column has U+0020 as the grouping separator, but the `fr` data has U+202F: `#,##0.###` × 1234.567 → `1 234,567` / `"1\u202F234,567"`.
* S6.4: each new Summary row changes one setting of the locale's pattern, except that the minimum of 5 integer digits is combined with no grouping, as in `00000.0000` and in the `01997` example of [Formatting](../../../docs/ldml/tr35-numbers.md#Formatting) (L770).

---

## Section 7: Special Pattern Characters (`#Special_Pattern_Characters`)

* **TR35 Specification Link**: [`tr35-numbers.md#Special_Pattern_Characters`](../../../docs/ldml/tr35-numbers.md#Special_Pattern_Characters) (UTS #35 Part 3, Section 3.2: *Special Pattern Characters*, with the tables [Number Pattern Character Definitions](../../../docs/ldml/tr35-numbers.md#Number_Pattern_Character_Definitions) and [Sample Patterns and Results](../../../docs/ldml/tr35-numbers.md#Sample_Patterns_and_Results); L669–L753 at `c33251cf82`). The quote below has L671–L732. The CLDR conventions and `minimumGroupingDigits` (L734–L753) are in Section 8.
* **Related specification text**: L755 onward ([Explicit Plus Signs](../../../docs/ldml/tr35-numbers.md#Explicit_Plus), Section 9), L781 onward ([Scientific Notation](../../../docs/ldml/tr35-numbers.md#sci)), L858 onward ([Quoting Rules](../../../docs/ldml/tr35-numbers.md#Quoting_Rules)), and Sections 7 and 8 of the [currency document](../currency/tr35_currency_test_coverage.md) (the `¤` row and the placement of the currency symbol)

### 7.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> Many characters in a pattern are taken literally; they are matched during parsing and output unchanged during formatting. Special characters, on the other hand, stand for other characters, strings, or classes of characters. For example, the '#' character is replaced by a localized digit for the chosen numberSystem. Often the replacement character is the same as the pattern character; in the U.S. locale, the ',' grouping character is replaced by ','. However, the replacement is still happening, and if the symbols are modified, the grouping character changes. Some special characters affect the behavior of the formatter by their presence; for example, if the percent character is seen, then the value is multiplied by 100 before being displayed.
>
> To insert a special character in a pattern as a literal, that is, without any special meaning, the character must be quoted. There are some exceptions to this which are noted below. The Localized Replacement column shows the replacement from _[Number Symbols](../../../docs/ldml/tr35-numbers.md#Number_Symbols)_ or the numberSystem's digits: _italic_ indicates a special function.
>
> Invalid sequences of special characters (such as “¤¤¤¤¤¤” in current CLDR) should be handled for formatting and parsing as described in [Handling Invalid Patterns](../../../docs/ldml/tr35.md#Invalid_Patterns).
>
> ###### Table: <a name="Number_Pattern_Character_Definitions" href="../../../docs/ldml/tr35-numbers.md#Number_Pattern_Character_Definitions">Number Pattern Character Definitions</a>
>
> | Symbol | Location | Localized Replacement | Meaning |
> | :-- | :-- | :-- | :-- |
> | 0 | Number | digit | Digit |
> | 1-9 | Number | digit | '1' through '9' indicate rounding. |
> | @ | Number | digit | Significant digit |
> | # | Number | digit, _nothing_ | Digit, omitting leading/trailing zeros |
> | . | Number | decimal, currencyDecimal | Decimal separator or monetary decimal separator |
> | - | Number | minusSign, plusSign, approximatelySign | Minus sign. **Warning:** the pattern '-'0.0 is not the same as the pattern -0.0. In the former case, the minus sign is a literal. In the latter case, it is a special symbol, which is replaced by the minusSymbol, and can also be replaced by the plusSymbol for a format like +12% as in [Explicit Plus Signs](../../../docs/ldml/tr35-numbers.md#Explicit_Plus). |
> | , | Number | group, currencyGroup | Grouping separator. May occur in both the integer part and the fractional part. The position determines the grouping. |
> | E | Number | exponential, superscriptingExponent | Separates mantissa and exponent in scientific notation. _Need not be quoted in prefix or suffix._ |
> | + | Exponent or Number (for explicit plus) | plusSign | Prefix positive exponents with localized plus sign. Used for explicit plus for numbers as well, as described in [Explicit Plus Signs](../../../docs/ldml/tr35-numbers.md#Explicit_Plus). _Need not be quoted in prefix or suffix._ |
> | % | Prefix or suffix | percentSign | Multiply by 100 and show as percentage |
> | ‰ (U+2030) | Prefix or suffix | perMille | Multiply by 1000 and show as per mille (aka “basis points”) |
> | ; | Subpattern boundary | _syntax_ | Separates positive and negative subpatterns. When there is no explicit negative subpattern, an implicit negative subpattern is formed from the positive pattern with a prefixed - (ASCII U+002D HYPHEN-MINUS). |
> | ¤ (U+00A4) | Prefix or suffix | _currency symbol/name from currency specified in API_ | Any sequence is replaced by the localized currency symbol for the currency being formatted, as in the table below. If present in a pattern, the monetary decimal separator and grouping separators (if available) are used instead of the numeric ones. If data is unavailable for a given sequence in a given locale, the display may fall back to ¤ or ¤¤. See also the formatting for currency display names, steps 2 and 4 in [Currencies](../../../docs/ldml/tr35-numbers.md#Currencies). <table><tr><th>No.</th><th>Replacement / Example</th></tr><tr><td rowspan="2">¤</td><td>Standard currency symbol</td></tr><tr><td>_C$12.00_</td></tr><tr><td rowspan="2">¤¤</td><td>ISO currency symbol (constant)</td></tr><tr><td>_CAD 12.00_</td></tr><tr><td rowspan="2">¤¤¤</td><td>Appropriate currency display name for the currency, based on the plural rules in effect for the locale</td></tr><tr><td>_5.00 Canadian dollars_</td></tr><tr><td rowspan="2" >¤¤¤¤¤</td><td>Narrow currency symbol. The same symbols may be used for multiple currencies. Thus the symbol may be ambiguous, and should only be where the context is clear.</td></tr><tr><td>_$12.00_</td></tr><tr><td>_others_</td><td>_Invalid in current CLDR. Reserved for future specification_</td></tr></table> |
> | * | Prefix or suffix boundary | _padding character specified in API_ | Pad escape, precedes pad character |
> | ' | Prefix or suffix | _syntax-only_ | Used to quote special characters in a prefix or suffix, for example, `"'#'#"` formats 123 to `"#123"`. To create a single quote itself, use two in a row: `"# o''clock"`. |
>
> A pattern contains a positive subpattern and may contain a negative subpattern, for example, "#,##0.00;(#,##0.00)". Each subpattern has a prefix, a numeric part, and a suffix. If there is no explicit negative subpattern, the implicit negative subpattern is the ASCII minus sign (-) prefixed to the positive subpattern. That is, "0.00" alone is equivalent to "0.00;-0.00". (The data in CLDR is normalized to remove an explicit negative subpattern where it would be identical to the implicit form.)
>
> Note that if a negative subpattern is used as-is: a minus sign is _not_ added, eg "0.00;0.00" ≠ "0.00;-0.00". Trailing semicolons are ignored, eg "0.00;" = "0.00". Whitespace is not ignored, including those around semicolons, so "0.00 ; -0.00" ≠ "0.00;-0.00".
>
> If there is an explicit negative subpattern, it serves only to specify the negative prefix and suffix; the number of digits, minimal digits, and other characteristics are ignored in the negative subpattern. That means that "#,##0.0#;(#)" has precisely the same result as "#,##0.0#;(#,##0.0#)". However in the CLDR data, the format is normalized so that the other characteristics are preserved, just for readability.
>
> > **Note:** The thousands separator and decimal separator in patterns are always ASCII ',' and '.'. They are substituted by the code with the correct local values according to other fields in CLDR. The same is true of the - (ASCII minus sign) and other special characters listed above.
>
> A currency decimal pattern normally contains a currency symbol placeholder (¤, ¤¤, ¤¤¤, or ¤¤¤¤¤). The currency symbol placeholder may occur before the first digit, after the last digit symbol, or where the decimal symbol would otherwise be placed (for formats such as "12€50", as in "12€50 pour une omelette").
>
> | Placement | Examples                                                                         |
> |-----------|----------------------------------------------------------------------------------|
> | Before    | "¤#,##0.00" "¤ #,##0.00" "¤-#,##0.00" "¤ -#,##0.00" "-¤#,##0.00" "-¤ #,##0.00" … |
> | After     | "#,##0.00¤" "#,##0.00 ¤" "#,##0.00-¤" "#,##0.00- ¤" "#,##0.00¤-" "#,##0.00 ¤-" … |
> | Decimal   | "#,##0¤00"                                                                       |
>
> Below is a sample of patterns, special characters, and results:
>
> ###### Table: <a name="Sample_Patterns_and_Results" href="../../../docs/ldml/tr35-numbers.md#Sample_Patterns_and_Results">Sample Patterns and Results</a>
>
> <table><tbody>
> <tr><th>explicit pattern:</th><td colspan="2">0.00;-0.00</td><td colspan="2">0.00;0.00-</td><td colspan="2">0.00+;0.00-</td></tr>
> <tr><th>decimalSign:</th><td colspan="2">,</td><td colspan="2">,</td><td colspan="2">,</td></tr>
> <tr><th>minusSign:</th><td colspan="2">∸</td><td colspan="2">∸</td><td colspan="2">∸</td></tr>
> <tr><th>plusSign:</th><td colspan="2">∔</td><td colspan="2">∔</td><td colspan="2">∔</td></tr>
> <tr><th>number:</th><td>3.1415</td><td>-3.1415</td><td>3.1415</td><td>-3.1415</td><td>3.1415</td><td>-3.1415</td></tr>
> <tr><th>formatted:</th><td>3,14</td><td>∸3,14</td><td>3,14</td><td>3,14∸</td><td>3,14∔</td><td>3,14∸</td></tr>
> </tbody></table>
>
> _In the above table, ∸ = U+2238 DOT MINUS and ∔ = U+2214 DOT PLUS are used for illustration._
>
> The prefixes, suffixes, and various symbols used for infinity, digits, thousands separators, decimal separators, and so on may be set to arbitrary values, and they will appear properly during formatting. _However, care must be taken that the symbols and strings do not conflict, or parsing will be unreliable._ For example, either the positive and negative prefixes or the suffixes must be distinct for any parser using this data to be able to distinguish positive from negative values. Another example is that the decimal separator and thousands separator should be distinct characters, or parsing will be impossible.
>
> The _grouping separator_ is a character that separates clusters of integer digits to make large numbers more legible. It is commonly used for thousands, but in some locales it separates ten-thousands. The _grouping size_ is the number of digits between the grouping separators, such as 3 for "100,000,000" or 4 for "1 0000 0000". There are actually two different grouping sizes: One used for the least significant integer digits, the _primary grouping size_, and one used for all others, the _secondary grouping size_. In most locales these are the same, but sometimes they are different. For example, if the primary grouping interval is 3, and the secondary is 2, then this corresponds to the pattern "#,##,##0", and the number 123456789 is formatted as "12,34,56,789". If a pattern contains multiple grouping separators, the interval between the last one and the end of the integer defines the primary grouping size, and the interval between the last two defines the secondary grouping size. All others are ignored, so "#,##,###,####" == "###,###,####" == "##,#,###,####".
>
> The grouping separator may also occur in the fractional part, such as in “#,##0.###,#”. This is most commonly done where the grouping separator character is a thin, non-breaking space (U+202F), such as “1.618 033 988 75”. See [physics.nist.gov/cuu/Units/checklist.html](https://physics.nist.gov/cuu/Units/checklist.html).

---

### 7.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** Special characters in the decimal (including compact), percent, and scientific patterns of all locales in `common/main`:

| Character | Use in CLDR patterns |
| :--- | :--- |
| `0`, `#`, `.`, `,` | the non-compact patterns (`#,##0.###`, `#,##0%`); compact patterns use only `0` |
| `%` | a suffix; a prefix in `tr` (`%#,##0`) and `eu` (`% #,##0` / `"%\u00A0#,##0"`) |
| `E` | `#E0`; unquoted in a suffix in the `hu` compact patterns (`0 E` / `"0\u00A0E"`) |
| `'` | quotes `.` in compact patterns: `de` `0 Mio'.'` / `"0\u00A0Mio'.'"`, `ru` `0 тыс'.'` / `"0\u00A0\u0442\u044B\u0441'.'"` |
| `;`, `-` | only the `blo` percent pattern `% #,#0;% -#,#0` / `"%\u00A0#,#0;%\u00A0-#,#0"` (not a CORE or extended locale) |
| `+` | only the `en_US_POSIX` scientific pattern `0.000000E+000` (not a CORE or extended locale) |
| `@`, `1`–`9`, `‰`, `*`, `''`, `,` after `.` | none |

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S7.1** | *"Many characters in a pattern are taken literally; they are matched during parsing and output unchanged during formatting. Special characters, on the other hand, stand for other characters, strings, or classes of characters."* [...] *"Some special characters affect the behavior of the formatter by their presence; for example, if the percent character is seen, then the value is multiplied by 100 before being displayed."* | • **S7.1a**: literal characters (compact suffixes: `format_length = "short"` × `1234565.0`)<br>• **S7.1b**: `#` and `0` replaced by the digits of the numbering system (`"bn"`)<br>• **S7.1c**: `%` × `number_format = "percent"` | • S7.1a: `en` short 1234565.0 → `1.2M`.<br>• S7.1b: `bn` 1234565.0 → `১২,৩৪,৫৬৫`.<br>• S7.1c: `en` 1.2 → `120%`. |
| **S7.2** | *"To insert a special character in a pattern as a literal, that is, without any special meaning, the character must be quoted. There are some exceptions to this which are noted below."* | • a quoted special character × `"short"` × `input` that reaches it (`"de"` × `1234565.0`; `"ru"` × `-1230.05`) | `de` short 1234565.0 → `1,2 Mio.` / `"1,2\u00A0Mio."`; `ru` short −1230.05 → `-1,2 тыс.` / `"-1,2\u00A0\u0442\u044B\u0441."`. |
| **S7.3** | *"Invalid sequences of special characters (such as “¤¤¤¤¤¤” in current CLDR) should be handled for formatting and parsing as described in Handling Invalid Patterns."* | — | No CLDR pattern is invalid. |
| **S7.4** | The rows `0`, `#`, `.`, `,`, `E`, `%`, and `'` of [Number Pattern Character Definitions](../../../docs/ldml/tr35-numbers.md#Number_Pattern_Character_Definitions) | • each character in a CORE pattern (`#,##0.###`, `#,##0%`, `#E0`, compact patterns) | See S7.1, S7.2, and Section 2 (`decimal`, `group`, `percentSign`, `exponential`). |
| **S7.5** | Row `-`: *"Minus sign."* [...] *"the pattern '-'0.0 is not the same as the pattern -0.0. In the former case, the minus sign is a literal. In the latter case, it is a special symbol, which is replaced by the minusSymbol, and can also be replaced by the plusSymbol for a format like +12% as in Explicit Plus Signs."* | • **S7.5a**: the implicit minus sign × negative `input` (Section 2, S2.8)<br>• **S7.5b**: the minus sign replaced by the plus sign (*"+12%"*): `number_format = "percent"` × `sign_display = "always"` × positive `input` (`1.2`) × several `locale` values (`"en"`, `"de"`, `"ar"`, `"ar_EG"`)<br>• **S7.5c**: a quoted `'-'` | • S7.5a: `en` −1230.05 → `-1,230.05`.<br>• S7.5b: `en` 1.2 → `+120%`; `de` → `+120 %` / `"+120\u00A0%"`; `ar` → `‎+120‎%‎` / `"\u200E+120\u200E%\u200E"`; `ar_EG` → `؜+١٢٠٪؜` / `"\u061C+\u0661\u0662\u0660\u066A\u061C"`.<br>• S7.5c: no CLDR pattern quotes `-`. |
| **S7.6** | Row `E`: *"Separates mantissa and exponent in scientific notation. Need not be quoted in prefix or suffix."* | • `E` in a suffix (`"hu"` short: `0 E` / `"0\u00A0E"`) × `input` that reaches it (`-1230.05`) | `hu` short −1230.05 → `-1,2 E` / `"-1,2\u00A0E"`. |
| **S7.7** | Row `%`: *"Multiply by 100 and show as percentage"*, with the location *"Prefix or suffix"* | • `%` as a suffix (CORE) and as a prefix (`"tr"`, `"eu"`) × `number_format = "percent"` | `en` 1.2 → `120%`; `tr` → `%120`; `eu` → `% 120` / `"%\u00A0120"`. |
| **S7.8** | Row `,`: *"May occur in both the integer part and the fractional part."*, and *"The grouping separator may also occur in the fractional part, such as in “#,##0.###,#”."* [...] | • a pattern with a grouping separator after the decimal point | No CLDR pattern has one. |
| **S7.9** | The rows `1-9`, `@`, `+`, `‰`, and `*`, and the examples of row `'` | — | No pattern of a CORE or extended locale has these characters or `''`. The settings of `@` and `1`–`9` are described in [Significant Digits](../../../docs/ldml/tr35-numbers.md#sigdig) and [Rounding](../../../docs/ldml/tr35-numbers.md#Rounding); `*` is [Padding](../../../docs/ldml/tr35-numbers.md#Padding). |
| **S7.10** | Row `;`: *"Separates positive and negative subpatterns. When there is no explicit negative subpattern, an implicit negative subpattern is formed from the positive pattern with a prefixed - (ASCII U+002D HYPHEN-MINUS)."*, and L697–L701: *"A pattern contains a positive subpattern and may contain a negative subpattern"* [...] | • **S7.10a**: the implicit negative subpattern × negative `input` × each pattern with a prefix or suffix<br>• **S7.10b**: an explicit negative subpattern; trailing semicolons, whitespace, and `"#,##0.0#;(#)"` | • S7.10a: `de` −1230.05 → `-1.230,05`; percent → `-123.005 %` / `"-123.005\u00A0%"`.<br>• S7.10b: no CORE or extended locale has an explicit negative subpattern. |
| **S7.11** | Row `¤` and L705–L711 (the currency symbol placeholders and their placement) | — | Currencies; see Sections 7 and 8 of the currency document. |
| **S7.12** | *"The thousands separator and decimal separator in patterns are always ASCII ',' and '.'. They are substituted by the code with the correct local values according to other fields in CLDR. The same is true of the - (ASCII minus sign) and other special characters listed above."* | • the same as S7.4 and S7.5a | See Section 6 (S6.1) and S7.5a. |
| **S7.13** | The table [Sample Patterns and Results](../../../docs/ldml/tr35-numbers.md#Sample_Patterns_and_Results) (`0.00;-0.00`, `0.00;0.00-`, `0.00+;0.00-`) | — | Explicit negative subpatterns and symbols (∸, ∔) that CLDR does not have. |
| **S7.14** | *"However, care must be taken that the symbols and strings do not conflict, or parsing will be unreliable."* [...] | — | Parsing. |
| **S7.15** | *"The grouping separator is a character that separates clusters of integer digits to make large numbers more legible."* [...] *"For example, if the primary grouping interval is 3, and the secondary is 2, then this corresponds to the pattern "#,##,##0", and the number 123456789 is formatted as "12,34,56,789"."* [...] | • the same as Section 2, S2.3 and S2.4 | See S2.3a and S2.4a (`bn` 1234565.0 → `১২,৩৪,৫৬৫`). |

---

### 7.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S7.1**, **S7.2**, **S7.4**, **S7.5a**, **S7.10a**, **S7.12**, **S7.15** | the special characters of the CORE patterns | ✅ **Covered** | CORE values: all five styles, all `CORE_LOCALES`, and `CORE_NUMBERS` (`de` and `ru` short reach the quoted `.`). |
| **S7.5b** | `sign_display = "always"` × `"percent"` | 🟡 **Missing: new dimension** | Section 2 adds `sign_display` rows for `"decimal"` only. **Action**: add `"always"` rows for `"percent"` (see the Summary). |
| **S7.6**, **S7.7** | `locale = "hu"` (`E` in a suffix), `"tr"` and `"eu"` (`%` as a prefix) | 🟡 **Missing: `locale` in CORE** | Extended locales, in `decimals_modern_locales.tsv` with all five styles and `CORE_NUMBERS`. No action needed. |
| **S7.3**, **S7.5c**, **S7.8**, **S7.9**, **S7.10b** | — | ⚪ **Out of scope** | No pattern of a CORE or extended locale has these characters or subpatterns. |
| **S7.11** | — | ⚪ **Out of scope** | Currencies; see the currency document. |
| **S7.13**, **S7.14** | — | ⚪ **Out of scope** | An illustration with symbols that CLDR does not have, or parsing. |

### 7.4 Notes

* S7.5, S7.13: the text says *"minusSymbol"* and *"plusSymbol"*, and the sample table *"decimalSign"*; the symbols are named `minusSign`, `plusSign`, and `decimal` ([Number Symbols](../../../docs/ldml/tr35-numbers.md#Number_Symbols)).
* S7.9: the only `+` is in the exponent of `en_US_POSIX` (*"Prefix positive exponents with localized plus sign"*); explicit plus signs for numbers are API settings (Section 9).

---

## Section 8: CLDR Pattern Conventions and `minimumGroupingDigits` (`#Examples_of_minimumGroupingDigits`)

* **TR35 Specification Link**: [`tr35-numbers.md#Examples_of_minimumGroupingDigits`](../../../docs/ldml/tr35-numbers.md#Examples_of_minimumGroupingDigits) (UTS #35 Part 3, Section 3.2: *Special Pattern Characters*, its last part; L734–L753 at `c33251cf82`). The quote below has L734–L753.
* **Related specification text**: L730 (grouping sizes; Section 7, S7.15) and Section 2 (S2.3 and S2.4: `group`)

### 8.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> For consistency in the CLDR data, the following conventions are observed:
>
> * All number patterns should be minimal: there should be no leading # marks except to specify the position of the grouping separators (for example, avoid  ##,##0.###).
> * All formats should have one 0 before the decimal point (for example, avoid #,###.##)
> * Decimal formats should have three hash marks in the fractional position (for example, #,##0.###).
> * Currency formats should have two zeros in the fractional position (for example, ¤ #,##0.00).
>     * The exact number of decimals is overridden with the decimal count in supplementary data or by API settings.
> * The only time two thousands separators need to be used is when the number of digits varies, such as for Hindi: #,##,##0.
> * The **minimumGroupingDigits** can be used to suppress groupings below a certain value. This is used for languages such as Polish, where one would only write the grouping separator for values above 9999. The minimumGroupingDigits contains the default for the locale.
>     * The attribute value is used by adding it to the grouping separator value. If the input number has fewer integer digits, the grouping separator is suppressed.
>     * ##### <a name="Examples_of_minimumGroupingDigits" href="../../../docs/ldml/tr35-numbers.md#Examples_of_minimumGroupingDigits">Examples of minimumGroupingDigits</a>
>
>         | minimum­GroupingDigits | Pattern Grouping | Input Number | Formatted |
>         |-----------------------:|-----------------:|-------------:|----------:|
>         |                      1 |                3 |         1000 |     1,000 |
>         |                      1 |                3 |        10000 |    10,000 |
>         |                      2 |                3 |         1000 |      1000 |
>         |                      2 |                3 |        10000 |    10,000 |
>         |                      1 |                4 |        10000 |    1,0000 |
>         |                      2 |                4 |        10000 |     10000 |

---

### 8.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** The resolved `minimumGroupingDigits` is 2 for `pt_PT` (CORE) and for the extended locales `be`, `bg`, `es` (not `es_419`, `es_MX`, or `es_US`), `et`, `hu`, `hy`, `it`, `ka`, `lv`, `pl`, `sl`, and `sq`. It is 1 for the other CORE and extended locales. Among the other locales, `ee` has 3. `pt_PT` and `ru` have the same `group` (U+00A0) and grouping size 3.

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S8.1** | The conventions: *"All number patterns should be minimal"* [...] *"All formats should have one 0 before the decimal point"* [...] *"Decimal formats should have three hash marks in the fractional position"* [...] *"The only time two thousands separators need to be used is when the number of digits varies, such as for Hindi: #,##,##0."* | — | Constraints on the data. The CORE decimal patterns follow them (`#,##0.###`; `bn`: `#,##,##0.###`). |
| **S8.2** | **`minimumGroupingDigits`** — *"can be used to suppress groupings below a certain value. This is used for languages such as Polish, where one would only write the grouping separator for values above 9999. The minimumGroupingDigits contains the default for the locale."*; *"The attribute value is used by adding it to the grouping separator value. If the input number has fewer integer digits, the grouping separator is suppressed."* | • a `locale` with `minimumGroupingDigits` 2 (`"pt_PT"`) and one with 1 and the same `group` (`"ru"`) × `input` with 4 integer digits (`-1230.05`) and with 7 (`1234565.0`) | `pt_PT` −1230.05 → `-1230,05` (4 integer digits, fewer than 2 + 3); 1234565.0 → `1 234 565` / `"1\u00A0234\u00A0565"`; `ru` −1230.05 → `-1 230,05` / `"-1\u00A0230,05"`. |
| **S8.3** | The rows of [Examples of minimumGroupingDigits](../../../docs/ldml/tr35-numbers.md#Examples_of_minimumGroupingDigits) with grouping size 3 | • **S8.3a**: `minimumGroupingDigits` 1 × `1000.0`, `10000.0` (`"en"`)<br>• **S8.3b**: `minimumGroupingDigits` 2 × `1000.0`, `10000.0` (`"pt_PT"`) | • S8.3a: `en` 1000.0 → `1,000`; 10000.0 → `10,000`.<br>• S8.3b: `pt_PT` 1000.0 → `1000`; 10000.0 → `10 000` / `"10\u00A0000"`. |
| **S8.4** | The rows with grouping size 4 | — | No CLDR pattern has a primary grouping size of 4 (Section 2, S2.3b). |

---

### 8.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S8.1** | — | ⚪ **Out of scope** | Constraints on the data. |
| **S8.2** | `locale = "pt_PT"`, `"ru"` × `-1230.05`, `1234565.0` | ✅ **Covered** | CORE values: `pt_PT` and `ru` in `CORE_LOCALES`; `-1230.05` and `1234565.0` in `CORE_NUMBERS`. |
| **S8.3** | `input = 1000.0`, `10000.0` | 🟡 **Missing: `input` in CORE** | Extended values (10³, 10⁴), in `decimals_extended_numbers.tsv` with all `CORE_LOCALES` and all five styles. No action needed. |
| **S8.4** | grouping size 4 | ⚪ **Out of scope** | No CLDR data. |

### 8.4 Notes

* S8.2: `pl`, the example of the text, is an extended locale with `minimumGroupingDigits` 2. In the text, *"values above 9999"* have at least 5 integer digits, that is, 2 plus the grouping size 3, so *"the grouping separator value"* means the grouping size.

---

## Section 9: Explicit Plus Signs (`#Explicit_Plus`)

* **TR35 Specification Link**: [`tr35-numbers.md#Explicit_Plus`](../../../docs/ldml/tr35-numbers.md#Explicit_Plus) (UTS #35 Part 3, Section 3.2.1: *Explicit Plus Signs*; L755–L763 at `c33251cf82`). The quote below has L757–L763.
* **Related specification text**: Section 2 (S2.9: `plusSign`) and Section 7 (S7.5: the `-` row; S7.13: the sample table)

### 9.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> An explicit "plus" format can be formed, so as to show a visible + sign when formatting a non-negative number. The displayed plus sign can be an ASCII plus or another character, such as ＋ U+FF0B FULLWIDTH PLUS SIGN or ➕ U+2795 HEAVY PLUS SIGN; it is taken from whatever is set for plusSign in _[Number Symbols](../../../docs/ldml/tr35-numbers.md#Number_Symbols)_.
>
> 1. Get the negative subpattern (explicit or implicit).
> 2. Replace any unquoted ASCII minus sign by an ASCII plus sign.
> 3. If there are any replacements, use that for the positive subpattern.
>
> For an example, see [Sample Patterns and Results](../../../docs/ldml/tr35-numbers.md#Sample_Patterns_and_Results).

---

### 9.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** `plusSign` is `+` in the CORE locales, except `ar` (`‎+` / `"\u200E+"`) and `ar_EG` (`؜+` / `"\u061C+"`). No locale has U+FF0B or U+2795; the other `plusSign` values are `+` with bidi marks. No pattern of a CORE or extended locale has an explicit negative subpattern (Section 7), so step 1 always gets the implicit one.

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S9.1** | *"An explicit "plus" format can be formed, so as to show a visible + sign when formatting a non-negative number. The displayed plus sign can be an ASCII plus or another character, such as ＋ U+FF0B FULLWIDTH PLUS SIGN or ➕ U+2795 HEAVY PLUS SIGN; it is taken from whatever is set for plusSign in Number Symbols."* | • **S9.1a**: `sign_display = "always"` × non-negative `input`, including zero (`0.0`, `1.2`) × `locale` values with different `plusSign` (`"en"`, `"ar"`, `"ar_EG"`)<br>• **S9.1b**: a `plusSign` other than an ASCII plus | • S9.1a: see S2.9; `en` 0.0 → `+0`.<br>• S9.1b: no CLDR data. |
| **S9.2** | Steps 1–3: *"Get the negative subpattern (explicit or implicit)."*; *"Replace any unquoted ASCII minus sign by an ASCII plus sign."*; *"If there are any replacements, use that for the positive subpattern."* | • **S9.2a**: the implicit negative subpattern × `sign_display = "always"` × patterns with and without a suffix: `"decimal"` (S2.9) and `"percent"` (S7.5b) × several `locale` values (`"de"`)<br>• **S9.2b**: an explicit negative subpattern, or one without a minus sign | • S9.2a: `de` 1.2 → `+1,2`; percent → `+120 %` / `"+120\u00A0%"`.<br>• S9.2b: no pattern of a CORE or extended locale has one. |
| **S9.3** | *"For an example, see Sample Patterns and Results."* | — | See S7.13. |

---

### 9.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S9.1a**, **S9.2a** | `sign_display = "always"` × `"decimal"`, `"percent"` × non-negative `input` | 🟡 **Missing: new dimension** | **Action**: Section 2's `sign_display` rows (`"decimal"`) and Section 7's (`"percent"`); see the Summary. |
| **S9.1b**, **S9.2b**, **S9.3** | — | ⚪ **Out of scope** | No CLDR data, or the sample table of Section 7. |

### 9.4 Notes

* S9.1: the text does not say whether negative zero (`-0.0`, an extended value) counts as a non-negative number.

---

## Section 10: Formatting and Special Values (`#Formatting`)

* **TR35 Specification Link**: [`tr35-numbers.md#Formatting`](../../../docs/ldml/tr35-numbers.md#Formatting) (UTS #35 Part 3, Section 3.3: *Formatting*, with its subsection [Special Values](../../../docs/ldml/tr35-numbers.md#special-values); L765–L779 at `c33251cf82`). The quote below has L767–L779.
* **Related specification text**: L781 onward ([Scientific Notation](../../../docs/ldml/tr35-numbers.md#sci), Section 11), L811 onward ([Significant Digits](../../../docs/ldml/tr35-numbers.md#sigdig), Section 12), L840 onward ([Rounding](../../../docs/ldml/tr35-numbers.md#Rounding), Section 13), and Section 2 (S2.14: `infinity` and `nan`)

### 10.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> Formatting is guided by several parameters, all of which can be specified either using a pattern or using an external API designed for number formatting. The following description applies to formats that do not use [scientific notation](../../../docs/ldml/tr35-numbers.md#sci) or [significant digits](../../../docs/ldml/tr35-numbers.md#sigdig).
>
> * If the number of actual integer digits exceeds the _maximum integer digits_, then only the least significant digits are shown. For example, 1997 is formatted as "97" if the maximum integer digits is set to 2.
> * If the number of actual integer digits is less than the _minimum integer digits_, then leading zeros are added. For example, 1997 is formatted as "01997" if the minimum integer digits is set to 5.
> * If the number of actual fraction digits exceeds the _maximum fraction digits_, then half-even rounding it performed to the maximum fraction digits. For example, 0.125 is formatted as "0.12" if the maximum fraction digits is 2. This behavior can be changed by specifying a rounding increment and a rounding mode.
> * If the number of actual fraction digits is less than the _minimum fraction digits_, then trailing zeros are added. For example, 0.125 is formatted as "0.1250" if the minimum fraction digits is set to 4.
> * Trailing fractional zeros are not displayed if they occur _j_ positions after the decimal, where _j_ is less than the maximum fraction digits. For example, 0.10004 is formatted as "0.1" if the maximum fraction digits is four or less.
>
> #### Special Values
>
> `NaN` is represented as a single character, typically `(U+FFFD)` . This character is determined by the localized number symbols. This is the only value for which the prefixes and suffixes are not used.
>
> Infinity is represented as a single character, typically ∞ `(U+221E)` , with the positive or negative prefixes and suffixes applied. The infinity character is determined by the localized number symbols.

---

### 10.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** Digit counts of the CORE patterns:

| `number_format` | Pattern | Integer digits | Fraction digits |
| :--- | :--- | :--- | :--- |
| `"decimal"` | `#,##0.###` (`bn`: `#,##,##0.###`) | at least 1, no maximum | 0–3 |
| `"percent"` | `#,##0%` (`de`, `ru`: `#,##0 %` / `"#,##0\u00A0%"`) | at least 1, no maximum | 0 |
| `"scientific"` | `#E0` | see Section 11 | see Section 11 |

`infinity` is `∞` (U+221E) in the CORE locales. `nan` is `NaN`, except `ar` and `ar_EG` (`ليس رقمًا` / `"\u0644\u064A\u0633\u00A0\u0631\u0642\u0645\u064B\u0627"`) and `ru` (`не число` / `"\u043D\u0435\u00A0\u0447\u0438\u0441\u043B\u043E"`).

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S10.1** | *"Formatting is guided by several parameters, all of which can be specified either using a pattern or using an external API designed for number formatting. The following description applies to formats that do not use scientific notation or significant digits."* | — (introduces S10.2–S10.6) | — |
| **S10.2** | *"If the number of actual integer digits exceeds the maximum integer digits, then only the least significant digits are shown. For example, 1997 is formatted as "97" if the maximum integer digits is set to 2."* | • `integer_width = "max: 2"` × `input` with more than 2 integer digits (`1234565.0`, `-1230.05`) × several `locale` values (`"en"`, `"ar_EG"`) | `en` 1234565.0 → `65`; −1230.05 → `-30.05`; `ar_EG` 1234565.0 → `٦٥` / `"\u0666\u0665"`. |
| **S10.3** | *"If the number of actual integer digits is less than the minimum integer digits, then leading zeros are added. For example, 1997 is formatted as "01997" if the minimum integer digits is set to 5."* | • `integer_width = "min: 5"` (Section 6) | See S6.4e; `en` 0.0 → `00000`. |
| **S10.4** | *"If the number of actual fraction digits exceeds the maximum fraction digits, then half-even rounding it performed to the maximum fraction digits. For example, 0.125 is formatted as "0.12" if the maximum fraction digits is 2. This behavior can be changed by specifying a rounding increment and a rounding mode."* | • **S10.4a**: `input` with more fraction digits than the maximum (`0.00831765` × `"decimal"`, `"percent"`)<br>• **S10.4b**: ties, where half-even rounding differs from half-up (`0.0005`, `0.0015`; percent: `0.005`, `0.015`, `0.125`, `0.135`)<br>• **S10.4c**: a rounding increment and a rounding mode | • S10.4a: `en` 0.00831765 → `0.008`; percent → `1%`.<br>• S10.4b: `en` 0.0005 → `0`; 0.0015 → `0.002`; percent 0.005 → `0%`; 0.015 → `2%`; 0.125 → `12%`; 0.135 → `14%`.<br>• S10.4c: see Section 13. |
| **S10.5** | *"If the number of actual fraction digits is less than the minimum fraction digits, then trailing zeros are added. For example, 0.125 is formatted as "0.1250" if the minimum fraction digits is set to 4."* | • `precision = "fraction: 4-5"` (Section 6) | See S6.4d; `en` 1.2 → `1.2000`. |
| **S10.6** | *"Trailing fractional zeros are not displayed if they occur j positions after the decimal, where j is less than the maximum fraction digits. For example, 0.10004 is formatted as "0.1" if the maximum fraction digits is four or less."* | • `input` with fewer fraction digits than the maximum (`1.2` × `#,##0.###`) | `en` 1.2 → `1.2`, not `1.200`. |
| **S10.7** | *"`NaN` is represented as a single character, typically `(U+FFFD)` . This character is determined by the localized number symbols. This is the only value for which the prefixes and suffixes are not used."* | • `input = NaN` × the styles with a prefix or suffix (`"percent"`, `"short"`, `"long"`) and `"scientific"` × `locale` values with different `nan` (`"en"`, `"ru"`, `"ar"`) | `en` percent → `NaN` (no `%`); `ru` percent → `не число` / `"\u043D\u0435\u00A0\u0447\u0438\u0441\u043B\u043E"`; `ar` short → `ليس رقمًا` / `"\u0644\u064A\u0633\u00A0\u0631\u0642\u0645\u064B\u0627"`. |
| **S10.8** | *"Infinity is represented as a single character, typically ∞ `(U+221E)` , with the positive or negative prefixes and suffixes applied. The infinity character is determined by the localized number symbols."* | • **S10.8a**: `input = Infinity`, `-Infinity` × `"percent"` × several `locale` values (`"en"`, `"de"`, `"ar_EG"`)<br>• **S10.8b**: × `"scientific"`<br>• **S10.8c**: × `"short"`, `"long"` | • S10.8a: `en` → `∞%`, `-∞%`; `de` → `∞ %` / `"\u221E\u00A0%"`; `ar_EG` → `∞٪؜` / `"\u221E\u066A\u061C"`.<br>• S10.8b: `en` → `∞`, `-∞` (`#E0` has no prefix or suffix).<br>• S10.8c: ❓ see the notes. |

---

### 10.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S10.1** | — | — | Introduction. |
| **S10.2** | `integer_width = "max: 2"` | 🟡 **Missing: new dimension** | No CLDR pattern has a maximum number of integer digits. **Action**: add the `integer_width` value `"max: 2"` (see the Summary). |
| **S10.3**, **S10.5** | `integer_width = "min: 5"`, `precision = "fraction: 4-5"` | 🟡 **Missing: new dimension** | **Action**: Section 6's rows (see the Summary). |
| **S10.4a**, **S10.6** | `input` with more or fewer fraction digits than the pattern | ✅ **Covered** | CORE values: `0.00831765` and `1.2` with `"decimal"` and `"percent"`. |
| **S10.4b** | ties | 🟡 **Missing: `input` in CORE** | Extended values (5 × 10⁻⁴, 1.5 × 10⁻³, 5 × 10⁻³, 1.5 × 10⁻², `0.125`, `0.135`), in `decimals_extended_numbers.tsv` with all `CORE_LOCALES` and all five styles. No action needed. |
| **S10.4c** | — | — | See Section 13. |
| **S10.7**, **S10.8** | `input = NaN`, `Infinity`, `-Infinity` × `"percent"`, `"scientific"`, `"short"`, `"long"` | 🟡 **Missing: `input`** | Section 2 adds the special values with `"decimal"` and `format_length = ""` only. **Action**: add them for the other four styles (see the Summary). The expected value of S10.8c is open. |

### 10.4 Notes

* S10.7: no CLDR `nan` value is a single character, and none is U+FFFD.
* S10.8c: step 1 of the compact formatting steps (Section 4) takes the pattern with the greatest type less than or equal to N. For infinity that is the largest type (`en` short: `000T`, 10¹⁴), which would give `∞T`; the text does not say whether infinity skips the compact patterns. For NaN no type compares as less than or equal, so the `"0"` pattern applies (Section 5) and the result is `NaN` (S10.7).
* S10.4: typo *"rounding it performed"* (*is performed*).

---

## Section 11: Scientific Notation (`#sci`)

* **TR35 Specification Link**: [`tr35-numbers.md#sci`](../../../docs/ldml/tr35-numbers.md#sci) (UTS #35 Part 3, Section 3.4: *Scientific Notation*; L781–L809 at `c33251cf82`). The quote below has L783–L809.
* **Related specification text**: L829 ([Significant Digits](../../../docs/ldml/tr35-numbers.md#sigdig), Section 12: *"Significant digits may be used together with exponential notation."*) and Section 2 (S2.11 and S2.12: `exponential`, `superscriptingExponent`)

### 11.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> Numbers in scientific notation are expressed as the product of a mantissa and a power of ten, for example, 1234 can be expressed as 1.234 x 10<sup>3</sup>. The mantissa is typically in the half-open interval [1.0, 10.0) or sometimes [0.0, 1.0), but it need not be. In a pattern, the exponent character immediately followed by one or more digit characters indicates scientific notation. Example: "0.###E0" formats the number 1234 as "1.234E3".
>
> * The number of digit characters after the exponent character gives the minimum exponent digit count. There is no maximum. Negative exponents are formatted using the localized minus sign, _not_ the prefix and suffix from the pattern. This allows patterns such as "0.###E0 m/s". To prefix positive exponents with a localized plus sign, specify '+' between the exponent and the digits: "0.###E+0" will produce formats "1E+1", "1E+0", "1E-1", and so on. (In localized patterns, use the localized plus sign rather than '+'.)
> * The minimum number of integer digits is achieved by adjusting the exponent. Example: 0.00123 formatted with "00.###E0" yields "12.3E-4". This only happens if there is no maximum number of integer digits. If there is a maximum, then the minimum number of integer digits is fixed at one.
> * The maximum number of integer digits, if present, specifies the exponent grouping. The most common use of this is to generate _engineering notation_, in which the exponent is a multiple of three, for example, "##0.###E0". The number 12345 is formatted using "##0.####E0" as "12.345E3".
> * When using scientific notation, the formatter controls the digit counts using logic for significant digits. The maximum number of significant digits comes from the mantissa portion of the pattern: the string of #, 0, and period (".") characters immediately preceding the E. To get the maximum number of significant digits, use the following algorithm:
>
>     1.  If the mantissa pattern contains a period:
>         1.  If the mantissa pattern contains at least one 0:
>             *   Return the number of 0s before the period added to the number of #s or 0s after the period
>         2.  Else:
>             *   Return 1 plus the number of #s after the period
>     2.  Else:
>         1.  If the mantissa pattern contains at least one 0:
>             *   Return the number of 0s.
>         2.  Else:
>             *   Return positive infinity.
>
>     Examples:
>
>     *   0.##E0 means a max of 3 significant digits.
>     *   #.##E0 also means a max of 3 significant digits.
>     *   #.0#E0 means a max of 2 significant digits.
>     *   0E0 means a max of 1 significant digit.
>     *   #E0 means infinite precision.
>     *   ###E0 means engineering notation with infinite precision.
> *   Exponential patterns may not contain grouping separators.

---

### 11.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** The scientific pattern of the CORE locales is `#E0`: one exponent digit, no `+`, and a mantissa with no `0` and no period, so by the algorithm the maximum number of significant digits is infinite. Other scientific patterns in `common/main`:

| Pattern | Locales |
| :--- | :--- |
| `[#E0]` | `gu`, `hi`, `hi_Latn`, `mr`, `pa` (extended); `mai`, `sa` |
| `#` | `lo`, `si` (extended) |
| `0.000000E+000` | `en_US_POSIX` |

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S11.1** | *"Numbers in scientific notation are expressed as the product of a mantissa and a power of ten, for example, 1234 can be expressed as 1.234 x 10<sup>3</sup>."* [...] *"In a pattern, the exponent character immediately followed by one or more digit characters indicates scientific notation."* | • **S11.1a**: `number_format = "scientific"` × `input` of different magnitudes (`CORE_NUMBERS`)<br>• **S11.1b**: a scientific pattern without an exponent character (`"si"`, `"lo"`: `#`) | • S11.1a: `en` 1234565.0 → `1.234565E6`; −1230.05 → `-1.23005E3`; 0.00831765 → `8.31765E-3`; 1.2 → `1.2E0`.<br>• S11.1b: ❓ see the notes. |
| **S11.2** | *"The number of digit characters after the exponent character gives the minimum exponent digit count. There is no maximum. Negative exponents are formatted using the localized minus sign, not the prefix and suffix from the pattern."* | • **S11.2a**: `#E0` × exponents of one digit (`CORE_NUMBERS`) and of two digits (`input` ≥ 10¹⁰)<br>• **S11.2b**: a negative exponent (`0.00831765`) × `locale` values with different `minusSign` (`"ar"`, `"ar_EG"`)<br>• **S11.2c**: a minimum of more than one exponent digit (`E00`) | • S11.2a: `en` 1234565.0 → `1.234565E6`; 1.5E12 → `1.5E12`.<br>• S11.2b: `ar` → `8.31765E‎-3` / `"8.31765E\u200E-3"`; `ar_EG` → `٨٫٣١٧٦٥أس؜-٣` / `"\u0668\u066B\u0663\u0661\u0667\u0666\u0665\u0623\u0633\u061C-\u0663"`.<br>• S11.2c: no pattern of a CORE or extended locale has it. |
| **S11.3** | *"This allows patterns such as "0.###E0 m/s". To prefix positive exponents with a localized plus sign, specify '+' between the exponent and the digits: "0.###E+0" will produce formats "1E+1", "1E+0", "1E-1", and so on."* | — | No pattern of a CORE or extended locale has a suffix after the exponent or `E+` (`en_US_POSIX` only). |
| **S11.4** | *"The minimum number of integer digits is achieved by adjusting the exponent. Example: 0.00123 formatted with "00.###E0" yields "12.3E-4"."* [...] | — | No CLDR scientific pattern has a minimum of more than one integer digit. |
| **S11.5** | *"The maximum number of integer digits, if present, specifies the exponent grouping. The most common use of this is to generate engineering notation, in which the exponent is a multiple of three, for example, "##0.###E0". The number 12345 is formatted using "##0.####E0" as "12.345E3"."* | — | No CLDR scientific pattern has a maximum number of integer digits. |
| **S11.6** | *"When using scientific notation, the formatter controls the digit counts using logic for significant digits. The maximum number of significant digits comes from the mantissa portion of the pattern: the string of #, 0, and period (".") characters immediately preceding the E."*, the algorithm, and its examples (*"#E0 means infinite precision."*) | • **S11.6a**: `#E0` × `input` with many significant digits (`1234565.0`: 7)<br>• **S11.6b**: a maximum number of significant digits × `"scientific"`: `precision = "significant: 3"`, `"significant: 2-4"` × `CORE_NUMBERS` × several `locale` values<br>• **S11.6c**: the other example patterns (`0.##E0`, `#.0#E0`, `0E0`, `###E0`) | • S11.6a: `en` 1234565.0 → `1.234565E6` (7 digits).<br>• S11.6b: `en` 1234565.0 → `1.23E6` (3) / `1.235E6` (2–4); 1.2 → `1.20E0` / `1.2E0`; 0.00831765 → `8.32E-3` / `8.318E-3`; `de` 1234565.0 → `1,23E6` / `1,235E6`.<br>• S11.6c: no CLDR data. |
| **S11.7** | *"Exponential patterns may not contain grouping separators."* | — | A constraint on the data. |

---

### 11.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S11.1a**, **S11.2a** (one digit), **S11.2b**, **S11.6a** | `"scientific"` × `CORE_NUMBERS` × `en`, `ar`, `ar_EG` | ✅ **Covered** | CORE values: `"scientific"` with all `CORE_LOCALES` and `CORE_NUMBERS`. |
| **S11.2a** (two digits) | `input` ≥ 10¹⁰ | 🟡 **Missing: `input` in CORE** | Extended values (10¹⁰ to 5 × 10¹²), in `decimals_extended_numbers.tsv` with all `CORE_LOCALES` and all five styles. No action needed. |
| **S11.1b** | `locale = "si"`, `"lo"` × `"scientific"` | 🟡 **Missing: `locale` in CORE** | Extended locales, in `decimals_modern_locales.tsv`. No action needed; the expected value is open (see the notes). |
| **S11.6b** | `precision` × `"scientific"` | 🟡 **Missing: new dimension** | Section 4 adds `precision` rows for compact formats only. **Action**: add `"significant: 3"` and `"significant: 2-4"` rows for `"scientific"` (see the Summary). |
| **S11.2c**, **S11.3**, **S11.4**, **S11.5**, **S11.6c**, **S11.7** | — | ⚪ **Out of scope** | No scientific pattern of a CORE or extended locale has these features, or a constraint on the data. |

### 11.4 Notes

* S11.1b: the scientific pattern `#` of `si` and `lo` has no exponent character, so by S11.1 it is not scientific notation (`si` 1234565.0 → `1234565`); it also has no `0`, which S6.5 asks for.
* S11.1: the `[#E0]` patterns have literal brackets: `hi` 1234565.0 → `[1.234565E6]`.
* S11.6b: `"significant: 3"` also sets a minimum of 3 significant digits, so 1.2 → `1.20E0`. The specification's examples set only a maximum: `0.##E0` (at most 3) gives `1.2E0`.

---

## Section 12: Significant Digits (`#sigdig`)

* **TR35 Specification Link**: [`tr35-numbers.md#sigdig`](../../../docs/ldml/tr35-numbers.md#sigdig) (UTS #35 Part 3, Section 3.5: *Significant Digits*, with the table [Significant Digits Examples](../../../docs/ldml/tr35-numbers.md#Significant_Digits_Examples); L811–L829 at `c33251cf82`). The quote below has L813–L829.
* **Related specification text**: L765 onward ([Formatting](../../../docs/ldml/tr35-numbers.md#Formatting), Section 10), Section 4 (S4.8b: `precision` with compact formats), and Section 11 (S11.6: scientific notation)

### 12.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> There are two ways of controlling how many digits are shown: (a) significant digits counts, or (b) integer and fraction digit counts. Integer and fraction digit counts are described above. When a formatter is using significant digits counts, it uses however many integer and fraction digits are required to display the specified number of significant digits. It may ignore min/max integer/fraction digits, or it may use them to the extent possible.
>
> ###### Table: <a name="Significant_Digits_Examples" href="../../../docs/ldml/tr35-numbers.md#Significant_Digits_Examples">Significant Digits Examples</a>
>
> | Pattern | Minimum significant digits | Maximum significant digits | Number | Output |
> | :-- | :-- | :-- | :-- | :-- |
> | `@@@` | 3 | 3 | 12345 | `12300` |
> | `@@@` | 3 | 3 | 0.12345 | `0.123` |
> | `@@##` | 2 | 4 | 3.14159 | `3.142` |
> | `@@##` | 2 | 4 | 1.23004 | `1.23` |
>
> * In order to enable significant digits formatting, use a pattern containing the `'@'` pattern character. In order to disable significant digits formatting, use a pattern that does not contain the `'@'` pattern character.
> * Significant digit counts may be expressed using patterns that specify a minimum and maximum number of significant digits. These are indicated by the `'@'` and `'#'` characters. The minimum number of significant digits is the number of `'@'` characters. The maximum number of significant digits is the number of `'@'` characters plus the number of `'#'` characters following on the right. For example, the pattern `"@@@"` indicates exactly 3 significant digits. The pattern `"@##"` indicates from 1 to 3 significant digits. Trailing zero digits to the right of the decimal separator are suppressed after the minimum number of significant digits have been shown. For example, the pattern `"@##"` formats the number 0.1203 as `"0.12"`.
> * Implementations may forbid the use of significant digits in combination with min/max integer/fraction digits. In such a case, if a pattern uses significant digits, it may not contain a decimal separator, nor the `'0'` pattern character. Patterns such as `"@00"` or `"@.###"` would be disallowed.
> * Any number of `'#'` characters may be prepended to the left of the leftmost `'@'` character. These have no effect on the minimum and maximum significant digits counts, but may be used to position grouping separators. For example, `"#,#@#"` indicates a minimum of one significant digit, a maximum of two significant digits, and a grouping size of three.
> * The number of significant digits has no effect on parsing.
> * Significant digits may be used together with exponential notation. Such patterns are equivalent to a normal exponential pattern with a minimum and maximum integer digit count of one, a minimum fraction digit count of `Minimum Significant Digits - 1`, and a maximum fraction digit count of `Maximum Significant Digits - 1`. For example, the pattern `"@@###E0"` is equivalent to `"0.0###E0"`.

---

### 12.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** No CLDR pattern contains `@` (Section 7), so significant digits come only from API settings. The `precision` values below correspond to patterns of the table:

| `precision` | Minimum significant digits | Maximum significant digits | As a pattern |
| :--- | :--- | :--- | :--- |
| `"significant: 3"` (Section 4) | 3 | 3 | `@@@` |
| `"significant: 2-4"` | 2 | 4 | `@@##` |

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S12.1** | *"There are two ways of controlling how many digits are shown: (a) significant digits counts, or (b) integer and fraction digit counts. Integer and fraction digit counts are described above. When a formatter is using significant digits counts, it uses however many integer and fraction digits are required to display the specified number of significant digits."* | • **S12.1a**: (b) integer and fraction digit counts (Section 10)<br>• **S12.1b**: (a) significant digits × `number_format = "decimal"`, `format_length = ""` | • S12.1a: see Section 10.<br>• S12.1b: see S12.2. |
| **S12.2** | The table [Significant Digits Examples](../../../docs/ldml/tr35-numbers.md#Significant_Digits_Examples) (`@@@` × 12345 → `12300`; `@@##` × 3.14159 → `3.142`) | • `precision = "significant: 3"`, `"significant: 2-4"` × `number_format = "decimal"`, `format_length = ""` × `CORE_NUMBERS` × several `locale` values | `en` 1234565.0 → `1,230,000` (3) / `1,235,000` (2–4); −1230.05 → `-1,230` (both); 1.2 → `1.20` / `1.2`; 0.00831765 → `0.00832` / `0.008318`; `de` 1234565.0 → `1.230.000` / `1.235.000`. |
| **S12.3** | *"In order to enable significant digits formatting, use a pattern containing the `'@'` pattern character."* [...] *"The minimum number of significant digits is the number of `'@'` characters. The maximum number of significant digits is the number of `'@'` characters plus the number of `'#'` characters following on the right."* | — | No CLDR pattern contains `@`. |
| **S12.4** | *"Trailing zero digits to the right of the decimal separator are suppressed after the minimum number of significant digits have been shown. For example, the pattern `"@##"` formats the number 0.1203 as `"0.12"`."* | • a minimum below the maximum (`"significant: 2-4"`) and an equal minimum and maximum (`"significant: 3"`) × `input` with fewer significant digits (`1.2`) | `en` 1.2 → `1.2` (2–4), `1.20` (3). |
| **S12.5** | *"It may ignore min/max integer/fraction digits, or it may use them to the extent possible."*; *"Implementations may forbid the use of significant digits in combination with min/max integer/fraction digits."* [...] | — | An implementation choice. The expected values above use the significant digits only. |
| **S12.6** | *"Any number of `'#'` characters may be prepended to the left of the leftmost `'@'` character. These have no effect on the minimum and maximum significant digits counts, but may be used to position grouping separators."* | • significant digits × the locale's grouping × `input` with 7 integer digits (`1234565.0`) × `locale` values with different `group` (`"de"`, `"ar_EG"`) | `de` 1234565.0 → `1.230.000` (3); `ar_EG` → `١٬٢٣٠٬٠٠٠` / `"\u0661\u066C\u0662\u0663\u0660\u066C\u0660\u0660\u0660"`. |
| **S12.7** | *"The number of significant digits has no effect on parsing."* | — | Parsing. |
| **S12.8** | *"Significant digits may be used together with exponential notation. Such patterns are equivalent to a normal exponential pattern with a minimum and maximum integer digit count of one, a minimum fraction digit count of `Minimum Significant Digits - 1`, and a maximum fraction digit count of `Maximum Significant Digits - 1`."* [...] | • `precision` × `"scientific"` (S11.6b) | `"significant: 3"` is equivalent to `0.00E0`: `en` 1234565.0 → `1.23E6` (S11.6b). |

---

### 12.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S12.1a** | integer and fraction digit counts | ✅ **Covered** | See Section 10. |
| **S12.1b**, **S12.2**, **S12.4**, **S12.6** | `precision = "significant: 3"`, `"significant: 2-4"` × `"decimal"`, `format_length = ""` | 🟡 **Missing: new dimension** | Section 4 adds `precision` rows for compact formats only. **Action**: add them for `"decimal"` with `format_length = ""` (see the Summary). |
| **S12.8** | `precision` × `"scientific"` | 🟡 **Missing: new dimension** | **Action**: Section 11's rows (see the Summary). |
| **S12.3**, **S12.5**, **S12.7** | — | ⚪ **Out of scope** | Pattern syntax that CLDR does not use, an implementation choice, or parsing. |

### 12.4 Notes

* S12.2: the table's outputs are not grouped (`12300`); with the locale's pattern they are (S12.6; `en` 12345 → `12,300`).
* The text does not say how many significant digits zero has, so the expected value of `0.0` × `"significant: 3"` is open (`0`, `0.0`, or `0.00`).

---

## Section 13: Rounding (`#Rounding`)

* **TR35 Specification Link**: [`tr35-numbers.md#Rounding`](../../../docs/ldml/tr35-numbers.md#Rounding) (UTS #35 Part 3, Section 3.7: *Rounding*; L840–L856 at `c33251cf82`). The quote below has L842–L856.
* **Related specification text**: L771 (half-even rounding to the maximum fraction digits; Section 10, S10.4) and Section 12 of the [currency document](../currency/tr35_currency_test_coverage.md) (currency rounding)

### 13.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> Patterns support rounding to a specific increment. For example, 1230 rounded to the nearest 50 is 1250. Mathematically, rounding to specific increments is performed by dividing by the increment, rounding to an integer, then multiplying by the increment. To take a more bizarre example, 1.234 rounded to the nearest 0.65 is 1.3, as follows:
>
> <table><tbody>
> <tr><th>Original:</th><td>1.234</td></tr>
> <tr><th>Divide by increment (0.65):</th><td>1.89846…</td></tr>
> <tr><th>Round:</th><td>2</td></tr>
> <tr><th>Multiply by increment (0.65):</th><td>1.3</td></tr>
> </tbody></table>
>
> To specify a rounding increment in a pattern, include the increment in the pattern itself. "#,#50" specifies a rounding increment of 50. "#,##0.05" specifies a rounding increment of 0.05.
>
> * Rounding only affects the string produced by formatting. It does not affect parsing or change any numerical values.
> * An implementation may allow the specification of a _rounding mode_ to determine how values are rounded. In the absence of such choices, the default is to round "half-even", as described in IEEE arithmetic. That is, it rounds towards the "nearest neighbor" unless both neighbors are equidistant, in which case, it rounds towards the even neighbor. Behaves as for round "half-up" if the digit to the left of the discarded fraction is odd; behaves as for round "half-down" if it's even. Note that this is the rounding mode that minimizes cumulative error when applied repeatedly over a sequence of calculations.
> * Some locales use rounding in their currency formats to reflect the smallest currency denomination.
> * In a pattern, digits '1' through '9' specify rounding, but otherwise behave identically to digit '0'.

---

### 13.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** No CLDR pattern contains a digit `1`–`9` (Section 7), and every `rounding` value in `<fractions>` is `0`, so rounding increments come only from API settings. The steps for the two increments of the text (50 and 0.65) with CORE inputs:

| Increment | Input | Divided by the increment | Rounded (half-even) | Result |
| :--- | :--- | :--- | :--- | :--- |
| 50 | 1234565.0 | 24691.3 | 24691 | 1234550 |
| 50 | −1230.05 | −24.601 | −25 | −1250 |
| 50 | 1.2 | 0.024 | 0 | 0 |
| 0.65 | 1.2 | 1.846… | 2 | 1.3 |
| 0.65 | −1230.05 | −1892.38… | −1892 | −1229.8 |
| 0.65 | 1234565.0 | 1899330.77… | 1899331 | 1234565.15 |

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S13.1** | *"Patterns support rounding to a specific increment. For example, 1230 rounded to the nearest 50 is 1250. Mathematically, rounding to specific increments is performed by dividing by the increment, rounding to an integer, then multiplying by the increment. To take a more bizarre example, 1.234 rounded to the nearest 0.65 is 1.3, as follows:"*, and the table | • `precision = "increment: 50"`, `"increment: 0.65"` × `number_format = "decimal"`, `format_length = ""` × `CORE_NUMBERS` × several `locale` values | `en` 1234565.0 → `1,234,550` (50) / `1,234,565.15` (0.65); −1230.05 → `-1,250` / `-1,229.8`; 1.2 → `0` / `1.3`; `de` −1230.05 → `-1.250` / `-1.229,8`. |
| **S13.2** | *"To specify a rounding increment in a pattern, include the increment in the pattern itself. "#,#50" specifies a rounding increment of 50. "#,##0.05" specifies a rounding increment of 0.05."* | — | No CLDR pattern has a rounding increment. |
| **S13.3** | *"Rounding only affects the string produced by formatting. It does not affect parsing or change any numerical values."* | — | Not visible in a formatted string. |
| **S13.4** | *"An implementation may allow the specification of a rounding mode to determine how values are rounded. In the absence of such choices, the default is to round "half-even", as described in IEEE arithmetic."* [...] | • **S13.4a**: half-even × ties (Section 10, S10.4b)<br>• **S13.4b**: other rounding modes | • S13.4a: see S10.4b (`en` percent 0.125 → `12%`; 0.135 → `14%`).<br>• S13.4b: optional (*"may allow"*). |
| **S13.5** | *"Some locales use rounding in their currency formats to reflect the smallest currency denomination."* | — | Currencies; see Section 12 of the currency document. |
| **S13.6** | *"In a pattern, digits '1' through '9' specify rounding, but otherwise behave identically to digit '0'."* | — | No CLDR pattern contains them. |

---

### 13.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S13.1** | `precision = "increment: 50"`, `"increment: 0.65"` | 🟡 **Missing: new dimension** | No CLDR pattern has an increment, and the generator does not set one. **Action**: add the `precision` values `"increment: 50"` and `"increment: 0.65"` (see the Summary). |
| **S13.4a** | ties | 🟡 **Missing: `input` in CORE** | See S10.4b. No action needed. |
| **S13.2**, **S13.6** | — | ⚪ **Out of scope** | Pattern syntax that CLDR does not use. |
| **S13.3**, **S13.4b** | — | ⚪ **Out of scope** | Not visible in formatted strings, or optional. |
| **S13.5** | — | ⚪ **Out of scope** | Currencies; see the currency document. |

### 13.4 Notes

* S13.1: the expected values apply the increment to the locale's pattern (`#,##0.###`), so 1.2 → `1.3`. A pattern with the increment, such as `#,##0.65`, would also set a minimum of 2 fraction digits and give `1.30`.

---

## Section 14: Miscellaneous Patterns (`#Miscellaneous_Patterns`)

* **TR35 Specification Link**: [`tr35-numbers.md#Miscellaneous_Patterns`](../../../docs/ldml/tr35-numbers.md#Miscellaneous_Patterns) (UTS #35 Part 3, Section 2.5: *Miscellaneous Patterns*, with the subsections [approximately](../../../docs/ldml/tr35-numbers.md#approximately), [atMost](../../../docs/ldml/tr35-numbers.md#atmost), [atLeast](../../../docs/ldml/tr35-numbers.md#atleast), and [range](../../../docs/ldml/tr35-numbers.md#range); L560–L594 at `c33251cf82`). The quote below has L562–L594.
* **Related specification text**: L1855 onward ([Number Range Formatting](../../../docs/ldml/tr35-numbers.md#Number_Range_Formatting), Section 15, which uses the `range` pattern) and Section 2 (S2.10: `approximatelySign`)

### 14.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> ```dtd
> <!ELEMENT miscPatterns (alias | (default*, pattern*, special*)) >
> <!ATTLIST miscPatterns numberSystem CDATA #IMPLIED >
> ```
>
> The miscPatterns supply additional patterns for special purposes. The currently defined values are:
>
> #### approximately
>
> > indicates an approximate number, such as: “\~99”. This pattern is not currently in use; see ICU-20163.
>
> #### atMost
>
> > indicates a number or lower, such as: “`≤`99” to indicate that there are 99 items or fewer.
>
> #### atLeast
>
> > indicates a number or higher, such as: “99+” to indicate that there are 99 items or more.
>
> #### range
>
> > indicates a range of numbers, such as: “99–103” to indicate that there are from 99 to 103 items.
>
> _For example:_
>
> ```xml
> <miscPatterns numberSystem="…">
>   <pattern type="approximately">~{0}</pattern>
>   <pattern type="atLeast">≥{0}</pattern>
>   <pattern type="atMost">≤{0}</pattern>
>   <pattern type="range">{0}–{1}</pattern>
> </miscPatterns>
> ```

---

### 14.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** Resolved `miscPatterns` of the default numbering system:

| Locale | `approximately` | `atLeast` | `atMost` | `range` |
| :--- | :--- | :--- | :--- | :--- |
| `root` | `~{0}` | `≥{0}` | `≤{0}` | `{0}–{1}` |
| `en`, `bn`, `de`, `de_CH` | `~{0}` (`de`, `de_CH`: `≈{0}`) | `{0}+` | `≤{0}` | `{0}–{1}` |
| `ar`, `ar_EG` | `~{0}` | `+{0}` | `≤{0}` | `{0}–{1}` |
| `ja` | `約 {0}` | `{0} 以上` | `{0} 以下` | `{0}～{1}` (U+FF5E) |
| `pt_PT` | `~{0}` | `+{0}` | `≤{0}` | `{0} - {1}` |
| `ru` | `≈{0}` | `≥{0}` | `≤{0}` | `{0}–{1}` |

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S14.1** | *"The miscPatterns supply additional patterns for special purposes."* | — (introduces S14.2–S14.5) | — |
| **S14.2** | **approximately** — *"indicates an approximate number, such as: “\~99”. This pattern is not currently in use; see ICU-20163."* | — | Not in use; approximate numbers use the `approximatelySign` (S2.10, Section 15). |
| **S14.3** | **atMost** — *"indicates a number or lower, such as: “`≤`99” to indicate that there are 99 items or fewer."* | • `misc_pattern = "atMost"` × `CORE_NUMBERS` × `locale` values with different patterns (`"en"`, `"ja"`) | `en` 1.2 → `≤1.2`; −1230.05 → `≤-1,230.05`; `ja` 1.2 → `1.2 以下`. |
| **S14.4** | **atLeast** — *"indicates a number or higher, such as: “99+” to indicate that there are 99 items or more."* | • `misc_pattern = "atLeast"` × `CORE_NUMBERS` × `locale` values with the sign before and after the number (`"en"`, `"ar"`, `"ar_EG"`, `"pt_PT"`, `"ru"`) | `en` 1234565.0 → `1,234,565+`; `ar` → `+1,234,565`; `ar_EG` → `+١٬٢٣٤٬٥٦٥`; `pt_PT` → `+1 234 565` / `"+1\u00A0234\u00A0565"`; `ru` → `≥1 234 565` / `"≥1\u00A0234\u00A0565"`. |
| **S14.5** | **range** — *"indicates a range of numbers, such as: “99–103” to indicate that there are from 99 to 103 items."* | • the `range` pattern | See Section 15. |
| **S14.6** | The example `miscPatterns` | — | The example's patterns are those of `root`. |

---

### 14.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S14.1**, **S14.6** | — | — | Introduction and example. |
| **S14.2** | — | ⚪ **Out of scope** | Not in use. |
| **S14.3**, **S14.4** | `misc_pattern = "atMost"`, `"atLeast"` | 🟡 **Missing: new dimension** | The generator does not use `miscPatterns`. **Action**: add the `misc_pattern` dimension (see the Summary). |
| **S14.5** | — | — | See Section 15. |

### 14.4 Notes

* S14.3, S14.4: the text does not say how the patterns combine with the rest of the formatting. The expected values substitute the formatted number for `{0}`; for a negative number, `ar` atLeast gives `+‎-1,230.05` / `"+\u200E-1,230.05"`.
* S14.6: the example's `atLeast` is `≥{0}` (`root`), while the text's example *"99+"* matches `en` (`{0}+`).

---

## Section 15: Number Range Formatting (`#Number_Range_Formatting`)

* **TR35 Specification Link**: [`tr35-numbers.md#Number_Range_Formatting`](../../../docs/ldml/tr35-numbers.md#Number_Range_Formatting) (UTS #35 Part 3: *Number Range Formatting*, with the subsections [Approximate Number Formatting](../../../docs/ldml/tr35-numbers.md#Approximate_Number_Formatting), [Collapsing Number Ranges](../../../docs/ldml/tr35-numbers.md#Collapsing_Number_Ranges), and [Range Pattern Processing](../../../docs/ldml/tr35-numbers.md#Range_Pattern_Processing); L1855–L1940 at `c33251cf82`). The quote below has L1857–L1940.
* **Related specification text**: L560 onward ([Miscellaneous Patterns](../../../docs/ldml/tr35-numbers.md#Miscellaneous_Patterns), Section 14: the `range` pattern), L1485 onward ([Plural Ranges](../../../docs/ldml/tr35-numbers.md#Plural_Ranges)), Section 2 (S2.10: `approximatelySign`), and Section 4 (compact formats)

### 15.1 Verbatim Specification Snippet (`docs/ldml/tr35-numbers.md`)

> Often ranges of numbers are presented to users, such as in “Length: 3.2–4.5 centimeters”. This means any length from 3.2 cm to 4.5 cm, inclusive.
>
> To format a number range, the following steps are taken:
>
> 1. Format the lower bound and the upper bound independently following the steps in [Number Format Patterns](../../../docs/ldml/tr35-numbers.md#Number_Format_Patterns), preserving semantic annotations\*.
> 1. If the resulting values are identical, stop evaluating these steps and, instead, perform the steps in [Approximate Number Formatting](../../../docs/ldml/tr35-numbers.md#Approximate_Number_Formatting).
>     1. Note: This behavior may be customized in order to, for example, print the range despite the endpoints being identical. However, a spec-compliant implementation must support approximate number formatting.
> 1. Perform the steps in [Collapsing Number Ranges](../../../docs/ldml/tr35-numbers.md#Collapsing_Number_Ranges), obtaining modified *lower* and *upper* values.
> 1. Obtain a number range pattern by following the steps in [Range Pattern Processing](../../../docs/ldml/tr35-numbers.md#Range_Pattern_Processing).
> 1. Substitute *lower* as `{0}` and *upper* as `{1}` into the range pattern from the previous step.
>
> \* Semantic annotations are discussed in [Collapsing Number Ranges](../../../docs/ldml/tr35-numbers.md#Collapsing_Number_Ranges).
>
> For plural rule selection of number ranges, see [Plural Ranges](../../../docs/ldml/tr35-numbers.md#Plural_Ranges).
>
> ### <a name="Approximate_Number_Formatting" href="../../../docs/ldml/tr35-numbers.md#Approximate_Number_Formatting">Approximate Number Formatting</a>
>
> *Approximate number formatting* refers to a specific format of numbers in which the value is understood to not be exact; for example, "\~5 minutes".
>
> To format an approximate number, follow the normal number formatting procedure in [Number Format Patterns](../../../docs/ldml/tr35-numbers.md#Number_Format_Patterns), but substitute the `approximatelySign` from [Number Symbols](../../../docs/ldml/tr35-numbers.md#Number_Symbols) in for the minus sign placeholder.
>
> If the number is negative, or if the formatting options request the sign to be displayed, *prepend* the `approximatelySign` to the plus or minus sign before substituting it into the pattern. For example, "\~-5" means "approximately negative five". This procedure may change in the future.
>
> ### <a name="Collapsing_Number_Ranges" href="../../../docs/ldml/tr35-numbers.md#Collapsing_Number_Ranges">Collapsing Number Ranges</a>
>
> *Collapsing* a number range refers to the process of removing duplicated information in the *lower* and *upper* values. For example, if the lower string is "3.2 centimeters" and the upper string is "4.5 centimeters", it is desirable to remove the extra "centimeters" token.
>
> This operation requires *semantic annotations* on the formatted value. The exact form of the semantic annotations is implementation-dependent. However, implementations may consider the following broad categories of tokens:
>
> 1. Numerical value, including decimal and grouping separators
> 1. Sign symbol
> 1. Scientific or compact notation
> 1. Unit of measurement
>
> For example, consider the string `-5.3M US dollars`. It may be annotated as follows:
>
> - `-` → sign symbol
> - `5.3` → numerical value
> - `M` → compact notation
> - `US dollars` → unit of measurement for the currency USD
>
> Two tokens are *semantically equivalent* if they have the same *semantic annotations*, even if they are not the exact same string. For example:
>
> 1. "centimeter" is semantically equivalent to "centimeters".
> 1. "K" (the thousands symbol in compact decimals) is NOT semantically equivalent to "K" (the measurement unit Kelvin).
>
> The above description describes the expected output. Internally, the implementation may determine the equivalent units of measurement by passing the codes back from the number formatters, allowing for a precise determination of "semantically equivalent".
>
> Two semantically equivalent tokens can be *collapsed* if they appear at the start of both values or the end of both values.
> However, the implementation may choose different levels of aggressiveness with regard to collapsing tokens.
> An API for displaying ranges should permit control over whether the tokens are collapsed or not, and the levels of aggressiveness.
> The currently recommended heuristic is:
>
> 1. Never collapse scientific or compact notation. This is to avoid producing ambiguous strings such as "3–5M" (could represent 3–5,000,000 or 3,000,000–5,000,000).
> 2. Only collapse if the tokens are more than one code point in length. This is to increase clarity of strings such as "$3–$5".
> 3. To perform the collapse, remove the token that is closest to the range separator.
> That is, for a prefix element, remove from the end value of the range, and for a suffix element remove it from the start value of the range:
>     * USD 2 – USD 5 ⇒ USD 2 – 5
>     * 2M EUR – 5M EUR ⇒  2M – 5M EUR
>     * 2 km – 5 km ⇒ 2 – 5 km
>     * 2M ft – 5M ft ⇒ 2M – 5M ft
> 4. When the tokens can have distinct plural forms, modify the remaining token so that it has the correct plural form. That is, use [Plural Ranges](../../../docs/ldml/tr35-numbers.md#Plural_Ranges) to calculate the correct plural category for the range, and pick the variant of that the remaining token corresponding to that plural form.
>
> In bidi contexts, the data is built so that rule #3 works **visually**.
> For example, if a range from 2 km to 5 km would be presented visually as "_mk 5 – mk 2_", the collapsed form would be "_mk 5 – 2_".
> (The _mk_ is a stand-in for the native representation.)
> This requires consistent visually reordering among the elements: the range, the prefixes and the suffixes.
> Thus a prefix value will be reordered to be visually a suffix value, and the order of the range will be visually reversed.
>
> ### <a name="Range_Pattern_Processing" href="../../../docs/ldml/tr35-numbers.md#Range_Pattern_Processing">Range Pattern Processing</a>
>
> To obtain a number range pattern, the following steps are taken:
>
> 1. Load the range pattern found in [Miscellaneous Patterns](../../../docs/ldml/tr35-numbers.md#Miscellaneous_Patterns).
> 1. Optionally add spacing to the range pattern.
>
> To determine whether to add spacing, the currently recommended heuristic is:
>
> 1. If the *lower* string ends with a character other than a digit, or if the *upper* string begins with a character other than a digit.
> 2. If the range pattern does not contain a character having the `White_Space` binary Unicode property after the `{0}` or before the `{1}` placeholders.
>
> These heuristics may be refined in the future.
>
> To add spacing, insert a non-breaking space (U+00A0) at the positions in item 2 above.

---

### 15.2 Sentence-by-Sentence `(Dimension / Value)` Coverage Breakdown

**CLDR data.** The `range` patterns (Section 14), and the lengths in code points of the tokens that the collapsing heuristic compares:

| Locale | `range` | `minusSign` | Percent suffix |
| :--- | :--- | :--- | :--- |
| `en`, `bn`, `de_CH` | `{0}–{1}` | `-` (1) | `%` (1) |
| `ja` | `{0}～{1}` | `-` (1) | `%` (1) |
| `de`, `ru` | `{0}–{1}` | `-` (1) | ` %` / `"\u00A0%"` (2) |
| `pt_PT` | `{0} - {1}` | `-` (1) | `%` (1) |
| `ar` | `{0}–{1}` | `‎-` / `"\u200E-"` (2) | `‎%‎` / `"\u200E%\u200E"` (3) |
| `ar_EG` | `{0}–{1}` | `؜-` / `"\u061C-"` (2) | `٪؜` / `"\u066A\u061C"` (2) |

The breakdown uses four pairs of `input` and `upper_input`: P1 = (1234565.0, 5000000.0), P2 = (−1230.05, −1.2), P3 = (1.2, 1.2), and P4 = (1234565.0, 1234567.0).

| # | Verbatim Sentence / Normative Clause | Required `(Dimension = Value)` Combinations to Cover Clause | CLDR Data Evidence & Expected Behavior |
| :---: | :--- | :--- | :--- |
| **S15.1** | Step 1: *"Format the lower bound and the upper bound independently following the steps in Number Format Patterns, preserving semantic annotations"*, and step 5 (substitute the two values into the range pattern) | • `upper_input` × `number_format = "decimal"`, `format_length = ""` × P1 × `locale` values with different `range` patterns (`"en"`, `"ja"`, `"pt_PT"`) | `en` P1 → `1,234,565–5,000,000`; `ja` → `1,234,565～5,000,000`; `pt_PT` → `1 234 565 - 5 000 000` / `"1\u00A0234\u00A0565 - 5\u00A0000\u00A0000"`. |
| **S15.2** | Step 2: *"If the resulting values are identical, stop evaluating these steps and, instead, perform the steps in Approximate Number Formatting."*, and its note | • **S15.2a**: equal inputs (P3) × several `locale` values (`"en"`, `"de"`, `"ja"`) and `"percent"`<br>• **S15.2b**: different inputs with identical formatted values (P4) × `"short"`, `"long"`, and the same pair × `"decimal"`, where the values differ<br>• **S15.2c**: the customization of the note | • S15.2a: `en` P3 → `~1.2`; `de` → `≈1,2`; `ja` → `約1.2`; `en` percent → `~120%`.<br>• S15.2b: `en` short P4 → `~1.2M`; long → `~1.2 million`; decimal → `1,234,565–1,234,567`.<br>• S15.2c: optional (*"may be customized"*). |
| **S15.3** | *"To format an approximate number, follow the normal number formatting procedure in Number Format Patterns, but substitute the `approximatelySign` from Number Symbols in for the minus sign placeholder."*; *"If the number is negative, or if the formatting options request the sign to be displayed, [...] the `approximatelySign` to the plus or minus sign before substituting it into the pattern. For example, "\~-5" means "approximately negative five"."* | • **S15.3a**: `sign_display = "approximately"` × non-negative `input` (S2.10)<br>• **S15.3b**: `"approximately"` × negative `input` (`-1230.05`)<br>• **S15.3c**: `sign_display = "approximately, always"` × positive `input` (`1.2`) | • S15.3a: see S2.10.<br>• S15.3b: `en` −1230.05 → `~-1,230.05`; `de` → `≈-1.230,05`; `ja` → `約-1,230.05`; `ar` → `~‎-1,230.05` / `"~\u200E-1,230.05"`.<br>• S15.3c: `en` 1.2 → `~+1.2`; `de` → `≈+1,2`; `ar` → `~‎+1.2` / `"~\u200E+1.2"`. |
| **S15.4** | Rule 1: *"Never collapse scientific or compact notation. This is to avoid producing ambiguous strings such as "3–5M" (could represent 3–5,000,000 or 3,000,000–5,000,000)."* | • `upper_input` × `"short"`, `"long"`, `"scientific"` × a pair whose values have the same compact or exponent token (P1: `M`; P4: `E6`) | `en` short P1 → `1.2M – 5M` / `"1.2M\u00A0–\u00A05M"`; long → `1.2 million – 5 million` / `"1.2 million\u00A0–\u00A05 million"`; `de` short → `1,2 Mio. – 5 Mio.` / `"1,2\u00A0Mio.\u00A0–\u00A05\u00A0Mio."`; `en` scientific P4 → `1.234565E6–1.234567E6`. |
| **S15.5** | Rule 2: *"Only collapse if the tokens are more than one code point in length."* | • `upper_input` × tokens of one code point: `%` (`"en"` × `"percent"` × P1) and `-` (`"en"` × P2) | `en` percent P1 → `123,456,500% – 500,000,000%` / `"123,456,500%\u00A0–\u00A0500,000,000%"`; P2 → `-1,230.05 – -1.2` / `"-1,230.05\u00A0–\u00A0-1.2"`. |
| **S15.6** | Rule 3: *"To perform the collapse, remove the token that is closest to the range separator."*, and its examples | • **S15.6a**: a suffix of more than one code point (`"de"` × `"percent"` × P1)<br>• **S15.6b**: a prefix of more than one code point (`"ar"` × P2) | • S15.6a: `de` percent P1 → `123.456.500–500.000.000 %` / `"123.456.500–500.000.000\u00A0%"`.<br>• S15.6b: ❓ `ar` P2 → `‎-1,230.05–1.2` / `"\u200E-1,230.05–1.2"`; see the notes. |
| **S15.7** | Rule 4: *"When the tokens can have distinct plural forms, modify the remaining token so that it has the correct plural form."* [...], and the paragraph on bidi contexts (L1920–L1924) | — | Plural forms apply to units, outside this generator (compact tokens are never collapsed); the bidi paragraph describes the visual order. |
| **S15.8** | *"The exact form of the semantic annotations is implementation-dependent."*; *"However, the implementation may choose different levels of aggressiveness with regard to collapsing tokens."* [...] | — | Implementation choices. The expected values above follow the recommended heuristic. |
| **S15.9** | Range pattern processing: *"Load the range pattern found in Miscellaneous Patterns."*; *"Optionally add spacing to the range pattern."*; the spacing heuristic (L1933–L1938); *"To add spacing, insert a non-breaking space (U+00A0) at the positions in item 2 above."* | • **S15.9a**: values that end and begin with a digit (no spacing)<br>• **S15.9b**: a value that ends or begins with another character (spacing)<br>• **S15.9c**: a `range` pattern with white space (`"pt_PT"`) × P2 | • S15.9a: see S15.1 (`en` P1 → `1,234,565–5,000,000`).<br>• S15.9b: see S15.4 and S15.5.<br>• S15.9c: `pt_PT` P2 → `-1230,05 - -1,2` (no U+00A0 added). |

---

### 15.3 Comparison Against `GenerateDecimalFormatTestData.java`

| Clause | Required `(Dimension = Value)` Combination | Status | Generator Evidence / Action Required |
| :---: | :--- | :---: | :--- |
| **S15.1**, **S15.2a**, **S15.2b**, **S15.4**, **S15.5**, **S15.6**, **S15.9** | `upper_input` × the five styles × P1–P4 | 🟡 **Missing: new dimension** | The generator formats single numbers only. **Action**: add the `upper_input` dimension (see the Summary). |
| **S15.3a** | `sign_display = "approximately"` × non-negative `input` | 🟡 **Missing: new dimension** | **Action**: Section 2's `sign_display` rows (see the Summary). |
| **S15.3b**, **S15.3c** | `"approximately"` × `-1230.05`; `"approximately, always"` × `1.2` | 🟡 **Missing: new dimension** | Section 2's `"approximately"` rows have only non-negative inputs. **Action**: add these rows (see the Summary). |
| **S15.2c**, **S15.7**, **S15.8** | — | ⚪ **Out of scope** | Optional behavior, units, or implementation choices. |

### 15.4 Notes

* S15.6b: in `ar` and `ar_EG` the minus sign has two code points (a bidi mark and `-`), so rule 2 allows the collapse, and rule 3 removes it from the upper value: `ar` P2 → `‎-1,230.05–1.2` / `"\u200E-1,230.05–1.2"`, which reads as a range from −1230.05 to 1.2. In `en` both signs stay (`-` has one code point). The text does not say whether a sign may be collapsed.
* S15.9: the heuristic adds no spacing to scientific values, which begin and end with digits: `en` scientific P1 → `1.234565E6–5E6`.

---

## Summary: Required Generator Changes

| Change | Needed by |
| :--- | :--- |
| Add the `numbering_system` dimension, with its rows in a separate file: `CORE_LOCALES` × `"latn"`, `"native"`, `"traditio"`, `"finance"` × `number_format = "decimal"`, `format_length = ""` × `CORE_NUMBERS` (+170 rows: 9 × 4 × 5, minus the 10 `ja` rows for `"traditio"` and `"finance"`, whose numbering systems are algorithmic) | S1.3a–b, S1.5, S1.6b, S1.7b, S1.8 |
| Add the `sign_display` dimension, with its rows in a separate file: `CORE_LOCALES` × `"always"` × `number_format = "decimal"`, `format_length = ""` × `CORE_NUMBERS` (45 rows), and `CORE_LOCALES` × `"approximately"` × the same × the non-negative `CORE_NUMBERS` (36 rows) (+81 rows) | S2.9, S2.10, S9.1a, S9.2a, S15.3a |
| Add the `exponent_style` dimension, with its rows in a separate file: the 7 `CORE_LOCALES` with Latin digits (all but `ar_EG` and `bn`) × `"superscript"` × `number_format = "scientific"` × `CORE_NUMBERS` (+35 rows) | S2.12 |
| Add the special values `Infinity`, `-Infinity`, and `NaN`, with their rows in a separate file: `CORE_LOCALES` × `number_format = "decimal"`, `format_length = ""` (+27 rows) | S2.14 |
| Add `numbering_system` rows for `number_format = "percent"`, in a separate file: `CORE_LOCALES` × `"latn"`, `"native"` × `format_length = ""` × `CORE_NUMBERS` (+90 rows) | S3.7a |
| Add the `precision` dimension, with its rows in a separate file: `CORE_LOCALES` × `"significant: 3"`, `"fraction: 1"` × `number_format = "decimal"` × `format_length = "short"`, `"long"` × `CORE_NUMBERS` and `1040000.0` (+216 rows: 9 × 2 × 2 × 6) | S4.7d, S4.8b, S5.4 |
| Add `1040000.0` to the extended numbers (+45 rows in `decimals_extended_numbers.tsv`: `CORE_LOCALES` × the five styles) | S4.7d |
| Add the `grouping` and `integer_width` dimensions and the `precision` value `"fraction: 4-5"`, with their rows in a separate file: `CORE_LOCALES` × (`grouping = "off"`; `integer_width = "min: 5"` with `grouping = "off"`; `precision = "fraction: 4-5"`) × `number_format = "decimal"`, `format_length = ""` × `CORE_NUMBERS` (+135 rows: 9 × 3 × 5) | S6.2c, S6.4c–e, S10.3, S10.5 |
| Add `sign_display = "always"` rows for `number_format = "percent"`, in a separate file: `CORE_LOCALES` × `format_length = ""` × `CORE_NUMBERS` (+45 rows) | S7.5b, S9.2a |
| Add the `integer_width` value `"max: 2"`, with its rows in a separate file: `CORE_LOCALES` × `number_format = "decimal"`, `format_length = ""` × `CORE_NUMBERS` (+45 rows) | S10.2 |
| Add the special values `Infinity`, `-Infinity`, and `NaN` for the other styles, in a separate file: `CORE_LOCALES` × `number_format = "percent"`, `"scientific"`, and `"decimal"` with `format_length = "short"` and `"long"` (+108 rows: 9 × 4 × 3) | S10.7, S10.8 |
| Add `precision` rows for `number_format = "scientific"`, in a separate file: `CORE_LOCALES` × `"significant: 3"`, `"significant: 2-4"` × `CORE_NUMBERS` (+90 rows) | S11.6b, S12.8 |
| Add `precision` rows for `number_format = "decimal"`, `format_length = ""`, in a separate file: `CORE_LOCALES` × `"significant: 3"`, `"significant: 2-4"` × `CORE_NUMBERS` (+90 rows) | S12.1b, S12.2, S12.4, S12.6 |
| Add the `precision` values `"increment: 50"` and `"increment: 0.65"`, with their rows in a separate file: `CORE_LOCALES` × `number_format = "decimal"`, `format_length = ""` × `CORE_NUMBERS` (+90 rows) | S13.1 |
| Add the `misc_pattern` dimension, with its rows in a separate file: `CORE_LOCALES` × `"atLeast"`, `"atMost"` × `number_format = "decimal"`, `format_length = ""` × `CORE_NUMBERS` (+90 rows) | S14.3, S14.4 |
| Add the `upper_input` dimension, with its rows in a separate file: `CORE_LOCALES` × the five styles × the four pairs of `input` and `upper_input` (+180 rows: 9 × 5 × 4) | S15.1, S15.2a–b, S15.4–S15.6, S15.9 |
| Add `sign_display` rows for `"approximately"` × `-1230.05` and `"approximately, always"` × `1.2`, in a separate file: `CORE_LOCALES` × `number_format = "decimal"`, `format_length = ""` (+18 rows) | S15.3b, S15.3c |

The rows that change the digits are `ar` × `"native"` and `"traditio"` (`arab`), and `ar_EG` and `bn` × `"latn"`. The other rows check that each key falls back to the expected numbering system.

Together, Sections 1–15 add 1,555 rows: 1,510 in separate files (170 + 81 + 35 + 27 + 90 + 216 + 135 + 45 + 45 + 108 + 90 + 90 + 90 + 90 + 180 + 18) and 45 in `decimals_extended_numbers.tsv`; `decimals.tsv` is unchanged.
