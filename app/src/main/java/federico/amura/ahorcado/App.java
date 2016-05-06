package federico.amura.ahorcado;

import android.app.Application;
import android.content.Context;

/**
 * Created by federico on 18/04/16.
 */
public class App extends Application {

    public static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }
}
