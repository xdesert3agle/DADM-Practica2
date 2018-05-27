package es.dadm.practica2.Objects;

import java.util.Calendar;
import java.util.Date;

import es.dadm.practica2.Util.ImgUtil;

public class Ticket {
    private int id;
    private String title;
    private String description;
    private String category;
    private double price;
    private Date date;
    private String imgFilename;
    private double latitude;
    private double longitude;
    private String OCRtext;
    private String address;

    public Ticket(){
        this.date = new Date();
        this.imgFilename = ImgUtil.DEFAULT_IMG_FILENAME;
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

    public String getImgFilename() {
        return imgFilename;
    }

    public void setImgFilename(String imgFilename) {
        this.imgFilename = imgFilename;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOCRtext() {
        return OCRtext;
    }

    public void setOCRtext(String OCRtext) {
        this.OCRtext = OCRtext;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}