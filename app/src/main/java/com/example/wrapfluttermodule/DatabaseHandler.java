package com.example.wrapfluttermodule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wrapfluttermodule.Crypto.CryptoHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cryptoManager";
    private static final String TABLE_CRYPTOS = "cryptos";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "coinName";
    private static final String KEY_COIN_VALUE = "priceValue";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CRYPTO_TABLE = "CREATE TABLE " + TABLE_CRYPTOS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_COIN_VALUE + " TEXT)";
        db.execSQL(CREATE_CRYPTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRYPTOS);
        onCreate(db);
    }

    void insertJSON (CryptoHelper crypto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, crypto.getCryptoName());
        values.put(KEY_COIN_VALUE, crypto.getPriceValue());

        db.insert(TABLE_CRYPTOS, null, values);
        db.close();

    }

    /* isNotEmpty()
    * mCursor.moveToFirst() -> Returns a boolean of whether it successfully found an element or not.
    * Use it to move to the first row in the cursor and at the same time check if a row actually exists.
    * */
    public Boolean isNotEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_CRYPTOS, null);
        Boolean rowExists;

        if (mCursor.moveToFirst()) {
            // DO SOMETHING WITH CURSOR
            rowExists = true;

        } else {
            // I AM EMPTY
            rowExists = false;
        }
        return rowExists;
    }

    void updateJSON(CryptoHelper crypto, int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        final String where = KEY_ID + " = " + ID;

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, crypto.getCryptoName());
        values.put(KEY_COIN_VALUE, crypto.getPriceValue());

        // updating row
//        db.update(TABLE_CRYPTOS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(crypto.getID()) });
        db.update(TABLE_CRYPTOS, values, where, null);

    }

    void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CRYPTOS, null, null);
        getAllCurrencies().clear();
    }

    void updateAll(CryptoHelper crypto) {
        SQLiteDatabase db = this.getWritableDatabase();
        int ID = 0;
        String where = KEY_ID + " = " + ID;
        String whereArgs[] = { String.valueOf(crypto.getID()) };

        String selectQuery = "SELECT * FROM " + TABLE_CRYPTOS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        ContentValues values = new ContentValues();

        if (cursor.moveToFirst()) {
            do {
                values.put(KEY_NAME, crypto.getCryptoName());
                values.put(KEY_COIN_VALUE, crypto.getPriceValue());
                db.update(TABLE_CRYPTOS, values, "id=?", whereArgs);
                ID++;
                Log.d("Updated sql", "Update just ran");
                Log.d("Updated sql", "Crypto name "+crypto.getCryptoName()+ "ID value "+ ID);
            }while (cursor.moveToPosition(ID));
        } else {
            cursor.moveToNext();
        }
    }

    public List<CryptoHelper> getAllCurrencies() {
        List<CryptoHelper> cryptoList =  new ArrayList<CryptoHelper>();

        // Select all query
        String selectQuery = "SELECT * FROM " + TABLE_CRYPTOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if(cursor.moveToFirst()) {
            do {
                CryptoHelper cryptoHelper = new CryptoHelper();
                cryptoHelper.setID(Integer.parseInt(cursor.getString(0)));
                cryptoHelper.setCryptoName(cursor.getString(1));
                cryptoHelper.setPriceValue(cursor.getString(2));
                // Adding crypto to list
                cryptoList.add(cryptoHelper);
            } while (cursor.moveToNext());
        }
        return cryptoList;
    }
}
