package survfate.poesimpleguild;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.SwingUtilities;

import survfate.poesimpleguild.resources.ResourcesLoader;
import survfate.poesimpleguild.resources.ResourcesLoader.StatusLoadWorker;
import survfate.poesimpleguild.ui.MainPanel;

public class MainClass {
	public static String VERSION = "v0.1.5.1";

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				HttpClient.createHttpClient();
				ResourcesLoader.loadResources();
				MainPanel.createAndShowGUI();

				// Load Saved Guild ID from .txt
				File savedguildidFile = new File("savedguildid.txt");
				if (savedguildidFile.exists()) {
					try {
						List<String> lines = Files.readAllLines(Paths.get(savedguildidFile.getAbsolutePath()),
								Charset.forName("UTF-8"));
						MainPanel.textFieldGuildID.setText(lines.get(0));
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}
				// Load Status
				ResourcesLoader.StatusLoadWorker loadStatus = new StatusLoadWorker();
				loadStatus.execute();
			}
		});
	}
}
