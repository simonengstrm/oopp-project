package vcf;

import model.*;
import model.exceptions.NameNotAllowedException;
import model.exceptions.NameNotAvailableException;
import model.exceptions.TagNotFoundException;
import model.notes.NoteBook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * Class that reads a file or directory and adds contacts to a given list
 */
class VCFParser implements IVCFParser {

    private final ContactList contacts;

    private final HashMap<String, FIELD> fields = initFIELDSHashMap();

    private HashMap<String, FIELD> initFIELDSHashMap() {
        HashMap<String, FIELD> map = new HashMap<>();
        for (FIELD field : FIELD.values()) {
            map.put(field.getCode(), field);
        }
        return map;
    }

    private final TagHandler tagHandler;

    /**
     * Creates a new parser for VCF files
     *
     * @param contactList the ContactList where the new contacts will be added
     */
    public VCFParser(ContactList contactList, TagHandler tagHandler) {
        this.contacts = contactList;
        this.tagHandler = tagHandler;
    }

    public void addContact(Path path) throws IOException, NameNotAllowedException {
        if (isVCFFile(path)) {
            readContact(path);
        } else {
            throw new FileNotFoundException();
        }
    }

    public void addContactsFromDirectory(Path directory) throws IOException {
        int createdContacts = 0;
        for (Path path : Files.newDirectoryStream(directory)) {
            if (isVCFFile(path)) {
                try {
                    addContact(path);
                    createdContacts++;
                } catch (IOException | NameNotAllowedException ignored) {

                }
            }
        }
        if (createdContacts == 0) throw new IOException("No *.vcf files found in the directory");
    }

    private boolean isVCFFile(Path path) {
        return path.toString().toLowerCase().endsWith(".vcf");
    }

    private void readContact(Path path) throws IOException, NameNotAllowedException {
        HashMap<FIELD, List<String>> data = parseData(path);
        Contact.ContactCache cache = new Contact.ContactCache();
        readName(data, cache);
        readAddress(data, cache);
        readTags(data, cache);
        readPhoneNumber(data, cache);
        readNote(data, cache);
        createUUID(cache);
        contacts.addContact(cache);
    }

    private HashMap<FIELD, List<String>> parseData(Path path) throws IOException {
        HashMap<FIELD, List<String>> parsedData = new HashMap<>();
        for (FIELD field : FIELD.values()) {
            parsedData.put(field, new ArrayList<>());
        }
        Scanner scanner = new Scanner(path);
        String line;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            FIELD type = fields.get(line.split(":")[0].split(";")[0]);
            if (type == null) {
                continue;
            }
            Collection<String> data = getDataFromLine(line, type);
            parsedData.get(type).addAll(data);
        }
        return parsedData;
    }

    private Collection<String> getDataFromLine(String line, FIELD type) {
        Collection<String> data = new ArrayList<>();
        switch (type) {
            case ADDRESS:
            case NAME:
            case NOTE: {
                data.add(line.split(":", 2)[1]);
                break;
            }
            case CATEGORIES: {
                String[] categories = line.split(":")[1].split(",");
                data.addAll(Arrays.asList(categories));
                break;
            }
            case FORMATTED_NAME:
            case VERSION: {
                data.add(line.split(":")[1]);
                break;
            }
            case TELEPHONE: {
                String[] telInfo = line.split(":");
                data.add(telInfo[telInfo.length - 1]);
                break;
            }
        }
        return data;
    }

    private void readName(HashMap<FIELD, List<String>> data, Contact.ContactCache cache) {
        if (data.get(FIELD.FORMATTED_NAME).size() > 0) {
            cache.name = data.get(FIELD.FORMATTED_NAME).get(0);
            return;
        }
        if (data.get(FIELD.NAME).size() <= 0) {
            cache.name = "";
            return;
        }
        String[] unFormattedName = data.get(FIELD.NAME).get(0).split(";");
        cache.name = unFormattedName[3] +
                unFormattedName[1] +
                unFormattedName[2] +
                unFormattedName[0] +
                unFormattedName[4];
    }

    private void readAddress(HashMap<FIELD, List<String>> data, Contact.ContactCache cache) {
        if (!(data.get(FIELD.ADDRESS).size() > 0)) {
            cache.address = "";
            return;
        }
        String[] addressParts = data.get(FIELD.ADDRESS).get(0).split(";");
        List<String> activeParts = new ArrayList<>();
        for (String part : addressParts) {
            if (part.length() > 0) {
                activeParts.add(part);
            }
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = activeParts.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        cache.address = sb.toString();
    }

    private void readTags(HashMap<FIELD, List<String>> data, Contact.ContactCache cache) {
        List<ITag> tags = new ArrayList<>();
        data.get(FIELD.CATEGORIES).forEach(tag -> {
            try {
                tags.add(tagHandler.createTag(tag));
            } catch (NameNotAvailableException e) {
                try {
                    tags.add(tagHandler.getTag(tag));
                } catch (TagNotFoundException ignored) {
                }
            } catch (NameNotAllowedException ignored) {
            }
        });
        cache.tags = tags;
    }

    private void readPhoneNumber(HashMap<FIELD, List<String>> data, Contact.ContactCache cache) {
        List<String> numbers = data.get(FIELD.TELEPHONE);
        cache.phoneNumber = numbers.size() > 0 ? numbers.get(0) : "";
    }

    private void readNote(HashMap<FIELD, List<String>> data, Contact.ContactCache cache) {
        NoteBook noteBook = new NoteBook();
        data.get(FIELD.NOTE).forEach(noteBook::addNote);
        cache.noteBook = noteBook;
    }

    private void createUUID(Contact.ContactCache cache) {
        cache.directoryId = UUID.randomUUID();
    }
}
