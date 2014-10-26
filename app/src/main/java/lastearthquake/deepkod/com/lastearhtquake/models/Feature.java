
package lastearthquake.deepkod.com.lastearhtquake.models;

import com.google.gson.annotations.Expose;



public class Feature {

    @Expose
    private String type;
    @Expose
    private Properties properties;
    @Expose
    private Geometry geometry;
    @Expose
    private String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
