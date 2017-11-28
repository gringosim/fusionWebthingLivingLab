package es.upm.vlc;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BrowseParser 
{

	private static final String 
	ELEMENT = "element",
		TYPE = "type",
		PATH = "path",
		NAME = "name";
	
	public static ArrayList<BrowseFile> parseBrowse(String file)
	{
		
		JSONObject root;
		try { root = new JSONObject(file); }
		catch (JSONException e) { return null; }
		 ArrayList<BrowseFile> list = new ArrayList<BrowseFile>();
		
		 JSONArray array = root.optJSONArray(ELEMENT);
		 
		 int i;		 
		 
		 for (i=0; i<array.length(); i++)
		 {
			JSONObject node = array.optJSONObject(i);
			String 
				type = node.optString(TYPE),
				path = node.optString(PATH),
				name = node.optString(NAME);
			
			BrowseFile object = new BrowseFile(name, type, path);
			list.add(object);
		 }


		return list;
	}
	
}

