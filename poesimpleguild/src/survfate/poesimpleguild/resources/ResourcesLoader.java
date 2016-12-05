package survfate.poesimpleguild.resources;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import survfate.poesimpleguild.HttpClient;
import survfate.poesimpleguild.MainClass;
import survfate.poesimpleguild.ui.MainPanel;

public class ResourcesLoader {
	public static Font robotoFont;
	public static ImageIcon[] challengeIcon = new ImageIcon[46];
	public static HashMap<String, URL> supporterTagsURL = new HashMap<String, URL>();

	public void loadFont() throws FontFormatException, IOException {
		// Load Roboto Font
		robotoFont = Font.createFont(Font.TRUETYPE_FONT,
				ResourcesLoader.class.getResourceAsStream("Roboto-Regular.ttf"));
	}

	public void loadChallengeIcons() throws MalformedURLException {
		// Load Challenge Icons
		URL[] challengeIconURL = new URL[41];
		for (int i = 1; i <= 40; i++) {
			challengeIconURL[i] = new URL("http://web.poecdn.com/image/icons/achievements/" + i + ".png?v=11");
			challengeIcon[i] = new ImageIcon(challengeIconURL[i]);
		}
	}

	public void loadSupporterTagImages() throws MalformedURLException {
		// Load SupporterTag Images
		/* Close Beta */
		URL default_supporter = new URL("http://web.poecdn.com/image/forum/supporter-tag/default_supporter.png?v=2");
		URL bronze_supporter = new URL("http://web.poecdn.com/image/forum/supporter-tag/bronze_supporter.png?v=2");
		URL silver_supporter = new URL("http://web.poecdn.com/image/forum/supporter-tag/silver_supporter.png?v=2");
		URL gold_supporter = new URL("http://web.poecdn.com/image/forum/supporter-tag/gold_supporter.png?v=2");
		URL diamond_supporter = new URL("http://web.poecdn.com/image/forum/supporter-tag/diamond_supporter.png?v=2");

		/* Open Beta */
		URL open_beta = new URL("http://web.poecdn.com/image/forum/supporter-tag/open-beta/open-beta.png?v=2");
		URL regal = new URL("http://web.poecdn.com/image/forum/supporter-tag/open-beta/regal.png?v=2");
		URL divine = new URL("http://web.poecdn.com/image/forum/supporter-tag/open-beta/divine.png?v=2");
		URL exalted = new URL("http://web.poecdn.com/image/forum/supporter-tag/open-beta/exalted.png?v=2");
		URL eternal = new URL("http://web.poecdn.com/image/forum/supporter-tag/open-beta/eternal.png?v=2");
		URL ruler_of_wraeclast = new URL(
				"http://web.poecdn.com/image/forum/supporter-tag/open-beta/ruler-of-wraeclast.png?v=2");

		/* Sacrifice of the Vaal */
		URL survivor = new URL("http://web.poecdn.com/image/forum/supporter-tag/release/survivor.png?v=4");
		URL warrior = new URL("http://web.poecdn.com/image/forum/supporter-tag/release/warrior.png?v=4");
		URL champion = new URL("http://web.poecdn.com/image/forum/supporter-tag/release/champion.png?v=4");
		URL conqueror = new URL("http://web.poecdn.com/image/forum/supporter-tag/release/conqueror.png?v=4");

		/* Forsaken Masters */
		URL apprentice = new URL("http://web.poecdn.com/image/forum/supporter-tag/release2/Apprentice.png");
		URL journeyman = new URL("http://web.poecdn.com/image/forum/supporter-tag/release2/Journeyman.png");
		URL master = new URL("http://web.poecdn.com/image/forum/supporter-tag/release2/Master.png");
		URL grandmaster = new URL("http://web.poecdn.com/image/forum/supporter-tag/release2/Grandmaster.png");

		/* The Awakening */
		URL awakening = new URL("http://web.poecdn.com/image/forum/supporter-tag/awakening/Awakening.png");
		URL axiom = new URL("http://web.poecdn.com/image/forum/supporter-tag/awakening/Axiom.png");
		URL vaal = new URL("http://web.poecdn.com/image/forum/supporter-tag/awakening/Vaal.png");
		URL lunaris = new URL("http://web.poecdn.com/image/forum/supporter-tag/awakening/Lunaris.png");
		URL highgate = new URL("http://web.poecdn.com/image/forum/supporter-tag/awakening/Highgate.png");

		/* Ascendancy */
		URL aspirant = new URL("http://web.poecdn.com/image/forum/supporter-tag/ascendancy/Aspirant.png");
		URL challenger = new URL("http://web.poecdn.com/image/forum/supporter-tag/ascendancy/Challenger.png");
		URL sovereign = new URL("http://web.poecdn.com/image/forum/supporter-tag/ascendancy/Sovereign.png");
		URL ascendant = new URL("http://web.poecdn.com/image/forum/supporter-tag/ascendancy/Ascendant.png");

		/* Prophecy */
		URL prophecy = new URL("http://web.poecdn.com/image/forum/supporter-tag/prophecy/Prophecy.png");

		/* Atlas of Worlds */
		URL minotaur_supporter = new URL(
				"http://web.poecdn.com/image/forum/supporter-tag/atlas/minotaur_supporter.png?v=2");
		URL hydra_supporter = new URL("http://web.poecdn.com/image/forum/supporter-tag/atlas/hydra_supporter.png?v=2");
		URL chimera_supporter = new URL(
				"http://web.poecdn.com/image/forum/supporter-tag/atlas/chimera_supporter.png?v=2");
		URL phoenix_supporter = new URL(
				"http://web.poecdn.com/image/forum/supporter-tag/atlas/phoenix_supporter.png?v=2");

		/* Breach */
		URL breachspawn = new URL("https://web.poecdn.com/image/forum/supporter-tag/breach/Breachspawn.png");
		URL breachlord = new URL("https://web.poecdn.com/image/forum/supporter-tag/breach/Breachlord.png");

		/* Put them in */
		supporterTagsURL.put("default_supporter", default_supporter);
		supporterTagsURL.put("bronze_supporter", bronze_supporter);
		supporterTagsURL.put("silver_supporter", silver_supporter);
		supporterTagsURL.put("gold_supporter", gold_supporter);
		supporterTagsURL.put("diamond_supporter", diamond_supporter);

		supporterTagsURL.put("open_beta", open_beta);
		supporterTagsURL.put("regal", regal);
		supporterTagsURL.put("divine", divine);
		supporterTagsURL.put("exalted", exalted);
		supporterTagsURL.put("eternal", eternal);
		supporterTagsURL.put("ruler_of_wraeclast", ruler_of_wraeclast);

		supporterTagsURL.put("survivor", survivor);
		supporterTagsURL.put("warrior", warrior);
		supporterTagsURL.put("champion", champion);
		supporterTagsURL.put("conqueror", conqueror);

		supporterTagsURL.put("apprentice", apprentice);
		supporterTagsURL.put("journeyman", journeyman);
		supporterTagsURL.put("master", master);
		supporterTagsURL.put("grandmaster", grandmaster);

		supporterTagsURL.put("awakening", awakening);
		supporterTagsURL.put("axiom", axiom);
		supporterTagsURL.put("vaal", vaal);
		supporterTagsURL.put("lunaris", lunaris);
		supporterTagsURL.put("highgate", highgate);

		supporterTagsURL.put("aspirant", aspirant);
		supporterTagsURL.put("challenger", challenger);
		supporterTagsURL.put("sovereign", sovereign);
		supporterTagsURL.put("ascendant", ascendant);

		supporterTagsURL.put("prophecy", prophecy);

		supporterTagsURL.put("minotaur_supporter", minotaur_supporter);
		supporterTagsURL.put("hydra_supporter", hydra_supporter);
		supporterTagsURL.put("chimera_supporter", chimera_supporter);
		supporterTagsURL.put("phoenix_supporter", phoenix_supporter);

		supporterTagsURL.put("breachspawn", breachspawn);
		supporterTagsURL.put("breachlord", breachlord);
	}

