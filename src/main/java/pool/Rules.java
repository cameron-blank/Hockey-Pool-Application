package pool;

public class Rules {
    // Multipliers
    public double  forwardGoals = 1, forwardAssists = 1,
            defenseGoals = 1, defenseAssists = 1,
            goalieWins = 1, goalieShutouts = 1, goalieOtl = 1,
            overtimeGoals = 0;

    public Rules() {}
    public Rules(double forwardGoals, double forwardAssists, double defenseGoals,
                 double defenseAssists, double goalieWins, double goalieShutouts, double goalieOtl, double overtimeGoals) {
        this.forwardGoals = forwardGoals; this.forwardAssists = forwardAssists;
        this.defenseGoals = defenseGoals; this.defenseAssists = defenseAssists;
        this.goalieWins = goalieWins; this.goalieShutouts = goalieShutouts; this.goalieOtl = goalieOtl;
        this.overtimeGoals = overtimeGoals;
    }


}
