package com.ftp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SQLiteModule extends ReactContextBaseJavaModule {

    public SQLiteModule(ReactApplicationContext reactContext) {
        super(reactContext); //required by React Native
    }
    @NonNull
    @Override
    public String getName() {
        return "SQLite";
    }

    @ReactMethod
    public void readMaterialsFromDB(Callback errorCallback, Callback successCallback) {
        try {
            DataBaseHelper myDBHandler = new DataBaseHelper(getReactApplicationContext());
            try {myDBHandler.createDataBase();} catch (IOException e) {
                errorCallback.invoke(e.getMessage());
            }
            List<Material> data = myDBHandler.openDataBase();
            Gson gson = new Gson();
            String json = gson.toJson(data);
            successCallback.invoke(json);
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }
    }
}

