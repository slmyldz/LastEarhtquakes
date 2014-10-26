package lastearthquake.deepkod.com.lastearhtquake;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by P-User on 25.10.2014.
 */
public class KoeriEarthquakeClient {

    static AsyncHttpClient client = new AsyncHttpClient();
    static String baseUrl = "http://earthquake-report.com/feeds/";

    public static void getLast(AsyncHttpResponseHandler responseHandler){
        client.get("http://www.koeri.boun.edu.tr/sismo/zeqmap/xmlt/son24saat.xml",responseHandler );
    }

}
