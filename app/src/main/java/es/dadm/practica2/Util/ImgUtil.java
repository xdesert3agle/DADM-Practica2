package es.dadm.practica2.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.File;
import java.io.FileOutputStream;

import es.dadm.practica2.R;

public class ImgUtil {
    public final static String DEFAULT_IMG_FILENAME = "default.jpg";

    private ImgUtil(){}

    // Guarda una imagen en la memoria externa
    public static void saveImage(Bitmap bitmap, String filename, Context context){
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);

        try {
            FileOutputStream out = new FileOutputStream(file);
            scaleBitmap(bitmap, 750).compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Guarda una imagen del drawable en la memoria externa
    public static void saveImageFromDrawable(int imgID, String filename, Context context){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgID);
        ImgUtil.saveImage(bitmap, filename, context);
    }

    // Elimina una imagen de la memoria externa
    public static void deleteImage(String filename, Context context){
        File file = getImgFile(filename, context);

        if (file.exists()){
            file.delete();
        }
    }

    // Escala a la anchura deseada una imagen
    private static Bitmap scaleBitmap(Bitmap source, int width){
        float aspectRatio = source.getWidth() / (float)source.getHeight();
        int height = Math.round(width / aspectRatio);

        return Bitmap.createScaledBitmap(source, width, height, false);
    }

    // Devuelve en un File una imagen de la memoria externa
    public static File getImgFile(String filename, Context context){
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
    }

    // Devuelve en un Bitmap una imagen de la memoria externa
    public static Bitmap getImageAsBitmap(String filename, Context context){
        return BitmapFactory.decodeFile(getImgFile(filename, context).getAbsolutePath());
    }

    // Devuelve un icono de la libreria 'Iconics'
    public static IconicsDrawable getFontAwesomeIcon(FontAwesome.Icon icon, int color, int dp, Context context) {
        return new IconicsDrawable(context)
                .icon(icon)
                .color(color)
                .sizeDp(dp);
    }
}