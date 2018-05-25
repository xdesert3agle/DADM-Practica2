package es.dadm.practica2.Screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
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
import es.dadm.practica2.Adapters.CategoryAdapter;
import es.dadm.practica2.Interfaces.ElementActions;
import es.dadm.practica2.Objects.Category;
import es.dadm.practica2.Objects.CategoryUtil;
import es.dadm.practica2.R;
import es.dadm.practica2.Util.ImgUtil;

public class Categories extends AppCompatActivity {
    @BindView(R.id.rvTickets) RecyclerView mRecycler;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private Drawer mDrawer;
    private CategoryAdapter mAdapter;
    private CategoryUtil mCategoryUtil = CategoryUtil.getInstance();
    private List<Category> mCategoryList = new ArrayList<>();
    private int mSelCategoryPosition;

    public static final String TAG_CATEGORY_POSITION = "Category position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ButterKnife.bind(this);

        registerForContextMenu(mRecycler);

        mCategoryList = mCategoryUtil.getCategories(this);

        setSupportActionBar(mToolbar);
        setToolbarCategoryCount();

        setUpDrawer();

        // Si no hay ninguna categoría en el array se muestra un Toast indicándolo
        if (mCategoryList.size() == 0) Toast.makeText(this, R.string.MSG_START_NO_CATEGORIES_FOUND, Toast.LENGTH_SHORT).show();

        mAdapter = new CategoryAdapter(mCategoryList, this, new ElementActions() {
            @Override
            public void onItemClicked(int position) {
                startActivity(new Intent(Categories.this, AddEditCategory.class).putExtra(TAG_CATEGORY_POSITION, mCategoryList.get(position).getId()));
            }

            @Override
            public void onCreateContextMenu(View view, ContextMenu menu, int position) {
                mSelCategoryPosition = position;

                MenuInflater inflater = new MenuInflater(Categories.this);
                inflater.inflate(R.menu.long_press_menu, menu);
            }
        });

        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCategotyList();
        setToolbarCategoryCount();
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
        switch (item.getItemId()) {
            case R.id.addBill: // Registrar una factura nueva
                startActivity(new Intent(Categories.this, AddEditCategory.class));
                break;
            default:
                throw new IllegalArgumentException("No se ha podido reconocer el botón presionado.");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.lpDeleteElement:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.MSG_CATEGORY_DELETE_CONFIRM)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (mCategoryList.size() > 1) {
                                    deleteSelectedCategory();
                                    refreshCategotyList();
                                } else {
                                    Toast.makeText(Categories.this, R.string.MSG_LAST_CATEGORY_DELETE_ERROR, Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
        }

        return false;
    }

    private void setToolbarCategoryCount(){
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.TITLE_CATEGORY_CONTAINER), mCategoryList.size()));
    }

    public void refreshCategotyList(){
        mAdapter.notifyDataSetChanged();
    }

    public void deleteSelectedCategory(){
        // Se recupera la categoría sobre la que el usuario ha hecho la pulsación larga
        Category selectedCategory = mCategoryUtil.getCategory(mSelCategoryPosition);

        ImgUtil.deleteImage(selectedCategory.getImgFilename(), this); // // Se borra tanto la imagen de la categoría de la memoria externa del teléfono...
        mCategoryUtil.deleteCategory(mSelCategoryPosition, this); // ...como la categoría del array de categorías

        Toast.makeText(this, String.format(getString(R.string.MSG_DELETED_CATEGORY), selectedCategory.getTitle()), Toast.LENGTH_SHORT).show();
        setToolbarCategoryCount();
    }

    public void setUpDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName(R.string.DRAWER_OPTION_TICKETS)
                .withIcon(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_file2, Color.DKGRAY, 24, Categories.this));

        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.DRAWER_OPTION_CATEGORIES)
                .withIcon(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_folder, Color.DKGRAY, 24, Categories.this));

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
                        switch (position) {
                            case 1:
                                startActivity(new Intent(Categories.this, TicketTabContainer.class));
                                mDrawer.closeDrawer();
                                break;

                            case 2:
                                mDrawer.closeDrawer();
                                break;

                            default:
                                throw new IllegalArgumentException("No se ha podido reconocer el botón presionado.");
                        }
                        return true;
                    }
                })
                .withActionBarDrawerToggle(true)
                .build();

        mDrawer.setSelection(item2);
    }
}
