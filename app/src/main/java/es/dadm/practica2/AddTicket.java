package es.dadm.practica2;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AddTicket extends AppCompatActivity {
    @BindView(R.id.ivTicketImg) ImageView ivTicketImg;
    @BindView(R.id.fabPhotoFromGallery) FloatingActionButton fabAddPhoto;
    @BindView(R.id.etTitle) EditText etTitle;
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.etPrice) EditText etPrice;
    @BindView(R.id.spCategories) Spinner spCategories;
    @BindView(R.id.btnCreateTicket) Button btnCreateTicket;

    static final int PICK_IMAGE = 1;
    String[] galleryPermissions = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    TicketDB mTicketDB;
    File mTicketImgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.ADD_TICKET_TITLE));
        setSupportActionBar(toolbar);

        mTicketDB = TicketDB.getInstance();

        btnCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ticket newTicket = fetchTicketInfo();

                mTicketDB.insertTicket(newTicket);

                finish();
            }
        });

        fabAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openGallery(AddTicket.this, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(AddTicket.this, "Ha habido un error al procesar la imagen.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {

                // Se guarda la imagen en un 'File' y se convierte a 'Bitmap' para ponersela al formulario
                mTicketImgFile = imagesFiles.get(0);
                Bitmap bmTicketImg = BitmapFactory.decodeFile(mTicketImgFile.getAbsolutePath());

                // Se guarda la imagen en la memoria externa del teléfono
                ImageUtilities.saveImage(bmTicketImg, mTicketImgFile.getName(), AddTicket.this);

                // Se pone la imagen en el formulario
                ivTicketImg.setImageBitmap(bmTicketImg);
            }
        });
    }

    public Ticket fetchTicketInfo(){
        Ticket ticket = new Ticket();

        ticket.setTitle(etTitle.getText().toString());
        ticket.setDescription(etDescription.getText().toString());
        ticket.setPrice(Double.parseDouble(etPrice.getText().toString()));
        ticket.setCategory(spCategories.getSelectedItem().toString());
        ticket.setImgFilename(mTicketImgFile.getName());

        return ticket;
    }
}