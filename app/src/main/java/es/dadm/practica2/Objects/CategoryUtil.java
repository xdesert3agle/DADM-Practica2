package es.dadm.practica2.Objects;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dadm.practica2.R;
import es.dadm.practica2.Util.ImgUtil;

public class CategoryUtil {
    private static CategoryUtil instance = null;
    private static final String CATEGORY_FILE = "categories.txt";

    private List<Category> mCategoryList;

    private CategoryUtil(Context context) {
        mCategoryList = new ArrayList<>();
        mCategoryList.add(getDefaultCategory(context));
    }

    public static void init(Context context){
        if (instance == null){
            instance = new CategoryUtil(context);
        }
    }

    public static CategoryUtil getInstance(){
        return instance;
    }

    // Transforma el array de categorías en un fichero .json
    public void persistCategories(Context context) {
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(mCategoryList);

        try {
            File f = new File(context.getFilesDir(), CATEGORY_FILE);
            FileOutputStream fout = new FileOutputStream(f);

            fout.write(jsonString.getBytes());
            fout.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Returnea el array de categorías del fichero .json
    public List<Category> getCategories(Context context) {
        String jsonString = "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(CATEGORY_FILE)));
            jsonString = br.readLine(); // El archivo JSON siempre tiene una sola línea

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!jsonString.isEmpty()) {
            Gson gson = new GsonBuilder().create();
            Type listType = new TypeToken<List<Category>>(){}.getType();

            mCategoryList = gson.fromJson(jsonString, listType);
        }

        return mCategoryList;
    }

    // Añade una nueva categoría al array y persiste la lista
    public void addCategory(Category category, Context context) {
        mCategoryList.add(category);
        persistCategories(context);
    }

    // Actualiza la información de una categoría y persiste la lista
    public void updateCategory(int position, Category updatedCategory, Context context) {
        mCategoryList.set(position, updatedCategory);
        persistCategories(context);
    }

    // Borra una categoría y persiste la lista
    public void deleteCategory(int position, Context context) {
        mCategoryList.remove(position);
        persistCategories(context);
    }

    // Genera una ID para una categoría nueva
    public int getNewID(){
        int newID = mCategoryList.size() > 0 ? mCategoryList.get(mCategoryList.size() - 1).getId() + 1 : 0;

        return newID;
    }

    // Devuelve la categoría que se encuentra en una determinada posición del array de categorías
    public Category getCategory(int categoryPosition){
        return mCategoryList.get(categoryPosition);
    }

    // Crea y devuelve una categoría para ser usada como categoría por defecto
    public Category getDefaultCategory(Context context){
        Category stockCategory = new Category();

        stockCategory.setId(0);
        stockCategory.setTitle(context.getString(R.string.DEFAULT_CATEGORY_TITLE));
        stockCategory.setDescription(context.getString(R.string.DEFAULT_CATEGORY_DESCRIPTION));
        stockCategory.setDetails(context.getString(R.string.DEFAULT_CATEGORY_DETAILS));
        stockCategory.setImgFilename(ImgUtil.DEFAULT_IMG_FILENAME);

        return stockCategory;
    }
}