package ad.mobile.finalprojectkel1;

public class User {
    private String NIDN;
    private String gender;
    private String birthDate;
    private String phone;
    private String email;



    private String id;

    public User (String NIDN,
                 String gender,
                 String birthDate,
                 String phone,
                 String email,
                 String id){
        this.NIDN = NIDN;
        this.gender = gender;
        this.birthDate =  birthDate;
        this.phone = phone;
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getNIDN() {
        return NIDN;
    }
    public void setNIDN(String NIDN) {
        this.NIDN = NIDN;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }


}
