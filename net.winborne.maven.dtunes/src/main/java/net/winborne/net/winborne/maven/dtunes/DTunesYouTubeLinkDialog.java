package net.winborne.net.winborne.maven.dtunes;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;

public class DTunesYouTubeLinkDialog extends JFrame {

	private JPanel contentPane;
	private static Process process;
	private static DTunesYouTubeLinkDialog frame;
	private static JTextField textField;
	private JButton button;
	private JButton btnNewButton;
	private static ScheduledExecutorService executor;
	private static String videoID = "";
	private static String videoTitle = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new DTunesYouTubeLinkDialog();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * This wizard allows the user to add as many YouTube links to their playlist as
	 * they like. Links are checked at this stage but not downloaded yet.
	 */
	public DTunesYouTubeLinkDialog() {
		setTitle("Add YouTube link to queue");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 140);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[424px]", "[25][25][25]"));

		JLabel lblNewLabel = new JLabel("Enter YouTube link to add song to the download queue:");
		contentPane.add(lblNewLabel, "cell 0 0,grow");

		textField = new JTextField();
		contentPane.add(textField, "cell 0 1,grow");
		textField.setColumns(10);

		button = new JButton("Add another song...");
		button.setPreferredSize(new Dimension(100, 100));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(button, "flowx,cell 0 2,grow");

		btnNewButton = new JButton("Done");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int indexOfVideoID = textField.getText().indexOf("?v=");
				char[] linkArray = textField.getText().toCharArray();
				for (int i = indexOfVideoID + 3; i < textField.getText().length(); i++) {
					videoID += linkArray[i];
				}

				File file = new File("youtube-dl-log.txt");

				ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c",
						"youtube-dl.exe " + textField.getText() + " -x").redirectOutput(Redirect.to(file));
				try {
					process = processBuilder.start();
				} catch (IOException er) {
					// TODO Auto-generated catch block
					er.printStackTrace();
				}

				RunnableYouTubeLinkDialogUpdater rpdu = new RunnableYouTubeLinkDialogUpdater();
				rpdu.run();
				executor = Executors.newScheduledThreadPool(1);
				executor.scheduleAtFixedRate(rpdu, 0, 250, TimeUnit.MILLISECONDS);

			}
		});
		contentPane.add(btnNewButton, "cell 0 2,grow");

	}

	// kill the runnable and the youtube-dl process
	public static void killProcess() {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM youtube-dl.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executor.shutdown();
	}

	// gives the video ID to the runnable so it can trim the string there
	public static String returnLink() {
		return videoID;
	}

	// called by the runnable when it finds the video title
	public static void setVideoTitle(String videoTitleIn) {
		System.out.println("Current video title: " + videoTitleIn);
		videoTitle = videoTitleIn;
		int choice = JOptionPane.showConfirmDialog(null, "Is the video title\n" + videoTitleIn + "\ncorrect?",
				"Verify video title", JOptionPane.YES_NO_OPTION);
		if (choice == 1) {
			System.out.println("user chose no");
			// do nothing
		} else {
			System.out.println("user chose yes");
			DTunesWindow.saveSong(videoTitleIn, textField.getText());
			textField.setText("");
		}
	}
}
