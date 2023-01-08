package net.winborne.net.winborne.maven.dtunes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RunnableYouTubeLinkDialogPinner implements Runnable {

	@Override
	public void run() {
		DTunesWindow.pinYouTubeLinkDialog();
	}
}
