package boxgame.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class GameResultModelTest {

    GameResultModel resultModel = new GameResultModel();

    @Test
    void addEntry() {
        resultModel.addEntry(new GameResultModel.GameResultModelEntry("Test", 1, "Unfinished", new Date(), new Date()));
    }

    @Test
    void getEntries() {
        resultModel.getEntries();
    }
}