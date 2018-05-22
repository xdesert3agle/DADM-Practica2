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
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Objects.Category;
import es.dadm.practica2.Objects.CategoryList;
import es.dadm.practica2.Objects.Ticket;
import es.dadm.practica2.R;
import es.dadm.practica2.Util.ImgUtil;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddEditCategory extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.ivImg) ImageView ivCategoryImg;
    @BindView(R.id.fabActionMenu) FloatingActionsMenu fabActionMenu;
    @BindView(R.id.fabPhotoFromGallery) FloatingActionButton fabPhotoFromGallery;
    @BindView(R.id.fabPhotoFromCamera) FloatingActionButton fabPhotoFromCamera;
    @BindView(R.id.etTitle) EditText etTitle;
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.btnCreateEditCategory) Button btnCreateEditCategory;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    public final static int CAMERA_REQUEST = 1;
    public final static int GALLERY_REQUEST = 2;
    private final String CATEGORY_FILE_PATH = "categories.txt";

    private String mImgName;
    private Category mNewCategory;
    private Category mSelCategory;
    private CategoryList mCategoryList = new CategoryList(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        ButterKnife.bind(this);

        btnCreateEditCategory.setOnClickListener(this);
        fabPhotoFromGallery.setOnClickListener(this);
        fabPhotoFromCamera.setOnClickListener(this);

        // Se colocan los iconos a los botones de la galería y de la cámara
        fabPhotoFromGallery.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_image, Color.WHITE, 26, AddEditCategory.this));
        fabPhotoFromCamera.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_camera, Color.WHITE, 26, AddEditCategory.this));

        /*if (isEditMode()){ // Si el usuario ha entrado a editar un ticket...
            int targetCategoryID = getIntent().getExtras().getInt(fragmentList.TAG_TICKET_POSITION);

            mSelCategory = mTicketDB.getTicketWithID(targetCategoryID);

            ivCategoryImg.setImageBitmap(ImgUtil.getImageAsBitmap(mSelCategory.getImgFilename(), this));
            etTitle.setText(mSelCategory.getTitle());
            etDescription.setText(mSelCategory.getDescription());
            btnCreateEditTicket.setText(R.string.BTN_EDIT_CATEGORY);

            mToolbar.setTitle(R.string.TITLE_EDIT_CATEGORY);
        } else { // Si el usuario ha entrado a añadir un ticket nuevo...*/
            mToolbar.setTitle(R.string.TITLE_ADD_CATEGORY);
        //}
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnCreateEditCategory:
                if (!isEditMode()) {
                    if (!emptyFieldsLeft()){
                        Log.d("Info", "Vaya insercion loco");
                        insertNewCategory();
                    } else {
                        Toast.makeText(this, R.string.MSG_EMPTY_FIELDS, Toast.LENGTH_SHORT).show();
                        break;
                    }

                } else {
                    updateSelectedCategory();
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
                Toast.makeText(AddEditCategory.this, R.string.MSG_IMG_PICK_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {

                // Se guarda la imagen en un 'File' y se convierte a 'Bitmap' para ponersela al formulario
                File categoryImgFile = imagesFiles.get(0);
                mImgName = categoryImgFile.getName();

                Bitmap bmCategoryImg = BitmapFactory.decodeFile(categoryImgFile.getAbsolutePath());

                // Se guarda la imagen en la memoria externa del teléfono
                ImgUtil.saveImage(bmCategoryImg, mImgName, AddEditCategory.this);

                // Se pone la imagen en el formulario
                ivCategoryImg.setImageBitmap(bmCategoryImg);
            }
        });
    }

    private void openCamera() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            EasyImage.openCamera(AddEditCategory.this, 0);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_CAMERA_RAT),
                    CAMERA_REQUEST, perms);
        }
    }

    private void openGallery() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            EasyImage.openGallery(AddEditCategory.this, 0);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_GALLERY_RAT),
                    GALLERY_REQUEST, perms);
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
                EasyImage.openCamera(AddEditCategory.this, 0);
                break;
            case GALLERY_REQUEST:
                EasyImage.openGallery(AddEditCategory.this, 0);
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public void fetchNewCategoryInfo(){
        mNewCategory = new Category();

        // Se recoge la información del formulario
        mNewCategory.setTitle(etTitle.getText().toString());
        mNewCategory.setDescription(etDescription.getText().toString());
        mNewCategory.setImgFilename(mImgName);
    }

    public void insertNewCategory(){
        fetchNewCategoryInfo();
        Log.d("Cantidad antes", String.valueOf(mCategoryList.getCount()));
        mCategoryList.persistToJson(CATEGORY_FILE_PATH, mNewCategory);
        Log.d("Cantidad despues", String.valueOf(mCategoryList.getCount()));
    }

    public void updateSelectedCategory(){

        // Se recoge la información del formulario, haya cambiado o no
        mSelCategory.setTitle(etTitle.getText().toString());
        mSelCategory.setDescription(etDescription.getText().toString());

        if (mImgName != null){ // La imagen solo se setea si ha cambiado
            ImgUtil.deleteImage(mSelCategory.getImgFilename(), this);
            mSelCategory.setImgFilename(mImgName);
        }

        mCategoryList.addCategory(mSelCategory);
    }

    public boolean emptyFieldsLeft(){
        return etTitle.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty() || mImgName == null;
    }

    public boolean isEditMode(){
        return getIntent().getExtras() != null;
    }
}