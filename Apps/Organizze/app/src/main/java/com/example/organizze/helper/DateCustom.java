package com.example.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }
    public static String mesAnoDataEscolhida(String data){
        String retornoData[] = data.split("/");
        String dia = retornoData[0]; // dia 17
        String mes = retornoData[1]; // mes 10
        String ano = retornoData[2]; // ano 2022

        String mesAno = mes + ano;

        return mesAno;
    }
}
