package com.banti.framework.platform.peer;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JDialog;

import javax.swing.border.Border;

public class SplashScreen {
    private ImageIcon mainImageIcon;
    private JPanel optionPanel;
    private JFrame parent;
    private JDialog splashScreen = null;

    private boolean mode;

  public SplashScreen(JFrame parent, ImageIcon mainImage, JPanel optionPanel) {
      this(parent, mainImage, optionPanel, true);
  }

  public SplashScreen(JFrame parent, ImageIcon mainImage, JPanel optionPanel, boolean mode) {
      this.parent = parent;
      this.mainImageIcon = mainImage;
      this.optionPanel = optionPanel;
      this.mode = mode;
  }
  /**
   * This method will display the message.
   *
   * @param  void
   * @return void
   */
  public void show() {
      	JLabel splashLabel = null;

        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);

        splashScreen = new JDialog(parent, mode);
        splashScreen.setUndecorated(true);
        splashScreen.getContentPane().setLayout(new BorderLayout());

        try {
            
            if (mainImageIcon != null) 
                splashLabel = new JLabel(mainImageIcon);

            if (splashLabel == null) return;
            
            splashLabel.setBorder(compound);
            splashScreen.getContentPane().add(splashLabel, BorderLayout.CENTER);
         

            if (optionPanel != null) {
                optionPanel.setBorder(BorderFactory.createRaisedBevelBorder());
                splashScreen.getContentPane().add(optionPanel, BorderLayout.SOUTH);
            }
            
            splashScreen.pack();

            if (parent != null) {
                splashScreen.setLocationRelativeTo(parent);
                /*
                Rectangle screenRect = parent.getGraphicsConfiguration().getBounds();
                int x = screenRect.x + screenRect.width / 2 - splashScreen.getSize().width / 2;
                int y = screenRect.y + screenRect.height / 2 - splashScreen.getSize().height / 2;
                splashScreen.setLocation(x, y);
                */
            }
            
            splashScreen.setVisible(true);
            
        } catch (Exception e) {
            return;
        }
    }

  public void hideSplashScreen() {
    splashScreen.setVisible(false);
    splashScreen.dispose();
  }
}

