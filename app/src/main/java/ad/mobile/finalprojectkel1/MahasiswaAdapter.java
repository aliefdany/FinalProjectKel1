package ad.mobile.finalprojectkel1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ItemVH> implements Filterable {
    private final Context context;
    private List<Mahasiswa> data;

    private List<Mahasiswa> dataOriginal;

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence search) {

                Log.d("Filter", search.toString());

                String searchLower = search.toString().toLowerCase();

                FilterResults results = new FilterResults();
                ArrayList<Mahasiswa> filteredMahasiswa = new ArrayList<>();

                for(Mahasiswa c: dataOriginal) {
                    if (c.getName().toLowerCase().contains(searchLower) || c.getNIM().toLowerCase().contains(searchLower) || c.getProdi().matches(search.toString())) {
                        filteredMahasiswa.add(c);
                    }
                }

                results.values = filteredMahasiswa;
                results.count = filteredMahasiswa.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data = (ArrayList<Mahasiswa>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

    @NonNull
    @Override
    public ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(context).inflate(R.layout.item_mahasiswa, parent, false);

        ItemVH viewHolder = new ItemVH(rowView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemVH holder, int position) {

        Mahasiswa k = this.data.get(position);

        holder.dbRef = k.dbRef;

        holder.tvNamaMahasiswa.setText(k.getName());
        holder.tvNIM.setText(k.getNIM());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public MahasiswaAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void setData(List<Mahasiswa> data) {
        this.data = data;
        this.dataOriginal = data;
    }

    class ItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvNamaMahasiswa;
        private TextView tvNIM;

        private ImageButton btDeleteMahasiswa;

        public DatabaseReference dbRef;


        public ItemVH(@NonNull View itemView) {
            super(itemView);

            this.tvNamaMahasiswa = (TextView) itemView.findViewById(R.id.tvNamaMahasiswa);
            this.tvNIM = (TextView) itemView.findViewById(R.id.tvNIM);
            this.btDeleteMahasiswa = (ImageButton) itemView.findViewById(R.id.btDeleteMahasiswa);


            itemView.setOnClickListener(this);

            btDeleteMahasiswa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteDialog();
                }
            });
        }

        void showDeleteDialog() {
            final Dialog dialog = new Dialog(context);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.delete_mahasiswa_dialog);

//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            Button btConfirmDeleteTweet = (Button) dialog.findViewById(R.id.btConfirmDeleteTweet);

            btConfirmDeleteTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbRef.removeValue();

                    dialog.dismiss();

                }
            });

            dialog.show();
        }

        public void removeAt(int position) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, data.size());
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, EditStudent.class);
            i.putExtra("id", this.tvNIM.getText());
            context.startActivity(i);
        }
    }
}
