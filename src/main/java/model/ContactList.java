package model;

import model.exceptions.NameNotAllowedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/***
 * Wrapper for a list of contacts.
 */
public class ContactList implements IObservable {

    private List<Contact> contactList = new ArrayList<>();
    private final List<IObserver> observers = new ArrayList<>();

    /***
     * Creates a new contact list wrapper object.
     */
    ContactList() {
        // Constructor here to explicitly declare that it is package-private
    }

    /***
     * Wraps a given list of contacts.
     * @param contacts the list to be wrapped
     */
    ContactList(List<Contact> contacts) {
        this.contactList = contacts;
    }

    /***
     * Adds a contact to the contactList.
     * @param name the name of the contact
     */
    public void addContact(String name) throws NameNotAllowedException {
        if (name.isEmpty()) {
            throw new NameNotAllowedException("Contacts must have a name");
        }
        contactList.add(new Contact(name));
        notifyObservers();
    }

    /***
     * Adds a contact to the contactList
     * @param contact the contact object
     */
    public void addContact(Contact contact) {
        contactList.add(contact);
        notifyObservers();
    }

    /***
     * Creates and adds a contact to the ContactList given a specified Contact-Cache
     * @param cache The ContactCache to create the contact from
     * @throws NameNotAllowedException if the name of the contact is blank.
     */
    public void addContact(Contact.ContactCache cache) throws NameNotAllowedException {
        if (cache.name.isEmpty()) {
            throw new NameNotAllowedException("Contacts must have a name");
        }
        if (cache.directoryId == null) {
            cache.directoryId = UUID.randomUUID();
        }
        if (cache.tags == null) {
            cache.tags = new ArrayList<>();
        }
        contactList.add(new Contact(cache));
        notifyObservers();
    }

    /***
     * Removes a contact from the contactList
     * @param contact the contact to be removed
     */
    public void removeContact(Contact contact) {
        contactList.remove(contact);
        notifyObservers();
    }

    /***
     * Returns the contactList
     * @return the contact list
     */
    public List<Contact> getList() {
        return new ArrayList<>(this.contactList);
    }

    @Override
    public void subscribe(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unSubscribe(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.onEvent();
        }
    }

    @Override
    public String toString() {
        return "ContactList{" +
                "contactList=" + contactList +
                '}';
    }
}
