package com.banti.framework.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
   
public class ComboBoxObject extends JFrame implements ActionListener  
{  
    private JComboBox comboBox;  
   
    public ComboBoxObject()  
    {  
        comboBox = new JComboBox();  
        comboBox.addActionListener( this );  
//  
        comboBox.addItem( new Item(1, "car" ) );  
        comboBox.addItem( new Item(2, "plane" ) );  
        comboBox.addItem( new Item(3, "train" ) );  
        comboBox.addItem( new Item(4, "boat" ) );  
//  
        getContentPane().add( comboBox );  
    }  
   
    public void actionPerformed(ActionEvent e)  
    {  
        Item item = (Item)comboBox.getSelectedItem();  
        System.out.println( item.getId() + " : " + item.getDescription() );  
    }  
   
    class Item  
    {  
        private int id;  
        private String description;  
   
        public Item(int id, String description)  
        {  
            this.id = id;  
            this.description = description;  
        }  
   
        public int getId()  
        {  
            return id;  
        }  
   
        public String getDescription()  
        {  
            return description;  
        }  
   
        public String toString()  
        {  
            return description;  
        }  
    }  
   
    public static void main(String[] args)  
    {  
        JFrame frame = new ComboBoxObject();  
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );  
        frame.pack();  
        frame.setVisible( true );  
     }  
   
} 