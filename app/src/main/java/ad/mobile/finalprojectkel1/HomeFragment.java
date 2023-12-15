package ad.mobile.finalprojectkel1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements ValueEventListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private INavbar activity;

    private View layout;


    private RecyclerView rvMahasiswa;
    private List<Mahasiswa> listMahasiswa;
    private MahasiswaAdapter adapterMahasiswa;
    private EditText tvSearch;

    private Button btOpenFilter;

    private View filterOverlay;

    private ProgressBar dbLoading;

    private Chip chipSI;
    private Chip chipTI;
    private Chip chipTIF;
    private Chip chipTekkom;

    private Chip chipPTI;
    private Chip chipIlkom;

    private Button btFilter;

    private TextView tvUserGreeting;

    private String url = "https://final-project-papb-de61c-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference db;
    private DatabaseReference appDb;

    private BottomSheetDialog bottomSheetDialog;

    private ArrayList<String> filters;

    public HomeFragment(View filterOverlay) {
        // doesn't do anything special
        this.filterOverlay = filterOverlay;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.db = FirebaseDatabase.getInstance(url).getReference();
        this.appDb = this.db.child("mahasiswa");

        // Inflate the layout for this fragment
        this.layout = inflater.inflate(R.layout.home_fragment, container, false);


        this.bottomSheetDialog = new BottomSheetDialog(
                this.layout.getContext());
        View bottomSheetView = LayoutInflater.from(this.layout.getContext())
                .inflate(R.layout.bottom_sheet,
                        (LinearLayout) this.layout.findViewById(R.id.bottomSheetContainer));

        this.chipSI = bottomSheetView.findViewById(R.id.chipSI);
        this.chipTI = bottomSheetView.findViewById(R.id.chipTI);
        this.chipTIF = bottomSheetView.findViewById(R.id.chipTIF);
        this.chipTekkom = bottomSheetView.findViewById(R.id.chipTekkom);
        this.chipPTI = bottomSheetView.findViewById(R.id.chipPTI);
        this.chipIlkom = bottomSheetView.findViewById(R.id.chipIlkom);

        this.chipSI.setOnCheckedChangeListener(this);
        this.chipTI.setOnCheckedChangeListener(this);
        this.chipTIF.setOnCheckedChangeListener(this);
        this.chipTekkom.setOnCheckedChangeListener(this);
        this.chipPTI.setOnCheckedChangeListener(this);
        this.chipIlkom.setOnCheckedChangeListener(this);

        this.btFilter = bottomSheetView.findViewById(R.id.btFilter);

        this.btFilter.setOnClickListener(this);

        this.filters = new ArrayList<>();

        this.bottomSheetDialog.setContentView(bottomSheetView);
        this.bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                filterOverlay.setVisibility(View.GONE);
            }
        });


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


            listMahasiswa.add(new Mahasiswa(
                    map.get("name").toString(),
                    map.get("nim").toString(),
                    map.get("prodi").toString(),
                    map.get("fakultas").toString(),
                    map.get("email").toString(),
                    map.get("alamat").toString(),
                    map.get("phoneNumber").toString(),
                    snap.getRef()));
        }

        this.layout.findViewById(R.id.dbLoading).setVisibility(ProgressBar.GONE);
        this.btOpenFilter = this.layout.findViewById(R.id.btOpenFilter);
        this.btOpenFilter.setOnClickListener(this);

        this.tvSearch = this.layout.findViewById(R.id.etSearch);
        this.tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                handleSearch(editable.toString());
            }
        });

        this.rvMahasiswa = this.layout.findViewById(R.id.rvMahasiswa);

        this.adapterMahasiswa = new MahasiswaAdapter(this.layout.getContext());
        this.adapterMahasiswa.setData(this.listMahasiswa);

        this.rvMahasiswa.setLayoutManager(new LinearLayoutManager(this.layout.getContext()));

        this.rvMahasiswa.setAdapter(this.adapterMahasiswa);

        this.adapterMahasiswa.notifyDataSetChanged();
    }

    public void handleSearch(String searchParams) {
        this.adapterMahasiswa.getFilter().filter(searchParams);
    }

    public void handleFilter(ArrayList<String> arrayList) {
        this.adapterMahasiswa.getFilter().filter(concatenateToRegex(arrayList));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onClick(View v) {
        //  opens filter dialog

        if(v.getId() == R.id.btFilter) {
            this.bottomSheetDialog.dismiss();
            this.filterOverlay.setVisibility(View.GONE);

            handleFilter(this.filters);
        } else {
            this.bottomSheetDialog.show();

            this.filterOverlay.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(isChecked){
                this.filters.add(buttonView.getText().toString());
            } else {
                removeStringItem(this.filters, buttonView.getText().toString());
            }


            Log.d("filters", printAllStrings(this.filters));

    }

    public void removeStringItem(ArrayList<String> arrayList, String valueToRemove) {
        // Using iterator to safely remove elements during iteration
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            String currentElement = iterator.next();
            if (currentElement.equals(valueToRemove)) {
                iterator.remove();
            }
        }
    }

    public String printAllStrings(ArrayList<String> arrayList) {
        String s = "";
        for (String str : arrayList) {
            s += str + ",";
        }

        return s;
    }

    public String concatenateToRegex(ArrayList<String> arrayList) {
        StringBuilder regexBuilder = new StringBuilder();

        for (String str : arrayList) {
            regexBuilder.append(str).append("|");
        }

        // Remove the last "|" if the list is not empty
        if (!arrayList.isEmpty()) {
            regexBuilder.deleteCharAt(regexBuilder.length() - 1);
        }

        return regexBuilder.toString();
    }
}
