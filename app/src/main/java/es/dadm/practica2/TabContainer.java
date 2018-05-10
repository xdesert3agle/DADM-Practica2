package es.dadm.practica2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.adapters.TabsAdapter;


public class TabContainer extends AppCompatActivity {
    @BindView(R.id.vpContent) ViewPager mViewPager;
    @BindView(R.id.tlTabs) TabLayout mTabLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    public static final String DATABASE_NAME = "BDTickets.db";

    private Drawer mDrawer;

    private List<Ticket> mTicketList = new ArrayList<>();

    private static final String BUNDLE_BILL_LIST = "Ticket list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_container);

        ButterKnife.bind(this);

        TicketSQLiteHelper TicketHelper = new TicketSQLiteHelper(this, null, 1);
        loadTicketsFromDB();

        setUpViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        // Escribe el número de facturas existentes en el título de la Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.TAB_CONTAINER_TITLE), getTotalTickets()));

        setUpDrawer();

        //startActivity(new Intent(TabContainer.this, RegisterBill.class));
        /*ActionBarDrawerToggle toggle =

        mDrawerMenu.addDrawerListener(toggle);
        toggle.syncState();*/
    }

    /*@Override
    public void onBackPressed() {
        if (mDrawerMenu.isDrawerOpen(GravityCompat.START)){
            mDrawerMenu.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    private void setUpViewPager(ViewPager viewPager){
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_BILL_LIST, (ArrayList) mTicketList);

        fragmentList fragmentList = new fragmentList();
        fragmentTiles fragmentTiles = new fragmentTiles();
        fragmentCards fragmentCards = new fragmentCards();

        fragmentList.setArguments(args);
        fragmentTiles.setArguments(args);
        fragmentCards.setArguments(args);

        tabsAdapter.addFragment(fragmentList, getString(R.string.TAB_LIST_TITLE));
        tabsAdapter.addFragment(fragmentTiles, getString(R.string.TAB_TILES_TITLE));
        tabsAdapter.addFragment(fragmentCards, getString(R.string.TAB_CARDS_TITLE));

        viewPager.setOffscreenPageLimit(2); // Carga todas las tabs al entrar a la aplicación. El parámetro se calcula de: (nº total tabs / 2) + 1.
        viewPager.setAdapter(tabsAdapter);
    }

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
        switch(item.getItemId()){
            case R.id.addBill: // Registrar una factura nueva
                startActivity(new Intent(TabContainer.this, RegisterBill.class));
                break;
            default:
                throw new IllegalArgumentException("No se ha podido reconocer el botón presionado.");
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadTicketsFromDB() {
        //Abrimos la base de datos 'bdpeliculas' en modo escritura
        TicketSQLiteHelper ticketHelper = new TicketSQLiteHelper(this, null, 1);

        mTicketList = ticketHelper.getTicketsFromBD();
    }

    public int getTotalTickets(){
        return mTicketList.size();
    }

    public void setUpDrawer(){
        PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName(R.string.DRAWER_OPTION_BILLS)
                .withIcon(getFontAwesomeIcon(FontAwesome.Icon.faw_file2, Color.DKGRAY, 24));

        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.DRAWER_OPTION_CATEGORIES)
                .withIcon(getFontAwesomeIcon(FontAwesome.Icon.faw_folder, Color.DKGRAY, 24));

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Miguel Ángel Vicente Baeza")
                                .withEmail("miguel.vicente@goumh.umh.es")
                                .withIcon(R.mipmap.ic_launcher_round)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch(position){
                            case 0:
                                Toast.makeText(TabContainer.this, "Has presionado el botón de las facturas.", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(TabContainer.this, "Has presionado el botón de las categorías.", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                throw new IllegalArgumentException("The pressed button has not been recognised.");
                        }
                        return true;
                    }
                })
                .withActionBarDrawerToggle(true)
                .build();
    }

    public IconicsDrawable getFontAwesomeIcon(FontAwesome.Icon icon, int color, int dp){

        return new IconicsDrawable(this)
                .icon(icon)
                .color(color)
                .sizeDp(dp);
    }
}