package com.example.mynotes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private AnotacaoPrefs prefs;
    private EditText edtNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new AnotacaoPrefs(getApplicationContext());
        edtNote = findViewById(R.id.txtNote);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String note = edtNote.getText().toString();

                if (note.equals("")){
                    Snackbar snackbar = Snackbar.make(view, "Impossível salvar texto em branco.", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.rgb(67,48,42));
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    prefs.salvarAnotacao(note);
                    Toast.makeText(getApplicationContext(), "salvo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        String noted = prefs.recuperarAnotacao();
        if(!noted.equals("")){
            edtNote.setText(noted);
        }

    }
}