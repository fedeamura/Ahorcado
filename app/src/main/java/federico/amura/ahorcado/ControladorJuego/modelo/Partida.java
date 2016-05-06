package federico.amura.ahorcado.ControladorJuego.modelo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import federico.amura.ahorcado.ControladorJuego.CallbackPartida;

/**
 * Creado por Federico Amura el 27/04/16.
 */
@SuppressWarnings({"unused", "NullableProblems"})
public class Partida implements Parcelable {
    @NonNull
    char[] alfabeto;

    @NonNull
    Palabra palabra;

    @NonNull
    Errores errores;

    @NonNull
    Monedas monedas;

    @NonNull
    ArrayList<Character> letras_intentadas;

    @NonNull
    CallbackPartida callbackPartida;

    public enum Estado {
        esperando, jugando, perdido, ganado
    }

    @NonNull
    Estado estado;

    public Partida(@NonNull char[] alfabeto, @NonNull Palabra palabra, @NonNull Errores errores, @NonNull Monedas monedas, @NonNull CallbackPartida callbackPartida) {
        this.alfabeto = alfabeto;
        this.palabra = palabra;
        this.errores = errores;
        this.monedas = monedas;
        this.letras_intentadas = new ArrayList<>();
        this.estado = Estado.esperando;
        this.callbackPartida = callbackPartida;
    }

    public void setCallbackPartida(@NonNull CallbackPartida callbackPartida) {
        this.callbackPartida = callbackPartida;
    }

    public void iniciar() {
        if (this.estado != Estado.esperando) {
            throw new RuntimeException("Intentando iniciar una partida en estado " + this.estado);
        }

        this.estado = Estado.jugando;
        this.callbackPartida.onJuegoIniciado();
    }

    public void resumir() {
        if (this.estado == Estado.esperando) {
            throw new RuntimeException("Intentando resumir una partida no estado " + this.estado);
        }

        this.callbackPartida.onJuegoResumido();
    }

    public char adivinarLetra() {
        if (this.estado != Estado.jugando) {
            throw new RuntimeException("Intentando adivinar una eltra con una partida en estado " + this.estado.name());
        }

        if (!isMonedasDisponibles()) {
            throw new RuntimeException("Intentando adivinar una letra sin monedas");
        }

        monedas.usarMoneda();
        callbackPartida.actualizarMonedas();
        return palabra.getLetraSinAdivinar();
    }

    public boolean isMonedasDisponibles() {
        return monedas.isMonedasDisponibles();
    }

    public int getMonedasDisponibles() {
        return monedas.getMonedasMaximas() - monedas.getMonedasUsadas();
    }

    public int getMonedasTotales() {
        return monedas.getMonedasMaximas();
    }

    public void insertarLetra(char c) {
        if (this.estado != Estado.jugando) {
            throw new RuntimeException("Intentando insertar una letra con una partida en estado " + this.estado);
        }
        if (letras_intentadas.contains(c)) {
            throw new RuntimeException("Intentando insertar una letra que ya fue insertada");
        }

        //Agrego la letra a las letras intentadas
        letras_intentadas.add(c);

        //Inserto la letra en la palabra
        boolean exitoInsertando = palabra.insertarLetra(c);

        //Si dio error la insercion aumento el error
        if (!exitoInsertando) {
            errores.aumentar();
        }

        switch (getEstado()) {
            case esperando:
                throw new RuntimeException("¿El estado es esperando?");
            case jugando:
                callbackPartida.actualizarErrores();
                callbackPartida.actualizarPalabra();
                callbackPartida.actualizarTeclado();
                break;
            case perdido:
                callbackPartida.onJuegoPerdido();
                break;
            case ganado:
                callbackPartida.onJuegoGanado();
                break;
        }
    }

    @NonNull
    public char[] getAlfabeto() {
        return alfabeto;
    }

    @NonNull
    public Palabra getPalabra() {
        return palabra;
    }

    public int getCantidadLetras() {
        return palabra.getPalabra().length();
    }

    public boolean isLetraInsertada(char letra) {
        return palabra.isLetraInsertada(letra);
    }

    public ArrayList<Character> getLetrasIntentadas() {
        return letras_intentadas;
    }

    public ArrayList<Character> getLetrasNoIntentadas() {
        ArrayList<Character> letrasNoIntentadas = new ArrayList<>();
        ArrayList<Character> letrasIntentadas = getLetrasIntentadas();
        for (char c : alfabeto) {
            if (!letrasIntentadas.contains(c)) {
                letrasNoIntentadas.add(c);
            }
        }

        return letrasNoIntentadas;
    }

    @NonNull
    public Errores getErrores() {
        return errores;
    }

    @NonNull
    public Estado getEstado() {
        actualizarEstado();
        return estado;
    }

    private void actualizarEstado() {
        if (errores.isPerdio()) {
            this.estado = Estado.perdido;
            return;
        }

        if (palabra.isCompletada()) {
            this.estado = Estado.ganado;
        }
    }

        /* Parcel */

    protected Partida(Parcel in) {
        int cantidadAlfabeto = in.readInt();
        alfabeto = new char[cantidadAlfabeto];
        in.readCharArray(alfabeto);
        palabra = (Palabra) in.readValue(Palabra.class.getClassLoader());
        errores = (Errores) in.readValue(Errores.class.getClassLoader());
        letras_intentadas = new ArrayList<>();
        in.readList(letras_intentadas, Character.class.getClassLoader());
        estado = (Estado) in.readValue(Estado.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(alfabeto.length);
        dest.writeCharArray(alfabeto);
        dest.writeValue(palabra);
        dest.writeValue(errores);
        dest.writeByte((byte) (0x01));
        dest.writeList(letras_intentadas);
        dest.writeValue(estado);
    }

    @SuppressWarnings("unused")
    public final Parcelable.Creator<Partida> CREATOR = new Parcelable.Creator<Partida>() {
        @Override
        public Partida createFromParcel(Parcel in) {
            return new Partida(in);
        }

        @Override
        public Partida[] newArray(int size) {
            return new Partida[size];
        }
    };
}