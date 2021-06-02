package boxgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.util.Pair;

/**
 * Model of the box game and rules.
 */
public class BoxGameModel {

    public static int BOARD_ROW = 1;
    public static int BOARD_COLUMN = 16;

    private final ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_ROW][BOARD_COLUMN];

    /**
     * Create circles on all squares and assign color to each one according to rules.
     */
    public BoxGameModel() {
        for (int i = 0; i < BOARD_ROW; i++) {
            for (int j = 0; j < BOARD_COLUMN; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<>(Square.NONE);

                if (j < 6 && j % 2 == 0) {
                    board[i][j] = new ReadOnlyObjectWrapper<>(Square.RED);
                } else if (j < 6) {
                    board[i][j] = new ReadOnlyObjectWrapper<>(Square.BLACK);
                }
            }
        }
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * Rules to change circle colors.
     *
     * @param row the row of the board.
     * @param col the column of the board.
     * @param index the index of each circle to determine the change of colors.
     * {@return the information of column and index which can be assigned to determine the color change}
     */
    public Pair move(int row, int col, int index) {
        int count = 0;

        board[row][col].set(
                switch (board[row][col].get()) {
                    case RED -> {
                        count = 1;
                        yield Square.NONE;
                    }
                    case BLACK -> {
                        count = 2;
                        yield Square.NONE;
                    }
                    case NONE -> {
                        if(index == 1) {
                            yield Square.RED;
                        } else if(index == 2) {
                            yield Square.BLACK;
                        } else {
                            yield Square.NONE;
                        }
                    }
                }
        );

        return new Pair<>(col, count);
    }

    /**
     * Rules to determine clicks on moving circle are not transparent.
     *
     * @param row the row of the board.
     * @param col the column of the board.
     * {@return a boolean value to state the clicked circles are not transparent}
     */
    public boolean isSquareEmpty(int row,int col){
        return !board[row][col].get().equals(Square.BLACK)
                && !board[row][col].get().equals(Square.RED);
    }

    /**
     * Rules to check is the game is complete according to rules.
     * Add multiple breaks to save resources.
     *
     * {@return a boolean value to state if it is true the game is finished}
     */
    public boolean isComplete() {
        for (int j = 0; j < BOARD_COLUMN; j++) {
            if (board[0][j].get().equals(Square.RED)) {
                if (board[0][j + 1].get().equals(Square.RED)) {
                    if (board[0][j + 2].get().equals(Square.RED)) {
                        if (board[0][j + 3].get().equals(Square.BLACK)) {
                            if (board[0][j + 4].get().equals(Square.BLACK)) {
                                if (board[0][j + 5].get().equals(Square.BLACK)) {
                                    return true;
                                }
                                else break;
                            }
                            else break;
                        }
                        else break;
                    }
                    else break;
                }
                else break;
            } else if (board[0][j].get().equals(Square.BLACK)) break;
        }

        return false;
    }
}
