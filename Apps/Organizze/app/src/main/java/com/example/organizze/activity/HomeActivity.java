package com.example.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.organizze.adapter.AdapterMovimentacao;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.organizze.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private MaterialCalendarView calendario_main;
    private TextView txtUsuario, txtSaldo;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioRef;
    private DatabaseReference movimentacaoRef;

    private Double despesaTotal;
    private Double receitaTotal;
    private Double resumoUsuario;

    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;

    private AdapterMovimentacao adapter;
    private List<Movimentacao> movsList = new ArrayList<>();
    private Movimentacao movimentacao;

    private String mesAnoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtUsuario = findViewById(R.id.lblUsuario);
        txtSaldo = findViewById(R.id.lblSaldo);
        calendario_main = findViewById(R.id.calendario_main);
        recyclerView = findViewById(R.id.recyclerMovimentos);

        configurarCalendarView();

        //Configurar adapter
        adapter = new AdapterMovimentacao(movsList, this);

        //Configurar RecyclerView

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapter );

        swipe();
    }

    public void atualizarSaldo(){

        String email = mAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(email);
        usuarioRef = mDataBase.child("usuarios").child(idUsuario);

        if(movimentacao.getTipo().equals("r")){

            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);

        }

        if(movimentacao.getTipo().equals("d")){

            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);

        }

    }

    public void recuperarResumo(){
        String email = mAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(email);
        usuarioRef = mDataBase.child("usuarios").child(idUsuario);

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultFormat = decimalFormat.format(resumoUsuario);

                txtUsuario.setText("Olá, " + usuario.getNome());
                txtSaldo.setText("R$ " + resultFormat);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void recuperarMovimentacoes(){

        String emailUsuario = mAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        movimentacaoRef = mDataBase.child("movimentacao")
                .child( idUsuario )
                .child( mesAnoSelecionado );

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movsList.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Movimentacao movimentacao = dados.getValue( Movimentacao.class );
                    movimentacao.setKey(dados.getKey());
                    movsList.add( movimentacao );

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void configurarCalendarView(){
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendario_main.setTitleMonths(meses);

        CalendarDay data = calendario_main.getCurrentDate();
        String mesFormat = String.format("%02d", (data.getMonth() + 1) );
        mesAnoSelecionado = mesFormat + "" + data.getYear();

        calendario_main.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                String mesFormat = String.format("%02d", (date.getMonth() + 1) );
                mesAnoSelecionado = mesFormat + "" + date.getYear();

                mDataBase.removeEventListener(valueEventListenerMovimentacoes);

                recuperarMovimentacoes();
            }
        });
    }

    /*
    *       OVERRIDES E BUTTONS
    */

    public void adicionarDespesa(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void adicionarReceita(View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair :
                mAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarResumo();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }

    /*
    *       SWIPE
    */

    public void swipe(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                excluirMovimentacao( viewHolder );

            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);

        alert.setTitle("Excluir Movimentação da Conta");
        alert.setMessage("Tem certeza que deserja excluir esta movimentação?");
        alert.setCancelable(false);

        alert.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int position = viewHolder.getAdapterPosition();
                movimentacao = movsList.get(position);

                String emailUsuario = mAuth.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);

                movimentacaoRef = mDataBase.child("movimentacao")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);

                movimentacaoRef.child( movimentacao.getKey() ).removeValue();
                adapter.notifyItemRemoved(position);
                atualizarSaldo();

            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.notifyDataSetChanged();
            }
        });

        alert.create();
        alert.show();

    }


}