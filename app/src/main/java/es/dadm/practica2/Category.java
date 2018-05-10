package es.dadm.practica2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Category implements Parcelable{
    private String title;
    private String description;
    private List<Ticket> TicketList = new ArrayList<>();

    public Category(){}

    public Category(Parcel source){
        readFromParcel(source);
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

    public List<Ticket> getTicketList() {
        return TicketList;
    }

    // MÉTODOS PARCELABLE
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeTypedList(TicketList);
    }

    private void readFromParcel(Parcel source) {
        title = source.readString();
        description = source.readString();
        TicketList = new ArrayList<Ticket>();
        source.readTypedList(TicketList, Ticket.CREATOR);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getNumberOfBills(){
        return TicketList.size();
    }
}