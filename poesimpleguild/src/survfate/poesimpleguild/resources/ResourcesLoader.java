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

public class ResourcesLoader {
	public static Font robotoFont;
	public static ImageIcon[] challengeIcon = new ImageIcon[41];
	public static HashMap<String, URL> supporterTagsURL = new HashMap<String, URL>();

	public void loadFont() throws FontFormatException, IOException {
		// Load Roboto Font
		URL robotoFontURL = new URL(
				"https://raw.githubusercontent.com/google/fonts/master/apache/roboto/Roboto-Regular.ttf");
		robotoFont = Font.createFont(Font.TRUETYPE_FONT, robotoFontURL.openStream());
	}

	public void loadChallengeIcons() throws MalformedURLException {
		// Load Challenge Icons
		URL[] challengeIconURL = new URL[41];
		for (int i = 1; i <= 40; i++) {
			challengeIconURL[i] = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/icons/achievements/" + i + ".png?v=8");
			challengeIcon[i] = new ImageIcon(challengeIconURL[i]);
		}
	}

	public void loadSupporterTagImages() throws MalformedURLException {
		// Load SupporterTag Images
		/* Close Beta */
		URL default_supporter = new URL(
				"https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/default_supporter.png?v=2");
		URL bronze_supporter = new URL(
				"https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/bronze_supporter.png?v=2");
		URL silver_supporter = new URL(
				"https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/silver_supporter.png?v=2");
		URL gold_supporter = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/gold_supporter.png?v=2");
		URL diamond_supporter = new URL(
				"https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/diamond_supporter.png?v=2");

		/* Open Beta */
		URL open_beta = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/open-beta/open-beta.png?v=2");
		URL regal = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/open-beta/regal.png?v=2");
		URL divine = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/open-beta/divine.png?v=2");
		URL exalted = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/open-beta/exalted.png?v=2");
		URL eternal = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/open-beta/eternal.png?v=2");
		URL ruler_of_wraeclast = new URL(
				"https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/open-beta/ruler-of-wraeclast.png?v=2");

		/* Sacrifice of the Vaal */
		URL survivor = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/release/survivor.png?v=4");
		URL warrior = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/release/warrior.png?v=4");
		URL champion = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/release/champion.png?v=4");
		URL conqueror = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/release/conqueror.png?v=4");

		/* Forsaken Masters */
		URL apprentice = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/release2/Apprentice.png");
		URL journeyman = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/release2/Journeyman.png");
		URL master = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/release2/Master.png");
		URL grandmaster = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/release2/Grandmaster.png");

		/* The Awakening */
		URL awakening = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/awakening/Awakening.png");
		URL axiom = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/awakening/Axiom.png");
		URL vaal = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/awakening/Vaal.png");
		URL lunaris = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/awakening/Lunaris.png");
		URL highgate = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/awakening/Highgate.png");

		/* Ascendancy */
		URL aspirant = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/ascendancy/Aspirant.png");
		URL challenger = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/ascendancy/Challenger.png");
		URL sovereign = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/ascendancy/Sovereign.png");
		URL ascendant = new URL("https://p7p4m6s5.ssl.hwcdn.net/image/forum/supporter-tag/ascendancy/Ascendant.png");

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
	}

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
}