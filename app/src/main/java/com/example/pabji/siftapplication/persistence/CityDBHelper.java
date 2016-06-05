package com.example.pabji.siftapplication.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pabji.siftapplication.models.Building;

import java.util.ArrayList;

/**
 * Created by JoseFelix on 16/05/2016.
 */
public class CityDBHelper {

    private static final String TAG = "COSTUME_DBHELPER";

    public static long insertBuilding(SQLiteDatabase db,String intro, String name, String description, String url_image, String latitud, String longitud, String id){

        ContentValues newCity = new ContentValues();
        newCity.put(CitySQLiteOpenHelper.KEY_NAME, name);
        newCity.put(CitySQLiteOpenHelper.KEY_DESCRIPTION, description);
        newCity.put(CitySQLiteOpenHelper.KEY_URL_IMAGE, url_image);
        newCity.put(CitySQLiteOpenHelper.KEY_LATITUD, latitud);
        newCity.put(CitySQLiteOpenHelper.KEY_LONGITUD, longitud);
        newCity.put(CitySQLiteOpenHelper.KEY_INTRO, intro);
        newCity.put(CitySQLiteOpenHelper.KEY_ID, id);

        long rows = db.insert(CitySQLiteOpenHelper.DATABASE_TABLE, null, newCity);
        Log.d(TAG,"rows insertadas: " + rows);
        return rows;
    }

   // public static void updateBuilding(){

 //   }
    public static void deleteBuilding(SQLiteDatabase db, String name){

        String where = CitySQLiteOpenHelper.DATABASE_NAME  + "=" + "'" + name + "'";
        int rows = db.delete(CitySQLiteOpenHelper.DATABASE_NAME, where, null);

    }

    public static ArrayList<Building> getBuildings(SQLiteDatabase db){


        String[] campos = new String[]{CitySQLiteOpenHelper.KEY_NAME, CitySQLiteOpenHelper.KEY_DESCRIPTION,
                CitySQLiteOpenHelper.KEY_URL_IMAGE,CitySQLiteOpenHelper.KEY_ID,CitySQLiteOpenHelper.KEY_LATITUD,CitySQLiteOpenHelper.KEY_LONGITUD,CitySQLiteOpenHelper.KEY_INTRO};

        Cursor c = db.query(CitySQLiteOpenHelper.DATABASE_TABLE, campos, null, null, null, null,
                null);

        // Recorremos los resultados para mostrarlos en pantalla
        //tvResultado.setText("");
        ArrayList<Building> buildings = new ArrayList<>();
        if (c.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                String name = c.getString(0);
                String description = c.getString(1);
                String url_image = c.getString(2);
                String id = c.getString(3);
                Double latitud = Double.valueOf(c.getString(4));
                Double longitud = Double.valueOf(c.getString(5));
                String intro = c.getString(6);

                Building building = new Building(description,name,latitud,longitud,url_image,id,intro);
                buildings.add(building);
              //  Log.d("Conseguimos: name: " + name, "category: " + category + "materials: " + materials + "steps: " + steps + "prize: " + prize + "uri_image: " + uri_image + "\n");
            } while (c.moveToNext());
        }
        c.close();

        return buildings;
    }
}
