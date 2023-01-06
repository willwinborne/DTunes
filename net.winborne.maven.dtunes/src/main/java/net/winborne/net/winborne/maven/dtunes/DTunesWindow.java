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

public class DTunesWindow extends JFrame {

	private static final int FIXED_WIDTH = 650;
	private JPanel contentPane;
	private static DTunesProgressDialog frame;
	private static Process process;

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
			Process process = processBuilder.start();

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

	public static Process getProcess() {
		return process;
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
	 * Main DTunes Window Control
	 */
	public DTunesWindow() {
		setResizable(false);

		setTitle("DTunes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 927, 547);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement("USA");
		listModel.addElement("USB");
		listModel.addElement("USC");
		contentPane.setLayout(new MigLayout("", "[:140px:140px][25px:n:25px][::100px][][][][][][][][][][][][][]", "[1px][498px]"));
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(1000, 1000));
		JPanel singlePanel = new JPanel();
		JPanel bulkPanel = new JPanel();
		tabbedPane.add("Single", singlePanel);
		
		JPanel panel = new JPanel();
		singlePanel.add(panel);
				panel.setLayout(new GridLayout(0, 1, 0, 0));
		
				final JButton btnNewButton = new JButton("  Add from file...  ");
				panel.add(btnNewButton);
				
						JButton btnNewButton_1 = new JButton("Add songs from YouTube...");
						panel.add(btnNewButton_1);
						btnNewButton_1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								EventQueue.invokeLater(new Runnable() {
									public void run() {
										try {
											frame = new DTunesProgressDialog();
											frame.setVisible(true);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});

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
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						convertSongFromWebmToMp3("hi");
						System.out.println("button clicked");
						// In response to a button click:
						// final JFileChooser fc = new JFileChooser();
						// int returnVal = fc.showOpenDialog(btnNewButton);
					}
				});
		tabbedPane.add("Bulk", bulkPanel);
		
		JPanel panel_1 = new JPanel();
		bulkPanel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnNewButton_2 = new JButton("  Add song from file...  ");
		panel_1.add(btnNewButton_2);
		
		JButton btnNewButton_1_1 = new JButton("Add song from YouTube...");
		panel_1.add(btnNewButton_1_1);
		tabbedPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		contentPane.add(tabbedPane, "flowy,cell 0 1,alignx center");
		Dimension d = new Dimension(300, 470);
																				
																						JList<String> list = new JList<String>((ListModel) listModel);
																						list.setVisibleRowCount(50);
																						list.setFixedCellHeight(12);
																						list.setFixedCellWidth(500);
																						list.setMinimumSize(d);
																						
																								list.setBorder(new LineBorder(new Color(0, 0, 0)));
																								contentPane.add(list, "cell 12 1");
																		
																				JLabel lblNewLabel = new JLabel("Song count: " + Integer.toString(listModel.size()));
																				lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
																				lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
																				contentPane.add(lblNewLabel, "cell 15 1,alignx right,growy");

	}

}
