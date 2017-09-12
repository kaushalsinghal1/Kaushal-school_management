package com.banti.framework.platform;

import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.banti.framework.core.InitializationFailedException;
import com.banti.framework.platform.ViewMediator.DefaultCommand.Login;
import com.banti.framework.platform.ViewMediator.DefaultCommand.Logout;
import com.banti.framework.platform.ViewMediator.DefaultCommand.Print;
import com.banti.framework.platform.ViewMediator.DefaultCommand.PrintPreview;
import com.banti.framework.platform.ViewMediator.DefaultCommand.Switch;
import com.banti.framework.platform.ViewMediator.DefaultCommand.WindowEntry;
import com.banti.framework.theme.ToolKit;
import com.banti.framework.utils.BrowserLauncher;

/**
 * TODO
 */
final class MenuBar implements ViewColleague {

	private JMenuBar menuBar;
	private JToolBar toolBar;

	private InnerMenu windowMenu;

	private FileModule fileModule;
	private WindowModule windowModule;
	private ToolModule toolModule;

	private ViewMediator view;
	private ModuleSchemeFactory factory;
	private RuntimeEngine runtimeEngine;

	private HashMap windowLists;

	private ImageIcon onIcon;
	private ImageIcon offIcon;
	private ImageIcon emptyCmdIcon;
	private String ON_IMAGE = "/com/banti/framework/platform/images/miniOn.gif";
	private String OFF_IMAGE = "/com/banti/framework/platform/images/miniOff.gif";
	private String EMPTY_CMD_IMAGE = "/banti/cysols/framework/platform/images/emptycmd.gif";
	private Stack menuStack;

	private PropertyChangeListener listener;

	MenuBar(ViewMediator mediator, ModuleSchemeFactory factory) {
		view = mediator;
		this.factory = factory;
		runtimeEngine = Application.Engine;

		windowLists = new HashMap();
		windowLists.put(ViewMediator.TopScreenTitle,
				new InnerWindowMenuItemList());

		menuBar = new JMenuBar();
		menuBar.setVisible(false);

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setVisible(false);
		toolBar.setBorder(BorderFactory.createEtchedBorder());
		toolBar.setMargin(new Insets(0, 10, 0, 10));

		ToolKit tool = ToolKit.getInstance();
		onIcon = tool.createImageIcon(ON_IMAGE);
		offIcon = tool.createImageIcon(OFF_IMAGE);
		emptyCmdIcon = tool.createImageIcon(EMPTY_CMD_IMAGE);

		menuStack = new Stack();

		listener = new InnerPropertyChangeListener();

	}

	public void windowOpened(InternalWindow window) {
		// For title changed
		window.addPropertyChangeListener(listener);

		// Adding window menu as a new window menu.
		String title = view.getCurrentDeskTopTitle();
		if (windowLists.get(title) == null)
			windowLists.put(title, new InnerWindowMenuItemList());

		InnerWindowMenuItemList list = (InnerWindowMenuItemList) windowLists
				.get(title);
		if (list.getMenuItems().length == 0)
			windowMenu.addSeparator();

		if (window.getTitle() == null | window.getTitle().equals(""))
			window.setTitle("Window");

		WindowEntry entry = view.CommandBuilder.createWindowEntry(window);
		entry.setVisible(true);
		InnerMenuItem menuItem = new InnerMenuItem(entry.getRuntimeAction());

		windowMenu.add(menuItem);
		list.add(window, menuItem);

		windowModule.setAllEnabled(true);
	}
	
	public void selectMenu(){
		((JMenu)menuBar.getComponent(0)).doClick();
	}

	public void windowClosed(InternalWindow window) {
		String title = view.getCurrentDeskTopTitle();

		if (windowLists.get(title) == null)
			return;

		InnerWindowMenuItemList list = (InnerWindowMenuItemList) windowLists
				.get(title);

		if (list == null)
			return;
		if (!list.contains(window))
			return;

		InnerMenuItem menuItem = list.getMenuItem(window);
		windowMenu.remove(menuItem);
		list.remove(window);

		if (list.getMenuItems().length == 0) {
			// When there is no window, a separator is removed.
			windowMenu.remove(windowMenu.getItemCount() - 1);

			windowModule.setAllEnabled(false);
		}
	}

	public void windowSelected(InternalWindow window) {
		// do nothing.
	}

