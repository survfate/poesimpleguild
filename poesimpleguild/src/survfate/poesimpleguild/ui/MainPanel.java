package survfate.poesimpleguild.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultCaret;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.miginfocom.swing.MigLayout;
import survfate.poesimpleguild.Account;
import survfate.poesimpleguild.HttpClient;
import survfate.poesimpleguild.MainClass;
import survfate.poesimpleguild.resources.ResourcesLoader;
import survfate.poesimpleguild.resources.ResourcesLoader.StatusLoadWorker;
import survfate.poesimpleguild.utils.InvalidGuildIDException;
import survfate.poesimpleguild.utils.JTable2XLS;
import survfate.poesimpleguild.utils.ProfileWithoutGuildIDException;
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

	public JMenuBar menuBar;
	public JMenu menuOption, menuGetLastest, menuAbout;
	public static JMenu menuLoadStatus;

	public static JCheckBoxMenuItem checkBoxMenuItemPoeStatistics;

	private static JCheckBoxMenuItem checkBoxMenuItemPoeTrade;

	private JCheckBoxMenuItem checkBoxMenuItemAutoFit;

	private JTable table;
	private TableColumnAdjuster adjuster;
	private DefaultTableModel tableModel;
	private JPanel panel;
	private JPanel controlPanel, statusPanel;

	public static JLabel poeStatisticsStatus, poeTradeStatus, lastestVersion;

	private JButton buttonGet, buttonXLS;
	private ButtonGroup buttonGroup;
	private JRadioButton radioGuildID, radioProfileName;
	public static JTextField textFieldGuildID;
	private JTextField textFieldProfileName;
	public JComboBox<Integer> comboBoxThreadAmount;

	private String currentGuildID;

	public static JTextArea logOutput;

	public MainPanel() {
		setLayout(new BorderLayout());
		tableModel = new DefaultTableModel(new String[] { "Order", "Account Name", "Member Type (Status)",
				"Challenge(s) Done", "Total Forum Posts", "Joined Date", "Last Visted Date", "Last Ladder Tracked",
				"Supporter Tag(s)", "Poe.Trade Online", "Profile URL" }, 0) {
			/**
					 *
					 */
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
				case 3:
				case 4:
				case 8:
					return Integer.class;
				case 5:
				case 6:
				case 7:
					return Date.class;
				case 9:
					return Boolean.class;
				case 10:
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
		table.getRowSorter().toggleSortOrder(0);

		adjuster = new TableColumnAdjuster(table);
		adjuster.adjustColumns();

		// Set Renderers to Columns
		NumberRenderer numberRenderer = new NumberRenderer();
		table.getColumnModel().getColumn(0).setCellRenderer(numberRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(new ChallengeIconsRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(numberRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(new DateRenderer());
		table.getColumnModel().getColumn(6).setCellRenderer(new DateRenderer());
		table.getColumnModel().getColumn(7).setCellRenderer(new TimeRenderer());
		table.getColumnModel().getColumn(8).setCellRenderer(new SupporterTagsRenderer());

		URLRenderer urlRenderer = new URLRenderer();
		table.getColumnModel().getColumn(10).setCellRenderer(urlRenderer);
		table.addMouseListener(urlRenderer);
		table.addMouseMotionListener(urlRenderer);

		add(new JScrollPane(table), BorderLayout.CENTER);

		menuBar = new JMenuBar();
		menuOption = new JMenu("Option");
		menuOption.setMnemonic(KeyEvent.VK_O);
		menuBar.add(menuOption);

		menuLoadStatus = new JMenu("Load Status");
		menuLoadStatus.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				ResourcesLoader.StatusLoadWorker loadStatus = new StatusLoadWorker();
				loadStatus.execute();
			}
		});
		menuBar.add(menuLoadStatus);

		menuGetLastest = new JMenu("Get Lastest Version");
		menuGetLastest.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported())
					try {
						Desktop.getDesktop()
								.browse(new URI("https://github.com/survfate/poesimpleguild/releases/latest"));
					} catch (IOException | URISyntaxException exception) {
						exception.printStackTrace();
					}
			}
		});
		menuBar.add(menuGetLastest);

		menuAbout = new JMenu("About");
		menuAbout.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported())
					try {
						Desktop.getDesktop()
								.browse(new URI("https://github.com/survfate/poesimpleguild/blob/master/README.md"));
					} catch (IOException | URISyntaxException exception) {
						exception.printStackTrace();
					}
			}
		});
		menuBar.add(menuAbout);

		checkBoxMenuItemPoeStatistics = new JCheckBoxMenuItem("Load PoeStatistics.com Last Ladder Tracked Data");
		menuOption.add(checkBoxMenuItemPoeStatistics);
		checkBoxMenuItemPoeTrade = new JCheckBoxMenuItem("Poe.Trade Online Check (WARNING: Slow!)");
		menuOption.add(checkBoxMenuItemPoeTrade);
		checkBoxMenuItemAutoFit = new JCheckBoxMenuItem("Fit All Columns On Complete");
		menuOption.add(checkBoxMenuItemAutoFit);

		panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		add(panel, BorderLayout.NORTH);

		controlPanel = new JPanel();
		controlPanel.setLayout(new MigLayout("center", "15[fill][fill]15[fill]15", ""));
		panel.add(controlPanel, BorderLayout.CENTER);

		radioGuildID = new JRadioButton();
		radioGuildID.addActionListener(this);
		controlPanel.add(radioGuildID);
		controlPanel.add(new JLabel("Guild ID:"));
		textFieldGuildID = new JTextField(6);
		controlPanel.add(textFieldGuildID);
		buttonGet = new JButton("Get Guild Members Data");
		buttonGet.addActionListener(this);
		controlPanel.add(buttonGet, "wrap, growy, span 0 2");

		radioProfileName = new JRadioButton();
		radioProfileName.addActionListener(this);
		controlPanel.add(radioProfileName);
		controlPanel.add(new JLabel("Profile Name:"));
		textFieldProfileName = new JTextField(6);
		textFieldProfileName.setEnabled(false);
		controlPanel.add(textFieldProfileName, "wrap, split 2");

		buttonGroup = new ButtonGroup();
		buttonGroup.add(radioGuildID);
		buttonGroup.add(radioProfileName);
		buttonGroup.setSelected(radioGuildID.getModel(), true);

		controlPanel.add(new JLabel("Thread(s):"), "skip 1");
		Integer[] threads = { 1, 2, 4, 8, 16 };
		comboBoxThreadAmount = new JComboBox<Integer>(threads);
		comboBoxThreadAmount.setSelectedIndex(3);
		controlPanel.add(comboBoxThreadAmount);

		buttonXLS = new JButton("Save Table As .XLS");
		buttonXLS.setEnabled(false);
		buttonXLS.addActionListener(this);
		controlPanel.add(buttonXLS);

		statusPanel = new JPanel();
		controlPanel.add(statusPanel, "dock east, gapbottom 15");

		statusPanel.setLayout(new MigLayout("center", "[fill]15[fill]", ""));
		statusPanel.setBorder(new TitledBorder(new LineBorder(Color.black, 1), "Status"));

		statusPanel.add(new JLabel("PoeStatistics.com"));
		poeStatisticsStatus = new JLabel("Unknown");
		statusPanel.add(poeStatisticsStatus, "wrap");

		statusPanel.add(new JLabel("Poe.Trade"));
		poeTradeStatus = new JLabel("Unknown");
		statusPanel.add(poeTradeStatus, "wrap");

		statusPanel.add(new JLabel("Lastest Version"));
		lastestVersion = new JLabel("Unknown");
		statusPanel.add(lastestVersion, "wrap");

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
		JFrame frame = new JFrame("PoE Simple Guild " + MainClass.VERSION);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MainPanel newContentPane = new MainPanel();
		newContentPane.setOpaque(true);
		frame.setJMenuBar(newContentPane.menuBar);
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
		if (event.getSource() == radioGuildID) {
			textFieldGuildID.setEnabled(true);
			textFieldProfileName.setEnabled(false);
		} else if (event.getSource() == radioProfileName) {
			textFieldProfileName.setEnabled(true);
			textFieldGuildID.setEnabled(false);
		} else if (event.getSource() == buttonGet) {
			if ((radioGuildID.isSelected() && !textFieldGuildID.getText().trim().equals(""))
					|| (radioProfileName.isSelected() && !textFieldProfileName.getText().trim().equals(""))) {
				// Clear all texts and print out Guild details
				logOutput.setText("");
				GuildLoadWorker loadGuild = new GuildLoadWorker();
				loadGuild.execute();
			} else
				JOptionPane.showMessageDialog(null, "Please enter a valid Guild ID or Profile.", "Error", 0);
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

	private static class TaskListener implements PropertyChangeListener {
		TaskListener() {
		}

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			// System.out.println(e.toString());
		}
	}

	// SwingWorker for geting Guild information
	public class GuildLoadWorker extends SwingWorker<Void, Void> {
		GuildLoadWorker() {
			addPropertyChangeListener(new TaskListener());
		}

		@Override
		protected Void doInBackground() throws ParseException, URISyntaxException, IOException, InvalidGuildIDException,
				InterruptedException, ExecutionException, ProfileWithoutGuildIDException {

			long tStart = System.currentTimeMillis();
			enableControl(false);

			if (radioProfileName.isSelected()) {
				// Below is reuse of Account code, need to rewrite later

				// Encode URL to ASCII String to avoid special characters error
				String urlStr = "http://www.pathofexile.com/account/view-profile/"
						+ textFieldProfileName.getText().trim();
				URL url = new URL(urlStr);
				URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
						url.getQuery(), url.getRef());

				// Parsing account details using Jsoup and OkHttp
				Document accountDoc = Jsoup.parse(HttpClient.runURL(uri.toASCIIString()));
				if (accountDoc.getElementsByClass("details").first().child(2).child(0).text().equals("Guild:"))
					textFieldGuildID.setText(accountDoc.getElementsByClass("details").first().child(2).child(2)
							.attr("href").substring(15));
				else
					throw new ProfileWithoutGuildIDException();
			}

			currentGuildID = textFieldGuildID.getText().trim();

			if (checkBoxMenuItemPoeTrade.isSelected() == false) {
				table.getColumnModel().getColumn(9).setCellRenderer(new CheckBoxRenderer(false));
			} else
				table.getColumnModel().getColumn(9).setCellRenderer(new CheckBoxRenderer(true));

			if (checkBoxMenuItemPoeStatistics.isSelected() == false) {
				TableColumnModel tableColumnModel = table.getColumnModel();
				tableColumnModel.removeColumn(tableColumnModel.getColumn(7));
			}

			tableModel.getDataVector().removeAllElements();
			tableModel.fireTableDataChanged();

			Document guildDoc = Jsoup
					.parse(HttpClient.runGuildURL("http://www.pathofexile.com/guild/profile/" + currentGuildID));
			Element detailsContent = guildDoc.getElementsByClass("details-content").first();
			int index = 1;
			int guildSize = detailsContent.getElementsByClass("member").size();

			logOutput.append("Guild Name: " + detailsContent.getElementsByClass("name").first().text() + "\t");
			try {
				logOutput.append("Guild Tag: " + detailsContent.getElementsByClass("guild-tag").first().text() + "\n");
				logOutput.append(
						"Guild Status: " + detailsContent.getElementsByClass("guild-status").first().text() + "\n");
			} catch (Exception e) {
				// Skipped since some Guild don't have these two
			}
			logOutput.append("Created: " + detailsContent.child(1).childNode(3).toString() + "\n");
			logOutput.append("Total Members: " + guildSize + "\n");

			// Multithreading for the loop
			int threadsUsed = (int) comboBoxThreadAmount.getSelectedItem();
			ExecutorService executorService = Executors.newFixedThreadPool(threadsUsed);
			logOutput.append("Status: Job started with " + threadsUsed + " working thread(s) \n");
			CompletionService<String> completionService = new ExecutorCompletionService<String>(executorService);

			// Run through each Guildmembers
			for (Element member : detailsContent.getElementsByClass("member")) {
				Runnable memberWorker = new Member2TableRowRunnable(member, tableModel, index);
				index++;
				completionService.submit(memberWorker, "successful");
				// executorService.execute(memberWorker);
			}
			executorService.shutdown();

			int amountCompleted = 0;
			while (!executorService.isTerminated()) {
				Future<String> future = completionService.take();
				if (future.get().equals("successful"))
					amountCompleted++;
				logOutput.append("Status: Loaded " + amountCompleted + "/" + guildSize + " members\n");
			}

			if (checkBoxMenuItemAutoFit.isSelected() == true)
				adjuster.adjustColumns();
			long tEnd = System.currentTimeMillis();
			long tDelta = tEnd - tStart;
			double elapsedSeconds = tDelta / 1000.0;
			logOutput.append(
					"Process completed, elapsed time: " + elapsedSeconds + " second(s), average time per account: "
							+ (new DecimalFormat("##.000")).format(elapsedSeconds / guildSize) + " second(s)");

			return null;
		}

		@Override
		protected void done() {
			try {
				get();
			} catch (ExecutionException e) {
				e.getCause().printStackTrace();
				if (e.getCause().getClass().getSimpleName().equals("InvalidGuildIDException")
						|| e.getCause().getClass().getSimpleName().equals("UnknownHostException")
						|| e.getCause().getClass().getSimpleName().equals("NullPointerException"))
					JOptionPane.showMessageDialog(null, "Invalid Guild ID or Profile Name! Please try again.", "Error",
							0);

				else if (e.getCause().getClass().getSimpleName().equals("ProfileWithoutGuildIDException"))
					JOptionPane.showMessageDialog(null, "This Profile does not belong in a Guild! Please try again.",
							"Error", 0);

				else if (e.getCause().getClass().getSimpleName().equals("SocketTimeoutException")) {
					JOptionPane.showMessageDialog(null, "Connection timed out! Please try again later.", "Error", 0);
					this.cancel(true); // Cancel the SwingWorker
					tableModel.fireTableDataChanged();
					enableControl(true);
				}

				else if (e.getCause().getClass().getSimpleName().equals("IOException"))
					; // ignore
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			tableModel.fireTableDataChanged();
			enableControl(true);
		}
	}

	public static class Member2TableRowRunnable implements Runnable {
		private final Element member;
		private final DefaultTableModel tableModel;
		private int index;

		Member2TableRowRunnable(Element member, DefaultTableModel tableModel, int index) {
			this.member = member;

			this.tableModel = tableModel;
			this.index = index;
		}

		@Override
		public void run() {
			//
			String accountName = member.child(0).text().trim();
			String title = (member.child(0).child(0).childNodeSize() == 2)
					? member.child(0).child(0).child(0).attr("title") : null;
			int challengeNums = (title == null) ? 0
					: Integer.parseInt(new StringTokenizer(title).nextToken("Completed").trim());

			// Instantiate account object
			try {
				Account account = new Account(accountName);
				int forumPosts = account.getForumPosts();
				Date joinedDate = account.getJoinedDate();
				Date lastVisitedDate = account.getLastVisitedDate();
				Date lastLadderTracked = (checkBoxMenuItemPoeStatistics.isSelected() == false
						|| account.getLastLadderTracked().equals(Date.from(Instant.ofEpochSecond(0)))) ? null
								: account.getLastLadderTracked();
				StringTokenizer tokenizer = new StringTokenizer(account.getSupporterTagKeys());
				String tagKeys = (account.getSupporterTagKeys() != "")
						? String.valueOf(tokenizer.countTokens()) + " " + account.getSupporterTagKeys() : null;
				String memberType = member.child(1).text().trim() + " " + account.getStatus();
				boolean poeTradeOnline = (checkBoxMenuItemPoeTrade.isSelected() == true)
						? account.getPoeTradeOnlineStatus() : false;
				URL profileURL = account.getURL();
				tableModel.addRow(new Object[] { index, accountName, memberType, challengeNums, forumPosts, joinedDate,
						lastVisitedDate, lastLadderTracked, tagKeys, poeTradeOnline, profileURL });
			} catch (ParseException | URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void enableControl(boolean enable) {
		buttonGet.setEnabled(enable);
		buttonXLS.setEnabled(enable);
		radioGuildID.setEnabled(enable);
		radioProfileName.setEnabled(enable);
		if (radioGuildID.isSelected())
			textFieldGuildID.setEnabled(enable);
		else
			textFieldProfileName.setEnabled(enable);
		comboBoxThreadAmount.setEnabled(enable);
		menuOption.setEnabled(enable);
		menuLoadStatus.setEnabled(enable);
	}
}
