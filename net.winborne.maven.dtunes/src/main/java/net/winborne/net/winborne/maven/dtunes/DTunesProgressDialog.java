package net.winborne.net.winborne.maven.dtunes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;

public class DTunesProgressDialog extends JFrame {

	private JPanel contentPane;
	private static double progress = 0;
	private static JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DTunesProgressDialog frame = new DTunesProgressDialog();
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
		setTitle("Downloading song... (0%)");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		progressBar = new JProgressBar();
		contentPane.add(progressBar);
		
		
	}
	
	public static void setProgress(String progressIn) {
		System.out.println("setting progress..");
		
		try {
			progress = Double.parseDouble(progressIn);
			progressBar.setValue((int) Math.round(Double.parseDouble(progressIn)));
		} catch (Exception e) {
			System.out.println("Streaming letters, probably.");
		}
		
	}
	
	public static double getProgress() {
		return progress;
	}

}
