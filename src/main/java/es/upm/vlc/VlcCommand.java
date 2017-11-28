package es.upm.vlc;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.http.protocol.HTTP;

import es.upm.vlc.RemoteRequest.OnServerResultListener;



/**
 * Public class that provides the possible commands for VLC requests.
 */
public class VlcCommand implements OnServerResultListener
{
	private Authentication auth;
	private int flag;
	private String path;

	private final int 
		NONE = -1,
		TODOSTATUS = 0,
		STATUS = 1,
		PLAYLIST = 2,
		BROWSE = 3,
		TODOPLAYLIST = 4,
		TODOSONG = 5,
		TODOADDSONG = 6,
		TODOADDPLAYLIST = 7,
		EMPTY = 8;
	
	private final String
		PROTOCOL = "http://",
		REQ = "/requests/",
		QUERY_STATUS = "status.json?",
		QUERY_PLAYLIST = "playlist.json?",
		QUERY_BROWSE = "browse.json?dir=",
			COMMAND_PLAY = "command=pl_play",
			COMMAND_PAUSE = "command=pl_pause",
			COMMAND_STOP = "command=pl_stop",
			COMMAND_PREVIOUS = "command=pl_previous",
			COMMAND_NEXT = "command=pl_next",
			COMMAND_REPEAT = "command=pl_repeat",
			COMMAND_LOOP = "command=pl_loop",
			COMMAND_RANDOM = "command=pl_random",
			COMMAND_EMPTY = "command=pl_empty",
			COMMAND_VOLUME_UP = "command=volume&val=+",
			COMMAND_VOLUME_DOWN = "command=volume&val=-",
			VAL = "40",
			COMMAND_MUTE = "command=volume&val=0",
			COMMAND_ADD_PLAY = "command=in_play&input=";
		
	private String URL;
	private RemoteRequest remoteRequest;
	
	private OnServerListener osl;
	
	public interface OnServerListener
	{
		public void onStatus(int error, Status status);
		public void onBrowse(int error, ArrayList<BrowseFile> browse);
		public void onRawServerResponse(ServerResponse sr) throws Exception;
	}
	
	
	/**
	 * Constructor
	 *
	 */
	
	public VlcCommand(VlcServer server)
	{
		this(server.getIP(), server.getPassword());
	}
	
	public VlcCommand(String server, String password)
	{
		initialize(server, password);
	}
	
	public void initialize(String server, String password)
	{
		URL =  PROTOCOL + server + REQ;
		auth = new Authentication(password);
	}
	
	public void setOnServerListener(OnServerListener listener)
	{
		osl = listener;
	}


	/**
	 * Shows the status of the program in that moment json .
	 */
	public void getStatus()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS;
		flag = STATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	/**
	 * Shows the complete playlist in json.
	 */
	public void getPlaylist()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_PLAYLIST;
		flag = PLAYLIST;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	/**
	 * Shows the complete browse in json.
	 */
	public void getBrowse(String path)
	{
		try
		{
			path = URLEncoder.encode(path, HTTP.UTF_8);
			path = path.replaceAll(Pattern.quote("+"), "%20");
		}
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_BROWSE + path;
		flag = BROWSE;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	
	/**
	 * Sends a play order to the remote VLC server.
	 */
	public void play()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_PLAY;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	
	/**
	 * Sends a pause order to the remote VLC server.
	 */
	public void pause()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_PAUSE;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	
	/**
	 * Sends a stop order to the remote VLC server.
	 */
	public void stop()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_STOP;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	
	/**
	 * Sends a previous order to the remote VLC server.
	 */
	public void previous()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_PREVIOUS;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	
	/**
	 * Sends a next order to the remote VLC server.
	 */
	public void next()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_NEXT;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	
	/**
	 * Sends a repeat order to the remote VLC server.
	 * This method repeat one song.
	 */
	public void repeat()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_REPEAT;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	
	/**
	 * Sends a loop order to the remote VLC server.
	 * This method repeat the whole playlist.
	 */
	public void loop()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_LOOP;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	
	/**
	 * Sends a random order to the remote VLC server.
	 */
	public void random()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_RANDOM;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	
	/**
	 * Sends an empty order to the remote VLC server.
	 */
	public void empty(int f)
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_PLAYLIST + COMMAND_EMPTY;
		flag = f;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	public void empty()
	{
		empty(EMPTY);
	}
	
	
	/**
	 * Volume UP.
	 */
	public void volumeUp()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_VOLUME_UP + VAL;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	/**
	 * Volume DOWN. 
	 */
	public void volumeDown()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_VOLUME_DOWN + VAL;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	/**
	 * MUTE.
	 */
	public void mute()
	{
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_MUTE;
		flag = TODOSTATUS;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	/**
	 * Add a song to the actual playlist.
	 */
	private void addPlaylist()
	{
		flag = NONE;
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_PLAYLIST + COMMAND_ADD_PLAY + path;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
		
	/**
	 * Add a song to the Vlc.
	 */
	public void addSongAlone()
	{
		flag = NONE;
		remoteRequest = new RemoteRequest(auth, this);
		String request = URL + QUERY_STATUS + COMMAND_ADD_PLAY + path;
		remoteRequest.setUrls(request);
		remoteRequest.execute();
	}
	
	public void emptyAndAddSong(String playsong)
	{
		try
		{
			path = URLEncoder.encode(playsong, HTTP.UTF_8);
			path = path.replaceAll(Pattern.quote("+"), "%20");
			path = path.replaceAll("%2F","%5C");
		}
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		empty(TODOADDSONG);
	}
	
	public void emptyAndAddPlaylist(String playlist)
	{
		try
		{
			path = URLEncoder.encode(playlist, HTTP.UTF_8);
			path = path.replaceAll(Pattern.quote("+"), "%20");
			path = path.replaceAll("%2F","%5C");
		}
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		empty(TODOADDPLAYLIST);
	}

	@Override
	public void onServerResponse(ServerResponse sr) throws Exception
	{
		if(sr==null) {
			System.out.println("throw new Exception(Error);");
			osl.onRawServerResponse(null);
			throw new RuntimeException("Connetion Error");
		}
		else osl.onRawServerResponse(sr);	
		if (sr!=null)
		{
			String json = sr.getResponse();
			int error =  sr.getError();
			if (error!=HttpURLConnection.HTTP_OK)
			{
				if (osl!=null) osl.onStatus(error, null);
			}
			else
			{
				if (flag == STATUS) 
				{
					if (osl!=null)
					{
						Status status = StatusParser.parseStatus(json);
						osl.onStatus(error, status);
					}
				}
				else if (flag == PLAYLIST) { }
				else if (flag == BROWSE) 
				{ 
					if (osl!=null)
					{
						ArrayList<BrowseFile> browse = BrowseParser.parseBrowse(json);
						osl.onBrowse(error, browse);
					}
				}
				else if (flag == TODOSTATUS) getStatus();
				else if (flag == TODOSONG) getStatus();
				else if (flag == TODOPLAYLIST) getPlaylist();
				else if (flag == TODOADDPLAYLIST) addPlaylist();
				else if (flag == TODOADDSONG) addSongAlone();
			}
		}
	}

}
