package com.school.console;

import org.hibernate.HibernateException;

import com.banti.framework.core.Account;
import com.banti.framework.cwt.ProgressCommunicator;
import com.banti.framework.cwt.ProgressCommunicatorMonitor;
import com.banti.framework.platform.Application;
import com.banti.framework.platform.LoginListener;
import com.banti.framework.platform.LogoutListener;
import com.school.hiebernate.HiebernateDboUtil;
import com.school.utils.MsgDialogUtils;

public class SetUpApplication implements LoginListener, LogoutListener {

	@Override
	public void loginSucceeded(Account account) {
		if (Application.account != null) {

			new SearchProgress(account);
		}
	}

	@Override
	public void loginFailed() {
	}

	@Override
	public void logoutSucceeded(Account account) {

	}

	@Override
	public void logoutFailed() {

	}

	class SearchProgress implements ProgressCommunicator {
		Account account;

		public SearchProgress(Account account) {
			this.account = account;
			ProgressCommunicatorMonitor communicatorMonitor = new ProgressCommunicatorMonitor(
					"Connecting to database...", true, SearchProgress.this);
			communicatorMonitor.start();
		}

		@Override
		public void run() {
			try {
				HiebernateDboUtil.getLoginDetails(account.getName());
			} catch (HibernateException e) {
				MsgDialogUtils
						.showWarningDialog(
								Application.Frame,
								"Unable to connect data base.\n Please Check Database server is running that configured in \"config/hibernate.cfg file\"");
				System.exit(0);
			} catch (Exception e) {
				MsgDialogUtils
						.showWarningDialog(
								Application.Frame,
								"Unable to connect data base.\n Please Check Database server is running that configured in \"config/hibernate.cfg file\"");
				System.exit(0);
			}
		}

		@Override
		public void doPostProcess() {

		}

		@Override
		public void doCancel() {

		}
	}

}
