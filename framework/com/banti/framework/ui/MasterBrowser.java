package com.banti.framework.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.banti.framework.platform.InternalWindow;

public abstract class MasterBrowser extends InternalWindow {

	JInternalFrame internalFrame;
	JPanel buttonPanel;
	JMenu executeMenu;
	JMenu searchMenu;
	JPanel middalePanel;
	public static int LAYRED_1 = 1;
	public static int LAYRED_2 = 2;
	protected boolean secondLayed = false;
	private JSplitPane mainSplitPane;

	public MasterBrowser(String title) {
		super(title, true, true, true, true);
		init();
	}

	private void init() {
		JPanel panel = new JPanel(new GridLayout(1, 0));
	//	JScrollPane rightPane = new JScrollPane(getMainSplitPane());
		initializeMenuBar();
		JSplitPane  rightPane=getMainSplitPane();
		JScrollPane leftPane = getTreeScrollPane();

		// Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(leftPane);
		splitPane.setRightComponent(getMainSplitPane());

		Dimension minimumSize = new Dimension(100, 50);
		leftPane.setMinimumSize(minimumSize);
		rightPane.setMinimumSize(new Dimension(300, 50));
		splitPane.setDividerLocation(200);
		splitPane.setPreferredSize(new Dimension(600, 100));

		// Add the split pane to this panel.
		panel.add(splitPane);
		getContentPane().add(panel);
	}

	public void setFramesize(JFrame frame) {
		Dimension d = frame.getSize();
		setSize(d.width - 60, d.height - 60);
		setLocation(new Point(0, 0));
	}

	private JPanel getRightPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(getTopPanel(), BorderLayout.NORTH);
		panel.add(getTableScrollPane(), BorderLayout.CENTER);
		panel.add(getButtonPanel(), BorderLayout.PAGE_END);
		return panel;
	}

	private JSplitPane getMainSplitPane() {
		mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane.setTopComponent(getRightPanel());
		return mainSplitPane;
	}

	public void addSecondLayed(Component component) {
		if (!secondLayed) {
			secondLayed = true;
//			JScrollPane scrollPane=new JScrollPane(component);
//			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			mainSplitPane.setBottomComponent(component);
			int height=(int) (mainSplitPane.getHeight()*0.6);
			mainSplitPane.setDividerLocation(height);
		//	mainSplitPane.setDividerLocation(400);
		}
		// return mainSplitPane;
	}

	public void removeSecondLayed() {
		if (secondLayed) {
			mainSplitPane.remove(LAYRED_2);
			secondLayed = false;
		}
		// return mainSplitPane;
	}

	private void initializeMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		searchMenu = new JMenu("Search");
		//searchMenu.add(new JMenuItem("Search"));
		executeMenu = new JMenu("Execute");
		executeMenu.add(new JMenuItem("Execute"));
		menuBar.add(executeMenu);
		menuBar.add(searchMenu);
		setJMenuBar(menuBar);
		setSearchMenuAction(getSearchAction());
	}

	protected void setSearchMenuAction(Action action) {
		if (action != null) {
			searchMenu.add(action);
		}
	}
	
	protected abstract Action getSearchAction();

	protected void setExecuteMenuItem(List<Action> list) {
		executeMenu.removeAll();
		for (Iterator<Action> iterator = list.iterator(); iterator
				.hasNext();) {
			Action action = iterator.next();
			executeMenu.add(action);
		}
	}

	protected void setSearchMenuEnable(boolean enable) {
		searchMenu.setEnabled(enable);
	}

	protected void setExecuteMenuEnable(boolean enable) {
		executeMenu.setEnabled(enable);

	}

	// protected void setExecuteAction(Action action);
	protected abstract JScrollPane getTreeScrollPane();

	protected abstract JPanel getTopPanel();

	protected abstract JPanel getButtonPanel();

	protected abstract JScrollPane getTableScrollPane();

	protected abstract void loadList();
}
