package com.example.teabreak

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.widget.Toast
import com.example.teabreak.DatabaseInfo.TableInfo


/**
 * DatabaseOperations: "dbOps"
 *
 * @desc This file handles the execution of the creation and deletion queries for the SQLite
 * database, along with the creation and deletion of the individual tea entries within the database.
 * Includes a function to return all teas in the database in the form of an ArrayList
 */

class DatabaseOperations(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "TeaItems.db"
        const val DATABASE_VERSION = 1
    }

    /* Executes the database creation query */
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(DatabaseInfo.CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL(DatabaseInfo.DROP_TABLE_QUERY)
        onCreate(db)
    }


    /**
     * addTea:
     * @desc adds a tea to the SQLite database.  Puts all values into a ContentValues object,
     * then uses this object to insert a new row into the database
     * @param name
     * @param type
     * @param amount
     * @param temp
     * @param time
     */
    fun addTea(context: Context, name: String, type: String, amount: String, temp: Int, time: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(TableInfo.COLUMN_TEA_NAME, name)
        cv.put(TableInfo.COLUMN_TEA_TYPE, type)
        cv.put(TableInfo.COLUMN_TEA_AMOUNT, amount)
        cv.put(TableInfo.COLUMN_TEA_TEMP, temp)
        cv.put(TableInfo.COLUMN_TEA_TIME, time)

        try {
            db.insert(TableInfo.TABLE_NAME, null, cv)
        } catch(e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

        db.close()
    }


    /**
     * deleteTea:
     * @desc deletes a tea from the SQLite database by row ID
     * @param context context passed from the TeaAdapter
     * @param teaID the ID of the tea to be deleted
     * @return Boolean - whether the operation was successful or not
     */
    fun deleteTea(context: Context, teaID: Int): Boolean {
        val query = "DELETE FROM ${TableInfo.TABLE_NAME} WHERE ${BaseColumns._ID} = $teaID"
        val db = this.writableDatabase
        var result = false
        try {
            db.execSQL(query)
            result = true
        } catch(e: Exception) {
            Toast.makeText(context, "deleteTea failed", Toast.LENGTH_SHORT).show()
        }
        db.close()
        return result
    }


    /**
     * getTeas:
     * @desc scrapes the database and returns all teas, including ID column
     * @param context context passed from the MainActivity
     * @return ArrayList<TeaItem>
     */
    fun getTeas(context: Context): ArrayList<TeaItem> {
        val query = "SELECT * FROM " + TableInfo.TABLE_NAME
        val db = this.readableDatabase
        val teas = ArrayList<TeaItem>()

        val cursor = db.rawQuery(query, null)

        if (cursor.count == 0){
            Toast.makeText(context, "No Teas Found", Toast.LENGTH_SHORT).show()
        } else {
            while(cursor.moveToNext()) {
                val tea = TeaItem(
                    cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)),
                    cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_TEA_NAME)),
                    cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_TEA_TYPE)),
                    cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_TEA_AMOUNT)),
                    cursor.getInt(cursor.getColumnIndex(TableInfo.COLUMN_TEA_TEMP)),
                    cursor.getInt(cursor.getColumnIndex(TableInfo.COLUMN_TEA_TIME)),
                    cursor.getBlob(cursor.getColumnIndex(TableInfo.COLUMN_TEA_IMAGE))
                )
                teas.add(tea)
            }
        }
        cursor.close()
        db.close()
        return teas
    }

}