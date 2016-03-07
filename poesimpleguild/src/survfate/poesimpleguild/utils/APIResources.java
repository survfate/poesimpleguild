package survfate.poesimpleguild.utils;

import java.io.IOException;
import java.util.ArrayList;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import survfate.poesimpleguild.HttpClient;

public class APIResources {

	public static String[] getActiveLeaguesAPIShortName() throws IOException {
		ArrayList<String> activeLeagues = new ArrayList<String>();

		JsonObject jsonObject = Json.parse(HttpClient.runURL("http://api.exiletools.com/ladder?activeleagues=1"))
				.asObject();
		for (String leagueName : jsonObject.names()) {
			activeLeagues.add(jsonObject.get(leagueName).asString());
		}
		return activeLeagues.toArray(new String[activeLeagues.size()]);
	}

	public static String[] getActiveLeaguesName() throws IOException {
		ArrayList<String> activeLeagues = new ArrayList<String>();

		JsonArray jsonArray = Json.parse(HttpClient.runURL("http://api.pathofexile.com/leagues?type=main")).asArray();
		for (JsonValue leagues : jsonArray) {
			activeLeagues.add(leagues.asObject().get("id").asString());
		}
		return activeLeagues.toArray(new String[activeLeagues.size()]);
	}

}
