package com.calenaur.pandemic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.register.Registrar;
import com.calenaur.pandemic.api.store.PromiseHandler;
import com.calenaur.pandemic.app.PandemicApplication;
import com.calenaur.pandemic.fragment.BackActionListener;
import com.calenaur.pandemic.fragment.ProductionFragment;
import com.calenaur.pandemic.fragment.ResearchChoiceFragment;
import com.calenaur.pandemic.navigation.NavigationUtils;
import com.google.android.material.navigation.NavigationView;
import java.lang.ref.WeakReference;

public class GameActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ConstraintLayout loader;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private LocalUser localUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        drawerLayout = findViewById(R.id.drawer_layout);
        if (getIntent().hasExtra("local_user")) {
            localUser = (LocalUser) getIntent().getSerializableExtra("local_user");
            loader = findViewById(R.id.loader);
            loader.bringToFront();
            API api = ((PandemicApplication)getApplication()).getAPI();
            Registrar registrar = new Registrar();
            LoadTask loadTask = new LoadTask(api, localUser, registrar, this);
            loadTask.execute(drawerLayout, loader);
            SharedGameDataViewModel sharedGameDataViewModel = ViewModelProviders.of(this).get(SharedGameDataViewModel.class);
            sharedGameDataViewModel.setLocalUser(localUser);
            sharedGameDataViewModel.setApi(api);
            sharedGameDataViewModel.setRegistrar(registrar);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setNavigationItemSelectedListener(this);

    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getFragments().size() < 1)
            return null;

        Fragment navHostFragment = fragmentManager.getFragments().get(0);
        if (!(navHostFragment instanceof NavHostFragment))
            return null;

        FragmentManager childFragmentManager = navHostFragment.getChildFragmentManager();
        if (childFragmentManager.getFragments().size() < 1)
            return null;

        return childFragmentManager.getFragments().get(0);
    }

    /**
     * @return whether or not to consume the event
     */
    private boolean raiseOnBackAction(Fragment fragment) {
        if (fragment instanceof BackActionListener)
            return ((BackActionListener) fragment).onBackAction();

        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (raiseOnBackAction(getCurrentFragment()))
            return false;

        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        Fragment fragment = getCurrentFragment();
        if (raiseOnBackAction(fragment))
            return;

        if (fragment instanceof ProductionFragment)
            return;

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        drawerLayout.closeDrawer(GravityCompat.START);
        Bundle bundle = new Bundle();
        bundle.putSerializable("local_user", localUser);
        return NavigationUtils.onNavDestinationSelectedWithArgs(menuItem, navController, bundle) || super.onOptionsItemSelected(menuItem);
    }

    private static class LoadTask extends AsyncTask<View, Void, Void> {

        private WeakReference<API> api;
        private WeakReference<LocalUser> localUser;
        private WeakReference<Registrar> registrar;
        private WeakReference<Activity> activityRef;

        LoadTask(API api, LocalUser localUser, Registrar registrar, Activity activity) {
            this.api = new WeakReference<>(api);
            this.localUser = new WeakReference<>(localUser);
            this.registrar = new WeakReference<>(registrar);
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(View... views) {
            Activity activity = activityRef.get();
            if (activity == null) {
                return null;
            }

            if (views.length < 1) {
                return null;
            }

            DrawerLayout drawerLayout = (DrawerLayout) views[0];
            View loader = views[1];
            activity.runOnUiThread(() -> {
                AlphaAnimation inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                loader.setAnimation(inAnimation);
                loader.setVisibility(View.VISIBLE);
            });

            try {
                registrar.get().updateAll(api.get(), localUser.get());
                while (!registrar.get().isUpdated()) {
                    Thread.sleep(1000);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            activity.runOnUiThread(() -> {
                AlphaAnimation outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                loader.setAnimation(outAnimation);
                loader.setVisibility(View.GONE);
                drawerLayout.removeView(loader);
            });
            return null;
        }
    }
}