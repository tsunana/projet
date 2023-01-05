package fr.miage.m2.projet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Objects;

//dans la classe build.grade, ajouter implementation 'com.android.support:appcompat-v7:28.0.0'
//j'ai l'impression que ça ne marche pas chez moi, on dirait un pb de compatibilité de versions

public class Sprite implements Serializable {

    private GeoPoint geo_point;
    //private @ServerTimestamp String timestamp;
    private Marker marker;

    private int id;
    private static int incrId = 1;
    private String name;
    // Coordonnées du sprite
    private double latitude;
    private double longitude;


    // Image du sprite
    private byte[] image;
    private int imageId;

    public Sprite(String name, double latitude, double longitude) {
        //id = incrId;
        //incrId++;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

        this.marker = marker;
        setGeo_point(latitude,longitude);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sprite sprite = (Sprite) o;
        return Double.compare(sprite.latitude, latitude) == 0 && Double.compare(sprite.longitude, longitude) == 0 && name.equals(sprite.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return this.id;
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

    public void setGeo_point(Double latitude, Double longitude) {

        this.geo_point = new GeoPoint(latitude, longitude);
    }

    public LatLng getLatLng(){
        LatLng sLatLng = new LatLng(this.getLatitude(), this.getLongitude());
        return sLatLng;
    }

    @Override
    public String toString() {
        return "Sprite{" +

                "name='" + name + '\'' +
                ", geo_point=" + geo_point  +
                '}';
    }





    /*
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    // convert from bitmap to byte array
    public byte[] getBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getImage().compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public Bitmap getImage(){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
     public void setImage(Bitmap image) {
        this.image = getBytes();
    }

     public void draw(Canvas canvas){
        canvas.drawBitmap(this.getImage(), (float) latitude, (float) longitude,null);
    }*/

}

