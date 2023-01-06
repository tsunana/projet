package fr.miage.m2.projet;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.LinkedList;
import java.util.Random;

public class SpriteDAO extends AppCompatActivity {
    private final static int VERSION_BDD = 1;
    private final static String TABLE_NAME = "sprites";
    private SQLiteDatabase bdd;
    private SpriteSQLiteDatabase spriteSqLiteDatabase;
    private Intent i_end;

    String[] projection = {
            "id",
            "name",
            "latitude",
            "longitude",
    };



    private static Sprite lisa = new Sprite("sprite1", 43.3114334, -0.3843101);
    private static Sprite ylan = new Sprite("sprite2", 43.31139373779297,-0.3854704797267914);
    private static Sprite maroua = new Sprite("sprite3", 43.31139373779297,-0.3854704797267914);
    private static Sprite hassan = new Sprite("sprite4", 43.30260467529297,-0.3971642553806305);
    private static Sprite dervin = new Sprite("sprite5", 43.30260467529297,-0.3971642553806305);

    private static final Random RANDOM = new Random();


    private static final LinkedList<Sprite> SPRITE = new LinkedList<>();
    static {
        SPRITE.add(lisa);
        SPRITE.add(ylan);
        //SPRITE.add(maroua);
        //SPRITE.add(hassan);
        //SPRITE.add(dervin);

    }



    public SpriteDAO(Context context) {
        this.spriteSqLiteDatabase = new SpriteSQLiteDatabase(context, "sprites", null, VERSION_BDD);
        clean();
        if(!SPRITE.isEmpty()){
            Sprite sprite = getRandomSprite();
            addSprite(sprite);
        }else{
            clean();

        }





        //addSprite(jeje);


    }


    private void startSprite(){

    }











    public static Sprite getRandomSprite() {
        int index = RANDOM.nextInt(SPRITE.size());
        Sprite monType = SPRITE.get(index);
        SPRITE.remove(index);
        return monType;
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

        values.put("id",1);
        values.put("name", sprite.getName());
        values.put("latitude", sprite.getLatitude());
        values.put("longitude", sprite.getLongitude());
        //on insère l'objet dans la BDD via le ContentValues
        System.out.println("ok added");
        System.out.println(sprite.getId());

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


    public void clean(){
        open();
        bdd.delete(TABLE_NAME,null,null);
        close();
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