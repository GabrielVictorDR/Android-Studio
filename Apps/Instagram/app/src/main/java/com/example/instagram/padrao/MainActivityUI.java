package com.example.instagram.padrao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivityUI extends AppCompatActivity {

    private Toolbar toolbar;
    //
    private FirebaseAuth firebaseAuth;

    private void loadControls(){
        toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Instagram");
        setSupportActionBar(toolbar);
        //
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        //
        configurarBottomNavigationView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        loadControls();
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_sair:
                deslogarUsuario();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivityUI.this, LoginActivityUI.class));
        finish();
    }

    private void configurarBottomNavigationView() {
        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bottomNavigation);
        habilitarNavegacao(bnve);
        //
        instanciarFragmentTransaction().replace(R.id.viewPager, new FeedFragmentUI()).commit();
    }

    private void habilitarNavegacao(BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.ic_home:
                        instanciarFragmentTransaction().replace(R.id.viewPager, new FeedFragmentUI()).commit();
                        return true;
                    case R.id.ic_pesquisa:
                        instanciarFragmentTransaction().replace(R.id.viewPager, new PesquisaFragmentUI()).commit();
                        return true;
                    case R.id.ic_postagem:
                        instanciarFragmentTransaction().replace(R.id.viewPager, new PostagemFragmentUI()).commit();
                        return true;
                    case R.id.ic_perfil:
                        instanciarFragmentTransaction().replace(R.id.viewPager, new PerfilFragmentUI()).commit();
                        return true;
                }

                return false;
            }
        });
    }

    private FragmentTransaction instanciarFragmentTransaction() {

        //Este método retorna um objeto do tipo FragmentTransaction
        //Para ser utilizado em dois outros métodos (habilitarNavegacao e configurarBottomNavigationView)
        //Sem ter que instanciar um transaction duas vezes.

        FragmentTransaction transaction;
        FragmentManager fragmentManager = getSupportFragmentManager();
        return transaction = fragmentManager.beginTransaction();
    }

}