package com.banti.framework.theme;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.Action;
import javax.swing.ImageIcon;


import com.banti.framework.utils.I18NManager;

/**
 * TODO
 */
public class ToolKit {

    private Toolkit toolkit;

    private static I18NManager i18nManager;
    private static String RESOURCE_CLASS = "com.banti.framework.resources.Resources";
    
    private static ToolKit thisClass = null;
    
    private ToolKit() {
        toolkit = Toolkit.getDefaultToolkit();
    }
    
    public static ToolKit getInstance() {
        if (thisClass == null) thisClass = new ToolKit();
        return thisClass;
    }
        
    public static String getString(String key) {
        if (i18nManager == null) 
            i18nManager = I18NManager.getI18NManager(Locale.getDefault(), RESOURCE_CLASS);
        
        if (key == null) return "";
        
        try {
            return i18nManager.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }
    
    public static String getString(String key, String tag, String value) {
        if (i18nManager == null) 
            i18nManager = I18NManager.getI18NManager(Locale.getDefault(), RESOURCE_CLASS);

        if (key == null) return "";
        
        try {
            return replace(i18nManager.getString(key), tag, value);
        } catch (MissingResourceException e) {
            return key;
        }
    }
    
	public Dimension getScreenSize() {
		return toolkit.getScreenSize();
	}

    public static String replace(String message, String tag, String value) {
        if (i18nManager == null) 
            i18nManager = I18NManager.getI18NManager(Locale.getDefault(), RESOURCE_CLASS);

        return i18nManager.replaceTag(message, tag, value);
    }
    

    public Image createImage(URL url) {
        try {
            return toolkit.createImage(url);
        } catch (Exception e) {
            return null;
        }
    }
    
    public Image createImage(String imagePath) {
        try {
            URL url = this.getClass().getResource(imagePath);
            if (url == null) 
                	return toolkit.createImage(imagePath);    

            return toolkit.createImage(url);
        } catch (Exception e) {
            return null;
        }
    }
    
    public ImageIcon createImageIcon(URL url) {
        try {
            return new ImageIcon(url);
        } catch (Exception e) {
            return null;
        }
    }
    
    public ImageIcon createImageIcon(String imagePath) {
        try {
            URL url = this.getClass().getResource(imagePath);
            if (url == null)
                	return new ImageIcon(imagePath);

            return new ImageIcon(url);

        } catch (Exception e) {
            return null;
        }
    }
    
    /*public Action createAction(Command cmd) {
        if (cmd == null) return null;
        return cmd.new RuntimeAction(cmd); 
    }*/
}
