package es.dadm.practica2.Util;

import android.app.Application;

import es.dadm.practica2.Objects.CategoryUtil;
import es.dadm.practica2.Objects.TicketDB;

public class Initialize extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        TicketDB.init(getApplicationContext());
        CategoryUtil.init();
    }
}