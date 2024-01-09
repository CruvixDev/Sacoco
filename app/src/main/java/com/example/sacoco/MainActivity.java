package com.example.sacoco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.sacoco.fragments.ClothesListFragment;
import com.example.sacoco.fragments.EmailFragment;
import com.example.sacoco.fragments.HomeFragment;
import com.example.sacoco.fragments.MenuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        String activityBaseTitle = getString(R.string.main_activity_title_base);
        this.setTitle(String.format(activityBaseTitle, getString(R.string.main_activity_menu_home)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);
    }

    private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        String activityBaseTitle = getString(R.string.main_activity_title_base);
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            loadFragment(HomeFragment.class);
            this.setTitle(String.format(activityBaseTitle, getString(R.string.main_activity_menu_home)));
        }
        else if (itemId == R.id.clothes_list) {
            loadFragment(ClothesListFragment.class);
            this.setTitle(String.format(activityBaseTitle, getString(R.string.main_activity_menu_clothes_list)));
        }
        else if (itemId == R.id.settings) {
            loadFragment(MenuFragment.class);
            this.setTitle(String.format(activityBaseTitle, getString(R.string.main_activity_menu_settings)));
        }
        else if (itemId == R.id.email) {
            loadFragment(EmailFragment.class);
            this.setTitle(String.format(activityBaseTitle, getString(R.string.main_activity_menu_email)));
        }

        return true;
    };

    public void loadFragment(Class<? extends Fragment> fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.
                beginTransaction().
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                setReorderingAllowed(true).
                replace(R.id.fragmentContainerView, fragmentClass, null).
                commit();
    }
}
