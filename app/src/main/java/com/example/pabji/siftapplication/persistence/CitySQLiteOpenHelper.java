package com.example.pabji.siftapplication.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JoseFelix on 16/05/2016.
 */
public class CitySQLiteOpenHelper extends SQLiteOpenHelper {

    private static CitySQLiteOpenHelper instance;

    public static final String DATABASE_NAME = "cityCatchedDataBase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "cityCatchedTable";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_URL_IMAGE = "url_image";
    public static final String KEY_LONGITUD = "longitud";
    public static final String KEY_LATITUD = "latitud";
    public static final String KEY_INTRO = "intro";


    //SQL statement for create table costume  //Comprobar que sea con mayusculas
    String citySqlCreate = "CREATE TABLE " + DATABASE_TABLE + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_LATITUD + " TEXT, "
            + KEY_LONGITUD + " TEXT, "
            + KEY_NAME + " TEXT unique, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_INTRO + " TEXT, "
            + KEY_URL_IMAGE + " TEXT)";



    public static synchronized CitySQLiteOpenHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        if(instance==null){
            instance = new CitySQLiteOpenHelper(context, name, factory, version);
        }
        return instance;
    }


    private CitySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(citySqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL(citySqlCreate);
    }
}
