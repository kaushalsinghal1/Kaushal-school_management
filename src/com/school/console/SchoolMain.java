package com.school.console;

import java.awt.Font;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.banti.framework.core.InitializationFailedException;
import com.banti.framework.logging.housekeeping.LogHouseKeeper;
import com.banti.framework.platform.Application;
import com.banti.framework.platform.LoginManager;
import com.banti.framework.platform.PluginCommand;
import com.banti.framework.platform.RuntimeRegistry;
import com.banti.framework.theme.ToolKit;
import com.school.console.admin.module.AdminModule;
import com.school.console.admin.module.DefaultAdminModuleSchema;
import com.school.fees.search.DefaultSearchCommandSchema;
import com.school.fees.search.StudentSearchCommand;
import com.school.framework.peer.SchoolLoginManager;
import com.school.resource.ResourcesUtils;
import com.school.setting.DefaultSettingModuleSchema;
import com.school.setting.SettingModule;
import com.school.student.module.AdmissionPluginModule;
import com.school.student.module.DefaultFeeDeatilsModuleSchema;
import com.school.student.module.DefaultStudentModuleSchema;
import com.school.student.module.FeeDetailsPludinModule;
import com.school.student.module.StudenPluginCommand;
import com.school.utils.Logger;
import com.school.utils.MsgDialogUtils;

public class SchoolMain extends Application {
	private static SchoolMain school;
	private final String logo_path = "/com/banti/framework/platform/images/s1.jpg";

	static {
		// UIManager.put("swing.boldMetal", Boolean.FALSE);
		// UIManager.put("swing.boldMetal", Boolean.TRUE);

		Font font = new Font("Sans-serif", Font.TRUETYPE_FONT, 12);
		FontUIResource fontUIResource = new FontUIResource(font);
		UIDefaults defaultTable = UIManager.getLookAndFeelDefaults();

		Set set = defaultTable.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof String) {
				String s = (String) o;

				if (s.indexOf("acceleratorFont") > 0)
					continue;
				if (s.indexOf("titleFont") > 0)
					continue;
				// if (s.indexOf("Menu") >= 0) continue;
				if (s.endsWith("font") || s.endsWith("Font")) {
					UIManager.put(s, fontUIResource);
				}
			}
		}
	}

	public SchoolMain() {
		super(getFrameTitle());
		// MasterBrowser browser = new MasterBrowser("Master Browser");
		ToolKit toolKit = ToolKit.getInstance();
		setIconImage(toolKit.createImage(logo_path));
		try {
			// --------------------
			String imgPath = "/com/banti/framework/platform/images/sms.jpg";
			JPanel jPanel = new JPanel();
			jPanel.add(new JLabel("Loading................"));
			showSplashScreen(toolKit.createImageIcon(imgPath), jPanel, 2,
					"Loading ...");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// -------------------
			LoginManager loginManager = new SchoolLoginManager();
			super.setLoginManager(loginManager);

			RuntimeRegistry engine = super.getRuntimeRegistry();
			// MasterBrowser browser = new MasterBrowser("Master Browser");
			engine.register((PluginCommand) new StudenPluginCommand(
					new DefaultStudentModuleSchema()));

			engine.register((PluginCommand) new AdmissionPluginModule());

			engine.register((PluginCommand) new FeeDetailsPludinModule(
					new DefaultFeeDeatilsModuleSchema()));
			engine.register(new StudentSearchCommand(
					new DefaultSearchCommandSchema()));

			engine.register(new AdminModule(new DefaultAdminModuleSchema()));
			engine.register(new SettingModule(new DefaultSettingModuleSchema()));
			MainLicenceCommand mCommand = new MainLicenceCommand();
			engine.register(mCommand);
			LicenceManagement licenceManagement = new LicenceManagement(
					mCommand.getActivator());
			loginManager.addLoginListener(licenceManagement);
			loginManager.addLogoutListener(licenceManagement);
			//loginManager.addLoginListener(new SetUpApplication());

			// engine.register(new SubMasterCommand("Sub Browser"));
			super.setup();

		} catch (InitializationFailedException e) {
			e.printStackTrace();
		}
		// Sets line based drag mode
		super.View.setDragMode(false);

		// Sets no confirmation mode when Exit
		super.View.setExitConfirmMode(true);

		// Sets roll-over tool bar mode.
		super.View.setToolBarRollOver(true);

		try {
			super.setup();
			// super.loadPreference();

			// Look and Feel
			try {
				UIManager
						.setLookAndFeel("org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel");
				// LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);

				SwingUtilities.updateComponentTreeUI(this);

			} catch (Exception e) {
				super.loadPreference();
			}

			LogHouseKeeper hk = LogHouseKeeper.getInstance();
			try {
				hk.load();
			} catch (IOException e) {
				throw new InitializationFailedException(
						"Unable to read log house keeping configuration", e);
			}
			hk.executeOnce();
			hk.start();

		} catch (InitializationFailedException e) {
			Logger.DEBUG.warning("KOBAN Console Instance cannot be created.");
			Logger.EXCEPTION.log(Level.WARNING,
					"KOBAN Console Instance cannot be created", e);

			String message = ResourcesUtils.getString("MSG_CONSOLE_INIT_ERROR");
			MsgDialogUtils.showWarningDialog(null, message);
			System.exit(0);
		}

		super.View.show();
		// super.View.setDefaultMenuSelection(Application.Frame);
		// setSystemLookAndFeel();
		setupEnterActionForAllButtons();
		// after login
	}

	public void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Couldn't use system look and feel.");
		}
	}

	private void setupEnterActionForAllButtons() {
		InputMap im = (InputMap) UIManager.getDefaults().get(
				"Button.focusInputMap");
		Object pressedAction = im.get(KeyStroke.getKeyStroke("pressed SPACE"));
		Object releasedAction = im
				.get(KeyStroke.getKeyStroke("released SPACE"));

		im.put(KeyStroke.getKeyStroke("pressed ENTER"), pressedAction);
		im.put(KeyStroke.getKeyStroke("released ENTER"), releasedAction);

	}

	public static SchoolMain getInstance() {
		if (school == null) {
			school = new SchoolMain();
		}
		return school;
	}

	public void logOutApplication() {
		logOutApp();
	}

	private static String getFrameTitle() {
		return "School Management System";
	}

	public static void main(String[] args) {
		System.setProperty("VERSION_VALUE", "1.0.0");
		System.setProperty("BUILD_VALUE", "1238456");
		System.setProperty("CONTACT_VALUE", "Kaushal- +918792703578");
		SchoolMain ap = SchoolMain.getInstance();
		ap.setVisible(true);
		ap.showLoginDialog();

	}

	public static void showWarningDialog(String message) {
		school.showMessageDialog(message, Application.WARNING);
	}
}
