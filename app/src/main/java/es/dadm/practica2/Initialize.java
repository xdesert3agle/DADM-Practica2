package es.dadm.practica2;

import android.app.Application;

public class Initialize extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        TicketSQLiteHelper.passContext(getApplicationContext());
    }
}