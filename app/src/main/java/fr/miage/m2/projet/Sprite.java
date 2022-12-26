package fr.miage.m2.projet;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

public class Sprite {
    private int id;
    private String name;
    private GeoPoint geo_point;
    private @ServerTimestamp String timestamp;

    public Sprite(int id, String name, GeoPoint geo_point, String timestamp) {
        this.id = id;
        this.name = name;
        this.geo_point = geo_point;
        this.timestamp = timestamp;
    }

    public Sprite() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", geo_point=" + geo_point +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
