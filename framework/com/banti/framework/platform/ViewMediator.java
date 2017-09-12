package com.banti.framework.platform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.banti.framework.core.Account;
import com.banti.framework.core.InitializationFailedException;
import com.banti.framework.platform.Display.ScrollableDesktopPane;
import com.banti.framework.platform.peer.AccountMgmtDialog;
import com.banti.framework.platform.peer.LicenseEntryPane;
import com.banti.framework.platform.peer.PasswordChangePane;
import com.banti.framework.platform.peer.PreferencePane;
import com.banti.framework.platform.peer.SplashScreen;
import com.banti.framework.theme.ToolKit;
import com.banti.framework.utils.BrowserLauncher;

/**
 * This class works like a mediator, which has a Display, a MenuBar, a StatusBar
 * and a WindowBar as its Colleagues. You can control Those components from your
 * Application concrete class by ViewMeditor through
 * <i>Application.View.somemethod()</i>. The responsibility of ViewMediator is
 * based on Mediator pattern.
 * 
 */
public final class ViewMediator {

	static DefaultCommand CommandBuilder;
	static final String TopScreenTitle = "MAIN";

	private Display display;
	private MenuBar menubar;
	private StatusBar statusbar;
	private WindowBar windowbar;

	private Application application;
	private ArrayList colleagueList;

	private boolean exitConfirmMode = true;
	private String exitMessage = null;
	private boolean toolBarRoolOver = false;

	private Component eastC;
	private Component westC;
	private Component northC;

	/**
	 * Non-public constructor
	 * 
	 */
	ViewMediator(Application application, ModuleSchemeFactory schemeFactory,
			boolean logo) throws InitializationFailedException {

		this.application = application;
		CommandBuilder = new DefaultCommand();

		menubar = new MenuBar(this, schemeFactory);
		display = new Display(this);
		statusbar = new StatusBar(this);
		windowbar = new WindowBar(this);

		menubar.init(logo);

		colleagueList = new ArrayList(1);

	}

	/**
	 * Shows all views, Display area, MenuBar, ToolBar, StatusBar and WindowBar.
	 * 
	 */
	public void show() {
		display.show();
		menubar.show();
		statusbar.show();
		windowbar.show();

		Iterator itr = colleagueList.iterator();
		while (itr.hasNext()) {
			ViewColleague colleague = (ViewColleague) itr.next();
			colleague.show();
		}
	}

	/**
	 * Adds a specified ViewColleague concrete instance.
	 * 
	 * @param viewColleague
	 *            - a ViewColleague concrete instance to be added.
	 */
	public void addViewColleague(ViewColleague viewColleague) {
		colleagueList.add(viewColleague);
	}

	/**
	 * Removes a specified ViewColleague concrete instance.
	 * 
	 * @param viewColleague
	 *            - a ViewColleague concrete instance to be removed.
	 */
	public void removeViewColleague(ViewColleague viewColleague) {
		colleagueList.remove(viewColleague);
	}

	/**
	 * Removes all added ViewCollieague instances.
	 * 
	 */
	public void removeAllViewColleagues() {
		colleagueList.clear();
	}

	/**
	 * Chages Status Incidator on the StatusBar.
	 * 
	 * @param color
	 *            Color object, the color you can choose is as follows <br>
	 *            Color.BLUE: left indicator gets Blue. <br>
	 *            Color.GREEN: center incidator gets Green. <br>
	 *            Color.RED: right indicator gets Red. <br>
	 *            Color.YELLOW: center indicator gets Yellow. <br>
	 *            Color.BLACK: all indicators get Black. <br>
	 */
	public void changeStatus(Color color) {
		statusbar.changeStatus(color);
	}

	public void setStatusMessage(String message) {
		if (message == null)
			return;
		statusbar.setMessage(message);
	}

