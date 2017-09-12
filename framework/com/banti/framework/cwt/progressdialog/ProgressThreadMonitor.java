package com.banti.framework.cwt.progressdialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 * This is an abstract class which controls a threaded process and its progress
 * with JProgressBar. To create a concrete class, you need to implements three
 * methods,
 * 
 * <pre>
 * 
 *  
 *    - 
 *  
 * <code>
 * 
 *  void doPreProcess()
 *  
 * </code>
 * 
 *  
 *    - 
 *  
 * <code>
 * 
 *  void doPostProcess()
 *  
 * </code>
 * 
 *  
 *    - 
 *  
 * <code>
 * 
 *  void doFinally()
 *  
 * </code>
 * </pre>
 * 
 * which are invoked - before the main process, - after the main process, -
 * after doPostProcess even if some exception occurred while executing the
 * process respectively.
 * 
 * To use the concrete class, call the method <code>start()</code> with the
 * main process as its argument which implements <code>Runnable</code>
 * interface.
 */

public class ProgressThreadMonitor {

    public static final int DEFAULT_DELAY = 100;
    public static final int DEFAULT_STEP = 2;

    public static final int DEFAULT_MINIMUM = 0;
    public static final int DEFAULT_MAXIMUM = 100;

    private static final int WAIT = 1;

    private static final int MAIN_RUNNING = 0;
    private static final int MAIN_FINISHED = 1;
    private static final int POST_START = 2;
    private static final int POST_FINISHED = 3;
    private static final int CLOSE_START = 4;
    private static final int ALL_FINISHED = 5;

    private int currentProgress;
    private int nextProgress = -1;
    private int maxProgress;
    private boolean autoUpdate = true;

    private int waitCount;

    private int progressStatus;

    private int step = DEFAULT_STEP;
    private int delay = DEFAULT_DELAY;

    private int userMin = DEFAULT_MINIMUM;
    private int userMax = DEFAULT_MAXIMUM;

    private Thread thread;
    private MonitoredProcess monitoredProcess;
    private LinkedList<ProgressEvent> eventQueue;
    private ArrayList<ProgressHandler> handlerList;
    private Object result;

    private Timer progressTimer;

    private JProgressBar progressBar;
    private Dimension progressBarSize;

    /**
     * Create progress bar with the default min and max.
     *  
     */
    public ProgressThreadMonitor() {
        this(DEFAULT_MINIMUM, DEFAULT_MAXIMUM);
    }

    /**
     * Create progress bar with the specified min and max.
     *  
     */
    public ProgressThreadMonitor(int min, int max) {
        userMin = min;
        userMax = max;
        initialize();
    }

    /**
     * Returns the current step to increase the progressBar value.
     * 
     * @return step of the progress bar.
     */
    public int getStep() {
        return step;
    }

    /**
     * Set a current step.
     * 
     * @param step
     *            desired step of the progressBar.
     */
    public void setStep(int step) {
        if (step <= 0) {
            this.step = 1;
        } else if (step > userMax) {
            this.step = userMax;
        } else {
            this.step = step;
        }
    }

    /**
     * Returns the current delay used by Timer.
     * 
     * @return current delay of Timer action.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Set delay used by Timer.
     * 
     * @param delay
     *            desired delay of Timer action.
     */
    public void setDelay(int delay) {
        this.delay = delay;
        progressTimer.setDelay(delay);
    }

    /**
     * Return the minimum value of progress bar.
     * 
     * @return the minimum value of progress bar.
     */
    public int getMinimum() {
        userMin = progressBar.getMinimum();
        return userMin;
    }

    /**
     * Set the minmum value of progress bar.
     * 
     * @param min
     *            minimum value of progress bar.
     */
    public void setMinimum(int min) {
        userMin = min;
        progressBar.setMinimum(min);
    }

    /**
     * Return the maximum value of progress bar.
     * 
     * @return the maximum value of progress bar.
     */
    public int getMaximum() {
        userMax = progressBar.getMaximum();
        return userMax;
    }

    /**
     * Set the maxmum value of progress bar.
     * 
     * @param max
     *            maximum value of progress bar.
     */
    public void setMaximum(int max) {
        userMax = max;
        progressBar.setMaximum(max);
    }

    public void paintImmediately() {
        progressBar.paintImmediately(0, 0, progressBarSize.width, progressBarSize.height);
    }

    /**
     * Returns boolean value whether the process is running.
     * 
     * @return true if this process is runnning
     */
    public boolean isRunning() {
        return (progressStatus != ALL_FINISHED);
    }

    public void enableAutoUpdate(boolean enable) {
        autoUpdate = enable;
    }

    public void setNextProgress(int progress) {
        nextProgress = normalizeValue(progress);
    }

    public void updateProgress(int progress, Object event) {
        synchronized (eventQueue) {
            eventQueue.addLast(new ProgressEvent(normalizeValue(progress), event));
        }
    }

    public synchronized void addProgressHandler(ProgressHandler handler) {
        handlerList.add(handler);
    }

    public synchronized void removeProgressHandler(ProgressHandler handler) {
        handlerList.remove(handler);
    }

    public final JProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * This executes a group of processes automatically with the progressBar.
     * 
     * @param process
     *            the main process of the group.
     */
    public void start(MonitoredProcess process) {
        start(process, userMax, getStep());
    }