	public void tabCreated(String title) {
		windowLists.put(title, new InnerWindowMenuItemList());
		fileModule.removeTab.setEnabled(true);
	}

	public void tabClosed(String title) {
		// InnerWindowMenuItemList is removed.
		windowLists.remove(title);

		if (windowLists.size() == 1) {
			fileModule.removeTab.setEnabled(false);
		}
	}

	public void tabChanged(String title) {
		// If main tab is selected, "remove tab" gets disabled.
		if (ViewMediator.TopScreenTitle.equals(title)) {
			fileModule.removeTab.setEnabled(false);
		} else {
			fileModule.removeTab.setEnabled(true);
		}

		// Re-arranges window menu.
		windowMenu.removeAll();

		Command[] cmds;
		cmds = windowModule.getCommands();

		if (cmds != null && cmds.length > 0) {
			menuStack.push(windowMenu);

			for (int i = 0; i < cmds.length; i++) {
				if (cmds[i] == null)
					continue;

				if (cmds[i].needsSeparator() && i != 0)
					windowMenu.addSeparator();

				cmds[i].accept(this);
			}

			menuStack.pop();
		}

		InnerWindowMenuItemList list = (InnerWindowMenuItemList) windowLists
				.get(title);
		if (list == null)
			return;

		// If there are some window in the new selected tab,
		// InnerWindowMenuItemList of the
		// selected index are obtained and those window menu items are
		// re-registered in the
		// window menu.
		InnerMenuItem[] menus;
		menus = list.getMenuItems();

		if (menus.length == 0) {
			windowModule.setAllEnabled(false);
		} else {
			windowMenu.addSeparator();
			for (int i = 0; i < menus.length; i++) {
				windowMenu.add(menus[i]);
			}
			windowModule.setAllEnabled(true);
		}
	}

	public void show() {
		menuBar.setVisible(true);
		Switch barSwitch = view.CommandBuilder.getToolBarSwitch();
		if (barSwitch != null) {
			toolBar.setVisible(barSwitch.isSelected());
		} else {
			toolBar.setVisible(true);
		}
	}

	// ----------------------- non-public methods
	// --------------------------------------------------------------//

	void init() throws InitializationFailedException {
		init(false);
	}

