package com.github.maximkirko.training_2017_android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.maximkirko.training_2017_android.model.User;

import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private final static int DB_VER = 1;
    public static final String DB_NAME = "vk-simple_chat.db";
    public static final String TABLE_NAME = "user";
    private final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (id integer PRIMARY KEY AUTOINCREMENT, first_name text, last_name text, photo_100 text, online boolean);";
    private final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // region DB fields
    public static final String DB_FIELD_USER_ID = "_id";
    public static final String DB_FIELD_FIRST_NAME = "first_name";
    public static final String DB_FIELD_LAST_NAME = "last_name";
    public static final String DB_FIELD_PHOTO_100 = "photo_100";
    public static final String DB_FIELD_ONLINE = "online";
    // endregion

    private List<User> friends;

    public DBHelper(Context context, List<User> friends) {
        super(context, DB_NAME, null, DB_VER);
        this.friends = friends;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        fillData(db);
        getDBRows(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    private void fillData(SQLiteDatabase db) {
        if (db != null) {
            ContentValues values;
            for (User user : friends) {
                values = new ContentValues();

                values.put("id", user.getId());
                values.put("first_name", user.getFirst_name());
                values.put("last_name", user.getLast_name());
                values.put("photo_100", user.getPhoto_100());
                values.put("online", user.isOnline());

                db.insert(TABLE_NAME, null, values);
            }
        } else {
            Log.d("DB", "null");
        }
    }

    private void getDBRows(SQLiteDatabase db) {
        Log.d("DB", "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("user", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int firstNameColIndex = c.getColumnIndex("first_name");
            int lastNameColIndex = c.getColumnIndex("last_name");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("DB",
                        "ID = " + c.getInt(idColIndex) +
                                ", first_name = " + c.getString(firstNameColIndex) +
                                ", last_name = " + c.getString(lastNameColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("DB", "0 rows");
        c.close();
    }
}