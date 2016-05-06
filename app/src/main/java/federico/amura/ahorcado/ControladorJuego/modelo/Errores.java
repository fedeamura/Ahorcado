package federico.amura.ahorcado.ControladorJuego.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creado por Federico Amura el 27/04/16.
 */
@SuppressWarnings("unused")
public class Errores implements Parcelable {
    int erroresCometidos;
    int erroresMaximos;

    public Errores(int erroresMaximos) {
        this.erroresCometidos = 0;
        this.erroresMaximos = erroresMaximos;
    }

    public int getErroresCometidos() {
        return erroresCometidos;
    }

    public int getErroresMaximos() {
        return erroresMaximos;
    }

    public boolean isPerdio() {
        return erroresCometidos == erroresMaximos;
    }

    public void aumentar() {
        if (isPerdio()) {
            throw new RuntimeException("Intentando aumentar un error con errores maximos alcanzados");
        }
        erroresCometidos++;
    }

    /* Parcel */

    protected Errores(Parcel in) {
        erroresCometidos = in.readInt();
        erroresMaximos = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(erroresCometidos);
        dest.writeInt(erroresMaximos);
    }

    @SuppressWarnings("unused")
    public final Parcelable.Creator<Errores> CREATOR = new Parcelable.Creator<Errores>() {
        @Override
        public Errores createFromParcel(Parcel in) {
            return new Errores(in);
        }

        @Override
        public Errores[] newArray(int size) {
            return new Errores[size];
        }
    };
}