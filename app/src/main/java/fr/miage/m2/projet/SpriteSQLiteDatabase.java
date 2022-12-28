package fr.miage.m2.projet;


import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SpriteSQLiteDatabase extends SQLiteOpenHelper {
    private String query = "CREATE TABLE sprites(id INTEGER PRIMARY KEY AUTOINCREMENT,"+ "name VARCHAR(100), latitude DOUBLE, longitude DOUBLE, img BLOB)";
    public SpriteSQLiteDatabase(@Nullable Context context, @Nullable String name, @Nullable
            android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase sqLiteDatabase) {
        //On crée la base en exécutant la requête de création
        sqLiteDatabase.execSQL(query);
    }
    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase sqLiteDatabase, int i, int i1){
        //On peut fait ce qu'on veut ici mais on va juste supprimer la table et la recréer
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS sprites");

        // create new table
        onCreate(sqLiteDatabase);
    }







}
