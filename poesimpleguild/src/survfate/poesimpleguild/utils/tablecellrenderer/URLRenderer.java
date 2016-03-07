package survfate.poesimpleguild.utils.tablecellrenderer;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class URLRenderer extends DefaultTableCellRenderer implements MouseListener, MouseMotionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// private static Rectangle lrect = new Rectangle();
	// private static Rectangle irect = new Rectangle();
	// private static Rectangle trect = new Rectangle();
	private int row = -1;
	private int col = -1;
	private boolean isRollover;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

		String str = Objects.toString(value, "");

		if (!table.isEditing() && this.row == row && this.col == column && this.isRollover) {
			setText("<html><u>" + str);
		} else if (hasFocus) {
			setText("<html>" + str);
		} else {
			setText(str);
		}
		return this;
	}

	private static boolean isURLColumn(JTable table, int column) {
		return column >= 0 && table.getColumnClass(column).equals(URL.class);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		JTable table = (JTable) e.getComponent();
		Point pt = e.getPoint();
		int prevRow = row;
		int prevCol = col;
		boolean prevRollover = isRollover;
		row = table.rowAtPoint(pt);
		col = table.columnAtPoint(pt);
		isRollover = isURLColumn(table, col); // && pointInsidePrefSize(table,
												// pt);
		if (row == prevRow && col == prevCol && isRollover == prevRollover || !isRollover && !prevRollover) {
			return;
		}

		// >>>> HyperlinkCellRenderer.java
		// @see
		// http://java.net/projects/swingset3/sources/svn/content/trunk/SwingSet3/src/com/sun/swingset3/demos/table/HyperlinkCellRenderer.java
		Rectangle repaintRect;
		if (isRollover) {
			Rectangle r = table.getCellRect(row, col, false);
			repaintRect = prevRollover ? r.union(table.getCellRect(prevRow, prevCol, false)) : r;
		} else { // if (prevRollover) {
			repaintRect = table.getCellRect(prevRow, prevCol, false);
		}
		table.repaint(repaintRect);
		// <<<<
		// table.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		JTable table = (JTable) e.getComponent();
		if (isURLColumn(table, col)) {
			table.repaint(table.getCellRect(row, col, false));
			row = -1;
			col = -1;
			isRollover = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JTable table = (JTable) e.getComponent();
		Point pt = e.getPoint();
		int ccol = table.columnAtPoint(pt);
		if (isURLColumn(table, ccol)) { // && pointInsidePrefSize(table, pt)) {
			int crow = table.rowAtPoint(pt);
			URL url = (URL) table.getValueAt(crow, ccol);
			// System.out.println(url);
			try {
				// Web Start
				// BasicService bs = (BasicService)
				// ServiceManager.lookup("javax.jnlp.BasicService");
				// bs.showDocument(url);
				if (Desktop.isDesktopSupported()) { // JDK 1.6.0
					Desktop.getDesktop().browse(url.toURI());
				}
			} catch (URISyntaxException | IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		/* not needed */ }

	@Override
	public void mouseEntered(MouseEvent e) {
		/* not needed */ }

	@Override
	public void mousePressed(MouseEvent e) {
		/* not needed */ }

	@Override
	public void mouseReleased(MouseEvent e) {
		/* not needed */ }
}
