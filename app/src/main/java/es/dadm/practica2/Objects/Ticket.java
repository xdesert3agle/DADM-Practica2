package es.dadm.practica2.Objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class Ticket {
    private int id;
    private String title;
    private String description;
    private String category;
    private double price;
    private Date date;
    private String imgFilename;

    public Ticket(){
        this.date = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate(){
        return date;
    }

    public String getFormatedDate() {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        return day + "-" + month + "-" + year + " " + hour + ":" + minute;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void printInfo(){
        Log.d("ID", String.valueOf(this.id));
        Log.d("Importe", String.valueOf(this.price));
        Log.d("Fecha de creación", getFormatedDate());
        Log.d("Descripción corta", this.title);
        Log.d("Descripción larga", this.description);
    }

    public String getImgFilename() {
        return imgFilename;
    }

    public void setImgFilename(String imgFilename) {
        this.imgFilename = imgFilename;
    }
}