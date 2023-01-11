package net.winborne.net.winborne.maven.dtunes;

public class RunnableDialogPinner implements Runnable {

	@Override
	public void run() {
		DTunesWindow.pinDialog();
	}
}
