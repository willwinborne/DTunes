package net.winborne.net.winborne.maven.dtunes;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

/**
 * Entry point of DTunes. Created by William Winborne 12/4/2022.
 */
public class App {
	public static void main(String[] args) {
		
		// plant a copy of youtube-dl
		// test
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("youtube-dl.exe");
		File targetFile = new File("youtube-dl.exe");
		
		try {
			java.nio.file.Files.copy(is, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
 