package survfate.poesimpleguild.utils.tablecellrenderer;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.table.DefaultTableCellRenderer;

public class DateRenderer extends DefaultTableCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	SimpleDateFormat dateFormat;

	public DateRenderer() {
		dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
	}

	@Override
	public void setValue(Object value) {
		setText(value != null ? dateFormat.format(value) : "");
	}
}
