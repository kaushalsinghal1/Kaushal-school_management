package com.banti.framework.platform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.BevelBorder;

import com.banti.framework.platform.ViewMediator.DefaultCommand.Switch;
import com.banti.framework.theme.ToolKit;


final class StatusBar implements ViewColleague {

    private ViewMediator view;

    private JPanel statusBar;
    private JLabel messageBar;
    
    private JProgressBar progressBar;

    private JPanel optionPanel;
    private JPanel iconPanel;

    private JLabel rightIconLabel, centerIconLabel, leftIconLabel;
    private ImageIcon redIcon;
    private ImageIcon greenIcon;
    private ImageIcon blueIcon;
    private ImageIcon yellowIcon;
    private ImageIcon blackIcon;


    private String RED_IMAGE = "/com/cysols/framework/platform/images/redSB.gif";
    private String GREEN_IMAGE = "/com/cysols/framework/platform/images/greenSB.gif";
    private String BLUE_IMAGE = "/com/cysols/framework/platform/images/blueSB.gif";
    private String YELLOW_IMAGE = "/com/cysols/framework/platform/images/yellowSB.gif";
    private String BLACK_IMAGE = "/com/cysols/framework/platform/images/blackSB.gif";

    
    StatusBar(ViewMediator mediator) {
        view = mediator;

        ToolKit tool = ToolKit.getInstance();

        statusBar = new JPanel(new BorderLayout());
        statusBar.setToolTipText(tool.getString("STATUS_BAR_TOOL_TIP"));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        messageBar = new JLabel();

        progressBar = new JProgressBar();
        Dimension d_bar = new Dimension(200, 16);
        progressBar.setSize(d_bar);
        progressBar.setMinimumSize(d_bar);
        progressBar.setPreferredSize(d_bar);
        
        iconPanel = new JPanel(new GridLayout(1, 3));
        redIcon = tool.createImageIcon(RED_IMAGE);
        greenIcon = tool.createImageIcon(GREEN_IMAGE);
        blueIcon = tool.createImageIcon(BLUE_IMAGE);
        yellowIcon = tool.createImageIcon(YELLOW_IMAGE);
        blackIcon = tool.createImageIcon(BLACK_IMAGE);

        leftIconLabel = new JLabel(blackIcon);
        centerIconLabel = new JLabel(blackIcon);
        rightIconLabel = new JLabel(blackIcon);
        iconPanel.add(leftIconLabel);
        iconPanel.add(centerIconLabel);
        iconPanel.add(rightIconLabel);
        
        optionPanel = new JPanel(new BorderLayout());

        optionPanel.add(progressBar, BorderLayout.CENTER);
        optionPanel.add(iconPanel, BorderLayout.EAST);

        statusBar.add(messageBar, BorderLayout.WEST);
        statusBar.add(optionPanel, BorderLayout.EAST);

    }

    public void windowOpened(InternalWindow window) {
        if (window == null) return;
        if (window.isSelected()) {
            this.changeStatus(Color.GREEN); 
        } else {
            this.changeStatus(Color.BLACK);
        }
    }

    public void windowClosed(InternalWindow window) {
        if (window == null) return;

        if (view.getCurrentDeskTopPane().getSelectedWindow() == null
                	|| view.getCurrentDeskTopPane().getSelectedWindow() == window) {            
            this.changeStatus(Color.BLACK);
        
        } else if (view.getCurrentDeskTopPane().getWindowCount() == 0) {
            this.changeStatus(Color.BLACK);
            
        } else {
            this.changeStatus(Color.GREEN);
        }
    }

    public void windowSelected(InternalWindow window) {
        if (window.isSelected()) {
            this.changeStatus(Color.GREEN);
        } else {
            this.changeStatus(Color.BLACK);
        }
    }
    
    public void tabCreated(String title) {
        this.changeStatus(Color.BLACK);
    }
    
    public void tabChanged(String title) {
        this.changeStatus(Color.YELLOW);
    }
    
    public void tabClosed(String title) {
        this.changeStatus(Color.BLACK);
    }

    public void show() {
        Switch barSwitch = view.CommandBuilder.getStatusBarSwitch();
        this.changeStatus(Color.BLACK);
        
        if (barSwitch != null) {
            statusBar.setVisible(barSwitch.isSelected());
        } else {
            statusBar.setVisible(true);
        }
    }

    JPanel Bar() {
        return statusBar;
    }

    void setIndeterminatedProgress(boolean aFlag) {
        progressBar.setIndeterminate(aFlag);
    }
    
    /**
     * Sets a message on the StatusBar
     * 
     * @param message -
     *                      a message to be displayed on StatusBar.
     */
    void setMessage(String message) {
        messageBar.setText(" " + message);
    }

    /**
     * Chages Status Icon on the StatusBar.
     * 
     * @param color - Color object
     *                      the color you can choose is as follows <br>
     *                      Color.GREEN: left signal gets Green. <br>
     *                      Color.BLUE: left signal gets Blue. <br>
     *                      Color.RED: right signal gets Red. <br>
     *                      Color.YELLOW: right signal gets Yellow. <br>
     *                      Color.BLACK: both signals gets Black. <br>
     */
    void changeStatus(Color color) {
        if (color == null) {
            leftIconLabel.setIcon(blackIcon);

        } else if (color == Color.GREEN) {
            centerIconLabel.setIcon(greenIcon);
            rightIconLabel.setIcon(blackIcon);

        } else if (color == Color.RED) {
            centerIconLabel.setIcon(blackIcon);
            rightIconLabel.setIcon(redIcon);
        
        } else if (color == Color.BLUE) {
            leftIconLabel.setIcon(blueIcon);
        
        } else if (color == Color.YELLOW) {
            centerIconLabel.setIcon(yellowIcon);
            rightIconLabel.setIcon(blackIcon);
        
        } else if (color == Color.BLACK) {
            centerIconLabel.setIcon(blackIcon);
            rightIconLabel.setIcon(blackIcon);

        } else {
            leftIconLabel.setIcon(blackIcon);
            centerIconLabel.setIcon(blackIcon);
            rightIconLabel.setIcon(blackIcon);
        }
        
        iconPanel.repaint();
    }
}