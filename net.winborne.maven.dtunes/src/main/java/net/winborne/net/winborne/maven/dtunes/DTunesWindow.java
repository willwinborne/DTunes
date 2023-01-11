package net.winborne.net.winborne.maven.dtunes;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import java.awt.GridLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class DTunesWindow extends JFrame {

	// ui
	private JPanel contentPane;
	private static DTunesProgressDialog frame;
	private static DTunesYouTubeLinkDialog frame2;
	private static JTable table;
	private static DefaultTableModel model;

	// services
	private static ScheduledExecutorService executor;
	private static ScheduledExecutorService pinnerExecutor;
	private static ScheduledExecutorService dlmanExecutor;
	private static ScheduledExecutorService pinnerExecutorForDownloader;

	// program state
	private static Boolean downloaderIsOpen = false;
	private static Boolean linkDialogIsOpen = false;
	private static Boolean songIsDownloading = false;
	private static ArrayList<Song> playlist;
	private static Song song;
	private static int songIndex = 0;
	
	// developer
	private static String testPlaylistLocation = "C:\\Users\\willw\\OneDrive\\Desktop\\testPlaylist.txt";

	/**
	 * Dispose of the YouTubeLinkDialog. Also shuts down the pinner runnable.
	 */
	public static void disposeYouTubeLinkDialog() {
		pinnerExecutor.shutdown();
		frame2.dispose();
		linkDialogIsOpen = false;
		System.out.println("[INFO] YouTube link dialog and pinner disposed.");
	}

	/**
	 * Save a song that has been added to the queue to the JList. Also create a Song
	 * object and save it to the playlist.
	 */
	public static void saveSong(String songTitle, String songURL) {
		if (table.getValueAt(0, 1) == "No songs added!" && table.getValueAt(0, 0) == "N/A") {
			model.removeRow(0);
			model.insertRow(0, new Object[] { table.getRowCount() + 1, songTitle, songURL, ".m4a" });
		} else {
			model.addRow(new Object[] { table.getRowCount() + 1, songTitle, songURL, ".m4a" });
		}

		Song s = new Song(table.getRowCount() + 1, songTitle, songURL, ".m4a");
		playlist.add(s);
		System.out.println(s.toString());
	}

	/**
	 * Pins the existing dialog to the main window. Meant to be called by a runnable
	 * with a scheduled executor service.
	 */
	public static void pinDialog() {
		if (linkDialogIsOpen) {
			frame2.setLocation(
					DTunesWindow.getFrames()[0].getX()
							+ (DTunesWindow.getFrames()[0].getWidth() / 2 - (frame2.getWidth() / 2)),
					DTunesWindow.getFrames()[0].getY()
							+ (DTunesWindow.getFrames()[0].getHeight() / 2 - (frame2.getHeight() / 2)));
		} else {
			frame.setLocation(
					DTunesWindow.getFrames()[0].getX()
							+ (DTunesWindow.getFrames()[0].getWidth() / 2 - (frame.getWidth() / 2)),
					DTunesWindow.getFrames()[0].getY()
							+ (DTunesWindow.getFrames()[0].getHeight() / 2 - (frame.getHeight() / 2)));
		}
	}

	/**
	 * Called by external classes to signal that a song is finished downloading.
	 * This shuts down the progress updater runnable scheduler.
	 */
	public static void signalSongDoneDownloading() {
		System.out.println("[INFO] A song is done downloading. Shutting down the progress executor.");
		songIsDownloading = false;
		executor.shutdownNow();
	}

	/**
	 * Called by external classes to signal that a song is currently downloading.
	 * Also starts the progress updater runnable scheduler.
	 */
	public static void signalSongIsDownloading() {
		System.out.println("[INFO] A song is ready to download. Starting up a progress executor.");
		songIsDownloading = true;
		RunnableProgressDialogUpdater rpdu = new RunnableProgressDialogUpdater();
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(rpdu, 0, 1000, TimeUnit.MILLISECONDS);
	}

	/**
	 * Print the entire playlist
	 */
	public static void debugPlaylist() {
		System.out.println("[INFO] Current playlist:");
		for (Song s : playlist) {
			System.out.println(s.getSongTitle());
		}
	}

	/**
	 * Called by external classes to retrieve the song to download
	 * 
	 * @return
	 */
	public static Song getSong() {

		if (song == null) {
			song = playlist.get(0);
			songIndex++;
			return song;
		}

		if (songIndex == playlist.size()) {
			dlmanExecutor.shutdown();
			System.out.println(
					"[INFO] Last song has finished downloading, shutting down the dlman executor and the download dialog.");
			downloaderIsOpen = false;
			frame.dispose();
		}

		song = playlist.get(songIndex);
		songIndex++;
		System.out.println("[INFO] Main window giving song to dlman: " + song.getSongTitle());
		return song;
	}

	/**
	 * Tells whether DTunes is currently downloading a song.
	 * 
	 * @return
	 */
	public static Boolean getSongIsDownloading() {
		return songIsDownloading;
	}

	/**
	 * Convert the downloaded song into an mp3
	 */
	@SuppressWarnings("unused")
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
		playlist = new ArrayList<Song>();
		setTitle("DTunes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 927, 547);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu x = new JMenu("Export and import");
		JMenu y = new JMenu("Developer");

		final JMenuItem o1 = new JMenuItem("Kill youtube-dl processes");
		o1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("taskkill /F /IM youtube-dl.exe");
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		});
		
		final JMenuItem o2 = new JMenuItem("Import test playlist");
		o2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> contents = new ArrayList<String>();
				File f = new File(testPlaylistLocation);
				try {
					Scanner sca = new Scanner(f);
					while (sca.hasNextLine()) {
						contents.add(sca.nextLine());
					}
					sca.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				for (int i = 0; i < contents.size(); i++) {
					System.out.println("[INFO] IMPORT: " + (i + 1) + " / " + contents.size() + " songs imported");
					String[] songContents = contents.get(i).split(",");
					Song s = new Song(Integer.parseInt(songContents[0]), songContents[1], songContents[2],
							songContents[3]);
					saveSong(s.getSongTitle(), s.getSongURL());
				}

			}
		});


		final JMenuItem m1 = new JMenuItem("Export current playlist to zip file...");
		m1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("JMenuBar interacted - export playlist...");

				String exportString = "";
				for (Song s : playlist) {
					// trim /n if it is the first song
//					if (playlist.indexOf(s) == 0 || playlist.indexOf(s) == playlist.size() - 1) {
//						exportString += s.toString().trim();
//						return;
//					}
					exportString += s.toString();
				}

				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(m1) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					FileWriter myWriter;
					try {
						myWriter = new FileWriter(file);
						myWriter.write(exportString);
						myWriter.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}
		});
		final JMenuItem m2 = new JMenuItem("Import playlist from zip file...");
		m2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("JMenuBar interacted - import playlist...");
				// clear table
