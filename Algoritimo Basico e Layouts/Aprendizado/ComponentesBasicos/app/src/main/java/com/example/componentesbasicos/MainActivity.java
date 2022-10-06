package com.example.componentesbasicos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    private TextInputEditText campoNome;
    private TextInputEditText campoEmail;
    private TextView format, fortmatSwitchEToggle;
    private CheckBox checkAzul, checkVerde, checkVermelho;
    private RadioButton rbMasc, rbFem;
    private RadioGroup radioGroup;

    private Switch switchAlt;
    private ToggleButton toggleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Campos e Text
        campoNome = findViewById(R.id.txtNome);
        campoEmail = findViewById(R.id.txtEmail);
        format = findViewById(R.id.txtResultado);
        fortmatSwitchEToggle = findViewById(R.id.txtSwitchToggle);

        // CheckBox
        checkAzul = findViewById(R.id.cbAzul);
        checkVerde = findViewById(R.id.cbVerde);
        checkVermelho = findViewById(R.id.cbVermelho);

        // RadioButtons
        radioGroup = findViewById(R.id.radioGroup);
        rbMasc = findViewById(R.id.rbMasculino);
        rbFem = findViewById(R.id.rbFeminino);

        // Switch e ToggleButtons
        switchAlt = findViewById(R.id.switch1);
        toggleButton = findViewById(R.id.toggleButton3);


        enviarRadioButton();
        adicionarListener();

    }

    // Aula Componentes Básico
    public void enviarRadioButton() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {                             // if(rbFem.isChecked()){
                if (checkedId == R.id.rbFeminino) {                                                       //      format.setText("Feminino");
                    format.setText("Feminino");                                                         // } else {
                } else {                                                                                //      format.setText("Masculino");
                    format.setText("Masculino");                                                        // }
                }
            }

        });
    }

    public void enviarCheckBox() {
        String texto = "";

        if (checkAzul.isChecked()) {
            texto += "Azul Selecionado - ";
        }

        if (checkVerde.isChecked()) {
            texto += "Verde Selecionado - ";
        }

        if (checkVermelho.isChecked()) {
            texto += "Vermelho Selecionado - ";
        }

        format.setText(texto);
    }

    public void encaminarTexto(View view) {
        enviarCheckBox();
    }


    // Aula 87 - Switch e ToggleButtons

    public void adicionarListener() {

        // ToggleButton
        // CheckBox
        switchAlt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    fortmatSwitchEToggle.setText("Ligado");
                } else {
                    fortmatSwitchEToggle.setText("Desligado");
                }

            }

        });
    }

    // Aula 88 - Toast

    public void abrirToast(View view) {

        ImageView imagem = new ImageView((getApplicationContext()));
        imagem.setImageResource(android.R.drawable.star_big_off);

        TextView textView = new TextView(getApplicationContext());
//        textView.setBackground(R.color.purple_700);
        textView.setText("Olá Toast em TextView!");

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(imagem);
        //toast.setView(textView);
        toast.show();

         /*
        Toast.makeText(
                getApplicationContext(),
                "Olá Toast",
                Toast.LENGTH_SHORT
        ).show();
         */
    }

    // Aula 89 - AlertDialog

    public void abrirDialog(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        //Configurar titulo e mensagem
        dialog.setTitle("Título do Dialog");
        dialog.setMessage("Mensagem da Dialog");

        //Configurar cancelamento
        dialog.setCancelable(false);

        //Configurar icone
        dialog.setIcon( android.R.drawable.ic_btn_speak_now );

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Executar ação ao clicar no botão sim", Toast.LENGTH_LONG).show();
            }
        });

        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Executar ação ao clicar no botão não", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.create();
        dialog.show();
    }

}