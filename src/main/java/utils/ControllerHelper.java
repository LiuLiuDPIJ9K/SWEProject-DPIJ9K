package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class ControllerHelper {

    public static void loadAndShowFXML(FXMLLoader fxmlLoader, String resourceName, Stage stage) throws IOException {
        Logger.trace("Loading FXML resource {}", resourceName);
        fxmlLoader.setLocation(fxmlLoader.getClass().getResource(resourceName));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
