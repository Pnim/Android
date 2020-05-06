package com.example.voiceassistent;

import android.os.Build;
import android.text.Html;

import androidx.annotation.RequiresApi;
import androidx.core.util.Consumer;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SuperPuperRoboAI {

    public static String replaceWhiteSpace(String text) {
        return text.replaceAll("[\\s]+", " ");
    }

    public static String normalizeEnding(int b) {
        int data = Math.abs(b);
        if(data % 10 == 0 || (data >= 10 && data <= 20))
            return "градусов ";
        if(data % 10 == 1)
            return "градус ";
        if(data >= 22 && data <= 4)
            return "градуса ";
        return "";
    }

    private static DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(){

        @Override
        public String[] getMonths() {
            return new String[]{ "января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря" };
        }
    };

    private static String getDate(String question) throws ParseException {
        String date = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat =  new SimpleDateFormat("dd MMMM YYYY", dateFormatSymbols);

        if(question.contains("сегодня")){
            Date today = calendar.getTime();
            date = dateFormat.format(today) + ",";
        }
        if(question.contains("завтра")){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            date += dateFormat.format(tomorrow) + ",";
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        if(question.contains("вчера")){
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            Date yesterday = calendar.getTime();
            date += dateFormat.format(yesterday) + ",";
            calendar.add(Calendar.DAY_OF_YEAR, +1);
        }
        String pattern="\\d{1,2}\\.\\d{1,2}\\.\\d{4}";
        Matcher matcher=Pattern.compile(pattern).matcher(question);
        String tempDate = null;
        if(!matcher.find()) {
            tempDate = null;
        }else {
            tempDate = question.substring(matcher.start(), matcher.end());
        }
        if (tempDate != null) {
            Date currentDate = new SimpleDateFormat("dd.MM.yyyy").parse(tempDate);
            date += dateFormat.format(currentDate);
        }
        return  date;
    }

    public static void processQuestion(String question, final Consumer<String> addMessageFunc) {
        question = question.toLowerCase();
        if (question.contains("привет")) {
            addMessageFunc.accept("Привет!");
        }
        else if (question.contains("как дела")) {
            addMessageFunc.accept("Отлично, как у тебя?");
        }
        else if (question.contains("не очень")) {
            addMessageFunc.accept("Почему не очень?");
        }
        else if (question.contains("чем занимаешься")) {
            addMessageFunc.accept("Сижу.");
        }
        else if (question.contains("как настроение")) {
            addMessageFunc.accept("Как у человека");
        }
        else if (question.contains("расскажи о себе")) {
            addMessageFunc.accept("Самый настоящий! ИСКУСТВЕННЫЙ! Интеллект.");
        }
        else if (question.contains("сегодня день")) {
            addMessageFunc.accept("Сегодня " +new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        }
        else if (question.contains("который час")) {
            addMessageFunc.accept("Сейчас " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        }
        else if (question.contains("погода в городе")) {
            try {
                Pattern pattern = Pattern.compile("погода в городе (.+)", Pattern.CASE_INSENSITIVE);
                Matcher match = pattern.matcher(question);
                if (match.find()) {
                    String cityName = match.group(1);
                    ForecastToString.getForecast(cityName, s -> {
                        if (s != null) {
                            addMessageFunc.accept(s);
                        } else {
                            addMessageFunc.accept("Не знаю:(");
                        }
                    });
                }
            }
            catch (Exception e) {
                addMessageFunc.accept("Не получается узнать :(");
            }
        }
        else if (question.contains("перевести число")) {
            try {
                final  String number = question.replaceAll("[^0-9\\+]", "");
                String finalQuestion = question;
                ConvertedNumberToString.getConvertNumber(number, s -> {
                    if (s != null) {
                        addMessageFunc.accept(s);
                    } else {
                        addMessageFunc.accept("Не получается узнать!");
                    }
                });
            }
            catch (Exception e) {
                addMessageFunc.accept("Не получается узнать :(");
            }
        }
        else if (question.contains("праздник")) {
            try {
                String[] ans = {""};
                String findDate = getDate(question);
                String[] strings = findDate.split(",");
                Observable.fromCallable(() -> {
                    ans[0] = "";
                    for (int i = 0; i < strings.length; i++) {
                        try {
                            ans[0] += strings[i] +": " + HtmlParser.getHoliday(strings[i]) + "\n";

                        } catch (IOException e) {
                            ans[0] = "Не получается узнать!";
                        }
                    }
                    return ans[0];
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((res)->{
                    addMessageFunc.accept(ans[0]);
                });
            } catch (Exception e) {
                addMessageFunc.accept("Не получается узнать :(");
            }
        }
    }
}
