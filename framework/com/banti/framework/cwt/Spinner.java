/*
 * File : IntegerSpinner.java
 * Created : 2005/05/31
 * cysol_koban_dev
 *
 * Copyright (c) Cyber Solutions Inc.
 * All Rights Reserved.
 */
package com.banti.framework.cwt;

import java.awt.BorderLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * @author cysol
 *
 */
public class Spinner extends JSpinner {

    public Spinner(int minimum, int maximum) {
        this(minimum, minimum, maximum, 1);
    }

    public Spinner(int value, int minimum, int maximum) {
        this(value, minimum, maximum, 1);
    }

    public Spinner(int value, final int minimum, final int maximum, int stepSize) {
        super(new SpinnerNumberModel(value, minimum, maximum, stepSize));
        if (super.getEditor() instanceof JSpinner.NumberEditor) {
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) super.getEditor();
            editor.getTextField().setDocument(new InnterDocument(minimum, maximum));
        }
    }

    public void applyPattern(String suffix) {
        if (super.getEditor() instanceof JSpinner.NumberEditor) {
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) super.getEditor();
            editor.getFormat().applyPattern(suffix);
        }
        this.fireStateChanged();
    }

    public void setEditable(boolean flag) {
        if (super.getEditor() instanceof JSpinner.NumberEditor) {
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) super.getEditor();
            editor.getTextField().setEditable(flag);
        }
    }

    public int getInt() {
        return ((Integer) super.getValue()).intValue();
    }

    public void setInt(int value) {
        super.setValue(new Integer(value));
    }

    public void setValue(Object obj) {
        if (obj instanceof String) {
            Integer value = new Integer(((String) obj));
            obj = value;
        }
        super.setValue(obj);
    }

    private static class InnterDocument extends PlainDocument {

        private int maximum;
        private int minimum;

        InnterDocument(int min, int max) {
            maximum = max;
            minimum = min;
        }

        public void replace(int offset, int length, String text, AttributeSet a) throws BadLocationException {
            super.writeLock();
            try {
                if (length > 0) {
                    super.remove(offset, length);
                }
                if (text != null && text.length() > 0) {
                    insertString(offset, text, a);
                }
                //super.replace(offset, length, text, a);
            } finally {
                super.writeUnlock();
            }
        }

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

            if (str == null) {
                return;
            }
            char[] chs = str.toCharArray();
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] != '-' && (chs[i] < '0' || chs[i] > '9')) {
                    return;
                }
            }
            String newStr = getText(0, offs) + str + getText(offs, getLength() - offs);

            try {
                int number = Integer.parseInt(newStr);
                if (number < minimum || number > maximum) {
                    return;
                }
            } catch (NumberFormatException e) {
                return;
            }
            super.insertString(offs, str, a);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Spinner spinner = new Spinner(3, 0, 10, 1);
        spinner.applyPattern("00");
        spinner.setEditable(false);
        frame.getContentPane().add(spinner, BorderLayout.NORTH);

        JFormattedTextField textField = new JFormattedTextField();
        textField.setDocument(new Spinner.InnterDocument(0, 10));
        frame.getContentPane().add(textField, BorderLayout.CENTER);

        spinner.setValue("3");
        frame.pack();
        frame.setVisible(true);
    }
}