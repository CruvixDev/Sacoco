package com.example.sacoco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sacoco.fragments.ClothesListFragment;
import com.example.sacoco.fragments.EmailFragment;
import com.example.sacoco.fragments.HomeFragment;
import com.example.sacoco.fragments.MenuFragment;
import com.example.sacoco.viewmodels.BagViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BagViewModel bagViewModel;
    private BottomNavigationView bottomNavigationView;
    private TextView activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        activityTitle = findViewById(R.id.mainActivityBaseTitle);
        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);

        bagViewModel = new ViewModelProvider(this).get(BagViewModel.class);
    }

    private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        String activityBaseTitle = getString(R.string.main_activity_title_base);

        switch (item.getItemId()) {
            case R.id.home:
                loadFragment(HomeFragment.class);
                activityTitle.setText(activityBaseTitle + getString(R.string.main_activity_menu_home));
                break;
            case R.id.clothes_list:
                loadFragment(ClothesListFragment.class);
                activityTitle.setText(activityBaseTitle + getString(R.string.main_activity_menu_clothes_list));
                break;
            case R.id.settings:
                loadFragment(MenuFragment.class);
                activityTitle.setText(activityBaseTitle + getString(R.string.main_activity_menu_settings));
                break;
            case R.id.email:
                loadFragment(EmailFragment.class);
                activityTitle.setText(activityBaseTitle + getString(R.string.main_activity_menu_email));
                break;
        }

        return true;
    };

    private void loadFragment(Class fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.
                beginTransaction().
                setReorderingAllowed(true).
                replace(R.id.fragmentContainerView, fragmentClass, null).
                commit();
    }
}