package boxgame.javafx.controller;

import boxgame.state.BoxGameModel;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import lombok.val;
import org.tinylog.Logger;

import java.util.ArrayList;

public class BoxGameController {

    @FXML
    private GridPane board;

    @FXML
    private Button giveUpButton;

    @FXML
    private Label stepsLabel;

    private BoxGameModel model = new BoxGameModel();

    private IntegerProperty steps = new SimpleIntegerProperty();

    private boolean canMove = true;

    @FXML
    private void initialize() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }

        steps.set(0);
        stepsLabel.textProperty().bind(steps.asString());
    }

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

    ArrayList<Pair> clickCount = new ArrayList<>();

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
        }
    }

    public void handleResetButton() {
        Logger.info("Resetting Game");
        board.getChildren().clear();
        model = new BoxGameModel();
        clickCount.clear();
        initialize();
    }

}
