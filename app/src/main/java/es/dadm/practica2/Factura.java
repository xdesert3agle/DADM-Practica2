package es.dadm.practica2;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class Factura {
    private int id;
    private String photoFileName;
    private String category;
    private int amount;
    private Date date;
    private String shortDesc;
    private String longDesc;

    public Factura(){
        this.date = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public void printInfo(){
        Log.d("ID", String.valueOf(this.id));
        Log.d("Categoría", this.category);
        Log.d("Importe", String.valueOf(this.amount));
        Log.d("Fecha de creación", getFormatedDate());
        Log.d("Descripción corta", this.shortDesc);
        Log.d("Descripción larga", this.longDesc);
    }
}
