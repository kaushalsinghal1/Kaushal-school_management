package com.banti.framework.platform.theme;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

import com.banti.framework.theme.ToolKit;

/**
 * TODO
 */
public class EmeraldTheme extends DefaultMetalTheme {
    private final ColorUIResource primary1 = new ColorUIResource(50, 100, 50);
    private final ColorUIResource primary2 = new ColorUIResource(100, 150, 100);
    private final ColorUIResource primary3 = new ColorUIResource(150, 200, 150);
    
    
    /* (non-Javadoc)
     * @see javax.swing.plaf.metal.MetalTheme#getName()
     */
    public String getName() {
        ToolKit tool = ToolKit.getInstance();
        return tool.getString("EMERALD");
    }
    /* (non-Javadoc)
     * @see javax.swing.plaf.metal.MetalTheme#getPrimary1()
     */
    protected ColorUIResource getPrimary1() {
        return primary1;
    }
    /* (non-Javadoc)
     * @see javax.swing.plaf.metal.MetalTheme#getPrimary2()
     */
    protected ColorUIResource getPrimary2() {
        return primary2;
    }
    /* (non-Javadoc)
     * @see javax.swing.plaf.metal.MetalTheme#getPrimary3()
     */
    protected ColorUIResource getPrimary3() {
        return primary3;
    }
}
