package com.framework.platform.mail;

import java.awt.event.ActionEvent;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.banti.framework.platform.Command;
import com.banti.framework.theme.ToolKit;

public class MailCommand extends Command {
	private static final String NAME = "Send Mail";
private static final String EMAIL_ICON="/com/banti/framework/platform/images/email.jpg";
	public MailCommand() {
		super(NAME);
		setLoginNeeds(true);
		setVisible(true);
		setDescription("Mail Sender");
		setIcon(ToolKit.getInstance().createImageIcon(EMAIL_ICON));
		
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		new MailSenderAction();
	}

}
