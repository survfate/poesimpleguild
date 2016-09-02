package survfate.poesimpleguild.utils.tablecellrenderer;

import java.awt.Component;
import java.net.URL;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableCellRenderer;

import survfate.poesimpleguild.resources.ResourcesLoader;

public class SupporterTagsRenderer extends DefaultTableCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static HashMap<String, URL> supporterTagsURL = ResourcesLoader.supporterTagsURL;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		// Center the number
		setHorizontalAlignment(SwingConstants.CENTER);

		// ToolTip configure
		ToolTipManager.sharedInstance().registerComponent(this);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

		if (value != null) {
			String srcString = "";
			StringTokenizer tokenizer = new StringTokenizer(value.toString());
			setValue(tokenizer.countTokens() - 1);
			tokenizer.nextToken();

			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				srcString += "<img border=1 src=" + supporterTagsURL.get(token) + ">";
				if (tokenizer.hasMoreTokens())
					srcString += "<br>";
			}

			setToolTipText("<html><div style='text-align:center;'>" + srcString);
		} else {
			// setToolTipText("This Account does not have any SupporterTags
			setToolTipText(null);
		}

		// setText(...);
		// setIcon(...);
		// setToolTipText(...);
		return this;
	}

}