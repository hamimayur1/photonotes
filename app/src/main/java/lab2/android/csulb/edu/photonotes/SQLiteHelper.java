package lab2.android.csulb.edu.photonotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mayur hami.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PhotoNotes";

    // Account table name
    private static final String Photos_Table = "Photos";


    // Account Table Columns names
    private static final String P_Id = "Account_Id";
    private static final String P_Location = "Account_Location";
    private static final String P_Caption = "Account_Caption";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Account Table
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + Photos_Table + "("
                + P_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + P_Location + " TEXT,"
                + P_Caption + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Photos_Table);
        // Create tables again
        onCreate(db);
    }

    // Add new Account
    public boolean addtoList(DataGetSet Dataset) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        ContentValues values = new ContentValues();
        values.put(P_Id, Dataset.id);
        values.put(P_Caption, Dataset.caption);
        values.put(P_Location, Dataset.location);

        // Inserting Row
        long store_id = db.insert(Photos_Table, null, values);
        db.close(); // Closing database connection

        if(store_id>0)
            return true;

        return false;

    }

    //Get Account Data
    public ArrayList<DataGetSet> getListDB() {
        ArrayList<DataGetSet> getSetData = new ArrayList<DataGetSet>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "select * from " + Photos_Table + " where 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                DataGetSet dgs = new DataGetSet();
                dgs.id = cursor.getString(0);
                dgs.caption = cursor.getString(2);
                dgs.location = cursor.getString(1);
                getSetData.add(dgs);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return getSetData;
    }

    //Get Account Data
    public DataGetSet getPhoto(String id) {
        DataGetSet dgs = new DataGetSet();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "select * from " + Photos_Table + " where `" + P_Id + "` = '" + id + "'";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                dgs.id = cursor.getString(0);
                dgs.caption = cursor.getString(2);
                dgs.location = cursor.getString(1);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return dgs;
    }


    //Get Account Data
    public boolean deletePhoto(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "delete from " + Photos_Table + " where `" + P_Id + "` = '" + id + "'";
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return false;
    }
}
