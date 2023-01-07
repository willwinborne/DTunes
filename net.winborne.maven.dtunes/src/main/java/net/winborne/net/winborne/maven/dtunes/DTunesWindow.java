package net.winborne.net.winborne.maven.dtunes;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListModel;
import javax.swing.border.SoftBevelBorder;

import org.apache.logging.log4j.core.util.IOUtils;

import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;
import java.awt.GridLayout;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class DTunesWindow extends JFrame {

	private static final int FIXED_WIDTH = 650;
	private JPanel contentPane;
	private static DTunesProgressDialog frame;
	private static DTunesYouTubeLinkDialog frame2;
	private static Process process;
	private JTable table;

	/**
	 * Download a song from YouTube given a URL. File goes into the working
	 * directory.
	 */
	private Boolean downloadSongFromYouTube(String url) throws FileNotFoundException, UnsupportedEncodingException {
		System.out.println("Downloading from YouTube with URL: " + url);

		File file = new File("youtube-dl-log.txt");

		ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c",
				"youtube-dl.exe " + "https://www.youtube.com/watch?v=XOzs1FehYOA" + " -x")
				.redirectOutput(Redirect.to(file));
		try {
			process = processBuilder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RunnableProgressDialogUpdater rpdu = new RunnableProgressDialogUpdater();
		rpdu.run();
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(rpdu, 0, 250, TimeUnit.MILLISECONDS);
		return true;
	}

	/**
	 * Convert the downloaded song into an mp3
	 */
	private static Boolean convertSongFromWebmToMp3(String filename) {
		boolean succeeded = true;
		ConvertProgressDialogUpdater listener = new ConvertProgressDialogUpdater();

		try {
			File source = new File("Bush - Glycerine-hOllF3TgAsM.webm");
			File target = new File("Bush - Glycerine-hOllF3TgAsM.ogg");

			// Audio Attributes
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec("libmp3lame");
			audio.setBitRate(128000);
			audio.setChannels(2);
			audio.setSamplingRate(44100);

			// Encoding attributes
			EncodingAttributes attrs = new EncodingAttributes();
			// TODO: does this work? it was mp3 before
			attrs.setOutputFormat("ogg");
			attrs.setAudioAttributes(audio);

			// Encode
			Encoder encoder = new Encoder();
			encoder.encode(new MultimediaObject(source), target, attrs, listener);

		} catch (Exception ex) {
			ex.printStackTrace();
			succeeded = false;
		}

		return succeeded;
	}

	/**
	 * Main DTunes Window Definition
	 */
	public DTunesWindow() {
		setResizable(false);

		setTitle("DTunes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 927, 547);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu x = new JMenu("Export and import");

		JMenuItem m1 = new JMenuItem("Export current playlist to zip file...");
		m1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("JMenuBar interacted - export playlist...");
			}
		});
		JMenuItem m2 = new JMenuItem("Import playlist from zip file...");
		m2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("JMenuBar interacted - import playlist...");
			}
		});
		x.add(m1);
		x.add(m2);
		menuBar.add(x);

		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(100, 00));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement("USA");
		listModel.addElement("USB");
		listModel.addElement("USC");
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		contentPane.setLayout(new MigLayout("", "[46px,grow][184px][300px][67px]", "[]"));
		Dimension d = new Dimension(300, 470);

		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[155px][32px][117px][165px][99px][217px]",
				"[][10][][][][][25][][][][25][][][][][][][][][][][]"));

		JLabel lblNewLabel_5 = new JLabel("DPLAYER Companion");
		panel.add(lblNewLabel_5, "cell 0 0");

		JLabel lblNewLabel_1 = new JLabel("Step 1: Import your songs");
		panel.add(lblNewLabel_1, "cell 0 3");

		final JButton btnNewButton = new JButton("Add songs from file...");
		panel.add(btnNewButton, "cell 0 4");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convertSongFromWebmToMp3("hi");
				System.out.println("button clicked");
				// In response to a button click:
				// final JFileChooser fc = new JFileChooser();
				// int returnVal = fc.showOpenDialog(btnNewButton);
			}
		});

		JButton btnNewButton_1 = new JButton("Add songs from YouTube...");
		panel.add(btnNewButton_1, "cell 0 5");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							frame2 = new DTunesYouTubeLinkDialog();
							frame2.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		JLabel lblNewLabel_2 = new JLabel("Step 2: Download from YouTube");

		panel.add(lblNewLabel_2, "cell 0 7");

		JButton btnNewButton_3 = new JButton("Download YouTube songs");
		panel.add(btnNewButton_3, "cell 0 8");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							frame = new DTunesProgressDialog();
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							downloadSongFromYouTube("https://www.youtube.com/watch?v=hOllF3TgAsM");
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
			}
		});

		JButton btnNewButton_5 = new JButton("Convert and apply playlist to DPLAYER");
		panel.add(btnNewButton_5, "cell 0 9");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		JLabel lblNewLabel_3 = new JLabel("Step 3: Apply to DPLAYER");
		panel.add(lblNewLabel_3, "cell 0 11");

		JButton btnNewButton_2 = new JButton("Apply playlist!");
		panel.add(btnNewButton_2, "cell 0 12");

		JLabel lblNewLabel_4 = new JLabel("");
		panel.add(lblNewLabel_4, "cell 0 16");
		
				JPanel panel_1 = new JPanel();
				contentPane.add(panel_1, "cell 1 0 2 1");
				panel_1.setLayout(new MigLayout("", "[grow][1px]", "[][grow][1px]"));
				Object[][] data = {
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
						{"0", "b", ".com", "0"},
				
				};
				String[] columnNames = {"#", "Title", "URL", "Length"};
				
				
				table = new JTable(data, columnNames);
				table.setFillsViewportHeight(true);
				JScrollPane scrollPane = new JScrollPane(table);
				table.setFillsViewportHeight(true);
				
				panel_1.add(scrollPane, "cell 0 1 2 1,grow");

	}

}
