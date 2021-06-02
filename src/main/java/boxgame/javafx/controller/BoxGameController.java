package boxgame.javafx.controller;

import boxgame.model.BoxGameModel;
import boxgame.model.GameResultModel;
import com.google.gson.Gson;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.val;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;


/**
 * Provides controls to the box game view.
 */
public class BoxGameController {

    @FXML
    private GridPane board;

    @FXML
    private Label stepsLabel;

    private BoxGameModel model = new BoxGameModel();

    private final IntegerProperty steps = new SimpleIntegerProperty();

    private boolean canMove = true;

    private final GameResultModel resultModel = new GameResultModel();

    private static String playerName;

    private static String outcome;

    private Date startDate;

    /**
     * Initialize the game and create square to the GridPane.
     */
    @FXML
    private void initialize() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }

        startDate = new Date();
        steps.set(0);
        stepsLabel.textProperty().bind(steps.asString());

        if(steps.get() == 0) {
            setOutcome("Unfinished");
        }
    }

    /**
     * Create circles/stones according to game rules.
     *
     * @param i the row coordinate of the squares.
     * @param j the column coordinate of the squares.
     * {@return the squares and created circles on the board}
     */
    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        var piece = new Circle(30);
        square.getStyleClass().add("square");

        piece.fillProperty().bind(
                new ObjectBinding<>() {
                    {
                        super.bind(model.squareProperty(i, j));
                    }

                    @Override
                    protected Paint computeValue() {
                        return switch (model.squareProperty(i, j).get()) {
                            case NONE -> Color.TRANSPARENT;
                            case RED -> Color.RED;
                            case BLACK -> Color.BLACK;
                        };
                    }
                }
        );

        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    /**
     * Pair type of ArrayList to store the order and information of mouse clicks on circles.
     */
    ArrayList<Pair> clickCount = new ArrayList<>();

    /**
     * To handle each mouse click.
     * Use {@code moveCount} to store the column and index of circles to be moved.
     * Use {@code move} to change the color of circles based on stored information(move the circle).
     * Store game data if the game is success.
     *
     * @param event the action of a mouse click.
     */
    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var col = GridPane.getColumnIndex(square);
        var row = GridPane.getRowIndex(square);
        var length = clickCount.size();

        Pair<Integer, Integer> moveCount;

        if (length < 2) {
            moveCount = model.move(row, col, 0);
            Logger.info("Clicked on square {}", col);
            canMove = true;

            if (moveCount.getValue() != 0 )  {
                clickCount.add(moveCount);
            }
        }
        else {
            if (length % 2 == 0) {
                Pair click1 = clickCount.get(length - 2);
                Pair click2 = clickCount.get(length - 1);
                val key_1 = (int) click1.getKey();
                val key_2 = (int) click2.getKey();
                val value_1 = (int) click1.getValue();
                val value_2 = (int) click2.getValue();

                if (key_1 > key_2 && model.isSquareEmpty(row, col)
                        && model.isSquareEmpty(row, col - 1)) {
                    for (int j = key_2 + 1; j < key_1; j++) {
                        if (!model.isSquareEmpty(row, j)) {
                            Logger.info("Invalid move!");
                            model.move(row, key_1, value_1);
                            model.move(row, key_2, value_2);
                            canMove = false;
                        }
                    }

                    if(canMove) {
                        model.move(row, col, value_1);
                        model.move(row, col - 1, value_2);
                        steps.set(steps.get() + 1);
                        Logger.info("Moved to square {} and {}", col - 1, col);
                    }
                } else if (key_1 < key_2 && model.isSquareEmpty(row, col)
                        && model.isSquareEmpty(row, col + 1)) {
                    for (int j = key_1 + 1; j < key_2; j++) {
                        if (!model.isSquareEmpty(row, j)) {
                            Logger.info("Invalid move!");
                            model.move(row, key_1, value_1);
                            model.move(row, key_2, value_2);
                            canMove = false;
                        }
                    }

                    if(canMove) {
                        model.move(row, col, value_1);
                        model.move(row, col + 1, value_2);
                        steps.set(steps.get() + 1);
                        Logger.info("Moved to square {} and {}", col, col + 1);
                    }
                } else {
                    model.move(row, key_1, value_1);
                    model.move(row, key_2, value_2);
                    Logger.info("Invalid move!");
                }
            }

            clickCount.clear();
        }

        if (model.isComplete()) {
            Logger.info("Success!");
            setOutcome("Finished");
            try {
                handleGiveUpButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if(steps.get() == 0) {
                setOutcome("Unfinished");
            }
            setOutcome("Unfinished");
        }
    }

    /**
     * Handle click on reset button.
     */
    public void handleResetButton() {
        Logger.info("Resetting Game");
        board.getChildren().clear();
        model = new BoxGameModel();
        clickCount.clear();
        initialize();
    }

    public static void setPlayerName(String string) {
        playerName = string;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setOutcome(String string) {
        outcome = string;
    }

    public String getOutcome() {
        return outcome;
    }

    public Date getStartDate() {
        return startDate;
    }

    public GameResultModel getResultModel() {
        return resultModel;
    }

    /**
     * Save results stored in hashset to json file using Gson plugin.
     *
     * @throws IOException if no file can be found.
     */
    public void saveResult() throws IOException {
        Date endDate = new Date();

        Gson gson = new Gson();

        getResultModel().addEntry(
                new GameResultModel.GameResultModelEntry(
                        getPlayerName(), steps.get(), getOutcome(), getStartDate(), endDate)
                );

        //getResultModel().getEntries().stream().sorted(Comparator.comparingInt(GameResultModel.GameResultModelEntry::getStepCount));
        Files.write(Path.of("src/main/java/boxgame/result/GameResult.json"), gson.toJson(getResultModel()).getBytes());
    }

    /**
     * Handle click on give up button.
     * Invoke method of saving date and navigate to result view.
     *
     * @throws IOException if there is exception in {@code saveResult}.
     */
    public void handleGiveUpButton() throws IOException {
        saveResult();

        Stage currentStage = (Stage) board.getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/result.fxml"))));
        stage.setResizable(false);
        stage.show();
    }

}
