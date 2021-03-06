package controller.javafx.components;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.search.ISearchObservable;
import model.search.ISearchObserver;
import model.search.ISearchable;
import model.search.SearchEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * A component responsible for getting text input and returning results relevant to the given input.
 * Client decides when to query for the results. Searches can be done without handling the results.
 *
 * @param <T> the searchable type of the domain to search.
 * @author Simon Johnsson
 */
public class SearchBar<T extends ISearchable<String>> extends ViewComponent implements ISearchObservable {

    private SearchEngine<T> searchEngine;
    private List<T> results;
    private final int tolerance;
    private final List<ISearchObserver> observers;

    @FXML
    private TextField textField;
    @FXML
    private Button searchButton;

    /**
     * Constructs a search bar with the given search base and tolerance.
     * Default results are the entire search base.
     *
     * @param searchBase the information to iterate
     * @param tolerance  the maximum allowed edit distance from the search query to the result
     */
    SearchBar(List<T> searchBase, int tolerance) {
        this.searchEngine = new SearchEngine<>(searchBase);
        observers = new ArrayList<>();
        this.tolerance = tolerance;
        results = searchBase;
        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                search(keyEvent);
            }
        });
        searchButton.setOnAction(this::search);
    }

    /**
     * Performs a search operation with the input text and updates the result list.
     * Searches are case-insensitive and returns a result depending on the input query and tolerance.
     * Tolerance is the maximum allowed edit distance for the query and result.
     * If no input is made, the entire search base is considered the result.
     *
     * @param event the event triggering the search
     */
    @FXML
    void search(Event event) {
        if ("".equals(textField.getText())) {
            results = searchEngine.getSearchBase();
        } else {
            results = searchEngine.search(textField.getText(), tolerance);
        }
        notifyResult();
    }

    /**
     * Returns the results from the previous search operation.
     *
     * @return a list of the searchable type
     */
    List<T> getResults() {
        return new ArrayList<>(results);
    }

    /**
     * Constructs a new search engine with the given search base and sets it to the current engine
     *
     * @param searchBase the new search base
     */
    void updateSearchBase(List<T> searchBase) {
        this.searchEngine = new SearchEngine<>(searchBase);
    }


    @Override
    public void subscribe(ISearchObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(ISearchObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyResult() {
        for (ISearchObserver obs : observers) {
            obs.onSearch();
        }
    }
}