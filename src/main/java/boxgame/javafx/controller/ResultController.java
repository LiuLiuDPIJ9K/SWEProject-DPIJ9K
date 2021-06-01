package boxgame.javafx.controller;

import utils.ControllerHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.tinylog.Logger;

import javax.inject.Inject;
import java.io.IOException;

public class ResultController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private TableView tableView;



    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        ControllerHelper.loadAndShowFXML(fxmlLoader, "/fxml/mainmenu.fxml", stage);
    }
}
