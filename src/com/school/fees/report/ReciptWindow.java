package com.school.fees.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import com.school.console.SchoolMain;

public class ReciptWindow extends JDialog implements ActionListener {

	private ReciptModel model;

	public ReciptWindow(ReciptModel model) {
		super(SchoolMain.Frame, "Reciept Window", true);
		this.model = model;
		setLayout(new BorderLayout());
		add(getButtonPanel(), BorderLayout.NORTH);
		RecieptPanel recieptPanel = new RecieptPanel(model);
		recieptPanel.setBorder(BorderFactory.createEtchedBorder(1));
		recieptPanel.setPreferredSize(new Dimension(450, 450));
		add(recieptPanel, BorderLayout.CENTER);
		setSize(new Dimension(450, 480));
		setLocationRelativeTo(getRootPane());
		setVisible(true);
	}

	private JPanel getPanel(ReciptModel model) {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(getButtonPanel(), c);
		c.gridx = 0;
		c.gridy = 1;
		RecieptPanel recieptPanel = new RecieptPanel(model);
		recieptPanel.setBorder(BorderFactory.createEtchedBorder(1));
		recieptPanel.setPreferredSize(new Dimension(450, 450));
		recieptPanel.setSize(new Dimension(450, 450));
		panel.add(recieptPanel, c);
		return panel;
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton print = new JButton("Print");
		print.setActionCommand("PRINT");
		print.addActionListener(this);
		JButton printPreview = new JButton("Print Preview");
		printPreview.setActionCommand("PRINT_PREVIEW");
		printPreview.addActionListener(this);
		panel.add(print);
		panel.add(printPreview);
		panel.setBackground(Color.WHITE);
		return panel;
	}

	protected JRootPane createRootPane() {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				dispose();
			}
		};
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(actionListener, stroke,
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("PRINT".equals(e.getActionCommand())) {
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(new RecieptPrintable(model));
			boolean ok = job.printDialog();
			if (ok) {
				try {
					job.print();
				} catch (PrinterException ex) {
				}
			}
		} else if ("PRINT_PREVIEW".equals(e.getActionCommand())) {
			dispose();
			   new PrintPreview(new RecieptPrintable(model), new PageFormat());
		}

	}

}
