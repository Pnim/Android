package com.example.voiceassistent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlParser {
    private static final String URL = "http://mirkosmosa.ru/holiday/2020";

    public static String getHoliday(String date) throws IOException {
        Document document = Jsoup.connect(URL).get();
        Element body = document.body();
        Elements div = body.select("div.month_cel_date");
        String result = "Праздник в этот день не найден";
        for (Element element: div)
        {
            String dataHoliday = element.select( "span").get(0).text();
            if (date.startsWith("0")) {
                date = date.substring(1);
            }
            if (dataHoliday.equals(date)){
                Elements month = element.siblingElements();
                Element monthElement = month.first();
                String li = monthElement.select("li").text();
                result = li;
                break;
            }
        }
        return result;
    }
}
