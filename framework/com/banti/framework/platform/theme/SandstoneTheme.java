/*
 * Copyright (c) Cyber Solutions Inc.
 * All Rights Reserved.
 * 
 * Created on 2004/08/22
 */
package com.banti.framework.platform.theme;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

import com.banti.framework.theme.ToolKit;

public class SandstoneTheme  extends DefaultMetalTheme {
    
    private final ColorUIResource primary1 = new ColorUIResource(90, 90, 50);
    private final ColorUIResource primary2 = new ColorUIResource(160, 150, 110);
    private final ColorUIResource primary3 = new ColorUIResource(200, 180, 140);
    
    private final ColorUIResource secondary1 = new ColorUIResource( 110,  110,  110);
    private final ColorUIResource secondary2 = new ColorUIResource(160, 160, 160);
    private final ColorUIResource secondary3 = new ColorUIResource(230, 220, 180);

    /* (non-Javadoc)
     * @see javax.swing.plaf.metal.MetalTheme#getName()
     */
    public String getName() {
        ToolKit tool = ToolKit.getInstance();
        return tool.getString("SANDSTONE");
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

    /* (non-Javadoc)
     * @see javax.swing.plaf.metal.MetalTheme#getSecondary1()
     */
    protected ColorUIResource getSecondary1() {
        return secondary1;
    }
    /* (non-Javadoc)
     * @see javax.swing.plaf.metal.MetalTheme#getSecondary2()
     */
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }
    /* (non-Javadoc)
     * @see javax.swing.plaf.metal.MetalTheme#getSecondary3()
     */
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }
}
