package federico.amura.ahorcado.ControladorJuego.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creado por Federico Amura el 27/04/16.
 */
@SuppressWarnings("unused")
public class Palabra implements Parcelable {
    String palabra;
    char[] letras_completadas;

    public Palabra(String palabra) {
        this.palabra = palabra;
        this.letras_completadas = new char[palabra.length()];
    }

    public String getPalabra() {
        return palabra;
    }

    public char[] getLetras_completadas() {
        return letras_completadas;
    }

    public boolean insertarLetra(char c) {
        int index = palabra.indexOf(c);
        if (index == -1) {
            return false;
        }
        while (index != -1) {
            letras_completadas[index] = c;
            index = palabra.indexOf(c, index + 1);
        }

        return true;
    }

    public boolean isLetraInsertada(char c) {
        for (char letra : letras_completadas) {
            if (letra == c) {
                return true;
            }
        }
        return false;
    }

    public boolean isCompletada() {
        for (int i = 0; i < palabra.length(); i++) {
            char c1 = palabra.charAt(i);
            char c2 = letras_completadas[i];
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return palabra;
    }

    /* Parcel */

    protected Palabra(Parcel in) {
        palabra = in.readString();
        letras_completadas = new char[palabra.length()];
        in.readCharArray(letras_completadas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(palabra);
        dest.writeCharArray(letras_completadas);
    }

    @SuppressWarnings("unused")
    public final Parcelable.Creator<Palabra> CREATOR = new Parcelable.Creator<Palabra>() {
        @Override
        public Palabra createFromParcel(Parcel in) {
            return new Palabra(in);
        }

        @Override
        public Palabra[] newArray(int size) {
            return new Palabra[size];
        }
    };
}
