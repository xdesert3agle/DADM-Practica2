package es.dadm.practica2.Interfaces;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

// Acciones que tienen todos los tickets y categorías
public interface ElementActions {
    void onItemClicked(int position); // OnClick
    void onCreateContextMenu(View view, ContextMenu menu, int position); // Menú contextual
}