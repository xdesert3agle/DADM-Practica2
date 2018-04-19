package es.dadm.practica2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dadm.practica2.util.CardAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rvCards) RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Factura factura = new Factura();
        factura.setId(1);
        factura.setCategory("Videojuegos");
        factura.setAmount(299);
        factura.setShortDesc("PS4 Slim (1 TB)");
        factura.setLongDesc("Prueba descripción larga de PS4");

        Factura factura2 = new Factura();
        factura2.setId(2);
        factura2.setCategory("Videojuegos");
        factura2.setShortDesc("God Of War");
        factura2.setLongDesc("Prueba descripción larga de God of War");
        factura2.setAmount(60);

        List<Factura> facturas = new ArrayList<>();
        facturas.add(factura);
        facturas.add(factura2);

        mRecycler.setAdapter(new CardAdapter(facturas));
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        //factura.printInfo();
    }
}
