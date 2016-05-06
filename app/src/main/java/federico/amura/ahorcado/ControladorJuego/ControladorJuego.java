package federico.amura.ahorcado.ControladorJuego;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

import federico.amura.ahorcado.ControladorJuego.modelo.Errores;
import federico.amura.ahorcado.ControladorJuego.modelo.Monedas;
import federico.amura.ahorcado.ControladorJuego.modelo.Palabra;
import federico.amura.ahorcado.ControladorJuego.modelo.Partida;
import federico.amura.ahorcado.Preferencias_PalabrasAgregadas;

/**
 * Creado por Federico Amura el 17/04/16.
 */
@SuppressWarnings("unused")
public class ControladorJuego {

    private static ControladorJuego instance;

    public static ControladorJuego getInstance() {
        if (instance == null) {
            instance = new ControladorJuego();
        }
        return instance;
    }

    private int erroresMaximos = 7;
    private int monedasMaximas = 3;

    private ArrayList<String> palabras;


    public ControladorJuego() {
        actualizarPalabras();
    }

    /* Private API */

    private void agregarPalabraSinOrdenar(@NonNull String palabra) {
        agregarPalabra(palabra, false);
    }

    private void agregarPalabra(@NonNull String palabra, boolean ordenar) {
        palabra = palabra.trim();
        palabra = palabra.toLowerCase();
        if (!palabras.contains(palabra)) {
            palabras.add(palabra);
        }
        if (ordenar) {
            Collections.sort(palabras);
        }
    }

    public Partida crearPartida(@NonNull CallbackPartida callbackPartida) {
        char[] alfabeto = "abcdefghijklmnñopqrstuvwxyz".toCharArray();
        Palabra palabra = generarNuevaPalabra();
        Errores errores = new Errores(erroresMaximos);
        Monedas monedas = new Monedas(monedasMaximas);

        return new Partida(alfabeto, palabra, errores, monedas, callbackPartida);
    }

    private Palabra generarNuevaPalabra() {
        String palabra = palabras.get(new Random().nextInt(palabras.size()));
        return new Palabra(palabra);
    }

    /* Public API */

    public void actualizarPalabras() {
        palabras = new ArrayList<>();

        agregarPalabraSinOrdenar("Ingenieria");
        agregarPalabraSinOrdenar("Sistema");
        agregarPalabraSinOrdenar("Informacion");
        agregarPalabraSinOrdenar("Software");
        agregarPalabraSinOrdenar("Hardware");
        agregarPalabraSinOrdenar("Android");
        agregarPalabraSinOrdenar("Java");
        agregarPalabraSinOrdenar("Programador");
        agregarPalabraSinOrdenar("Testing");
        agregarPalabraSinOrdenar("Windows");
        agregarPalabraSinOrdenar("Linux");
        agregarPalabraSinOrdenar("Arquitectura");
        agregarPalabraSinOrdenar("Investigacion");
        agregarPalabraSinOrdenar("Laboratorio");
        agregarPalabraSinOrdenar("Universidad");
        agregarPalabraSinOrdenar("Computadora");
        agregarPalabraSinOrdenar("Analisis");
        agregarPalabraSinOrdenar("Diseño");

        Set<String> palabrasAgregadas = Preferencias_PalabrasAgregadas.getPalabrasAgregadas();
        for (String palabra : palabrasAgregadas) {
            agregarPalabraSinOrdenar(palabra);
        }
    }

    public void agregarPalabra(@NonNull String palabra) {
        agregarPalabra(palabra, true);
    }

    public ArrayList<String> getPalabras() {
        return palabras;
    }

    public int getErroresMaximos() {
        return erroresMaximos;
    }

    public void setErroresMaximos(int erroresMaximos) {
        this.erroresMaximos = erroresMaximos;
    }

    /* State */

    public void saveState(Bundle state, Partida partida) {
        state.putParcelable("partida", partida);
    }

    public Partida loadState(Bundle state) {
        return state.getParcelable("partida");
    }


}
