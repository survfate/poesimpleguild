package survfate.poesimpleguild;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PoeStatistics {
	private String charName;
	private Date accountLastTrackedDate;

	public PoeStatistics(String profile) throws ParseException, IOException {
		Document poestatDoc = Jsoup.parse(HttpClient.runURL("http://poestatistics.com/users/" + profile + "/"));
		Element contentgraczClass = poestatDoc.getElementsByAttributeValueContaining("class", "content gracz").first();

		try {
			if (contentgraczClass.child(2).text()
					.equals("This user do not have any character on top15000 in any ladder.")) {
				Element firstRow = contentgraczClass.getElementById("udzialWEventach").child(1).child(0);

				this.charName = firstRow.child(0).child(0).text();
				String charURL = "http://poestatistics.com" + firstRow.child(0).child(0).attr("href");
				String eventURL = "http://poestatistics.com" + firstRow.child(1).child(0).attr("href");

				// Get event end Date
				Document jsoupEventDoc = Jsoup.parse(HttpClient.runURL(eventURL));
				Element eventInfo = jsoupEventDoc.getElementsByClass("eventInfo").first();

				SimpleDateFormat poestatisticsLeagueDateFormat = new SimpleDateFormat("d MMMM yyyy 'at' HH:mm",
						Locale.ENGLISH);
				Date eventEndDate = poestatisticsLeagueDateFormat
						.parse(eventInfo.child(1).childNode(1).toString().replaceAll("&nbsp;", " ").trim());

				Document jsoupCharDoc = Jsoup.parse(HttpClient.runURL(charURL));
				if (jsoupCharDoc.getElementsByClass("infoPostaci").first().nextElementSibling().nodeName()
						.equals("table")) {
					// Recently tracked char has playtime data
					Element table = jsoupCharDoc.getElementsByClass("infoPostaci").first().nextElementSibling();
					Element lastRow = table.getElementsByClass("podsumowanie").first().previousElementSibling();
					String lastRowTime = lastRow.child(0).text().trim();

					Pattern datePattern = Pattern.compile("([0-9]{1,2})?d");
					Pattern hourPattern = Pattern.compile("([0-9]{1,2})?h");
					Matcher dateMatcher = datePattern.matcher(lastRowTime);
					Matcher hourMatcher = hourPattern.matcher(lastRowTime);

					Calendar c = Calendar.getInstance();
					c.setTime(eventEndDate);
					// Add the playtime data to the league end Date
					if (dateMatcher.find())
						c.add(Calendar.DATE, Integer.parseInt(dateMatcher.group(1)));
					if (hourMatcher.find())
						c.add(Calendar.HOUR, Integer.parseInt(hourMatcher.group(1)));

					this.accountLastTrackedDate = c.getTime();
				} else {
					// If recently tracked char don't have playtime data, get
					// the event end Date instead
					this.accountLastTrackedDate = eventEndDate;
				}
			} else {
				// The user have at least one char in the top 15000 in a ladder
				this.charName = contentgraczClass.child(2).child(0).child(1).child(0).text();
				String charURL = "http://poestatistics.com"
						+ contentgraczClass.child(2).child(0).child(1).child(0).child(0).attr("href");

				Document jsoupCharDoc = Jsoup.parse(HttpClient.runURL(charURL));
				Element table = jsoupCharDoc.getElementsByClass("clear").first().nextElementSibling();
				SimpleDateFormat poestatisticsCharDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				this.accountLastTrackedDate = poestatisticsCharDateFormat
						.parse(table.child(0).child(1).child(0).text());
			}
		} catch (NullPointerException | IOException e) {
			// User don't exist or can't be accessed
			this.accountLastTrackedDate = new Date(0);
		}
	}

	public String getCharName() {
		return this.charName;
	}

	public Date getAccountLastTrackedDate() {
		return this.accountLastTrackedDate;
	}
}
