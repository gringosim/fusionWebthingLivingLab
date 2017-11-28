package es.upm.vlc;

public class Song 
{
	private String artist, title, artwork;
	
	public Song(String artist, String title, String artwork)
	{
		this.artist = artist;
		this.title = title;
		this.artwork = artwork;
	}
	
	public String getArtist() { return artist; }
	
	public String getTitle() { return title; }
	
	public String getArtwork() { return artwork; }
}
