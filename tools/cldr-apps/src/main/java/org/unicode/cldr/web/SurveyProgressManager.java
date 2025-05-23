/** Copyright (C) 2010-2014 IBM Corporation and Others. All Rights Reserved. */
package org.unicode.cldr.web;

import com.ibm.icu.text.NumberFormat;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.unicode.cldr.icu.dev.util.ElapsedTimer;
import org.unicode.cldr.util.StackTracker;

/**
 * @author srl
 */
public class SurveyProgressManager implements CLDRProgressIndicator {
    private static final Logger logger = SurveyLog.forClass(SurveyProgressManager.class);
    private Deque<SurveyProgressTask> tasks = new LinkedList<>();

    private class SurveyProgressTask implements CLDRProgressIndicator.CLDRProgressTask {
        boolean dead = false;
        int progressMax = 0; // "an operation is in progress"
        int progressCount = 0;
        String progressSub = null;
        long taskTime = System.currentTimeMillis();
        long subTaskTime = taskTime;
        String progressWhat = null;

        public SurveyProgressTask(String what, int max) {
            this.progressMax = max;
            this.progressWhat = what;
            this.progressCount = 0;
        }

        @Override
        public void close() {
            if (tasks.isEmpty() || !tasks.contains(this)) {
                SurveyLog.warnOnce(
                        logger,
                        "State Error: Closing an already-closed CLDRProgressIndicator at stack "
                                + StackTracker.currentStack());
            } else {
                tasks.remove(this); // remove from deque
            }
            logger.info(
                    "Progress ("
                            + progressWhat
                            + ") DONE - "
                            + ElapsedTimer.elapsedTime(taskTime, System.currentTimeMillis())
                            + " @"
                            + SurveyMain.uptime);
            dead = true;
        }

        @Override
        public void update(int count) {
            progressCount = count;
            subTaskTime = System.currentTimeMillis();
            logger.fine(
                    "Progress ("
                            + progressWhat
                            + ") on #"
                            + progressCount
                            + " @"
                            + SurveyMain.uptime);
        }

        @Override
        public void update(int count, String what) {
            progressCount = count;
            progressSub = what;
            subTaskTime = System.currentTimeMillis();
        }

        /**
         * Update the sub-progress without moving the count
         *
         * @param what
         */
        @Override
        public void update(String what) {
            progressSub = what;
            if (progressMax < 0) {
                progressCount++;
            }
            subTaskTime = System.currentTimeMillis();
            logger.fine("Progress (" + progressWhat + ") on " + what + " @" + SurveyMain.uptime);
        }

        @Override
        public long startTime() {
            return taskTime;
        }

        /**
         * What's the most recent time that anything happened?
         *
         * @return
         */
        public long recentTime() {
            if (subTaskTime > taskTime) {
                return subTaskTime;
            } else {
                return taskTime;
            }
        }

        NumberFormat pctFmt = NumberFormat.getPercentInstance();

        @Override
        public String toString() {
            if (dead) return "";
            StringBuffer buf = new StringBuffer();
            long now = System.currentTimeMillis();

            double max = progressMax;

            buf.append("<b>" + progressWhat + "</b>");
            if ((now - taskTime) > 5000) {
                buf.append(
                        " <span class='elapsedtime'>"
                                + ElapsedTimer.elapsedTime(taskTime, now)
                                + "</span>");
            }

            double cur = progressCount;

            appendProgressBar(buf, cur, max);

            if (progressMax >= 0) { // only show the actual number if >0
                buf.append(progressCount + " of " + progressMax + " &mdash; ");
            }
            if (max > 0) {
                double pct = (cur / max);
                buf.append(pctFmt.format(pct));
            } else if (progressCount > 0) {
                buf.append("(#" + progressCount + ")");
            }
            if (progressSub != null) {
                buf.append("<br/>" + progressSub);
            }
            if ((subTaskTime > taskTime) && ((now - subTaskTime) > 5000)) {
                buf.append(
                        " <span class='elapsedtime'>"
                                + ElapsedTimer.elapsedTime(subTaskTime, now)
                                + "</span>");
            }
            return buf.toString();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.unicode.cldr.web.CLDRProgressIndicator#openProgress(java.lang.String)
     */
    @Override
    public CLDRProgressTask openProgress(String what) {
        return openProgress(what, -100);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.unicode.cldr.web.CLDRProgressIndicator#openProgress(java.lang.String,
     * int)
     */
    @Override
    public CLDRProgressTask openProgress(String what, int max) {
        SurveyProgressTask t = new SurveyProgressTask(what, max);
        tasks.addLast(t);
        logger.info("Progress (" + what + ") BEGIN");
        return t;
    }

    public String getProgress() {
        // make a list of non-dead tasks, sorted by age
        Set<SurveyProgressTask> orderedTasks =
                new TreeSet<>(
                        new Comparator<SurveyProgressTask>() {

                            @Override
                            public int compare(SurveyProgressTask arg0, SurveyProgressTask arg1) {
                                return (int) (arg1.recentTime() - arg0.recentTime());
                            }
                        });
        for (SurveyProgressTask t : tasks) {
            if (t != null && !t.dead) orderedTasks.add(t);
        }

        StringBuffer buf = new StringBuffer();
        buf.append(
                "<table id='progress-list' border=0 class='progress-list"
                        + (orderedTasks.isEmpty() ? " progress-idle" : " progress-busy")
                        + "'><tr>");
        buf.append("<th>");
        if (orderedTasks.isEmpty()) {
            buf.append(
                    "<span id='busy0' onclick='document.getElementById(\"progress\").className=\"popout\";'><!-- idle -->\u00BB</span>");
        } else {
            buf.append(
                    "<span id='busy0' onclick='document.getElementById(\"progress\").className=\"popout\";'><!-- busy -->\u00BB</span>");
        }
        buf.append(
                "<span id='busy1' onclick='document.getElementById(\"progress\").className=\"\";'><!-- hide -->\u00AB</span>"
                        + "</th>");
        for (SurveyProgressTask t : orderedTasks) {
            buf.append("<td>");
            buf.append(t.toString());
            buf.append("</td>");
        }
        if (buf.length() > 0) {
            buf.append("</tr></table>");
        }
        return buf.toString();
    }

    public static StringBuffer appendProgressBar(StringBuffer buf, double cur, double max) {
        if (max > 0) {
            if (cur > max) {
                cur = max;
            }
            if (cur < 0) {
                cur = 0;
            }

            double barWid = (cur / max) * SurveyMain.PROGRESS_WID;

            int barX = (int) barWid;
            int remainX = SurveyMain.PROGRESS_WID - barX;

            buf.append("<table class='stprogress' border=0 ><tr height='12'>");
            if (barX > 0) {
                buf.append("<td class='bar' width='" + barX + "' >");
                buf.append("</td>");
            }
            if (remainX > 0) {
                buf.append("<td class='remain' width='" + remainX + "'>");
                buf.append("</td>");
            }
            buf.append("</table>");
        }
        return buf;
    }
}
