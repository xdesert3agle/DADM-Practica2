package es.dadm.practica2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String title;
    private String description;
    private List<Ticket> TicketList = new ArrayList<>();

    public Category(){}

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

    public List<Ticket> getTicketList() {
        return TicketList;
    }
}