package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a list of Note objects with restricted operations.
 * Note objects contained in the list are sorted after age using the LocalDateTime class.
 * @see java.time.LocalDateTime
 */
public class Notes {

    // Elements represented as a list of note objects
    private final List<Note> elements;

    /**
     * Default constructor containing an empty list.
     */
    public Notes() {
        elements = new ArrayList<>();
    }

    /**
     * Copy constructor.
     * Instantiates a new sorted list from the list contained in the given notes.
     * @param oldNotes the notes to be copied
     */
    public Notes(Notes oldNotes) {
        this.elements = new ArrayList<>(oldNotes.elements);
        elements.sort(Note::compareTo);
    }

    /**
     * Private copy constructor for setting a selected list of note in this.
     * @param notes the new list of elements
     */
    private Notes(List<Note> notes) {
        this.elements = notes;
        elements.sort(Note::compareTo);
    }

    /**
     * Instantiates an empty note with default constructor and appends it to the list of elements.
     * New note objects will always be newer than the object last added to the list.
     */
   public void addNote() {
        elements.add(new Note());
    }

    /**
     * Instantiates a note with the given string and appends it to the list of elements.
     * New note objects will always be newer than the object last added to the list.
     * @param text the String of text to be contained in the note.
     */
   public void addNote(String text) {
        elements.add(new Note(text));
    }

    /**
     * Removes the Note from the list of elements at the specified position.
     * @param index the index of the note to be removed.
     */
   public void removeNote(int index) {
        elements.remove(index);
    }

    /**
     * Gives the current number of elements in notes
     * @return the number of elements
     */
   public int size() {
        return elements.size();
    }

    /**
     * Gives the text for the note at the given index.
     * @param index the index of the note to view
     * @return the text of the viewed note
     */
   public String viewNoteAt(int index) {
        return elements.get(index).viewNote();
    }

    /**
     * Replaces the note at the given index with a new one containing the given text.
     * The list order is unaffected.
     * @param index the index of the note being edited
     * @param text the new text
     */
   public void editNoteAt(int index, String text) {
        Note note = elements.get(index).editNote(text);
        elements.set(index, note);
    }

    /**
     * Returns a copy of the list of note objects.
     * @return the list of notes
     */
   public List<Note> getSortedElem() {
        List<Note> list = new ArrayList<>(elements);
        list.sort(Note::compareTo);
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notes notes = (Notes) o;
        return elements.equals(notes.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}
