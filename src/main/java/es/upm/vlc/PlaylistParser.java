package es.upm.vlc;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaylistParser 
{
	private static final String 
	CHILDREN = "children",
			NAME = "name",
			DURATION = "duration";
	
	public static ArrayList<PlaylistFile> parsePlaylist(String file)
	{
		
		JSONObject root;
		try { root = new JSONObject(file); }
		catch (JSONException e) { return null; }
		 ArrayList<PlaylistFile> playlist = new ArrayList<PlaylistFile>();
		
		 JSONArray array = root.optJSONArray(CHILDREN);
		 root = array.optJSONObject(0);
		 array = root.optJSONArray(CHILDREN);
		 
		 int i;		 
		 
		 for (i=0; i<array.length(); i++)
		 {
			JSONObject node = array.optJSONObject(i);
			String 	name = node.optString(NAME);
			int duration = node.optInt(DURATION);
			
			PlaylistFile object = new PlaylistFile(name, duration);
			playlist.add(object);
		 }

		return playlist;
	}
}


//  {
//    "ro":"rw",
//    "type":"node",
//    "name":"Sin definir",
//    "id":"1",
//    "children":[{
//        "ro":"ro",
//        "type":"node",
//        "name":"Lista de reproducción",
//        "id":"2",
//        "children":[{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"Jason Mraz - Im Yours",
//            "id":"6",
//            "duration":243,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/Jason%20Mraz%20-%20Im%20Yours.mp3"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"Sam Tsui & Christina Grimmie",
//            "id":"7",
//            "duration":268,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/Just%20A%20Dream%20by%20Nelly%20-%20Sam%20Tsui%20&%20Christina%20Grimmie.mp3"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"Robe con Fito & Fitipaldis",
//            "id":"8",
//            "duration":223,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/Ni%20negro%20ni%20blanco%20-%20Robe%20con%20Fito%20&%20Fitipaldis.mp3"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"Natteravn",
//            "id":"9",
//            "duration":237,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/Rasmus%20Seebach%20-%20Natteravn.mp3"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"Smoke Weed Everyday (Hedegaard Remix) Unoffical Music video",
//            "id":"10",
//            "duration":185,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/Smoke%20Weed%20Everyday%20(Hedegaard%20Remix)%20Unoffical%20Music%20video.mp3"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"No Tomorrow",
//            "id":"11",
//            "duration":171,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/02%20-%20ORSON%20-%20No%20Tomorrow.mp3",
//            "current":"current"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"Majareta",
//            "id":"12",
//            "duration":169,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/02%20La%20Fuga%20-%20Majareta.mp3"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"Baby Blue",
//            "id":"13",
//            "duration":215,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/Baby%20Blue%20-%20Badfinger.mp3"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"Old time Rock n Roll with lyrics",
//            "id":"14",
//            "duration":189,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/Bob%20Seger%20-%20Old%20time%20Rock%20n%20Roll%20with%20lyrics.mp3"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"L'Amour Toujours ( Official Video )",
//            "id":"15",
//            "duration":242,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/Gigi%20D'Agostino%20-%20L'Amour%20Toujours%20(%20Official%20Video%20).mp3"
//          },{
//            "ro":"rw",
//            "type":"leaf",
//            "name":"You Make My Dreams Come True",
//            "id":"16",
//            "duration":184,
//            "uri":"file:///C:/Users/l.salvador/PFC%20Lucia/Musica/Hall%20&%20Oates%20~%20You%20Make%20My%20Dreams%20Come%20True.mp3"
//          }]
//      },{
//        "ro":"ro",
//        "type":"node",
//        "name":"Biblioteca multimedia",
//        "id":"3",
//        "children":[]
//      }]
//  }
