package team;

import common.Constants;
import common.CustomFileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import player.Player;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Team {
    private static String rosterURL = Constants.BASE_URL + "%s" + Constants.TEAM_ROSTER_SUFFIX;

    String teamName, city, abbreviation;
    ArrayList<Player> roster;
    int id;

    public Team(String teamName) {
        Team team = findTeamInfo(teamName);
        this.teamName = teamName;
        this.city = team.city;
        this.abbreviation = team.abbreviation;
        this.id = team.id;
        this.roster = team.roster;
    }

    public Team(int id) {
        Team team = findTeamInfo(id);
        this.id = id;
        this.teamName = team.teamName;
        this.city = team.city;
        this.abbreviation = team.abbreviation;
        this.roster = team.roster;
    }

    public Team(String teamName, String city, String abbreviation, int id, ArrayList<Player> roster) {
        this.teamName = teamName;
        this.city = city;
        this.abbreviation = abbreviation;
        this.roster = roster;
        this.id = id;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray getAllTeams() {
        return CustomFileUtils.getJSONObjectFromURL(Constants.TEAMS_LIST_URL).getJSONArray("teams");
    }

    public static ArrayList<Player> getRoster(String teamName) {
        JSONArray jsonArray = getAllTeams();
        String teamURLSuffix = "";
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject team = jsonArray.getJSONObject(i);
            if (team.getString("teamName").equals(teamName)) {
                teamURLSuffix = team.getString("link");
                break;
            }
        }

        JSONObject roster = CustomFileUtils.getJSONObjectFromURL(String.format(rosterURL, teamURLSuffix));
        JSONArray playerObjects = roster.getJSONArray("roster");
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < playerObjects.length(); i++) {
            JSONObject player = playerObjects.getJSONObject(i);

            String playerName = player.getJSONObject("person").getString("fullName");
            int playerID = player.getJSONObject("person").getInt("id");
            String playerNumber = !player.isNull("jerseyNumber") ? player.getString("jerseyNumber") : "";
            String playerPosition = player.getJSONObject("position").getString("abbreviation");

            players.add(new Player(playerName, playerID, playerPosition, playerNumber, teamName));
        }

        return players;
    }

    private static Team findTeamInfo(String teamName) {
        JSONArray teamsArrayObject = CustomFileUtils.getJSONObjectFromURL(Constants.TEAMS_LIST_URL).getJSONArray("teams");
        for (int i = 0; i < teamsArrayObject.length(); i++) {
            JSONObject team = teamsArrayObject.getJSONObject(i);
            String name = team.getString("teamName");
            if (name.equals(teamName)) {
                int id = team.getInt("id");
                String city = team.getString("locationName");
                String abbreviation = team.getString("abbreviation");
                ArrayList<Player> roster = getRoster(name);
                return new Team(teamName, city, abbreviation, id, roster);
            }
        }

        return null;
    }

    private static Team findTeamInfo(int id) {
        JSONObject teamObject = CustomFileUtils.getJSONObjectFromURL(Constants.TEAMS_LIST_URL + "/" + id).getJSONArray("teams").getJSONObject(0);
        return findTeamInfo(teamObject.getString("teamName"));
    }

    public static String getTeamName(int id) {
        JSONObject teamObject = CustomFileUtils.getJSONObjectFromURL(Constants.TEAMS_LIST_URL + "/" + id).getJSONArray("teams").getJSONObject(0);
        return teamObject.getString("teamName");
    }

}
