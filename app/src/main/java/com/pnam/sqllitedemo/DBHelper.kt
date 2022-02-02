package com.pnam.sqllitedemo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
            CREATE TABLE TEST_TABLE(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                NAME NVARCHAR(100) NOT NULL
            )
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        /***ver 1 don't care***/
    }

    internal fun findTestModels(): List<TestModel> {
        val cursor = readableDatabase.rawQuery("SELECT * FROM TEST_TABLE", null)
        cursor.moveToFirst()
        val list: MutableList<TestModel> = mutableListOf()
        while (!cursor.isAfterLast) {
            list.add(TestModel(cursor.getInt(0), cursor.getString(1)))
            cursor.moveToNext()
        }
        return list
    }

    internal fun findTestModelsByName(name: String): List<TestModel> {
        val cursor = readableDatabase.query(
            true,
            "TEST_TABLE",
            null,
            "NAME LIKE ?",
            arrayOf("%${name}%"),
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val list: MutableList<TestModel> = mutableListOf()
        while (!cursor.isAfterLast) {
            list.add(TestModel(cursor.getInt(0), cursor.getString(1)))
            cursor.moveToNext()
        }
        return list
    }

    internal fun insertTestModel(name: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("NAME", name)
        db.insert("TEST_TABLE", null, values)
        db.close()
    }

    internal fun updateTestModel(testModel: TestModel) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("NAME", testModel.name)
        db.update("TEST_TABLE", values, "ID = ?", arrayOf(testModel.id.toString()))
        db.close()
    }

    internal fun deleteTestModel(id: Int) {
        val db = writableDatabase
        db.delete("TEST_TABLE", "ID = ?", arrayOf(id.toString()))
        db.close()
    }

    companion object {
        const val DB_NAME = "DB_NAME"
        const val DB_VERSION = 1
    }
}