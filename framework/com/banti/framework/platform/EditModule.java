package com.banti.framework.platform;

import java.util.ArrayList;

import com.banti.framework.platform.EditKit.Copy;
import com.banti.framework.platform.EditKit.Cut;
import com.banti.framework.platform.EditKit.DeleteAll;
import com.banti.framework.platform.EditKit.Paste;
import com.banti.framework.platform.EditKit.SelectAll;
import com.banti.framework.platform.module.EditModuleScheme;


/**
 * TODO 
 */
final class EditModule extends Module {

    private Copy copy;
    private Cut cut;
    private Paste paste;
    private SelectAll selectAll;
    private DeleteAll deleteAll;
    
    private ArrayList cmdList;
    
    EditModule(EditModuleScheme editScheme) {
        super(editScheme.EDIT_MENU_NAME);
        super.setDescription(editScheme.EDIT_DESCRIPTION);
        super.setEnabled(editScheme.EDIT_ENABLE);
        super.setVisible(editScheme.EDIT_VISIBLE);
        super.setMnemonic(editScheme.EDIT_MNEMONIC);
        
        copy  = EditKit.CommandBuilder.new Copy(editScheme.COPY_MENU_NAME);
        copy.setDescription(editScheme.COPY_DESCRIPTION);
        copy.setIcon(editScheme.COPY_ICON);
        copy.setVisible(editScheme.COPY_VISIBLE);
        copy.setEnabled(editScheme.COPY_ENABLE);
        copy.setButtonEnabled(editScheme.COPY_BUTTON_ENABLE);
        copy.setMnemonic(editScheme.COPY_MNEMONIC);
        copy.setAccelerator(editScheme.COPY_ACCELERATOR);
        
        cut  = EditKit.CommandBuilder.new Cut(editScheme.CUT_MENU_NAME);
        cut.setDescription(editScheme.CUT_DESCRIPTION);
        cut.setIcon(editScheme.CUT_ICON);
        cut.setVisible(editScheme.CUT_VISIBLE);
        cut.setEnabled(editScheme.CUT_ENABLE);
        cut.setButtonEnabled(editScheme.CUT_BUTTON_ENABLE);
        cut.setMnemonic(editScheme.CUT_MNEMONIC);        
        cut.setAccelerator(editScheme.CUT_ACCELERATOR);
        
        paste  = EditKit.CommandBuilder.new Paste(editScheme.PASTE_MENU_NAME);
        paste.setDescription(editScheme.PASTE_DESCRIPTION);
        paste.setIcon(editScheme.PASTE_ICON);
        paste.setVisible(editScheme.PASTE_VISIBLE);
        paste.setEnabled(editScheme.PASTE_ENABLE);
        paste.setButtonEnabled(editScheme.PASTE_BUTTON_ENABLE);
        paste.setMnemonic(editScheme.PASTE_MNEMONIC);                
        paste.setAccelerator(editScheme.PASTE_ACCELERATOR);       

        selectAll  = EditKit.CommandBuilder.new SelectAll(editScheme.SELECTALL_MENU_NAME);
        selectAll.setDescription(editScheme.SELECTALL_DESCRIPTION);
        selectAll.setIcon(editScheme.SELECTALL_ICON);
        selectAll.setVisible(editScheme.SELECTALL_VISIBLE);
        selectAll.setEnabled(editScheme.SELECTALL_ENABLE);
        selectAll.setButtonEnabled(editScheme.SELECTALL_BUTTON_ENABLE);
        selectAll.setMnemonic(editScheme.SELECTALL_MNEMONIC);                
        selectAll.setAccelerator(editScheme.SELECTALL_ACCELERATOR);       
        selectAll.setSeparator(true);
        
        deleteAll  = EditKit.CommandBuilder.new DeleteAll(editScheme.DELETEALL_MENU_NAME);
        deleteAll.setDescription(editScheme.DELETEALL_DESCRIPTION);
        deleteAll.setIcon(editScheme.DELETEALL_ICON);
        deleteAll.setVisible(editScheme.DELETEALL_VISIBLE);
        deleteAll.setEnabled(editScheme.DELETEALL_ENABLE);
        deleteAll.setButtonEnabled(editScheme.DELETEALL_BUTTON_ENABLE);
        deleteAll.setMnemonic(editScheme.DELETEALL_MNEMONIC);                
        deleteAll.setAccelerator(editScheme.DELETEALL_ACCELERATOR);       

        cmdList = new ArrayList(5);        
        cmdList.add(copy);
        cmdList.add(cut);
        cmdList.add(paste);
        cmdList.add(deleteAll);
        cmdList.add(selectAll);
        
        if (editScheme.EDIT_COMMANDS != null) {
            for (int i = 0; i < editScheme.EDIT_COMMANDS.length; i++) {
                Command cmd = editScheme.EDIT_COMMANDS[i];
                cmdList.add(cmd);
            }
        }
        
        EditKit.set(copy);
        EditKit.set(cut);
        EditKit.set(paste);
        EditKit.set(deleteAll);
        EditKit.set(selectAll);
    }
    
    /* (non-Javadoc)
     * @see com.cysols.framework.platform.Module#getCommands()
     */
    public Command[] getCommands() {
        Command[] cmds = new Command[cmdList.size()];        
        for (int i = 0; i < cmdList.size(); i++) {
            cmds[i] = (Command) cmdList.get(i);
            if (i == 0) cmds[i].setSeparator(true);
        }
        
        return cmds;
    }
    
}