	void init(boolean logo) throws InitializationFailedException {
		try {
			ToolKit tool = ToolKit.getInstance();
			menuBar.setToolTipText(tool.getString("MENU_BAR_TOOL_TIP"));
			toolBar.setToolTipText(tool.getString("TOOL_BAR_TOOL_TIP"));

			// File Menu
			// ----------------------------------------------------------------------------------
			/*
			 * FileModule fileModule is defined as an instance variable since
			 * Exit is a special command.
			 */
			fileModule = new FileModule(factory.createFileModuleScheme(), view);
			fileModule.accept(this);

			// Edit Menu
			// ----------------------------------------------------------------------------------
			/*
			 * EditModule editModule is defined as an instance variable since
			 * copy, cut and paste are special commands
			 */
			Module editModule = new EditModule(factory.createEditModuleScheme());
			editModule.accept(this);

			// View Menu
			// ----------------------------------------------------------------------------------
			Module viewModule = new ViewModule(
					factory.createViewModuleScheme(), view);
			viewModule.accept(this);

			// Tool Menu
			// ----------------------------------------------------------------------------------
			/*
			 * Tool Menu is for PluginCommand. Those commands must be activated
			 * by RuntimeEngine. RuntimeEngine validate its loaded plugins.
			 */
			toolModule = new ToolModule(factory.createToolModuleScheme());
			runtimeEngine.activateToolModule(this, toolModule);

			// Plugged Menu
			// -------------------------------------------------------------------------------
			runtimeEngine.activatePluggedModule(this);

			// Option Menu
			// ----------------------------------------------------------------------------------
			Module optionModule = new OptionModule(
					factory.createOptionModuleScheme(), view);
			optionModule.accept(this);

			// Window Menu
			// ----------------------------------------------------------------------------------
			/*
			 * WindowtModule windowtModule is defined as an instance variable
			 * since cascade, tile and all iconify are special commands
			 */
			windowModule = new WindowModule(factory.createWindowModuleScheme(),
					view);
			windowModule.accept(this);

			// Help Menu
			Module helpModule = new HelpModule(
					factory.createHelpModuleScheme(), view);
			helpModule.accept(this);

			/*
			 * FileModule.Exit is attached as a tool button in the last of
			 * ToolBarif Button is enabled.
			 */
			if (fileModule.Exit.isButtonEnabled()) {
				toolBar.add(Box.createHorizontalGlue());

				InnerButton button = new InnerButton(
						fileModule.Exit.getRuntimeAction());
				button.setName(null);
				toolBar.add(button);
			}

			if (logo) {
				final JButton logoButton = new JButton(
						tool.createImageIcon("/com/banti/framework/platform/images/CyberLogo_m.gif"));
				logoButton.setSize(1, 1);
				logoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
				logoButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						try {
							BrowserLauncher.openURL(Constant.WEB_URL);
						} catch (Exception e) {
							// do nothing.
						}
					}
				});
				logoButton.setMargin(new Insets(0, -2, 0, -2));
				logoButton.setBorder(BorderFactory
						.createEmptyBorder(1, 1, 1, 1));
				logoButton.setRolloverEnabled(true);
				menuBar.add(Box.createHorizontalGlue());
				menuBar.add(logoButton);

			}

		} catch (InitializationFailedException e) {
			throw e;

		} catch (Exception e) {
			// All Exception when it is thrown from init() is thrown as
			// InitializationFailedEXception.
			throw new InitializationFailedException(
					"MenuBar initialization failed.", e);
		}
	}


	private void connectLoginManager(Command command) {
		if (Application.Login == null)
			return;

		if (command.needsLogin()) {
			Application.Login.registerCommand(command);
		}
		this.plugListenerToLoginManager(command);
	}

	private void plugListenerToLoginManager(Object obj) {
		if (Application.Login == null)
			return;
		if (obj == null)
			return;

		/*
		 * if (Application.Login instanceof RemoteLoginManager) {
		 * RemoteLoginManager rLoginManager = (RemoteLoginManager)
		 * Application.Login; if (obj instanceof RemoteLoginListener) {
		 * RemoteLoginListener listener = (RemoteLoginListener) obj;
		 * rLoginManager.addRemoteLoginListener(listener); } if (obj instanceof
		 * RemoteLogoutListener) { RemoteLogoutListener listener =
		 * (RemoteLogoutListener) obj;
		 * rLoginManager.addRemoteLogoutListener(listener); } }
		 */

		if (obj instanceof LoginListener) {
			LoginListener listener = (LoginListener) obj;
			Application.Login.addLoginListener(listener);
		}
		if (obj instanceof LogoutListener) {
			LogoutListener listener = (LogoutListener) obj;
			Application.Login.addLogoutListener(listener);
		}
	}

	void visit(Command command) {
		InnerMenu currentMenu = (InnerMenu) menuStack.peek();
		currentMenu.add(new InnerMenuItem(command.getRuntimeAction()));
		if (command.isButtonEnabled()) {
			if (command.needsSeparator())
				toolBar.addSeparator();
			toolBar.add(new InnerButton(command.getRuntimeAction()));
		}

		this.connectLoginManager(command);
	}

	void visit(ToggledCommand command) {
		InnerMenu currentMenu = (InnerMenu) menuStack.peek();
		currentMenu.add(new InnerCheckBoxMenuItem(command.getRuntimeAction()));
		if (command.isButtonEnabled()) {
			if (command.needsSeparator())
				toolBar.addSeparator();
			toolBar.add(new InnerToggleButton(command.getRuntimeAction()));
		}

		this.connectLoginManager(command);
	}

	void visit(PluginCommand command) {
		InnerMenu currentMenu = (InnerMenu) menuStack.peek();
		runtimeEngine.activate(command);
		currentMenu.add(new InnerMenuItem(command.getRuntimeAction()));
		if (command.isButtonEnabled()) {
			if (command.needsSeparator())
				toolBar.addSeparator();
			toolBar.add(new InnerButton(command.getRuntimeAction()));
		}

		this.connectLoginManager(command);
	}

	void visit(PluginToggledCommand command) {
		InnerMenu currentMenu = (InnerMenu) menuStack.peek();
		runtimeEngine.activate(command);
		currentMenu.add(new InnerCheckBoxMenuItem(command.getRuntimeAction()));
		if (command.isButtonEnabled()) {
			if (command.needsSeparator())
				toolBar.addSeparator();
			toolBar.add(new InnerToggleButton(command.getRuntimeAction()));
		}

		this.connectLoginManager(command);
	}

	void visit(Module module) {
		InnerMenu menu = new InnerMenu(module.getRuntimeAction());
		menuStack.push(menu);

		Command[] cmds;
		cmds = module.getCommands();
		if (cmds != null && cmds.length > 0) {

			for (int i = 0; i < cmds.length; i++) {
				if (cmds[i] == null)
					continue;
				if (cmds[i].needsSeparator() && i != 0)
					menu.addSeparator();

				cmds[i].accept(this);
			}
		}

		menu = (InnerMenu) menuStack.pop();
		if (menuStack.isEmpty()) {
			menuBar.add(menu);
		} else {
			InnerMenu parentMenu = (InnerMenu) menuStack.peek();
			parentMenu.add(menu);
		}

		this.connectLoginManager(module);
	}

	void visit(PluginModule module) {
		InnerMenu menu = new InnerMenu(module.getRuntimeAction());
		menuStack.push(menu);

		Command[] cmds;
		cmds = module.getCommands();
		if (cmds != null && cmds.length > 0) {
			for (int i = 0; i < cmds.length; i++) {
				if (cmds[i] == null)
					continue;
				if (cmds[i].needsSeparator() && i != 0)
					menu.addSeparator();

				cmds[i].accept(this);
			}
		}

		menu = (InnerMenu) menuStack.pop();
		if (menuStack.isEmpty()) {
			menuBar.add(menu);
		} else {
			InnerMenu parentMenu = (InnerMenu) menuStack.peek();
			parentMenu.add(menu);
		}

		this.connectLoginManager(module);
	}

	void visit(OptionModule optionModule) {
		System.out.println("Option Module called");
	}

	void visit(FileModule fileModule) {
		InnerMenu fileMenu = new InnerMenu(fileModule.getRuntimeAction());
		menuStack.push(fileMenu);

		ToolKit tool = ToolKit.getInstance();
		if (Application.Login != null) {
			Login login = view.CommandBuilder.new Login(
					Application.Login.LOGIN_MENU_NAME, Application.Login);
			login.setButtonEnabled(true);
			login.accept(this);

			Logout logout = view.CommandBuilder.new Logout(
					Application.Login.LOGOUT_MENU_NAME, Application.Login);
			logout.setButtonEnabled(true);
			logout.accept(this);
		}

		Command[] cmds;
		cmds = fileModule.getCommands();

		if (cmds != null && cmds.length > 0) {
			for (int i = 0; i < cmds.length; i++) {
				if (cmds[i] == null)
					continue;

				if (cmds[i].needsSeparator() && cmds[i].isVisiable())
					fileMenu.addSeparator();

				cmds[i].accept(this);
			}
		}

		// Print Menu Initialization
		if (fileModule.Printable || fileModule.Previewable) {

			PrintController pController = new PrintController(view);
			fileMenu.addMenuListener(pController.new PrintMenuArranger());
			fileMenu.addSeparator();

			if (fileModule.Printable) {
				Print print = view.CommandBuilder.new Print(
						tool.getString("PRINT"));
				print.setIcon(tool
						.createImageIcon("/com/banti/framework/platform/images/print.gif"));
				pController.set(print);
				print.accept(this);
				if (Application.Login != null)
					print.setEnabled(false);
			}

			if (fileModule.Previewable) {
				PrintPreview preview = view.CommandBuilder.new PrintPreview(
						tool.getString("PREVIEW"));
				preview.setIcon(tool
						.createImageIcon("/com/banti/framework/platform/images/preview.gif"));
				pController.set(preview);
				preview.accept(this);
				if (Application.Login != null)
					preview.setEnabled(false);
			}
		}

		// Exit Menu initialization
		InnerMenu menu = (InnerMenu) menuStack.pop();
		if (menuStack.isEmpty()) {
			if (fileModule.Exit.needsSeparator())
				fileMenu.addSeparator();
			fileMenu.add(new InnerMenuItem(fileModule.Exit.getRuntimeAction()));
			menuBar.add(fileMenu);
		} else {
			InnerMenu parentMenu = (InnerMenu) menuStack.peek();
			parentMenu.add(menu);
		}

		this.connectLoginManager(fileModule);
	}

	void visit(WindowModule windowModule) {
		windowMenu = new InnerMenu(windowModule.getRuntimeAction());
		menuStack.push(windowMenu);

		Command[] cmds;
		cmds = windowModule.getCommands();

		if (cmds != null && cmds.length > 0) {
			for (int i = 0; i < cmds.length; i++) {
				if (cmds[i] == null)
					continue;
				if (cmds[i].needsSeparator() && i != 0)
					windowMenu.addSeparator();

				cmds[i].accept(this);
			}
		}

		InnerMenu menu = (InnerMenu) menuStack.pop();
		if (menuStack.isEmpty()) {
			windowMenu.addMenuListener(new InnerWindowMenuArranger());
			menuBar.add(menu);
		} else {
			InnerMenu parentMenu = (InnerMenu) menuStack.peek();
			parentMenu.add(menu);
		}
	}

	void repaint() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				menuBar.repaint();
				toolBar.repaint();
			}
		});
	}

	JMenuBar Menu() {
		return menuBar;
	}

	JToolBar Tools() {
		return toolBar;
	}

	// -----------------------------Inner Classes
	// ------------------------------------------------------------------//

	// -----------------------------InnerWindowMenuItemList-------------------------------------------------//
	/**
	 * This is a private inner class, is a ValueObject and keeps the pairs of
	 * InternalWindow instances and its window MenuItem in "Window" menu.
	 */
	private class InnerWindowMenuItemList {
		private ArrayList winList;
		private HashMap winMenuItems;

		InnerWindowMenuItemList() {
			winList = new ArrayList();
			winMenuItems = new HashMap();
		}

		void add(InternalWindow window, InnerMenuItem menu) {
			winList.add(window);
			winMenuItems.put(window, menu);
		}

		boolean contains(InternalWindow window) {
			return winList.contains(window);
		}

		InnerMenuItem getMenuItem(InternalWindow window) {
			return (InnerMenuItem) winMenuItems.get(window);
		}

		void remove(InternalWindow window) {
			winMenuItems.remove(window);
			winList.remove(window);
		}

		InnerMenuItem[] getMenuItems() {
			InnerMenuItem[] menus = new InnerMenuItem[winList.size()];
			for (int i = 0; i < winList.size(); i++) {
				menus[i] = (InnerMenuItem) winMenuItems.get(winList.get(i));
			}

			return menus;
		}

		void changeIcons() {
			for (int i = 0; i < winList.size(); i++) {
				InternalWindow win = (InternalWindow) winList.get(i);
				if (win == null)
					continue;

				InnerMenuItem menuItem = (InnerMenuItem) winMenuItems.get(win);
				if (menuItem == null)
					continue;

				if (win.isSelected()) {
					menuItem.setIcon(onIcon);
				} else {
					menuItem.setIcon(offIcon);
				}
			}
		}
	}

	// -----------------------------InnerWindowMenuArranger-------------------------------------------------//
	private class InnerWindowMenuArranger implements MenuListener {

		InnerWindowMenuArranger() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.MenuListener#menuSelected(javax.swing.event.MenuEvent
		 * )
		 */
		public void menuSelected(MenuEvent me) {
			try {
				InnerWindowMenuItemList list = (InnerWindowMenuItemList) windowLists
						.get(view.getCurrentDeskTopTitle());

				if (list == null)
					return;
				list.changeIcons();

			} catch (IndexOutOfBoundsException ie) {
				return;
			}
		}

		public void menuCanceled(MenuEvent me) {
			// do nothing.
		}

		public void menuDeselected(MenuEvent me) {
			// do nothing.
		}
	}

	// -----------------------------InnerMenuItem----------------------------------------------//
	/**
     *
     */
	private class InnerMenuItem extends javax.swing.JMenuItem implements
			PropertyChangeListener {

		private Command.RuntimeAction action;

		InnerMenuItem(Command.RuntimeAction action) {
			super(action);
			this.action = action;
			super.setVisible(action.isVisible());
			super.setEnabled(action.isEnabled());
			action.addPropertyChangeListener(this);
		}

		public void propertyChange(PropertyChangeEvent e) {
			super.setEnabled(action.isEnabled());
			super.setVisible(action.isVisible());
		}
	}

	// -----------------------------InnerToggledMenuItem----------------------------------------------//
	/**
     *
     */
	private class InnerCheckBoxMenuItem extends javax.swing.JCheckBoxMenuItem
			implements PropertyChangeListener {

		private Command.RuntimeAction action;

		InnerCheckBoxMenuItem(Command.RuntimeAction action) {
			super(action);
			this.action = action;
			super.setVisible(action.isVisible());
			super.setEnabled(action.isEnabled());
			super.setSelected(action.isSelected());
			super.setIcon(null);
			action.addPropertyChangeListener(this);
		}

		protected void fireItemStateChanged(java.awt.event.ItemEvent ie) {
			if (ie == null)
				return;
			if (ie.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				if (action != null)
					action.setSelected(true);
			} else {
				if (action != null)
					action.setSelected(false);
			}

			super.fireItemStateChanged(ie);
		}

		public void propertyChange(PropertyChangeEvent e) {
			super.setEnabled(action.isEnabled());
			super.setVisible(action.isVisible());
			super.setSelected(action.isSelected());
		}
	}

	// -----------------------------InnerMenu----------------------------------------------//
	/**
     *
     */
	private class InnerMenu extends javax.swing.JMenu implements
			PropertyChangeListener {

		private Command.RuntimeAction action;

		InnerMenu(Command.RuntimeAction action) {
			super(action);
			this.action = action;
			super.setVisible(action.isVisible());
			super.setEnabled(action.isEnabled());
			action.addPropertyChangeListener(this);
		}

		public void propertyChange(PropertyChangeEvent e) {
			super.setEnabled(action.isEnabled());
			super.setVisible(action.isVisible());
		}
	}

	// -----------------------------InnerButton-----------------------------------------------------//
	private class InnerButton extends javax.swing.JButton implements
			PropertyChangeListener {

		private Command.RuntimeAction action;

		InnerButton(Command.RuntimeAction action) {
			super(action);
			// super.setMargin(new Insets(0, 0, 0, 0));

			this.action = action;
			super.setVisible(action.isButtonEnabled());
			super.setEnabled(action.isEnabled());
			super.setText(null);
			if (super.getIcon() == null)
				super.setIcon(emptyCmdIcon);

			action.addPropertyChangeListener(this);
		}

		public void setSelected(boolean aFlag) {
			super.setSelected(aFlag);
			if (action != null)
				action.setSelected(aFlag);
		}

		public void propertyChange(PropertyChangeEvent e) {
			super.setEnabled(action.isEnabled());
			super.setVisible(action.isVisible());
		}
	}

	// -----------------------------InnerToggleButton-----------------------------------------------------//
	private class InnerToggleButton extends javax.swing.JToggleButton implements
			PropertyChangeListener {

		private Command.RuntimeAction action;

		InnerToggleButton(Command.RuntimeAction action) {
			super(action);
			// super.setMargin(new Insets(0, 0, 0, 0));

			this.action = action;
			super.setVisible(action.isButtonEnabled());
			super.setEnabled(action.isEnabled());
			super.setSelected(action.isSelected());
			super.setText(null);
			if (super.getIcon() == null)
				super.setIcon(emptyCmdIcon);

			action.addPropertyChangeListener(this);
		}

		protected void fireItemStateChanged(java.awt.event.ItemEvent ie) {
			if (ie == null)
				return;
			if (ie.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				if (action != null)
					action.setSelected(true);
			} else {
				if (action != null)
					action.setSelected(false);
			}

			super.fireItemStateChanged(ie);
		}

		public void propertyChange(PropertyChangeEvent e) {
			super.setEnabled(action.isEnabled());
			super.setVisible(action.isVisible());
			super.setSelected(action.isSelected());
		}
	}

	// -----------------------------InnerPropertyChangeListener-------------------------------------------//
	private class InnerPropertyChangeListener implements PropertyChangeListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
		 * PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent pce) {
			if (pce == null)
				return;

			if (pce.getPropertyName().equals(InternalWindow.TITLE_PROPERTY)) {
				try {
					InternalWindow window = (InternalWindow) pce.getSource();
					String newTitle = (String) pce.getNewValue();
					String title = view.getCurrentDeskTopTitle();

					if (windowLists.get(title) == null)
						return;

					InnerWindowMenuItemList list = (InnerWindowMenuItemList) windowLists
							.get(title);

					if (!list.contains(window))
						return;

					InnerMenuItem menuItem = list.getMenuItem(window);
					menuItem.setText(newTitle);

				} catch (ClassCastException e) {
					return;
				}
			}
		}
	}

}
