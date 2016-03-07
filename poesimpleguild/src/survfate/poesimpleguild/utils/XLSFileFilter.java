package survfate.poesimpleguild.utils;

import java.io.File;

public class XLSFileFilter extends javax.swing.filechooser.FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isDirectory())
			return true;
		String filename = file.getName();
		return filename.toUpperCase().endsWith(".XLS");
	}

	@Override
	public String getDescription() {
		return "Excel 97-2003 Workbook (*.xls)";
	}

}
