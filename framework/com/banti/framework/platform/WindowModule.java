package com.banti.framework.platform;

import java.util.ArrayList;

import com.banti.framework.platform.module.WindowModuleScheme;

/**
 * TODO
 */
final class WindowModule extends Module {

    private Command cascade;
    private Command tile;
    private Command iconify;
    private Command deiconify;
    private Command maximize;
    private Command allclose;
    
    private ArrayList cmdList;
    
    public WindowModule(WindowModuleScheme winScheme, ViewMediator view) {
        super(winScheme.WINDOW_MENU_NAME);
        
        this.setDescription(winScheme.WINDOW_DESCRIPTION);
        this.setVisible(winScheme.WINDOW_VISIBLE);
        this.setEnabled(winScheme.WINDOW_ENABLE);
        this.setMnemonic(winScheme.WINDOW_MNEMONIC);

       cascade =  view.CommandBuilder.new CascadeArrange(winScheme.CASCADE_MENU_NAME);
       cascade.setDescription(winScheme.CASCADE_DESCRIPTION);
       cascade.setIcon(winScheme.CASCADE_ICON);
       cascade.setVisible(winScheme.CASCADE_VISIBLE);
       cascade.setEnabled(winScheme.CASCADE_ENABLE);
       cascade.setButtonEnabled(winScheme.CASCADE_BUTTON_ENABLE);
       cascade.setMnemonic(winScheme.CASCADE_MNEMONIC);
       cascade.setAccelerator(winScheme.CASCADE_ACCELERATOR);
       
       tile =  view.CommandBuilder.new TileArrange(winScheme.TILE_MENU_NAME);        
       tile.setDescription(winScheme.TILE_DESCRIPTION);
       tile.setIcon(winScheme.TILE_ICON);
       tile.setVisible(winScheme.TILE_VISIBLE);
       tile.setEnabled(winScheme.TILE_ENABLE);
       tile.setButtonEnabled(winScheme.TILE_BUTTON_ENABLE);
       tile.setMnemonic(winScheme.TILE_MNEMONIC);
       tile.setAccelerator(winScheme.TILE_ACCELERATOR);

       iconify =  view.CommandBuilder.new AllIconify(winScheme.ICONIFY_MENU_NAME);        
       iconify.setDescription(winScheme.ICONIFY_DESCRIPTION);
       iconify.setIcon(winScheme.ICONIFY_ICON);
       iconify.setVisible(winScheme.ICONIFY_VISIBLE);
       iconify.setEnabled(winScheme.ICONIFY_ENABLE);
       iconify.setButtonEnabled(winScheme.ICONIFY_BUTTON_ENABLE);
       iconify.setMnemonic(winScheme.ICONIFY_MNEMONIC);
       iconify.setAccelerator(winScheme.ICONIFY_ACCELERATOR);

       deiconify = view.CommandBuilder.new AllDeiconify(winScheme.DEICONIFY_MENU_NAME);        
       deiconify.setDescription(winScheme.DEICONIFY_DESCRIPTION);
       deiconify.setIcon(winScheme.DEICONIFY_ICON);
       deiconify.setVisible(winScheme.DEICONIFY_VISIBLE);
       deiconify.setEnabled(winScheme.DEICONIFY_ENABLE);
       deiconify.setButtonEnabled(winScheme.DEICONIFY_BUTTON_ENABLE);
       deiconify.setMnemonic(winScheme.DEICONIFY_MNEMONIC);
       deiconify.setAccelerator(winScheme.DEICONIFY_ACCELERATOR);

       maximize = view.CommandBuilder.new AllMaximize(winScheme.MAXIMIZE_MENU_NAME);        
       maximize.setDescription(winScheme.MAXIMIZE_DESCRIPTION);
       maximize.setIcon(winScheme.MAXIMIZE_ICON);
       maximize.setVisible(winScheme.MAXIMIZE_VISIBLE);
       maximize.setEnabled(winScheme.MAXIMIZE_ENABLE);
       maximize.setButtonEnabled(winScheme.MAXIMIZE_BUTTON_ENABLE);
       maximize.setMnemonic(winScheme.MAXIMIZE_MNEMONIC);
       maximize.setAccelerator(winScheme.MAXIMIZE_ACCELERATOR);

       allclose = view.CommandBuilder.new AllClose(winScheme.ALL_CLOSE_MENU_NAME);        
       allclose.setDescription(winScheme.ALL_CLOSE_DESCRIPTION);
       allclose.setIcon(winScheme.ALL_CLOSE_ICON);
       allclose.setVisible(winScheme.ALL_CLOSE_VISIBLE);
       allclose.setEnabled(winScheme.ALL_CLOSE_ENABLE);
       allclose.setButtonEnabled(winScheme.ALL_CLOSE_BUTTON_ENABLE);
       allclose.setMnemonic(winScheme.ALL_CLOSE_MNEMONIC);
       allclose.setAccelerator(winScheme.ALL_CLOSE_ACCELERATOR);

       cmdList = new ArrayList(4);
       cmdList.add(cascade);
       cmdList.add(tile);
       cmdList.add(deiconify);
       cmdList.add(iconify);
       cmdList.add(maximize);
       cmdList.add(allclose);
       
       if (winScheme.WINDOW_COMMANDS != null) {
           for (int i = 0; i < winScheme.WINDOW_COMMANDS.length; i++) {
               Command cmd = winScheme.WINDOW_COMMANDS[i];
               cmdList.add(cmd);
           }
       }
    }

        /* (non-Javadoc)
     * @see com.cysols.framework.platform.Module#getCommands()
     */
    public Command[] getCommands() {
        Command[] cmds = new Command[cmdList.size()];
        for (int i = 0; i < cmdList.size(); i++) {
            cmds[i] = (Command) cmdList.get(i);
        }
        
        return cmds;
    }
    
    void setAllEnabled(boolean aFlag) {
        cascade.setEnabled(aFlag);
        tile.setEnabled(aFlag);
        iconify.setEnabled(aFlag);
        deiconify.setEnabled(aFlag);
        maximize.setEnabled(aFlag);
        allclose.setEnabled(aFlag);
    }
    
    void accept(MenuBar bar) {
        bar.visit(this);
    }

}
