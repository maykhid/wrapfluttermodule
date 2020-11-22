package com.example.wrapfluttermodule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
