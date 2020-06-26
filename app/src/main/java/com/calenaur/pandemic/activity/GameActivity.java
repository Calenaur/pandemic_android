package com.calenaur.pandemic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.register.Registrar;
import com.calenaur.pandemic.app.PandemicApplication;
import com.calenaur.pandemic.navigation.NavigationUtils;
import com.google.android.material.navigation.NavigationView;



public class GameActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavController navController;
    private LocalUser localUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (getIntent().hasExtra("local_user")) {
            localUser = (LocalUser) getIntent().getSerializableExtra("local_user");
            API api = ((PandemicApplication)getApplication()).getAPI();
            Registrar registrar = new Registrar();
            registrar.updateAll(api, localUser);

            SharedGameDataViewModel viewModel = ViewModelProviders.of(this).get(SharedGameDataViewModel.class);
            viewModel.setLocalUser(localUser);
            viewModel.setApi(api);
            viewModel.setRegistrar(registrar);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        drawerLayout.closeDrawer(GravityCompat.START);
        Bundle bundle = new Bundle();
        bundle.putSerializable("local_user", localUser);
        return NavigationUtils.onNavDestinationSelectedWithArgs(menuItem, navController, bundle) || super.onOptionsItemSelected(menuItem);
    }


}