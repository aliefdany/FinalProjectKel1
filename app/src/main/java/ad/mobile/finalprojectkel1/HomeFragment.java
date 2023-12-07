package ad.mobile.finalprojectkel1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements ValueEventListener {
    private INavbar activity;

    private View layout;


    private RecyclerView rvMahasiswa;
    private List<Mahasiswa> listMahasiswa;
    private MahasiswaAdapter adapterMahasiswa;
    private EditText etCari;

    private String url = "https://final-project-papb-de61c-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference db;
    private DatabaseReference appDb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.db = FirebaseDatabase.getInstance(url).getReference();
        this.appDb = this.db.child("mahasiswa");

        // Inflate the layout for this fragment
        this.layout = inflater.inflate(R.layout.home_fragment, container, false);

        return this.layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        listMahasiswa = new ArrayList<>();

        this.appDb.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        this.listMahasiswa = new ArrayList<>();
        // Iterate through the children and add them to the ArrayList
        for (DataSnapshot snap : snapshot.getChildren()) {
            Map<Mahasiswa, Object> map = (Map<Mahasiswa, Object>) snap.getValue();

//            Log.d("TAG", "Value is: " + map);


            listMahasiswa.add(new Mahasiswa(map.get("id").toString(),
                    map.get("name").toString(),
                    map.get("nim").toString(),
                    map.get("prodi").toString(),
                    map.get("fakultas").toString(),
                    map.get("email").toString(),
                    map.get("alamat").toString(),
                    map.get("phoneNumber").toString()));
        }

        this.rvMahasiswa = this.layout.findViewById(R.id.rvMahasiswa);

        this.adapterMahasiswa = new MahasiswaAdapter(this.layout.getContext());
        this.adapterMahasiswa.setData(this.listMahasiswa);

        this.rvMahasiswa.setLayoutManager(new LinearLayoutManager(this.layout.getContext()));

        this.rvMahasiswa.setAdapter(this.adapterMahasiswa);

        this.adapterMahasiswa.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
