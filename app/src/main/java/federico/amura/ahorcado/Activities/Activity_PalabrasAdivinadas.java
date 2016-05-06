package federico.amura.ahorcado.Activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import federico.amura.ahorcado.Adaptadores.Adaptador_PalabrasAdivinadas;
import federico.amura.ahorcado.R;

@SuppressWarnings("unused")
public class Activity_PalabrasAdivinadas extends AppCompatActivity {

    @Bind(R.id.coordinateLayout)
    CoordinatorLayout view;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    Adaptador_PalabrasAdivinadas adaptador_palabras;

    @Override
    protected void onCreate(final Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_palabras_adivinadas);

        ButterKnife.bind(this);


        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.md_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        adaptador_palabras = new Adaptador_PalabrasAdivinadas();
        mRecyclerView.setAdapter(adaptador_palabras);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}

