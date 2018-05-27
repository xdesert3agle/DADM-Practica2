package es.dadm.practica2.Util;

import android.app.Application;

import es.dadm.practica2.Objects.CategoryUtil;
import es.dadm.practica2.Objects.TicketDB;
import es.dadm.practica2.R;

public class Init extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        TicketDB.init(getApplicationContext());
        CategoryUtil.init(getApplicationContext());

        ImgUtil.saveImageFromDrawable(R.drawable.black, ImgUtil.DEFAULT_IMG_FILENAME, getApplicationContext());
    }
}