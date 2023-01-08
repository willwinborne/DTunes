package net.winborne.net.winborne.maven.dtunes;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

public class DTunesProgressDialog extends JFrame {

	private JPanel contentPane;
	private static double progress = 0;
	private static JProgressBar progressBar;
	private static DTunesProgressDialog frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
	}
	/**
	 * Create the frame.
	 */
	public DTunesProgressDialog() {
		setAlwaysOnTop(true);
		setTitle("Downloading song...");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 350, 140);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[50][50][]", "[14][14px][][14][][14]"));
		
		JLabel lblNewLabel = new JLabel("Downloading ");
		contentPane.add(lblNewLabel, "flowx,cell 1 0");
		
		JLabel lblNewLabel_2 = new JLabel("Speed:");
		contentPane.add(lblNewLabel_2, "cell 2 0,alignx right");
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(1000, 50));
		contentPane.add(progressBar, "cell 0 1 3 1,alignx left,aligny top");
		
		JLabel lblNewLabel_1 = new JLabel("");
		contentPane.add(lblNewLabel_1, "flowx,cell 1 3");
		
		JLabel lblNewLabel_3 = new JLabel("Song 3 of 8");
		contentPane.add(lblNewLabel_3, "cell 1 4");
		
		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setPreferredSize(new Dimension(1000, 50));
		contentPane.add(progressBar_1, "cell 0 5 3 1");
	}
	
	/**
	 * Update the progress bar of the song that is downloading.
	 * @param progressIn
	 */
	public static void setProgress(String progressIn) {
		System.out.println("Parsing progress string " + progressIn);
		//if (progressIn == "00% ") {
		//	System.out.println("A video is done downloading");
		//	DTunesWindow.signalSongDoneDownloading();
		//}
		try {
			progress = Double.parseDouble(progressIn);
			progressBar.setValue((int) Math.round(Double.parseDouble(progressIn)));
		} catch (Exception e) {
			//System.out.println("Streaming letters, probably...");
		}
	}
	
	/**
	 * Return the progress of the song that is downloading.
	 * @return
	 */
	public static double getProgress() {
		return progress;
	}
}
