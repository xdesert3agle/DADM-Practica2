package es.dadm.practica2;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class Bill implements Parcelable{
    private int id;
    private String title;
    private String description;
    private int amount;
    private Date date;
    private String photoFileName;

    public Bill(Parcel source){
        readFromParcel(source);
    }

    public Bill(){
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

    public void printInfo(){
        Log.d("ID", String.valueOf(this.id));
        Log.d("Importe", String.valueOf(this.amount));
        Log.d("Fecha de creación", getFormatedDate());
        Log.d("Descripción corta", this.title);
        Log.d("Descripción larga", this.description);
    }

    // MÉTODOS PARCELABLE
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(photoFileName);
        dest.writeInt(amount);
        dest.writeLong(date.getTime());
        dest.writeString(title);
        dest.writeString(description);
    }

    private void readFromParcel(Parcel source) {
        id = source.readInt();
        photoFileName = source.readString();
        amount = source.readInt();
        date = new Date(source.readLong());
        title = source.readString();
        description = source.readString();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };
}
