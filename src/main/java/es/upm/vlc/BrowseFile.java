package es.upm.vlc;


public class BrowseFile
{

	private String name, type, path, ext;
	
	private final String directory = "dir";
	public final String[]
			audioExtensions = { "mp3", "wav" },
			videoExtensions = { "mp4" },
			playlistExtensions = { "xspf", "m3u", "pls" };
	
	public BrowseFile(String name, String type, String path)
	{
		this.name = name;
		this.type = type;
		this.path = path;
		if (isDir()) ext = directory;
		// Extensiones de 4 o mas
		else
		{
			String[] s = path.split("\\.");
			ext = s[s.length-1];
		}
	}
	
	public String getName() { return name; }
	public String getPath() { return path; }
	public String getExtension() { return ext; }
	public String getType() { return type; }
	public boolean isDir() { return type.equals("dir"); }
	public boolean isAudio()
	{
		boolean res = false;
		for (String e:audioExtensions) res = res || e.equalsIgnoreCase(ext);
		return res;
	}
	public boolean isVideo()
	{
		boolean res = false;
		for (String e:videoExtensions) res = res || e.equalsIgnoreCase(ext);
		return res;
	}
	public boolean isPlaylist()
	{
		boolean res = false;
		for (String e:playlistExtensions) res = res || e.equalsIgnoreCase(ext);
		return res;
	}
}
