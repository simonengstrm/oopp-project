package controller.javafx.components;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import model.Event;
import model.EventList;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

class CalendarPage extends ViewComponent implements Initializable {

    @FXML private Label weekLabel;
    @FXML private Button nextWeekButton;
    @FXML private Button previousWeekButton;

    @FXML private Label mondayLabel;
    @FXML private Label tuesdayLabel;
    @FXML private Label wednesdayLabel;
    @FXML private Label thursdayLabel;
    @FXML private Label fridayLabel;
    @FXML private Label saturdayLabel;
    @FXML private Label sundayLabel;

    @FXML private FlowPane mondayFlowPane;
    @FXML private FlowPane tuesdayFlowPane;
    @FXML private FlowPane wednesdayFlowPane;
    @FXML private FlowPane thursdayFlowPane;
    @FXML private FlowPane fridayFlowPane;
    @FXML private FlowPane saturdayFlowPane;
    @FXML private FlowPane sundayFlowPane;

    private EventList eventList;

    public CalendarPage(EventList eventList) {
        this.eventList = eventList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
