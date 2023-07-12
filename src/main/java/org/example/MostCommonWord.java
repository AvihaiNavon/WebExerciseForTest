package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//מוציא את המילה הכי נפוצה
public class MostCommonWord {
    public MostCommonWord() {
        String url = "https://www.mako.co.il";
        Map.Entry<String, Integer> mostCommonWordEntry = getMostCommonWord(url);
        System.out.println("The most common word on " + url + " is \"" + mostCommonWordEntry.getKey() + "\".");
        System.out.println("It appears " + mostCommonWordEntry.getValue() + " times.");
    }

    private  Map.Entry<String, Integer> getMostCommonWord(String url) {
        String html = getHtmlContent(url);
        Document document = Jsoup.parse(html);

        String text = document.text();
        String[] words = text.split("\\s+");
        Map<String, Integer> wordCountMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (String word : words) {
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

        Map.Entry<String, Integer> mostCommonWordEntry = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonWordEntry = entry;
                maxCount = entry.getValue();
            }
        }

        return mostCommonWordEntry;
    }

    private  String getHtmlContent(String url) {
        StringBuilder content = new StringBuilder();
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}