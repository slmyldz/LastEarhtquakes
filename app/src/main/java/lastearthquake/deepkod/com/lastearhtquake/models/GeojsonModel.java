
package lastearthquake.deepkod.com.lastearhtquake.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class GeojsonModel {

    @Expose
    private String type;
    @Expose
    private Metadata metadata;
    @Expose
    private List<Feature> features = new ArrayList<Feature>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

}
