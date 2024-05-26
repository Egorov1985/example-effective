package ru.egorov.effectiveexample.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckString {

    public static boolean checkingStringForPhoneNumber(String check){
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(check);
        return matcher.matches();
    }

    public static boolean checkingStringForEmail(String check){
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(check);
        return matcher.matches();
    }

    public static boolean checkingStringForDate(String check){
        Pattern pattern = Pattern.compile("\\d{2}.\\d{2}.\\d{4}");
        Matcher matcher = pattern.matcher(check);
        return matcher.matches();
    }

    public static boolean checkingStringForName(String check){
        Pattern pattern = Pattern.compile("^([А-ЯЁ][а-яё]+\\s[А-ЯЁ][а-яё]+\\s[А-ЯЁ][а-яё\\-]+$)|([А-ЯЁ][а-яё]+\\s[А-ЯЁ][а-яё]+$)|" +
                "([А-ЯЁ][а-яё]+$)");
        Matcher matcher = pattern.matcher(check);
        return matcher.matches();
    }
}
