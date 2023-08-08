package player;

public class GoalieStats implements Stats {
    public int gamesPlayed, wins, losses, otl, shutouts;
    public double goalsAgainstAverage, savePercentage;

    public GoalieStats(int gamesPlayed, int wins, int losses, int otl, int shutouts,
                       double goalsAgainstAverage, double savePercentage) {
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.losses = losses;
        this.otl = otl;
        this.shutouts = shutouts;
        this.goalsAgainstAverage = goalsAgainstAverage;
        this.savePercentage = savePercentage;
    }

    public String toString() {
        return String.format("Games Played: %s\nWins: %s\nLosses: %s\nOTL: %s\nShutouts: %s\n" +
                        "Goals Against Average: %s\nSave Percentage: %s", gamesPlayed, wins,
                losses, otl, shutouts, goalsAgainstAverage, savePercentage);
    }

}
