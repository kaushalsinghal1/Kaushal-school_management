package com.school.console.admin;

import java.awt.event.ActionEvent;

import org.hibernate.HibernateException;

import com.banti.framework.platform.Command;
import com.school.console.SchoolMain;

public class ClassDetailsCommand extends Command {
	public ClassDetailsCommand(String name) {
		super(name);
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		try {
			new ClassDetailsAction().setVisible(true);
		} catch (HibernateException ex) {
			SchoolMain.showWarningDialog("Database is down !");
		}		
	}

}
