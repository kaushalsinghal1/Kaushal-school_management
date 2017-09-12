package com.banti.framework.platform.theme;

import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.DefaultMetalTheme;

import com.banti.framework.theme.ToolKit;

/**
 * 
 */
public class ContrastTheme  extends DefaultMetalTheme {
    private final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);
    private final ColorUIResource primary2 = new ColorUIResource(200, 200, 200);
    private final ColorUIResource primary3 = new ColorUIResource(255, 255, 255);

    private final ColorUIResource primaryHighlight = new ColorUIResource(100,100,100);
    private final ColorUIResource secondary2 = new ColorUIResource(200, 200, 200);
    private final ColorUIResource secondary3 = new ColorUIResource(255, 255, 255);
    private final ColorUIResource controlHighlight = new ColorUIResource(100,100,100);
    
    /* (non-Javadoc)
     * @see javax.swing.plaf.metal.MetalTheme#getName()
     */
    public String getName() {
        ToolKit tool = ToolKit.getInstance();
        return tool.getString("CONTRAST");
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

    public ColorUIResource getPrimaryControlHighlight() { 
        return controlHighlight;
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
    
    public ColorUIResource getControlHighlight() {
        return super.getSecondary3();
    }

    public ColorUIResource getFocusColor() { 
        return super.getBlack();
    }

    public ColorUIResource getTextHighlightColor() { 
        return super.getBlack();
    }
    public ColorUIResource getHighlightedTextColor() {
        return super.getWhite();
    }  
    public ColorUIResource getMenuSelectedBackground() { 
        return super.getBlack();
    }    
    public ColorUIResource getMenuSelectedForeground() { 
        return super.getWhite();
    }    
    public ColorUIResource getAcceleratorForeground() { 
        return super.getBlack();
    }
    public ColorUIResource getAcceleratorSelectedForeground() { 
        return super.getWhite();
    }

    public void addCustomEntriesToTable(UIDefaults table) {

        Border blackLineBorder = new BorderUIResource(new LineBorder( super.getBlack() ));
        Border whiteLineBorder = new BorderUIResource(new LineBorder( super.getWhite() ));

	Object textBorder = new BorderUIResource( new CompoundBorder(
						       		blackLineBorder,
					               new BasicBorders.MarginBorder()));

        table.put( "ToolTip.border", blackLineBorder);
        table.put( "TitledBorder.border", blackLineBorder);
        table.put( "Table.focusCellHighlightBorder", whiteLineBorder);
        table.put( "Table.focusCellForeground", getWhite());

        table.put( "TextField.border", textBorder);
        table.put( "PasswordField.border", textBorder);
        table.put( "TextArea.border", textBorder);
        table.put( "TextPane.font", textBorder);
    }

}
