package com.banti.framework.platform;

import java.util.ArrayList;

import com.banti.framework.platform.ViewMediator.DefaultCommand.PasswordChange;
import com.banti.framework.platform.module.OptionModuleScheme;
import com.banti.framework.theme.ToolKit;
import com.framework.platform.mail.MailCommand;

/**
 * TODO
 */
final class OptionModule extends Module {

	Command accountMgt;
	Command mailCommand;
	private Command[] cmdArray;
	private ArrayList<Command> cmdList;

	OptionModule(OptionModuleScheme optionScheme, ViewMediator view) {
		super(optionScheme.OPTION_MENU_NAME);
		super.setDescription(optionScheme.OPTION_DESCRIPTION);
		super.setVisible(optionScheme.OPTION_VISIBLE);
		super.setEnabled(optionScheme.OPTION_ENABLE);
		super.setMnemonic(optionScheme.OPTION_MNEMONIC);

		cmdArray = optionScheme.OPTION_COMMANDS;
		accountMgt = view.CommandBuilder.new AccountMgntCommand(
				optionScheme.ACCOUNT_MGNT_NAME);
		accountMgt.setDescription(optionScheme.ACCOUNT_MGNT_DESCRIPTION);
		accountMgt.setIcon(optionScheme.ACCOUNT_MGNT_ICON);
		accountMgt.setLoginNeeds(true);
		accountMgt.setMnemonic(optionScheme.ACCOUNT_MGNT_MNEMONIC);
		//Command mailCommand=new 
		
		mailCommand=new MailCommand();
		
		cmdList = new ArrayList<Command>();
		cmdList.add(accountMgt);
		cmdList.add(mailCommand);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cysols.framework.platform.Module#getCommands()
	 */
	public Command[] getCommands() {
		Command[] cmds = new Command[cmdList.size()];
		for (int i = 0; i < cmdList.size(); i++) {
			cmds[i] = (Command) cmdList.get(i);
		}

		return cmds;
		//return cmdArray;
	}
}
