package model;

import model.exceptions.NameNotAvailableException;
import model.exceptions.TagNotFoundException;

import javax.lang.model.type.ArrayType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class User implements ICacheVisitable {
    //TODO fix javadoc, rushing to get runnable version W3
    private String name;
    private EventList eventList = new EventList();
    private ContactList contactList = new ContactList();
    private TagHandler tagHandler = new TagHandler();

    /***
     * Instantiates a user object with the specified name.
     * @param name the name of the user
     */
    public User(String name){
        this.name = name;
    }

    /***
     * Sets the name of the user
     * @param name new name of the user
     */
    public void setName(String name){
        this.name = name;
    }

    /***
     * Returns the name of the user
     * @return a string containing the name of the users
     */
    public String getName() {
        return this.name;
    }

    /***
     * Returns the users list of events
     * @return the list of events
     */
    public EventList getEvents() {
        return eventList;
    }

    /***
     * Returns a list of events that the given contact is tagged in
     * @param contact the subject contact
     * @return an arraylist of the events the contact is a part of
     */
    public List<Event> getContactEvents(Contact contact) {
        List<Event> contactEvents= new ArrayList<>();
        for (Event e: eventList.getList()) {
            if (e.getContacts().contains(contact))
                contactEvents.add(e);
        }
        return contactEvents;
    }


    public ContactList getContacts(){
        return contactList;
    }

    public ITag createTag(String name) throws NameNotAvailableException{
        return tagHandler.createTag(name);
    }

    public List<ITag> getTags(){
        return tagHandler.getTags();
    }

    public ITag getTag(String name) throws TagNotFoundException{
        return tagHandler.getTag(name);
    }

    public boolean setColor(ITag tag, String color) {
        return tagHandler.setColor(tag, color);
    }

    public void renameTag(ITag tag, String newName) throws NameNotAvailableException {
        tagHandler.rename(tag, newName);

    }


    /***
     * The user cache class contains fields which should be saved/loaded to persistent storage.
     */
    public static class UserCache {
        public String name;
        public List<Event> events;
        public List<Contact> contacts;
        public TagHandler tagHandler;

        public UserCache() {}
    }

    private UserCache getCache() {
        UserCache cache = new UserCache();
        cache.name = this.name;
        cache.events = new ArrayList<>(this.eventList.getList());
        cache.contacts = new ArrayList<>(this.contactList.getList());
        cache.tagHandler = this.tagHandler;
        return cache;
    }

    public User(UserCache cache) {
        this.eventList = new EventList(cache.events);
        this.contactList = new ContactList(cache.contacts);
        this.tagHandler = cache.tagHandler;
        this.name = cache.name;
    }

    /***
     * Invoke the user cache visitor case.
     */
    @Override
    public <E, T> Optional<T> accept(ICacheVisitor<E, T> visitor, E env) {
        return visitor.visit(this.getCache(), env);
    }
}
