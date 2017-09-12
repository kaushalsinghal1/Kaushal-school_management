package com.banti.framework.platform.peer;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import com.banti.framework.platform.Application;
import com.banti.framework.platform.theme.AquaTheme;
import com.banti.framework.platform.theme.ContrastTheme;
import com.banti.framework.platform.theme.EmeraldTheme;
import com.banti.framework.platform.theme.SandstoneTheme;
import com.banti.framework.theme.ToolKit;

/**
 * TODO
 */
public class PreferencePane extends JPanel implements ActionListener {
    
    // Possible Look & Feels
    private static final String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
    private static final String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    private static final String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    private static final String windowsClassic = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
    private static final String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
    private static final String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
    private static int typeCount = 6; 
    
    private static MetalTheme[] themes;
    private static int themeCount = 6; 
    
    
    private JRadioButton lafMetalButton;
    private JRadioButton[] lafButtons;
    private JRadioButton[] themeButtons;
    
    private Dialog dialog;
    private Application application;
    
    public PreferencePane(Dialog dialog, Application application) {
        super(new BorderLayout());
        this.dialog = dialog;
        this.application = application;
        
        ToolKit tool = ToolKit.getInstance();
        JPanel lafPanel = new JPanel();        
        lafPanel.setBorder(BorderFactory.createTitledBorder(tool.getString("LOOK_AND_FEEL")));
        
        lafButtons = new JRadioButton[this.typeCount];
        ButtonGroup lafGroup = new ButtonGroup();
        lafMetalButton = new JRadioButton();
        this.initializeLAFButton(0, metal, lafMetalButton, lafGroup, lafPanel);
        this.initializeLAFButton(1, motif, new JRadioButton(), lafGroup, lafPanel);
        this.initializeLAFButton(2, windows, new JRadioButton(), lafGroup, lafPanel);
        this.initializeLAFButton(3, windowsClassic, new JRadioButton(), lafGroup, lafPanel);
        this.initializeLAFButton(4, gtk, new JRadioButton(), lafGroup, lafPanel);
        this.initializeLAFButton(5, mac, new JRadioButton(), lafGroup, lafPanel);
        
        JPanel themePanel = new JPanel();
        themePanel.setBorder(BorderFactory.createTitledBorder(tool.getString("THEME")));
        
        ButtonGroup themeGroup = new ButtonGroup();
        themes = new MetalTheme[themeCount];
        themes[0] = 	new DefaultMetalTheme();
        JRadioButton defButton = new JRadioButton();
        themes[1] =  new EmeraldTheme();
        themes[2] =  new AquaTheme();
        themes[3] =  new SandstoneTheme();
        themes[4] =  new ContrastTheme();
        
        if (themes.length>5) {
            try {
                themes[5] = (MetalTheme) Class.forName("javax.swing.plaf.metal.OceanTheme").newInstance();
            } catch (Exception e) {
                themeCount--;
            }
        }
        
        themeButtons = new JRadioButton[themeCount];
        this.initializeThemeButton(0, defButton, themeGroup, themePanel);
        defButton.setText(tool.getString("STEEL"));
        defButton.setSelected(true);
        for (int i = 1; i < themeCount; i++) {
            JRadioButton b =  new JRadioButton();
            this.initializeThemeButton(i, b, themeGroup, themePanel);
            if (i == 5) b.setText(tool.getString("OCEAN"));
        }
        
        JPanel mainPanel = new JPanel(new GridLayout(2,1));
        mainPanel.add(lafPanel);
        mainPanel.add(themePanel);
        
        // Initializes Apply Button
        JButton applyButton = new JButton(tool.getString("APPLY"));
        applyButton.setActionCommand("APPLY");
        applyButton.addActionListener(this);
        // Initializes Close Button
        JButton closeButton = new JButton(tool.getString("CLOSE"));
        closeButton.setActionCommand("CLOSE");
        closeButton.addActionListener(this);
        // Deploys buttons on Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);
        buttonPanel.add(closeButton);

        super.add(mainPanel, BorderLayout.CENTER);
        super.add(buttonPanel, BorderLayout.SOUTH);

    }

