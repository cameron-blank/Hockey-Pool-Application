package team;

public class NameTranslator {

    static String[] abbreviations = {"ANA", "ARI", "BOS", "BUF", "CAR", "CBJ",
            "CGY", "CHI", "COL", "DAL", "DET", "EDM", "FLA", "LAK", "MIN",
            "MTL", "NJD", "NSH", "NYI", "NYR", "OTT", "PHI", "PIT", "SEA",
            "SJS", "STL", "TBL", "TOR", "VAN", "VGK", "WPG", "WSH"};
    static String[] teamNames = {"Ducks", "Coyotes", "Bruins", "Sabres", "Hurricanes",
            "Blue Jackets", "Flames", "Blackhawks", "Avalanche", "Stars",
            "Red Wings", "Oilers", "Panthers", "Kings", "Wild", "Canadiens",
            "Devils", "Predators", "Islanders", "Rangers", "Senators", "Flyers",
            "Penguins", "Kraken", "Sharks", "Blues", "Lightning", "Maple Leafs",
            "Canucks", "Golden Knights", "Jets", "Capitals"};
    static String[] cities = {"Anaheim", "Arizona", "Boston", "Buffalo", "Carolina",
            "Columbus", "Calgary", "Chicago", "Colorado", "Dallas", "Detroit",
            "Edmonton", "Florida", "Los Angeles", "Minnesota", "Montreal",
            "New Jersey", "Nashville", "New York", "New York", "Ottawa",
            "Philadelphia", "Pittsburgh", "Seattle", "San Jose", "St. Louis",
            "Tampa Bay", "Toronto", "Vancouver", "Vegas", "Winnipeg", "Washington"};

    private static String getXFromY(String key, String[] source, String[] destination) {
        for (int i = 0; i < source.length; i++) {
            if (source[i].equals(key))
                return destination[i];
        }
        return null;
    }

    public static String getAbbreviationFromTeamName(String teamName) {
        return getXFromY(teamName, teamNames, abbreviations);
    }

    public static String getTeamNameFromAbbreviation(String abbreviation) {
        return getXFromY(abbreviation, abbreviations, teamNames);
    }

    public static String getTeamNameFromFullName(String fullName) {
        for (int i = 0; i < teamNames.length; i++) {
            if (fullName.equals(cities[i] + teamNames[i]))
                return teamNames[i];
        }
        return null;
    }
}
