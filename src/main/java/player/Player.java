package player;

import common.Constants;
import common.CustomFileUtils;
import org.json.JSONObject;
import pool.Rules;
import team.NameTranslator;
import team.Team;

import java.util.ArrayList;

public class Player {

    public String name;
    public int id = 0;
    public String position = "";
    public String jerseyNumber = "";
    public String teamName;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        Player player = findPlayerInfo(id);
        this.position = player.position;
        this.jerseyNumber = player.jerseyNumber;
    }

    public Player(String name, String team) {
        this.name = name;
        Player player = findPlayerInfo(name, team);
        this.position = player.position;
        this.id = player.id;
        this.jerseyNumber = player.jerseyNumber;
        this.teamName = player.teamName;

    }

    public Player(String name, int id, String position, String jerseyNumber, String teamName) {
        this.name = name;
        this.id = id;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.teamName = teamName;
    }

    public Stats getRegularSeasonStats(String season) {
        String urlString = String.format(Constants.SINGLE_SEASON_STATS_URL, id, season);
        JSONObject statsPage = CustomFileUtils.getJSONObjectFromURL(urlString);

        JSONObject statsObject = statsPage.getJSONArray("stats").getJSONObject(0).getJSONArray("splits").getJSONObject(0).getJSONObject("stat");

        if (position.equals("G"))
            return new GoalieStats(statsObject.getInt("games"), statsObject.getInt("wins"), statsObject.getInt("losses"),
                    statsObject.getInt("ot"), statsObject.getInt("shutouts"), statsObject.getDouble("goalAgainstAverage"),
                    statsObject.getDouble("savePercentage"));

        return new SkaterStats(statsObject.getInt("goals"), statsObject.getInt("assists"),
                statsObject.getInt("games"), statsObject.getInt("pim"), statsObject.getInt("shots"),
                statsObject.getInt("plusMinus"), statsObject.getInt("hits"), statsObject.getInt("overTimeGoals"));
    }

    public double getPoolPoints(Rules rules, String season) {
        Stats stats = getRegularSeasonStats(season);
        if (position.equals("G")) {
            GoalieStats goalieStats = (GoalieStats) stats;
            return goalieStats.wins * rules.goalieWins +
                    goalieStats.otl * rules.goalieOtl +
                    goalieStats.shutouts * rules.goalieShutouts;
        } else {
            SkaterStats skaterStats = (SkaterStats) stats;
            if (Constants.forwardPositions.contains(position)) {
                return skaterStats.goals * rules.forwardGoals +
                        skaterStats.assists * rules.forwardAssists +
                        skaterStats.overtimeGoals * rules.overtimeGoals;
            } else {
                return skaterStats.goals * rules.defenseGoals +
                        skaterStats.assists * rules.defenseAssists +
                        skaterStats.overtimeGoals * rules.overtimeGoals;
            }
        }
    }

    public String toString() {
        return String.format("%s (%s): ID = %s, Position = %s", name, jerseyNumber, id, position);
    }

    private static Player findPlayerInfo(String name, String team) {
        ArrayList<Player> roster = Team.getRoster(team);
        for (Player player : roster) {
            if (player.name.equals(name))
                return player;
        }

        return null;
    }

    private static Player findPlayerInfo(int id) {
        JSONObject playerPage = CustomFileUtils.getJSONObjectFromURL(Constants.PEOPLE_URL_PREFIX + "/" + id);
        JSONObject playerInfo = playerPage.getJSONArray("people").getJSONObject(0);
        String jerseyNumber = playerInfo.getString("primaryNumber");
        String name = playerInfo.getString("fullName");
        String position = playerInfo.getJSONObject("primaryPosition").getString("abbreviation");
        String teamName = NameTranslator.getTeamNameFromFullName(playerInfo.getJSONObject("currentTeam").getString("name"));

        return new Player(name, id, position, jerseyNumber, teamName);
    }
}
