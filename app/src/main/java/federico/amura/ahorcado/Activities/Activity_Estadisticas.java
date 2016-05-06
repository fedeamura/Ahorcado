package federico.amura.ahorcado.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import federico.amura.ahorcado.ControladorJuego.ControladorJuego;
import federico.amura.ahorcado.Preferencias_Estadisticas;
import federico.amura.ahorcado.R;

@SuppressWarnings("unused")
public class Activity_Estadisticas extends AppCompatActivity {

    @Bind(R.id.coordinateLayout)
    CoordinatorLayout view;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    //Victorias
    @Bind(R.id.textoPorcentajePartidasGanadas)
    TextView mTextView_PorcentajePartidasGanadas;

    @Bind(R.id.textoCantidadPartidasGanadas)
    TextView mTextView_CantidadPartidasGanadas;

    @Bind(R.id.textoCantidadPartidasPerdidas)
    TextView mTextView_CantidadPartidasPerdidas;

    //Palabras adivinadas

    @Bind(R.id.textoPorcentajePalabrasAdivinadas)
    TextView mTextView_PorcentajePalabrasAdivinadas;

    @Bind(R.id.textoCantidadPalabrasAdivinadas)
    TextView mTextView_CantidadPalabrasAdivinadas;

    @Bind(R.id.textoCantidadPalabrasNoAdivinadas)
    TextView mTextView_CantidadPalabrasNoAdivinadas;


    @Override
    protected void onCreate(final Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_estadisticas);

        ButterKnife.bind(this);


        initToolbar();
        actualizarEstadisticas();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.md_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.inflateMenu(R.menu.estadisticas_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_reiniciarEstadisticas: {
                        Preferencias_Estadisticas.borrarTodo();
                        actualizarEstadisticas();
                    }
                    break;
                }
                return false;
            }
        });

    }

    @OnClick(R.id.cardView_victorias)
    public void onClickVictorias() {

    }

    @OnClick(R.id.cardView_palabrasAdivinadas)
    public void onClickPalabrasAdivinadas() {
        Intent intent = new Intent(this, Activity_PalabrasAdivinadas.class);
        startActivity(intent);
    }


    private void actualizarEstadisticas() {
        /* Victorias */

        //Ganadas
        long cantidadPartidasGanadas = Preferencias_Estadisticas.getCantidadPartidasGanadas();
        mTextView_CantidadPartidasGanadas.setText(String.valueOf(cantidadPartidasGanadas));

        //Perdidas
        long cantidadPartidasPerdidas = Preferencias_Estadisticas.getCantidadPartidasPerdidas();
        mTextView_CantidadPartidasPerdidas.setText(String.valueOf(cantidadPartidasPerdidas));

        //Porcentaje
        long total = cantidadPartidasGanadas + cantidadPartidasPerdidas;
        if (total != 0) {
            float porcentaje = ((float) cantidadPartidasGanadas) / total;
            porcentaje = porcentaje * 100;
            String texto = porcentaje + "%";
            mTextView_PorcentajePartidasGanadas.setText(texto);
        } else {
            mTextView_PorcentajePartidasGanadas.setText("0%");
        }

        /* Palabras adivinadas */

        ArrayList<String> palabras = ControladorJuego.getInstance().getPalabras();
        int cantidadPalabras = palabras.size();
        int cantidadAdivinadas = 0;
        for (String palabra : palabras) {
            if (Preferencias_Estadisticas.isPalabraAdivinada(palabra)) {
                cantidadAdivinadas++;
            }
        }

        //Palabras Adivinadas
        mTextView_CantidadPalabrasAdivinadas.setText(String.valueOf(cantidadAdivinadas));

        //Palabras No adivinadas
        mTextView_CantidadPalabrasNoAdivinadas.setText(String.valueOf(cantidadPalabras - cantidadAdivinadas));

        //Porcentaje adivinadas
        float porcentajeAdivinadas = ((float) cantidadAdivinadas) / cantidadPalabras;
        porcentajeAdivinadas = porcentajeAdivinadas * 100;
        String textoPorcentajeAdivinadas = porcentajeAdivinadas + "%";
        mTextView_PorcentajePalabrasAdivinadas.setText(textoPorcentajeAdivinadas);
    }

}

