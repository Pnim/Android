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

    private static String replaceWhiteSpace(String text) {
        return text.replaceAll("[\\s]+", " ");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getAnswerForQuestionBySuperPuperRoboAI(String question, Consumer<String> addMessageFunc ) {
        question = replaceWhiteSpace(question).toLowerCase();
        ArrayList<String> answers = new ArrayList<>();
        answers.addAll(processQuestion(question));
        for (int i = 0; i < answers.size(); i++) {
            addMessageFunc.accept(answers.get(i));
        }
    }

    public static String normalizeEnding(int b) {
        int data = Math.abs(b);
        if(data >= 11 && data <= 14 )
            return " градусов ";
        if(data % 10 == 0 || data % 10 >= 5 && data %10 <= 9)
            return " градусов ";
        if(data % 10 == 1)
            return " градус ";
        if(data >= 2 && data <=4)
            return " градуса ";
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static ArrayList<String> processQuestion(String question) {
        final ArrayList<String> result = new ArrayList<>();
        if (question.contains("привет")) {
            result.add("Привет!");
        }
        if (question.contains("как дела")) {
            result.add("Отлично, как у тебя?");
        }
        if (question.contains("не очень")) {
            result.add("Почему не очень?");
        }
        if (question.contains("чем занимаешься")) {
            result.add("Сижу.");
        }
        if (question.contains("как настроение")) {
            result.add("Как у человека");
        }
        if (question.contains("расскажи о себе")) {
            result.add("Самый настоящий! ИСКУСТВЕННЫЙ! Интеллект.");
        }
        if (question.contains("сегодня день")) {
            result.add("Сегодня " +new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        }
        if (question.contains("который час")) {
            result.add("Сейчас " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        }
        if (question.contains("какая погода в")) {
            try {
                Pattern pattern = Pattern.compile("какая погода в (.+)", Pattern.CASE_INSENSITIVE);
                Matcher match = pattern.matcher(question);
                if (match.find() || match.group(1) != null) {
                    String cityName = match.group(1);
                    ForecastToString.getForecast(cityName, new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            if (s != null) {
                                result.add(s);
                            } else{
                                result.add("Не знаю:(");
                            }
                        }
                    });
                }
            }
            catch (Exception e) {
                result.add("Не получается узнать :(");
            }
        }
        if (question.contains("перевести число")) {
            try {
                final  String number = question.replaceAll("[^0-9\\+]", "");
                String finalQuestion = question;
                ConvertedNumberToString.getConvertNumber(number, new java.util.function.Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        if (s != null) {
                            result.add(s);
                        } else {
                            result.add("Не получается!");
                        }
                    }
                });
            }
            catch (Exception e) {
                result.add("Не получается узнать :(");
            }
        }
        if (question.contains("праздник")) {
            try {
                String findDate = getDate(question);
                String res = "";
                String[] data = findDate.split(",");
                for (int i = 0; i < data.length; i++) {
                    try {
                        res += data[i] +": " + HtmlParser.getHoliday(data[i]) + "\n";

                    } catch (IOException e) {
                        res = "Упс, ошибочка...";
                    }
                }
                result.add(res);
            } catch (Exception e) {
                result.add("Не получается узнать :(");
            }

        }
        return result;
    }
}
