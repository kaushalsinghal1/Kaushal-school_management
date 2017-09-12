package com.banti.framework.img.crop;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/*
 * FileChooserDemo2.java requires these files: ImageFileView.java ImageFilter.java ImagePreview.java
 * Utils.java images/jpgIcon.gif (required by ImageFileView.java) images/gifIcon.gif (required by
 * ImageFileView.java) images/tiffIcon.gif (required by ImageFileView.java) images/pngIcon.png
 * (required by ImageFileView.java)
 */
public class FileChooserDemo2 extends JPanel implements ActionListener {
	static private String newline = "\n";
	private JTextArea log;
	private JFileChooser fc;
	private JLabel imgDisplay;

	public FileChooserDemo2() {
		super(new BorderLayout());

		// Create the log first, because the action listener
		// needs to refer to it.
		log = new JTextArea(5, 20);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		JButton sendButton = new JButton("Attach...");
		sendButton.addActionListener(this);

		add(sendButton, BorderLayout.PAGE_START);
		imgDisplay = new JLabel();
		add(imgDisplay, BorderLayout.CENTER);
		add(logScrollPane, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		// Set up the file chooser.
		if (fc == null) {
			fc = new JFileChooser();

			fc.addChoosableFileFilter(new ImageFilter());
			fc.setAcceptAllFileFilterUsed(false);
			fc.setAccessory(new ImagePreview(fc));
		}

		// Show it.
		int returnVal = fc.showDialog(FileChooserDemo2.this, "Attach");

		// Process the results.
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				// ImageIO.read(file);
				// image = ImageIO.read(new File(path));
				readAndSave(file);
				imgDisplay.setIcon(new ImageIcon(ImageIO.read(file)));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.append("Attaching file: " + file.getName() + "." + newline);
		} else {
			log.append("Attachment cancelled by user." + newline);
		}
		log.setCaretPosition(log.getDocument().getLength());

		// Reset the file chooser for the next time it's shown.
		fc.setSelectedFile(null);
	}

	private void readAndSave(File file) {
		byte[] bFile = new byte[(int) file.length()];

		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			// convert file into array of bytes
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			File f = new File("data\\AreaChart1.jpg");
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bFile);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("FileChooserDemo2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new FileChooserDemo2());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}
