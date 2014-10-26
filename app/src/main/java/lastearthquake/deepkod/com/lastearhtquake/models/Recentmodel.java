package lastearthquake.deepkod.com.lastearhtquake.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Recentmodel {

    @Expose
    private String title;
    @Expose
    private String magnitude;
    @Expose
    private String location;
    @Expose
    private String depth;
    @Expose
    private Double latitude;
    @Expose
    private Double longitude;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @Expose
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}