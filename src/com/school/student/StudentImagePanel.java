package com.school.student;

import java.awt.BorderLayout;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.HibernateException;

import com.banti.framework.core.ApplicationOpertaion;
import com.banti.framework.img.crop.CropImageDialog;
import com.banti.framework.img.crop.ImageFilter;
import com.banti.framework.img.crop.ImageLabel;
import com.banti.framework.img.crop.ImagePreview;
import com.school.constant.ApplicationConstant;
import com.school.constant.OperationConstant;
import com.school.hiebernate.HiebernateStudentDboUtil;
import com.school.hiebernate.dbo.StudentImageDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourcesUtils;
import com.school.utils.Logger;
import com.school.utils.MsgDialogUtils;

public class StudentImagePanel extends JPanel implements ActionListener,
		PropertyChangeListener {
	private JButton btnRegister;
	private JButton btnCancel;
	private JButton btnUpdate;
	ImageLabel imageLabel;
	JButton btnUpload;
	JFileChooser fc;
	CropImageDialog cropImageDialog;
	private Image image;
	private final String UPLOAD = "Upload";
	private JTextField txtOldSchoolName;
	private JTextField txtOldClass;
	private JTextField txtOldYear;
	JDialog dialog;
	private PropertyChangeListener propertyChangeListener;
	private StudentImageDetails studentImageDetails;
	private final String TEMP_IMAGE_FILE = ApplicationConstant.TEMP_IMAGE_FILE;

	public StudentImagePanel(StudentImageDetails studentImageDetails,
			JDialog dialog) {
		this.studentImageDetails = studentImageDetails;
		this.dialog = dialog;
		init();
		if (studentImageDetails != null) {
			setData();
		}
	}

	public StudentImagePanel() {
		init();
	}

	public StudentImagePanel(JDialog dialog) {
		init();
		this.dialog = dialog;
	}

	private void init() {
		setLayout(new BorderLayout());
		add(getMainPanel(), BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.SOUTH);
		setRegisterButtonEnabled(studentImageDetails == null);

	}

	private void setRegisterButtonEnabled(boolean enable) {
		btnRegister.setEnabled(enable
				&& ApplicationOpertaion
						.isAllowed(OperationConstant.STUDENT_REGISTER));
		btnUpdate.setEnabled(!enable
				&& ApplicationOpertaion
						.isAllowed(OperationConstant.STUDENT_UPDATE));

	}

	private JPanel getMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		// c.weightx = 0.5;
		c.weightx = 0.75;
		// c.insets = new Insets(5, 5, 2, 5);
		panel.add(getImageUploadPanel(), c);
		c.gridx = 0;
		c.gridy = 1;
		// c.weightx = 0.5;
		c.insets = new Insets(4, 4, 4, 4);
		panel.add(getOldSchoolInfoPanel(), c);
		// admin adpanel.add(getButtonPanel());
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
		imageLabel = new ImageLabel();
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

	private JPanel getOldSchoolInfoPanel() {
		JPanel panel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		// 0
		JLabel lb = new JLabel(ResourcesUtils.getString("OLD_SCHOOL_NAME"));
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 2, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 0;
		// c.weightx = 0.5;
		c.weightx = 1.0;
		c.gridwidth = 2;
		// c.insets = new Insets(5, 5, 5, 5);
		txtOldSchoolName = new JTextField();
		// cbClass.setSize(280, 20);
		lb.setLabelFor(txtOldSchoolName);
		panel.add(txtOldSchoolName, c);
		// 1
		lb = new JLabel(ResourcesUtils.getString("PASSOUT_CLASS"));
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.insets = new Insets(5, 5, 2, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		// c.insets = new Insets(5, 5, 5, 5);
		txtOldClass = new JTextField();
		// cbClass.setSize(280, 20);
		lb.setLabelFor(txtOldClass);
		panel.add(txtOldClass, c);
		// 2
		lb = new JLabel(ResourcesUtils.getString("PASSOUT_YEAR"));
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 2, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		// c.insets = new Insets(5, 5, 5, 5);
		txtOldYear = new JTextField(12);
		// cbClass.setSize(280, 20);
		lb.setLabelFor(txtOldYear);
		panel.add(txtOldYear, c);

		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(1),
				ResourcesUtils.getString("OLD_SCHOOL_INFO")));
		return panel;
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		btnRegister = new JButton(ResourcesUtils.getString("SAVE"));
		btnRegister.setActionCommand(OperationConstant.STUDENT_REGISTER);
		btnRegister.addActionListener(this);
		btnUpdate = new JButton(ResourcesUtils.getString("UPDATE"));
		btnUpdate.setActionCommand(OperationConstant.STUDENT_UPDATE);
		btnUpdate.addActionListener(this);
		btnCancel = new JButton(ResourcesUtils.getString("SKIP"));
		btnCancel.setActionCommand(CommandConstant.CANCEL_COMMAND);
		btnCancel.addActionListener(this);
		panel.add(btnRegister);
		panel.add(btnUpdate);
		panel.add(btnCancel);
		btnRegister.setFocusable(true);
		// setRegisterButtonEnabled(true);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (UPLOAD.equals(e.getActionCommand())) {
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
					cropImageDialog = new CropImageDialog(dialog, image);
					cropImageDialog.setPropetyListener(this);
					cropImageDialog.setVisible(true);
					btnRegister.setEnabled(true);
					// btnSave.setEnabled(true);

					// readAndSave(file);
					// imgDisplay.setIcon(new ImageIcon(ImageIO.read(file)));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (OperationConstant.STUDENT_REGISTER.equals(e
				.getActionCommand())) {
			StudentImageDetails studentImageDetails = createImageDeatails(null);
			if (studentImageDetails != null) {
				studentImageDetails.setAddDate(new Date());
				try {
					this.studentImageDetails = HiebernateStudentDboUtil
							.saveStudentImageDetails(studentImageDetails);
					MsgDialogUtils.showInformationDialog(this,
							"Student information has been saved Successfully!");
					propertyChangeListener
							.propertyChange(new PropertyChangeEvent(this,
									StudentRegistrationAction.IMAGE_REGISTER,
									null, studentImageDetails));
				} catch (HibernateException ex) {
					Logger.EXCEPTION.log(Level.WARNING,
							"Error While Saving image ", ex);
					MsgDialogUtils
							.showWarningDialog(this, "Data base Occured!");
				}
			} else {
				MsgDialogUtils.showWarningDialog(this,
						"Error occured while saving image!");
			}
		} else if (OperationConstant.STUDENT_UPDATE
				.equals(e.getActionCommand())) {
			StudentImageDetails studentImageDetails = createImageDeatails(this.studentImageDetails);
			if (studentImageDetails != null) {
				studentImageDetails.setAddDate(new Date());
				try {
					HiebernateStudentDboUtil
							.updateStudentImageDetails(studentImageDetails);
					MsgDialogUtils
							.showInformationDialog(this,
									"Student information has been Updated Successfully!");
					propertyChangeListener
							.propertyChange(new PropertyChangeEvent(this,
									StudentRegistrationAction.IMAGE_UPDATE,
									null, studentImageDetails));
				} catch (HibernateException ex) {
					Logger.EXCEPTION.log(Level.WARNING,
							"Error While Saving image ", ex);
					MsgDialogUtils
							.showWarningDialog(this, "Data base Occured!");

				}
			} else {
				MsgDialogUtils.showWarningDialog(this,
						"Error occured while saving image!");
			}
		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			propertyChangeListener.propertyChange(new PropertyChangeEvent(this,
					StudentRegistrationAction.CHANGEANDENABLE_TAB, true, 2));
		}
	}

	private void setData() {
		if (studentImageDetails != null) {
			if (studentImageDetails.getPhoto() != null) {
				byte[] bAvatar = studentImageDetails.getPhoto();
				try {
					File file = new File(TEMP_IMAGE_FILE);
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(bAvatar);
					fos.close();
					image = ImageIO.read(file);
					imageLabel.loadImage(image);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		txtOldSchoolName
				.setText(studentImageDetails.getOldSchoolName() != null ? studentImageDetails
						.getOldSchoolName() : "");
		txtOldClass
		.setText(studentImageDetails.getClassName() != null ? studentImageDetails
				.getClassName() : "");
		txtOldYear
		.setText(studentImageDetails.getYear() != null ? studentImageDetails
				.getYear() : "");
	}

	public void setPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListener = listener;
	}

	private StudentImageDetails createImageDeatails(
			StudentImageDetails imageDetails) {
		if (imageDetails == null)
			imageDetails = new StudentImageDetails();
		if (image != null)
			try {
				File f = new File(TEMP_IMAGE_FILE);
				if (!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				ImageIO.write((BufferedImage) image, "jpg", f);
				byte[] bFile = new byte[(int) f.length()];

				FileInputStream fileInputStream = new FileInputStream(f);
				// convert file into array of bytes
				fileInputStream.read(bFile);
				fileInputStream.close();

				imageDetails.setPhoto(bFile);
				Logger.DEBUG
						.fine("Image is saved successfully from image object");

			} catch (IOException e) {
				e.printStackTrace();
				Logger.DEBUG.log(Level.WARNING,
						"Exception Occued while saving image file");
				Logger.EXCEPTION.log(Level.WARNING,
						"Exception Occued while saving image file", e);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				Logger.DEBUG.log(Level.WARNING,
						"Exception Occued while saving image file");
				Logger.EXCEPTION.log(Level.WARNING,
						"Exception Occued while saving image file", e);
				return null;
			}
		imageDetails.setOldSchoolName(txtOldSchoolName.getText());
		imageDetails.setClassName(txtOldClass.getText());
		imageDetails.setYear(txtOldYear.getText());
		return imageDetails;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (CropImageDialog.CROPED_IMAGE.equals(evt.getPropertyName())) {
			if (evt.getNewValue() != null) {
				image = (Image) evt.getNewValue();
				imageLabel.loadImage(image);
			} else {
				// imageLabel.loadImage((Image)evt.getOldValue());
			}
			cropImageDialog.dispose();
		}

	}
}
