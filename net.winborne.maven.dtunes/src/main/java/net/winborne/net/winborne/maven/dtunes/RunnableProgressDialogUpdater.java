package net.winborne.net.winborne.maven.dtunes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RunnableProgressDialogUpdater implements Runnable {

	private BufferedReader br;

	@Override
	public void run() {
		File file = new File("youtube-dl-log.txt");
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String last = "";
		String line = "";
		try {
				while ((line = br.readLine()) != null) {
					
					if (line.contains("ETA")) {
					last = line;
					
					String[] progressArray = last.split(" ");
					String fileSize = progressArray[4];
					String downloadSpeed = progressArray[6];
					String eta = progressArray[8];
					
					
					char[] lastArray = last.toCharArray();
					String progress = "";
					
					if (last.length() > 0) {
						for (int i = 12; i <= 15; i++) {
							progress += lastArray[i];
						}
					}
					DTunesProgressDialog.setProgress(progress, fileSize, downloadSpeed, eta);

					if (progress.equals("00% ")) {
						DTunesWindow.signalSongDoneDownloading();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
