package ad.mobile.finalprojectkel1;

import com.google.firebase.database.DatabaseReference;

public class Mahasiswa {
    private String name;
    private String NIM;
    private String prodi;
    private String fakultas;
    private String email;
    private String alamat;
    private String phoneNumber;

    public DatabaseReference dbRef;

    public Mahasiswa(String name,
                     String NIM,
                     String prodi,
                     String fakultas,
                     String alamat,
                     String email,
                     String phoneNumber,
                     DatabaseReference dbRef) {
        this.name = name;
        this.NIM = NIM;
        this.prodi = prodi;
        this.fakultas = fakultas;
        this.alamat = alamat;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dbRef = dbRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNIM() {
        return NIM;
    }

    public void setNIM(String NIM) {
        this.NIM = NIM;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
