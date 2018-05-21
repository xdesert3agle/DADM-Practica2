package es.dadm.practica2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Adapters.TileAdapter;
import es.dadm.practica2.Interfaces.ElementActions;

public class Categories extends AppCompatActivity {
    /*@BindView(R.id.rvTickets)
    RecyclerView mRecycler;

    private TileAdapter mAdapter;
    private List<Category> mCategoryList;
    private TicketDB mTicketDB = TicketDB.getInstance();
    private CategoryPersist categoryPersist = new CategoryPersist();
    private int mSelTicketPosition;

    private final String CATEGORY_PATH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ButterKnife.bind(this);
        registerForContextMenu(mRecycler);

        mCategoryList = getCategoryList();

        mAdapter = new TileAdapter(mCategoryList, this, new ElementActions() {
            @Override
            public void onItemClicked(int position) {
                startActivity(new Intent(Categories.this, AddEditTicket.class).putExtra(fragmentList.TAG_TICKET_POSITION, mTicketList.get(position).getId()));
            }

            @Override
            public void onCreateContextMenu(View view, ContextMenu menu, int position) {
                mSelTicketPosition = position;

                MenuInflater inflater = new MenuInflater(Categories.this);
                inflater.inflate(R.menu.long_press_menu, menu);
            }
        });

        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public List<Category> getCategoryList(){
        return categoryPersist.loadFromJson();
    }*/
}
