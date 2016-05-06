package federico.amura.ahorcado.ControladorJuego;

/**
 * Creado por Federico Amura el 18/04/16.
 */
public interface CallbackPartida {

    //Inicio
    void onJuegoIniciado();

    void onJuegoResumido();

    //Eventos durante el juego
    void actualizarErrores();

    void actualizarMonedas();

    void actualizarPalabra();

    void actualizarTeclado();

    //Fin
    void onJuegoGanado();

    void onJuegoPerdido();

}
