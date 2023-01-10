package net.winborne.net.winborne.maven.dtunes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RunnableYouTubeDownloadManager implements Runnable {

	private static Process process;
	
	@Override
	public void run() {
		
		if (!DTunesWindow.getSongIsDownloading()) {
			DTunesWindow.signalSongIsDownloading();
			Song song = DTunesWindow.getSong();
			System.out.println();
			System.out.println();
			System.out.println("***DTunes isn't currently downloading a song. I am going to try to download " + song.getSongTitle() + ".***");

			File file = new File("youtube-dl-log.txt");

			ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c",
					"youtube-dl.exe " + "https://www.youtube.com/watch?v="+ song.getSongURL() + " -x")
					.redirectOutput(Redirect.to(file));
			try {
				process = processBuilder.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		
		
	}
}
