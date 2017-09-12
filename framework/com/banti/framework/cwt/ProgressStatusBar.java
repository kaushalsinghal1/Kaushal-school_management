package com.banti.framework.cwt;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

/**
 * This is an abstract class which controls a threaded process and its progress with JProgressBar.
 * To create a concrete class, you need to implements three methods,
 * <pre>
 *  - <code>void doPreProcess()</code>
 *  - <code>void doPostProcess()</code>
 *  - <code>void doFinally()</code>
 * </pre>
 * which are invoked
 *  - before the main process,
 *  - after the main process,
 *  - after doPostProcess even if some exception occurred while executing the process
 * respectively.
 * 
 * To use the concrete class, call the method <code>start()</code> with the main process as
 * its argument which implements <code>Runnable</code> interface. 
 *
 * @author iku
 */

abstract public class ProgressStatusBar {

    public static final int DEFAULT_DELAY = 100;
    public static final int DEFAULT_STEP = 2;

    public static final int DEFAULT_MINIMUM = 0;
    public static final int DEFAULT_MAXIMUM = 100;

    private static final int WAIT = 1;
    private static final int INDETERMINATEWAIT = 30;

    private static final int MAIN_RUNNING = 0;
    private static final int MAIN_FINISHED = 1;
    private static final int POST_START = 2;
    private static final int POST_FINISHED = 3;
    private static final int CLOSE_START = 4;
    private static final int ALL_FINISHED = 5;
    private static final int CANCELED = -1;

    protected JDialog dialog;
    protected JProgressBar progressBar;

    private Box box;
    private JLabel label;

    private int currentProgress;
    private int maxProgress;

    private int waitCount;
    private int indeterminateCount;

    private int progressStatus;

    private int step = DEFAULT_STEP;
    private int delay = DEFAULT_DELAY;

    private int userMin = DEFAULT_MINIMUM;
    private int userMax = DEFAULT_MAXIMUM;

    private Thread thread;

    private Timer progressTimer;

    private Dimension progressBarSize;

    private JPopupMenu pmCancel;
    private MouseListener mlCancel;

    //    private static I18NManager i18nManager;
    //    static {
    //        i18nManager = I18NManager.getI18NManager(Locale.getDefault(), "com.cysols.cwt.Resources");
    //    }

    /**
     * Create a non-Modal dialog with an empty title and the specified owner frame.
     * 
     * @param owner      frame which owns this instance. 
     * @param message    message which displayed over the progressBar. 
     */
    public ProgressStatusBar(Frame owner, String message) {
        this(owner, null, message, false);
    }

    /**
     * Create a dialog with an empty title and the specified owner frame.
     * 
     * @param owner      frame which owns this instance.
     * @param message    message displayed over the progressBar.
     * @param modal      create a Modal dialog if modal is true.
     */
    public ProgressStatusBar(Frame owner, String message, boolean modal) {
        this(owner, null, message, modal);
    }

    /**
     * Create a dialog with the specified title and the specified owner frame.
     * 
     * @param owner      frame which owns this instance.
     * @param title      title displayed by the native window system. Nothing is displayed if null.
     * @param message    message displayed over the progressBar.
     * @param modal      create a Modal dialog if modal is true.
     */
    public ProgressStatusBar(Frame owner, String title, String message, boolean modal) {
        if (title == null) {
            dialog = new JDialog(owner, modal);
            dialog.setUndecorated(true);
        } else {
            dialog = new JDialog(owner, title, modal);
        }

        initialize(message);
    }

    /**
     * Create a non-Modal dialog with an empty title and the specified owner dialog.
     * 
     * @param owner      dialog which owns this instance.
     * @param message    message displayed over the progressBar.
     */
    public ProgressStatusBar(Dialog owner, String message) {
        this(owner, null, message, false);
    }

    /**
     * Create a dialog with an empty title and the specified owner dialog.
     * @param owner      dialog which owns this instance.
     * @param message    message displayed over the progressBar.
     * @param modal      create a Modal dialog if modal is true. 
     */
    public ProgressStatusBar(Dialog owner, String message, boolean modal) {
        this(owner, null, message, modal);
    }

