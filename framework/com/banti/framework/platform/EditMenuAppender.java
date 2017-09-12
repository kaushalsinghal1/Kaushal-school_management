package com.banti.framework.platform;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

/**
 * This is a MouseListener concrete implementaion for showing a dropdown
 * menu from JTextComponent. "Cut", "Paste", etc get available from the
 * dropdown menu. This instance is created by
 * "<a href="EditKit.html#createEditMenuAppender(javax.swing.text.JTextComponent)">
 * EditKit.<i>createEditMenuAppender(javax.swing.text.JTextComponent)</i></a>
 * static method for the respective JTextComponent.
 */
public final class EditMenuAppender extends MouseAdapter implements CaretListener, FocusListener {

    private JTextComponent component;

    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private Action selectAllAction;
    private Action deleteAllAction;

    private JPopupMenu popup;

    EditMenuAppender(JTextComponent component) {
        this.component = component;
        this.component.addFocusListener(this);
        this.component.addCaretListener(this);

        ActionMap actionmap = component.getActionMap();
        cutAction = actionmap.get("cut-to-clipboard");
        copyAction = actionmap.get("copy-to-clipboard");
        pasteAction = actionmap.get("paste-from-clipboard");
        selectAllAction = actionmap.get("select-all");
        deleteAllAction = actionmap.get("delete-previous");

        EditKit.init();
        JMenuItem menuItem;
        popup = new JPopupMenu();
        menuItem = popup.add(EditKit.CutCommand.getRuntimeAction());
        menuItem.setText(EditKit.CutCommand.getName());
        menuItem.setIcon(null);

        menuItem = popup.add(EditKit.CopyCommand.getRuntimeAction());
        menuItem.setText(EditKit.CopyCommand.getName());
        menuItem.setIcon(null);

        menuItem = popup.add(EditKit.PasteCommand.getRuntimeAction());
        menuItem.setText(EditKit.PasteCommand.getName());
        menuItem.setIcon(null);

        menuItem = popup.add(EditKit.DeleteAllCommand.getRuntimeAction());
        menuItem.setText(EditKit.DeleteAllCommand.getName());
        menuItem.setIcon(null);

        popup.addSeparator();

        menuItem = popup.add(EditKit.SelectAllCommand.getRuntimeAction());
        menuItem.setText(EditKit.SelectAllCommand.getName());
        menuItem.setIcon(null);

    }

    /**
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        if (me == null)
            return;

        if (me.isPopupTrigger()) {
            popup.show(component, me.getX(), me.getY());
        }
    }

    /**
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent fe) {
        EditKit.PasteCommand.setAction(pasteAction);
        EditKit.SelectAllCommand.setAction(selectAllAction);
        EditKit.DeleteAllCommand.setAction(deleteAllAction);
        if (EditKit.pasteJudge()) {
            EditKit.PasteCommand.setEnabled(true);
            EditKit.PasteCommand.setOnceCalled();
        }else {
            EditKit.PasteCommand.setEnabled(false);
        }
    }

    /**
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */

    public void focusLost(FocusEvent fe) {
        if (fe.isTemporary() || fe.getSource() == this) {
            // ignore
        } else {
            EditKit.CutCommand.setEnabled(false);
            EditKit.CopyCommand.setEnabled(false);
            EditKit.PasteCommand.setEnabled(false);
            EditKit.SelectAllCommand.setEnabled(false);
            EditKit.DeleteAllCommand.setEnabled(false);
        }
    }

    /**
     * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
     */
    public void caretUpdate(CaretEvent ce) {
        if (ce != null && ce.getDot() != ce.getMark()) {
            EditKit.CutCommand.setAction(cutAction);
            EditKit.CopyCommand.setAction(copyAction);
            EditKit.PasteCommand.setAction(pasteAction);
        } else {
            EditKit.CutCommand.setAction(null);
            EditKit.CopyCommand.setAction(null);
        }
    }
}
