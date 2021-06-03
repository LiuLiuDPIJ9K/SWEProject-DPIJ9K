package boxgame.model;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoxGameModelTest {

    BoxGameModel model = new BoxGameModel();

    @Test
    void move() {
        int row = 0;
        int col = 1;
        int index = 0;

        Pair expected = new Pair(1,2);
        assertEquals(expected, model.move(row, col, index));
    }

    @Test
    void isSquareEmpty() {
        int row = 0;
        int col = 1;

        assertFalse(model.isSquareEmpty(row, col));

        col = 15;
        assertTrue(model.isSquareEmpty(row, col));
    }

    @Test
    void isComplete() {
        assertFalse(model.isComplete());
    }
}