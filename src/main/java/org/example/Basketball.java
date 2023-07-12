package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Basketball extends JFrame {

//לוקח טבלה של שחקני כדורסל שמופיעה באינטרנט ומכניסה לרשימה
    public Basketball() {
        //test();
        setTitle("basketball");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        generateNames();


        try {
            Document documentPlayers = Jsoup.connect("https://www.basketball-reference.com/players/a/").get();
            //generateNames(documentPlayers);
            System.out.println("Check");
            Document documentTeams = Jsoup.connect("https://www.basketball-reference.com/teams").get();
            // Send a GET request to the URL and retrieve the HTML content
            Document document = Jsoup.connect("https://www.basketball-reference.com/players/a/").get();


            List<String> playerList = new ArrayList<>();

            try {
                // Connect to the player index page
                Document doc = Jsoup.connect("https://www.basketball-reference.com/players").get();

                // Find the table containing the player list
                Element table = doc.select("table_container is_setup").first();

                // Find all the rows in the table
               // Elements rows = table.select("tbody tr");

                //Extract the player names from each row
//                for (Element row : rows) {
//                    Elements columns = row.select("td");
//                    Element playerColumn = columns.get(0);
//                    String playerName = playerColumn.text();
//                    System.out.println(playerName);
//                    playerList.add(playerName);
                // }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void test() {
        try {
            Document documentPlayers = Jsoup.connect("https://www.basketball-reference.com/players/a/").get();
            Document documentTeams = Jsoup.connect("https://www.basketball-reference.com/teams").get();
            Elements element = documentPlayers.getElementsByClass("sortable stats_table now_sortable");
            Element mainArticleEleElement = documentPlayers.getElementById("players");
            System.out.println(mainArticleEleElement.text());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void generateNames() {
        String baseUrl = "https://www.basketball-reference.com/players/";//url without the a-z
        String alphabet = "abcdefghijklmnopqrstuvwxyz";//a-z

        try {
            for (int i = 0; i < alphabet.length(); i++) {
                char letter = alphabet.charAt(i);
                String url = baseUrl + letter + "/";//loop running for a-z

                Document doc = Jsoup.connect(url).get();//running the website
                Elements rows = doc.select("#players > tbody > tr");

                System.out.println("Letter: " + letter);
                for (Element row : rows) {
                    Elements nameColumns = row.select("th[data-stat=player] a");

                    if (!nameColumns.isEmpty()) {
                        Element nameColumn = nameColumns.first();
                        String fullName = nameColumn.text();
                        System.out.println("Name: " + fullName);
                    }
                }

                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





