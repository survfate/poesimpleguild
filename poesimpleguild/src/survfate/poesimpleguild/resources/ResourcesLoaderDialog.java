package survfate.poesimpleguild.resources;

import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ResourcesLoaderDialog extends JDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 4865391892323320009L;

	public ResourcesLoaderDialog() {
		super(new JFrame(), true);
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setUndecorated(true);
		setLayout(new FlowLayout(FlowLayout.CENTER));

		final JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		add(progressBar);

		setSize(getPreferredSize());

		pack();
		setLocationRelativeTo(null);
	}
}