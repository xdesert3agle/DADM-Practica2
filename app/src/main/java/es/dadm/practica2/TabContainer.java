package es.dadm.practica2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.util.SectionsPageAdapter;


public class TabContainer extends AppCompatActivity {
    @BindView(R.id.vpContent) ViewPager mViewPager;
    @BindView(R.id.tlTabs) TabLayout mTabLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private SectionsPageAdapter mSectionsPageAdapter;
    private List<Category> mCategoryList = new ArrayList<>();

    private static final String BUNDLE_BILL_LIST = "Bill list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab_container);

        ButterKnife.bind(this);

        setUpViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        // Sets the Activity title to 'My bills (amount)'
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.TAB_CONTAINER_TITLE), getTotalBills()));
    }

    private void setUpViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        Category videogames = new Category();
        mCategoryList.add(videogames);

        Bill facturaPS4 = new Bill();
        facturaPS4.setId(1);
        //facturaPS4.setCategory("Videojuegos");
        facturaPS4.setAmount(299);
        facturaPS4.setTitle("PS4 Slim (1 TB)");
        facturaPS4.setDescription("Prueba descripción larga de PS4");

        Bill facturaGOW = new Bill();
        facturaGOW.setId(2);
        //facturaGOW.setCategory("Videojuegos");
        facturaGOW.setAmount(60);
        facturaGOW.setTitle("God Of War");
        facturaGOW.setDescription("Prueba descripción larga de God of War");

        videogames.getBillList().add(facturaPS4);
        videogames.getBillList().add(facturaGOW);

        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_BILL_LIST, (ArrayList) mCategoryList);

        fragmentList fragmentList = new fragmentList();
        fragmentTiles fragmentTiles = new fragmentTiles();
        fragmentCards fragmentCards = new fragmentCards();

        fragmentList.setArguments(args);
        fragmentTiles.setArguments(args);
        fragmentCards.setArguments(args);

        adapter.addFragment(fragmentList, getString(R.string.TAB_LIST_TITLE));
        adapter.addFragment(fragmentTiles, getString(R.string.TAB_TILES_TITLE));
        adapter.addFragment(fragmentCards, getString(R.string.TAB_CARDS_TITLE));

        viewPager.setOffscreenPageLimit(2); // Carga todas las tabs al entrar a la aplicación. El numero viene de: (nº total tabs / 2) + 1.
        viewPager.setAdapter(adapter);
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
            case R.id.addBill: // Register a new bill button
                Toast.makeText(this, "Register a bill", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalArgumentException("The pressed button has not been recognised.");
        }

        return super.onOptionsItemSelected(item);
    }

    public int getTotalBills(){
        int n = 0;

        for (Category c : mCategoryList) n += c.getNumberOfBills();

        return n;
    }
}
