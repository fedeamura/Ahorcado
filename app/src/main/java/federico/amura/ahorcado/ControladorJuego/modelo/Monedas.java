package federico.amura.ahorcado.ControladorJuego.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creado por Federico Amura el 27/04/16.
 */
@SuppressWarnings("unused")
public class Monedas implements Parcelable {
    int monedasUsadas;
    int monedasMaximas;

    public Monedas(int monedasMaximas) {
        this.monedasUsadas = 0;
        this.monedasMaximas = monedasMaximas;
    }

    public int getMonedasUsadas() {
        return monedasUsadas;
    }

    public int getMonedasMaximas() {
        return monedasMaximas;
    }

    public boolean isMonedasDisponibles() {
        return monedasUsadas != monedasMaximas;
    }

    public void usarMoneda() {
        if (!isMonedasDisponibles()) {
            throw new RuntimeException("Intentando usar una moneda cuando ya no quedan mas");
        }
        monedasUsadas++;
    }

    /* Parcel */

    protected Monedas(Parcel in) {
        monedasUsadas = in.readInt();
        monedasMaximas= in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(monedasUsadas);
        dest.writeInt(monedasMaximas);
    }

    @SuppressWarnings("unused")
    public final Creator<Monedas> CREATOR = new Creator<Monedas>() {
        @Override
        public Monedas createFromParcel(Parcel in) {
            return new Monedas(in);
        }

        @Override
        public Monedas[] newArray(int size) {
            return new Monedas[size];
        }
    };
}