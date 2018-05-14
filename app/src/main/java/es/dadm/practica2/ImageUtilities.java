package es.dadm.practica2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class ImageUtilities {

    public ImageUtilities(){}

    public static void saveImage(Bitmap bitmap, String filename, Context context){

        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);

        if (file.exists()){
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getImgFile(String filename, Context context){
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
    }

    public static Bitmap getImageAsBitmap(String filename, Context context){
        return BitmapFactory.decodeFile(getImgFile(filename, context).getAbsolutePath());
    }
}