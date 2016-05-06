package federico.amura.ahorcado.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import federico.amura.ahorcado.ControladorJuego.CallbackPartida;
import federico.amura.ahorcado.ControladorJuego.ControladorJuego;
import federico.amura.ahorcado.ControladorJuego.modelo.Palabra;
import federico.amura.ahorcado.ControladorJuego.modelo.Partida;
import federico.amura.ahorcado.Preferencias_Estadisticas;
import federico.amura.ahorcado.R;

@SuppressWarnings({"UnusedParameters", "FieldCanBeLocal"})
public class Activity_Ahorcado extends AppCompatActivity implements CallbackPartida {

    @Bind(R.id.imagenFondo)
    ImageView mImageView_Fondo;

    @Bind(R.id.imagenMunieco)
    ImageView mImageView_Muñeco;

    @Bind(R.id.contenedorLetras)
    ViewGroup mContenedor_Letras;

    @Bind(R.id.btnAdivinarLetra)
    Button mButton_AdivinarLetra;

    @Bind(R.id.contenedorTeclado)
    ViewGroup mContenedor_Teclado;

    private HashMap<Character, Button> teclado_letras;

    private float teclado_alpha_deshabilitado = 0.35f;
    private float teclado_alpha_habilitado = 1f;
    private int letra_w_default;
    private int subrayado_margen_default;
    private int subrayado_h_default;

    private Partida partida;

    @Override
    protected void onCreate(final Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_ahorcado);

        ButterKnife.bind(this);

        letra_w_default = (int) getResources().getDimension(R.dimen.letra_w);
        subrayado_margen_default = (int) getResources().getDimension(R.dimen.subrayado_margin);
        subrayado_h_default = (int) getResources().getDimension(R.dimen.subrayado_h);

        //Inicializo
        initImagenes();
        initFondo();

        if (state == null) {
            partida = ControladorJuego.getInstance().crearPartida(this);
            onPreAnimation();
        } else {
            partida = ControladorJuego.getInstance().loadState(state);
            partida.setCallbackPartida(this);
        }

