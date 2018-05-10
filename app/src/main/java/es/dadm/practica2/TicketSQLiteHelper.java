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

public class TicketSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BDTickets.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Facturas";
    //private static final String[] COLUMNAS = {"_id", "FOTO", "CATEGORÍA", "PRECIO", "FECHA", "DESC_CORTA", "DESC_LARGA"};
    private static final String[] COLUMNAS = {"_id", "FOTO", "PRECIO", "FECHA", "DESC_CORTA", "DESC_LARGA"};

    private SQLiteDatabase db;
    private Cursor cursor;

    private SQLiteStatement insertStatement;
    private static final String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + "(" +
            COLUMNAS[0] + ", " +
            COLUMNAS[1] + ", " +
            COLUMNAS[2] + ", " +
            COLUMNAS[3] + ", " +
            COLUMNAS[4] + ", " +
            COLUMNAS[5] + ")" + " VALUES (?, ?, ?, ?, ?, ?, ?)";

    //Sentencia SQL para crear la tabla de Tickets
    private static final String CREATE_DB = "CREATE TABLE " + TABLE_NAME +
            "(" + COLUMNAS[0] + " INTEGER PRIMARY KEY, " +
            COLUMNAS[1] + " TEXT, " +
            //COLUMNAS[2] + " INTEGER NOT NULL, " +
            COLUMNAS[2] + " DOUBLE NOT NULL, " +
            COLUMNAS[3] + " LONG NOT NULL, " +
            COLUMNAS[4] + " TEXT NOT NULL, " +
            COLUMNAS[5] + " TEXT NOT NULL)";


    public TicketSQLiteHelper(Context contexto, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        Log.d("Info", "Ha entrado en el onCreate");
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        Log.d("Info", "Ha entrado en el onUpgrade");
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Se crea la nueva versión de la tabla
        db.execSQL(CREATE_DB);
    }

    /* Método para obtener todas las peliculas de la bd */

    public List<Ticket> getTicketsFromBD() {
        //Abrimos la base de datos 'bdpeliculas' en modo escritura
        db = getWritableDatabase();

        cursor = db.query("Peliculas", COLUMNAS, "", null, null, null, null);

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
        //Alternativa 1: método sqlExec()
        //String sql = "INSERT INTO Peliculas (_id,genero, ...) VALUES ('" + id + "','" + genero + "') ";
        //db.execSQL(sql);

        //Alternativa 2: método insert()
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
        ticket.setPhotoFileName(cursor.getString(1));
        ticket.setAmount(cursor.getInt(2));
        // No asignamos la fecha, ya que se crea y se asigna automáticamente en el constructor de 'Ticket'
        ticket.setTitle(cursor.getString(4));
        ticket.setDescription(cursor.getString(5));

        return ticket;
    }

    /*
     * Método para asignar valores al registro a actualizar o a insertar
     * */

    private ContentValues asignarValores(Ticket ticket){
        ContentValues newRegistry = new ContentValues();

        newRegistry.put("FOTO",ticket.getPhotoFileName());
        newRegistry.put("PRECIO",ticket.getAmount());
        newRegistry.put("FECHA",ticket.getFormatedDate());
        newRegistry.put("DESC_CORTA",ticket.getTitle());
        newRegistry.put("DESC_LARGA",ticket.getDescription());

        return newRegistry;
    }
}