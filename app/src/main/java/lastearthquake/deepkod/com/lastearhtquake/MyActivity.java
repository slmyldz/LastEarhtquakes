package lastearthquake.deepkod.com.lastearhtquake;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        try{
            marker.icon(BitmapDescriptorFactory.fromBitmap(getCircle(Double.parseDouble(model.getMagnitude()))));
            googleMap.addMarker(marker);

        }catch (IllegalArgumentException e ){

        }





   }
    void addMarker(Feature model){

        MarkerOptions marker = new MarkerOptions().position(new LatLng(model.getGeometry().getCoordinates().get(1),model.getGeometry().getCoordinates().get(0)))
                .flat(true);

        try{
        marker.icon(BitmapDescriptorFactory.fromBitmap(getCircle(model.getProperties().getMag())));
        googleMap.addMarker(marker);

        }catch (IllegalArgumentException e ){

        }




    }

    void addMarker(String date,String loc, String lat, String lng, String mag, String depth){
        MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)))
                     .flat(true);
      try{
            marker.icon(BitmapDescriptorFactory.fromBitmap(getCircle(Double.parseDouble(mag))));
            googleMap.addMarker(marker);

        }catch (IllegalArgumentException e ){

        }




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


   Bitmap getCircle(Double mag) throws IllegalArgumentException {

       int wh = (int)Math.floor(mag)*10;
       int[] color = new int[wh*wh];
       int c;
       if(mag>=6.0){
        c= Color.rgb(0,0,0);
       }
       else if(mag>=6.0){
        c= Color.rgb(255-wh+20,0,0);
       }
       else if(mag>=4.0){
        c= Color.rgb(85-wh,0,255-wh);
       }else if(mag>=4.0){
        c= Color.rgb(255-wh,0,0);
       }else if(mag>=3.0){
        c= Color.rgb(0,208-wh,255-wh);
       }else if(mag>=2.0){
        c= Color.rgb(209-wh,247-wh,255-wh);
        }else{
        c= Color.rgb(255-wh,255-wh,255-wh);
       }

       for(int i = 0 ;i<color.length;i++){
           color[i]=c;
       }


        Bitmap a =Bitmap.createBitmap(color,wh,wh, Bitmap.Config.RGB_565);
        Canvas can = new Canvas();
        int radius = 100;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#CD5C5C"));
        //can.drawCircle(Float.parseFloat(mag),Float.parseFloat(mag),radius,paint);



        return getRoundedBitmap(a) ;
    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
