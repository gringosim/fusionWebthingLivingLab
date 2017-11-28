package es.upm.vlc;

public class PlayerState 
{
	private int state, time, volume, length;
	
	public static final int
		PLAYING = 1,
		PAUSED = 2,
		STOPPED = 0;
		
		public  PlayerState(int state, int time, int volume, int length)
		{
			this.state = state;
			this.time = time;
			this.volume = volume;
			this.length = length;
		}
		
		public int getState() { return state; }
		
		public int getTime() { return time; }
		
		public int getVolume() { return volume; }
		
		public int getLength() { return length; }
}
