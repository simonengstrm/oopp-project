package attachmenthandler;

import java.nio.file.Paths;

/**
 * A static class to provide the instance of the service.
 */
public final class AttachmentHandlerFactory {
    static private IAttachmentHandler instance = null;

    /**
     * @return The AttachmentHandler Service
     */
    public synchronized static IAttachmentHandler getService() {
        if (instance == null) {
            instance = new AttachmentHandler(Paths.get(System.getProperty("user.home") + "/.prm/"));
        }
        return instance;
    }

    private AttachmentHandlerFactory() {
    }
}
