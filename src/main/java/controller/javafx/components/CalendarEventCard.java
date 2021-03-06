package controller.javafx.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.Event;
import model.IObserver;

import java.util.Objects;

class CalendarEventCard extends ViewComponent implements IObserver {

    @FXML
    private AnchorPane calendarEventAnchorPane;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label eventTimeLabel;
    @FXML
    private Label eventAddressLabel;

    private Event event;

    CalendarEventCard(Event event) {
        this.event = event;
        event.subscribe(this);
        setLabels();
        setBorder();
    }

    private void setLabels() {
        eventNameLabel.setText(event.getName());
        eventTimeLabel.setText(event.getDateTime().toLocalTime().toString());
        eventAddressLabel.setText(event.getAddress());
    }

    private void setBorder() {
        if (!Objects.isNull(event.getTag())) {
            getPane().setStyle("-fx-border-color: #" + event.getTag().getColor() + ";");
        }
    }

    public void unsubscribe() {
        event.unSubscribe(this);
    }

    @Override
    public void onEvent() {
        setLabels();
        setBorder();
    }
}
