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

    // Largeur et hauteur du sprite
    //private int width;
    //private int height;

    // Image du sprite
    private byte[] image;
    private int imageId;

    public Sprite(String name, double latitude, double longitude) {
        //this.id = incrId;
        //incrId++;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

        this.marker = marker;
        setGeo_point(latitude,longitude);

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
    public void setMarker(Marker marker) {
        this.marker = marker;
    }


    public Marker getMarker(){
        return this.marker;
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "id=" + id +
                ", name='" + name + '\'' +
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

