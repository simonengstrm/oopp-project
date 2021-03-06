package attachmenthandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * This class implements the functionality specified by the IAttachmentHandler interface.
 */
class AttachmentHandler implements IAttachmentHandler {
    private final Path baseDirectory;

    /**
     * @param baseDirectory Base directory to store files in
     */
    AttachmentHandler(Path baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public void addAttachment(UUID id, Path sourceFile) throws IOException {
        Path attachmentDirectory = baseDirectory.resolve(id + "/attachments");
        Files.createDirectories(attachmentDirectory);
        Files.copy(sourceFile, attachmentDirectory
                .resolve(sourceFile.getFileName()), REPLACE_EXISTING);

    }

    public void addAttachment(UUID id, Path sourceFile, String category) throws IOException, IllegalArgumentException {
        category = category.toLowerCase(Locale.getDefault());
        if (!category.chars().allMatch(Character::isLetter)) {
            throw new IllegalArgumentException("A category should only contain letters");
        }
        Path attachmentDirectory = baseDirectory.resolve(id + "/attachments");
        Files.createDirectories(attachmentDirectory.resolve(category));
        Files.copy(sourceFile, attachmentDirectory
                .resolve(category)
                .resolve(sourceFile.getFileName()), REPLACE_EXISTING);
    }

    public List<Path> getAttachments(UUID id) throws IOException {
        Path attachmentDirectory = baseDirectory.resolve(id + "/attachments");
        List<Path> files = new ArrayList<>();
        if (!Files.exists(attachmentDirectory)) {
            return files;
        }
        Files.walk(attachmentDirectory)
                .filter(Files::isRegularFile).forEach(files::add);
        return files;
    }

    public List<Path> getAttachments(UUID id, String category) throws IOException, IllegalArgumentException {
        Path attachmentDirectory = baseDirectory.resolve(id + "/attachments");
        category = category.toLowerCase(Locale.getDefault());
        if (!category.chars().allMatch(Character::isLetter)) {
            throw new IllegalArgumentException("A category should only contain letters");
        }
        List<Path> files = new ArrayList<>();
        if (!Files.exists(attachmentDirectory.resolve(category))) {
            return files;
        }
        Files.walk(attachmentDirectory.resolve(category)).filter(Files::isRegularFile).forEach(files::add);
        return files;
    }

    public List<String> getAttachmentCategories(UUID id) throws IOException {
        List<String> attachmentCategories = new ArrayList<>();
        Path attachmentDirectory = baseDirectory.resolve(id + "/attachments");
        Files.walk(attachmentDirectory, 1)
                .filter(Files::isDirectory)
                .forEach((directory) -> {
                    if (directory != attachmentDirectory) {
                        attachmentCategories.add(directory.getFileName().toString());
                    }
                });
        return attachmentCategories;
    }


    public void removeAttachment(UUID id, Path attachment) throws IOException, IllegalArgumentException {
        if (attachment.toString().contains(baseDirectory.resolve(id.toString()).toString())) {
            Files.delete(attachment);
        } else {
            throw new IllegalArgumentException("Attachment does not belong to contact");
        }
    }


    public void removeAttachmentCategory(UUID id, String category) throws IOException {
        Path attachmentDirectory = baseDirectory.resolve(id + "/attachments");
        if (Files.exists(attachmentDirectory.resolve(category))) {
            deleteDirectoryRecursively(attachmentDirectory.resolve(category));
        }
    }


    public void removeAllAttachments(UUID id) throws IOException {
        if (Files.exists(baseDirectory.resolve(id + "/attachments"))) {
            deleteDirectoryRecursively(baseDirectory.resolve(id + "/attachments"));
        }
    }


    public void removeAllFiles(UUID id) throws IOException {
        if (Files.exists(baseDirectory.resolve(id.toString()))) {
            deleteDirectoryRecursively(baseDirectory.resolve(id.toString()));
        }
    }


    public void saveMainImage(UUID id, Path picture) throws IllegalArgumentException, IOException {
        List<String> imageFileExtensions = Arrays.asList("bmp", "gif", "jpeg", "jpg", "png");
        Path mainImageDirectory = baseDirectory.resolve(id.toString()).resolve("mainImage/");
        if (!imageFileExtensions.contains(getFileExtension(picture).toLowerCase(Locale.getDefault()))) {
            throw new IllegalArgumentException("The specified file needs to be a image.");
        }
        removeMainImage(id);
        Files.createDirectories(mainImageDirectory);
        Files.copy(picture, mainImageDirectory.resolve(picture.getFileName()), REPLACE_EXISTING);
    }


    public void removeMainImage(UUID id) throws IOException {
        Path mainImage = baseDirectory.resolve(id.toString()).resolve("mainImage");
        if (Files.exists(mainImage)) {
            deleteDirectoryRecursively(mainImage);
        }
    }


    public Path getMainImage(UUID id) throws IOException {
        Path mainImageDirectory = baseDirectory.resolve(id.toString()).resolve("mainImage/");
        List<Path> mainImageDirectoryFiles = new ArrayList<>();
        Files.walk(mainImageDirectory, 1).filter(Files::isRegularFile).forEach(mainImageDirectoryFiles::add);

        if (mainImageDirectoryFiles.isEmpty()) {
            throw new NoSuchFileException("There is no mainImage for this ID");
        } else {
            return mainImageDirectoryFiles.get(0);
        }
    }


    private void deleteDirectoryRecursively(Path directory) throws IOException {
        Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private String getFileExtension(Path file) {
        String name = file.getFileName().toString();
        int lastDot = name.lastIndexOf(".");
        if (lastDot == -1) {
            return ""; //no extension
        }
        return name.substring(lastDot + 1); // returns extension without dot
    }

}
