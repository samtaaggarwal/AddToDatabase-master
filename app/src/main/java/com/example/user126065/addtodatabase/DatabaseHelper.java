package com.example.user126065.addtodatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by user126065 on 5/5/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String Database_Name = "names.db";
    public static final String Table_Name = "mytable";
    public DatabaseHelper(Context context)
    {
        super(context, Database_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+ Table_Name + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, newimage blob)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS" +Table_Name);
        onCreate(db);

    }
    public boolean addData(String name,byte[] img)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("newimage", img);
        long result = db.insert(Table_Name,null,contentValues);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    public Cursor getdata()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from " + Table_Name;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_Name +
                " WHERE NAME " + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param id
     * @return
     */
    public Cursor getItemImage(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_Name +
                " WHERE ID "  + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    } /**
 * Updates the name field
 * @param newName
 * @param id
 * @param oldName
 */
public void updateName(String newName, int id, String oldName, String colname)
{
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "UPDATE " + Table_Name + " SET " + colname +
            " = '" + newName + "' WHERE ID "  + " = '" + id + "'" +
            " AND NAME "  + " = '" + oldName + "'";
    //Log.d(TAG, "updateName: query: " + query);
    //Log.d(TAG, "updateName: Setting name to " + newName);
    db.execSQL(query);
}

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + Table_Name + " WHERE ID"
                 + " = '" + id + "'" +
                " AND  NAME" + " = '" + name + "'";
        //Log.d(TAG, "deleteName: query: " + query);
        //Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

    public void updateImage(byte[] newImg, int id, String colname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + Table_Name + " SET " + colname +
                " = '" + newImg + "' WHERE ID "  + " = '" + id + "'";
        //Log.d(TAG, "updateName: query: " + query);
        //Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

}

