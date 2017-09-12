package com.school.resource;

import java.util.Locale;
import java.util.MissingResourceException;

import com.banti.framework.utils.I18NManager;

public class ResourcesUtils {
	 private static I18NManager i18nManager;
	    private static String RESOURCE_CLASS = "com.school.resource.Resources";
	    
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
	    
	    public static String replace(String message, String tag, String value) {
	        if (i18nManager == null) 
	            i18nManager = I18NManager.getI18NManager(Locale.getDefault(), RESOURCE_CLASS);

	        return i18nManager.replaceTag(message, tag, value);
	    }
	    
}
