package common;

import java.util.Arrays;
import java.util.List;

public interface Constants {
    String BASE_URL = "https://statsapi.web.nhl.com";
    String TEAMS_LIST_URL = BASE_URL + "/api/v1/teams";
    String PEOPLE_URL_PREFIX = BASE_URL + "/api/v1/people";
    String TEAM_ROSTER_SUFFIX = "/roster";
    String SINGLE_SEASON_STATS_URL = PEOPLE_URL_PREFIX + "/%s/stats?stats=statsSingleSeason&season=%s";
    String SEASONS_LIST_URL = BASE_URL + "/api/v1/seasons";

    List<String> forwardPositions = Arrays.asList("LW", "C", "RW");
    List<String> skaterPositions = Arrays.asList("LW", "C", "RW", "D");

    String resourcesDirectory = System.getProperty("user.dir") + "/src/main/resources/";
    String testResourcesDirectory = System.getProperty("user.dir") + "/src/test/resources/";

}
