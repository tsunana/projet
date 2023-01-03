package fr.miage.m2.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.google.android.gms.maps.model.LatLng;

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

    String[] projectionLat = {

            "latitude",

    };
    String[] projectionLong = {

            "longitude",
    };


    public SpriteDAO(Context context) {
        this.spriteSqLiteDatabase = new SpriteSQLiteDatabase(context, "sprites", null, VERSION_BDD);
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
        return bdd.insert(TABLE_NAME, null, values);
    }

    public Sprite getSpriteByName(String titre) {
        open();
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD
        String selection = "NAME = ?";
        String[] selectionArgs = {titre};
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

    public Sprite getLat(String id) {
        open();
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD
        String selection = "ID = ?";
        String[] selectionArgs = {id};
        Cursor cursor = bdd.query(
                TABLE_NAME,
                projectionLat,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        System.out.println("ok");
        return cursorToName(cursor);
    }
    public Sprite getLong(String id) {
        open();
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD
        String selection = "ID = ?";
        String[] selectionArgs = {id};
        Cursor cursor = bdd.query(
                TABLE_NAME,
                projectionLong,
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