    /**
     * @param i
     * @param theme
     */
    private void initializeThemeButton(int index, JRadioButton button, 
            									ButtonGroup group, JPanel panel) {

        themeButtons[index] = button;
        button.setText(themes[index].getName());
        button.setActionCommand("THEME");
        button.addActionListener(this);
        
        group.add(themeButtons[index]);
        panel.add(themeButtons[index]);
        

        Preferences prefs = Preferences.userNodeForPackage(application.getClass());
        String theme = prefs.get("theme", DefaultMetalTheme.class.getName());
        for (int i = 0; i < themes.length; i++) {
            if (themes[i] == null)
                continue;
            if (themes[i].getClass().getName().equals("javax.swing.plaf.metal.OceanTheme"))
                theme = MetalLookAndFeel.getCurrentTheme().getClass().getName();
                
        }

        if (themes[index].getClass().getName().equals(theme)) {
            themeButtons[index].setSelected(true); 
        }
        
        if (lafMetalButton.isSelected()) {
            themeButtons[index].setEnabled(true);
        } else {
            themeButtons[index].setEnabled(false);
        }
    }

    private void initializeLAFButton(int index, String laf, JRadioButton button, 
            								ButtonGroup group, JPanel panel) {
        
        // GTK is disabled at present
        if (gtk.equals(laf)) return;
        
        LookAndFeel currentLAF = UIManager.getLookAndFeel();

        lafButtons[index] =button;

        try {
            Class c = Class.forName(laf);
            LookAndFeel lafClass = (LookAndFeel) c.newInstance();

            button.setText(lafClass.getName());
            if (windowsClassic.equals(laf)) {
                button.setText(lafClass.getName() + " Classic");
                button.setSelected(true);
            }
            
            button.setActionCommand(laf);
            button.addActionListener(this);
            
            if (lafClass.isSupportedLookAndFeel()) {
                button.setEnabled(true);
            } else {
                button.setEnabled(false);
            }
            
            if (currentLAF.getClass().getName().equals(lafClass.getClass().getName())) {
                button.setSelected(true);
            }
            
            group.add(button);
            panel.add(button);
        
        } catch (Exception e) {
            button.setVisible(false);
            return;
        }
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae == null) return;
        String action = ae.getActionCommand();
        if (action == null) return;

        if (action.equals("CLOSE")) {
            dialog.dispose();
            
        } else if (action.equals("APPLY")) {
            Preferences prefs = Preferences.userNodeForPackage(application.getClass());
            
            for (int i = 0; i < lafButtons.length; i++) {
                if (lafButtons[i].isSelected()) {
                    String className = lafButtons[i].getActionCommand();

                    try {
                        UIManager.setLookAndFeel(className);

                        if (className.equals(metal)) {
                            for (int j = 0; j < themeButtons.length; j++) {
                                if (themeButtons[j].isSelected()) {
                                    prefs.put("theme", themes[j].getClass().getName());
                                    break;
                                }
                            }

                        } else {
                            prefs.remove("theme");
                        }
                        
                        SwingUtilities.updateComponentTreeUI(this);
                        SwingUtilities.updateComponentTreeUI(dialog);
                        SwingUtilities.updateComponentTreeUI(application);
                        
                        dialog.pack();
                        dialog.repaint();
                        
                        prefs.put("laf", className);
                        
                    }  catch (Exception e) {
                        ToolKit tool = ToolKit.getInstance();
                        JOptionPane.showMessageDialog(this,
                                								tool.getString("MSG_LAF_ERROR"),
    															tool.getString("WARNING"),
    															JOptionPane.WARNING_MESSAGE);
                    }

                    return;
                }
            }
        
        } else if (action.equals("THEME")) {
            for (int j = 0; j < themeButtons.length; j++) {
                if (themeButtons[j].isSelected()) {
                    MetalLookAndFeel.setCurrentTheme(themes[j]);
                    break;
                }
            }

        } else {
            if (lafMetalButton.isSelected()) {                
                for (int i = 0; i < themeButtons.length; i++) {
                    themeButtons[i].setEnabled(true);
                }
            } else {
                for (int i = 0; i < themeButtons.length; i++) {
                    themeButtons[i].setEnabled(false);
                }                
            }
        }
    }
  
}