	// public static List<String> supporterTagsScaled =
	// Arrays.asList("prophecy", "minotaur_supporter", "hydra_supporter",
	// "chimera_supporter", "phoenix_supporter");

	public ResourcesLoader() throws FontFormatException, IOException {
		loadFont();
		loadChallengeIcons();
		loadSupporterTagImages();
	}

	public static void loadResources() {
		Dialog dialog = new ResourcesLoaderDialog();
		SwingWorker<Void, Void> loaderWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				// Instantiate ResourcesLoader object
				new ResourcesLoader();
				return null;
			}

			@Override
			public void done() {
				dialog.setVisible(false);
				dialog.dispose();
			}
		};
		loaderWorker.execute();
		dialog.setVisible(true);
	}

	public static class StatusLoadWorker extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() throws IOException {
			// Change stuff during load
			MainPanel.menuLoadStatus.setEnabled(false);
			MainPanel.poeStatisticsStatus.setText("Loading");
			MainPanel.poeTradeStatus.setText("Loading");
			MainPanel.lastestVersion.setText("Loading");

			if (HttpClient.testURL("http://poestatistics.com")) {
				MainPanel.poeStatisticsStatus.setText("<html><b><font color='green'>Online</font></b></html>");
				MainPanel.checkBoxMenuItemPoeStatistics.setSelected(true);
				MainPanel.checkBoxMenuItemPoeStatistics.setEnabled(true);
			} else {
				MainPanel.poeStatisticsStatus.setText("<html><b><font color='red'>Offline</font></b></html>");
				MainPanel.checkBoxMenuItemPoeStatistics.setSelected(false);
				MainPanel.checkBoxMenuItemPoeStatistics.setEnabled(false);
			}

			if (HttpClient.testURL("http://poe.trade"))
				MainPanel.poeTradeStatus.setText("<html><b><font color='green'>Online</font></b></html>");
			else
				MainPanel.poeTradeStatus.setText("<html><b><font color='red'>Offline</font></b></html>");

			// Dirty code instead of using GitHub API
			if (HttpClient.testURL("https://github.com/survfate/poesimpleguild/releases")) {
				Document githubDoc = Jsoup
						.parse(HttpClient.runURL("https://github.com/survfate/poesimpleguild/releases"));
				String lastestVersion = githubDoc.getElementsByClass("release-meta").first()
						.getElementsByClass("tag-references").first().child(0).child(0).attr("href").substring(30);
				String lastestVersionHTML = (lastestVersion.equals(MainClass.VERSION))
						// if (current) VERSION matched lastestVersion
						? "<html><b><font color='green'>" + lastestVersion + "</font></b></html>"
						// else
						: "<html><b><font color='red'>" + lastestVersion + "</font></b></html>";
				MainPanel.lastestVersion.setText(lastestVersionHTML);
			}
			return null;
		}

		@Override
		protected void done() {
			MainPanel.menuLoadStatus.setEnabled(true);
		}
	}
}