package com.ftp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.Throws;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/com.ftp/databases/";
    private static String DB_NAME = "myDb.sqlite";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist){
//            Log.d("dbExist", "true");
            //overwrite db if exists earlier
            copyDataBase();
        }else{
            try {
//                Log.d("dbExist", "called copy");
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file unnecessarily.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        boolean checkDB = false;
        try{
            File dbDir = new File(DB_PATH);
            if(!dbDir.exists()) {
                dbDir.mkdir();
            }
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            if(file.exists()){
                checkDB = true;
            }

        }catch(Exception e){
            //database does't exist yet.
            checkDB = false;
        }
        return checkDB;
    }

    /**
     * Copies your database from your files folder to the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
        File dbFile = myContext.getFilesDir();
        String mPath = new File(dbFile.getPath(), DB_NAME).getPath();
        InputStream myInput = new FileInputStream(mPath);
        String outFileName = DB_PATH + DB_NAME;
        Log.d("adi", mPath);
        Log.d("adi", outFileName);
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public List<Material> openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

            SQLiteDatabase mDb = this.getReadableDatabase();
            List<Material> materials = new ArrayList<>();

                String sql = "SELECT * FROM Materials;";
                Cursor mCur = mDb.rawQuery(sql, null);
                if (mCur != null) {
                    if (mCur.moveToFirst()) {
                        do {
                            materials.add(new Material(
                                    mCur.getString(0),
                                    mCur.getString(1),
                                    mCur.getString(2)));
                        } while (mCur.moveToNext());
                    }
                    mCur.close();
                }
                return materials;


    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}