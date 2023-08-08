package player;

public class SkaterStats implements Stats {
    public int goals, assists, pim, shots, gamesPlayed, plusMinus, hits, overtimeGoals;
    public SkaterStats(int goals, int assists, int gamesPlayed, int pim, int shots, int plusMinus, int hits, int overtimeGoals) {
        this.goals = goals;
        this.assists = assists;
        this.gamesPlayed = gamesPlayed;
        this.pim = pim;
        this.shots = shots;
        this.plusMinus = plusMinus;
        this.hits = hits;
        this.overtimeGoals = overtimeGoals;
    }

    public String toString() {
        return String.format("Games Played: %s\nGoals: %s\nAssists: %s\nPoints: %s\n" +
                "Penalty Minutes: %s\n+/-: %s\nShots: %s\nHits: %s\nOvertime Goals: %s", gamesPlayed, goals,
                assists, goals + assists, pim, plusMinus, shots, hits, overtimeGoals);
    }

}
