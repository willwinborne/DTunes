package net.winborne.net.winborne.maven.dtunes;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;

public class DTunesYouTubeLinkDialog extends JFrame {

	private JPanel contentPane;
	private static double progress = 0;
	private static DTunesYouTubeLinkDialog frame;
	private JTextField textField;
	private JButton button;
	private JButton btnNewButton;

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
	 * Create the frame.
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
		contentPane.add(btnNewButton, "cell 0 2,grow");
		
		
	}

}
