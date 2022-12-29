package fr.miage.m2.projet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;

//dans la classe build.grade, ajouter implementation 'com.android.support:appcompat-v7:28.0.0'
//j'ai l'impression que ça ne marche pas chez moi, on dirait un pb de compatibilité de versions

public class Sprite {

    private GeoPoint geo_point;
    //private @ServerTimestamp String timestamp;
    private Marker marker;

    private int id;
    private static int incrId = 1;
    private String name;
    // Coordonnées du sprite
    private double latitude;
    private double longitude;

    // Largeur et hauteur du sprite
    //private int width;
    //private int height;

    // Image du sprite
    private byte[] image;

    public Sprite(String name, double latitude, double longitude, byte[] image) {
        this.id = incrId;
        incrId++;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        // convert from byte array to bitmap

        this.marker = marker;
        setGeo_point(latitude,longitude);

    }
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




    public void setImage(Bitmap image) {
        this.image = getBytes();
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

    public void draw(Canvas canvas){
        canvas.drawBitmap(this.getImage(), (float) latitude, (float) longitude,null);
    }
    @Override
    public String toString() {
        return "Sprite{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", geo_point=" + geo_point  +
                '}';
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }


    public Marker getMarker(){
        return this.marker;
    }
}

