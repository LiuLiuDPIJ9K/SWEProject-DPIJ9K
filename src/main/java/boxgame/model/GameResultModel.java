package boxgame.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GameResultModel {

    private transient static final int LIMIT = 10;

    private final Set<GameResultModelEntry> entries;

    public GameResultModel() {
        entries = new HashSet<>();
    }

    public GameResultModel(Set<GameResultModelEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(GameResultModelEntry entry) {
        if (entries.size() < LIMIT) {
            entries.add(entry);
        }
    }

    public static class GameResultModelEntry {
        private final String playerName;
        private final int stepCount;
        private final String outcome;
        private final Date startDate;
        private final Date endDate;

        public GameResultModelEntry(String playerName, int stepsCount, String outcome, Date startDate, Date endDate) {
            this.playerName = playerName;
            this.stepCount = stepsCount;
            this.outcome = outcome;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getOutcome() {
            return outcome;
        }

        public int getStepCount() {
            return stepCount;
        }
    }

    public Set<GameResultModelEntry> getEntries() {
        return entries;
    }

}
