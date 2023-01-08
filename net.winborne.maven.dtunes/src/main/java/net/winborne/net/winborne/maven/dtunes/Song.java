package net.winborne.net.winborne.maven.dtunes;

public class Song {
	
	private String songTitle;
	private String songURL;
	private String songExtension;
	private int songNumber;
		
	public Song(int songNumberIn, String songTitleIn, String songURLIn, String songExtensionIn) {
		songTitle = songTitleIn;
		songURL = songURLIn;
		songExtension = songExtensionIn;
		songNumber = songNumberIn;
		
	}
	
	public String getSongTitle() {
		return songTitle;
	}
	
	public int getSongNumber() {
		return songNumber;
	}
	
	public String getSongExtension() {
		return songExtension;
	}
	
	public String getSongURL() {
		return songURL;
	}
	
	public String toString() {
		return "SONG #" + songNumber + "\n" +
			   "TITLE: " + songTitle + "\n" +
			   "URL: " + songURL + "\n" +
			   "EXTENSION: " + songExtension + "\n";
	}
}
