package es.dadm.practica2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    private static final String DATABASE_NAME = "BDTickets.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Facturas";
    private static final String[] COLUMNAS = {"_id", "FOTO", "CATEGORÍA", "PRECIO", "FECHA", "DESC_CORTA", "DESC_LARGA"};

    private Context context;
    private SQLiteDatabase db;

    private SQLiteStatement insertStatement;
    private static final String INSERT = "insert into " + TABLE_NAME + "(" +
            COLUMNAS[0] + ", " +
            COLUMNAS[1] + ", " +
            COLUMNAS[2] + ", " +
            COLUMNAS[3] + ", " +
            COLUMNAS[4] + ", " +
            COLUMNAS[5] + ", " +
            COLUMNAS[6] + ")" + " VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String CREATE_DB = "CREATE TABLE " + TABLE_NAME +
            "(" + COLUMNAS[0] + " INTEGER PRIMARY KEY, " +
            COLUMNAS[1] + " TEXT, " +
            COLUMNAS[2] + " INTEGER NOT NULL, " +
            COLUMNAS[3] + " DOUBLE NOT NULL, " +
            COLUMNAS[4] + " LONG NOT NULL, " +
            COLUMNAS[5] + " TEXT NOT NULL, " +
            COLUMNAS[6] + " TEXT NOT NULL)";

    public DataHelper(Context context) {
        this.context = context;
        SQLOpenHelper  openHelper = new SQLOpenHelper (this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStatement = this.db.compileStatement(INSERT);
    }

    public long insert(String name) {

        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("nombre", name);

        //Insertamos el registro en la base de datos
        db.insert(TABLE_NAME, null, nuevoRegistro);


        // this.insertStatement.bindString(1, name);

        //return this.insertStatement.executeInsert();
        return 1;
    }

    public int deleteAll() {
        return db.delete(TABLE_NAME, null, null);
    }

    public List<String> selectAll() {
        List<String> list = new ArrayList<String>();
        Cursor cursor = db.query(TABLE_NAME, COLUMNAS,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    private static class SQLOpenHelper extends SQLiteOpenHelper {
        SQLOpenHelper (Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("SQL", "onUpgrade: eliminando tabla si ??sta existe, y cre??ndola de nuevo");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