    /**
     * Create a dialog with the specified title and the specified owner dialog.
     * 
     * @param owner      dialog which owns this instance.
     * @param title      title displayed by the native window system. Nothing is displayed if null.
     * @param message    message displayed over the progressBar.
     * @param modal      create a Modal dialog if modal is true.
     */
    public ProgressStatusBar(Dialog owner, String title, String message, boolean modal) {
        if (title == null) {
            dialog = new JDialog(owner, modal);
            dialog.setUndecorated(true);
        } else {
            dialog = new JDialog(owner, title, modal);
        }

        initialize(message);
    }

    /**
     * Set a message string for the progress bar.
     * 
     * @param message     message string displayed over the progressBar.
     */
    public void setMessage(String message) {
        label.setText(message);
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
     * @param step desired step of the progressBar. 
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
     * @param delay desired delay of Timer action.
     */
    public void setDelay(int delay) {
        this.delay = delay;
        progressTimer.setDelay(delay);
    }

    /**
     * Return the minimum value of progress bar.
     * @return the minimum value of progress bar.
     */
    public int getMin() {
        userMin = progressBar.getMinimum();
        return userMin;
    }

    /**
     * Set the minmum value of progress bar.
     * @param min minimum value of progress bar.
     */
    public void setMin(int min) {
        userMin = min;
        progressBar.setMinimum(min);
    }

    /**
     * Return the maximum value of progress bar.
     * @return the maximum value of progress bar.
     */
    public int getMax() {
        userMax = progressBar.getMaximum();
        return userMax;
    }

    /**
     * Set the maxmum value of progress bar.
     * @param max maximum value of progress bar.
     */
    public void setMax(int max) {
        userMax = max;
        progressBar.setMaximum(max);
    }

    protected int getCurrent() {
        return currentProgress;
    }
    
    /**
     * Set a string on the progress bar after the main process finished.
     * 
     * @param s string displayed on the progressBar. 
     */
    public void setProgressString(String s) {
        progressBar.setString(s);
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

    /**
     * Show the progress bar dialog.
     *
     */
    public void showProgressBar() {
        dialog.setVisible(true);
    }

    /**
     * Hide the progress bar dialog.
     *
     */
    public void hideProgressBar() {
        dialog.setVisible(false);
    }

    private class InnerCanceler extends MouseAdapter implements ActionListener {
        private JMenuItem miCancel;
        private InnerCanceler(JMenuItem menu) {
            super();
             miCancel = menu;
        }

        public void actionPerformed(ActionEvent ae) {
                    if (progressStatus == MAIN_RUNNING) {
                        progressStatus = CANCELED;
                    }
        }
        
        public void mouseClicked(MouseEvent me) {
            if (progressStatus != MAIN_RUNNING) return;
            
            if (javax.swing.SwingUtilities.isRightMouseButton(me)) {
                pmCancel.setSelected(miCancel);
                pmCancel.show(me.getComponent(), me.getX(), me.getY());
            }
        }
    }
    
    public void setCancelEnabled(boolean enable) {
        if (enable) {
            if (pmCancel == null) {
                pmCancel = new JPopupMenu();
                JMenuItem miCancel = new JMenuItem("Cancel");
                InnerCanceler canceler = new InnerCanceler(miCancel);
                miCancel.addActionListener(canceler);
                pmCancel.add(miCancel);
                mlCancel = canceler;
            }
            dialog.addMouseListener(mlCancel);
        } else {
            pmCancel = null;
            dialog.removeMouseListener(mlCancel);
        }
    }

    /**
     * This method is called before the main process is performed.
     * 
     */
    abstract protected void doPreProcess();

    /**
     * This method is called after the main process finished.
     * 
     */
    abstract protected void doPostProcess();

    /**
     * This method is called even if processes are interrupted by some exception.
     * 
     */
    abstract protected void doFinally();

    /**
     * This method is called when cancel is selected by popup menu.
     *
     */
    protected void doCancel() {
    }

    /**
     * This executes a group of processes automatically with the progressBar.
     * 
     * @param process the main process of the group. 
     */
    public void start(Runnable process) {
        start(process, userMax, getStep());
    }

    /**
     * This executes a group of processes automatically with the progressBar
     * whose maximum is <code>max</code>.
     * 
     * @param process the main process of the group.
     * @param max max value to which the progressBar is allowed to increase.
     */
    public void start(Runnable process, int max) {
        start(process, max, getStep());
    }

    /**
     * This executes a group of processes automatically with the progressBar
     * whose maximum value is <code>max</code> and increased by the specified step value each time.
     * 
     * @param process the main process of the group.
     * @param max max value to which the progressBar is allowed to increase.
     * @param step step of the progressBar.
     */
    public void start(final Runnable process, int max, int step) {
        progressStatus = MAIN_RUNNING;
        waitCount = 0;
        indeterminateCount = 0;

        currentProgress = userMin;
        maxProgress = normalizeValue(max);
        progressBar.setValue(userMin);
        progressBar.setString("");

        setStep(step);

        doPreProcess();

        thread = new Thread(new Runnable() {
            public void run() {
                process.run();
                if (progressStatus != CANCELED && progressStatus != CLOSE_START) {
                        progressStatus = MAIN_FINISHED;
                        if (pmCancel != null && pmCancel.isVisible()) {
                            pmCancel.setVisible(false);
                        }
                }
            }
        });

        progressTimer.start();
        thread.start();

        dialog.setVisible(true);
    }

    /**
     * Update the progressBar to the specified value.
     * 
     * @param value value the progressBar to be updated.  
     */
    protected void setValue(int value) {
        value = normalizeValue(value);

        updateToValue(value);
        progressBar.paintImmediately(0, 0, progressBarSize.width, progressBarSize.height);
    }

    /**
     * Calculate preferred location for the progressBar dialog.
     * 
     * @return preferred location point for the progressBar dialog
     */
    protected Point getPreferredLocation() {
        Component comp = dialog.getParent();
        int width = dialog.getWidth();
        Dimension size = comp.getSize();
        Point location = comp.getLocation();
        location.x += (size.width - width) / 2;
        location.y += size.height * 3 / 5;

        return location;
    }

    public void setLocationRelativeTo(Component c) {
        dialog.setLocationRelativeTo(c);
    }

    private void initialize(String message) {
        progressBar = new JProgressBar(userMin, userMax);
        progressBarSize = progressBar.getPreferredSize();
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(progressBarSize);

        progressStatus = ALL_FINISHED;

        label = new JLabel(message);

        box = Box.createVerticalBox();
        box.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        box.add(label);
        box.add(progressBar);

        dialog.getContentPane().add(box);

        dialog.pack();
        progressBarSize = progressBar.getSize();

        int width = dialog.getWidth();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = new Point((size.width - width) / 2, size.height * 3 / 5);
        dialog.setLocation(location);

        progressTimer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (progressStatus == MAIN_RUNNING) {
                    if (!thread.isAlive()) {
                        /*
                         * The thread of main process was interrupted by some exception.
                         */
                        progressStatus = CLOSE_START;
                        return;
                    }

                    if (indeterminateCount > INDETERMINATEWAIT) {
                        if (!progressBar.isIndeterminate()) {
                            progressBar.setIndeterminate(true);
                            progressTimer.setDelay(DEFAULT_DELAY * 2);
                        }
                    } else if (currentProgress == maxProgress) {
                        indeterminateCount++;
                    } else {
                        updateBar();
                    }

                } else if (progressStatus == MAIN_FINISHED) {
                    progressBar.setIndeterminate(false);
                    progressTimer.setDelay(DEFAULT_DELAY);

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
                            dialog.setVisible(false);
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

                } else if (progressStatus == CANCELED) {
                    progressStatus = CLOSE_START;
                    doCancel();
                }
            }
        });
    }

    private void windup() {
        boolean processCompleted = false;

        try {
            doPostProcess();
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
        dialog.dispose();
        doFinally();
    }

    private void updateBar() {
        if (currentProgress < maxProgress) {
            currentProgress += step;
        }
        if (currentProgress >= userMax) {
            progressBar.setValue(userMax - 1);
        } else {
            progressBar.setValue(currentProgress);
        }
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

}
