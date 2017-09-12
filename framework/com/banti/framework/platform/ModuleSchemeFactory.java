package com.banti.framework.platform;

import com.banti.framework.platform.module.EditModuleScheme;
import com.banti.framework.platform.module.FileModuleScheme;
import com.banti.framework.platform.module.HelpModuleScheme;
import com.banti.framework.platform.module.OptionModuleScheme;
import com.banti.framework.platform.module.ToolModuleScheme;
import com.banti.framework.platform.module.ViewModuleScheme;
import com.banti.framework.platform.module.WindowModuleScheme;

/**
 * This class is an interface that represents <i>"AbstractFactory"</i> pattern. 
 * ModuleSchemeFactory creates; <br>
 * <a href="module/FileModuleScheme.html"><i>FileModuleScheme</i></a> by "createFileModuleScheme()" method,<br>
 * <a href="module/EditModuleScheme.html"><i>EditModuleScheme</i></a> by "createEditModuleScheme()" method,<br>
 * <a href="module/ViewModuleScheme.html"><i>ViewModuleScheme</i></a> by "createViewModuleScheme()" method,<br>
 * <a href="module/ToolModuleScheme.html"><i>ToolModuleScheme</i></a> by "createToolModuleScheme()" method,<br>
 * <a href="module/OptionModuleScheme.html"><i>OptionModuleScheme</i></a> by "createOptionModuleScheme()" method,<br>
 * <a href="module/WindowModuleScheme.html"><i>WindowModuleScheme</i></a> by "createWindowModuleScheme()" method,<br>
 * <a href="module/HelpModuleScheme.html"><i>HelpModuleScheme</i></a> by "createHelpModuleScheme()" method
 * <br>
 * <p> A concrete factory creates and returns the respective Module Schemes, which will be modified and
 * arranged according to a developper's requirements.
 */
public interface ModuleSchemeFactory {
    
    /**
     * Creates and Returns your own <i>FileModuleScheme</i>.
     * 
     * @return concrete FileModuleScheme instance.
     * 
     * @see com.cysols.framework.platform.module.FileModuleScheme
     */
    public FileModuleScheme createFileModuleScheme();
    
    /**
     * Creates and Returns your own <i>EditModuleScheme</i>.
     * 
     * @return concrete EditModuleScheme instance.
     * 
     *  @see com.cysols.framework.platform.module.EditModuleScheme
     */
    public EditModuleScheme createEditModuleScheme();

    /**
     * Creates and Returns your own <i>ViewModuleScheme</i>.
     * 
     * @return concrete ViewModuleScheme instance.
     * 
     *  @see com.cysols.framework.platform.module.ViewModuleScheme
     */
    public ViewModuleScheme createViewModuleScheme();

    /**
     * Creates and Returns your own <i>ToolModuleScheme</i>.
     * 
     * @return concrete ToolModuleScheme instance.
     * 
     *  @see com.cysols.framework.platform.module.ToolModuleScheme
     */
    public ToolModuleScheme createToolModuleScheme(); 

    /**
     * Creates and Returns your own <i>OptionModuleScheme</i>.
     * 
     * @return concrete OptionModuleScheme instance.
     * 
     *  @see com.cysols.framework.platform.module.OptionModuleScheme
     */
    public OptionModuleScheme createOptionModuleScheme();
    
    /**
     * Creates and Returns your own <i>WindowModuleScheme</i>.
     * 
     * @return concrete WindowModuleScheme instance.
     * 
     *  @see com.cysols.framework.platform.module.WindowModuleScheme
     */
    public WindowModuleScheme createWindowModuleScheme();
    
    /**
     * Creates and Returns your own <i>HelpModuleScheme</i>.
     * 
     * @return concrete HelpModuleScheme instance.
     * 
     *  @see com.cysols.framework.platform.module.HelpModuleScheme
     */
    public HelpModuleScheme createHelpModuleScheme();
}
