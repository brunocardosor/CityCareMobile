package com.example.administrador.citycaremobile.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrador on 25/09/2017.
 */

public class PatternUtils {
    //String com sintaxe do Regex
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //Criando o padrão para o regex, definindo o caso como não sensitive
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    public boolean emailValido(String email){
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
