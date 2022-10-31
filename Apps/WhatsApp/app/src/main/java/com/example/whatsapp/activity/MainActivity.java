package com.example.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.fragment.ContatosFragment;
import com.example.whatsapp.fragment.MensagensFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAuth();

    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Conversas", MensagensFragment.class)
                .add("Contatos", ContatosFragment.class)
                .create()
        );

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        smartTabLayout = findViewById(R.id.viewPagerTab);
        smartTabLayout.setViewPager(viewPager);

        //Configuração do SearchView:
        searchView = findViewById(R.id.searchPrincipal);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                MensagensFragment fragment = (MensagensFragment) adapter.getPage(0);

                //Se o parametro newText, que é a entrada na barra de pesquisa não estiver vazia:
                if (newText != null && !newText.isEmpty()) {

                    fragment.pesquisarConversas( newText );

                }

                return true;
            }


        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                MensagensFragment fragment = (MensagensFragment) adapter.getPage( 0 );

                fragment.recarregarConversas();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        //Configuração do botão de pesquisa
        MenuItem menuItem = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair :

                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();

                break;
            case R.id.menuConfig :
                startActivity(new Intent(this, ConfigActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}