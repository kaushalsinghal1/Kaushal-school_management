package com.banti.framework.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Loads the resources class according to the given locale and the class file.
 * This class is a singletun class, getString method of this class is used to
 * get the value for the specified key of the resources.
 * 
 * How to use it ?<code><pre>
 * String consoleResources = &quot;pakcgae.resource.MyResources&quot;;ResourceBundle resourceBundle =  I18NManager.getI18NManager(
 *  Locale.getDefault(), consoleResources)
 *  .getResourceBundle();
 *  String labelVal = resourceBundle.getString(&quot;NAME&quot;);
 *   
 * </pre></code>
 */

public class I18NManager {

	public static final String DEFAULT_ENCODING = "SJIS";

	public static final String FILE_ENCODING = "file.encoding";

	public static String getFileEncoding() {
		return getFileEncoding(DEFAULT_ENCODING);
	}

	public static String getFileEncoding(String defaultEncoding) {
		String fileEncoding = System.getProperty(FILE_ENCODING);
		if (fileEncoding == null) {
			fileEncoding = defaultEncoding;
		}
		return fileEncoding;
	}

	private static Map resourcesMap = new HashMap();
	private ResourceBundle messages;

	/** Constructor */
	private I18NManager(Locale currentLocale, String classFile) {
		messages = ResourceBundle.getBundle(classFile, currentLocale);
	}

	/**
	 * Method gets the instance of I18NManager.
	 * 
	 * @param language
	 * @param country
	 * @param classFile
	 * @return I18NManager
	 */
	public static I18NManager getI18NManager(String language, String country,
			String classFile) {
		return getI18NManager(new Locale(language, country), classFile);
	}

	/**
	 * Method gets the instance of I18NManager.
	 * 
	 * @param currentLocale
	 * @param classFile
	 * @return I18NManager
	 */
	public static I18NManager getI18NManager(Locale currentLocale,
			String classFile) {
		String key = currentLocale.getDisplayLanguage() + "," + classFile;
		if (resourcesMap.containsKey(key)) {
			return (I18NManager) resourcesMap.get(key);
		}

		I18NManager i18nmanager = new I18NManager(currentLocale, classFile);
		resourcesMap.put(key, i18nmanager);
		return i18nmanager;
	}

	/**
	 * Gets the ResourceBundle for a particular I18NManager To get the local
	 * string, call getString(key) method on ResourceBundle.
	 * 
	 * @return ResourceBundle
	 */
	public ResourceBundle getResourceBundle() {
		return messages;
	}

	/** Gets the resource stroing from the bundel */
	public String getString(String key) {
		try {
			return messages.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * The tag specified in { } will be replaced by the value. For Example : In
	 * Put ------- [Message: en] Do you really want to delete this server:
	 * {IPADDRESS} ? [Message: ja_JP] Server: {IPADDRESS} wo honntouni
	 * sakujoshimasuka?
	 * 
	 * Usage ------ String inPutMessage =
	 * resourceBundle.getString("DELETE_SERVER"); String outputMessage =
	 * I18NManager.replaceTag( inPutMessage, "IPADDRESS", "192.168.0.1");
	 * 
	 * Out put ------- [Message: en] Do you really want to delete this server:
	 * 192.168.0.1 ? [Message: ja_JP] Server: 192.168.0.1 wo honntouni
	 * sakujoshimasuka?
	 * 
	 * @param message
	 *            - input string to be modified
	 * @param tag
	 *            - string which has to be replaced by the value
	 * @param value
	 *            - string which has to be writen in the place of tag
	 * @return - returns message withour change if replace operation fails.
	 */
	public static String replaceTag(String message, String tag, String value) {
		if (message == null || tag == null || value == null) {
			return message;
		}
		tag = "{" + tag + "}";

		int index = message.indexOf(tag);
		if (index >= 0) {
			int len = tag.length();
			message = (message.substring(0, index) + value + message
					.substring(index + len));
		}
		return message;
	}
}