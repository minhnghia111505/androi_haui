package com.example.qltgl;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.qltgl.databinding.ActivityMainBinding;
import com.example.qltgl.ui.activity.CreateOrderActivity;
import com.example.qltgl.ui.activity.CustomerManagementActivity;
import com.example.qltgl.ui.activity.LoginActivity;
import com.example.qltgl.ui.activity.ServiceManagementActivity;
import com.example.qltgl.ui.fragment.DashboardFragment;
import com.example.qltgl.ui.fragment.MoreFragment;
import com.example.qltgl.util.SessionManager;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        loadFragment(new DashboardFragment());

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    fragment = new DashboardFragment();
                } else if (id == R.id.nav_students) {
                    startActivity(new Intent(MainActivity.this, CustomerManagementActivity.class));
                    return false;
                } else if (id == R.id.nav_subjects) {
                    startActivity(new Intent(MainActivity.this, ServiceManagementActivity.class));
                    return false;
                } else if (id == R.id.nav_notifications) {
                    startActivity(new Intent(MainActivity.this, CreateOrderActivity.class));
                    return false;
                } else if (id == R.id.nav_more) {
                    fragment = new MoreFragment();
                }

                if (fragment != null) {
                    loadFragment(fragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
