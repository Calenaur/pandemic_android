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
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.Tier;
import com.calenaur.pandemic.api.model.event.Event;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserDisease;
import com.calenaur.pandemic.api.model.user.UserEvent;
import com.calenaur.pandemic.api.net.response.ErrorCode;

import com.calenaur.pandemic.api.register.Registrar;
import com.calenaur.pandemic.api.store.PromiseHandler;
import com.calenaur.pandemic.app.PandemicApplication;
import com.calenaur.pandemic.fragment.BackActionListener;
import com.calenaur.pandemic.fragment.ProductionFragment;
import com.calenaur.pandemic.navigation.NavigationUtils;
import com.google.android.material.navigation.NavigationView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int EVENT_DELAY_MILLIS = 30000;
    private static final int SAVE_DELAY_MILLIS = 60000;

    private ConstraintLayout loader;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private LocalUser localUser;
    private Handler eventHandler;
    private Handler saveHandler;
    private SharedGameDataViewModel data;
    private long lastBalance;
    private boolean isPaused;

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
            data = ViewModelProviders.of(this).get(SharedGameDataViewModel.class);
            data.setLocalUser(localUser);
            data.setApi(api);
            data.setRegistrar(registrar);
            lastBalance = data.getBalance();
            startSaveHandler();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setNavigationItemSelectedListener(this);
        isPaused = false;
    }

    public void startEventHandler() {
        eventHandler = new Handler();
        eventHandler.postDelayed(this::handleEvent, EVENT_DELAY_MILLIS);
    }

    public void startSaveHandler() {
        saveHandler = new Handler();
        saveHandler.postDelayed(this::handleSave, SAVE_DELAY_MILLIS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    private void handleEvent() {
        if (isPaused) {
            eventHandler.postDelayed(this::handleEvent, EVENT_DELAY_MILLIS);
            return;
        }

        Random random = new Random();
        if (random.nextFloat() < 0.1)
            if (data.getRegistrar().isUpdated() && !isFinishing()) {
                Event[] events = data.getRegistrar().getEventRegistry().toArray(new Event[]{});
                ArrayList<Integer> idList = new ArrayList<>();
                for (Event event : events)
                    if (event != null)
                        if (!data.getRegistrar().getUserEventRegistry().containsKey(event.id))
                            if (event.getTier().getID() <= localUser.getTier().getID())
                                for (int i=0; i<event.rarity; i++)
                                    idList.add(event.id);

                if (idList.size() > 0) {
                    int id = idList.get(random.nextInt(idList.size()));
                    Event event = data.getRegistrar().getEventRegistry().get(id);
                    if (event != null) {
                        Activity gameActivity = this;
                        UserEvent userEvent = new UserEvent(localUser.getUid(), event.id);
                        data.getApi().getUserStore().putUserEvent(localUser, userEvent, new PromiseHandler<Void>() {
                            @Override
                            public void onDone(Void object) {
                                data.getRegistrar().getUserEventRegistry().register(userEvent.id, userEvent);
                                onEvent(localUser, event);
                                AlertDialog dialog = new AlertDialog.Builder(gameActivity)
                                        .setTitle(event.name)
                                        .setCancelable(false)
                                        .setMessage(event.description)
                                        .setPositiveButton(R.string.ok, null)
                                        .create();
                                dialog.show();
                            }

                            @Override
                            public void onError(ErrorCode errorCode) {

                            }
                        });
                    }
                }
            }

        eventHandler.postDelayed(this::handleEvent, EVENT_DELAY_MILLIS);
    }

    private void onEvent(LocalUser localUser, Event event) {
        UserDisease userDisease = null;
        Tier tier = null;
        switch (event.id) {
            case 1:
                tier = Tier.Uncommon;
                break;
            case 2:
                tier = Tier.Rare;
                break;
            case 3:
                tier = Tier.Epic;
                break;
            case 4:
                tier = Tier.Legendary;
                break;
            case 5:
            case 6:
                break;
            case 7:
                data.pay(-5000);
                break;
            case 8:
                data.pay(5000);
                break;
            case 9:
                userDisease = new UserDisease(localUser.getUid(), 1);
                break;
            case 10:
                userDisease = new UserDisease(localUser.getUid(), 2);
                break;
        }

        if (userDisease != null) {
            final UserDisease finalUserDisease = userDisease;
            data.getApi().getUserStore().putUserDisease(localUser, userDisease, new PromiseHandler<Void>() {
                @Override
                public void onDone(Void object) {
                    data.getRegistrar().getUserDiseaseRegistry().register(finalUserDisease.id, finalUserDisease);
                    data.calcClickValue();
                }

                @Override
                public void onError(ErrorCode errorCode) {

                }
            });
        } else {
            data.calcClickValue();
        }

        if (tier != null) {
            localUser.setTier(tier);
            data.getApi().getUserStore().putTier(localUser, tier, PromiseHandler.EMPTY_HANDLER);
        }
    }

    private void handleSave() {
        if (isPaused) {
            saveHandler.postDelayed(this::handleSave, SAVE_DELAY_MILLIS);
            return;
        }

        long balance = data.getBalance();
        if (lastBalance != balance) {
            data.getApi().getUserStore().putBalance(localUser, data.getBalance(), PromiseHandler.EMPTY_HANDLER);
            lastBalance = balance;
        }
        saveHandler.postDelayed(this::handleSave, SAVE_DELAY_MILLIS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isPaused = false;
        handleSave();
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
        private WeakReference<GameActivity> activityRef;

        LoadTask(API api, LocalUser localUser, Registrar registrar, GameActivity activity) {
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
                activityRef.get().startEventHandler();
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