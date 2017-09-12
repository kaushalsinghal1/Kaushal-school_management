package com.banti.framework.cwt;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.banti.framework.utils.DateUtils;

public class TimeChooserPane extends JPanel implements ActionListener {

    private JTextField jtfInput;
    private JButton jbArrow;

    private final String UP_ARROW = "/com/banti/framework/cwt/images/rightarrow.gif";

    private final String OPEN = "OPEN";
    private final String OK = "OK";

    private CalendarDialog cDialog;
    private JFrame frame;
    private JDialog dialog;
    private boolean modal;
    
    public TimeChooserPane(JFrame frame, boolean modal) {
        super();
        this.initComponents();
        this.frame = frame;
        this.modal = modal;
    }

    public TimeChooserPane(JDialog dialog, boolean modal) {
        super();
        this.initComponents();
        this.dialog = dialog;
        this.modal = modal;
    }

    public void addOpenActionListener(ActionListener al) {
        jbArrow.addActionListener(al);
    }

    public void removeOpenActionListener(ActionListener al) {
        jbArrow.removeActionListener(al);
    }

    /**
     * Creates this component. If the lista data table is empty, the no popup will be displayed.
     */
    private void initComponents() {
        jtfInput = new JTextField();
        jtfInput.setEditable(false);
        setToDefaultValues();
        ImageIcon ii = new ImageIcon(getClass().getResource(UP_ARROW));
        jbArrow = new JButton(ii);
        jbArrow.setActionCommand(OPEN);
        jbArrow.addActionListener(this);

        jtfInput.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jbArrow.setMargin(new Insets(2, 2, 2, 2));

        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, jtfInput);
        add(BorderLayout.EAST, jbArrow);
    }

    public void setToDefaultValues() {
        this.setTimeStr(DateUtils.getSystemDate());
    }

    /**
     * Displays the specified text.
     * 
     * @param text
     * @return void
     */
    public void setTimeStr(String text) {
        if (text != null)
            jtfInput.setText(text);
    }

    /**
     * Returns the currently selected value.
     * 
     * @param void
     * @return selectedValue
     */
    public String getTimeStr() {
        return jtfInput.getText();
    }

    /**
     * Overrides original method to enable/disable indivisual components.
     * 
     * @param enable specify true/false for enable/disable
     */
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        jtfInput.setEnabled(enable);
        jbArrow.setEnabled(enable);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae == null)
            return;

        String action = ae.getActionCommand();

        if (OPEN.equalsIgnoreCase(action)) {
            if (frame != null) {
                cDialog = new CalendarDialog(frame, modal);
            } else if (dialog != null) {
                cDialog = new CalendarDialog(dialog, modal);
            }
            
            cDialog.setResizable(false);
            cDialog.addOKActionListener(this);

            cDialog.setTimeStr(this.getTimeStr());
            cDialog.setLocationRelativeTo(this);
            cDialog.setVisible(true);

        } else if (OK.equalsIgnoreCase(action)) {
            this.setTimeStr(cDialog.getTimeStr());
            cDialog.dispose();
        }
    }
}