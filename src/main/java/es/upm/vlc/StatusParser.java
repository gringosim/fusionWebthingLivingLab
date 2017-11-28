package es.upm.vlc;

import org.json.JSONException;
import org.json.JSONObject;

public class StatusParser
{

	private static final String 
		TIME = "time",
		VOLUME = "volume",
		LENGTH = "length",
		RANDOM = "random",
		STATE = "state",
			PLAYING = "playing",
			PAUSED = "paused",
//			STOPPED = "stopped",
		LOOP = "loop",
		INFORMATION = "information",
			CATEGORY = "category",
				META = "meta",
					ARTWORK = "artwork_url",
					ARTIST = "artist",
					TITLE = "title",
		REPEAT = "repeat";
	
	public static Status parseStatus(String file)
	{
		JSONObject root;
		try { root = new JSONObject(file); }
		catch (JSONException e) { return null; }
		
		PlayerState ps = parseState(root);
		PlayerMode pm = parseMode(root);
		Song s = parseSong(root);
		
		Status status = new Status(s, ps, pm);
		return status;
	}
	
	private static Song parseSong (JSONObject node)
	{
		try
		{
			node = node.getJSONObject(INFORMATION);
			node = node.getJSONObject(CATEGORY);
			node = node.getJSONObject(META);
			String 
				artwork = node.optString(ARTWORK),
				artist = node.optString(ARTIST),
				title = node.optString(TITLE);
			
			Song s = new Song(artist, title, artwork);
			return s;
		}
		catch (JSONException e) { return null; }
	}
	
	private static PlayerState parseState (JSONObject root)
	{
		int
			idstate,
			time = root.optInt(TIME),
			volume = root.optInt(VOLUME),
			length = root.optInt(LENGTH);
		String state = root.optString(STATE);
		if (state.equals(PLAYING)) idstate = PlayerState.PLAYING;
		else if (state.equals(PAUSED)) idstate = PlayerState.PAUSED;
		else idstate = PlayerState.STOPPED;
		PlayerState ps = new PlayerState(idstate, time, volume, length);
		return ps;
	}
	
	private static PlayerMode parseMode (JSONObject root)
	{
		boolean
			random = root.optBoolean(RANDOM),
			loop = root.optBoolean(LOOP),
			repeat = root.optBoolean(REPEAT);
		
		PlayerMode pm = new PlayerMode(random, loop, repeat);
		return pm;
	}
}

