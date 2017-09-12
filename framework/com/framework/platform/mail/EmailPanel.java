package com.framework.platform.mail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.banti.framework.cwt.ProgressCommunicator;
import com.banti.framework.cwt.ProgressCommunicatorMonitor;

public class EmailPanel extends JPanel implements ActionListener,
		ProgressCommunicator {
	private JLabel lblFromAddress;
	private JTextField txtFromAddress;

	private JLabel lblPassword;
	private JPasswordField txtPassword;

	private JLabel lblToAddress;
	private JTextField txtToAddress;

	private JLabel lblSubject;
	private JTextField txtSubject;

	private JLabel lblAttachments;
	private JList lstAttachments;
	private DefaultListModel listModel;
	private JMenuItem removeItem;
	private JPopupMenu removePopMenu;
	// private JProgressBar pgrAttachment;

	private JButton btnBrowse;
	private JButton btnClear;
	// private JButton btnAttach;

	private JLabel lblMessage;
	private JTextArea txtMessage;

	private JButton btnSend;

	private JPanel mailPanel;
	private JDialog dialog;

	public EmailPanel(JDialog dialog) {
		this();
		this.dialog = dialog;
	}

	public EmailPanel() {
		lblFromAddress = new JLabel("From");
		lblFromAddress.setBounds(20, 20, 75, 20);
		txtFromAddress = new JTextField(20);
		// txtFromAddress.setVisible(true);
		txtFromAddress.setBounds(100, 20, 200, 20);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(20, 50, 75, 20);
		txtPassword = new JPasswordField(20);
		txtPassword.setEchoChar('*');
		txtPassword.setBounds(100, 50, 200, 20);

		lblToAddress = new JLabel("To");
		lblToAddress.setBounds(20, 80, 75, 20);
		txtToAddress = new JTextField(20);
		txtToAddress.setBounds(100, 80, 200, 20);

		lblSubject = new JLabel("Subject");
		lblSubject.setBounds(20, 110, 75, 20);
		txtSubject = new JTextField(20);
		txtSubject.setBounds(100, 110, 200, 20);

		lblAttachments = new JLabel("Attachments");
		lblAttachments.setBounds(20, 140, 150, 20);
		listModel = new DefaultListModel();
		// listModel.addElement("");
		lstAttachments = new JList(listModel);
		lstAttachments
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		lstAttachments.setLayoutOrientation(JList.HORIZONTAL_WRAP);

		// progressbar
		/*
		 * pgrAttachment = new JProgressBar(0,100);
		 * pgrAttachment.setBounds(20,400, 400, 20);
		 * pgrAttachment.setValue(100); pgrAttachment.setStringPainted(true);
		 */

		// popmenu
		removePopMenu = new JPopupMenu();
		// popmenu item
		removeItem = new JMenuItem("remove");
		removeItem.addActionListener(this);
		removeItem.setActionCommand("removeItem");
		removePopMenu.add(removeItem);// adding item to popupmenu

		lstAttachments.setComponentPopupMenu(removePopMenu);

		JScrollPane attachmentScrollingArea = new JScrollPane(lstAttachments);
		attachmentScrollingArea.setBounds(20, 160, 300, 70);

		btnClear = new JButton("Clear");
		btnClear.setBounds(325, 185, 90, 20);
		btnClear.addActionListener(this);
		btnClear.setActionCommand("clear");

		btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(325, 210, 90, 20);
		btnBrowse.addActionListener(this);
		btnBrowse.setActionCommand("browse");

		lblMessage = new JLabel("Message");
		lblMessage.setBounds(20, 240, 150, 20);
		txtMessage = new JTextArea(6, 20);
		JScrollPane messageScrollingArea = new JScrollPane(txtMessage);
		messageScrollingArea.setBounds(20, 260, 400, 100);

		btnSend = new JButton("Send");
		btnSend.setBounds(345, 380, 75, 20);
		btnSend.addActionListener(this);
		btnSend.setActionCommand("send");

		// addind ui components to panel
		mailPanel = new JPanel();
		mailPanel.setLayout(null);
		mailPanel.setBounds(20, 20, 440, 320);
		mailPanel.add(lblFromAddress);
		mailPanel.add(txtFromAddress);
		mailPanel.add(lblPassword);
		mailPanel.add(txtPassword);
		mailPanel.add(lblToAddress);
		mailPanel.add(txtToAddress);
		mailPanel.add(lblAttachments);
		mailPanel.add(attachmentScrollingArea);
		mailPanel.add(btnClear);
		mailPanel.add(btnBrowse);

		mailPanel.add(lblSubject);
		mailPanel.add(txtSubject);
		mailPanel.add(lblMessage);
		mailPanel.add(messageScrollingArea);
		mailPanel.add(btnSend);
		// mailPanel.add(pgrAttachment);
		mailPanel.setBorder(BorderFactory.createTitledBorder(""));
		// setVisible(true);
		// this.add(mailPanel);

	}

	public JPanel createPanel() {
		// JPanel panel = new EmailPanel();
		return mailPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if ("removeItem".equals(arg0.getActionCommand())) {
			if (listModel.size() > 0) {
				if (lstAttachments.getSelectedIndex() >= 0) {
					listModel.remove(lstAttachments.getSelectedIndex());
				}
			} else {
				JOptionPane.showMessageDialog(this, "emty attachment  list",
						"Warning", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

		}
		if ("clear".equals(arg0.getActionCommand())) {
			listModel.clear();
		}
		if ("browse".equals(arg0.getActionCommand())) {
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String fileName = fc.getSelectedFile().getPath();
				if (listModel.size() == 0) {
					listModel.addElement(fileName);
				} else if (listModel.size() <= 2) {

					for (int i = 0; i < listModel.size(); i++) {
						if (fileName.equals(listModel.get(i).toString())) {
							JOptionPane.showMessageDialog(this,
									"File already selected", "Warning",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
					listModel.addElement(fileName);

				} else {
					JOptionPane.showMessageDialog(this,
							"Maximum 3 files can be attached", "Warning",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
		}
		if ("send".equals(arg0.getActionCommand())) {

			if (Validation.isValidEmailAddress(txtFromAddress.getText())) {

			} else {
				JOptionPane.showMessageDialog(this, "Invalid From Address",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (txtPassword.getText().length() > 0) {

			} else {
				JOptionPane.showMessageDialog(this, "Enter Password",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (Validation.isValidEmailAddress(txtToAddress.getText())) {

			} else {
				JOptionPane.showMessageDialog(this, "Invalid To Address",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (txtSubject.getText().length() > 0) {

			} else {
				JOptionPane.showMessageDialog(this, "Subject cannot be empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (txtMessage.getText().length() > 0) {

			} else {
				JOptionPane.showMessageDialog(this, "Message cannot be empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			new ProgressCommunicatorMonitor(dialog, "Mail Sending..", false,
					this).start();

		}

	}

	private String[] getAttachmentFileList() {
		String[] attachmentFilelist;
		if (listModel.getSize() == 0) {
			return null;
		}
		attachmentFilelist = new String[listModel.getSize()];
		for (int i = 0; i < listModel.getSize(); i++) {
			attachmentFilelist[i] = listModel.get(i).toString();
		}
		return attachmentFilelist;
	}

	boolean mailresult = false;

	@Override
	public void run() {
		Mail email = new Mail();
		char passwordChars[] = txtPassword.getPassword();
		String password = String.valueOf(passwordChars);
		mailresult = email.sendSimpleMail(txtFromAddress.getText(), password,
				txtToAddress.getText(), txtSubject.getText(),
				txtMessage.getText(), getAttachmentFileList());

	}

	@Override
	public void doPostProcess() {
		if (mailresult) {
			JOptionPane.showMessageDialog(this, "Email  send Successfully",
					"Alert", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Email  sending failed",
					"Alert", JOptionPane.WARNING_MESSAGE);
		}
		
	}

	@Override
	public void doCancel() {

	}

}
