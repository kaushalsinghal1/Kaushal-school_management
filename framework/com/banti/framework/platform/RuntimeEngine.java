package com.banti.framework.platform;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.banti.framework.platform.Command.RuntimeAction;

/**
 * TODO
 */
 final class RuntimeEngine implements RuntimeRegistry {

    public static final String SECTION_NAME = "Plugin";
    public static final String ATTRIBUTE_NAME = "PluginBuilder";
    
    private ArrayList pluginCmdList;
    private ArrayList pluginModuleList;
    private String loadPath;
    
    private String sectionName;
    private String attributeName;    
    
    RuntimeEngine() {
        pluginCmdList = new ArrayList();
        pluginModuleList = new ArrayList();
        loadPath = ".";
        sectionName = SECTION_NAME;
        attributeName = ATTRIBUTE_NAME;
    }

    /**
     *  Registers PluginCommand concrete instance.
     * The registered command is shown in "Tool" menu.
     * 
     * @param command - PluginCommand concrete instance.
     * @see com.cysols.framework.platform.RuntimeRegistry#register(com.cysols.framework.platform.PluginCommand)
     */
    public void register(PluginCommand command) {
        if (command == null) return;
        
        synchronized (pluginCmdList) {
            pluginCmdList.add(command);
        }
    }

    /**
     * Unregisters PluginCommand instance.
     * The command gets unvisible in "TooL" menu and is removed from
     * this registry.
     * 
     * @param command
     * @see com.cysols.framework.platform.RuntimeRegistry#unregister(com.cysols.framework.platform.PluginCommand)
     */
    public void unregister(PluginCommand command) {
        if (command == null) return;
        
        command.setVisible(false);
        synchronized (pluginCmdList) {
            pluginCmdList.remove(command);
        }
    }    
    
    /**
     *  Registers PluginModule instance.
     * The retistered module is shown between "Tool" and "Option".
     * 
     * @param module - PluginModule concrete instance.
     * @see com.cysols.framework.platform.RuntimeRegistry#register(com.cysols.framework.platform.PluginModule)
     */
    public void register(PluginModule module) {
        if (module == null) return;
        
        synchronized(pluginModuleList) {
            pluginModuleList.add(module);
        }
    }

    /**
     * Unregisters PluginModule instance.
     * The module gets unvisible on the menu and is removed from 
     * this registry.
     * 
     * @param module - PluginModule concrete instance to be removed.
     * @see com.cysols.framework.platform.RuntimeRegistry#unregister(com.cysols.framework.platform.PluginModule)
     */
    public void unregister(PluginModule module) {
        if (module == null) return;
        
        module.setVisible(false);
        synchronized(pluginModuleList) {
            pluginModuleList.remove(module);
        }
    }

    /* (non-Javadoc)
     * @see com.cysols.framework.platform.RuntimeRegistry#addLoadClassPath(java.lang.String)
     */
    public void addLoadClassPath(String classpath) {
        loadPath = loadPath + File.pathSeparatorChar +  classpath;
    }

    /* (non-Javadoc)
     * @see com.cysols.framework.platform.RuntimeRegistry#getLoadClassPath()
     */
    public String getLoadClassPath() {
        return loadPath;
    }


    /* (non-Javadoc)
     * @see com.cysols.framework.platform.RuntimeRegistry#loadFromManifest()
     */
    public boolean loadFromManifest() {
        boolean status = false;
      /*  ManifestPluginLoader loader = new ManifestPluginLoader();

        String classPathes = this.getLoadClassPath();
        String pathSeparator = "" + File.pathSeparatorChar;
        StringTokenizer st = new StringTokenizer(classPathes, pathSeparator);
        
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            loader.addPluginDir(new File(path));
        }

        List builders = loader.findPlugins(sectionName, attributeName);
        
        if (builders == null || builders.size() ==0) {
            return false;
        }
        
        PluginBuilder builder;
        Iterator itr = builders.iterator();
        while(itr.hasNext()) {
            try {
                builder = (PluginBuilder) itr.next();
                builder.initialize();
                PluginCommand[] cmds = builder.fetch();
                if (cmds == null || cmds.length == 0) continue;
                for (int i = 0; i < cmds.length; i++) {
                    if (cmds[i] instanceof PluginModule && cmds[i].getOrder() > 1000) {
                        PluginModule pModule = (PluginModule) cmds[i];
                        Command[] commands = pModule.getCommands();
                        commands[0].setSeparator(true);
                        this.register(pModule);
                    } else {
                        this.register(cmds[i]);
                    }
                        status = true;
                }
            } catch (ClassCastException e) {
                continue;
            }
        }
        */
        return status;
    }

    /* (non-Javadoc)
     * @see com.cysols.framework.platform.RuntimeRegistry#loadFromJarFile()
     */
    public boolean loadFromJarEntries() {
        boolean status = false;

       /* String classPathes = this.getLoadClassPath();
        String pathSeparator = "" + File.pathSeparatorChar;
        StringTokenizer st = new StringTokenizer(classPathes, pathSeparator);
        
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            File dir = new File(path);
            String[] jarfiles = dir.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    File file = new File(dir.getAbsolutePath() + "/" + name);
                    if (file.isDirectory()) {
                        return false;
                    }
                    return (name.endsWith(".jar"));
                }
            });

            for (int i = 0; i < jarfiles.length; i++) {
                File jar = new File(path + "/" + jarfiles[i]);
                JarFile jarFile;
                try {
                	 jarFile = new JarFile(path + "/" + jarfiles[i]);
                } catch (IOException e) {
                    continue;
                }
                Enumeration enu = jarFile.entries();
                	
                while (enu.hasMoreElements()) {
                    ZipEntry ze = (ZipEntry) enu.nextElement();
                    String packageTree = ze.getName();
                    if (packageTree.indexOf( "PluginBuilder.class") > -1) {                         
                        packageTree = packageTree.substring(0, packageTree.lastIndexOf(".class"));
                        packageTree = packageTree.replaceAll("[\\/\\\\]", "\\.");
                        
                        if (packageTree.equalsIgnoreCase("com.cysols.framework.platform.PluginBuilder")) {
                            continue;
                        }
                
                        try {
                            URL[] url = new URL[1];
                            url[0] = jar.toURL();
                            ClassLoader loader = new URLClassLoader(url);
                            Class c = loader.loadClass(packageTree);
                            PluginBuilder builder = (PluginBuilder) c.newInstance();
                            builder.initialize();
                            PluginCommand[] cmds = builder.fetch();
                            if (cmds == null || cmds.length == 0) continue;
                            for (int j = 0; i < cmds.length; j++) {
                                if (cmds[i] instanceof PluginModule && cmds[i].getOrder() > 1000) {
                                    PluginModule pModule = (PluginModule) cmds[i];
                                    Command[] commands = pModule.getCommands();
                                    commands[0].setSeparator(true);
                                    this.register(pModule);
                                } else {
                                    this.register(cmds[j]);
                                }
                                status = true;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    } // End of (if package includes "PluginBuilder.class")
                } // End of while roop in jar
            } // End of for roop by jar files
        } // End of while roop by load path
        */
        return status;
    }


    /* (non-Javadoc)
     * @see com.cysols.framework.platform.RuntimeRegistry#setSectionName(java.lang.String)
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;        
    }

    /* (non-Javadoc)
     * @see com.cysols.framework.platform.RuntimeRegistry#setAttributName(java.lang.String)
     */
    public void setAttributName(String attributeName) {
        this.attributeName = attributeName;        
    }

    
    // -----------non-public methods ---------------------------------------------------------------
    void activateToolModule(MenuBar menuBar, ToolModule module) {        
        PluginCommand[] cmds = this.getPluggedCommands();
        for (int i = 0; i < cmds.length; i++) {
            this.activate(cmds[i]);
            module.addCommand(cmds[i]);
        }
        
        module.accept(menuBar);
    }
    
    void activatePluggedModule(MenuBar menuBar) {        
        PluginModule[] modules = this.getPluggedModules();
        for (int i = 0; i < modules.length; i++) {
            Activator activator = modules[i].getActivator();
            modules[i].setVisible(activator.isActivated());            
            modules[i].accept(menuBar);
        }
    }

    void activate(PluginCommand command) {
        Activator activator = command.getActivator();
     //   command.setVisible(activator.isActivated());
        RuntimeAction action = command.getRuntimeAction();
        action.plug(this);
    }
    
    
    void execute(ActionEvent ae) {
        PluginCommand command = (PluginCommand) ae.getSource();
        Activator activator = command.getActivator();
        if (activator.isActivated()) {
            command.entryPoint(ae);
        } else {
           
        }
    }
    
    PluginCommand[] getPluggedCommands() {
        if (pluginCmdList.size() == 0) {
            return new PluginCommand[0];
        }
    
        Collections.sort(pluginCmdList, new InnerPluginCommandComparator());
        PluginCommand[] cmds = new PluginCommand[pluginCmdList.size()];
        for (int i = 0; i < pluginCmdList.size(); i++) {
            cmds[i] = (PluginCommand) pluginCmdList.get(i);
        }
        
        return cmds;
    }
    
    PluginModule[] getPluggedModules() {
        if (pluginModuleList.size() == 0) {
            return new PluginModule[0];
        }
        
        Collections.sort(pluginModuleList, new InnerPluginCommandComparator());
        PluginModule[] modules = new PluginModule[pluginModuleList.size()];
        for (int i = 0; i < pluginModuleList.size(); i++) {
            modules[i] = (PluginModule) pluginModuleList.get(i);
        }
        
        return modules;
    }

    //-----------Inner Classes ------------------------------------------------------------------------
    
    // ---------- Inner PluginCommand Comparator class -----------------------------------
    private class InnerPluginCommandComparator implements Comparator {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
            PluginCommand cmd1 = (PluginCommand) o1;
            PluginCommand cmd2 = (PluginCommand) o2;
            
            if (cmd1.getOrder() == cmd2.getOrder()) {
                return 0;
            } else if (cmd1.getOrder() < cmd2.getOrder()) {
                return -1;
            } else {
                return 1;
            }
        }
        
    } // End of Inner PluginCommand Comparator class

} // End of RuntimeEngine class