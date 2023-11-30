package ad.mobile.finalprojectkel1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements INavbar {

    private FragmentManager fragmentManager;
    private NavbarFragment navbarFragment;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.navbarFragment = new NavbarFragment(this);
        this.homeFragment = new  HomeFragment();
        this.fragmentManager = this.getSupportFragmentManager();

        this.fragmentManager.beginTransaction()
                .add(R.id.navbar, this.navbarFragment, "Navigation Bar")
                .add(R.id.page, this.homeFragment, "Home Page")
                .commit();
    }

    @Override
    public void navigate(int navItem) {

    }
}