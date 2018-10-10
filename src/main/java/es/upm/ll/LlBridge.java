package es.upm.ll;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.lang.String;
import javax.swing.SwingWorker;

import es.upm.vlc.ServerResponse;
import javax.swing.SwingWorker;


public class LlBridge implements ServiceInterface {
    public String generateDomain(){
        String serviceDomain = ""; //Define service domain (HTTPS)
        return serviceDomain;
    };
    public String defineService(){
        String serviceID = "";
        return serviceID;
    };
    public String adequateParams(){
        String value="";
        return value;
    };
    public String generateDbDomain(){
        String dbHost = "192.168.1.168";
        return dbHost;
    };
}