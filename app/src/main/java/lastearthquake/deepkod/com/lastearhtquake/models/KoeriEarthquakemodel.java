package lastearthquake.deepkod.com.lastearhtquake.models;

/**
 * Created by P-User on 26.10.2014.
 */
public class KoeriEarthquakemodel {
    private String time ;
    private String lokasyon;
    private String lat ;
    private String lng;
    private String mag ;
    private String Depth ;

    public KoeriEarthquakemodel(
            String time,
            String lokasyon,
            String lat,
            String lng,
            String mag,
            String Depth){
        super();
        this.setDepth(Depth);
        this.setMag(mag);
        this.setLokasyon(lokasyon);
        this.setLat(lat);
        this.setLng(lng);
        this.setTime(time);

    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLokasyon() {
        return lokasyon;
    }

    public void setLokasyon(String lokasyon) {
        this.lokasyon = lokasyon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public String getDepth() {
        return Depth;
    }

    public void setDepth(String depth) {
        Depth = depth;
    }



}
