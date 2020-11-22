package com.example.wrapfluttermodule.Crypto;

public class CryptoHelper {
    int _id;
    String _crypto_name;
    String _price_value;

    public CryptoHelper() {}

    public CryptoHelper(int id, String crypto_name, String price_value) {
        this._id = id;
        this._crypto_name = crypto_name;
        this._price_value = price_value;
    }
    public CryptoHelper(String crypto_name, String price_value) {
//        this._id = id;
        this._crypto_name = crypto_name;
        this._price_value = price_value;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }
    public String getCryptoName(){
        return this._crypto_name;
    }

    public void setCryptoName(String crypto_name) {
        this._crypto_name = crypto_name;
    }
    public String getPriceValue(){
        return this._price_value;
    }

    public void setPriceValue(String price_value){
        this._price_value = price_value;
    }

    }
