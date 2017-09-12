package com.banti.framework.cwt.progressdialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class TestProgressDialog implements MonitoredProcess, ActionListener {
	ProgressThreadMonitor monitor;
	ProgressStatusDialog statusDialog;
	JProgressBar progressBar;

	public TestProgressDialog() {
		monitor = new ProgressThreadMonitor();
		progressBar=new JProgressBar();
		statusDialog = new ProgressStatusDialog((JFrame) null, "Sample",
				progressBar, this);
	}

	@Override
	public boolean doPreProcess() {
		return true;
	}

	@Override
	public Object doMainProcess() {
		try {
			monitor.setValue(0);
			System.out.println("Main process stared");
			monitor.setNextProgress(50);
			statusDialog.addMessage("Mail process started");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void doPostProcess(Object result) {

	}

	@Override
	public void doFinally() {

	}

	@Override
	public void start() {
		monitor.start(this);
	}

	public static void main(String[] args) {
		new TestProgressDialog();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
