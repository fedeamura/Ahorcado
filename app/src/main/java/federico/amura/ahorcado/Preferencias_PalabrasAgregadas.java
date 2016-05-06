package federico.amura.ahorcado;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import federico.amura.ahorcado.ControladorJuego.ControladorJuego;

/**
 * Creado por Federico Amura el 22/04/16.
 */
@SuppressWarnings("unused")
public class Preferencias_PalabrasAgregadas {

    private static final String pref_name = "palabrasagregadas";

    private static final String key_palabras = "palabrasagregadas";

    public static void agregarPalabra(@NonNull String palabra) {
        if (ControladorJuego.getInstance().getPalabras().contains(palabra)) {
            return;
        }

        SharedPreferences preferences = App.getContext().getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        Set<String> palabras = getPalabrasAgregadas();
        if (!palabras.contains(palabra)) {
            palabras.add(palabra);
        }
        preferences.edit().putStringSet(key_palabras, palabras).commit();
    }

    public static Set<String> getPalabrasAgregadas() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        //noinspection unchecked
        return preferences.getStringSet(key_palabras, new HashSet<String>());
    }

    public static void borrarPalabras() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }
}