    /**
     * This executes a group of processes automatically with the progressBar
     * whose maximum is <code>max</code>.
     * 
     * @param process
     *            the main process of the group.
     * @param max
     *            max value to which the progressBar is allowed to increase.
     */
    public void start(MonitoredProcess process, int max) {
        start(process, max, getStep());
    }

    /**
     * This executes a group of processes automatically with the progressBar
     * whose maximum value is <code>max</code> and increased by the specified
     * step value each time.
     * 
     * @param process
     *            the main process of the group.
     * @param max
     *            max value to which the progressBar is allowed to increase.
     * @param step
     *            step of the progressBar.
     */
    public void start(MonitoredProcess process, int max, int step) {
        progressStatus = MAIN_RUNNING;
        waitCount = 0;
        monitoredProcess = process;

        currentProgress = userMin;
        maxProgress = normalizeValue(max);
        progressBar.setValue(userMin);

        setStep(step);

        if (!monitoredProcess.doPreProcess()) {
            return;
        }

        thread = new Thread(new Runnable() {
            public void run() {
                result = monitoredProcess.doMainProcess();
                progressStatus = MAIN_FINISHED;
            }
        });

        progressTimer.start();
        thread.start();
    }

    /**
     * Update the progressBar to the specified value.
     * 
     * @param value
     *            value the progressBar to be updated.
     */
    public void setValue(int value) {
        value = normalizeValue(value);

        updateToValue(value);
    }

    private void initialize() {
        progressBar = new JProgressBar(userMin, userMax);
        progressBarSize = progressBar.getPreferredSize();
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(progressBarSize);

        eventQueue = new LinkedList<ProgressEvent>();
        handlerList = new ArrayList<ProgressHandler>();

        progressStatus = ALL_FINISHED;

        progressTimer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (progressStatus == MAIN_RUNNING) {
                    if (!thread.isAlive()) {
                        /*
                         * The thread of main process was interrupted by some
                         * exception.
                         */
                        progressStatus = CLOSE_START;
                        return;
                    }

                    if (!eventQueue.isEmpty()) {
                        synchronized (eventQueue) {
                            Iterator<ProgressEvent> eqItr = eventQueue.iterator();
                            while (eqItr.hasNext()) {
                                ProgressEvent pe = eqItr.next();
                                eqItr.remove();
                                updateToValue(pe.getProgress());
                                Iterator<ProgressHandler> itr = handlerList.iterator();
                                while (itr.hasNext()) {
                                    ProgressHandler handler = itr.next();
                                    handler.handleProgress(pe.getProgress(), pe.getEventValue());
                                }
                            }
                        }
                    } else if (autoUpdate) {
                        updateBar();
                    }
                } else if (progressStatus == MAIN_FINISHED) {
                    progressTimer.setDelay(DEFAULT_DELAY);

                    synchronized (eventQueue) {
                        Iterator<ProgressEvent> eqItr = eventQueue.iterator();
                        while (eqItr.hasNext()) {
                            ProgressEvent pe = eqItr.next();
                            eqItr.remove();
                            updateToValue(pe.getProgress());
                            Iterator<ProgressHandler> itr = handlerList.iterator();
                            while (itr.hasNext()) {
                                ProgressHandler handler = itr.next();
                                handler.handleProgress(pe.getProgress(), pe.getEventValue());
                            }
                        }
                    }

                    if (currentProgress < maxProgress) {
                        updateToValue(maxProgress);
                    }
                    progressStatus = POST_START;

                } else if (progressStatus == POST_START) {
                    if (maxProgress == userMax) {
                        if (waitCount < WAIT) {
                            waitCount++;
                        } else {
                            progressStatus = CLOSE_START;
                            //close();
                            windup();
                        }
                    } else {
                        progressStatus = POST_FINISHED;
                        windup();
                    }

                } else if (progressStatus == POST_FINISHED) {
                    if (currentProgress >= userMax) {
                        progressStatus = CLOSE_START;
                        return;
                    } else if (waitCount >= WAIT) {
                        updateToValue(userMax);
                        progressStatus = CLOSE_START;
                    } else {
                        updateBar();
                    }
                    waitCount++;

                } else if (progressStatus == CLOSE_START) {
                    progressStatus = ALL_FINISHED;
                    close();

                }
            }
        });
    }

    private void windup() {
        boolean processCompleted = false;

        try {
            monitoredProcess.doPostProcess(result);
            maxProgress = userMax;
            processCompleted = true;
        } finally {
            if (!processCompleted) {
                progressStatus = ALL_FINISHED;
                close();
            }
        }
    }

    private void close() {
        progressTimer.stop();
        progressTimer.setDelay(delay);
        monitoredProcess.doFinally();
    }

    private void updateBar() {
        int next = maxProgress;

        if (nextProgress > 0) {
            next = nextProgress;
        }
        if (currentProgress < next) {
            currentProgress += step;
        }
        if (currentProgress >= userMax) {
            currentProgress = userMax;
        }
        progressBar.setValue(currentProgress);
    }

    private void updateToValue(int value) {
        currentProgress = value;
        progressBar.setValue(value);
    }

    private int normalizeValue(int value) {
        return normalizeValue(value, userMin, userMax);
    }

    private int normalizeValue(int value, int min, int max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * This class is used for internal use only.
     * 
     * @author iku
     */
    private static class ProgressEvent {
        private int progress;
        private Object eventValue;

        public ProgressEvent(int progress, Object eventValue) {
            this.progress = progress;
            this.eventValue = eventValue;
        }

        public int getProgress() {
            return progress;
        }

        public Object getEventValue() {
            return eventValue;
        }
    }
}