package boxgame.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.tinylog.Logger;

import java.io.IOException;

/**
 * Provide controls to main menu view.
 */
public class MainMenuController {

    @FXML
    private TextField nameID;

    @FXML
    private void initialize() {
        nameID.setText(System.getProperty("user.name"));
    }

    /**
     * Navigate to the box game view and set the player name.
     *
     * @param event the action of click next button.
     * @throws IOException if no FXML file was found.
     */
    @FXML
    private void handleNextButton(ActionEvent event) throws IOException {
        Logger.info("Name entered: {}", nameID.getText());
        BoxGameController.setPlayerName(nameID.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/boxgame.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
