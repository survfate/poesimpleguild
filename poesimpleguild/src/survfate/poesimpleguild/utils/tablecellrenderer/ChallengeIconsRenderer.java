package survfate.poesimpleguild.utils.tablecellrenderer;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import survfate.poesimpleguild.resources.ResourcesLoader;

public class ChallengeIconsRenderer extends DefaultTableCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon[] challengeIcon = ResourcesLoader.challengeIcon;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value != null && Integer.parseInt(value.toString()) != 0) {
			setText("Completed " + value.toString() + " Challenge");
			setIcon(challengeIcon[Integer.parseInt(value.toString())]);
		} else {
			setText(null);
			setIcon(null);
		}

		// setText(...);
		// setIcon(...);
		// setToolTipText(...);
		return this;
	}
}