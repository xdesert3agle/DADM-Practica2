package es.dadm.practica2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AddEditTicket extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.ivTicketImg) ImageView ivTicketImg;
    @BindView(R.id.fabActionMenu) FloatingActionsMenu fabActionMenu;
    @BindView(R.id.fabPhotoFromGallery) FloatingActionButton fabPhotoFromGallery;
    @BindView(R.id.fabPhotoFromCamera) FloatingActionButton fabPhotoFromCamera;
    @BindView(R.id.etTitle) EditText etTitle;
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.etPrice) EditText etPrice;
    @BindView(R.id.spCategories) Spinner spCategories;
    @BindView(R.id.btnCreateEditTicket) Button btnCreateEditTicket;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private TicketDB mTicketDB;
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

        etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        // Se colocan los iconos a los fab
        fabPhotoFromGallery.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_image, Color.WHITE, 26, AddEditTicket.this));
        fabPhotoFromCamera.setIconDrawable(ImgUtil.getFontAwesomeIcon(FontAwesome.Icon.faw_camera, Color.WHITE, 26, AddEditTicket.this));

        mTicketDB = TicketDB.getInstance();

        if (isEditMode()){ // Si el usuario ha entrado a editar
            int targetTicketID = getIntent().getExtras().getInt(fragmentList.TAG_TICKET_POSITION);

            mSelTicket = mTicketDB.getTicketWithID(targetTicketID);

            ivTicketImg.setImageBitmap(ImgUtil.getImageAsBitmap(mSelTicket.getImgFilename(), this));
            etTitle.setText(mSelTicket.getTitle());
            etDescription.setText(mSelTicket.getDescription());
            etPrice.setText(String.valueOf(mSelTicket.getPrice()));
            btnCreateEditTicket.setText(R.string.BTN_EDIT_TICKET);

            mToolbar.setTitle(R.string.TITLE_EDIT_TICKET);
        } else { // Si el usuario ha entrado a añadir un ticket nuevo
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
                EasyImage.openGallery(AddEditTicket.this, 0);
                fabActionMenu.collapse();

                break;

            case R.id.fabPhotoFromCamera: // Opción 2 presionada. La foto viene de la cámara de fotos
                EasyImage.openCamera(AddEditTicket.this, 0);
                fabActionMenu.collapse();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(AddEditTicket.this, "Ha habido un error al procesar la imagen.", Toast.LENGTH_SHORT).show();
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
            }
        });
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
}