        //PreDraw
        final View rootView = findViewById(android.R.id.content);
        if (rootView == null) return;
        rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                rootView.getViewTreeObserver().removeOnPreDrawListener(this);
                Activity_Ahorcado.this.onPreDraw(rootView, state);
                return false;
            }
        });
    }

    private void onPreDraw(@NonNull View view, @Nullable Bundle state) {
        //Inicio el juego o seteo el listener
        if (state == null) {
            partida.iniciar();
        } else {
            partida.resumir();
        }
    }

    private void initImagenes() {
//        Glide.with(this).load(R.drawable.pizarron).preload();
//        Glide.with(this).load(R.drawable.dibujo0).preload();
//        Glide.with(this).load(R.drawable.dibujo1).preload();
//        Glide.with(this).load(R.drawable.dibujo2).preload();
//        Glide.with(this).load(R.drawable.dibujo3).preload();
//        Glide.with(this).load(R.drawable.dibujo4).preload();
//        Glide.with(this).load(R.drawable.dibujo5).preload();
//        Glide.with(this).load(R.drawable.dibujo6).preload();
//        Glide.with(this).load(R.drawable.dibujo7).preload();
    }

    private void initFondo() {
        Glide.with(this).load(R.drawable.pizarron)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mImageView_Fondo);
    }

    /* Eventos */

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .title("¿Desea cancelar el juego actual?")
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //Destruyo el controlador, termino el juego
                        Activity_Ahorcado.super.onBackPressed();
                    }
                })
                .show();
    }

    public void onClickLetra(char letra) {
        partida.insertarLetra(letra);
    }

    @OnClick(R.id.btnAdivinarLetra)
    public void onBoton_AdivinarClick() {
        if (partida.isMonedasDisponibles()) {
            char letra = partida.adivinarLetra();
            onClickLetra(letra);
        }
    }

    /* State */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ControladorJuego.getInstance().saveState(outState, partida);
    }

    /* Interface Juego */

    @Override
    public void onJuegoIniciado() {
        inicializarPalabra(partida.getPalabra());
        inicializarTeclado(partida.getAlfabeto());

        actualizarErrores();
        actualizarMonedas();

        animarInicio();
    }

    @Override
    public void onJuegoResumido() {
        inicializarPalabra(partida.getPalabra());
        inicializarTeclado(partida.getAlfabeto());

        actualizarErrores();
        actualizarMonedas();
        actualizarPalabra();
        actualizarTeclado();

        switch (partida.getEstado()) {
            case esperando:
                break;
            case jugando:
                break;
            case perdido:
                onJuegoPerdido();
                break;
            case ganado:
                onJuegoGanado();
                break;
        }
    }

    @Override
    public void actualizarErrores() {
        int id = getResources().getIdentifier("dibujo" + partida.getErrores().getErroresCometidos(), "drawable", getPackageName());
        Glide.with(this)
                .load(id)
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mImageView_Muñeco);
    }

    @Override
    public void actualizarMonedas(){
        int monedasDisponibles = partida.getMonedasDisponibles();
        int monedasTotales = partida.getMonedasTotales();
        mButton_AdivinarLetra.setText("Adivinar letra (" + monedasDisponibles + "/" + monedasTotales + ")");
    }

    @Override
    public void actualizarPalabra() {
        for (int i = 0; i < partida.getCantidadLetras(); i++) {
            final ViewGroup view = (ViewGroup) mContenedor_Letras.getChildAt(i);
            ViewCompat.setAlpha(view, 1);

            final TextView mTextView_Letra = (TextView) view.findViewById(R.id.letra);

            final char letra = mTextView_Letra.getText().charAt(0);
            final boolean insertada = partida.isLetraInsertada(letra);
            ViewCompat.animate(mTextView_Letra).alpha(insertada ? 1 : 0);
        }
    }

    @Override
    public void actualizarTeclado() {
        ArrayList<Character> letrasNoIntentadas = partida.getLetrasNoIntentadas();
        for (char c : letrasNoIntentadas) {
            View view = teclado_letras.get(c);
            view.setEnabled(true);
            ViewCompat.animate(view).alpha(teclado_alpha_habilitado);
        }

        ArrayList<Character> letrasIntentadas = partida.getLetrasIntentadas();
        for (char c : letrasIntentadas) {
            View view = teclado_letras.get(c);
            view.setEnabled(false);
            ViewCompat.animate(view).alpha(teclado_alpha_deshabilitado);
        }
    }

    @Override
    public void onJuegoGanado() {
        animarFin();

        //Actualizo las estadisticas
        Preferencias_Estadisticas.aumentarCantidadPartidasGanadas();
        Preferencias_Estadisticas.setPalabraAdivinada(partida.getPalabra().getPalabra());

        //Pregunto si quiere volver a jugar
        new MaterialDialog.Builder(this)
                .title("Fin de la partida")
                .content("¡Ganaste!\nPalabra: " + partida.getPalabra())
                .cancelable(false)
                .positiveText("Volver a jugar")
                .negativeText("Salir")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        partida = ControladorJuego.getInstance().crearPartida(Activity_Ahorcado.this);
                        partida.iniciar();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Activity_Ahorcado.this.finish();
                    }
                })
                .show();
    }

    @Override
    public void onJuegoPerdido() {
        animarFin();

        //Actualizo las estadisticas
        Preferencias_Estadisticas.aumentarCantidadPartidasPerdidas();

        //Pregunto si quiere volver a jugar
        new MaterialDialog.Builder(this)
                .title("Fin de la partida")
                .content("¡Perdiste!")
                .cancelable(false)
                .positiveText("Reintentar")
                .negativeText("Salir")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        partida = ControladorJuego.getInstance().crearPartida(Activity_Ahorcado.this);
                        partida.iniciar();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Activity_Ahorcado.this.finish();
                    }
                })
                .show();
    }

    /* Soporte */

    private void inicializarPalabra(@NonNull Palabra palabra) {
        mContenedor_Letras.removeAllViews();

        //La fuente
        final Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/fuente.ttf");

        final int cantidadLetras = palabra.getPalabra().length();

        for (int i = 0; i < cantidadLetras; i++) {

            //Creo la View
            final View view = LayoutInflater.from(this).inflate(R.layout.letra, mContenedor_Letras, false);
            final TextView mTextView_Letra = (TextView) view.findViewById(R.id.letra);
            final View mView_Subrayado = view.findViewById(R.id.subrayado);

            //Oculto por defecto
            ViewCompat.setAlpha(view, 0);

            //Agrego la view al contenedor
            mContenedor_Letras.addView(view);

            //Calculo el tamaño de la letra
            int w_max = mContenedor_Letras.getWidth();
            int w_calculado = palabra.getPalabra().length() * letra_w_default;

            int letra_w = letra_w_default;
            float scale = 1;
            int subrayado_h = subrayado_h_default;
            int subrayado_margen = subrayado_margen_default;

            //Arreglo el tamaño por si la palabra es mas larga que el contenedor
            if (w_calculado > w_max) {
                letra_w = w_max / palabra.getPalabra().length();
                scale = ((float) letra_w) / letra_w_default;
                subrayado_h = (int) (subrayado_h * scale);
                subrayado_margen = (int) (subrayado_margen * scale);
            }

            //Tamaño del contenedor de la letra
            view.getLayoutParams().width = letra_w;
            view.getLayoutParams().height = letra_w;

            //Aplico scale a la letra
            ViewCompat.setScaleX(mTextView_Letra, scale);
            ViewCompat.setScaleY(mTextView_Letra, scale);

            //Subrayado h
            mView_Subrayado.getLayoutParams().height = subrayado_h;

            //Subrayado margen
            ((ViewGroup.MarginLayoutParams) mView_Subrayado.getLayoutParams()).leftMargin = subrayado_margen;
            ((ViewGroup.MarginLayoutParams) mView_Subrayado.getLayoutParams()).rightMargin = subrayado_margen;
            view.requestLayout();

            //Le pongo la letra, y en invisible
            final char letra = palabra.getPalabra().charAt(i);

            mTextView_Letra.setTypeface(fuente);
            ViewCompat.setAlpha(mTextView_Letra, 0);
            mTextView_Letra.setText(String.valueOf(letra));
        }
    }

    private void inicializarTeclado(@NonNull char[] alfabeto) {
        mContenedor_Teclado.removeAllViews();

        teclado_letras = new HashMap<>();

        final int cantidadPorFila = 10;
        final int lineas = (int) Math.ceil(((float) alfabeto.length / cantidadPorFila));

        //La fuente
        final Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/fuente.ttf");

        //Creo el teclado
        final LinearLayout teclado = new LinearLayout(this);
        teclado.setOrientation(LinearLayout.VERTICAL);
        teclado.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //Creo las filas
        for (int i = 0; i < lineas; i++) {
            final LinearLayout fila = new LinearLayout(this);
            fila.setOrientation(LinearLayout.HORIZONTAL);
            fila.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            teclado.addView(fila);

            //Creo las letras de la fila
            for (int j = i * cantidadPorFila; j < (i * cantidadPorFila) + cantidadPorFila; j++) {
                if (j < alfabeto.length) {
                    final Button button = (Button) LayoutInflater.from(this).inflate(R.layout.teclado_button_letra, fila, false);
                    final char letra = alfabeto[j];

                    teclado_letras.put(letra, button);

                    button.setText(String.valueOf(letra));
                    button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    fila.addView(button);

                    //Le seteo la fuente
                    button.setTypeface(fuente);

                    //On Click Letra
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickLetra(letra);
                        }
                    });
                }
            }
        }

        mContenedor_Teclado.addView(teclado);
    }

    // Anims
    private void onPreAnimation() {
        //Oculto la imagen
        ViewCompat.setAlpha(mImageView_Muñeco, 0);
        ViewCompat.setScaleX(mImageView_Muñeco, 0);
        ViewCompat.setScaleY(mImageView_Muñeco, 0);

        //Letras
        for (int i = 0; i < mContenedor_Letras.getChildCount(); i++) {
            View view = mContenedor_Letras.getChildAt(i);

            ViewCompat.setAlpha(view, 0);
            ViewCompat.setScaleX(view, 0);
            ViewCompat.setScaleY(view, 0);
        }

        //Boton
        ViewCompat.setAlpha(mButton_AdivinarLetra, 0);

        //Teclado
        ViewCompat.setAlpha(mContenedor_Teclado, 0);
    }

    private void animarInicio() {
        //Imagen
        int delayImagen = 300;
        ViewCompat.animate(mImageView_Muñeco).alpha(1).scaleX(1).scaleY(1).setStartDelay(delayImagen);

        //Letras
        int delayLetras = delayImagen + 300;
        for (int i = 0; i < mContenedor_Letras.getChildCount(); i++) {
            View view = mContenedor_Letras.getChildAt(i);

            ViewCompat.animate(view).alpha(1).scaleX(1).scaleY(1).setStartDelay(delayLetras + (i * 100));
        }

        //Boton Adivinar
        int delayBoton = delayLetras + ((mContenedor_Letras.getChildCount()) * 100) + 300;
        ViewCompat.animate(mButton_AdivinarLetra).alpha(1).setStartDelay(delayBoton);

        //Teclado
        int delayTeclado = delayBoton + 300;
        ViewCompat.animate(mContenedor_Teclado).alpha(1).setStartDelay(delayTeclado);
    }

    private void animarFin() {
        //Imagen
        ViewCompat.animate(mImageView_Muñeco).alpha(0).scaleX(0).scaleY(0).setStartDelay(0);

        //Letras
        for (int i = 0; i < mContenedor_Letras.getChildCount(); i++) {
            View view = mContenedor_Letras.getChildAt(i);
            ViewCompat.animate(view).alpha(0).scaleX(0).scaleY(0).setStartDelay(0);
        }

        //Boton
        ViewCompat.animate(mButton_AdivinarLetra).alpha(0).setStartDelay(0);

        //Teclado
        ViewCompat.animate(mContenedor_Teclado).alpha(0).setStartDelay(0);
    }
}

