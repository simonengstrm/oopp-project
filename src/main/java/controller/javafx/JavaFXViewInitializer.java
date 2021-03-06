package controller.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

/***
 * A utility class containing functionality to initialize the JavaFX controller and view.
 */
public final class JavaFXViewInitializer {

    private JavaFXViewInitializer() {

    }

    /***
     * Initiates the JavaFX environment using the given stage from the launch method, and the user from the instantiated model.
     * Loads the root window and sets the RootWindow class to the controller.
     * Displays the scene.
     * @param stage the stage to initialize the scene upon
     * @param user the instantiated "model" data-object
     * @throws IOException throws an IOException if path is faulty.
     */
    public static void initializeJavaFXView(Stage stage, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXViewInitializer.class.getResource("/view/javafx/RootWindow.fxml"));
        loader.setControllerFactory(aClass -> new RootWindow(user));

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("PRM");
        stage.show();
    }

    /***
     * Creates a new UserWizard given a JavaFX stage.
     * @param stage the stage to display the wizard on.
     * @return a reference to the UserWizard object.
     * @throws IOException if the FXML file of the UserWizard cannot be loaded.
     */
    public static UserWizard createUserWizard(Stage stage) throws IOException {
        UserWizard wizard = new UserWizard();
        FXMLLoader loader = new FXMLLoader(JavaFXViewInitializer.class.getResource("/view/javafx/UserWizard.fxml"));
        loader.setController(wizard);

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("User Wizard");
        stage.show();

        return wizard;
    }

}
