
package lastearthquake.deepkod.com.lastearhtquake.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class Geometry {

    @Expose
    private String type;
    @Expose
    private List<Double> coordinates = new ArrayList<Double>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

}
