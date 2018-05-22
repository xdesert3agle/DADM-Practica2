package es.dadm.practica2.Screens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import es.dadm.practica2.Util.ImgUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Util.DecimalDigitsInputFilter;
import es.dadm.practica2.R;
import es.dadm.practica2.Objects.Ticket;
import es.dadm.practica2.Objects.TicketDB;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AppSettingsDialog;
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
    @BindView(R.id.btnCreateEditTicket) Button btnCreateEditTicket;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.mapTicketLocation) MapView mapTicketLocation;

    public final static int CAMERA_REQUEST = 1;
    public final static int GALLERY_REQUEST = 2;
    public final static int LOCATION_REQUEST = 3;

    private GoogleMap mMap;

    private TextRecognizer mTextRecognizer;

    private TicketDB mTicketDB = TicketDB.getInstance();
    private String mImgName;
    private Ticket mNewTicket;
    private Ticket mSelTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ticket);

        ButterKnife.bind(this);

        btnCreateEditTicket.setOnClickListener(this);
        fabPhotoFromGallery.setOnClickListener(this);
        fabPhotoFromCamera.setOnClickListener(this);

        mapTicketLocation.getMapAsync(this);

        etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        mTextRecognizer = new TextRecognizer.Builder(this).build();

        // Se colocan los iconos a los botones de la galería y de la cámara
        fabPhotoFromGallery.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_image, Color.WHITE, 26, AddEditTicket.this));
        fabPhotoFromCamera.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_camera, Color.WHITE, 26, AddEditTicket.this));

        if (isEditMode()){ // Si el usuario ha entrado a editar un ticket...
            int targetTicketID = getIntent().getExtras().getInt(fragmentList.TAG_TICKET_POSITION);

            mSelTicket = mTicketDB.getTicketWithID(targetTicketID);

            ivTicketImg.setImageBitmap(ImgUtil.getImageAsBitmap(mSelTicket.getImgFilename(), this));
            etTitle.setText(mSelTicket.getTitle());
            etDescription.setText(mSelTicket.getDescription());
            etPrice.setText(String.valueOf(new DecimalFormat("#.00").format(mSelTicket.getPrice())));
            btnCreateEditTicket.setText(R.string.BTN_EDIT_TICKET);

            mToolbar.setTitle(R.string.TITLE_EDIT_TICKET);
        } else { // Si el usuario ha entrado a añadir un ticket nuevo...
            mToolbar.setTitle(R.string.TITLE_ADD_TICKET);
        }

        setSupportActionBar(mToolbar);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnCreateEditTicket:
                if (!isEditMode()) {
                    if (!emptyFieldsLeft()){
                        insertNewTicket();
                    } else {
                        Toast.makeText(this, R.string.MSG_EMPTY_FIELDS, Toast.LENGTH_SHORT).show();
                        break;
                    }

                } else {
                    updateSelectedTicket();
                }

                finish();
                break;

            case R.id.fabPhotoFromGallery: // Opción 1 presionada. La foto viene de la galería
                openGallery();

                break;

            case R.id.fabPhotoFromCamera: // Opción 2 presionada. La foto viene de la cámara de fotos
                openCamera();

                break;
        }

        fabActionMenu.collapse();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(AddEditTicket.this, R.string.MSG_IMG_PICK_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {

                // Se guarda la imagen en un 'File' y se convierte a 'Bitmap' para ponersela al formulario
                File ticketImgFile = imagesFiles.get(0);
                mImgName = ticketImgFile.getName();

                Bitmap bmTicketImg = BitmapFactory.decodeFile(ticketImgFile.getAbsolutePath());

                // Se guarda la imagen en la memoria externa del teléfono
                ImgUtil.saveImage(bmTicketImg, mImgName, AddEditTicket.this);

                // Se pone la imagen en el formulario
                ivTicketImg.setImageBitmap(bmTicketImg);

                Log.d("OCR", getOCRFromImage(bmTicketImg));
            }
        });
    }

    private void openCamera() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            EasyImage.openCamera(AddEditTicket.this, 0);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_CAMERA_RAT),
                    CAMERA_REQUEST, perms);
        }
    }

    private void openGallery() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            EasyImage.openGallery(AddEditTicket.this, 0);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_GALLERY_RAT),
                    GALLERY_REQUEST, perms);
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            mMap.setMyLocationEnabled(true);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_GALLERY_RAT),
                    LOCATION_REQUEST, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("MissingPermission")
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
                Log.d("Concencido", "concedido");
                mMap.setMyLocationEnabled(true);
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public void fetchNewTicketInfo(){
        mNewTicket = new Ticket();

        // Se recoge la información del formulario
        mNewTicket.setTitle(etTitle.getText().toString());
        mNewTicket.setDescription(etDescription.getText().toString());
        mNewTicket.setPrice(Double.parseDouble(etPrice.getText().toString()));
        mNewTicket.setCategory(spCategories.getSelectedItem().toString());
        mNewTicket.setImgFilename(mImgName);
    }

    public void insertNewTicket(){
        fetchNewTicketInfo();
        mTicketDB.insertTicket(mNewTicket);
    }

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

    public boolean emptyFieldsLeft(){
        return etTitle.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty() || etPrice.getText().toString().isEmpty() || mImgName == null;
    }

    public boolean isEditMode(){
        return getIntent().getExtras() != null;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.

        Log.d("OnMapReady antes de request", "OnMapReady antes de request");

        requestLocation();

        Log.d("OnMapReady despues de request", "OnMapReady despues de request");

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public String getOCRFromImage(Bitmap bitmap){
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
}