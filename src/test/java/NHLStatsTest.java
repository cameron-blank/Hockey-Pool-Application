import common.Constants;
import common.Pair;
import player.Player;
import pool.PoolLeague;
import pool.PoolTeam;
import pool.Rules;

import java.util.ArrayList;

public class NHLStatsTest {

    public static void main (String[] args) {
        PoolLeague poolLeague = new PoolLeague(Constants.testResourcesDirectory + "poolLeague.txt");
        String str = getStandingsString(poolLeague);

        System.out.print(str);
    }

    private static String getStandingsString(PoolLeague poolLeague) {
        ArrayList<Pair<Pair<PoolTeam, Double>, ArrayList<Pair<Player, Double>>>> standings = poolLeague.getExpandedStandings();
        String str = "";
        int ranking = 1;

        for(Pair<Pair<PoolTeam, Double>, ArrayList<Pair<Player, Double>>> pair : standings) {
            str += "%s. %s: %s\n".formatted(ranking, pair.k.k.teamName.toUpperCase(), pair.k.v);

            for(Pair<Player, Double> playerStats : pair.v) {
                String position = Constants.forwardPositions.contains(playerStats.k.position) ? "F" : playerStats.k.position;
                str += "%-22s (%s): %s\n".formatted(playerStats.k.name, position, playerStats.v);
            }

            str += "\n";
            ranking++;
        }

        return str;
    }


}
