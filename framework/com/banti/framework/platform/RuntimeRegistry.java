package com.banti.framework.platform;

/**
 * RuntimeRegistry gives you to register/unregister special plugin command/module
 * and to load them dynamically from Jar files.
 * It is possible to obtain RuntimeRegistry concrete instance by 
 * "<a href="Application.html#getRuntimeRegistry()">Application.getRuntimeRegistyr()</a>" method.
 * After it is obtained, Plug-in is registered by the following way;<br>
 * <pre>
 *   class YourApplication exntends Application {
 *
 *        aMethod() {       
 *          RuntimeRegistry registry = super.getRuntimeRegistry();
 *          registry.register(new YourPluginCommand());
 *          registry.register(new YourPluginModule());
 *      	}
 *     }
 * </pre>
 *  
 * This is a static registration. On the other hand, RuntimeRegistry can load Plug-in dynamically
 * from Jar files. Then, <a href="PlugInBuilder.html">PluginBuilder</a> is instantiated by
 * Class.newInstance() method and the respective Plug-in is created by PluginBuilder.<br>
 * <br>
 * When a Jar file is put under &lt:base.dir&gt;/plugin/ and the following definition is contaned
 * in a manifest file in the jar,<br>
 * -------<br>
 * <b>
 * Name: Plugin<br>
 * PluginBuilder: com.yourcompany.YourPluginBuilder</br>
 * </b>
 * -------<br>
 * <br>
 * 
 * Then, the next program loads <i>YourPluginBuilder</i> from a Jar file under "&lt;base.dir&gt;/plugin/"
 * directory, and the plugged command which are obtained from "YourPluginBuilder.fetch()"
 * method are listed in Tool menu.<br>
 * <pre>
 *   class YourApplication exntends Application {
 *
 *        aMethod() {       
 *          RuntimeRegistry registry = super.getRuntimeRegistry();
 *          registry.addLoadClassPath("plugin/");
 *          registry.loadFromManifest();
 *     	   } 
 *     }
 * </pre>
 * 
 * Or RuntimeRegistry can load the same way if your concrete PluginBuilder is named
 * <i>Any</i>PluginBuilder and is included in a Jar file under the added path.
 * "loadFromJarEntries()" method seeks <i>Any</i>PluginBuilder class from all Jar files
 * under the specified directory.
 */
public interface RuntimeRegistry {

    /**
     *  Registers PluginCommand instance.
     * The registered command is shown in "Tool" menu.
     * 
     * @param command PluginCommand concrete instance.
     */
    public void register(PluginCommand command);
    
    /**
     * Unregisters PluginCommand instance.
     * The command gets unvisible in "TooL" menu and is removed from
     * this registry.
     * 
     * @param command PluginCommand concrete instance to be removed.
     */
    public void unregister(PluginCommand command);

    /**
     *  Registers PluginModule instance.
     * The retistered module is shown between "Tool" and "Option".
     * 
     * @param module PluginModule concrete instance.
     */
    public void register(PluginModule module);
    
    /**
     * Unregisters PluginModule instance.
     * The module gets unvisible on the menu and is removed from 
     * this registry.
     * 
     * @param module PluginModule concrete instance to be removed.
     */
    public void unregister(PluginModule module);
    
    /**
     * Adds the path of Jar file. RuntimeRegistry seeks Jar files from the added paths when
     * loading Jar files.
     * 
     * @param classpath the file path under which Jar files are put.
     */
    public void addLoadClassPath(String classpath);
    
    /**
     * Returns the added file pathes.
     * 
     * @return the file path which RuntimeRegistry seeks Jar files from.
     */
    public String getLoadClassPath();
    
    /**
     * Loads Plug-in from Jar files based on the plugin definition in Jar files.
     * This method must be called after one or more file path of Jar files are registered by
     * "addLoadClassPath()" method. This method seeks Jar files from the file pathes
     * and reads their manifests. Based on the manifests, RuntimeRetistry instantiates
     * <a href="PlugInBuilder.html">PluginBuilder</a> and loads Plug-in by using
     * "<a href="PluginBuilder.html#fetch()">PluginBuilder.fetch()</a>" method.<br>
     * <br>
     * For loading dynamically, the following contents must be included in the manifest.<br>
     * <br>
     * -------<br>
     * <b>
     * Name: Plugin<br>
     * PluginBuilder: com.yourcompany.YourPluginBuilder</br>
     * </b>  
     * -------<br>
     * "Plugin" is a section name, and "PluginBuilder" is an attribute name. 
     * The other section name is available by setting the specified name through 
     * "setSectionName()" and "setAttributeName()".
     * 
     * @return true if all found PluginBuilders are instantiated. false if one or more failures occur.
     */
    public boolean loadFromManifest();
    
    /**
     * RuntimeRegistry can load PluginBuilder from Jar files if your concrete PluginBuilder 
     * is named<i>Any</i>PluginBuilder and is included in a Jar file under the added path.
     * "loadFromJarEntries()" method seeks <i>Any</i>PluginBuilder class from all Jar files
     * under the specified directory.
     * 
     * @return true if *PluginBuilder class is found from Jar files in the added path and
     * are instantiated successfully.
     */
    public boolean loadFromJarEntries();
    
    /**
     * Sets a specified section name, which is used when "loadFromManifest()" method
     * is called. The default section name is "<b>Plugin</b>".
     * 
     * @param sectionName a specified section name in a manifest.
     */
    public void setSectionName(String sectionName);
    
    /**
     * Sets a specified attribute name, which is used when "loadFromManifest()" method
     * is called. The default attribute name is "<b>PluginBuilder</b>".
     * 
     * @param attributeName a specified attribute name in a manifest.
     */
    public void setAttributName(String attributeName);
}
