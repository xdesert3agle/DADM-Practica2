package es.dadm.practica2.Screens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Objects.Ticket;
import es.dadm.practica2.Objects.TicketDB;
import es.dadm.practica2.R;
import es.dadm.practica2.Util.DrawerMenuActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class TicketLocations extends DrawerMenuActivity implements EasyPermissions.PermissionCallbacks, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    @BindView(R.id.mapTicketLocation) MapView mapTicketLocation;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private GoogleMap mMap;
    private static List<String> locationPerms = new ArrayList<>();
    private Bundle mMapBundle = null;
    private Location mLocation;
    private TicketDB mTicketDB = TicketDB.getInstance();
    private List<Ticket> mTicketList = new ArrayList<>();
    private DisplayMetrics mMetrics = new DisplayMetrics();
    private LoadToast lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_locations);

        ButterKnife.bind(this);

        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

         lt = new LoadToast(TicketLocations.this)
                .setText("Buscando tu localización...")
                .setTranslationY(mMetrics.heightPixels * 5/8)
                .setProgressColor(Color.RED)
                .show();


        mTicketList = mTicketDB.getTicketsFromBD();


        setSupportActionBar(mToolbar);
        setToolbarLocationCount();

        if (hasLocationPermision()){
            initMap();
        }

        if (savedInstanceState != null) {
            mMapBundle = savedInstanceState.getBundle(AddEditTicket.MAP_VIEW_BUNDLE_KEY);
        }

        setUpDrawer(mToolbar);
        drawer.setSelectionAtPosition(3);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasLocationPermision()) {
            mapTicketLocation.onResume();
        } else {
            locationPerms.add(0, Manifest.permission.ACCESS_COARSE_LOCATION);
            locationPerms.add(1, Manifest.permission.ACCESS_FINE_LOCATION);

            if (!EasyPermissions.somePermissionPermanentlyDenied(this, locationPerms)) {
                requestLocation();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (hasLocationPermision()) {
            mapTicketLocation.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (hasLocationPermision()) {
            mapTicketLocation.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (hasLocationPermision()) {
            mapTicketLocation.onLowMemory();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case AddEditTicket.LOCATION_REQUEST:
                initMap();

                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (hasLocationPermision()){
            mMap.setMyLocationEnabled(true);

            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    mLocation = location;

                    LatLng myLatLng = new LatLng(mLocation.getLatitude(),
                            mLocation.getLongitude());

                    CameraPosition myPosition = new CameraPosition.Builder()
                            .target(myLatLng)
                            .zoom(5)
                            .tilt(30)
                            .build();

                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(myPosition));

                    lt.success();
                }
            });

            markAllTicketsLocationOnMap();

            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        switch (position) {
            case 1:
                startActivity(new Intent(TicketLocations.this, TicketTabContainer.class));
                drawer.closeDrawer();
                break;

            case 2:
                drawer.closeDrawer();
                startActivity(new Intent(TicketLocations.this, Categories.class));
                break;

            case 3:
                drawer.closeDrawer();
                break;

            default:
                throw new IllegalArgumentException("No se ha podido reconocer el botón presionado.");
        }
        return true;
    }

    private void requestLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_LOCATION_RAT), AddEditTicket.LOCATION_REQUEST, perms);
        }
    }

    private boolean hasLocationPermision(){
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        return EasyPermissions.hasPermissions(this, perms);
    }

    private void initMap(){
        mapTicketLocation.onCreate(mMapBundle);
        mapTicketLocation.getMapAsync(this);
    }

    public void markAllTicketsLocationOnMap(){
        int size = mTicketList.size();

        for (int i = 0; i < size; i++) {
            LatLng latLng = new LatLng(mTicketList.get(i).getLatitude(), mTicketList.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(""));
        }
    }

    public void setToolbarLocationCount(){
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.TITLE_TICKET_LOCATIONS), getTicketsWithLocation()));
    }

    public int getTicketsWithLocation(){
        int size = mTicketList.size();
        int n_tickets = 0;

        for (int i = 0; i < size; i++) {
            if (mTicketList.get(i).getLatitude() != 0 && mTicketList.get(i).getLongitude() != 0) {
                n_tickets++;
            }
        }

        return n_tickets;
    }
}
