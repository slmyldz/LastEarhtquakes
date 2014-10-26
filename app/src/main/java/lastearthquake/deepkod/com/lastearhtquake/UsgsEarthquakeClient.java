package lastearthquake.deepkod.com.lastearhtquake;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by P-User on 25.10.2014.
 */
public class UsgsEarthquakeClient {

    static AsyncHttpClient client = new AsyncHttpClient();
    static String baseUrl = "http://earthquake-report.com/feeds/";
    
    public static void getLast(AsyncHttpResponseHandler responseHandler,int limit ){
        client.get("http://comcat.cr.usgs.gov/fdsnws/event/1/query?limit="+limit+"&format=geojson",responseHandler );
    }

}
