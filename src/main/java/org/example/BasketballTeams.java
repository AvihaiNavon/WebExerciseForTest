package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;

import java.io.IOException;

public class BasketballTeams {
    public BasketballTeams(){
        try {
            Document documentTeams = Jsoup.connect("https://www.basketball-reference.com/teams").get();
            //System.out.println(documentTeams.outerHtml());//מדפיס לבדיקה את כל ה html
            //System.out.println(documentTeams.select("#teams_active th"));

             for (Element row:documentTeams.select("#teams_active th")){ //לוקחים את הקוד הכחול שמעבירים את העכבר לוקחים את המחלקה של הטבלה ועוברים על השורות שלה
              //System.out.println(row.select("a[href]").text());
                 System.out.println(row.text());


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
