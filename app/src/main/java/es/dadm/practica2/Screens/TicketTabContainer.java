package es.dadm.practica2.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import es.dadm.practica2.Abstract.DrawerMenuActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Adapters.TabsAdapter;
import es.dadm.practica2.R;
import es.dadm.practica2.Objects.Ticket;
import es.dadm.practica2.Objects.TicketDB;


public class TicketTabContainer extends DrawerMenuActivity {
    @BindView(R.id.vpContent) ViewPager mViewPager;
    @BindView(R.id.tlTabs) TabLayout mTabLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private TicketDB mTicketDB;
    private List<Ticket> mTicketList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ticket_tab_container);

        ButterKnife.bind(this);

        mTicketDB = TicketDB.getInstance();

        // Si no hay ningún ticket en la BD se muestra un Toast indicándolo
        if (mTicketDB.getTicketCount() == 0) Toast.makeText(this, R.string.MSG_START_NO_TICKETS_FOUND, Toast.LENGTH_SHORT).show();

        loadTicketsFromDB();

        setUpViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        // Escribe el número de facturas existentes en el título de la Toolbar
        setSupportActionBar(mToolbar);
        setToolbarTicketCount();

        setUpDrawer(mToolbar);
        drawer.setSelectionAtPosition(1);
    }

    private void setUpViewPager(ViewPager viewPager) {
        TabsAdapter tabAdapter = new TabsAdapter(getSupportFragmentManager());

        FragmentList fragmentList = new FragmentList();
        FragmentTiles fragmentTiles = new FragmentTiles();
        FragmentCards fragmentCards = new FragmentCards();

        tabAdapter.addFragment(fragmentList, getString(R.string.TAB_LIST_TITLE));
        tabAdapter.addFragment(fragmentTiles, getString(R.string.TAB_TILES_TITLE));
        tabAdapter.addFragment(fragmentCards, getString(R.string.TAB_CARDS_TITLE));

        viewPager.setAdapter(tabAdapter);
    }

    // Menú de la toolbar
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.addBill).setIcon(R.drawable.ic_add_white_24dp);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addBill: // Botón + --> Registrar una factura nueva
                startActivity(new Intent(TicketTabContainer.this, AddEditTicket.class));

                break;
            default:
                throw new IllegalArgumentException("No se ha podido reconocer el botón presionado.");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        setToolbarTicketCount();
    }

    // Actualiza el número de tickets en la toolbar
    private void setToolbarTicketCount(){
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.TITLE_TICKET_TAB_CONTAINER), mTicketDB.getTicketCount()));
    }

    // Devuevle el array de tickets de la BD
    private void loadTicketsFromDB() {
        mTicketList = mTicketDB.getTicketsFromBD();
    }

    // Opciones del menú lateral
    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        switch (position) {
            case 1:
                drawer.closeDrawer();
                break;

            case 2:
                drawer.closeDrawer();
                startActivity(new Intent(TicketTabContainer.this, Categories.class));
                break;

            case 3:
                startActivity(new Intent(TicketTabContainer.this, TicketLocations.class));
                drawer.closeDrawer();
                break;

            default:
                throw new IllegalArgumentException("No se ha podido reconocer el botón presionado.");
        }

        return true;
    }
}