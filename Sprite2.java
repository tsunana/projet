package fr.miage.m2.projet;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

//dans la classe build.grade, ajouter

public class Sprite2 {
    private int id;
    private String name;
    // Coordonnées du sprite
    private float x;
    private float y;

    // Largeur et hauteur du sprite
    private int width;
    private int height;

    // Image du sprite
    private Bitmap image;

    public Sprite2(int id, String name, float x, float y, int width, int height, Bitmap image) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
        this.width = width;
        this.height = height;
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

    public float getX() {
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
                ", geo_point=" + geo_point +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