//				if (table.getValueAt(0, 1) == "No songs added!" && table.getValueAt(0, 0) == "N/A") {
//					for (int i = 0; i < table.getRowCount(); i++) {
//						
//						model.removeRow(i);
//					}
//					playlist.clear();

				// }
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(m2) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					Scanner sc;
					ArrayList<String> contents = new ArrayList<String>();
					try {
						sc = new Scanner(file);
						while (sc.hasNextLine()) {
							contents.add(sc.nextLine());
						}
						sc.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					for (int i = 0; i < contents.size(); i++) {
						System.out.println("[INFO] IMPORT: " + (i + 1) + " / " + contents.size() + " songs imported");
						String[] songContents = contents.get(i).split(",");
						Song s = new Song(Integer.parseInt(songContents[0]), songContents[1], songContents[2],
								songContents[3]);
						saveSong(s.getSongTitle(), s.getSongURL());
					}

				}

			}
		});
		x.add(m1);
		x.add(m2);
		y.add(o1);
		y.add(o2);
		menuBar.add(x);
		menuBar.add(y);

		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(100, 00));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		System.out.println("[INFO] Working Directory = " + System.getProperty("user.dir"));
		contentPane.setLayout(new MigLayout("", "[60][2000]", "[]"));

		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 0,grow");
		panel.setLayout(
				new MigLayout("", "[120px][32px][117px][165px]", "[][][][][][][][][][][][][][][][][][][][][][]"));

		JLabel lblNewLabel_5 = new JLabel("DPLAYER Companion");
		panel.add(lblNewLabel_5, "cell 0 0");

		JLabel lblNewLabel_1 = new JLabel("Step 1: Import your songs");
		panel.add(lblNewLabel_1, "cell 0 3");

		JButton btnNewButton_1 = new JButton("Add songs from YouTube...");
		btnNewButton_1.setPreferredSize(new Dimension(100, 20));
		panel.add(btnNewButton_1, "cell 0 4");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							// open playlist wizard
							if (!downloaderIsOpen) {
								linkDialogIsOpen = true;
								frame2 = new DTunesYouTubeLinkDialog();
								frame2.setUndecorated(true);
								frame2.setVisible(true);
								frame2.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.GRAY));
								RunnableDialogPinner runnablePinner = new RunnableDialogPinner();
								runnablePinner.run();
								pinnerExecutor = Executors.newScheduledThreadPool(1);
								pinnerExecutor.scheduleAtFixedRate(runnablePinner, 0, 1, TimeUnit.MILLISECONDS);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		JLabel lblNewLabel_2 = new JLabel("Step 2: Download from YouTube");

		panel.add(lblNewLabel_2, "cell 0 6");

		JButton btnNewButton_3 = new JButton("Download YouTube songs");
		btnNewButton_3.setPreferredSize(new Dimension(100, 20));
		panel.add(btnNewButton_3, "cell 0 7");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {

							if (table.getValueAt(0, 1) == "No songs added!" && table.getValueAt(0, 0) == "N/A") {
								JOptionPane.showMessageDialog(null, "Add a song from YouTube first.", "No songs added",
										JOptionPane.ERROR_MESSAGE);
								return;
							}

							// open playlist downloader
							if (!linkDialogIsOpen) {
								downloaderIsOpen = true;
								frame = new DTunesProgressDialog();
								frame.setUndecorated(true);
								frame.setVisible(true);
								frame.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.GRAY));
								RunnableDialogPinner runnablePinnerForDownloader = new RunnableDialogPinner();
								runnablePinnerForDownloader.run();
								pinnerExecutorForDownloader = Executors.newScheduledThreadPool(1);
								pinnerExecutorForDownloader.scheduleAtFixedRate(runnablePinnerForDownloader, 0, 1,
										TimeUnit.MILLISECONDS);

								// dlman needs to check into this class to see which song it needs to download
								// runnables that it needs to call can be managed from here
								RunnableYouTubeDownloadManager dlman = new RunnableYouTubeDownloadManager();
								dlmanExecutor = Executors.newScheduledThreadPool(1);
								dlmanExecutor.scheduleAtFixedRate(dlman, 0, 250, TimeUnit.MILLISECONDS);
								System.out.println("Instantiated dlman.");

							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
			}
		});

		JLabel lblNewLabel_3 = new JLabel("Step 3: Apply to DPLAYER");
		panel.add(lblNewLabel_3, "cell 0 9");

		JButton btnNewButton_2 = new JButton("Apply playlist!");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnNewButton_2.setPreferredSize(new Dimension(100, 20));
		panel.add(btnNewButton_2, "cell 0 10");

		JLabel lblNewLabel_4 = new JLabel("");
		panel.add(lblNewLabel_4, "cell 0 16");

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, "cell 1 0");

		Object[][] data = { { "N/A", "No songs added!", "N/A", "N/A" }, };

		String[] columnNames = { "#", "Title", "URL / Video ID", "Filetype" };
		model = new DefaultTableModel(data, columnNames);
		table = new JTable(model) {

			public boolean editCellAt(int row, int column, java.util.EventObject e) {
				return false;
			}
		};

		table.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.setPreferredScrollableViewportSize(new Dimension(2000, 2000));
		TableColumn column = null;
		for (int i = 0; i < 3; i++) {
			column = table.getColumnModel().getColumn(i);
			if (i == 2 || i == 3) {
				column.setPreferredWidth(100);
			}
			if (i == 1) {
				column.setPreferredWidth(500);
			}

			if (i == 0) {
				column.setPreferredWidth(50);
			}
		}
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));

		panel_1.add(scrollPane);

	}

}
