package es.dadm.practica2.Screens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.r0adkll.slidr.Slidr;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Objects.Category;
import es.dadm.practica2.Objects.CategoryUtil;
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
    @BindView(R.id.etDetails) EditText etDetails;
    @BindView(R.id.btnCreateEditCategory) Button btnCreateEditCategory;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    public final static int CAMERA_REQUEST = 1;
    public final static int GALLERY_REQUEST = 2;

    private Category mNewCategory;
    private Category mSelCategory;
    private int mSelCategoryPosition;
    private String mImgName;
    private CategoryUtil mCategoryUtil = CategoryUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        ButterKnife.bind(this);
        Slidr.attach(this);

        // OnClickListeners de los botones
        btnCreateEditCategory.setOnClickListener(this);
        fabPhotoFromGallery.setOnClickListener(this);
        fabPhotoFromCamera.setOnClickListener(this);

        // Se colocan los iconos a los botones de la galería y de la cámara
        fabPhotoFromGallery.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_image, Color.WHITE, 26, AddEditCategory.this));
        fabPhotoFromCamera.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_camera, Color.WHITE, 26, AddEditCategory.this));

        if (isEditMode()){ // Si el usuario ha entrado a editar una categoría...
            mSelCategoryPosition = getIntent().getExtras().getInt(Categories.TAG_CATEGORY_POSITION);

            mSelCategory = mCategoryUtil.getCategory(mSelCategoryPosition);

            ivCategoryImg.setImageBitmap(ImgUtil.getImageAsBitmap(mSelCategory.getImgFilename(), this));
            etTitle.setText(mSelCategory.getTitle());
            etDescription.setText(mSelCategory.getDescription());
            etDetails.setText(mSelCategory.getDetails());
            btnCreateEditCategory.setText(R.string.BTN_EDIT_CATEGORY);

            mToolbar.setTitle(R.string.TITLE_EDIT_CATEGORY);
        } else { // Si el usuario ha entrado a añadir una categoría nueva...
            mToolbar.setTitle(R.string.TITLE_ADD_CATEGORY);
        }

        setSupportActionBar(mToolbar);

        // Botón de atrás
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
            case R.id.btnCreateEditCategory:
                if (!isEditMode()) { // Si el usuario entró a editar una cateogría
                    if (!emptyFieldsLeft()){ // Checkeo de campos en blanco
                        insertNewCategory();
                    } else {
                        Toast.makeText(this, R.string.MSG_EMPTY_FIELDS, Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else { // Si entró a añadir una categoría nueva
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
            @Override // Error al coger la imagen
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {

                Toast.makeText(AddEditCategory.this, R.string.MSG_IMG_PICK_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override // Imagen cogida correctamente
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {

                // Se guarda la imagen en un 'File' y se convierte a 'Bitmap' para ponersela al formulario
                File categoryImgFile = imagesFiles.get(0);
                mImgName = categoryImgFile.getName();

                Bitmap bmCategoryImg = BitmapFactory.decodeFile(categoryImgFile.getAbsolutePath());

                // Se guarda la imagen en la memoria externa del teléfono
                ImgUtil.saveImage(bmCategoryImg, mImgName, AddEditCategory.this);

                // Se pone la imagen en el header del formulario
                ivCategoryImg.setImageBitmap(bmCategoryImg);
            }
        });
    }

    // Abrir la cámara. Respetando permisos
    private void openCamera() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            EasyImage.openCamera(AddEditCategory.this, 0);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.MSG_CAMERA_RAT),
                    CAMERA_REQUEST, perms);
        }
    }

    // Abrir la galería. Respeta permisos
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

        // Se le pasan los request a la librería 'EasyPermissions' para que los gestione ella
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("MissingPermission") // EasyPermissions gestiona los permisos, pero Android sigue creyendo que no se han respetado
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
        // Si algun permiso ha sido denegado permanentemente...
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public void fetchNewCategoryInfo(){
        mNewCategory = new Category();

        // Se recoge la información del formulario
        mNewCategory.setId(mCategoryUtil.getNewID());
        mNewCategory.setTitle(etTitle.getText().toString());
        mNewCategory.setDescription(etDescription.getText().toString());
        mNewCategory.setDetails(etDescription.getText().toString());
        mNewCategory.setImgFilename(mImgName);
    }

    public void insertNewCategory(){
        fetchNewCategoryInfo();
        mCategoryUtil.addCategory(mNewCategory, this);
    }

    public void updateSelectedCategory(){

        // Se recoge la información del formulario, haya cambiado o no
        mSelCategory.setTitle(etTitle.getText().toString());
        mSelCategory.setDescription(etDescription.getText().toString());
        mSelCategory.setDetails(etDetails.getText().toString());

        if (mImgName != null){ // La imagen solo se setea si ha cambiado
            ImgUtil.deleteImage(mSelCategory.getImgFilename(), this);
            mSelCategory.setImgFilename(mImgName);
        }

        mCategoryUtil.updateCategory(mSelCategoryPosition, mSelCategory, this);
    }

    // Comprueba que no hayan campos vacíos en el formulario, ya que en la base de datos todos los campos son NOT NULL
    public boolean emptyFieldsLeft(){
        return etTitle.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty() || etDetails.getText().toString().isEmpty();
    }

    // Devuelve si el usuario entró a editar o a añadir una categoría nueva
    public boolean isEditMode(){
        return getIntent().getExtras() != null;
    }
}