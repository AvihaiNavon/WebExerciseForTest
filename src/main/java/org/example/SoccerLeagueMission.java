package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
// לחתוך טקסט ותוצאות של קבוצת כדורגל
import java.io.IOException;
import java.util.Scanner;

public class SoccerLeagueMission {
    public SoccerLeagueMission() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("הזן את שם קבוצת הכדורגל: ");
        String teamName = scanner.nextLine(); // קליטת שם הקבוצה מהמשתמש
        String url = "https://www.rsssf.org/tablesi/isra2022.html"; // כתובת האתר
        boolean isExist;
        String divider = "";

        try {
            Document document = Jsoup.connect(url).get(); // התחברות לכתובת האתר
            System.out.println(document.title()); // הדפסת כותרת העמוד (לצורך אימות)
            String data = document.body().text();

            for (int z = 1; z < 36; z++) { // לולאה על כל סיבוב
                isExist = false;
                System.out.println("סיבוב " + z);
                divider = data.substring(data.indexOf("Round " + z), data.indexOf("Round " + (z + 1))); // חלוקת הנתונים לסיבובים
                String[] splitter = divider.split("\n"); // חלוקת הנתונים לשורות

                String catcher = "";
                for (int i = 0; i < splitter.length; i++) { // חיפוש השורה שמכילה את שם הקבוצה
                    if (splitter[i].contains(teamName)) {
                        catcher = splitter[i];
                        isExist = true;
                        break;
                    }
                }

                if (isExist) {
                    String[] secondSplitter = catcher.split(" "); // חלוקת השורה למילים
                    String team = secondSplitter[0] + " " + secondSplitter[1]; // שם הקבוצה
                    String number = "";

                    for (int i = 2; i < secondSplitter.length; i++) { // חיפוש התוצאה של הקבוצה
                        if (!secondSplitter[i].equals("")) {
                            number = secondSplitter[i];
                            String help = number.charAt(0) + "";

                            if ("0123456789".contains(help)) {
                                break;
                            }
                        }
                    }

                    int first = 0;
                    int second = 0;

                    try {
                        first = Integer.parseInt(number.substring(0, 1)); // תוצאת הקבוצה הראשונה
                        second = Integer.parseInt(number.substring(2)); // תוצאת הקבוצה השנייה
                    } catch (Exception e) {
                        System.out.println("ok");
                    }

                    if (team.equals(teamName) || team.substring(team.length() - 1).equals(teamName)) {
                        if (first > second) {
                            System.out.println("מנצחת");
                        } else if (second > first) {
                            System.out.println("מפסידה");
                        } else {
                            System.out.println("תיקו!");
                        }
                    } else {
                        if (second > first) {
                            System.out.println("מנצחת");
                        } else if (second < first) {
                            System.out.println("מפסידה");
                        } else {
                            System.out.println("תיקו!");
                        }
                    }

                } else {
                    System.out.println("הקבוצה לא קיימת בסיבוב זה");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
