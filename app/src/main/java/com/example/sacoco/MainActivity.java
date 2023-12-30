package com.example.sacoco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sacoco.fragments.ClothesListFragment;
import com.example.sacoco.fragments.EmailFragment;
import com.example.sacoco.fragments.HomeFragment;
import com.example.sacoco.fragments.MenuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private TextView activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        String activityBaseTitle = getString(R.string.main_activity_title_base);
        activityTitle = findViewById(R.id.mainActivityBaseTitle);
        activityTitle.setText(String.format(activityBaseTitle, getString(R.string.main_activity_menu_home)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);
    }

    private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        String activityBaseTitle = getString(R.string.main_activity_title_base);
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            loadFragment(HomeFragment.class);
            activityTitle.setText(String.format(activityBaseTitle, getString(R.string.main_activity_menu_home)));
        }
        else if (itemId == R.id.clothes_list) {
            loadFragment(ClothesListFragment.class);
            activityTitle.setText(String.format(activityBaseTitle, getString(R.string.main_activity_menu_clothes_list)));
        }
        else if (itemId == R.id.settings) {
            loadFragment(MenuFragment.class);
            activityTitle.setText(String.format(activityBaseTitle, getString(R.string.main_activity_menu_settings)));
        }
        else if (itemId == R.id.email) {
            loadFragment(EmailFragment.class);
            activityTitle.setText(String.format(activityBaseTitle, getString(R.string.main_activity_menu_email)));
        }

        return true;
    };

    public void loadFragment(Class<? extends Fragment> fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.
                beginTransaction().
                setReorderingAllowed(true).
                replace(R.id.fragmentContainerView, fragmentClass, null).
                commit();
    }
}