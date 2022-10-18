package com.example.tarefasapp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String nome_db = "DB_TAREFAS";
    public static String TABELA_TAREFAS = "tarefas";



    public DbHelper(@Nullable Context context) {
        super(context, nome_db, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFAS + "(" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "nome TEXT NOT NULL" +
                     "); ";

        try{
            db.execSQL(sql);
            Log.i("INFO DB", "Sucesso ao criar a tabela " );
        }catch (Exception e) {
            Log.i("INFO DB", "Erro ao criar a tabela " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABELA_TAREFAS + " ;";

        try{
            db.execSQL(sql);
            onCreate(db);
            Log.i("INFO DB", "Sucesso ao atualizar app " );
        }catch (Exception e) {
            Log.i("INFO DB", "Erro ao atualizar app " + e.getMessage());
        }
    }

}
