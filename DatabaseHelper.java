package com.example.johnmeistrell.sqliteapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Traffic2.db";


    /* When car is stopped for 30 minutes and moves 1k feet, create Route row if not found. */
    /* Every 5 minutes or 5 miles, create Location and Leg row. */
    /* When car is in 1k feet of known location or stops, create Location, Leg and Timing rows if not found. */

    public static final String TABLE_NAME = "student_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SURNAME";
    public static final String COL_4 = "MARKS";

    public static final String TABLE_NAME_TIMING = "Timing_table";
    public static final String TCOL_01 = "ID";                   /* INTEGER PRIMARY KEY AUTOINCREMENT - String s - */
    public static final String TCOL_02 = "TIMING_GUID";          /* CHAR(16)      - String s - prim TTT */         
    public static final String TCOL_03 = "ROUTE_GUID";           /* CHAR(16)      - String s -          */
    public static final String TCOL_04 = "HOUR_T";               /* CHAR(2)       - String s - u1       */
    public static final String TCOL_05 = "MINUTE_T";             /* CHAR(2)       - String s - u1       */
    public static final String TCOL_06 = "STATE_EACH_SECOND";    /* CHAR(60)      - String s - RG user G full speed. g incr. r decr stopped inconsistant */

    public static final String TABLE_NAME_LOCATION = "Location_table";
    public static final String LCOL_01 = "ID";                   /* INTEGER PRIMARY KEY AUTOINCREMENT - String s - */
    public static final String LCOL_02 = "LOCAITON_GUID";        /* CHAR(16)       - String s - prim LLL */
    public static final String LCOL_03 = "NAME";                 /* CHAR(30)       - String s -          */
    public static final String LCOL_04 = "LONGITUDE";            /* DECIMAL(27,18) - java.math.BigDecimal g - */
    public static final String LCOL_05 = "LATITUDE";             /* DECIMAL(27,18) - java.math.BigDecimal g - */
    public static final String LCOL_06 = "TYPE";                 /* CHAR(1)        - String s - Light Note Stop */

    public static final String TABLE_NAME_LANE = "Lane_table";
    public static final String ACOL_01 = "ID";                   /* INTEGER PRIMARY KEY AUTOINCREMENT - String s - */
    public static final String ACOL_02 = "LANE_GUID";            /* CHAR(16)      - String s - prim GGG */
    public static final String ACOL_03 = "BEARING";              /* SMALLINT      - short  h -          */
    public static final String ACOL_04 = "LANE";                 /* CHAR(1)       - String s -          Left Right Strait All */
    public static final String ACOL_05 = "DELAY_SECONDS";        /* SMALLINT      - short  h -          */
    public static final String ACOL_06 = "SPEED_LIMIT";          /* SMALLINT      - short  h -          */
    public static final String ACOL_07 = "VOICE_NOTE";           /* VARCHAR(100)  - String s -          */
    public static final String ACOL_08 = "LOCAITON_GUID";        /* CHAR(16)      - String s -      LLL */
    public static final String ACOL_09 = "TIMING_GUID";          /* CHAR(16)      - String s -      TTT */

    public static final String TABLE_NAME_ROUTE = "Route_table"; /*                        opt        Car stops for 30min. Drives 500 feet. */ 
    public static final String RCOL_01 = "ID";                   /* INTEGER PRIMARY KEY AUTOINCREMENT - String s - */
    public static final String RCOL_02 = "ROUTE_GUID";           /* CHAR(16)      - String s - prim RRR */
    public static final String RCOL_03 = "ROUTE_NAME_GUID";      /* CHAR(16)      - String s -      NNN */
    public static final String RCOL_04 = "LANE_GUID";            /* CHAR(16)      - String s - u1   GGG */
    public static final String RCOL_05 = "ROUTE_ORDER";          /* INT           - int    i - u1       */
  
    public static final String TABLE_NAME_ROUTE_NAME = "Route_Name_table";
    public static final String NCOL_01 = "ID";                   /* INTEGER PRIMARY KEY AUTOINCREMENT - String s - */            
    public static final String NCOL_02 = "ROUTE_NAME_GUID";      /* CHAR(16)      - String s - prim NNN */
    public static final String NCOL_03 = "NAME";                 /* CHAR(30)      - String s -          */

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SURNAME TEXT,MARKS INTEGER,LAT INTEGER,LONG INTEGER,LIGHT_YN,VOICE TEXT,ROAD1_NAME TEXT,ROAD1_COMPASS INTEGER,ROAD1_SPEED_LIMIT INTEGER,ROAD1_ROUTE_NBR INTEGER,ROAD1_ROUTE_LEG INTEGER,ROAD1_ROUTE_NAME TEXT)");
        db.execSQL("create table " +  TABLE_NAME_TIMING +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TIMING_GUID CHAR(16),ROUTE_GUID CHAR(16),HOUR_T CHAR(2),MINUTE_T CHAR(2),STATE_EACH_SECOND CHAR(60))");
        db.execSQL("create table " +  TABLE_NAME_LOCATION +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,LOCAITON_GUID CHAR(16),NAME CHAR(30),LONGITUDE CHAR(30),LATITUDE CHAR(30),TYPE CHAR(1))");
        db.execSQL("create table " +  TABLE_NAME_LANE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,LANE_GUID CHAR(16),BEARING SMALLINT,LANE CHAR(1),DELAY_SECONDS SMALLINT,SPEED_LIMIT SMALLINT,VOICE_NOTE VARCHAR(100),LOCAITON_GUID CHAR(16),TIMING_GUID CHAR(16))");
        db.execSQL("create table " +  TABLE_NAME_ROUTE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,ROUTE_GUID CHAR(16),ROUTE_NAME_GUID CHAR(16),LANE_GUID CHAR(16),ROUTE_ORDER INT)");
        db.execSQL("create table " + TABLE_NAME_ROUTE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ROUTE_NAME_GUID CHAR(16),NAME CHAR(30))");
 
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TIMING); 
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCATION); 
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LANE); 
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROUTE); 
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROUTE_NAME); 
        onCreate(db);
    }

    public boolean insertData(String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


public boolean insertDataLITETiming(String sTIMING_GUID_T,String sROUTE_GUID_T,String sHOUR_T_T,String sMINUTE_T_T,String sSTATE_EACH_SECOND_T) {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put(TCOL_02,sTIMING_GUID_T);
         contentValues.put(TCOL_03,sROUTE_GUID_T);
         contentValues.put(TCOL_04,sHOUR_T_T);
         contentValues.put(TCOL_05,sMINUTE_T_T);
         long result = db.insert(TABLE_NAME_TIMING,null ,contentValues);
         if(result == -1)
             return false;
         else
             return true;
     }

public boolean insertDataLITELocation(String sLOCAITON_GUID_L,String sNAME_L,String gLONGITUDE_L,String gLATITUDE_L,String sTYPE_L) {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put(LCOL_02,sLOCAITON_GUID_L);
         contentValues.put(LCOL_03,sNAME_L);
         contentValues.put(LCOL_04,gLONGITUDE_L);
         contentValues.put(LCOL_05,gLATITUDE_L);
         contentValues.put(LCOL_06,sTYPE_L);
         long result = db.insert(TABLE_NAME_LOCATION,null ,contentValues);
         if(result == -1)
             return false;
         else
             return true;
     }


public boolean insertDataLITELane(String sLANE_GUID_A,short hBEARING_A,String sLANE_A,short hDELAY_SECONDS_A,short hSPEED_LIMIT_A,String sVOICE_NOTE_A,String sLOCAITON_GUID_A,String sTIMING_GUID_A) {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put(ACOL_02,sLANE_GUID_A);
         contentValues.put(ACOL_03,hBEARING_A);
         contentValues.put(ACOL_04,sLANE_A);
         contentValues.put(ACOL_05,hDELAY_SECONDS_A);
         contentValues.put(ACOL_06,hSPEED_LIMIT_A);
         contentValues.put(ACOL_07,sVOICE_NOTE_A);
         contentValues.put(ACOL_08,sLOCAITON_GUID_A);
         contentValues.put(ACOL_09,sTIMING_GUID_A);
         long result = db.insert(TABLE_NAME_LANE,null ,contentValues);
         if(result == -1)
             return false;
         else
             return true;
     }

public boolean insertDataLITERoute(String sROUTE_GUID_R,String sROUTE_NAME_GUID_R,String sLANE_GUID_R,int iROUTE_ORDER_R) {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put(RCOL_02,sROUTE_GUID_R);
         contentValues.put(RCOL_03,sROUTE_NAME_GUID_R);
         contentValues.put(RCOL_04,sLANE_GUID_R);
         contentValues.put(RCOL_05,iROUTE_ORDER_R);
         long result = db.insert(TABLE_NAME_ROUTE,null ,contentValues);
         if(result == -1)
             return false;
         else
             return true;
     }

public boolean insertDataLITERoute_Name(String sROUTE_NAME_GUID_N,String sNAME_N) {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put(NCOL_02,sROUTE_NAME_GUID_N);
         contentValues.put(NCOL_03,sNAME_N);
         long result = db.insert(TABLE_NAME_ROUTE_NAME,null ,contentValues);
         if(result == -1)
             return false;
         else
             return true;
     }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getAllDataLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resL = db.rawQuery("select * from "+TABLE_NAME_LOCATION,null);
        return resL;
    }

    public boolean updateData(String id,String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}

