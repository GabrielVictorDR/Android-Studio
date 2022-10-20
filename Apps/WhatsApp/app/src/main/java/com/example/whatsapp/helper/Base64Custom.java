package com.example.whatsapp.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificarBase64(String email) {

        byte[] encodedBytes = Base64.encode(email.getBytes(), Base64.DEFAULT);

        return new String(encodedBytes).replaceAll("(\\n|\\r)", "");

    }

}