	public void startIndeterminatedProgress() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				statusbar.setIndeterminatedProgress(true);
			}
		});
	}

	public void stopIndeterminatedProgress() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				statusbar.setIndeterminatedProgress(false);
			}
		});
	}

	/**
	 * Removes a current tab.
	 * 
	 */
	public void removeTab() {
		boolean isSecondTab = false;
		String title = this.getCurrentDeskTopTitle();

		if (display.getDesktopCount() == 2)
			isSecondTab = true;

		if (isSecondTab) {
			application.getContentPane().remove(display.Screen());
		}

		statusbar.tabClosed(title);
		windowbar.tabClosed(title);
		menubar.tabClosed(title);
		display.tabClosed(title);

		Iterator itr = colleagueList.iterator();
		while (itr.hasNext()) {
			ViewColleague colleague = (ViewColleague) itr.next();
			colleague.tabClosed(title);
		}

		if (isSecondTab) {
			application.getContentPane().add(display.Screen(),
					BorderLayout.CENTER);
			display.getCurrentDeskTopPane().add(windowbar.Bar(),
					BorderLayout.SOUTH);
			this.changeTab(this.getCurrentDeskTopTitle());
			application.getContentPane().validate();
			application.getContentPane().repaint();
		} else {
			this.changeTab(this.getCurrentDeskTopTitle());
		}

	}

	/**
	 * Creates a new tab.
	 * 
	 */
	public void createNewTab(String title) {
		boolean isSecondTab = false;

		if (display.getDesktopCount() == 1)
			isSecondTab = true;

		if (isSecondTab) {
			application.getContentPane().remove(display.Screen());
		}

		statusbar.tabCreated(title);
		windowbar.tabCreated(title);
		menubar.tabCreated(title);
		display.tabCreated(title);

		Iterator itr = colleagueList.iterator();
		while (itr.hasNext()) {
			ViewColleague colleague = (ViewColleague) itr.next();
			colleague.tabCreated(title);
		}

		if (isSecondTab) {
			application.getContentPane().add(display.Screen(),
					BorderLayout.CENTER);
		}

		display.getCurrentDeskTopPane()
				.add(windowbar.Bar(), BorderLayout.SOUTH);

		this.changeTab(this.getCurrentDeskTopTitle());

		if (isSecondTab) {
			application.getContentPane().validate();
			application.getContentPane().repaint();
		}
	}

	public void changeTab(String title) {
		display.tabChanged(title);
		menubar.tabChanged(title);
		statusbar.tabChanged(title);
		windowbar.tabChanged(title);

		for (int i = 0; i < colleagueList.size(); i++) {
			ViewColleague colleague = (ViewColleague) colleagueList.get(i);
			colleague.tabChanged(title);
		}

		display.getCurrentDeskTopPane()
				.add(windowbar.Bar(), BorderLayout.SOUTH);
	}

	/**
	 * Sets the rollover state of this toolbar. If the rollover state is true
	 * then the border of the toolbar buttons will be drawn only when the mouse
	 * pointer hovers over them. The default value of this property is false.
	 * 
	 * @param aFlag
	 *            - true for rollover toolbar buttons; otherwise false.
	 */
	public void setToolBarRollOver(boolean aFlag) {
		toolBarRoolOver = aFlag;
		menubar.Tools().setRollover(aFlag);
	}

	void setToolBarRollOver() {
		menubar.Tools().setRollover(toolBarRoolOver);
	}

	/**
	 * Sets a message that is displayed on Exit Confirmation dialog. It has the
	 * meaning only when Exit Confirmation mode is true.
	 * 
	 * @param message
	 *            a exit confirmation message displayed on a dialog.
	 */
	public void setExitConfirmMessage(String message) {
		exitMessage = message;
	}

	/**
	 * When true is set, a confirmation dialog appears if you want to exit an
	 * application. Otherwise, your application goes down without a message.
	 * <p>
	 * True is default setting.
	 * 
	 * @param aFlag
	 *            true or false. a Exit Confirmation dialog is displayed if
	 *            true; otherwise no message.
	 */
	public void setExitConfirmMode(boolean aFlag) {
		exitConfirmMode = aFlag;
	}

	/**
	 * When true is set, InternalWindow can be moved with its contents.
	 * Otherwise, only window line is displayed while moving.
	 * 
	 * @param mode
	 *            true or false, true means live mode during moving.
	 */
	public void setDragMode(boolean mode) {
		display.setDragMode(mode);
	}

	/**
	 * When true is set, the classic style of Windows Look & Feel gets
	 * available. Default style is used when false is set.
	 * 
	 * @param aFlag
	 *            true or false, true means the classic style of Windows Look &
	 *            Feel is activated.
	 */
	public static void setClassicStyleWinLookAndFeel(boolean aFlag) {
		if (aFlag) {
			System.setProperty("swing.noxp", "true");
		} else {
			System.setProperty("swing.noxp", "false");
		}
	}

	public void setFirstTabTitle(String title) {
		if (display.Screen() instanceof JTabbedPane) {
			((JTabbedPane) display.Screen()).setTitleAt(0, title);
		}
	}

	public void setWindowOffset(int x, int y) {
		display.setOffset(x, y);
	}

	public void setComponentAtRight(Component c) {
		ScrollableDesktopPane pane = display.getCurrentDeskTopPane();
		if (pane != null) {
			eastC = c;
			pane.add(eastC, BorderLayout.EAST);
			pane.validate();
			pane.repaint();
		}
	}

	public void removeComponentAtRight() {
		ScrollableDesktopPane pane = display.getCurrentDeskTopPane();
		if (eastC != null && pane != null) {
			pane.remove(eastC);
			eastC = null;
			pane.validate();
			pane.repaint();
		}
	}

	public void setComponentAtLeft(Component c) {
		ScrollableDesktopPane pane = display.getCurrentDeskTopPane();
		if (pane != null) {
			westC = c;
			pane.add(westC, BorderLayout.WEST);
			pane.validate();
			pane.repaint();
		}
	}

	public void removeComponentAtLeft() {
		ScrollableDesktopPane pane = display.getCurrentDeskTopPane();
		if (westC != null && pane != null) {
			pane.remove(westC);
			westC = null;
			pane.validate();
			pane.repaint();
		}
	}

	public void setComponentAtTop(Component c) {
		ScrollableDesktopPane pane = display.getCurrentDeskTopPane();
		if (pane != null) {
			northC = c;
			pane.add(northC, BorderLayout.NORTH);
			pane.validate();
			pane.repaint();
		}
	}

	public void removeComponentAtTop() {
		ScrollableDesktopPane pane = display.getCurrentDeskTopPane();
		if (northC != null && pane != null) {
			pane.remove(northC);
			northC = null;
			pane.validate();
			pane.repaint();
		}
	}

	public void setDefaultMenuSelection(JFrame frame) {
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menubar.selectMenu();
			}
		};
		ActionMap actionMap = frame.getRootPane().getActionMap();
		final String MENU_ACTION_KEY = "My Action";
		actionMap.put(MENU_ACTION_KEY, action);
		InputMap inputMap = frame.getRootPane().getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, 0, true),
				MENU_ACTION_KEY);
	}

	// ------------ non-public methods
	// ---------------------------------------------------------------
	void addWindow(InternalWindow window) {
		if (window == null)
			return;

		windowbar.windowOpened(window);
		menubar.windowOpened(window);
		display.windowOpened(window);
		statusbar.windowOpened(window);

		// for Koban v4.1 indexed help
		Application.CURRENT_HELP_ID = window.DEFAULT_HELP_ID;

		Iterator itr = colleagueList.iterator();
		while (itr.hasNext()) {
			ViewColleague colleague = (ViewColleague) itr.next();
			colleague.windowOpened(window);
		}

		this.repaint();
	}

	void removeWindow(InternalWindow window) {
		if (window == null)
			return;

		windowbar.windowClosed(window);
		menubar.windowClosed(window);
		display.windowClosed(window);

		// for Koban v4.1 indexed help
		Application.CURRENT_HELP_ID = null;

		Iterator itr = colleagueList.iterator();
		while (itr.hasNext()) {
			ViewColleague colleague = (ViewColleague) itr.next();
			colleague.windowClosed(window);
		}

		statusbar.windowClosed(window);
		CommandBuilder.removeWindow(window);
		this.repaint();
	}

	void selectWindow(InternalWindow window) {
		windowbar.windowSelected(window);
		menubar.windowSelected(window);
		display.windowSelected(window);
		statusbar.windowSelected(window);

		// for Koban v4.1 indexed help
		Application.CURRENT_HELP_ID = window.HELP_ID;

		Iterator itr = colleagueList.iterator();
		while (itr.hasNext()) {
			ViewColleague colleague = (ViewColleague) itr.next();
			colleague.windowSelected(window);
		}
	}

	void toFront(InternalWindow window) {
		window.toFront();
		try {
			window.setIcon(false);
		} catch (PropertyVetoException e) {
			// ignore
		}

		try {
			window.setSelected(true);
			if (!window.isVisible())
				window.setVisible(true);
		} catch (java.beans.PropertyVetoException e) {
			// ignore
		}
	}

	void deploy() {
		if (application == null)
			return;

		application.getContentPane().setLayout(new BorderLayout());
		application.getContentPane().add(display.Screen(), BorderLayout.CENTER);
		application.getContentPane().add(menubar.Tools(), BorderLayout.NORTH);
		application.getContentPane().add(statusbar.Bar(), BorderLayout.SOUTH);
		display.getCurrentDeskTopPane()
				.add(windowbar.Bar(), BorderLayout.SOUTH);

		application.setJMenuBar(menubar.Menu());
	}

	ScrollableDesktopPane getCurrentDeskTopPane() {
		return display.getCurrentDeskTopPane();
	}

	int getCurrentDeskTopIndex() {
		return display.getCurrentDeskTopIndex();
	}

	String getCurrentDeskTopTitle() {
		return display.getCurrentDeskTopTitle();
	}

	void repaint() {
		display.repaint();
		menubar.repaint();
		windowbar.repaint();
	}

	void updateUI() {
		menubar.Tools().setRollover(toolBarRoolOver);
		display.Screen().updateUI();
		menubar.Menu().updateUI();
		menubar.Tools().updateUI();
		windowbar.Bar().updateUI();
	}

	// ---------------- Default Command classes
	// ------------------------------------------------------------
	final class DefaultCommand {

		private Map winEntryList;
		private Exit exitCommand;
		private Switch toolSwitch;
		private Switch statusSwitch;
		private Switch windowSwitch;

		private DefaultCommand() {
			super();
			winEntryList = Collections.synchronizedMap(new HashMap());
			exitCommand = null;
			toolSwitch = null;
			statusSwitch = null;
			windowSwitch = null;
		}

		Exit getExitCommand() {
			return exitCommand;
		}

		Switch createToolBarSwitch(String name) {
			if (toolSwitch == null) {
				toolSwitch = new Switch(name, menubar.Tools());
			}
			return toolSwitch;
		}

		Switch getToolBarSwitch() {
			return toolSwitch;
		}

		Switch createStatusBarSwitch(String name) {
			if (statusSwitch == null) {
				statusSwitch = new Switch(name, statusbar.Bar());
			}
			return statusSwitch;
		}

		Switch getStatusBarSwitch() {
			return statusSwitch;
		}

		Switch createWindowBarSwitch(String name) {
			if (windowSwitch == null) {
				windowSwitch = new Switch(name, windowbar.Bar());

				/*
				 * propertyName="LookAndFeel" and newValue="WindowLookAndFeel or
				 * GTKLookAndFeel or MacLookAndFeel or
				 * WindowsClassicLookAndFeel", then WindowBar gets disabled
				 * since button deployoment doesn't work well.
				 * 
				 * And, if propertyName="LookAndFeel" and
				 * newValue="MetalLookAndFeel" and currentTheme= "OceanTheme",
				 * then then WindowBar gets disabled since button deployoment
				 * doesn't work well also.
				 */
				UIManager
						.addPropertyChangeListener(new PropertyChangeListener() {
							String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
							String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
							String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
							String windowsClassic = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
							String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
							String oceanTheme = "javax.swing.plaf.metal.OceanTheme";

							public void propertyChange(PropertyChangeEvent pce) {
								if (pce == null)
									return;
								String propertyName = pce.getPropertyName();
								String laf = pce.getNewValue().getClass()
										.getName();

								if (!propertyName.equals("lookAndFeel"))
									return;
								if (laf.equals(windows) || laf.equals(gtk)
										|| laf.equals(mac)
										|| laf.equals(windowsClassic)) {
									windowbar.Bar().setVisible(false);
									windowSwitch.setEnabled(false);
									windowSwitch.setSelected(false);
								} else if (laf.equals(metal)) {
									if (!(pce.getNewValue() instanceof MetalLookAndFeel))
										return;
									try {
										Class.forName("javax.swing.plaf.metal.OceanTheme");
										String themeName = MetalLookAndFeel
												.getCurrentTheme().getClass()
												.getName();
										// String themeName = "";
										if (oceanTheme.equals(themeName)) {
											windowbar.Bar().setVisible(false);
											windowSwitch.setEnabled(false);
											windowSwitch.setSelected(false);
										} else {
											windowbar
													.Bar()
													.setVisible(
															windowbar
																	.Bar()
																	.isVisible());
											windowSwitch.setEnabled(true);
											windowSwitch.setSelected(windowbar
													.Bar().isVisible());
										}
									} catch (Exception e) {
										windowbar.Bar().setVisible(
												windowbar.Bar().isVisible());
										windowSwitch.setEnabled(true);
										windowSwitch.setSelected(windowbar
												.Bar().isVisible());
									} catch (IllegalAccessError e) {
										windowbar.Bar().setVisible(
												windowbar.Bar().isVisible());
										windowSwitch.setEnabled(true);
										windowSwitch.setSelected(windowbar
												.Bar().isVisible());
									}
								} else {
									windowbar.Bar().setVisible(
											windowbar.Bar().isVisible());
									windowSwitch.setEnabled(true);
									windowSwitch.setSelected(windowbar.Bar()
											.isVisible());
								}
							}
						});

			}
			return windowSwitch;
		}

		Switch getWindowBarSwitch() {
			return windowSwitch;
		}

		void removeWindow(InternalWindow window) {
			winEntryList.remove(window);
		}

		WindowEntry createWindowEntry(InternalWindow window) {
			if (winEntryList.get(window) == null) {
				winEntryList.put(window, new WindowEntry(window.getTitle(),
						window));
			}
			return (WindowEntry) winEntryList.get(window);
		}

		// ------------------- Inner Command Concrete Classes of Default Menus
		final class Login extends Command {
			private LoginManager manager;

			Login(String name, LoginManager manager) {
				super(name);
				this.manager = manager;
				manager.plug(this);
			}

			public void entryPoint(ActionEvent ae) {
				manager.login(application);
			}
		}

		final class Logout extends Command {
			private LoginManager manager;

			Logout(String name, LoginManager manager) {
				super(name);
				this.manager = manager;
				manager.plug(this);
			}

			public void entryPoint(ActionEvent ae) {
				ToolKit tool = ToolKit.getInstance();
				int answer = application.showConfirmDialog(tool
						.getString("MSG_LOGOUT_CONFIRMATION"));

				if (answer == Application.YES) {
					manager.logout(application);
					new AllClose("").entryPoint(new ActionEvent(this, 0, "c"));
				}
			}
		}

		final class PasswordChange extends Command implements ActionListener {

			private String name;
			private JDialog dialog;
			private PasswordChangePane pcPanel;
			private boolean accountEnable;

			/**
             * 
             */
			public PasswordChange(String name, boolean account) {
				super(name);
				this.name = name;
				this.setLoginNeeds(true);
				this.accountEnable = account;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.cysols.framework.platform.Command#entryPoint(java.awt.event
			 * .ActionEvent)
			 */
			public void entryPoint(ActionEvent ae) {
				ToolKit tool = ToolKit.getInstance();
				// AccountSession session = Application.getSession();
				Account currentAccount = application.account;

				if (currentAccount == null) {
					application.showMessageDialog(
							tool.getString("MSG_NOT_LOGIN"),
							Application.WARNING);

					return;
				}

				JButton okButton;
				JButton applyButton;
				JButton cancelButton;

				dialog = new JDialog(application, name, true);

				pcPanel = new PasswordChangePane(dialog, Application.Login,
						currentAccount, accountEnable);

				okButton = new JButton(tool.getString("OK"));
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				okButton.registerKeyboardAction(this,
						KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
						JComponent.WHEN_FOCUSED);

				applyButton = new JButton(tool.getString("APPLY"));
				applyButton.setActionCommand("APPLY");
				applyButton.addActionListener(this);
				applyButton.registerKeyboardAction(this,
						KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
						JComponent.WHEN_FOCUSED);

				cancelButton = new JButton(tool.getString("CANCEL"));
				cancelButton.setActionCommand("CANCEL");
				cancelButton.addActionListener(this);
				cancelButton.registerKeyboardAction(this,
						KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
						JComponent.WHEN_FOCUSED);

				JPanel botPanel = new JPanel();
				botPanel.add(okButton);
				botPanel.add(applyButton);
				botPanel.add(cancelButton);

				dialog.getContentPane().add(pcPanel, BorderLayout.CENTER);
				dialog.getContentPane().add(botPanel, BorderLayout.SOUTH);

				// dialog.setSize(285, 225);
				dialog.pack();
				dialog.setResizable(false);
				dialog.setLocationRelativeTo(application);
				dialog.setVisible(true);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent ae) {
				if (ae == null)
					return;
				String action = ae.getActionCommand();

				if ("CANCEL".equals(ae.getActionCommand())) {
					dialog.dispose();

				} else if ("OK".equals(action) || "APPLY".equals(action)) {

					pcPanel.actionPerformed(ae);

					if ("OK".equals(action)) {
						dialog.dispose();
					}
				}

			}

		}

		final class Print extends Command {
			Print(String name) {
				super(name);
				super.setVisible(true);
				super.setEnabled(false);

			}

			public void entryPoint(ActionEvent ae) {
				if (ae == null)
					return;

				ScrollableDesktopPane deskTop = getCurrentDeskTopPane();
				InternalWindow selectedWindow = deskTop.getSelectedWindow();

				if (!(selectedWindow instanceof PrintControllable))
					return;

				PrintControllable printable = (PrintControllable) selectedWindow;

				printable.doPrint();
			}
		}

		final class PrintPreview extends Command {

			PrintPreview(String name) {
				super(name);
				super.setVisible(true);
				super.setEnabled(false);

			}

			public void entryPoint(ActionEvent ae) {
				if (ae == null)
					return;

				ScrollableDesktopPane deskTop = getCurrentDeskTopPane();
				InternalWindow selectedWindow = deskTop.getSelectedWindow();

				if (!(selectedWindow instanceof PrintControllable))
					return;

				PrintControllable printable = (PrintControllable) selectedWindow;

				JDialog dialog = printable.createPreviewDialog(application);
				if (dialog != null) {
					dialog.setModal(true);
					dialog.setLocationRelativeTo(application);
					dialog.setVisible(true);
				}
			}
		}

		final class AddTab extends Command {

			AddTab(String name) {
				super(name);
			}

			public void entryPoint(ActionEvent ae) {
				if (display.getDesktopCount() == 1) {
					createNewTab("" + 1); // ViewMediator.createNewTab() is
											// called.
				} else {
					createNewTab("" + display.getDesktopCount());
				}
			}
		}

		final class RemoveTab extends Command {

			RemoveTab(String name) {
				super(name);
			}

			public void entryPoint(ActionEvent ae) {
				removeTab(); // ViewMediator.removeTab() is called.
			}
		}

		/*
		 * final class OpenMap extends Command {
		 * 
		 * private String defaultTitle; private String defaultMap;
		 * 
		 * OpenMap(String name, String title, String defaultMapFilePath) {
		 * super(name); this.defaultTitle = title; this.defaultMap =
		 * defaultMapFilePath; }
		 * 
		 * public void entryPoint(ActionEvent ae) { InternalMapWindow win = new
		 * InternalWindow(defaultMap, defaultTitle); win.pack();
		 * win.setVisible(true); } }
		 */

		final class Exit extends Command {

			Exit(String name) {
				super(name);
				super.setSeparator(true);
				exitCommand = this;
			}

			public void entryPoint(ActionEvent ae) {
				ToolKit tool = ToolKit.getInstance();
				if (exitConfirmMode == true) {
					int answer;
					if (exitMessage != null) {
						answer = application.showConfirmDialog(exitMessage);
					} else {
						answer = application.showConfirmDialog(tool
								.getString("EXIT_MESSAGE"));
					}

					if (answer == Application.YES) {
						if (Application.Login != null) {
							if (Application.account != null) {
								Application.Login.setAllLogout(true);
								Application.Login.logout(application);
							}
						}

						application.dispose();
					}

				} else {
					if (Application.Login != null) {
						if (Application.account != null) {
							Application.Login.setAllLogout(true);
							Application.Login.logout(application);
						}
					}

					application.dispose();
				}
			}
		}

		// -----------------------
		final class AccountMgntCommand extends Command {
			boolean userEnabled;

			public AccountMgntCommand(String name) {
				super(name);
				super.setLoginNeeds(true);
				super.setSuperUserOnly(true);
				userEnabled = true;
			}

			public void setUserEnabled(boolean aFlag) {
				userEnabled = aFlag;
			}

			@Override
			public void entryPoint(ActionEvent ae) {
				if (ae == null)
					return;
				AccountMgmtDialog dialog = null;

				Account account = Application.account;
				if (account == null) {
					ToolKit tool = ToolKit.getInstance();
					JOptionPane.showMessageDialog(Application.Frame,
							tool.getString("MSG_NOT_LOGIN"),
							tool.getString("WARNING"), Application.WARNING);

					return;
				}

				if (!account.isSuperUser() && !userEnabled) {
					ToolKit tool = ToolKit.getInstance();
					JOptionPane.showMessageDialog(Application.Frame,
							tool.getString("MSG_ACCOUNG_MGMT_UNAVAILABLE"),
							tool.getString("WARNING"),
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				dialog = createAccountMgmtDialog(Application.Frame,
						Application.Login);
				if (dialog == null)
					return;

				dialog.pack();
				dialog.setSize(500, 200);
				dialog.setMinimumSize(new Dimension(500, 200));
				dialog.setLocationRelativeTo(Application.Frame);
				dialog.setVisible(true);
			}

			protected AccountMgmtDialog createAccountMgmtDialog(Frame owner,
					LoginManager manager) {
				AccountMgmtDialog dialog = new AccountMgmtDialog(
						Application.Frame, manager);
				if (!dialog.load()) {
					return null;
				}
				dialog.initComopnents();
				return dialog;
			}

		}

		final class Switch extends ToggledCommand {

			private JComponent comp;

			Switch(String name, JComponent component) {
				super(name);
				comp = component;
			}

			public void entryPoint(ActionEvent ae) {
				if (ae.getSource() != this)
					return;

				comp.setVisible(super.isSelected());
				super.setSelected(super.isSelected());
			}
		}

		// ------------------------"Preference" default
		// command-----------------------------------------------------------------//
		final class Preference extends Command {
			private String name;

			Preference(String name) {
				super(name);
				this.name = name;
			}

			public void entryPoint(ActionEvent ae) {
				JDialog dialog = new JDialog(application, name, true);
				PreferencePane mainPanel = new PreferencePane(dialog,
						application);
				dialog.getContentPane().add(mainPanel);
				dialog.setResizable(false);
				dialog.pack();
				dialog.setLocationRelativeTo(application);
				dialog.setVisible(true);
			}
		}

		final class CascadeArrange extends Command {

			CascadeArrange(String name) {
				super(name);
			}

			/**
			 * Arranges the Internal Windows like cascade style.
			 */
			public void entryPoint(ActionEvent ae) {
				// ViewMediator.getCurrentDeskTopPane() is called.
				ScrollableDesktopPane pane = getCurrentDeskTopPane();
				InternalWindow[] windows = pane.getAllInternalWindows();
				if (windows == null || windows.length == 0)
					return;

				int xVal = 0, yVal = 0, space = 25, displace = 30;
				int windowWidth = 450, windowHeight = 400;
				for (int i = 0; i < windows.length; i++) {
					try {
						if (windows[i].isIcon()) {
							windows[i].setIcon(false);
						}
						if (windows[i].isMaximizable()) {
							windows[i].setMaximum(false);
						}
						windows[i].setSelected(true);
					} catch (Exception exp) {
						// do nothing.
					}
					if (windows[i].isResizable()) {
						windows[i].setBounds(xVal, yVal, windowWidth,
								windowHeight);
					} else {
						windows[i].setBounds(xVal, yVal, windows[i].getWidth(),
								windows[i].getHeight());
					}
					xVal += space;
					yVal += space;
					if (yVal + windowHeight > pane.getSize().height) {
						yVal = 0;
					}
					if (xVal + windowWidth > pane.getSize().width) {
						xVal = displace;
						yVal = 0;
						displace += 50;
					}
					windows[i].toFront();
				}
			}
		}

		final class TileArrange extends Command {

			TileArrange(String name) {
				super(name);
			}

			/**
			 * Arranges the Internal Windows like tile style.
			 */
			public void entryPoint(ActionEvent ae) {
				// ViewMediator.getCurrentDeskTopPane() is called.
				ScrollableDesktopPane pane = getCurrentDeskTopPane();
				InternalWindow[] windows = pane.getAllInternalWindows();
				if (windows == null || windows.length == 0)
					return;

				int noOfFrames = windows.length;

				int root = (int) Math.sqrt(noOfFrames);
				int r2 = root * root;
				if (r2 == noOfFrames) {
					this.doPlacement(windows, root, root);
				} else {
					int rows = noOfFrames / root;
					int cols = root;
					this.doPlacement(windows, rows, cols);
				}
			}

			// Plases Internal Windows, used by doTileArrange() method.
			private void doPlacement(InternalWindow[] windows, int rows,
					int cols) {
				// ViewMediator.getCurrentDeskTopPane() is called.
				ScrollableDesktopPane pane = getCurrentDeskTopPane();
				int windowWidth = pane.getSize().width / cols;
				int windowHeight = (pane.getSize().height) / rows;
				boolean lastLine = false;

				int xVal = 0, yVal = 0;
				for (int i = 0; i < windows.length; i++) {
					try {
						if (windows[i].isIcon()) {
							windows[i].setIcon(false);
						}
						if (windows[i].isMaximizable()) {
							windows[i].setMaximum(false);
						}
					} catch (Exception exp) {
						// do nothing.
					}
					int win = i + 1;
					int currentRow = ((i / cols) + 1);
					if (currentRow >= rows && !lastLine) {
						lastLine = true;
						windowWidth = pane.getSize().width
								/ (windows.length - i);
					}
					if (windows[i].isResizable()) {
						windows[i].setBounds(xVal, yVal, windowWidth,
								windowHeight);
					} else {
						windows[i].setBounds(xVal, yVal, windows[i].getWidth(),
								windows[i].getHeight());
					}
					xVal += windowWidth;
					if (win % cols == 0) {
						if (!lastLine) {
							xVal = 0;
							yVal += windowHeight;
						}
					}
				}
			}
		}

		final class AllIconify extends Command {

			AllIconify(String name) {
				super(name);
			}

			/**
			 * Iconifies all InternalWindows
			 */
			public void entryPoint(ActionEvent ae) {
				// ViewMediator.getCurrentDeskTopPane() is called.
				ScrollableDesktopPane pane = getCurrentDeskTopPane();
				InternalWindow[] windows = pane.getAllInternalWindows();
				if (windows == null || windows.length == 0)
					return;
				for (int i = 0; i < windows.length; i++) {
					if (!windows[i].isIcon()) {
						try {
							windows[i].setIcon(true);
						} catch (Exception e) {
							// do nothing.
						}
					}
				}
			}
		}

		final class AllDeiconify extends Command {

			AllDeiconify(String name) {
				super(name);
			}

			/**
			 * De-Iconifies all InternalWindows
			 */
			public void entryPoint(ActionEvent ae) {
				// ViewMediator.getCurrentDeskTopPane() is called.
				ScrollableDesktopPane pane = getCurrentDeskTopPane();
				InternalWindow[] windows = pane.getAllInternalWindows();
				if (windows == null || windows.length == 0)
					return;
				for (int i = 0; i < windows.length; i++) {
					try {
						if (windows[i].isIcon()) {
							windows[i].setIcon(false);
						}
						if (windows[i].isMaximizable()) {
							windows[i].setMaximum(false);
						}
					} catch (Exception e) {
						// do nothing.
					}
				}
			}
		}

		final class AllMaximize extends Command {

			AllMaximize(String name) {
				super(name);
			}

			/**
			 * Maxmizes all InternalWindows
			 */
			public void entryPoint(ActionEvent ae) {
				// ViewMediator.getCurrentDeskTopPane() is called.
				ScrollableDesktopPane pane = getCurrentDeskTopPane();
				InternalWindow[] windows = pane.getAllInternalWindows();
				if (windows == null || windows.length == 0)
					return;
				for (int i = 0; i < windows.length; i++) {
					try {
						if (windows[i].isIcon()) {
							windows[i].setIcon(false);
						}
						if (windows[i].isMaximizable()) {
							windows[i].setMaximum(true);
						}
					} catch (Exception e) {
						// do nothing.
					}
				}
			}
		}

		final class AllClose extends Command {

			AllClose(String name) {
				super(name);
			}

			/**
			 * Maxmizes all InternalWindows
			 */
			public void entryPoint(ActionEvent ae) {
				// ViewMediator.getCurrentDeskTopPane() is called.
				ScrollableDesktopPane pane = getCurrentDeskTopPane();
				InternalWindow[] windows = pane.getAllInternalWindows();
				if (windows == null || windows.length == 0)
					return;
				for (int i = 0; i < windows.length; i++) {
					windows[i].dispose();
				}
			}
		}

		final class WindowEntry extends Command {

			private InternalWindow win;

			WindowEntry(String name, InternalWindow window) {
				super(name);
				win = window;
			}

			public void entryPoint(ActionEvent ae) {
				toFront(win); // ViewMediator.toFront() is called.
			}
		}

		final class HelpContents extends Command {
			private String help_url;
			private boolean isFile;

			HelpContents(String name, String helpURL, boolean isFile) {
				super(name);
				help_url = helpURL;
				this.isFile = isFile;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.cysols.framework.platform.Command#entryPoint(java.awt.event
			 * .ActionEvent)
			 */
			public void entryPoint(ActionEvent ae) {
				try {
					if (isFile) {
						if (Desktop.isDesktopSupported()) {
							File myFile = new File(help_url);
							Desktop.getDesktop().open(myFile);
						}
					} else {
						BrowserLauncher.openURL(help_url);
					}
				} catch (Exception e) {
					ToolKit tool = ToolKit.getInstance();
					String message = tool.getString("MSG_HELP_ERR");
					message = message + System.getProperty("line.separator")
							+ "\"" + help_url + "\"";
					application.showMessageDialog(message, Application.WARNING);
				}
			}
		}

		// ------------------------"About" default
		// command-----------------------------------------------------------------//
		final class About extends Command {
			private ImageIcon image;
			private String version;
			private String build;
			private JPanel optionPanel;

			About(String name, ImageIcon displayImage, String version,
					String build) {
				super(name);
				this.image = displayImage;
				this.optionPanel = null;
				this.version = version;
				this.build = build;
			}

			About(String name, ImageIcon displayImage, JPanel panel,
					String version, String build) {
				super(name);
				this.image = displayImage;
				this.optionPanel = panel;
				this.version = version;
				this.build = build;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.cysols.framework.platform.Command#entryPoint(java.awt.event
			 * .ActionEvent)
			 */
			public void entryPoint(ActionEvent ae) {
				JPanel tmpPanel = new JPanel();
				final SplashScreen aboutScreen = new SplashScreen(application,
						image, tmpPanel);

				JLabel versionLabel = new JLabel(version);
				JLabel buildLabel = new JLabel(build);
				JPanel messagePanel = new JPanel(new BorderLayout());
				messagePanel.add(versionLabel, BorderLayout.NORTH);
				messagePanel.add(buildLabel, BorderLayout.SOUTH);
				messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5,
						5));

				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						aboutScreen.hideSplashScreen();

						// ViewMediator's methods are called.
						if (getCurrentDeskTopPane().getSelectedWindow() == null) {
							changeStatus(Color.BLACK);
						} else {
							changeStatus(Color.GREEN);
						}
					}
				});

				if (optionPanel == null) {
					tmpPanel.add(okButton);
					tmpPanel.add(messagePanel);
				} else {
					JPanel lowerPanel = new JPanel();
					lowerPanel.add(okButton);
					lowerPanel.add(messagePanel);

					tmpPanel.setLayout(new BorderLayout());
					tmpPanel.add(optionPanel, BorderLayout.CENTER);
					tmpPanel.add(lowerPanel, BorderLayout.SOUTH);
				}
				aboutScreen.show();
			}
		}

		// ---------- Inner LicenseEntry Command class
		// ------------------------------------------------
		class LicenseEntry extends Command implements ActionListener,
				PropertyChangeListener {

			private String name;
			private LicenseManager registry;

			private JDialog dialog;

			/**
			 * 
			 * @param name
			 * @param registry
			 */
			public LicenseEntry(String name, LicenseManager registry) {
				super(name);
				this.name = name;
				this.registry = registry;
				super.setSuperUserOnly(true);
				this.registry.addPropertyChangeListener(this);
				if (Application.Login != null)
					super.setLoginNeeds(true);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.cysols.framework.platform.Command#entryPoint(java.awt.event
			 * .ActionEvent)
			 */
			public void entryPoint(ActionEvent ae) {
				if (application.Login != null) {
					Account account = Application.account;
					if (account == null || !account.isSuperUser()) {
						ToolKit tool = ToolKit.getInstance();
						application.showMessageDialog(
								tool.getString("MSG_LICENSE_UNAVAILABLE"),
								Application.WARNING);
						return;
					}
				}

				dialog = new JDialog(application, name, true);
				LicenseEntryPane mainPanel = new LicenseEntryPane(registry);
				PluginCommand[] commands = application.Engine
						.getPluggedCommands();
				PluginModule[] modules = application.Engine.getPluggedModules();
				mainPanel.initialize(commands, modules);
				mainPanel.addCloseActionListener(this);
				dialog.getContentPane().add(mainPanel);

				// dialog.setResizable(false);
				dialog.pack();
				dialog.setSize((int) dialog.getSize().getWidth(), (int) (dialog
						.getSize().getHeight() / 2));
				dialog.setLocationRelativeTo(application);
				dialog.setVisible(true);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent ae) {
				if (ae == null)
					return;
				String action = ae.getActionCommand();
				if (action.equals("CLOSE")) {
					dialog.dispose();
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
			 * PropertyChangeEvent)
			 */
			public void propertyChange(PropertyChangeEvent pce) {
				if (pce == null)
					return;
				String type = pce.getPropertyName();

				if (type.equalsIgnoreCase("ENABLED")) {
					Boolean enabled = (Boolean) pce.getNewValue();
					super.setEnabled(enabled.booleanValue());
				}
			}

		}

	} // End of DefaultCommand class

} // End of ViewMediator class
