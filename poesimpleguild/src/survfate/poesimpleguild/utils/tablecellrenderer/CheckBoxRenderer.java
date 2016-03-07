package survfate.poesimpleguild.utils.tablecellrenderer;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private boolean setEnabled;

	public CheckBoxRenderer(boolean setEnabled) {
		setHorizontalAlignment(SwingConstants.CENTER);
		this.setEnabled = setEnabled;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setEnabled(setEnabled);
		return this;
	}
}