package com.banti.framework.platform.module;

import com.banti.framework.platform.ModuleSchemeFactory;


/**
 * 
 */
public class DefaultModuleSchemeFactory implements ModuleSchemeFactory {
    
    public DefaultModuleSchemeFactory() {
        super();
    }
    
    public FileModuleScheme createFileModuleScheme() {
        return new DefaultFileModuleScheme();
    }
    
    public EditModuleScheme createEditModuleScheme() {
        return new DefaultEditModuleScheme();
    }
    
    public ViewModuleScheme createViewModuleScheme() {
        return  new DefaultViewModuleScheme();
    }

    public ToolModuleScheme createToolModuleScheme() {
        return new DefaultToolModuleScheme();
    }
    
    public OptionModuleScheme createOptionModuleScheme() {
        return new DefaultOptionModuleScheme();
    }
    
    public WindowModuleScheme createWindowModuleScheme() {
        return new DefaultWindowModuleScheme();
    }

    public HelpModuleScheme createHelpModuleScheme() {
        return new DefaultHelpModuleScheme();
    }
    
}
