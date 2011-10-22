/**
 * Kuebiko - OpenNoteAction.java
 * Copyright 2011 Dave Huffman (daveh303 at yahoo dot com).
 * TODO license info.
 */
package dmh.kuebiko.view;

import static javax.swing.KeyStroke.getKeyStroke;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;

import dmh.kuebiko.util.AbstractActionObserver;

/**
 * Swing action for opening a note in the current stack.
 *
 * @author davehuffman
 */
class OpenNoteAction extends AbstractActionObserver {
    private static final long serialVersionUID = 1L;
    
    private final NoteFrame noteFrame;
    
    OpenNoteAction(NoteFrame noteFrame) {
        super("Open Note"); // TODO i18n.
        this.noteFrame = noteFrame;
        
        putValue(SHORT_DESCRIPTION, "Open a note in the stack."); // TODO i18n.
        putValue(LONG_DESCRIPTION, getValue(SHORT_DESCRIPTION));
        putValue(ACCELERATOR_KEY, getKeyStroke(KeyEvent.VK_L, InputEvent.META_MASK));
        putValue(MNEMONIC_KEY, KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        noteFrame.getSearchText().requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        // Do nothing for now; there's no need for this action to handle updates.
    }
}