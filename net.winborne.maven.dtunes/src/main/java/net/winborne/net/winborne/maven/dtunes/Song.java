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
	
	// overloaded constructor
	public Song(String toStringIn) {
		
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
		return songNumber + "," + songTitle + "," + songURL + "," +songExtension + "\n";
	}
	
	public String toStringExtended() {
		return "SONG #" + songNumber + "\n" +
			   "TITLE: " + songTitle + "\n" +
			   "URL: " + songURL + "\n" +
			   "EXTENSION: " + songExtension + "\n";
	}
}
