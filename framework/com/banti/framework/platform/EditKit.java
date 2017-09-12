package com.banti.framework.platform;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.text.JTextComponent;

import com.banti.framework.platform.module.DefaultEditModuleScheme;

/**
 * This is a utility class that is a creator of
 * <a href="EditMenuAppender.html"><i>EditMenuAppender</i></a>. It is available to
 * append a dropdown menu to JTextComponent. "Copy", "Paste", etc can be used by
 * adding <i>EditMenuAppender</i> as a MouseListener.
 */
public class EditKit {

    static EditKit CommandBuilder = new EditKit();

    static Cut CutCommand;
    static Copy CopyCommand;
    static Paste PasteCommand;
    static SelectAll SelectAllCommand;
    static DeleteAll DeleteAllCommand;

    private EditKit() {
        super();
    }

    /**
     * Initializes edit menu even if it is not <i>Application</i> based application.
     * It is necessary to call init() if the appended dropdown menu is used by
     * another application.
     *
     */
    public static void init() {
        if (CutCommand == null) {
            new EditModule(new DefaultEditModuleScheme());
        }
    }

    /**
     * Creates <i>EditMenuAppender</i> for the JTextComponent. By adding it as MouseListener,
     * the JTextComponent can show a dropdown menu in which "Cut", "Paste", etc is available.
     *
     * @param component JTextComponent to which a dropdown menu is appended.
     * @return EditMenuAppender instance for the JTextComponent.
     */
    public static EditMenuAppender createEditMenuAppender(JTextComponent component) {
        return new EditMenuAppender(component);
    }

    static void set(Cut cut) {
        CutCommand = cut;
    }

    static void set(Copy copy) {
        CopyCommand = copy;
    }

    static void set(Paste paste) {
        PasteCommand = paste;
    }

    static void set(SelectAll selectAll) {
        SelectAllCommand = selectAll;
    }

    static void set(DeleteAll deleteAll) {
        DeleteAllCommand = deleteAll;
    }

    public static boolean pasteJudge() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        try {
            String s = (String) contents.getTransferData(DataFlavor.stringFlavor);
            if (s != null) {
                return true;
            }
            return false;
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ------------------- Inner Edit Command Classes
    private abstract class AbstractTextCommand extends Command {
        private Action action;

        AbstractTextCommand(String name) {
            super(name);
        }

        public void entryPoint(ActionEvent ae) {
            if (action != null) {
                action.actionPerformed(ae);
            }
        }

        void setAction(Action action) {
            if (action != null) {
                this.action = action;
                super.setEnabled(true);
            } else {
                super.setEnabled(false);
            }
        }
    }

    final class Cut extends AbstractTextCommand {
        Cut(String name) {
            super(name);
        }

        public void entryPoint(ActionEvent ae) {
            super.entryPoint(ae);
            PasteCommand.setEnabled(true);
            PasteCommand.setOnceCalled();
        }
    }

    final class Copy extends AbstractTextCommand {
        Copy(String name) {
            super(name);
        }

        public void entryPoint(ActionEvent ae) {
            super.entryPoint(ae);
            PasteCommand.setEnabled(true);
            PasteCommand.setOnceCalled();
        }
    }

    final class Paste extends AbstractTextCommand {
        private boolean once_called = false;

        Paste(String name) {
            super(name);
        }

        void setOnceCalled() {
            once_called = true;
        }

        void setAction(Action action) {
            super.setAction(action);
            if (!once_called) {
                super.setEnabled(false);
            } else {
                super.setEnabled(true);
            }
        }
    }

    final class SelectAll extends AbstractTextCommand {
        SelectAll(String name) {
            super(name);
        }
    }

    final class DeleteAll extends AbstractTextCommand {
        DeleteAll(String name) {
            super(name);
        }

        public void entryPoint(ActionEvent ae) {
            SelectAllCommand.entryPoint(ae);
            super.entryPoint(ae);
        }

    }
}
