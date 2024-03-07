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
import com.example.sacoco.viewmodels.PreferencesViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create the first instance of BagClothViewModel
        new ViewModelProvider(this, ViewModelProvider.Factory.from(
                BagClothViewModel.bagViewModelViewModelInitializer)).get(BagClothViewModel.class);

        PreferencesViewModel preferencesViewModel =
                new ViewModelProvider(this, ViewModelProvider.Factory.from(
                        PreferencesViewModel.preferencesViewModelInitializer)).get(PreferencesViewModel.class);

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
        preferencesViewModel.setAppVersion(versionName);
    }

    private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            loadFragment(HomeFragment.class);
        }
        else if (itemId == R.id.clothes_list) {
            loadFragment(ClothesListFragment.class);
        }
        else if (itemId == R.id.settings) {
            loadFragment(SettingsFragment.class);
        }
        else if (itemId == R.id.email) {
            loadFragment(EmailFragment.class);
        }

        return true;
    };

    /**
     * Replace a fragment in the fragment container view
     * @param fragmentClass the class of the fragment to replace
     */
    public void loadFragment(Class<? extends Fragment> fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.
                beginTransaction().
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                setReorderingAllowed(true).
                replace(R.id.fragmentContainerView, fragmentClass, null).
                commit();
    }

    /**
     * Modify the activity title
     * @param activityTitle the activity string title
     */
    public void setActivityTitle(String activityTitle) {
        String activityBaseTitle = getString(R.string.main_activity_title_base);
        this.setTitle(String.format(activityBaseTitle, activityTitle));
    }
}
