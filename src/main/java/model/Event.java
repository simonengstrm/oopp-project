package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/***
 * Represents an event occurring at a point in time, past or future, with a name/description and list of contacts/categories it is included in.
 */
public class Event {

    private String name;
    private Address address = new Address("");
    private LocalDateTime dateTime;
    private String description;

    private ITag tag;
    private Collection<Contact> contacts = new ArrayList<>();

    /***
     * Creates an event with the given parameters.
     * @param name the name of the event
     * @param address the physical address of the event
     * @param dateTime the date and time of the event
     * @param description the description of the event
     * @param contacts the list containing the IDs of the contacts tagged in the event
     * @param tag the list containing the IDs of the tags tagged on the event
     */
    public Event(String name, String address, LocalDateTime dateTime, String description, ArrayList<Contact> contacts, ITag tag) {
        this.name = name;
        this.address = new Address(address);
        this.dateTime = dateTime;
        this.description = description;
        this.contacts = contacts;
        this.tag = tag;
    }

    /***
     * Creates an event with the given parameters
     * @param name the name of the event
     * @param date the date of the event
     */
    public Event(String name, LocalDateTime date) {
        this.name = name;
        this.dateTime = date;
    }

    public boolean isInFuture() {
        return dateTime.compareTo(LocalDateTime.now()) > 0;
    }

    /***
     * Returns the name of the event
     * @return name of the event
     */
    public String getName() {
        return name;
    }

    /***
     * Sets the name of the event
     * @param name the name of the event
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * Returns the address of the event
     * @return address of the event
     */
    public String getAddress() {
        return address.getAddress();
    }

    /***
     * Sets the address of the event
     * @param address the address of the event
     */
    public void setAddress(String address) {
        this.address = this.address.setAddress(address);
    }

    /***
     * Returns the date/time object of the event
     * @return the date/time object of the event
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /***
     * Sets the date and time of the event
     * @param dateTime the date and time of the event
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /***
     * Returns the description of the event
     * @return the description of the event
     */
    public String getDescription() {
        return description;
    }

    /***
     * Sets the description of the event
     * @param description the description of the event
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /***
     * Adds a tag to the event
     * @param tag the tag to be added
     */
    public void addTag(ITag tag){
        this.tag = tag;
    }

    /***
     * Removes a tag from the event
     */
    public void removeTag(){
        tag = null;
    }

    /***
     * Returns tag
     * @return the tag
     */
    public ITag getTag(){
        return this.tag;
    }

    /***
     * Adds a contact to the event
     * @param contact the contact to be added
     */
    public void addContact(Contact contact){
        if (!contacts.contains(contact)){
            contacts.add(contact);
        }
    }

    /***
     * Removes a contact from the event
     * @param contact the contact to be removed
     */
    public void removeContact(Contact contact){
        contacts.remove(contact);
    }

    /***
     * Returns the contact arraylist.
     * @return the contact arraylist.
     */
    public Collection<Contact> getContacts(){
        return this.contacts;
    }
}
