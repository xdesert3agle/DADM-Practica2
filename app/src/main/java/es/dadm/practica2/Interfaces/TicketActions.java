package es.dadm.practica2.Interfaces;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

public interface TicketActions {
    void onItemClicked(int position);
    void onCreateContextMenu(View view, ContextMenu menu, int position);
}