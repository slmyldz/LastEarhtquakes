package lastearthquake.deepkod.com.lastearhtquake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lastearthquake.deepkod.com.lastearhtquake.models.Feature;
import lastearthquake.deepkod.com.lastearhtquake.models.GeojsonModel;
import lastearthquake.deepkod.com.lastearhtquake.models.KoeriEarthquakemodel;
import lastearthquake.deepkod.com.lastearhtquake.models.Recentmodel;


public class MyActivity extends Activity {
    private GoogleMap googleMap;
    Recentmodel[] recent;
    GeojsonModel geojson;
    ArrayList<KoeriEarthquakemodel> koeriEarthquakemodels = new ArrayList<KoeriEarthquakemodel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_my);

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            googleMap.setBuildingsEnabled(true);
            googleMap.setMyLocationEnabled(true);
            googleMap .getUiSettings().setZoomControlsEnabled(false);
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.infovindows, null);
                    TextView location =(TextView) v.findViewById(R.id.location);
                    TextView depth =(TextView) v.findViewById(R.id.depth);
                    TextView magnitude =(TextView) v.findViewById(R.id.magnitude);
                    TextView latlong =(TextView) v.findViewById(R.id.latlong);
                    TextView datetime =(TextView) v.findViewById(R.id.dateTime);

                    Double longitude = marker.getPosition().longitude;
                    Double latitude = marker.getPosition().latitude;
                    for(Recentmodel rm : recent){
                        if(rm.getLatitude()+rm.getLongitude()==longitude+latitude){
                            String loc = rm.getLocation();
                            location.setText(rm.getLocation().toUpperCase());
                            depth.setText("Depth:"+rm.getDepth());
                            magnitude.setText(rm.getMagnitude());
                            latlong.setText("Location: "+rm.getLatitude()+" , "+rm.getLongitude());
                            datetime.setText("Time: "+rm.getDateTime());
                        }
                    }

                    for(Feature g : geojson.getFeatures()){
                        if(g.getGeometry().getCoordinates().get(0)+g.getGeometry().getCoordinates().get(1)==longitude+latitude){
                            location.setText(g.getProperties().getPlace().toUpperCase());
                            depth.setText("Depth:" + g.getGeometry().getCoordinates().get(2));
                            magnitude.setText(g.getProperties().getMag()+"");
                            latlong.setText("Location: "+g.getGeometry().getCoordinates().get(0)+" , "+g.getGeometry().getCoordinates().get(1));
                            String dateTimeAsString = new DateTime( g.getProperties().getTime(), DateTimeZone.UTC ).toString();
                            datetime.setText("Time: "+dateTimeAsString);


                        }



                    }
                    for(KoeriEarthquakemodel g  : koeriEarthquakemodels) {
                        if (Double.parseDouble(g.getLat()) + Double.parseDouble(g.getLng()) == longitude + latitude) {
                            location.setText(g.getLokasyon().toUpperCase());
                            depth.setText("Depth:" + g.getDepth());
                            magnitude.setText(g.getMag());
                            latlong.setText("Location: " + g.getLat() + " , " + g.getLng());
                            datetime.setText("Time: " + g.getTime());
                        }
                    }
                        return v;
                }
            });
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Double longitude = marker.getPosition().longitude;
                    Double latitude = marker.getPosition().latitude;
                    for(Recentmodel rm : recent){
                        if(rm.getLatitude()+rm.getLongitude()==longitude+latitude){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rm.getLink()));
                            startActivity(browserIntent);
                        }
                    }
                    for(Feature rm : geojson.getFeatures()){
                        if(rm.getGeometry().getCoordinates().get(0)+rm.getGeometry().getCoordinates().get(1)==longitude+latitude){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rm.getProperties().getUrl()));
                            startActivity(browserIntent);
                        }
                    }
                }
            });
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    void addMarker(Recentmodel model){
        MarkerOptions marker = new MarkerOptions().position(new LatLng(model.getLatitude(),model.getLongitude()))
                .title(model.getLocation()+" "+model.getMagnitude()+"\n"+model.getLink())
                .flat(true);
        if(Double.parseDouble(model.getMagnitude())>=6.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        else if(Double.parseDouble(model.getMagnitude())>=5.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        else if(Double.parseDouble(model.getMagnitude())>=4.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        else if(Double.parseDouble(model.getMagnitude())>=2.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        else if(Double.parseDouble(model.getMagnitude())>=1.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        else
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));






        googleMap.addMarker(marker);
    }
    void addMarker(Feature model){

        MarkerOptions marker = new MarkerOptions().position(new LatLng(model.getGeometry().getCoordinates().get(1),model.getGeometry().getCoordinates().get(0)))
                .flat(true);

        if(model.getProperties().getMag()>=6.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        else if(model.getProperties().getMag()>=5.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        else if(model.getProperties().getMag()>=4.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        else if(model.getProperties().getMag()>=2.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        else if(model.getProperties().getMag()>=1.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        else
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));






        googleMap.addMarker(marker);
    }

    void addMarker(String date,String loc, String lat, String lng, String mag, String depth){
        MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)))
                     .flat(true);
        if(Double.parseDouble(mag)>=6.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        else if(Double.parseDouble(mag)>=5.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        else if(Double.parseDouble(mag)>=4.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        else if(Double.parseDouble(mag)>=2.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        else if(Double.parseDouble(mag)>=1.0)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        else
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));






        googleMap.addMarker(marker);
    }
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
        LastEarthquakeClient.getLast(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                String content = new String(bytes);
                Gson gs = new Gson();
                recent = gs.fromJson(content, Recentmodel[].class);
                for(Recentmodel model : recent){
                    addMarker(model);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {


            }
        });

        UsgsEarthquakeClient.getLast(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);
            Gson gs = new Gson();
                geojson= gs.fromJson(content,GeojsonModel.class);
                for(Feature f : geojson.getFeatures()){

                    addMarker(f);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        },50);
        KoeriEarthquakeClient.getLast(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int b, Header[] headers, byte[] bytes) {

                InputStream bis = new ByteArrayInputStream(bytes);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document dom =db.parse(bis);
                    Element docEle = dom.getDocumentElement();
                    NodeList nl = docEle.getElementsByTagName("earhquake");
                    if(nl != null && nl.getLength() > 0) {

                        for(int i = 0 ; i < nl.getLength();i++) {

                            //get the employee element
                            Element el = (Element) nl.item(i);
                            String time = el.getAttribute("name");
                            String lokasyon = el.getAttribute("lokasyon");
                            String lat = el.getAttribute("lat");
                            String lng = el.getAttribute("lng");
                            String mag = el.getAttribute("mag");
                            String Depth = el.getAttribute("Depth");

                            koeriEarthquakemodels.add(new KoeriEarthquakemodel(time, lokasyon, lat, lng, mag, Depth));
                            addMarker(time, lokasyon, lat, lng, mag, Depth);




                        }


                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(MyActivity.this,"KoeriEarthquakeError",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
