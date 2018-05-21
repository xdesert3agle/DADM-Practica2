package es.dadm.practica2;

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
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategoryPersist {
    /*private List<Category> mCategoryList;

    public CategoryPersist(){
        mCategoryList = new ArrayList<Category>();
    }

    public void addCategory(Category category)
    {
        mCategoryList.add(category);
    }

    public void persistToJson(String path, Category category) {
        //Serializo el objeto a cadena

        Gson gson = new GsonBuilder().create();

        mCategoryList.add(category);

        String jsonString = gson.toJson(mCategoryList);

        try{
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            osw.write(jsonString); //Escribimos la información

            osw.close(); //Y cerramos el fichero de texto
        }
        catch (IOException e){ //Si hay un error, escribimos el mensaje de error en el Log
            e.printStackTrace();
        }

    }

    public List<Category> loadFromJson(String rutaFichero) { //Obtener animal del fichero de texto
        String jsonString = "";

        try { //Componemos una cadena de texto a partir de varias lineas de archivo, donde estan guardados los datos de los usuarios
            File file = new File(rutaFichero);
            FileInputStream fis = new FileInputStream(file);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line); //Vamos leyendo linea a linea del texto y los vamos concatenando en un StringBuilder
            }

            isr.close();
            jsonString = sb.toString(); //El StringBuilder lo transformamos a string

        } catch (FileNotFoundException e) { //Si hay algún error, lo mostramos en el Log
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!jsonString.isEmpty()) {
            Gson gson = new GsonBuilder().create();
            Type listType = new TypeToken<List<Category>>(){}.getType();

            mCategoryList = gson.fromJson(jsonString.split("\\n")[0], listType);
        }

        return mCategoryList;
    }

    private String serialize(){
        return new Gson().toJson(this); // Serializamos la asignatura
    }*/

}