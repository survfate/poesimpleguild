package survfate.poesimpleguild.utils.tablecellrenderer;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.table.DefaultTableCellRenderer;

public class TimeRenderer extends DefaultTableCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	SimpleDateFormat timeFormat;

	public TimeRenderer() {
		timeFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a zzz", Locale.ENGLISH);
	}

	@Override
	public void setValue(Object value) {
		setText(value != null ? timeFormat.format(value) : "");
	}
}