package es.dadm.practica2;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class RegisterBill extends AppCompatActivity {
    @BindView(R.id.ivBillImg) ImageView ivBillImg;

    public static final int PICK_IMAGE = 1;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bill);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registrar nueva factura");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
            startActivityForResult(intent, PICK_IMAGE);
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE){
            switch (resultCode) {
                case -1:
                    Toast.makeText(this, "Imagen escogida correctamente.", Toast.LENGTH_SHORT).show();

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    ImageView imageView = findViewById(R.id.ivBillImg);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                    break;
                case 0:
                    Toast.makeText(this, "Por favor, escoge una imagen de la galería.", Toast.LENGTH_SHORT).show();
                    this.finish();
                    break;
                default:
                    throw new IllegalArgumentException("No se ha podido resolver la acción de pedir una imagen.");
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            default:
                throw new IllegalArgumentException("No se ha podido reconocer el botón presionado.");
        }

        return super.onOptionsItemSelected(item);
    }
}
