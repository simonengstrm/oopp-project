package controller.javafx.components;

import application.HostServicesProvider;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.nio.file.Path;

class AttachmentCard extends ViewComponent {
    @FXML
    private AnchorPane baseAnchorPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Button removeButton;
    private final Path attachment;
    private EventHandler<Event> deleteHandler;

    AttachmentCard(Path attachment) {
        this.attachment = attachment;
        nameLabel.setText(attachment.getFileName().toString());
        baseAnchorPane.setOnMouseClicked(this::openAttachment);
        this.removeButton.setOnAction(this::deleteAttachment);
    }

    private void openAttachment(MouseEvent event) {
        HostServicesProvider.getHostServices().showDocument(attachment.toString());
    }

    private void deleteAttachment(Event event) {
        deleteHandler.handle(event);
    }

    public void setDeleteHandler(EventHandler<Event> handler) {
        deleteHandler = handler;
    }
}
