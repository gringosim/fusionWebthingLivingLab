package es.upm.vlc;

public class PlaylistFile 
{
private String name;
private int duration;
	
	public PlaylistFile(String name, int duration)
	{
		this.name = name;
		this.duration = duration;
	}
	
	public String getName() { return name; }
	public int getDuration() { return duration; }
}
