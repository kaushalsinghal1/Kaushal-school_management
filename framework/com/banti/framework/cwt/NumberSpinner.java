package com.banti.framework.cwt;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NumberSpinner extends JPanel {

    private final static String UP_IMAGE = "/com/banti/framework/cwt/images/up.gif";
    private final static String DOWN_IMAGE = "/com/banti/framework/cwt/images/down.gif";

    private JTextField field;
    private JButton up;
    private JButton down;

    private int space;
    private int min;
    private int max;
    private int cur;

    private ChangeListener listener;

    /**
     * Method NumberSpinner.
     * @param min
     * @param max
     */
    public NumberSpinner(int min, int max, int space) {
        super(null);
        this.min = min;
        this.cur = min;
        this.max = max;
        this.space = space;
        init();
        field.addKeyListener(new PressRleaseKeyListener());
        add(field);
        int fieldX = space * 10;
        field.setBounds(0, 0, fieldX + 5, 25);
        add(up);
        up.setBounds(fieldX + 6, 0, 13, 12);
        add(down);
        down.setBounds(fieldX + 6, 13, 13, 12);
        setPreferredSize(new Dimension(fieldX + 20, 25));
    }

    public void setEnabled(boolean aFlag) {
        field.setEnabled(aFlag);
        up.setEnabled(aFlag);
        down.setEnabled(aFlag);
    }

    public String getToolTipText() {
        return field.getToolTipText();
    }

    public void setToolTipText(String text) {
        field.setToolTipText(text);
    }

    private void init() {
        field = new JTextField();
        up = new JButton(new ImageIcon(getClass().getResource(UP_IMAGE)));
        down = new JButton(new ImageIcon(getClass().getResource(DOWN_IMAGE)));
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == up) {
                    field.setText((String) getNextValue());
                } else if (source == down) {
                    field.setText((String) getPreviousValue());
                }
                if (listener != null) {
                    listener.stateChanged(new ChangeEvent(source));
                }
            }
        };

        up.addActionListener(al);
        down.addActionListener(al);
        up.setMargin(new Insets(0, 0, 1, 0));
        down.setMargin(new Insets(0, 0, 0, 0));
    }

    /**
     * Method getInt.
     * @return int
     */
    public int getInt() {
        return Integer.parseInt(getValue() + "");
    }

    /**
     * Method setInt.
     * @param value
     */
    public void setInt(int value) {
        if (value < 10) {
            field.setText("0" + value);
        } else {
            field.setText("" + value);
        }
        setValue(new Integer(value));
    }

    /** This class traps all the keys when the key is pressed 
     *  and when the key is released.
     */
    private class PressRleaseKeyListener extends KeyAdapter {

        /* Invoked when a key has been released.
         * @param KeyEvent ke
         * @return void
         */
        public void keyReleased(KeyEvent ke) {
            char c = ke.getKeyChar();
            String text = field.getText();
            if (text.length() == 0) {
                return;
            }

            String tmp = null;
            if ((c >= '0' && c <= '9' && text.length() <= space)
                || KeyEvent.VK_DELETE == ke.getKeyCode()
                || KeyEvent.VK_BACK_SPACE == ke.getKeyCode()) {

                tmp = round(text);

            } else {
                tmp = text.replaceAll("[^0-9]", "");
                if (tmp.length() > space) {
                    tmp = tmp.substring(0, space);
                }
                tmp = pad(round(tmp));
            }
            if (!text.equals(pad(tmp))) {
                field.setText(tmp);
            }
            setValue(pad(tmp));
        }

        public void keyTyped(KeyEvent ke) {
            if (listener != null) {
                listener.stateChanged(new ChangeEvent(ke));
            }
        }
    }

    private String round(String text) {
        try {
            int val = Integer.parseInt(text);
            if (val > max) {
                val = max;
            } else if (val < min) {
                val = min;
            }
            return "" + val;

        } catch (NumberFormatException exp) {
            return "0";
        }
    }

    private String pad(String text) {
        switch (text.length()) {
            case 1:
                return "0" + text;
            case 0:
                return "00";
        }
        return text;
    }

    /**
     * @see javax.swing.SpinnerModel#getPreviousValue()
     */
    public Object getPreviousValue() {
        if (cur <= min) {
            cur = max;
            int temp = Integer.parseInt("" + cur);
            if (temp <= 9) {
                return "" + "0" + cur;
            }
            return "" + cur;
        }

        int temp = Integer.parseInt("" + cur);
        if (temp <= 10) {
            return "" + "0" + --cur;
        }
        return "" + --cur;
    }

    /**
     * @see javax.swing.SpinnerModel#getValue()
     */
    public Object getValue() {
        int temp = Integer.parseInt("" + cur);
        if (temp <= 9) {
            return "" + "0" + cur;
        }
        return "" + cur;
    }

    /**
     * @see javax.swing.SpinnerModel#getNextValue()
     */
    public Object getNextValue() {
        if (cur >= max) {
            cur = min;
            int temp = Integer.parseInt("" + cur);
            if (temp <= 9) {
                return "" + "0" + cur;
            }
            return "" + cur;
        }

        int temp = Integer.parseInt("" + cur);
        if (temp < 9) {
            return "" + "0" + ++cur;
        }
        return "" + ++cur;
    }

    /**
     * @see javax.swing.SpinnerModel#setValue(Object)
     */
    public void setValue(Object cur) {
        int nVal = 0;
        try {
            nVal = Integer.parseInt(cur + "");
            this.cur = nVal;
        } catch (Exception e) {
        }

        if (listener != null) {
            listener.stateChanged(new ChangeEvent(cur));
        }
    }

    public void addValueChangeListener(ChangeListener listener) {
        this.listener = listener;
    }
}
