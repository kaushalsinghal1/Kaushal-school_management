package com.banti.framework.cwt;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TempCommunicator extends JFrame implements ProgressCommunicator {
    ProgressCommunicatorMonitor monitor;

    public TempCommunicator() {
        super("Test ");
        monitor = new ProgressCommunicatorMonitor(this, "Progress Test ", true, this);
        init();
    }

    private void init() {
        Container con = getContentPane();
        con.add(new JLabel("test.........."));
        JButton button = new JButton("Start");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                monitor.start();
            }
        });
        con.add(button);
        setSize(555, 555);
        setVisible(true);
        monitor.start();
    }

    @Override
    public void run() {
        System.out.println("Before Sleep");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("After Sleep");
    }

    @Override
    public void doPostProcess() {
        System.out.println("Progress Completed");

    }
    @Override
    public void doCancel() {
        System.out.println("Progress Cancelled");
    	
    }

    public static void main(String[] args) {
        new TempCommunicator();
    }

}
