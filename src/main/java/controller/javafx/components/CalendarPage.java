package controller.javafx.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

class CalendarPage extends ViewComponent implements IObserver {

    private final TagHandler tagHandler;
    @FXML
    private Label weekLabel;
    @FXML
    private Button nextWeekButton;
    @FXML
    private Button previousWeekButton;
    @FXML
    private Button newEventButton;

    @FXML
    private Label mondayLabel;
    @FXML
    private Label tuesdayLabel;
    @FXML
    private Label wednesdayLabel;
    @FXML
    private Label thursdayLabel;
    @FXML
    private Label fridayLabel;
    @FXML
    private Label saturdayLabel;
    @FXML
    private Label sundayLabel;

    @FXML
    private FlowPane mondayFlowPane;
    @FXML
    private FlowPane tuesdayFlowPane;
    @FXML
    private FlowPane wednesdayFlowPane;
    @FXML
    private FlowPane thursdayFlowPane;
    @FXML
    private FlowPane fridayFlowPane;
    @FXML
    private FlowPane saturdayFlowPane;
    @FXML
    private FlowPane sundayFlowPane;

    @FXML
    private StackPane calendarPageStackPane;
    @FXML
    private AnchorPane calendarPageAnchorPane;

    private LocalDate weekToDisplay;
    private Map<Integer, List<Event>> weekEvents = new HashMap<>();

    private EventCard eventCard;
    private List<CalendarEventCard> calendarEventCards = new ArrayList<>();

    private EventList eventList;
    private ContactList contactList;

    CalendarPage(EventList eventList, ContactList contactList, TagHandler tagHandler) {
        this.eventList = eventList;
        this.contactList = contactList;
        this.tagHandler = tagHandler;
        eventList.subscribe(this);

        calendarPageAnchorPane.toFront();

        newEventButton.setOnAction(this::newEvent);
        nextWeekButton.setOnAction(this::incrementWeek);
        previousWeekButton.setOnAction(this::decrementWeek);

        weekToDisplay = LocalDate.now();
        setLabels(weekToDisplay);

        for (int i = 1; i <= 7; i++) {
            weekEvents.put(i, new ArrayList<>());
        }

        this.getPane().addEventFilter(KeyEvent.KEY_PRESSED, this::keyPressed);

        onEvent();
    }

    private void keyPressed(KeyEvent key) {
        if (eventCard == null) {
            return;
        }
        KeyCode code = key.getCode();
        if (KeyCode.ESCAPE.equals(code)) {
            closeEventPane();
        } else if (KeyCode.ENTER.equals(code)) {
            eventCard.triggerSave();
        }
    }

    private void incrementWeek(ActionEvent actionEvent) {
        weekToDisplay = weekToDisplay.plusWeeks(1);
        setLabels(weekToDisplay);
        onEvent();
    }

    private void decrementWeek(ActionEvent actionEvent) {
        weekToDisplay = weekToDisplay.minusWeeks(1);
        setLabels(weekToDisplay);
        onEvent();
    }

    private void setLabels(LocalDate date) {
        // Sets date to monday of this week.
        date = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        TemporalField weekGetter = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

        weekLabel.setText("Week " + date.get(weekGetter) + " " + date.getYear());

        mondayLabel.setText("Monday " + date.getDayOfMonth() + "/" + date.getMonthValue());
        date = date.plusDays(1);
        tuesdayLabel.setText("Tuesday " + date.getDayOfMonth() + "/" + date.getMonthValue());
        date = date.plusDays(1);
        wednesdayLabel.setText("Wednesday " + date.getDayOfMonth() + "/" + date.getMonthValue());
        date = date.plusDays(1);
        thursdayLabel.setText("Thursday " + date.getDayOfMonth() + "/" + date.getMonthValue());
        date = date.plusDays(1);
        fridayLabel.setText("Friday " + date.getDayOfMonth() + "/" + date.getMonthValue());
        date = date.plusDays(1);
        saturdayLabel.setText("Saturday " + date.getDayOfMonth() + "/" + date.getMonthValue());
        date = date.plusDays(1);
        sundayLabel.setText("Sunday " + date.getDayOfMonth() + "/" + date.getMonthValue());
    }

    private void newEvent(ActionEvent actionEvent) {
        Event newEvent = eventList.addEvent();
        eventCard = new EventCard(tagHandler, contactList, newEvent);
        initEventCard(newEvent);
    }

    private void editEvent(Event event) {
        eventCard = new EventCard(tagHandler, contactList, event);
        initEventCard(event);
    }

    private void initEventCard(Event event) {
        eventCard.setOnDelete(actionEvent -> eventList.removeEvent(event));
        eventCard.setOnClose(this::closeEventPane);
        calendarPageStackPane.getChildren().add(eventCard.getPane());
        eventCard.getPane().toFront();
    }

    @Override
    public void onEvent() {
        clearCalendar();

        List<Event> eventsThisWeek = eventList.getEventsOfWeek(weekToDisplay);

        for (Event event : eventsThisWeek) {
            weekEvents.get(event.getDateTime().getDayOfWeek().getValue()).add(event);
        }

        for (Map.Entry<Integer, List<Event>> entry : weekEvents.entrySet()) {
            entry.getValue().sort(Comparator.comparing(Event::getDateTime));
            for (Event event : entry.getValue()) {
                CalendarEventCard calendarEventCard = new CalendarEventCard(event);
                calendarEventCard.getPane().setOnMouseClicked(mouseEvent -> editEvent(event));
                calendarEventCards.add(calendarEventCard);
                determineFlowPane(event, calendarEventCard);
            }
        }
    }

    private void closeEventPane(javafx.event.Event event) {
        closeEventPane();
    }

    private void closeEventPane() {
        calendarPageStackPane.getChildren().remove(eventCard.getPane());
        eventList.notifyObservers();
    }

    private void clearCalendar() {
        for (CalendarEventCard calendarEventCard : calendarEventCards) {
            calendarEventCard.unsubscribe();
        }

        clearFlowPanes();
        clearDays();
    }

    private void clearDays() {
        for (Map.Entry<Integer, List<Event>> entry : weekEvents.entrySet()) {
            entry.getValue().clear();
        }
    }

    private void clearFlowPanes() {
        mondayFlowPane.getChildren().clear();
        tuesdayFlowPane.getChildren().clear();
        wednesdayFlowPane.getChildren().clear();
        thursdayFlowPane.getChildren().clear();
        fridayFlowPane.getChildren().clear();
        saturdayFlowPane.getChildren().clear();
        sundayFlowPane.getChildren().clear();
    }

    private void determineFlowPane(Event event, CalendarEventCard calendarEventCard) {
        switch (event.getDateTime().getDayOfWeek().getValue()) {
            case 1:
                mondayFlowPane.getChildren().add(calendarEventCard.getPane());
                break;
            case 2:
                tuesdayFlowPane.getChildren().add(calendarEventCard.getPane());
                break;
            case 3:
                wednesdayFlowPane.getChildren().add(calendarEventCard.getPane());
                break;
            case 4:
                thursdayFlowPane.getChildren().add(calendarEventCard.getPane());
                break;
            case 5:
                fridayFlowPane.getChildren().add(calendarEventCard.getPane());
                break;
            case 6:
                saturdayFlowPane.getChildren().add(calendarEventCard.getPane());
                break;
            case 7:
                sundayFlowPane.getChildren().add(calendarEventCard.getPane());
                break;
            default:
                throw new IllegalArgumentException("Date of event could not be parsed");
        }
    }
}
