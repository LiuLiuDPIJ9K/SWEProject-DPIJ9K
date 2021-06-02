package boxgame.javafx.controller;

import boxgame.model.GameResultModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class ResultController {

    @FXML
    TableView<GameResultModel.GameResultModelEntry> highScoreTable;

    @FXML
    TableColumn<GameResultModel.GameResultModelEntry, String> playerName;

    @FXML
    TableColumn<GameResultModel.GameResultModelEntry, Integer> stepsName;

    @FXML
    TableColumn<GameResultModel.GameResultModelEntry, String> outcomeName;

    @FXML
    TableColumn<GameResultModel.GameResultModelEntry, Date> startDate;

    @FXML
    TableColumn<GameResultModel.GameResultModelEntry, Date> endDate;

    BoxGameController boxGame;

    public void initialize() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        boxGame = new BoxGameController();

        String json = Files.readString(Path.of("src/main/java/boxgame/result/GameResult.json"));

        GameResultModel gameResult = gson.fromJson(json, GameResultModel.class);

        gameResult.getEntries().forEach(gameResultEntry -> boxGame.getResultModel().addEntry(
                new GameResultModel.GameResultModelEntry(
                        gameResultEntry.getPlayerName(),
                        gameResultEntry.getStepCount(),
                        gameResultEntry.getOutcome(),
                        gameResultEntry.getStartDate(),
                        gameResultEntry.getEndDate()

                )
        ));

        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        stepsName.setCellValueFactory(new PropertyValueFactory<>("stepCount"));
        outcomeName.setCellValueFactory(new PropertyValueFactory<>("outcome"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        highScoreTable.getSortOrder().add(stepsName);

        //highScoreTable.getItems().clear();
        highScoreTable.getItems().addAll(boxGame.getResultModel().getEntries());
    }

    @FXML
    public void handleRestartButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/boxgame.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
