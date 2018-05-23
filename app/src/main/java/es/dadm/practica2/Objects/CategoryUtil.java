package es.dadm.practica2.Objects;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategoryUtil {
    private static CategoryUtil instance = null;
    private static final String CATEGORY_FILE = "categories.txt";

    private List<Category> mCategoryList;

    private CategoryUtil() {
        mCategoryList = new ArrayList<>();
    }

    public static void init(){
        if (instance == null){
            instance = new CategoryUtil();
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
        return mCategoryList.get(mCategoryList.size() - 1).getId() + 1;
    }

    public Category getCategory(int categoryPosition){
        return mCategoryList.get(categoryPosition);
    }

}