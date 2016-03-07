package survfate.poesimpleguild;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import survfate.poesimpleguild.utils.APIResources;

public class Account {
	/* Parameters */
	private String profile;
	private Element details;
	private Date joined;
	private Date lastVisited;
	private int forumPosts;
	private Date lastLadderOnline;
	private boolean poeTradeOnline;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);;

	public Account(String profile) throws IOException, ParseException, URISyntaxException {
		// Encode the profile parameter
		this.profile = java.net.URLEncoder.encode(profile, "UTF-8");

		// Encode URL to ASCII String to avoid special characters error
		String urlStr = "http://www.pathofexile.com/account/view-profile/" + profile;
		URL url = new URL(urlStr);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
				url.getQuery(), url.getRef());

		// Parsing account details using Jsoup and OkHttp
		Document jsoupDoc = Jsoup.parse(HttpClient.runURL(uri.toASCIIString()));
		details = jsoupDoc.getElementsByClass("details").first();

		this.joined = dateFormat.parse(details.child(3).childNode(3).toString().trim());
		this.lastVisited = dateFormat.parse(details.child(4).childNode(3).toString().trim());
		this.forumPosts = Integer.parseInt(details.child(5).childNode(3).toString().trim());
	}

	/* Methods */
	// Return an Profile URL of an account
	public URL getURL() {
		try {
			return new URL("http://www.pathofexile.com/account/view-profile/" + this.profile);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Return a joined date of an account
	public Date getJoinedDate() {
		return this.joined;
	}

	// Return the last forum visited date of an account
	public Date getLastVisitedDate() {
		return this.lastVisited;
	}

	// Return the total forum posts of an account
	public int getForumPosts() {
		return this.forumPosts;
	}

	// Return the last ladder online date of an account from all the characters,
	// using api.exiletools.com
	public Date getLastLadderOnline() throws IOException {
		lastLadderOnline = new Date(0);

		try {
			JsonObject jsonObject = Json
					.parse(HttpClient
							.runURL("http://api.exiletools.com/ladder?league=all&short=1&account=" + this.profile))
					.asObject();

			// Find the lastest date
			for (String charName : jsonObject.names()) {
				int epochSecond = 0;
				JsonValue jsonValue = jsonObject.get(charName);
				if (jsonValue.asObject().get("lastOnline") != null) {
					epochSecond = Integer.parseInt(jsonValue.asObject().get("lastOnline").asString());
				}

				// Epoch Timestamp to Human
				Date lastOnlineHuman = Date.from(Instant.ofEpochSecond(epochSecond));
				if (lastOnlineHuman.after(lastLadderOnline)) {
					this.lastLadderOnline = lastOnlineHuman;
				}
			}
		} catch (com.eclipsesource.json.ParseException e) {
			// Account without Ladder data
			return this.lastLadderOnline;
		}
		return this.lastLadderOnline;
	}

	// Return the Poe.Trade Online status of an account, using a brute-force
	// query trick (slow speed)
	public boolean getPoeTradeOnlineStatus() throws IOException {
		this.poeTradeOnline = false;
		String[] leagues = APIResources.getActiveLeaguesName();

		for (String league : leagues) {
			RequestBody formBody = new FormEncodingBuilder().add("league", league).add("seller", this.profile)
					.add("online", "x").build();
			Document jsoupDoc = Jsoup.parse(HttpClient.runURLWithRequestBody("http://poe.trade/search", formBody));
			if (!jsoupDoc.getElementsByClass("search-results-block").text().equals("")) {
				this.poeTradeOnline = true;
				break;
			}
		}
		return this.poeTradeOnline;
	}

	// Return a String contain Supporter Tags of an account
	public String getSupporterTagKeys() {
		String tagKeys = "";

		for (Element tag : details.getElementsByAttributeValueContaining("class", "roleLabel")) {
			try {
				String imgSrc = tag.child(0).attr("src");
				String imgFileName = imgSrc.substring(imgSrc.lastIndexOf('/') + 1, imgSrc.length());
				String imgKey = imgFileName.substring(0, imgFileName.lastIndexOf('.')).toLowerCase().replace('-', '_');
				tagKeys += imgKey + " ";
			} catch (java.lang.IndexOutOfBoundsException e) {
				// Skip on IndexOutOfBoundsException occur
				// e.printStackTrace();
			}
		}
		return tagKeys.trim();
	}

	//
	public String getStatus() {
		StringBuilder status = new StringBuilder();
		String delim = "";
		for (Element tag : details.getElementsByAttributeValueContaining("class", "roleLabel")) {
			if (tag.attr("class").equals("roleLabel valuedPosterText")) {
				status.append(delim).append("Valued Poster");
				delim = " | ";
				continue;
			}
			if (tag.attr("class").equals("roleLabel in-alpha")) {
				status.append(delim).append("Alpha Member");
				delim = " | ";
				continue;
			}
			if (tag.attr("class").equals("roleLabel onProbation")) {
				status.append(delim).append("On Probation");
				delim = " | ";
				continue;
			}
			if (tag.attr("class").equals("roleLabel banned")) {
				status.append(delim).append("Banned");
				delim = " | ";
				continue;
			}
		}
		if (!status.toString().equals(""))
			return "(" + status.toString().trim() + ")";
		else
			return "";
	}
}
