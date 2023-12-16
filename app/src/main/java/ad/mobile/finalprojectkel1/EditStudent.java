package ad.mobile.finalprojectkel1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class EditStudent extends AppCompatActivity implements ValueEventListener, View.OnClickListener {

    private EditText etNamaMahasiswa;
    private EditText etNIM;
    private EditText etProdi;
    private EditText etFakultas;
    private EditText etEmail;
    private EditText etAlamat;
    private EditText etPhone;

    private String url = "https://final-project-papb-de61c-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference db;
    private DatabaseReference appDb;

    private String idMahasiswa;

    private Mahasiswa mahasiswa;
    private View btEditMhs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        this.idMahasiswa = getIntent().getExtras().getString("id");

        this.etNamaMahasiswa = findViewById(R.id.etNamaMahasiwa);
        this.etNIM = findViewById(R.id.etNIM);
        this.etProdi = findViewById(R.id.etProdi);
        this.etFakultas = findViewById(R.id.etFakultas);
        this.etEmail = findViewById(R.id.etEmailMahasiswa);
        this.etAlamat = findViewById(R.id.etAlamatMahasiswa);
        this.etPhone = findViewById(R.id.etNoTelpMahasiswa);

        this.btEditMhs = findViewById(R.id.btEditMhs);

        this.btEditMhs.setOnClickListener(this);

        this.db = FirebaseDatabase.getInstance(url).getReference();
        this.appDb = this.db.child("mahasiswa");

        this.appDb.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        Map<Mahasiswa, Object> map = (Map<Mahasiswa, Object>) snapshot.child(this.idMahasiswa).getValue();

        this.mahasiswa = new Mahasiswa(
                map.get("name").toString(),
                map.get("nim").toString(),
                map.get("prodi").toString(),
                map.get("fakultas").toString(),
                map.get("email").toString(),
                map.get("alamat").toString(),
                map.get("phoneNumber").toString(), null);

        this.etNamaMahasiswa.setText(this.mahasiswa.getName());
        this.etNIM.setText(this.mahasiswa.getNIM());
        this.etProdi.setText(this.mahasiswa.getProdi());
        this.etFakultas.setText(this.mahasiswa.getFakultas());
        this.etEmail.setText(this.mahasiswa.getEmail());
        this.etAlamat.setText(this.mahasiswa.getAlamat());
        this.etPhone.setText(this.mahasiswa.getPhoneNumber());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btEditMhs){

            Handler h = new Handler(Looper.getMainLooper());
            Thread editMhsThread = new Thread(() -> {
                try {
                    Mahasiswa mahasiswa = new Mahasiswa(
                            this.etNamaMahasiswa.getText().toString(),
                            this.etNIM.getText().toString(),
                            this.etProdi.getText().toString(),
                            this.etFakultas.getText().toString(),
                            this.etEmail.getText().toString(),
                            this.etAlamat.getText().toString(),
                            this.etPhone.getText().toString(),
                            null
                    );

                    this.appDb.child(this.etNIM.getText().toString()).setValue(mahasiswa);

                    h.post(() -> {
                        finish();
                    });
                } catch(Exception e) {

                }
            });

            editMhsThread.start();
        }
    }
}