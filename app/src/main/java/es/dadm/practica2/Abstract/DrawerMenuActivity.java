package es.dadm.practica2.Abstract;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import es.dadm.practica2.R;
import es.dadm.practica2.Util.ImgUtil;

public abstract class DrawerMenuActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    protected Drawer drawer;

    public void setUpDrawer(Toolbar toolbar){
        PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName(R.string.DRAWER_OPTION_TICKETS)
                .withIcon(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_file2, Color.DKGRAY, 24, this));

        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.DRAWER_OPTION_CATEGORIES)
                .withIcon(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_folder, Color.DKGRAY, 24, this));

        PrimaryDrawerItem item3 = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.DRAWER_OPTION_MAP)
                .withIcon(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_map, Color.DKGRAY, 24, this));

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_header_background)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(R.string.DEV_NAME)
                                .withEmail(R.string.DEV_MAIL)
                                .withIcon(R.mipmap.ic_launcher_round)
                )
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        item3
                )
                .withOnDrawerItemClickListener(this)
                .withActionBarDrawerToggle(true)
                .build();
    }
}