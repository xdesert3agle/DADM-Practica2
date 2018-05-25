package es.dadm.practica2.Util;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import es.dadm.practica2.Objects.CategoryUtil;
import es.dadm.practica2.Objects.TicketDB;
import es.dadm.practica2.R;

public class Init extends Application{
    public static String DEFAULT_IMG = "default.jpg";

    @Override
    public void onCreate() {
        super.onCreate();

        TicketDB.init(getApplicationContext());
        CategoryUtil.init(getApplicationContext());

        Bitmap stockCategoryImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.black);
        ImgUtil.saveImage(stockCategoryImg, DEFAULT_IMG, this);
    }
}