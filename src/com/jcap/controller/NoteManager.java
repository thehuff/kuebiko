/**
 * Kuebiko - NoteManager.java
 * Copyright 2011 Dave Huffman (daveh303 at yahoo dot com).
 * TODO license info.
 */

package com.jcap.controller;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jcap.model.Note;
import com.jcap.model.NoteDao;

/**
 * Management class for notes. Acts as the note controller.
 *
 * @author davehuffman
 */
public class NoteManager {
    private static final Function<Note, String> TITLE_TRANSFORMER =
            new Function<Note, String>() {
                @Override
                public String apply(Note input) {
                    return input.getTitle();
                }
            };
            
    @Deprecated
    private final Predicate<Note> searchFilter = new Predicate<Note>() {
                @Override
                public boolean apply(Note note) {
                    return note.getTitle().contains(filter)
                            || note.getTags().contains(filter)
                            || note.getText().contains(filter);
                }
            };
    
    private final NoteDao noteDao;
    
    private List<Note> notes = null;
    @Deprecated
    private String filter;

    private final Collection<Note> deletedNotes;
    
    public NoteManager(NoteDao noteDao) {
        this.noteDao = noteDao;
        
        deletedNotes = Lists.newArrayList();
        loadAllNotes();
    }

    private void loadAllNotes() {
        notes = noteDao.readNotes();
    }
    
    public List<Note> getNotes() {
        return isBlank(filter)? Collections.unmodifiableList(notes)
                : ImmutableList.copyOf(Collections2.filter(notes, searchFilter));
    }
    
    public List<String> getNoteTitles() {
        return Collections.unmodifiableList(Lists.transform(notes, TITLE_TRANSFORMER));
    }
    
    /**
     * @return True if there are no notes.
     */
    public boolean isEmpty() {
        return notes.isEmpty();
    }
    
    public int getNoteCount() {
        return notes.size();
    }
    
    public Note getNoteAt(int index) {
        return notes.get(index);
    }
    
    public void addNote(Note newNote) {
        notes.add(newNote);
    }
    
    public void deleteNote(Note note) {
        if (!notes.remove(note)) {
            throw new IllegalArgumentException(String.format(
                    "Note [%s] does not exist.", note));
        }
        deletedNotes.add(note);
    }
    
    /**
     * Save any changes made to the notes.
     */
    public void saveAll() {
        for (Note note: deletedNotes) {
            noteDao.deleteNote(note);
        }
        deletedNotes.clear();
        
        for (Note note: notes) {
            switch (note.getState()) {
//            case DELETED:
//                noteDao.deleteNote(note);
//                break;
            case DIRTY:
                noteDao.updateNote(note);
                break;
            case NEW:
                noteDao.addNote(note);
                break;
            default:
                continue;
            }
        }
        
        // TODO may not be necessary.
        loadAllNotes();
    }
    
    @Deprecated
    public String getFilter() {
        return filter;
    }
    
    @Deprecated
    public void setFilter(String filter) {
        this.filter = filter;
    }
}