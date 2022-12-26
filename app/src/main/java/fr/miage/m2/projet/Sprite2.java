package fr.miage.m2.projet;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

//dans la classe build.grade, ajouter implementation 'com.android.support:appcompat-v7:28.0.0'
//j'ai l'impression que ça ne marche pas chez moi, on dirait un pb de compatibilité de versions

public class Sprite2 {

    private GeoPoint geo_point;
    private @ServerTimestamp String timestamp;

    private int id;
    private String name;
    // Coordonnées du sprite
    private float x;
    private float y;

    // Largeur et hauteur du sprite
    //private int width;
    //private int height;

    // Image du sprite
    private Bitmap image;

    public Sprite2(int id, String name, GeoPoint geo_point, Bitmap image) {
        this.id = id;
        this.name = name;
        this.geo_point = geo_point;
        //this.x = x;
        //this.y = y;
        this.timestamp = timestamp;
        //this.width = width;
        //this.height = height;
        this.image = image;
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

    /*public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
*/
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image,x,y,null);
    }
    @Override
    public String toString() {
        return "Sprite{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", geo_point=" + geo_point  +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

