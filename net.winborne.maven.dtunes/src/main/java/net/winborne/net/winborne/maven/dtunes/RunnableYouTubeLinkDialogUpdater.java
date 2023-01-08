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

	/**
	 * Determines when the title of the YouTube video has been retrieved. once the
	 * title is retrieved, the process is killed, the schedule for this runnable is
	 * destroyed, then the trimmed title is passed back to the YouTubeLinkDialog.
	 */
	@Override
	public void run() {
		File file = new File("youtube-dl-log.txt");
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if (line.contains("Destination")) {
					char[] lastArray = line.toCharArray();
					String videoID = DTunesYouTubeLinkDialog.returnLink();
					int ytdlTagIndex = line.indexOf("Destination");
					int videoIDIndex = line.indexOf(videoID);
					String title = "";
					for (int i = ytdlTagIndex + 13; i < videoIDIndex - 1; i++) {
						title += lastArray[i];
					}
					DTunesYouTubeLinkDialog.killProcess(false);
					DTunesYouTubeLinkDialog.setVideoTitle(title);

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
