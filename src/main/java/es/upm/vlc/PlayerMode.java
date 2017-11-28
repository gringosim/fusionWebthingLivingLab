package es.upm.vlc;

public class PlayerMode 
{
	private Boolean random, loop, repeat;
		
		public PlayerMode(Boolean random, Boolean loop, Boolean repeat)
		{
			this.random = random;
			this.loop = loop;
			this.repeat = repeat;
		}
		
		public Boolean getRandom() { return random; }
		
		public Boolean getLoop() { return loop; }
		
		public Boolean getRepeat() { return repeat; }
}
