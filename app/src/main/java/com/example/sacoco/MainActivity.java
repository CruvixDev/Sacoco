package com.example.sacoco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.sacoco.fragments.ClothesListFragment;
import com.example.sacoco.fragments.EmailFragment;
import com.example.sacoco.fragments.HomeFragment;
import com.example.sacoco.fragments.SettingsFragment;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BagClothViewModel bagClothViewModel = new ViewModelProvider(this, ViewModelProvider.Factory.
                from(BagClothViewModel.bagViewModelViewModelInitializer)).get(BagClothViewModel.class);

        this.currentId = R.id.home;
        setContentView(R.layout.activity_main_layout);

        String activityBaseTitle = getString(R.string.main_activity_title_base);
        this.setTitle(String.format(activityBaseTitle, getString(R.string.main_activity_menu_home)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);

        PackageInfo packageInfo;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        String versionName = packageInfo.versionName;
        bagClothViewModel.setAppVersion(versionName);
    }

    private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        int itemId = item.getItemId();

        if (itemId == R.id.home && this.currentId != R.id.home) {
            loadFragment(HomeFragment.class, getString(R.string.main_activity_menu_home));
        }
        else if (itemId == R.id.clothes_list && this.currentId != R.id.clothes_list) {
            loadFragment(ClothesListFragment.class, getString(R.string.main_activity_menu_clothes_list));
        }
        else if (itemId == R.id.settings && this.currentId != R.id.settings) {
            loadFragment(SettingsFragment.class, getString(R.string.main_activity_menu_settings));
        }
        else if (itemId == R.id.email && this.currentId != R.id.email) {
            loadFragment(EmailFragment.class, getString(R.string.main_activity_menu_email));
        }

        this.currentId = itemId;
        return true;
    };

    public void loadFragment(Class<? extends Fragment> fragmentClass, String fragmentTitle) {
        String activityBaseTitle = getString(R.string.main_activity_title_base);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.
                beginTransaction().
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                setReorderingAllowed(true).
                replace(R.id.fragmentContainerView, fragmentClass, null).
                addToBackStack(null).
                commit();

        this.setTitle(String.format(activityBaseTitle, fragmentTitle));
    }

    public void loadFragment(Class<? extends Fragment> fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.
                beginTransaction().
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                setReorderingAllowed(true).
                replace(R.id.fragmentContainerView, fragmentClass, null).
                addToBackStack(null).
                commit();
    }
}
