package federico.amura.ahorcado.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import federico.amura.ahorcado.R;

@SuppressWarnings("unused")
public class Activity_Principal extends AppCompatActivity {

    @Bind(R.id.drawerContainer)
    DrawerLayout mDrawerContainer;

    @Bind(R.id.drawer)
    NavigationView mDrawer;

    @Bind(R.id.fondo)
    View mFondo;

    @Bind(R.id.appBar)
    AppBarLayout mAppBar;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.btnIniciar)
    CardView mButton_Iniciar;

    int[] colores;

    @Override
    protected void onCreate(final Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_principal);

        ButterKnife.bind(this);

        initDrawer();
        initToolbar();

        colores = getResources().getIntArray(R.array.colores);

        int cDarker = darker(((ColorDrawable) mFondo.getBackground()).getColor(), 0.6f);
        mAppBar.setBackgroundColor(cDarker);
        mButton_Iniciar.setCardBackgroundColor(cDarker);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iniciarAnimBoton();
                iniciarAnimFondo();
            }
        }, 500);

    }

    private void initDrawer() {
        mDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_estadisticas: {
                        //Ciero el drawer
                        mDrawerContainer.closeDrawers();

                        //Inicio la activity
                        Intent intent = new Intent(Activity_Principal.this, Activity_Estadisticas.class);
                        startActivity(intent);
                    }
                    break;
                }
                return false;
            }
        });
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerContainer.openDrawer(GravityCompat.START);
            }
        });
    }

    private void iniciarAnimBoton() {
        ViewCompat.animate(mButton_Iniciar).scaleX(1.25f).scaleY(1.25f).setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                ViewCompat.animate(mButton_Iniciar).scaleY(1).scaleX(1).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        iniciarAnimBoton();
                    }
                });
            }
        });
    }

    private void iniciarAnimFondo() {
        int colorAnterior = ((ColorDrawable) mFondo.getBackground()).getColor();
        int colorNuevo = getColor();
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorAnterior, colorNuevo);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int c = (int) animation.getAnimatedValue();
                mFondo.setBackgroundColor(c);

                int cDarker = darker(c, 0.6f);

                mAppBar.setBackgroundColor(cDarker);
                mButton_Iniciar.setCardBackgroundColor(cDarker);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                iniciarAnimFondo();
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

    private int getColor() {
        int index = randInt(0, colores.length - 1);
        return colores[index];
    }

    public static int darker(int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }

    Random rand = new Random();

    public int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    @OnClick(R.id.btnIniciar)
    public void onClick_ButtonIniciar() {
        Intent intent = new Intent(this, Activity_Ahorcado.class);
        startActivity(intent);
    }

    /* Eventos */

    @Override
    public void onBackPressed() {
        if (mDrawerContainer.isDrawerOpen(GravityCompat.START)) {
            mDrawerContainer.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}

