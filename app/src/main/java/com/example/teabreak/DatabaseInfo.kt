package com.example.teabreak

import android.provider.BaseColumns


// DatabaseInfo:
// This file defines the database creation query, deletion query, and schema.

object DatabaseInfo {

    const val CREATE_TABLE_QUERY =
        "CREATE TABLE ${TableInfo.TABLE_NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
        "${TableInfo.COLUMN_TEA_NAME} TEXT," +
        "${TableInfo.COLUMN_TEA_TYPE} TEXT," +
        "${TableInfo.COLUMN_TEA_ORIGIN} TEXT," +
        "${TableInfo.COLUMN_TEA_AMOUNT} TEXT," +
        "${TableInfo.COLUMN_TEA_TEMP} INTEGER," +
        "${TableInfo.COLUMN_TEA_TIME} INTEGER)"

    const val DROP_TABLE_QUERY = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"

    object TableInfo: BaseColumns {
        const val TABLE_NAME = "teaTable"
        const val COLUMN_TEA_NAME = "teaName"
        const val COLUMN_TEA_TYPE = "teaType"
        const val COLUMN_TEA_ORIGIN = "teaOrigin"
        const val COLUMN_TEA_AMOUNT = "teaAmount"
        const val COLUMN_TEA_TEMP = "teaTemp"
        const val COLUMN_TEA_TIME = "teaTime"
    }

}