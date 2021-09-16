package sample.Model;

import java.util.ArrayList;

public class Stroke {
    String type;
    ArrayList<Double> coords;

    public Stroke(String type, ArrayList<Double> coords) {
        this.type = type.toUpperCase();
        this.coords = coords;
    }

    public Stroke(String type, double[] coords) {
        this.type = type.toUpperCase();
        this.coords = new ArrayList<Double>();
        for (double i: coords) this.coords.add(i);
    }

    public String toString() {
        return type + " " + coords.toString();
    }

    public String getType() {
        return type;
    }

    public ArrayList<Double> getCoords() {
        return coords;
    }
}
