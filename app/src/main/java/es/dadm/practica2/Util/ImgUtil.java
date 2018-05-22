package es.dadm.practica2.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.File;
import java.io.FileOutputStream;

public class ImgUtil {

    private ImgUtil(){}

    public static File getImgFile(String filename, Context context){
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
    }

    public static Bitmap getImageAsBitmap(String filename, Context context){
        return BitmapFactory.decodeFile(getImgFile(filename, context).getAbsolutePath());
    }

    public static void saveImage(Bitmap bitmap, String filename, Context context){
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);

        try {
            FileOutputStream out = new FileOutputStream(file);
            scaleBitmap(bitmap, 1/4).compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteImage(String filename, Context context){
        File file = getImgFile(filename, context);

        if (file.exists()){
            file.delete();
        }
    }



    private static Bitmap scaleBitmap(Bitmap source, double scale){
        float aspectRatio = source.getWidth() / (float)source.getHeight();

        int width = 720;
        int height = Math.round(width / aspectRatio);

        return Bitmap.createScaledBitmap(source, width, height, false);
    }

    public static IconicsDrawable getFontAwesomeIcon(FontAwesome.Icon icon, int color, int dp, Context context) {
        return new IconicsDrawable(context)
                .icon(icon)
                .color(color)
                .sizeDp(dp);
    }
}