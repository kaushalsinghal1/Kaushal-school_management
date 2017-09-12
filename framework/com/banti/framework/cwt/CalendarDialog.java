package com.banti.framework.cwt;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.banti.framework.utils.I18NManager;


public class CalendarDialog extends JDialog implements ActionListener {
  
    private static I18NManager i18nManager;
    static {
        i18nManager = 
            I18NManager.getI18NManager(Locale.getDefault(), "com.banti.framework.cwt.Resources");
    }

    private CalendarUI calUI;
    private JButton okButton;
    private JButton cancelButton;
    private ArrayList listeners;
    
    public CalendarDialog(JDialog dialog, boolean modal) {
        super(dialog, modal);
        this.initComponents();
        this.setComponents();
        super.setTitle(getString("DATE_SELECTOR"));
        super.setSize(285, 300);
        
        listeners = new ArrayList(1);
    }

    public CalendarDialog(JFrame frame, boolean modal) {
        super(frame, modal);
        this.initComponents();
        this.setComponents();
        super.setTitle(getString("DATE_SELECTOR"));
        super.setSize(285, 300);

        listeners = new ArrayList(1);
    }
    
    public void addOKActionListener(ActionListener al) {
        listeners.add(al);
    }

    public void removeOKActionLister(ActionListener al) {
        synchronized (listeners) {
            listeners.remove(al);
        }
    }
  

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae == null) return;
        
        if (ae.getSource() == okButton) {
            try {
                String dateStr = calUI.getDateInString();
                if ("".equals(dateStr)) {
                    throw new Exception();
                }
                
                synchronized (listeners) {
                    for (int i = 0; i < listeners.size(); i++) {
                        ActionListener al = (ActionListener) listeners.get(i);
                        al.actionPerformed(ae);
                    }
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        this.getString("SELECT_DATE"),
                        this.getString("CAL_MSG"),
                        JOptionPane.WARNING_MESSAGE);
                
                return;
             }
        }
    }
    
    private void initComponents() {
        calUI = new CalendarUI();
        okButton = new JButton(getString("OK"));
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        
        cancelButton = new JButton(getString("CANCEL"));
        cancelButton.setActionCommand("CANCEL");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
    }
  
    private void setComponents() {
        JPanel southPanel = new JPanel();
        southPanel.add(okButton);
        southPanel.add(cancelButton);

        getContentPane().add(calUI, BorderLayout.CENTER);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }
  
    public Calendar getCalendar() {
        return calUI.getDate();
    }
  
    public String getTimeStr() {
        return calUI.getDateInString();
    }
    
    public void setTimeStr(String str) {
        calUI.setDateStr(str);
     }
    
    private String  getString(String key) {
        return i18nManager.getString(key);
    }
  
}
