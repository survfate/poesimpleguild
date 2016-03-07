package survfate.poesimpleguild.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.miginfocom.swing.MigLayout;
import survfate.poesimpleguild.Account;
import survfate.poesimpleguild.HttpClient;
import survfate.poesimpleguild.resources.ResourcesLoader;
import survfate.poesimpleguild.utils.JTable2XLS;
import survfate.poesimpleguild.utils.TableColumnAdjuster;
import survfate.poesimpleguild.utils.XLSFileFilter;
import survfate.poesimpleguild.utils.tablecellrenderer.ChallengeIconsRenderer;
import survfate.poesimpleguild.utils.tablecellrenderer.CheckBoxRenderer;
import survfate.poesimpleguild.utils.tablecellrenderer.DateRenderer;
import survfate.poesimpleguild.utils.tablecellrenderer.NumberRenderer;
import survfate.poesimpleguild.utils.tablecellrenderer.SupporterTagsRenderer;
import survfate.poesimpleguild.utils.tablecellrenderer.TimeRenderer;
import survfate.poesimpleguild.utils.tablecellrenderer.URLRenderer;

public class MainPanel extends JPanel implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static String VERSION = "v0.1.4";
	private JTable table;
	private TableColumnAdjuster adjuster;
	private DefaultTableModel tableModel;
	private JPanel panel;
	private JPanel controlPanel;

	private JButton buttonGet, buttonXLS;
	private JCheckBox checkBoxPoeTrade, checkBoxAutoFit;
	private JTextField textField;

	private String currentGuildID;

	public static JTextArea logOutput;

	public MainPanel() {
		setLayout(new BorderLayout());
		tableModel = new DefaultTableModel(new String[] { "Account Name", "Member Type (Status)", "Challenge(s) Done",
				"Total Forum Posts", "Joined Date", "Last Visted Date", "Last Ladder Online", "Supporter Tag(s)",
				"Poe.Trade Online", "Profile URL" }, 0) {
			/**
					 *
					 */
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 2:
				case 3:
				case 7:
					return Integer.class;
				case 4:
				case 5:
				case 6:
					return Date.class;
				case 8:
					return Boolean.class;
				case 9:
					return URL.class;
				default:
					return Object.class;
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
				// This causes all cells to be not editable
			}
		};

		table = new JTable(tableModel) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean getScrollableTracksViewportWidth() {
				return getPreferredSize().width < getParent().getWidth();
			}
		};

		table.setPreferredScrollableViewportSize(new Dimension(700, 300));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(25);

		adjuster = new TableColumnAdjuster(table);
		adjuster.adjustColumns();

		// Set Renderers to Columns
		table.getColumnModel().getColumn(2).setCellRenderer(new ChallengeIconsRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(new NumberRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(new DateRenderer());
		table.getColumnModel().getColumn(5).setCellRenderer(new DateRenderer());
		table.getColumnModel().getColumn(6).setCellRenderer(new TimeRenderer());
		table.getColumnModel().getColumn(7).setCellRenderer(new SupporterTagsRenderer());

		URLRenderer urlRenderer = new URLRenderer();
		table.getColumnModel().getColumn(9).setCellRenderer(urlRenderer);
		table.addMouseListener(urlRenderer);
		table.addMouseMotionListener(urlRenderer);

		add(new JScrollPane(table), BorderLayout.CENTER);

		panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		add(panel, BorderLayout.NORTH);

		controlPanel = new JPanel();
		controlPanel.setLayout(new MigLayout("center", "[center][center]", ""));
		panel.add(controlPanel, BorderLayout.CENTER);
		controlPanel.add(new JLabel("Guild ID:"), "split 4");
		textField = new JTextField(6);
		controlPanel.add(textField);
		buttonGet = new JButton("Get Guild Members Data");
		buttonGet.addActionListener(this);
		controlPanel.add(buttonGet, "wrap");

		controlPanel.add(new JLabel("Poe.Trade Online Check? (Slow!)"), "split 4");
		checkBoxPoeTrade = new JCheckBox();
		checkBoxPoeTrade.addActionListener(this);
		controlPanel.add(checkBoxPoeTrade);

		controlPanel.add(new JLabel("Fit Columns On Complete"));
		checkBoxAutoFit = new JCheckBox();
		checkBoxAutoFit.addActionListener(this);
		controlPanel.add(checkBoxAutoFit);

		buttonXLS = new JButton("Save Table As .XLS");
		buttonXLS.setEnabled(false);
		buttonXLS.addActionListener(this);
		controlPanel.add(buttonXLS, "dock east");

		logOutput = new JTextArea(5, 40);
		DefaultCaret ouputCaret = (DefaultCaret) logOutput.getCaret();
		ouputCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		logOutput.setEditable(false);
		add(new JScrollPane(logOutput), BorderLayout.SOUTH);
	}

	public static void createAndShowGUI() {
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			// UIManager.setLookAndFeel(org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.getBeautyEyeLNFCrossPlatform());

			UIManager.put("RootPane.setupButtonVisible", false);

			// UIManager.getDefaults().put("TextArea.font",
			// UIManager.getFont("TextField.font"));

			Font robotoFont = ResourcesLoader.robotoFont;

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(robotoFont.deriveFont(13F));

			setUIFont(new FontUIResource(robotoFont.deriveFont(13F)));

		} catch (Exception e) {
			// If not available, you can set the GUI to another look
			// and feel.
		}
		// UIManager.put("swing.boldMetal", Boolean.FALSE);
		JFrame frame = new JFrame("PoE Simple Guild " + VERSION);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MainPanel newContentPane = new MainPanel();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.pack();
		frame.setVisible(true);
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		@SuppressWarnings("rawtypes")
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == buttonGet) {
			if (!textField.getText().equals("")) {
				// Clear all texts and print out Guild details
				logOutput.setText("");
				LoadGuildWorker loadGuild = new LoadGuildWorker();
				loadGuild.execute();
			} else
				JOptionPane.showMessageDialog(null, "Please enter a valid Guild ID.", "Error", 0);
		} else if (event.getSource() == buttonXLS) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File("C:\\" + currentGuildID + ".xls"));
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new XLSFileFilter());
			int option = fileChooser.showSaveDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				JTable2XLS.saveXLS(table, fileChooser.getSelectedFile());
			}
		}
	}

	// SwingWorker for geting Guild information
	public class LoadGuildWorker extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() {
			try {
				long tStart = System.currentTimeMillis();

				currentGuildID = textField.getText();
				buttonGet.setEnabled(false);
				buttonXLS.setEnabled(false);
				textField.setEnabled(false);
				checkBoxPoeTrade.setEnabled(false);
				checkBoxAutoFit.setEnabled(false);

				if (checkBoxPoeTrade.isSelected() == false) {
					table.getColumnModel().getColumn(8).setCellRenderer(new CheckBoxRenderer(false));
				} else
					table.getColumnModel().getColumn(8).setCellRenderer(new CheckBoxRenderer(true));

				tableModel.getDataVector().removeAllElements();
				tableModel.fireTableDataChanged();

				Document jsoupDoc = Jsoup.parse(
						HttpClient.runGuildURL("http://www.pathofexile.com/guild/profile/" + textField.getText()));
				Element detailsContent = jsoupDoc.getElementsByClass("details-content").first();
				int i = 1;
				int guildSize = detailsContent.getElementsByClass("member").size();

				logOutput.append("Guild Name: " + detailsContent.getElementsByClass("name").first().text() + "\t");
				try {
					logOutput.append(
							"Guild Tag: " + detailsContent.getElementsByClass("guild-tag").first().text() + "\n");
					logOutput.append(
							"Guild Status: " + detailsContent.getElementsByClass("guild-status").first().text() + "\n");
				} catch (Exception e) {
					// Some Guild don't have these two
				}
				logOutput.append("Created: " + detailsContent.child(1).childNode(3).toString() + "\n");
				logOutput.append("Total Members: " + guildSize + "\n");

				// Run through each Guildmembers
				for (Element member : detailsContent.getElementsByClass("member")) {
					String accountName = member.child(0).text().trim();
					String title = (member.child(0).child(0).childNodeSize() == 2)
							? member.child(0).child(0).child(0).attr("title") : null;
					int challengeNums = (title == null) ? 0
							: Integer.parseInt(new StringTokenizer(title).nextToken("Completed").trim());

					// Instantiate account object
					Account account = new Account(accountName);
					int forumPosts = account.getForumPosts();
					Date joinedDate = account.getJoinedDate();
					Date lastVisitedDate = account.getLastVisitedDate();
					Date lastLadderOnline = (account.getLastLadderOnline().equals(Date.from(Instant.ofEpochSecond(0))))
							? null : account.getLastLadderOnline();
					StringTokenizer tokenizer = new StringTokenizer(account.getSupporterTagKeys());
					String tagKeys = (account.getSupporterTagKeys() != "")
							? String.valueOf(tokenizer.countTokens()) + " " + account.getSupporterTagKeys() : null;
					String memberType = member.child(1).text().trim() + " " + account.getStatus();
					boolean poeTradeOnline = (checkBoxPoeTrade.isSelected() == true) ? account.getPoeTradeOnlineStatus()
							: false;
					URL profileURL = account.getURL();

					tableModel.addRow(new Object[] { accountName, memberType, challengeNums, forumPosts, joinedDate,
							lastVisitedDate, lastLadderOnline, tagKeys, poeTradeOnline, profileURL });
					logOutput.append("Status: Loaded " + i++ + "/" + guildSize + " members\n");

				}
				if (checkBoxAutoFit.isSelected() == true)
					adjuster.adjustColumns();
				long tEnd = System.currentTimeMillis();
				long tDelta = tEnd - tStart;
				double elapsedSeconds = tDelta / 1000.0;
				logOutput.append(
						"Process completed, elapsed time: " + elapsedSeconds + " second(s), average time per account: "
								+ (new DecimalFormat("##.000")).format(elapsedSeconds / guildSize) + " second(s)");

			} catch (survfate.poesimpleguild.utils.InvalidGuildIDException | java.net.UnknownHostException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Invalid Guild ID! Please try again.", "Error", 0);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void done() {
			tableModel.fireTableDataChanged();
			buttonGet.setEnabled(true);
			buttonXLS.setEnabled(true);
			textField.setEnabled(true);
			checkBoxPoeTrade.setEnabled(true);
			checkBoxAutoFit.setEnabled(true);
		}
	}
}
