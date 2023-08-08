package pool;

import common.Constants;
import common.CustomFileUtils;
import common.Pair;
import player.Player;
import team.NameTranslator;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PoolTeam {
    public ArrayList<Player> roster = new ArrayList<>();
    public String teamName;

    //TODO: Maybe save teamName to file, add functionality to import said file as well
    public PoolTeam(String teamName, ArrayList<Player> roster) {
        this.teamName = teamName;
        this.roster = roster;
    }

    public PoolTeam(String teamName) {
        this.teamName = teamName;
        File file = CustomFileUtils.findFileInDirectory(Constants.resourcesDirectory,
                "%s*.txt".formatted(teamName.replace(" ", "-")));
        if (file != null) {
            this.roster = createRosterFromFileContents(CustomFileUtils.fileToString(file.getPath()));
        }
    }

    // File Format: <Player> (<Abbreviation or ID>)<\n|,|;>...
    public PoolTeam(String teamName, String filePath) {
        this(teamName, filePath, false);
    }

    public PoolTeam(String teamName, String filePath, boolean useExistingFileIfPossible) {
        this.teamName = teamName;
        File file = CustomFileUtils.findFileInDirectory(Constants.resourcesDirectory,
                "%s_*.txt".formatted(teamName.replace(" ", "-")));
        if (file != null && useExistingFileIfPossible)
            this.roster = createRosterFromFileContents(CustomFileUtils.fileToString(file.getPath()));
        else
            this.roster = createRosterFromFileContents(CustomFileUtils.fileToString(filePath));
    }

    public static ArrayList<Player> createRosterFromFileContents(String fileContents) {
        ArrayList<Player> roster = new ArrayList<>();
        String[] players = fileContents.split("(,|\r\n|\n|;)");
        Pattern pattern = Pattern.compile(" ?([a-zA-Z\\u00C0-\\u024F\\u1E00-\\u1EFF \\.'-]*) \\((.*)\\)");

        for (int i = 0; i < players.length; i++) {
            Matcher matcher = pattern.matcher(players[i]);
            matcher.find();
            String playerName = matcher.group(1);
            if (matcher.group(2).matches("\\d*")) {
                int id = Integer.parseInt(matcher.group(2));
                roster.add(new Player(playerName, id));
            } else {
                String team = NameTranslator.getTeamNameFromAbbreviation(matcher.group(2));
                roster.add(new Player(playerName, team));
            }
        }

        return roster;
    }

    public PoolTeam addPlayer(Player player) {
        roster.add(player);
        return this;
    }

    public double getPointTotal(Rules rules, String season) {
        double accumulator = 0;
        ArrayList<Pair<Player, Double>> pointTotals = getPlayerPointTotals(rules, season);
        for (Pair<Player, Double> pair : pointTotals) {
            accumulator += pair.v;
        }
        return accumulator;
    }

    public ArrayList<Pair<Player, Double>> getPlayerPointTotals(Rules rules, String season) {
        ArrayList<Pair<Player, Double>> pointTotals = new ArrayList<>();
        for (Player player : roster)
            pointTotals.add(new Pair(player, player.getPoolPoints(rules, season)));

        Comparator<Pair<Player, Double>> comparator = (o1, o2) -> (o1.v < o2.v) ? 1 : (o1.v > o2.v) ? -1 : 0;
        Collections.sort(pointTotals, comparator);

        return pointTotals;
    }

    public PoolTeam saveTeam() {
        LocalDate date = LocalDate.now();
        String month = date.getMonthValue() > 9 ? date.getMonthValue() + "" : "0" + date.getMonthValue();
        String day = date.getDayOfMonth() > 9 ? date.getDayOfMonth() + "" : "0" + date.getDayOfMonth();
        String fileName = "%s_%s%s%s.txt".formatted(teamName.replace(" ", "-"), month, day, date.getYear());
        File file = CustomFileUtils.findFileInDirectory(Constants.resourcesDirectory,
                "%s*.txt".formatted(teamName.replace(" ", "_")));
        if (file != null) file.delete();

        String fileContents = "";
        for (Player player : roster) {
            fileContents += "%s (%s),".formatted(player.name, player.id);
        }

        String filePath = "%s%s%s".formatted(System.getProperty("user.dir"), "/src/main/resources/", fileName);
        CustomFileUtils.stringToFile(filePath, fileContents);

        return this;
    }

    public String toString() {
        String str = teamName.toUpperCase() + ":\n";
        for (Player player : roster) {
            str += player.name + String.format("(%s)", player.teamName) + "\n";
        }
        return str;
    }

}
