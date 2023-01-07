package net.winborne.net.winborne.maven.dtunes;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

public class RunnableYouTubeLinkDialogUpdater implements Runnable {

	private BufferedReader br;

	// Determines when the title of the YouTube video has been retrieved
	// once the title is retrieved, the process is killed, this runnable is
	// destroyed
	// then the trimmed title is passed back to the YouTubeLinkDialog.
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
				last = line;
				if (last.contains("Destination")) {
					char[] lastArray = last.toCharArray();
					String videoID = DTunesYouTubeLinkDialog.returnLink();
					int videoIDIndex = last.indexOf(videoID);
					String title = "";
					for (int i = 24; i < videoIDIndex - 1; i++) {
						title += lastArray[i];
					}

					DTunesYouTubeLinkDialog.killProcess();
					DTunesYouTubeLinkDialog.setVideoTitle(title);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
