package es.dadm.practica2.Objects;

import es.dadm.practica2.Util.ImgUtil;

public class Category {
    private int id;
    private String title;
    private String description;
    private String details;
    private String imgFilename;

    public Category(){
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImgFilename() {
        return imgFilename;
    }

    public void setImgFilename(String imgFilename) {
        this.imgFilename = imgFilename;
    }
}