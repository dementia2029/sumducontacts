package com.dementia.sumdu.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dementia.sumdu.data.ContactData;
import com.dementia.sumdu.data.Pidrozdil;
import com.dementia.sumdu.data.Viddil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DBManager {
    private static String DB_PATH = "";
    private static final String DATA_DB_NAME = "ssuphone.sqlite";
    private static final int DATA_DB_VERSION = 1;
    public static Context mContext;
    private DataDBHelper mDataDBHelper;
    private SQLiteDatabase mDataDB;
    private static DBManager sInstance;
    private static volatile boolean mDbIsOpen;

    public static synchronized DBManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBManager(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBManager(Context context) {
        mContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        mDbIsOpen = false;
    }

    public synchronized void open() throws IOException {
        boolean dataDbExist = checkExistDataDB();
        while (mDbIsOpen) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mDbIsOpen = true;
        mDataDBHelper = new DataDBHelper(mContext, DATA_DB_NAME, null, DATA_DB_VERSION);
        mDataDB = mDataDBHelper.getWritableDatabase();
        if (!dataDbExist) {
            mDataDBHelper.getReadableDatabase();
            copyDataDBFromAssets();
        }
    }

    private static boolean checkExistDataDB() {
        File dbFile = new File(DB_PATH + DATA_DB_NAME);
        return dbFile.exists();
    }

    private static void copyDataDBFromAssets() throws IOException {
        InputStream myInput = mContext.getAssets().open(DATA_DB_NAME);
        String outFileName = DB_PATH + DATA_DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void close() {
        if (mDataDB != null) {
            mDataDB.close();
        }
        if (mDataDBHelper != null) {
            mDataDBHelper.close();
        }
        mDbIsOpen = false;
    }

    private class DataDBHelper extends SQLiteOpenHelper {
        private DataDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public void saveFav(int id, String fav) {
        ContentValues cv = new ContentValues();
        cv.put("fav", fav);
        mDataDB.update("tbl_employee", cv, "_idem = ?", new String[]{String.valueOf(id)});
    }

    public ContactData getContact(int id) {
        ContactData contactData;

        int Index_contact_id, Index_vid_id, Index_contact_name, Index_position,
                Index_internal_phone, Index_city_phone, Index_cabinet, Index_email,
                Index_note, Index_fav, contact_id, vid_id;

        String contact_name, position, internal_phone, city_phone, cabinet, email, note, fav;

        Cursor cursor = mDataDB.rawQuery("SELECT * from tbl_employee where _idem=?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Index_contact_id = cursor.getColumnIndex("_idem");
            Index_vid_id = cursor.getColumnIndex("vid_id");
            Index_contact_name = cursor.getColumnIndex("name");
            Index_position = cursor.getColumnIndex("position");
            Index_internal_phone = cursor.getColumnIndex("internal_phone");
            Index_city_phone = cursor.getColumnIndex("city_phone");
            Index_cabinet = cursor.getColumnIndex("cabinet");
            Index_email = cursor.getColumnIndex("email");
            Index_note = cursor.getColumnIndex("note");
            Index_fav = cursor.getColumnIndex("fav");
            contact_id = cursor.getInt(Index_contact_id);
            vid_id = cursor.getInt(Index_vid_id);
            contact_name = cursor.getString(Index_contact_name);
            position = cursor.getString(Index_position);
            internal_phone = cursor.getString(Index_internal_phone);
            city_phone = cursor.getString(Index_city_phone);
            cabinet = cursor.getString(Index_cabinet);
            email = cursor.getString(Index_email);
            note = cursor.getString(Index_note);
            fav = cursor.getString(Index_fav);

            contactData = new ContactData(contact_id, contact_name, position, internal_phone, city_phone,
                    cabinet, email, note, vid_id, fav);
            cursor.close();
            return contactData;
        }else {
            cursor.close();
            return null;
        }
    }

    public ArrayList<ContactData> getAnother() {
        ArrayList<ContactData> allData = new ArrayList<>();
        ContactData contactData;

        int Index_contact_id, Index_vid_id, Index_contact_name, Index_position,
                Index_internal_phone, Index_city_phone, Index_cabinet, Index_email,
                Index_note, Index_fav, contact_id, vid_id;

        String contact_name, position, internal_phone, city_phone, cabinet, email, note, fav;

        Cursor cursor = mDataDB.rawQuery("SELECT * from tbl_employee where vid_id like ? ORDER BY name", new String[]{""});

        if (cursor.moveToFirst()) {
            Index_contact_id = cursor.getColumnIndex("_idem");
            Index_vid_id = cursor.getColumnIndex("vid_id");
            Index_contact_name = cursor.getColumnIndex("name");
            Index_position = cursor.getColumnIndex("position");
            Index_internal_phone = cursor.getColumnIndex("internal_phone");
            Index_city_phone = cursor.getColumnIndex("city_phone");
            Index_cabinet = cursor.getColumnIndex("cabinet");
            Index_email = cursor.getColumnIndex("email");
            Index_note = cursor.getColumnIndex("note");
            Index_fav = cursor.getColumnIndex("fav");

            do {
                contact_id = cursor.getInt(Index_contact_id);
                vid_id = cursor.getInt(Index_vid_id);
                contact_name = cursor.getString(Index_contact_name);
                position = cursor.getString(Index_position);
                internal_phone = cursor.getString(Index_internal_phone);
                city_phone = cursor.getString(Index_city_phone);
                cabinet = cursor.getString(Index_cabinet);
                email = cursor.getString(Index_email);
                note = cursor.getString(Index_note);
                fav = cursor.getString(Index_fav);

                contactData = new ContactData(contact_id, contact_name, position, internal_phone, city_phone,
                        cabinet, email, note, vid_id, fav);
                allData.add(contactData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allData;
    }

    public ArrayList<ContactData> getSearch(String query) {
        ArrayList<ContactData> allData = new ArrayList<>();
        ContactData contactData;

        int Index_contact_id, Index_vid_id, Index_contact_name, Index_position,
                Index_internal_phone, Index_city_phone, Index_cabinet, Index_email,
                Index_note, Index_fav, contact_id, vid_id;

        String contact_name, position, internal_phone, city_phone, cabinet, email, note, fav;

        Cursor cursor = mDataDB.rawQuery("SELECT * from tbl_employee where name like ? or city_phone like ?" +
                " or internal_phone like ? or position like ? or cabinet like ? order by name", new String[]{"%"+String.valueOf(query)+"%",
                "%"+String.valueOf(query)+"%","%"+String.valueOf(query)+"%","%"+String.valueOf(query)+"%","%"+String.valueOf(query)+"%"});

        if (cursor.moveToFirst()) {
            Index_contact_id = cursor.getColumnIndex("_idem");
            Index_vid_id = cursor.getColumnIndex("vid_id");
            Index_contact_name = cursor.getColumnIndex("name");
            Index_position = cursor.getColumnIndex("position");
            Index_internal_phone = cursor.getColumnIndex("internal_phone");
            Index_city_phone = cursor.getColumnIndex("city_phone");
            Index_cabinet = cursor.getColumnIndex("cabinet");
            Index_email = cursor.getColumnIndex("email");
            Index_note = cursor.getColumnIndex("note");
            Index_fav = cursor.getColumnIndex("fav");

            do {
                contact_id = cursor.getInt(Index_contact_id);
                vid_id = cursor.getInt(Index_vid_id);
                contact_name = cursor.getString(Index_contact_name);
                position = cursor.getString(Index_position);
                internal_phone = cursor.getString(Index_internal_phone);
                city_phone = cursor.getString(Index_city_phone);
                cabinet = cursor.getString(Index_cabinet);
                email = cursor.getString(Index_email);
                note = cursor.getString(Index_note);
                fav = cursor.getString(Index_fav);

                contactData = new ContactData(contact_id, contact_name, position, internal_phone, city_phone,
                        cabinet, email, note, vid_id, fav);
                allData.add(contactData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allData;
    }

    public ArrayList<ContactData> getAllFavs() {
        ArrayList<ContactData> allData = new ArrayList<>();
        ContactData contactData;

        int Index_contact_id, Index_vid_id, Index_contact_name, Index_position,
                Index_internal_phone, Index_city_phone, Index_cabinet, Index_email,
                Index_note, Index_fav, contact_id, vid_id;

        String contact_name, position, internal_phone, city_phone, cabinet, email, note, fav;

        Cursor cursor = mDataDB.rawQuery("SELECT * from tbl_employee where fav=1 ORDER BY name", null);

        if (cursor.moveToFirst()) {
            Index_contact_id = cursor.getColumnIndex("_idem");
            Index_vid_id = cursor.getColumnIndex("vid_id");
            Index_contact_name = cursor.getColumnIndex("name");
            Index_position = cursor.getColumnIndex("position");
            Index_internal_phone = cursor.getColumnIndex("internal_phone");
            Index_city_phone = cursor.getColumnIndex("city_phone");
            Index_cabinet = cursor.getColumnIndex("cabinet");
            Index_email = cursor.getColumnIndex("email");
            Index_note = cursor.getColumnIndex("note");
            Index_fav = cursor.getColumnIndex("fav");

            do {
                contact_id = cursor.getInt(Index_contact_id);
                vid_id = cursor.getInt(Index_vid_id);
                contact_name = cursor.getString(Index_contact_name);
                position = cursor.getString(Index_position);
                internal_phone = cursor.getString(Index_internal_phone);
                city_phone = cursor.getString(Index_city_phone);
                cabinet = cursor.getString(Index_cabinet);
                email = cursor.getString(Index_email);
                note = cursor.getString(Index_note);
                fav = cursor.getString(Index_fav);

                contactData = new ContactData(contact_id, contact_name, position, internal_phone, city_phone,
                        cabinet, email, note, vid_id, fav);
                allData.add(contactData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allData;
    }

    public LinkedHashMap<Integer, Pidrozdil> getAllData() {
        LinkedHashMap<Integer, Viddil> groupViddil = new LinkedHashMap<>();
        LinkedHashMap<Integer, Pidrozdil> groupPidrozdil = new LinkedHashMap<>();
        ContactData contactData;
        Pidrozdil pidrozdil;
        Viddil viddil;

        int Index_contact_id, Index_pidrozdil_id, Index_vid_id, Index_contact_name, Index_position,
                Index_internal_phone, Index_city_phone, Index_cabinet, Index_email,
                Index_note, Index_pidrozdil_name, Index_category_name, Index_viddil_name, Index_fav,
                contact_id, pidrozdil_id, vid_id;

        String contact_name, position, internal_phone, city_phone, cabinet, email, note, pidrozdil_name,
                category_name, viddil_name, fav;

        Cursor cursor = mDataDB.rawQuery("SELECT * from tbl_employee e inner join tbl_viddil v on " +
                "e.vid_id=v._idvid inner join tbl_pidrozdil p on p._idpid=v.pidrozdil_id " +
                "inner join tbl_kategorii k on k._idkat=p.kat_id", null);

        if (cursor.moveToFirst()) {
            Index_contact_id = cursor.getColumnIndex("_idem");
            Index_pidrozdil_id = cursor.getColumnIndex("_idpid");
            Index_vid_id = cursor.getColumnIndex("_idvid");
            Index_contact_name = cursor.getColumnIndex("name");
            Index_position = cursor.getColumnIndex("position");
            Index_internal_phone = cursor.getColumnIndex("internal_phone");
            Index_city_phone = cursor.getColumnIndex("city_phone");
            Index_cabinet = cursor.getColumnIndex("cabinet");
            Index_email = cursor.getColumnIndex("email");
            Index_note = cursor.getColumnIndex("note");
            Index_pidrozdil_name = cursor.getColumnIndex("partition");
            Index_category_name = cursor.getColumnIndex("category");
            Index_viddil_name = cursor.getColumnIndex("department");
            Index_fav = cursor.getColumnIndex("fav");

            do {
                contact_id = cursor.getInt(Index_contact_id);
                pidrozdil_id = cursor.getInt(Index_pidrozdil_id);
                vid_id = cursor.getInt(Index_vid_id);
                contact_name = cursor.getString(Index_contact_name);
                position = cursor.getString(Index_position);
                internal_phone = cursor.getString(Index_internal_phone);
                city_phone = cursor.getString(Index_city_phone);
                cabinet = cursor.getString(Index_cabinet);
                email = cursor.getString(Index_email);
                note = cursor.getString(Index_note);
                pidrozdil_name = cursor.getString(Index_pidrozdil_name);
                category_name = cursor.getString(Index_category_name);
                viddil_name = cursor.getString(Index_viddil_name);
                fav = cursor.getString(Index_fav);

                contactData = new ContactData(contact_id, contact_name, position, internal_phone, city_phone,
                        cabinet, email, note, vid_id, fav);

                if (groupViddil.get(vid_id) == null) {
                    viddil = new Viddil(vid_id, pidrozdil_id, viddil_name, contactData);
                    groupViddil.put(vid_id, viddil);
                } else {
                    groupViddil.get(vid_id).addData(contactData);
                }

                if (groupPidrozdil.get(pidrozdil_id) == null) {
                    pidrozdil = new Pidrozdil(pidrozdil_id, pidrozdil_name, category_name, groupViddil.get(vid_id));
                    groupPidrozdil.put(pidrozdil_id, pidrozdil);
                } else {
                    groupPidrozdil.get(pidrozdil_id).addData(groupViddil.get(vid_id));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return groupPidrozdil;
    }
}
