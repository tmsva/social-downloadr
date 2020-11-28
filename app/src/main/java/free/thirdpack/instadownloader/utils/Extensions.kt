package free.thirdpack.instadownloader.utils

import android.database.Cursor

fun Cursor.getString(columnIndex: String): String? = getString(getColumnIndex(columnIndex))

fun Cursor.getInt(columnIndex: String): Int = getInt(getColumnIndex(columnIndex))

fun Cursor.getLong(columnIndex: String): Long = getLong(getColumnIndex(columnIndex))