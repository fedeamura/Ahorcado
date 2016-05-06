package federico.amura.ahorcado;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Creado por Federico Amura el 22/04/16.
 */
@SuppressWarnings("unused")
public class Preferencias_Estadisticas {

    private static final String PREF_NAME = "estadisticas";

    public static void borrarTodo() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    /* Cantidad Victorias */

    private static final String KEY_CANTIDAD_PARTIDAS_GANADAS = "cantidadPartidasGanadas";
    private static final String KEY_CANTIDAD_PARTIDAS_PERDIDAS = "cantidadPartidasPerdidas";

    public static void aumentarCantidadPartidasGanadas() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        long cantidad = getCantidadPartidasGanadas();
        cantidad++;
        preferences.edit().putLong(KEY_CANTIDAD_PARTIDAS_GANADAS, cantidad).commit();
    }

    public static long getCantidadPartidasGanadas() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(KEY_CANTIDAD_PARTIDAS_GANADAS, 0);
    }

    public static void borrarCantidadPartidasGanadas() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().remove(KEY_CANTIDAD_PARTIDAS_GANADAS).commit();
    }

    public static void aumentarCantidadPartidasPerdidas() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        long cantidad = getCantidadPartidasPerdidas();
        cantidad++;
        preferences.edit().putLong(KEY_CANTIDAD_PARTIDAS_PERDIDAS, cantidad).commit();
    }

    public static long getCantidadPartidasPerdidas() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(KEY_CANTIDAD_PARTIDAS_PERDIDAS, 0);
    }

    public static void borrarCantidadPartidasPerdidas() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().remove(KEY_CANTIDAD_PARTIDAS_PERDIDAS).commit();
    }

    /* Palabras Adivinadas */

    public static void setPalabraAdivinada(@NonNull String palabra) {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(palabra, true).apply();
    }

    public static boolean isPalabraAdivinada(@NonNull String palabra) {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(palabra, false);
    }

    public static void borrarPalabraAdivinada(@NonNull String palabra) {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().remove(palabra).apply();
    }
}
