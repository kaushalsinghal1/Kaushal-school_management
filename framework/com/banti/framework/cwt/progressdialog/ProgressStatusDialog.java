package com.banti.framework.cwt.progressdialog;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.banti.framework.platform.Application;
import com.banti.framework.utils.I18NManager;

public class ProgressStatusDialog implements ActionListener, ProgressHandler {

    public final static String PROGRESS_ACTION_CLOSE = "maintenance.progress.close";

    public final static String PROGRESS_ACTION_ABORT = "maintenance.progress.abort";

    private static I18NManager i18nManager;
    static {
        i18nManager = I18NManager.getI18NManager(Locale.getDefault(), "com.banti.framework.resources.Resources");
    }
    protected JTextArea txtStatus;

    protected JScrollPane jsp;

    protected JProgressBar progressBar;

    protected JButton btnClose, btnAbort;

    protected boolean scrollToLatestMessage = true;

    protected JOptionPane optionPane;

    protected JDialog dialog;

    protected ActionListener statusListener;

    /**
     * Constructor It takes ActionListener as argument. Whenever the "Abort"
     * button is clicked the action is notified to caller.
     * 
     * @param parent -
     *            Component
     * @param title -
     *            String
     * @param al -
     *            ActionListener
     */
    public ProgressStatusDialog(Component parent, String title, JProgressBar bar, ActionListener al) {
        statusListener = al;
        progressBar = bar;
        optionPane = createOptionPanel();
        // dialog = optionPane.createDialog(parent, title);
        dialog = new JDialog(Application.Frame);
        initDialog(parent, title);
    }

    /**
     * Constructor with parent dialog. It takes ActionListener as argument.
     * Whenever the "Abort" button is clicked the action is notified to caller.
     * 
     * @param parent -
     *            Dialog
     * @param title -
     *            String
     * @param al -
     *            ActionListener
     */
    public ProgressStatusDialog(Dialog parent, String title, JProgressBar bar, ActionListener al) {
        statusListener = al;
        progressBar = bar;
        optionPane = createOptionPanel();
        dialog = new JDialog(parent);
        initDialog(parent, title);
    }

    /**
     * Constructor with parent dialog. It takes ActionListener as argument.
     * Whenever the "Abort" button is clicked the action is notified to caller.
     * 
     * @param parent -
     *            Dialog
     * @param title -
     *            String
     * @param al -
     *            ActionListener
     */
    public ProgressStatusDialog(Frame parent, String title, JProgressBar bar, ActionListener al) {
        statusListener = al;
        progressBar = bar;
        optionPane = createOptionPanel();
        dialog = new JDialog(parent);
        initDialog(parent, title);
    }

    private void initDialog(Component parent, String title) {
        dialog.setTitle(title);
        dialog.setContentPane(optionPane);
        dialog.setResizable(true);
        Dimension size = new Dimension(400, 300);
        dialog.setSize(size);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dialogClose();
            }

        });
    }

    /**
     * To initialize the UI Components
     */
    protected JOptionPane createOptionPanel() {

        txtStatus = new JTextArea();
        txtStatus.setWrapStyleWord(true);
        txtStatus.setEditable(false);
        txtStatus.setAutoscrolls(false);
        jsp = new JScrollPane(txtStatus);

        btnClose = new JButton(i18nManager.getString("CLOSE"));
        btnClose.setActionCommand(PROGRESS_ACTION_CLOSE);
        btnClose.addActionListener(this);
        btnClose.setEnabled(false);
        btnAbort = new JButton(i18nManager.getString("ABORT"));
        btnAbort.setActionCommand(PROGRESS_ACTION_ABORT);
        btnAbort.addActionListener(statusListener);

        JPanel jpControls = new JPanel();
        jpControls.setLayout(new BoxLayout(jpControls, BoxLayout.X_AXIS));
        jpControls.add(btnClose);
        jpControls.add(Box.createHorizontalStrut(20));
        jpControls.add(btnAbort);

        Object[] messages = { jsp, progressBar };
        Object[] options = { jpControls };
        return new JOptionPane(
            messages,
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.CANCEL_OPTION,
            null,
            options);
    }

    /**
     * Method to append the message to the Text Area.
     * 
     * @param String -
     *            msg
     * @return void
     */
    public void addMessage(String msg) {
        txtStatus.append(msg);
        if (scrollToLatestMessage) {
            jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
        }
    }

    /**
     * Method to set Text Area to display the messages.
     * 
     * @param String -
     *            msg
     * @return void
     */
    public void setMessage(String msg) {
        txtStatus.setText(msg);
    }

    /**
     * Method to update the ProgressBar with the value.
     * 
     * @param int -
     *            value
     * @return void
     */
    public void handleProgress(int value, Object text) {
        if (value == 100) {
            btnClose.setEnabled(true);
            btnAbort.setEnabled(false);
        }
        addMessage((String) text);
    }

    /**
     * Method to set the Scroll Pane to the Latest Messages.
     * 
     * @param boolean -
     *            scrollToLatestMessage
     * @return void
     */
    public void setScrollToLatestMessage(boolean latest) {
        this.scrollToLatestMessage = latest;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        if (command.equals(PROGRESS_ACTION_CLOSE)) {
            dialogClose();
        }
    }

    /**
     * Method to dispose the Dialog
     * 
     * @param void
     * @return void
     */
    public void close() {
        if (dialog != null) {
            dialog.dispose();
            optionPane = null;
            dialog = null;
        }
    }

    private void dialogClose() {
        close();
    }

    public void setFinished(boolean finish) {
        btnClose.setEnabled(finish);
        if (finish) {
            btnClose.requestFocus();
        }
        btnAbort.setEnabled(!finish);
    }

    public void setVisible(boolean visible) {
        dialog.setVisible(visible);
    }

}