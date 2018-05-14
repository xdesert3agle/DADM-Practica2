package es.dadm.practica2;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class ImgProvider {
    Context context;

    public ImgProvider(Context context){
        this.context = context;
    }

    public void saveImage(Bitmap bitMap, String filename){

        File file = new File(getImgFile(Environment.DIRECTORY_PICTURES), filename);

        if (file.exists()){
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getImgFile(String filename){
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
    }
}
