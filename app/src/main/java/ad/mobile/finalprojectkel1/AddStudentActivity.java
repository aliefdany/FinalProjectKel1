package ad.mobile.finalprojectkel1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddStudentActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etNamaMahasiswa;
    private EditText etNIM;
    private EditText etProdi;
    private EditText etFakultas;
    private EditText etEmail;
    private EditText etAlamat;
    private EditText etPhone;

    private Button btAdd;

    private String url = "https://final-project-papb-de61c-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference db;
    private DatabaseReference appDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        this.db = FirebaseDatabase.getInstance(url).getReference();
        this.appDb = this.db.child("mahasiswa");

        this.etNamaMahasiswa = findViewById(R.id.etNamaMahasiwa);
        this.etNIM = findViewById(R.id.etNIM);
        this.etProdi = findViewById(R.id.etProdi);
        this.etFakultas = findViewById(R.id.etFakultas);
        this.etEmail = findViewById(R.id.etEmailMahasiswa);
        this.etAlamat = findViewById(R.id.etAlamatMahasiswa);
        this.etPhone = findViewById(R.id.etNoTelpMahasiswa);

        this.btAdd = findViewById(R.id.btAddMhs);

        this.btAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btAddMhs){
            String uuid = UUID.randomUUID().toString();

            Mahasiswa mahasiswa = new Mahasiswa(
                    uuid,
                    this.etNamaMahasiswa.getText().toString(),
                    this.etNIM.getText().toString(),
                    this.etProdi.getText().toString(),
                    this.etFakultas.getText().toString(),
                    this.etEmail.getText().toString(),
                    this.etAlamat.getText().toString(),
                    this.etPhone.getText().toString()
            );

            this.appDb.child(uuid).setValue(mahasiswa);
        }
    }
}