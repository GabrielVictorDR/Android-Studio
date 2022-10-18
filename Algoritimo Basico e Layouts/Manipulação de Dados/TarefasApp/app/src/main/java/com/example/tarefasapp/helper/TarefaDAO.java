package com.example.tarefasapp.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tarefasapp.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO implements ITarefaDAO{

    private SQLiteDatabase escreva;
    private SQLiteDatabase leia;

    public TarefaDAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        escreva = dbHelper.getWritableDatabase();
        leia = dbHelper.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {

        ContentValues contv = new ContentValues();
        contv.put("nome", tarefa.getNome());

        try{
            escreva.insert(DbHelper.TABELA_TAREFAS, null, contv);
            Log.i("INFO", "Tarefa salva.");
        }catch (Exception e){
            Log.e("INFO", "Erro ao salvar tarefa" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {

        ContentValues contv = new ContentValues();
        contv.put("nome", tarefa.getNome());

        try{
            String[] args = {tarefa.getId().toString()};
            escreva.update(DbHelper.TABELA_TAREFAS, contv,  "id=?", args);
            Log.i("INFO", "Tarefa atualizada.");
        }catch (Exception e){
            Log.e("INFO", "Erro ao atualizar tarefa" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {
        try{
            String[] args = {tarefa.getId().toString()};
            escreva.delete(DbHelper.TABELA_TAREFAS, "id=?", args);
            Log.i("INFO", "Tarefa removida com sucesso.");
        }catch (Exception e){
            Log.e("INFO", "Erro ao remover tarefa" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Tarefa> listar() {

        List<Tarefa> tarefasListadas = new ArrayList<>();
        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + ";";
        Cursor c = leia.rawQuery(sql, null);

        while ( c.moveToNext() ){
            Tarefa t = new Tarefa();
            @SuppressLint("Range")
            Long id = c.getLong( c.getColumnIndex("id") );
            @SuppressLint("Range")
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));

            t.setId(id);
            t.setNome(nomeTarefa);

            tarefasListadas.add(t);
        }

        return tarefasListadas;
    }

}
