package federico.amura.ahorcado.Adaptadores;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import federico.amura.ahorcado.ControladorJuego.ControladorJuego;
import federico.amura.ahorcado.Preferencias_Estadisticas;
import federico.amura.ahorcado.R;

/**
 * Creado por Federico Amura el 22/04/16.
 */
public class Adaptador_PalabrasAdivinadas extends RecyclerView.Adapter<Adaptador_PalabrasAdivinadas.ItemVH> {

    private SortedList<String> palabras;

    public Adaptador_PalabrasAdivinadas() {
        palabras = new SortedList<>(String.class, new SortedListAdapterCallback<String>(this) {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }

            @Override
            public boolean areContentsTheSame(String oldItem, String newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(String item1, String item2) {
                return item1.equals(item2);
            }
        });

        setHasStableIds(true);
        actualizarPalabras();
    }

    @Override
    public long getItemId(int position) {
        return palabras.get(position).hashCode();
    }

    public void actualizarPalabras() {
        palabras.beginBatchedUpdates();

        //Quito todo
        while (palabras.size() != 0) {
            palabras.removeItemAt(0);
        }

        //Agrego solo las adivinadas
        ArrayList<String> listaPalabras = ControladorJuego.getInstance().getPalabras();
        for (String p : listaPalabras) {
            if (Preferencias_Estadisticas.isPalabraAdivinada(p)) {
                palabras.add(p);
            }
        }

        palabras.endBatchedUpdates();
    }

    @Override
    public ItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_palabra, parent, false);
        return new ItemVH(view);
    }

    @Override
    public void onBindViewHolder(ItemVH holder, int position) {
        holder.mTextView.setText(palabras.get(position));
    }

    @Override
    public int getItemCount() {
        return palabras.size();
    }

    public class ItemVH extends RecyclerView.ViewHolder {

        @Bind(R.id.texto)
        TextView mTextView;

        public ItemVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
