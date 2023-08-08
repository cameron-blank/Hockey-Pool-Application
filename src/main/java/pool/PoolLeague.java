package pool;

import common.Constants;
import common.CustomFileUtils;
import common.NHLInfo;
import common.Pair;
import org.json.JSONObject;
import player.Player;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PoolLeague {
    public ArrayList<PoolTeam> teams = new ArrayList<>();
    public String poolName;
    public Rules rules = null;
    public String season = NHLInfo.getLatestSeason();

    //TODO: Check to see if saving, loading saved file works
    /*  File Format:
        {
            "poolName" : "Gordon Head Pool"
            "season" : "20212022"
            "forwardGoals" : 1
            "forwardAssists" : 1
            "defenseGoals" : 2
            "defenseAssists" : 1
            "goalieWins" : 2
            "goalieShutouts" : 3
            "goalieOtl" : 1
            "overtimeGoals" : 0
        }
    */
    public PoolLeague(String poolName, ArrayList<PoolTeam> teams, Rules rules) {
        this.poolName = poolName;
        this.teams = teams;
        this.rules = rules;
    }

    public PoolLeague(String poolName, ArrayList<PoolTeam> teams, Rules rules, String season) {
        this(poolName, teams, rules);
        this.season = season;
    }

    // Teams separated by double newline (\n\n). First line in team is reserved for team name
    public PoolLeague(String poolName, String filePath, Rules rules) {
        this.poolName = poolName;
        this.rules = rules;
        String fileContents = CustomFileUtils.fileToString(filePath);
        teams = createTeamListFromFileContents(fileContents);
    }

    public PoolLeague(String poolName, String filePath, Rules rules, String season) {
        this(poolName, filePath, rules);
        this.season = season;
    }

    //TODO: Possibly refactor this constructor and the next
    public PoolLeague(String poolName, boolean useFileIfPossible) {
        File file = CustomFileUtils.findFileInDirectory(Constants.resourcesDirectory,
                "%s_*.txt".formatted(poolName.replace(" ", "-")));

        if (file != null && useFileIfPossible) {
            PoolLeague poolLeague = createPoolFromFile(file.getPath());
            this.poolName = poolLeague.poolName;
            this.teams = poolLeague.teams;
            this.rules = poolLeague.rules;
            this.season = poolLeague.season;
        } else {
            this.poolName = poolName;
        }

    }

    public PoolLeague(String filePath) {
        PoolLeague poolLeague = createPoolFromFile(filePath);
        this.poolName = poolLeague.poolName;
        this.teams = poolLeague.teams;
        this.rules = poolLeague.rules;
        this.season = poolLeague.season;
    }

    public static PoolLeague createPoolFromFile(String filePath) {

        String fileContents = CustomFileUtils.fileToString(filePath);
        String rulesString = fileContents.split("\\}")[0] + "}";
        JSONObject jsonObject = new JSONObject(rulesString);
        Rules rules = new Rules(jsonObject.getDouble("forwardGoals"), jsonObject.getDouble("forwardAssists"),
                jsonObject.getDouble("defenseGoals"), jsonObject.getDouble("defenseAssists"), jsonObject.getDouble("goalieWins"),
                jsonObject.getDouble("goalieShutouts"), jsonObject.getDouble("goalieOtl"), jsonObject.getDouble("overtimeGoals"));
        ArrayList<PoolTeam> teams = createTeamListFromFileContents(CustomFileUtils.fileToString(filePath));
        String season = jsonObject.getString("season");
        String poolName = jsonObject.getString("poolName");

        return new PoolLeague(poolName, teams, rules, season);
    }

    public ArrayList<Pair<PoolTeam, Double>> getStandings() {
        ArrayList<Pair<PoolTeam, Double>> standings = new ArrayList<>();
        for (PoolTeam team : teams)
            standings.add(new Pair(team, team.getPointTotal(rules, season)));

        Comparator<Pair<PoolTeam, Double>> comparator = (o1, o2) -> (o1.v < o2.v) ? 1 : (o1.v > o2.v) ? -1 : 0;

        Collections.sort(standings, comparator);

        return standings;
    }

    public PoolLeague saveLeague() {
        String fileContents = "{\n\t\"poolName\" : \"%s\",\n\t\"season\" : \"%s\",\n".formatted(poolName, season);
        fileContents += ("\t\"forwardGoals\" : %s,\n\t\"forwardAssists\" : %s,\n\t\"defenseGoals\" : %s,\n\t\"defenseAssists\" : %s,\n\t" +
                "\"goalieWins\" : %s,\n\t\"goalieShutouts\" : %s,\n\t\"goalieOtl\" : %s,\n\t\"overtimeGoals\" : %s\n}\n").formatted(rules.forwardGoals, rules.forwardAssists,
                        rules.defenseGoals, rules.defenseAssists, rules.goalieWins, rules.goalieShutouts, rules.goalieOtl, rules.overtimeGoals);

        for (PoolTeam team : teams) {
            fileContents += team.teamName + "\n";
            for (Player player : team.roster)
                fileContents += "%s (%s),".formatted(player.name, player.id);
            fileContents += "\n\n";
        }

        LocalDate date = LocalDate.now();
        String month = date.getMonthValue() > 9 ? date.getMonthValue() + "" : "0" + date.getMonthValue();
        String day = date.getDayOfMonth() > 9 ? date.getDayOfMonth() + "" : "0" + date.getDayOfMonth();
        String fileName = "%s_%s%s%s.txt".formatted(poolName.replace(" ", "-"), month, day, date.getYear());

        File file = CustomFileUtils.findFileInDirectory(Constants.resourcesDirectory, "%s*.txt".formatted(poolName.replace(" ", "_")));
        if (file != null) file.delete();

        String filePath = "%s%s%s".formatted(System.getProperty("user.dir"), "/src/main/resources/", fileName);
        CustomFileUtils.stringToFile(filePath, fileContents);

        return this;
    }

    public static ArrayList<PoolTeam> createTeamListFromFileContents(String fileContents) {
        fileContents = fileContents.replaceAll("^(\r?\n?)|(\r?\n?)$", "");

        ArrayList<PoolTeam> teams = new ArrayList<>();
        if (fileContents.charAt(0) == '{')
            fileContents = fileContents.replaceAll("\\{[^\\}]*\\}", "").replaceAll("^(\r?\n?)|(\r?\n?)$", "");

        String[] teamStrings = fileContents.replaceAll("^(\r?\n?)|(\r?\n?)$", "").split("\r?\n\r?\n");

        for (String team : teamStrings) {
            String teamName = team.split("\r?\n", 2)[0];
            team = team.split("\r?\n", 2)[1];
            ArrayList<Player> roster = PoolTeam.createRosterFromFileContents(team);
            teams.add(new PoolTeam(teamName, roster));
        }

        return teams;
    }

    public ArrayList<Pair<Pair<PoolTeam, Double>, ArrayList<Pair<Player, Double>>>> getExpandedStandings() {
        ArrayList<Pair<Pair<PoolTeam, Double>, ArrayList<Pair<Player, Double>>>> standings = new ArrayList<>();

        for (PoolTeam team : teams) {
            Pair<PoolTeam, Double> k = new Pair(team, team.getPointTotal(rules, season));
            ArrayList<Pair<Player, Double>> v = team.getPlayerPointTotals(rules, season);
            standings.add(new Pair(k, v));
        }

        return standings;
    }

    public String toString() {
        String str = String.format("-=%s=-\n", poolName.toUpperCase());
        for (PoolTeam poolTeam : teams) {
            str += poolTeam + "\n";
        }
        return str;
    }

}
