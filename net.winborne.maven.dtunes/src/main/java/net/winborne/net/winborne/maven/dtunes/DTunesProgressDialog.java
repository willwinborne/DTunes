package net.winborne.net.winborne.maven.dtunes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;

public class DTunesProgressDialog extends JFrame {

	private JPanel contentPane;
	private double progress = 0;

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
		
		JProgressBar progressBar = new JProgressBar();
		contentPane.add(progressBar);
		
		
	}
	
	public void setProgress(double progressIn) {
		progress = progressIn;
	}

}
