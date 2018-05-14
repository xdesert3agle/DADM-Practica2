package es.dadm.practica2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketDB extends SQLiteOpenHelper {

    private static TicketDB instance = null;

    private Context context;

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
    private static final String[] COLUMNS = {COL_ID, COL_CATEGORY_ID, COL_TITLE, COL_DESCRIPTION, COL_PRICE, COL_DATE, COL_PHOTO};

    private SQLiteDatabase db;
    private Cursor cursor;

    private SQLiteStatement insertStatement;
    private static final String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + "(" +
            COL_ID + ", " +
            COL_CATEGORY_ID + ", " +
            COL_TITLE + ", " +
            COL_DESCRIPTION + ", " +
            COL_PRICE + ", " +
            COL_DATE + ", " +
            COL_PHOTO + ")" + " VALUES (?, ?, ?, ?, ?, ?, ?)";

    //Sentencia SQL para crear la tabla de Tickets
    private static final String CREATE_DB = "CREATE TABLE " + TABLE_NAME +
            "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_CATEGORY_ID + " INTEGER NOT NULL, " +
            COL_TITLE + " TEXT NOT NULL, " +
            COL_DESCRIPTION + " TEXT NOT NULL, " +
            COL_PRICE + " DOUBLE NOT NULL, " +
            COL_DATE + " LONG NOT NULL, " +
            COL_PHOTO + " TEXT NOT NULL)";

    private TicketDB(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
        this.context = contexto;
    }

    public static void passContext(Context context){
        if (instance == null){
            instance = new TicketDB(context);
        }
    }

    public static TicketDB getInstance(){
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Se crea la nueva versión de la tabla
        db.execSQL(CREATE_DB);
    }

    /* Método para obtener todas las peliculas de la bd */

    public List<Ticket> getTicketsFromBD() {
        //Abrimos la base de datos 'bdpeliculas' en modo escritura
        db = getWritableDatabase();

        cursor = db.query(TABLE_NAME, COLUMNS, "", null, null, null, null);

        List<Ticket> ticketArray = new ArrayList<>();
        Ticket Ticket;

        if (cursor.moveToFirst()) {
            do {
                Ticket = obtenerValores();

                ticketArray.add(Ticket);
            } while(cursor.moveToNext());
        }
        return ticketArray;
    }

    public void insertTicket(Ticket ticket) {
        ContentValues nuevoRegistro = asignarValores(ticket);

        db = getWritableDatabase();
        db.insert(TABLE_NAME, null, nuevoRegistro);
    }

    /*
     * Método para obtener valores del cursor y devolver un objeto Pelicula
     * */

    private Ticket obtenerValores(){
        Ticket ticket = new Ticket();

        ticket.setId(cursor.getInt(0));
        ticket.setCategory(cursor.getString(1));
        ticket.setTitle(cursor.getString(2));
        ticket.setDescription(cursor.getString(3));
        ticket.setPrice(cursor.getInt(4));
        ticket.setDate(new Date(cursor.getLong(5)));
        ticket.setImgFilename(cursor.getString(6));

        return ticket;
    }

    /*
     * Método para asignar valores al registro a actualizar o a insertar
     * */

    private ContentValues asignarValores(Ticket ticket){
        ContentValues newRegistry = new ContentValues();

        newRegistry.put(COL_CATEGORY_ID, ticket.getCategory());
        newRegistry.put(COL_TITLE, ticket.getTitle());
        newRegistry.put(COL_DESCRIPTION, ticket.getDescription());
        newRegistry.put(COL_PRICE, ticket.getPrice());
        newRegistry.put(COL_DATE, ticket.getDate().getTime());
        newRegistry.put(COL_PHOTO,ticket.getImgFilename());

        return newRegistry;
    }
}