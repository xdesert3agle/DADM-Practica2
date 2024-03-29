package es.dadm.practica2.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketDB extends SQLiteOpenHelper {
    private static TicketDB instance = null;

    private static final String DB_NAME = "BDTickets.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Ticket";
    private static final String COL_ID = "_id";
    private static final String COL_CATEGORY_ID = "CATEGORY_ID";
    private static final String COL_TITLE = "TITLE";
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final String COL_PRICE = "PRICE";
    private static final String COL_DATE = "DATE";
    private static final String COL_PHOTO = "PHOTO";
    private static final String COL_LATITUDE = "LATITUDE";
    private static final String COL_LONGITUDE = "LONGITUDE";
    private static final String COL_ADDRESS = "ADDRESS";
    private static final String COL_OCR = "OCR_TEXT";
    private static final String[] COLUMNS = { COL_ID, COL_CATEGORY_ID, COL_TITLE, COL_DESCRIPTION, COL_PRICE, COL_DATE, COL_PHOTO, COL_LATITUDE, COL_LONGITUDE, COL_ADDRESS, COL_OCR} ;
    private static final String CREATE_DB = "CREATE TABLE " + TABLE_NAME +
            "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_CATEGORY_ID + " INTEGER NOT NULL, " +
            COL_TITLE + " TEXT NOT NULL, " +
            COL_DESCRIPTION + " TEXT NOT NULL, " +
            COL_PRICE + " DOUBLE NOT NULL, " +
            COL_DATE + " LONG NOT NULL, " +
            COL_PHOTO + " TEXT NOT NULL, " +
            COL_LATITUDE + " DOUBLE, " +
            COL_LONGITUDE + " DOUBLE, " +
            COL_ADDRESS + " TEXT, " +
            COL_OCR + " TEXT)";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    private SQLiteDatabase db;
    private Cursor cursor;

    private TicketDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static void init(Context context){
        if (instance == null){
            instance = new TicketDB(context.getApplicationContext());
        }
    }

    public static TicketDB getInstance(){
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int lastVersion, int newVersion) {
        // Se elimina la versión anterior de la tabla
        db.execSQL(DROP_TABLE + TABLE_NAME);

        // Se crea la nueva versión de la tabla
        db.execSQL(CREATE_DB);
    }

    // Carga todos los tickets de la base de datos a un List<Ticket>
    public List<Ticket> getTicketsFromBD() {
        db = getWritableDatabase();

        cursor = db.query(TABLE_NAME, COLUMNS, "", null, null, null, null);

        List<Ticket> ticketList = new ArrayList<>();
        Ticket ticket;

        if (cursor.moveToFirst()) {
            do {
                ticket = getTicketValues();

                ticketList.add(ticket);
            } while(cursor.moveToNext());
        }
        return ticketList;
    }

    // INSERT
    public void insertTicket(Ticket ticket) {
        ContentValues newRecord = getRecordValues(ticket);

        db = getWritableDatabase();
        db.insert(TABLE_NAME, null, newRecord);
    }

    // UPDATE
    public void updateTicket(Ticket ticket) {
        ContentValues record = getRecordValues(ticket);

        db = getWritableDatabase();
        db.update(TABLE_NAME, record, COL_ID + " = " + ticket.getId(), null);
    }

    // DELETE
    public void deleteTicket(Ticket ticket) {
        ContentValues record = getRecordValues(ticket);

        db = getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = " + ticket.getId(), null);
    }

    private Ticket getTicketValues(){
        Ticket ticket = new Ticket();

        ticket.setId(cursor.getInt(0));
        ticket.setCategory(cursor.getString(1));
        ticket.setTitle(cursor.getString(2));
        ticket.setDescription(cursor.getString(3));
        ticket.setPrice(cursor.getDouble(4));
        ticket.setDate(new Date(cursor.getLong(5)));
        ticket.setImgFilename(cursor.getString(6));
        ticket.setLatitude(cursor.getDouble(7));
        ticket.setLongitude(cursor.getDouble(8));
        ticket.setAddress(cursor.getString(9));
        ticket.setOCRtext(cursor.getString(10));

        return ticket;
    }

    private ContentValues getRecordValues(Ticket ticket){
        ContentValues newRecord = new ContentValues();

        newRecord.put(COL_CATEGORY_ID, ticket.getCategory());
        newRecord.put(COL_TITLE, ticket.getTitle());
        newRecord.put(COL_DESCRIPTION, ticket.getDescription());
        newRecord.put(COL_PRICE, ticket.getPrice());
        newRecord.put(COL_DATE, ticket.getDate().getTime());
        newRecord.put(COL_PHOTO, ticket.getImgFilename());
        newRecord.put(COL_LATITUDE, ticket.getLatitude());
        newRecord.put(COL_LONGITUDE, ticket.getLongitude());
        newRecord.put(COL_ADDRESS, ticket.getAddress());
        newRecord.put(COL_OCR, ticket.getOCRtext());

        return newRecord;
    }

    // Devuelve un ticket con una ID determinada
    public Ticket getTicketWithID(int targetID){
        Ticket ticket = new Ticket();

        cursor = db.query(TABLE_NAME, COLUMNS, COL_ID + " = " + targetID, null, null, null, null);

        if (cursor.moveToFirst()) {
            ticket = getTicketValues();
        }

        return ticket;
    }

    // Devuelve la cantidad de tickets que hay en la BD
    public int getTicketCount(){
        db = getWritableDatabase();
        return Math.toIntExact(DatabaseUtils.queryNumEntries(db, TABLE_NAME));
    }
}