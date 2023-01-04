package fr.miage.m2.projet;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class SpriteDAO {
    private final static int VERSION_BDD = 1;
    private final static String TABLE_NAME = "sprites";
    private SQLiteDatabase bdd;
    private SpriteSQLiteDatabase spriteSqLiteDatabase;
    String[] projection = {
            "id",
            "name",
            "latitude",
            "longitude",
    };



    GeoPoint geoPoint1 = new GeoPoint(43.3114334,-0.3843101);
    GeoPoint geoPoint2 = new GeoPoint(43.30260467529297,-0.3971642553806305);
    Sprite asptt = new Sprite("ASPTT", geoPoint1.getLatitude(), geoPoint1.getLongitude());
    Sprite jeje = new Sprite("jeje", geoPoint2.getLatitude(),geoPoint2.getLongitude());








    public SpriteDAO(Context context) {
        this.spriteSqLiteDatabase = new SpriteSQLiteDatabase(context, "sprites", null, VERSION_BDD);

        addSprite(asptt);
        addSprite(jeje);


    }

    public void open() {
        //On ouvre la BDD en écriture
        //..
        this.bdd = spriteSqLiteDatabase.getWritableDatabase();

    }

    public void close() {
        //On ferme la BDD
        //…
        spriteSqLiteDatabase.close();


    }

    public long addSprite(Sprite sprite) {
        open();
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        values.put("name", sprite.getName());
        values.put("latitude", sprite.getLatitude());
        values.put("longitude", sprite.getLongitude());
        //on insère l'objet dans la BDD via le ContentValues
        //System.out.println("ok added");
        //System.out.println(getId("ASPTT"));

        return bdd.insert(TABLE_NAME, null, values);
    }

    public Sprite getSpriteById(int idS) {
        open();
        String id = String.valueOf(idS);
        //Récupère dans un Cursor les valeur correspondant à un sprite contenu dans la BDD
        String selection = "ID = ?";
        String[] selectionArgs = {id};
        Cursor cursor = bdd.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        System.out.println("ok");
        return cursorToName(cursor);
    }




    public Sprite cursorToName(Cursor cursor) {

        if (cursor.getCount() == 0)
            return null;
        //…s
        while (cursor.moveToNext()) {
            //le numéro ISBN est a la postion 1 du tableau (colonne)
            //le titre est a la position  2 du
            return new Sprite(cursor.getString(1), cursor.getDouble(2),cursor.getDouble(3));

            //list.add(livre)
        }

        return null;
    }


    @SuppressLint("Range")
    public int getId(String name){
       open();

        String selection = "NAME = ?";
        String[] selectionArgs = {name};

        Cursor cursor = bdd.query(
                TABLE_NAME,   // La table à query
                new String[]{"id"}, // Les colonnes à retourner
                selection,         // La colonne pour la clause WHERE
                selectionArgs,     // Les valeurs pour la clause WHERE
                null,              // Ne pas group by
                null,              // Ne pas filter par row groups
                null);             // Ne pas trier

        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));

        }
        cursor.close();
        return id;

    }

    public long addEntry(Sprite sprite, byte[] image) throws SQLiteException {
        open();
        ContentValues values = new ContentValues();
        values.put("name", sprite.getName());
        values.put("lattitude", sprite.getName());
        values.put("longitude", sprite.getName());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_NAME, null, values);

    }


}