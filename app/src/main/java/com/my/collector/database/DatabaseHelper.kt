package com.my.collector.database

import android.database.sqlite.SQLiteOpenHelper
import com.my.collector.database.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 3) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, MAX_NUMBER INTEGER)")
        db.execSQL("CREATE TABLE " + ITEMS_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT,DESCRIPTION TEXT, IMAGE BYTE,FOREIGN_ID INTERGER, "
                + "FOREIGN KEY (FOREIGN_ID) REFERENCES " + CATEGORY_TABLE_NAME + "(ID))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE_NAME)
        onCreate(db)
    }

    fun insert(name: String?, number: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("NAME", name)
        contentValues.put("MAX_NUMBER", number)
        val result = db.insert(CATEGORY_TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun insert(name: String?, description: String?, image: ByteArray?, foreign_key: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("description", description)
        contentValues.put("image", image)
        contentValues.put("foreign_id", foreign_key)
        val result = db.insert(ITEMS_TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun select(): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("select * from $CATEGORY_TABLE_NAME", null)
    }

    fun select(id: Int): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("select * from $ITEMS_TABLE_NAME WHERE foreign_id = ?", arrayOf(id.toString()))
    }

    fun selectSingleItem(id: Int): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("select * from $CATEGORY_TABLE_NAME WHERE id = ?", arrayOf(id.toString()))
    }

    companion object {
        private const val DATABASE_NAME = "collector.db"
        private const val CATEGORY_TABLE_NAME = "categories_table"
        private const val ITEMS_TABLE_NAME = "item_table"
    }
}