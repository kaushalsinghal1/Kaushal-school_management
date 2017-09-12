package com.banti.framework.tree;

import java.awt.Color;

public class SearchNode {
	private String nodeStr;
	private Color backColor;
	private Color foreColor;

	public SearchNode() {
		nodeStr = "Search";
		backColor = Color.MAGENTA;
		foreColor = Color.BLACK;
	}

	public String getNodeStr() {
		return nodeStr;
	}

	public void setNodeStr(String nodeStr) {
		this.nodeStr = nodeStr;
	}

	public Color getBackColor() {
		return backColor;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}

	public Color getForeColor() {
		return foreColor;
	}

	public void setForeColor(Color foreColor) {
		this.foreColor = foreColor;
	}
	public String toString() {
		return nodeStr;
	}

}
