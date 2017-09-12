package com.school.console.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hibernate.HibernateException;

import com.school.console.SchoolMain;
import com.school.fees.SessionFeeDetailsAction;
import com.school.resource.CommandConstant;

public class AdminMenuAction implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		if (CommandConstant.CLASS_REGISTER_COMMAND.equals(e.getActionCommand())) {
			try {
				new ClassDetailsAction().setVisible(true);
			} catch (HibernateException ex) {
				SchoolMain.showWarningDialog("Database is down !");
			}

		} else if (CommandConstant.SESSION_REGISTER_COMMAND.equals(e
				.getActionCommand())) {
			new SessionDetailsAction().setVisible(true);
		} else if (CommandConstant.FEE_REGISTER_COMMAND.equals(e
				.getActionCommand())) {
			 new SessionFeeDetailsAction().setVisible(true);
			//new TestTable().setVisible(true);
		}

	}
}
