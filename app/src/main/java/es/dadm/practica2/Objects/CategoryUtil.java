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
import es.dadm.practica2.Util.Init;

public class CategoryUtil {
    private static CategoryUtil instance = null;
    private static final String CATEGORY_FILE = "categories.txt";

    private List<Category> mCategoryList;

    private CategoryUtil(Context context) {
        mCategoryList = new ArrayList<>();
        mCategoryList.add(getStockCategory(context));
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

    public int getCount(){
        return mCategoryList.size();
    }

    public int getNewID(){
        int newID;

        if (mCategoryList.size() == 0){
            newID = 0;
        } else {
            newID = mCategoryList.get(mCategoryList.size() - 1).getId() + 1;
        }

        return newID;
    }

    public Category getCategory(int categoryPosition){
        return mCategoryList.get(categoryPosition);
    }

    public Category getStockCategory(Context context){
        Category stockCategory = new Category();

        stockCategory.setId(0);
        stockCategory.setTitle(context.getString(R.string.DEFAULT_CATEGORY_TITLE));
        stockCategory.setDescription("Categoría por defecto, asignada a los tickets que no tienen categoría.");
        stockCategory.setImgFilename(Init.DEFAULT_IMG);

        return stockCategory;
    }

}