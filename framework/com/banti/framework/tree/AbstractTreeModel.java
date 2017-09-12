package com.banti.framework.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.banti.framework.logging.LoggerFactory;

public abstract class AbstractTreeModel implements TreeCellRenderer {
	private static java.util.logging.Logger DEBUG = LoggerFactory.getInstance()
			.getLogger("APPLICATION");
	protected JTree tree;
	private TreeSelectionListener listener;
	private List objectList;
	private DefaultMutableTreeNode top;
	private SearchNode searchNode;
	private DefaultTreeModel treeModel;
	private boolean isSearched = false;
	private TreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

	public AbstractTreeModel(List objectList, TreeSelectionListener listener) {
		this.objectList = objectList;
		this.listener = listener;
		init();
		searchNode = new SearchNode();
		// add(tree);
	}

	private void init() {
		top = new DefaultMutableTreeNode(defaultObject());
		createClassNodes(top);
		treeModel = new DefaultTreeModel(top);
		tree = new JTree(treeModel);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(listener);
		tree.setCellRenderer(this);

	}

	public void addSearchNode() {
		if (!isSearched) {
			DefaultMutableTreeNode category = new DefaultMutableTreeNode(
					searchNode);
			treeModel.insertNodeInto(category, top, 0);
			top.getChildAt(0);
			isSearched = true;
		}
		setDefaultSelectionRow(1);
	}

	public void removeSearchNode() {
		// DefaultMutableTreeNode category = top.getChildAt(0);
		if (isSearched) {
			if (top.getChildCount() > 0) {
				treeModel.removeNodeFromParent((MutableTreeNode) top
						.getChildAt(0));
				isSearched = false;
			}
		}
	}

	public JTree getTree() {
		return tree;
	}

	public void setSearchNode(SearchNode searchNode) {
		this.searchNode = searchNode;
	}

	public void setSelectionNode(int index) {
		tree.setSelectionRow(index);
	}

	private void createClassNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
		for (Iterator<Object> iterator = objectList.iterator(); iterator
				.hasNext();) {
			Object object = iterator.next();
			category = new DefaultMutableTreeNode(object);
			top.add(category);
		}

	}

	public void setDefaultSelectionRow(int row) {
		if (row < objectList.size()) {
			tree.setSelectionRow(row);
		} else {
			DEBUG.log(Level.WARNING,
					"Selection Row index greater then class list size");
		}

	}

	public void setList(List<Object> list) {
		removeAllExceptTop();
		this.objectList = list;
		createClassNodes(top);
	}

	private void removeAllExceptTop() {
		if (objectList != null) {
			for (int i = 0; i < objectList.size(); i++) {
				try {
					tree.remove(i);
				} catch (ArrayIndexOutOfBoundsException e) {
					DEBUG.log(Level.WARNING, "Object is not exist in tree ."
							+ e.getLocalizedMessage());
				}
			}
		}
	}

	public abstract Object defaultObject();

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		Component returnValue = defaultRenderer.getTreeCellRendererComponent(
				tree, value, selected, expanded, leaf, row, hasFocus);
		((JComponent) returnValue).setOpaque(false);
		if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
			Object userObject = ((DefaultMutableTreeNode) value)
					.getUserObject();
			if (userObject instanceof SearchNode) {
				final SearchNode s = (SearchNode) userObject;
				// JLabel lb = new JLabel();
				((JComponent) returnValue).setOpaque(true);
				returnValue.setBackground(s.getBackColor());
				returnValue.setForeground(s.getForeColor());
				return returnValue;
			}
		}
		return returnValue;
	}

}
