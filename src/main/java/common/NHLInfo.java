package common;

import org.json.JSONArray;
import org.json.JSONObject;

public class NHLInfo {
    public static String getLatestSeason() {
        JSONObject jsonObject = CustomFileUtils.getJSONObjectFromURL(Constants.SEASONS_LIST_URL);
        JSONArray jsonArray = jsonObject.getJSONArray("seasons");
        return jsonArray.getJSONObject(jsonArray.length() - 1).getString("seasonId");
    }
}
