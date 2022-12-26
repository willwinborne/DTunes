package net.winborne.net.winborne.maven.dtunes;

import java.awt.EventQueue;

/**
 * Entry point of DTunes. Created by William Winborne 12/4/2022. This
 * application is free and open source, never should you pay for this.
 */
public class App {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DTunesWindow frame = new DTunesWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
