package com.school.setting;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.HibernateException;

import com.banti.framework.img.crop.CropImageDialog;
import com.banti.framework.img.crop.ImageFilter;
import com.banti.framework.img.crop.ImageLabel;
import com.banti.framework.img.crop.ImagePreview;
import com.banti.framework.ui.DialogEsc;
import com.school.console.SchoolMain;
import com.school.resource.CommandConstant;
import com.school.resource.ResourcesUtils;
import com.school.shared.SchoolInfo;
import com.school.utils.Logger;
import com.school.utils.MsgDialogUtils;

public class SchoolInfoSetting extends DialogEsc implements ActionListener,
		PropertyChangeListener {

	private JTextField txtName;
	private JTextField txtSubtitle;
	private JTextField txtAddressLine1;
	private JTextField txtAddressLine2;
	private JTextField txtIdCardTitle;
	private JTextField txtLogopath;
	private boolean isUpdate = false;
	private JButton btnSAVE;
	private JButton btnClose;
	private static final String SAVE = "SAVE";
	private SchoolInfo schoolInfo = SchoolInfo.getInstance();
	ImageLabel imageLabel;
	JButton btnUpload;
	JFileChooser fc;
	CropImageDialog cropImageDialog;
	private Image image;
	private final String UPLOAD = "Upload";
	private final String IMAGE_FILE = schoolInfo.getIconPath();

	public SchoolInfoSetting() throws HibernateException {
		super(SchoolMain.Frame, ResourcesUtils.getString("SCHOOL_DETAILS"),
				true);
		init();
		setSize(350, 350);
		setLocationRelativeTo(SchoolMain.Frame);
		setResizable(false);
		setVisible(true);
	}

	private void init() {
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		con.add(getMiddlePanel(), BorderLayout.NORTH);
		con.add(getButtonPanel(), BorderLayout.SOUTH);
		// .add(getPanel());
		setDataValues();

	}

	private void setDataValues() {
		txtName.setText(schoolInfo.getName());
		txtSubtitle.setText(schoolInfo.getSubtitle());
		txtAddressLine1.setText(schoolInfo.getAddress1());
		txtAddressLine2.setText(schoolInfo.getAddress2());
		txtIdCardTitle.setText(schoolInfo.getIdCardTitle());
		//txtLogopath.setText(schoolInfo.getIconPath());
		image =schoolInfo.getLogo();
		if(image!=null){
			imageLabel.loadImage(image);
		}
	}

	private void setDataFromForm() {
		schoolInfo.setName(txtName.getText());
		schoolInfo.setSubtitle(txtSubtitle.getText());
		schoolInfo.setAddress1(txtAddressLine1.getText());
		schoolInfo.setAddress2(txtAddressLine2.getText());
		schoolInfo.setIdCardTitle(txtIdCardTitle.getText());
	//	schoolInfo.setIconPath(txtLogopath.getText());
		schoolInfo.save();
		File f = new File(IMAGE_FILE);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		try {
			ImageIO.write((BufferedImage) image, "jpg", f);
			schoolInfo.setLogo(image);
		} catch (IOException e) {
			Logger.EXCEPTION
			.log(Level.WARNING, "Error while read image", e);
		}
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());

		btnSAVE = new JButton("Save");
		btnSAVE.setActionCommand(SAVE);
		btnSAVE.addActionListener(this);
		btnClose = new JButton(ResourcesUtils.getString("CLOSE"));
		btnClose.setActionCommand(CommandConstant.CANCEL_COMMAND);
		btnClose.addActionListener(this);
		// panel.add(btnCalculate);
		btnUpload = new JButton("Upload logo");
		btnUpload.setActionCommand(UPLOAD);
		btnUpload.addActionListener(this);
		panel.add(btnUpload);
		panel.add(btnSAVE);
		panel.add(btnClose);
		btnSAVE.setFocusable(true);
		return panel;
	}

	private JPanel getMiddlePanel() {
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(100, 100));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 15);
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		JLabel lb = new JLabel("School Name");
		panel.add(lb, c);
		c.gridx = 1;
		txtName = new JTextField();
		lb.setLabelFor(txtName);
		panel.add(txtName, c);
		// 1
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Sub Title");
		panel.add(lb, c);
		c.gridx = 1;
		txtSubtitle = new JTextField();
		lb.setLabelFor(txtSubtitle);
		panel.add(txtSubtitle, c);

		// 2
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Address Line1");
		panel.add(lb, c);
		c.gridx = 1;
		txtAddressLine1 = new JTextField();
		lb.setLabelFor(txtAddressLine1);
		panel.add(txtAddressLine1, c);

		// 3
		c.gridx = 0;
		c.gridy = 3;
		lb = new JLabel("Address Line2");
		panel.add(lb, c);
		c.gridx = 1;
		txtAddressLine2 = new JTextField();
		lb.setLabelFor(txtAddressLine2);
		panel.add(txtAddressLine2, c);

		// 4
		c.gridy = 4;
		c.gridx = 0;
		lb = new JLabel("Student ID Card Title");
		panel.add(lb, c);
		c.gridx = 1;
		txtIdCardTitle = new JTextField();
		lb.setLabelFor(txtIdCardTitle);
		panel.add(txtIdCardTitle, c);
