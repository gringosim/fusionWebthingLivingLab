package es.upm.vlc;

public class Status
{
	private Song currentSong;
	private PlayerState state;
	private PlayerMode mode;
	
	public Status(Song currentSong, PlayerState state, PlayerMode mode)
	{
		this.currentSong = currentSong;
		this.state = state;
		this.mode = mode;
	}
	
	public Song getSong() { return currentSong; }
	
	public PlayerState getPlayerState() { return state; }
	
	public PlayerMode getPlayerMode() { return mode; }
	
}


