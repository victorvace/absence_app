package com.absences.victor.absences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.absences.victor.absences.adapters.CicloAdapter;
import com.absences.victor.absences.asyncTask.CiclosATask;
import com.absences.victor.absences.domains.Modulo;
import com.absences.victor.absences.interfaces.OnCicloItemClickListener;
import com.absences.victor.absences.interfaces.OnCiclosReturn;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnCiclosReturn, OnCicloItemClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String nameUser, emailUser, capitalLetter, tittle;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Modulo> listaCiclos;

    private CicloAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
        nameUser = prefs.getString("username", "username");
        emailUser = prefs.getString("email", "email@email.com");

        ActionBar actionBar;

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(tittle);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        listaCiclos = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        adapter = new CicloAdapter(this, listaCiclos);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        CiclosATask ciclosATask = new CiclosATask(this, this);
        ciclosATask.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        tittle = getString(R.string.ciclos);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_cycle:
                                menuItem.setChecked(true);
                                tittle = getString(R.string.title_cycle);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_settings:
                                menuItem.setChecked(true);
                                tittle = getString(R.string.title_settings);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                return true;
                            case R.id.item_navigation_drawer_logout:
                                menuItem.setChecked(true);

                                SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
                                prefs.edit().clear().commit();

                                startActivity(new Intent(MainActivity.this, LoginActivity.class));

                                Toast.makeText(getApplicationContext(), menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });

        getSupportActionBar().setTitle(tittle);

    }

    @Override
    public void onCiclosConsultados(ArrayList<Modulo> listaCiclos) {
        this.listaCiclos.clear();
        this.listaCiclos.addAll(listaCiclos);
        this.adapter.notifyDataSetChanged(); //Notifica que se ha cambiado
    }

    @Override
    public void onCiclosError() {
        Toast.makeText(MainActivity.this, "Error con el Listado de asignaturas", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);

        SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt("ID_ASIGNATURA", listaCiclos.get(position).getIdModulo());
        prefsEditor.commit();

        startActivity(new Intent(this, RecyclerAlumnos.class));
    }
}