//		// 5
//		c.gridy = 5;
//		c.gridx = 0;
//		lb = new JLabel("Logo path");
//		panel.add(lb, c);
//		c.gridx = 1;
//		txtLogopath = new JTextField();
//		lb.setLabelFor(txtLogopath);
//		txtLogopath.setEditable(false);
//		panel.add(txtLogopath, c);
		
		//5
		c.gridy = 6;
		c.gridx = 0;
		lb = new JLabel("Logo ");
		panel.add(lb, c);
		c.gridx = 1;
		imageLabel = new ImageLabel(new Dimension(100, 100));
		lb.setLabelFor(imageLabel);
		panel.add(imageLabel, c);
		return panel;
	}

	private JPanel getImageUploadPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		// 0
		// JLabel lb = new JLabel("Upload Photo");
		// c.gridx = 0;
		c.gridy = 0;
		// c.weightx = 0.5;
		c.insets = new Insets(5, 5, 2, 5);
		c.anchor = GridBagConstraints.LINE_START;
		// panel.add(lb, c);
		imageLabel = new ImageLabel(new Dimension(100, 100));
		c.gridx = 0;
		c.weightx = 2;
		panel.add(imageLabel, c);
		c.gridx = 2;
		c.weightx = 0.5;
		// c.insets = new Insets(10, 10, 2, 35);
		c.anchor = GridBagConstraints.LINE_START;
		btnUpload = new JButton("Upload");
		btnUpload.setActionCommand(UPLOAD);
		btnUpload.addActionListener(this);
		panel.add(btnUpload, c);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (SAVE.equals(e.getActionCommand())) {
			setDataFromForm();
			MsgDialogUtils.showInformationDialog(this,
					"School information saved successfully");
		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			dispose();
		} else if (UPLOAD.equals(e.getActionCommand())) {
			if (fc == null) {
				fc = new JFileChooser();

				fc.addChoosableFileFilter(new ImageFilter());
				fc.setAcceptAllFileFilterUsed(false);
				fc.setAccessory(new ImagePreview(fc));
			}

			// Show it.
			int returnVal = fc.showDialog(this, "Upload");

			// Process the results.
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					// ImageIO.read(file);
					image = ImageIO.read(file);
					imageLabel.loadImage(image);
					cropImageDialog = new CropImageDialog(this, image);
					cropImageDialog.setPropetyListener(this);
					cropImageDialog.setVisible(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (CropImageDialog.CROPED_IMAGE.equals(evt.getPropertyName())) {
			if (evt.getNewValue() != null) {
				image = (Image) evt.getNewValue();
				imageLabel.loadImage(image);
				//schoolInfo.setLogo(image);
			} else {
				// imageLabel.loadImage((Image)evt.getOldValue());
			}
			cropImageDialog.dispose();
		}

	}
}
