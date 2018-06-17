package es.dadm.practica2.Screens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.r0adkll.slidr.Slidr;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Objects.Category;
import es.dadm.practica2.Objects.CategoryUtil;
import es.dadm.practica2.Objects.Ticket;
import es.dadm.practica2.Objects.TicketDB;
import es.dadm.practica2.R;
import es.dadm.practica2.Util.DecimalDigitsInputFilter;
import es.dadm.practica2.Util.ImgUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.EasyPermissions;

public class AddEditTicket extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    @BindView(R.id.ivImg) ImageView ivTicketImg;
    @BindView(R.id.fabActionMenu) FloatingActionsMenu fabActionMenu;
    @BindView(R.id.fabPhotoFromGallery) FloatingActionButton fabPhotoFromGallery;
    @BindView(R.id.fabPhotoFromCamera) FloatingActionButton fabPhotoFromCamera;
    @BindView(R.id.etTitle) EditText etTitle;
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.etPrice) EditText etPrice;
    @BindView(R.id.spCategories) Spinner spCategories;
    @BindView(R.id.tvFormattedAddress) TextView tvFormattedAddress;
    @BindView(R.id.btnGetOCR) Button btnGetOCR;
    @BindView(R.id.btnCreateEditTicket) Button btnCreateEditTicket;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.llLocationContainer) LinearLayout llLocationContainer;
    @BindView(R.id.mapTicketLocation) MapView mapTicketLocation;

    public static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final int CAMERA_REQUEST = 1;
    public static final int GALLERY_REQUEST = 2;
    public static final int LOCATION_REQUEST = 3;

    private TextRecognizer mTextRecognizer;
    private TicketDB mTicketDB = TicketDB.getInstance();
    private CategoryUtil mCategoryUtil = CategoryUtil.getInstance();
    private List<Category> mCategoryList = new ArrayList<>();
    private List<String> mCategoryNamesList = new ArrayList<>();
    private OkHttpClient mHTTPClient = new OkHttpClient();
    private Bitmap mPickedImg;
    private String mImgName;
    private Ticket mNewTicket = new Ticket();
    private Ticket mSelTicket;
    private String mFormattedAddress;
    private GoogleMap mMap;
    private Location mLocation;
    private Bundle mMapBundle = null;
    private boolean permaDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ticket);

        ButterKnife.bind(this);
        Slidr.attach(this);

        btnCreateEditTicket.setOnClickListener(this);
        btnGetOCR.setOnClickListener(this);
        fabPhotoFromGallery.setOnClickListener(this);
        fabPhotoFromCamera.setOnClickListener(this);

        // Si se tienen permisos de localización se inicializa el mapa
        if (hasLocationPermision()){
            initMap();
        }

        if (savedInstanceState != null) {
            mMapBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        // Se recoge el array de categorías del fichero .json
        mCategoryList = mCategoryUtil.getCategories(this);

        // Se obtiene un array con los nombres de las categorías para asignarlo al Spinner
        fillCategoryNamesArray();
        ArrayAdapter<String> spCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mCategoryNamesList);
        spCategories.setAdapter(spCategoryAdapter);

        btnGetOCR.setCompoundDrawables(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_search, Color.WHITE, 16, this), null, null, null);
        etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        mTextRecognizer = new TextRecognizer.Builder(this).build();

        // Se colocan los iconos a los botones de la galería y de la cámara
        fabPhotoFromGallery.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_image, Color.WHITE, 26, AddEditTicket.this));
        fabPhotoFromCamera.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_camera, Color.WHITE, 26, AddEditTicket.this));

        if (isEditMode()){ // Si el usuario ha entrado a editar un ticket...
            int targetTicketID = getIntent().getExtras().getInt(FragmentList.TAG_TICKET_POSITION);

            mSelTicket = mTicketDB.getTicketWithID(targetTicketID);

            ivTicketImg.setImageBitmap(ImgUtil.getImageAsBitmap(mSelTicket.getImgFilename(), this));
            etTitle.setText(mSelTicket.getTitle());
            etDescription.setText(mSelTicket.getDescription());
            etPrice.setText(String.valueOf(new DecimalFormat("#.00").format(mSelTicket.getPrice())));
            btnCreateEditTicket.setText(R.string.BTN_EDIT_TICKET);
            mPickedImg = ImgUtil.getImageAsBitmap(mSelTicket.getImgFilename(), this);

            if (mSelTicket.getAddress() != null){
                tvFormattedAddress.setText(mSelTicket.getAddress());
            }

            mToolbar.setTitle(R.string.TITLE_EDIT_TICKET);
        } else { // Si el usuario ha entrado a añadir un ticket nuevo...
            mToolbar.setTitle(R.string.TITLE_ADD_TICKET);
        }

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnCreateEditTicket:
                if (!emptyFieldsLeft()){
                    if (!isEditMode()) { // Si el usuario entró a añadir un ticket nuevo
                        insertNewTicket();
                    } else { // Si el usuario entró a editar un ticket existente
                        updateSelectedTicket();
                    }

                    finish();
                }

                break;

            case R.id.fabPhotoFromGallery: // Opción 1 presionada. La foto viene de la galería
                openGallery();

                break;

            case R.id.fabPhotoFromCamera: // Opción 2 presionada. La foto viene de la cámara de fotos
                openCamera();

                break;

            case R.id.btnGetOCR: // Opción 3 presionada. Botón del OCR. Se busca texto en la imagen y si se encuentra se pone en la descripción
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                if (mPickedImg != null) {
                    String OCR = getOCRFromBitmap(mPickedImg);

                    if (!OCR.isEmpty()) {
                        etDescription.setText(OCR);
                        mNewTicket.setOCRtext(OCR);

                        Toast.makeText(this, R.string.MSG_OCR_TEXT_FOUND, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.MSG_OCR_NO_TEXT_FOUND, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, R.string.MSG_OCR_NO_IMG_SELECTED, Toast.LENGTH_SHORT).show();
                }

                break;
        }

        fabActionMenu.collapse();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override // Error al obtener una imagen
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(AddEditTicket.this, R.string.MSG_IMG_PICK_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {

                // Se guarda la imagen en un 'File' y se convierte a 'Bitmap' para ponersela al formulario
                File ticketImgFile = imagesFiles.get(0);
                mImgName = ticketImgFile.getName();

                mPickedImg = BitmapFactory.decodeFile(ticketImgFile.getAbsolutePath());

                // Se guarda la imagen en la memoria externa del teléfono
                ImgUtil.saveImage(mPickedImg, mImgName, AddEditTicket.this);

                // Se pone la imagen en el formulario
                ivTicketImg.setImageBitmap(mPickedImg);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        llLocationContainer.setVisibility(View.GONE);

        if (hasLocationPermision()) { // Si el usuario ha aprobado los permisos de localizacion se saca el mapa
            llLocationContainer.setVisibility(View.VISIBLE);
            mapTicketLocation.onResume();
        } else {
            if (!permaDenied) { // Si no los ha denegado permanentemente los pedimos (tambien sirve para cuando entra por primera vez)
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

    private void requestLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            llLocationContainer.setVisibility(View.VISIBLE);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_LOCATION_RAT), AddEditTicket.LOCATION_REQUEST, perms);
        }
    }

    // Abrir la cámara. Respetando permisos
    private void openCamera() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            EasyImage.openCamera(AddEditTicket.this, 0);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_CAMERA_RAT),
                    CAMERA_REQUEST, perms);
        }
    }

    // Abrir la galería. Respeta permisos
    private void openGallery() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            EasyImage.openGallery(AddEditTicket.this, 0);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_GALLERY_RAT),
                    GALLERY_REQUEST, perms);
        }
    }

    // Devuelve si el usuario ha dado permiso para usar su localización
    private boolean hasLocationPermision(){
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        return EasyPermissions.hasPermissions(this, perms);
    }

    // Inicializa el mapa con un Callback
    private void initMap(){
        mapTicketLocation.onCreate(mMapBundle);
        mapTicketLocation.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Se le pasan los request a la librería 'EasyPermissions' para que los gestione ella
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("MissingPermission") // EasyPermissions gestiona los permisos, pero Android sigue creyendo que no se han respetado
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                EasyImage.openCamera(AddEditTicket.this, 0);
                break;

            case GALLERY_REQUEST:
                EasyImage.openGallery(AddEditTicket.this, 0);
                break;

            case LOCATION_REQUEST:
                llLocationContainer.setVisibility(View.VISIBLE);
                initMap();

                break;
        }
    }

    // Si se rechazan los permisos
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            permaDenied = true;
        }
    }

    // Recoge la información del formulario introducida por el usuario
    public void fetchNewTicketInfo(){

        // Se recoge la información del formulario
        mNewTicket.setTitle(etTitle.getText().toString());
        mNewTicket.setDescription(etDescription.getText().toString());
        mNewTicket.setPrice(Double.parseDouble(etPrice.getText().toString()));
        mNewTicket.setCategory(spCategories.getSelectedItem().toString());

        if (mImgName != null) {
            mNewTicket.setImgFilename(mImgName);
        }

        if (hasLocationPermision()) {
            mNewTicket.setLatitude(mLocation.getLatitude());
            mNewTicket.setLongitude(mLocation.getLongitude());
        }
    }

    // Inserta un ticket nuevo en la base de datos con los datos que el usuario ha proporcionado
    public void insertNewTicket(){
        fetchNewTicketInfo();
        mTicketDB.insertTicket(mNewTicket);
    }

    // Updatea la información de un ticket
    public void updateSelectedTicket(){

        // Se recoge la información del formulario, haya cambiado o no
        mSelTicket.setTitle(etTitle.getText().toString());
        mSelTicket.setDescription(etDescription.getText().toString());
        mSelTicket.setPrice(Double.parseDouble(etPrice.getText().toString()));
        mSelTicket.setCategory(spCategories.getSelectedItem().toString());

        if (mImgName != null){ // La imagen solo se setea si ha cambiado
            ImgUtil.deleteImage(mSelTicket.getImgFilename(), this);
            mSelTicket.setImgFilename(mImgName);
        }

        mTicketDB.updateTicket(mSelTicket);
    }

    // Controla que no se dejen campos en blanco en el formulario de añadir un ticket
    public boolean emptyFieldsLeft(){
        int empty = 0;

        if (etTitle.getText().toString().isEmpty()) {
            etTitle.setError(getString(R.string.MSG_EMPTY_TITLE));
            empty++;
        }

        if (etDescription.getText().toString().isEmpty()) {
            etDescription.setError(getString(R.string.MSG_EMPTY_DESC));
            empty++;
        }

        if (etPrice.getText().toString().isEmpty()) {
            etPrice.setError(getString(R.string.MSG_EMPTY_PRICE));
            empty++;
        }

        return empty > 0;
    }

    // Devuelve si el usuario entró a añadir un ticket nuevo o a editar uno existente
    public boolean isEditMode(){
        return getIntent().getExtras() != null;
    }

    // Resultado del Callback
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (hasLocationPermision()){
            if (!isEditMode()){ // Si el usuario entró a añadir un ticket nuevo
                mMap.setMyLocationEnabled(true);

                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        mLocation = location;

                        LatLng myLatLng = new LatLng(mLocation.getLatitude(),
                                mLocation.getLongitude());

                        CameraPosition myPosition = new CameraPosition.Builder()
                                .target(myLatLng).zoom(17).bearing(90).tilt(30).build();
                        mMap.moveCamera(
                                CameraUpdateFactory.newCameraPosition(myPosition));

                        printFormattedAddress();
                    }
                });

                mMap.setOnMyLocationButtonClickListener(this);
                mMap.setOnMyLocationClickListener(this);
            } else { // Si el usuario entró a editar un ticket...
                if (mSelTicket.getLatitude() != 0 && mSelTicket.getLongitude() != 0) {
                    markTicketLocationOnMap();
                } else {
                    llLocationContainer.setVisibility(View.GONE);
                }

            }
        }
    }

    // Marca en el mapa la cocalización del ticket seleccionado (cuando el usuario entra a editar)
    public void markTicketLocationOnMap(){
        LatLng latLng = new LatLng(mSelTicket.getLatitude(), mSelTicket.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(""));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    // Hace una petición con la API de Google Geocoding para obtener la dirección completa del usuario en modo texto
    public void printFormattedAddress() {
        String parsedLatitude = String.valueOf(mLocation.getLatitude()).replace(",", ".");
        String parsedLongitude = String.valueOf(mLocation.getLongitude()).replace(",", ".");

        Request request = new Request.Builder()
                .url(String.format(getResources().getString(R.string.GEOCODING_HTTP_REQUEST_URL), parsedLatitude, parsedLongitude, getResources().getString(R.string.GEOCODING_API_KEY)))
                .build();

        mHTTPClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonObject jsonResponse = new JsonParser().parse(response.body().string()).getAsJsonObject();

                JsonArray jsonResults = jsonResponse.get("results").getAsJsonArray();

                mFormattedAddress = jsonResults.size() >= 3 ? jsonResults.get(2).getAsJsonObject().get("formatted_address").getAsString() : jsonResults.get(jsonResults.size() - 1).getAsJsonObject().get("formatted_address").getAsString();

                mNewTicket.setAddress(mFormattedAddress);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvFormattedAddress.setText(mFormattedAddress);
                    }
                });
            }
        });
    }

    // Devuelve el texto encontrado en un Bitmap (OCR)
    public String getOCRFromBitmap(Bitmap bitmap){
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<TextBlock> textBlocks = mTextRecognizer.detect(frame);
        String imageOCR = "";

        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock tBlock = textBlocks.valueAt(i);

            for (Text line : tBlock.getComponents()) {
                imageOCR = imageOCR + line.getValue() + "\n";
            }
        }

        return imageOCR;
    }

    // Coge todos los nombres de las categorías y los mete a un array para ser usado en el Spinner
    public void fillCategoryNamesArray(){
        int size = mCategoryList.size();

        for (int i = 0; i < size; i++) {
            mCategoryNamesList.add(mCategoryList.get(i).getTitle());
        }
    }
}
