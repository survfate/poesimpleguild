package survfate.poesimpleguild;

import javax.swing.SwingUtilities;

import survfate.poesimpleguild.resources.ResourcesLoader;
import survfate.poesimpleguild.ui.MainPanel;

public class MainClass {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				HttpClient.createHttpClient();
				ResourcesLoader.loadResources();
				MainPanel.createAndShowGUI();
			}
		});
	}
}
