package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//מחפש כתבות עם ערך שמשתמש הכניס למערכת
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsScraperGUI {
    private static final int MAX_ARTICLES = 5; // מספר הכתבות המרבי לתצוגה
    private JFrame frame;
    private JTextField keywordTextField;
    private JTextArea articlesTextArea;
    private JButton searchButton;
    private List<String> articlesList;
    private Set<String> uniqueArticlesSet;

    public NewsScraperGUI() {
        uniqueArticlesSet = new HashSet<>();
        frame = new JFrame("News Scraper");//יוצר חלון
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//סגירת חלון
        frame.setSize(600, 300);//גודל חלון
        frame.setLayout(new BorderLayout());//מגדיר גבולות

        keywordTextField = new JTextField();// יצירת כתובת טקסט
        keywordTextField.setColumns(5);//מגדיל את הגודל של התיבת טקסט
        searchButton = new JButton("Search");// יצירת כפתור חיפוש
        articlesTextArea = new JTextArea();// יצירת מקום לכתיבת התצואות של טקסט
        articlesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(articlesTextArea);// משתנה של גלילת טקסט

        JPanel topPanel = new JPanel(new FlowLayout());// יצירת פאנל
        topPanel.add(new JLabel("Keyword:"));// להוסיך טקסט לפאנל של מקום לכתיבת חיפוש
        topPanel.add(keywordTextField);// מוסיך חלון טקסט
        topPanel.add(searchButton);//מוסיף חיפוש

        frame.add(topPanel, BorderLayout.NORTH);//מוסיף לחלון את הפאנל עם כפתורי החיפוש
        frame.add(scrollPane, BorderLayout.CENTER);// מוסיף לחלון את הטקסט החופשי

        searchButton.addActionListener(new ActionListener() { //כאשר נלחץ על כפתור החיפוש יבוצע פעולה
            public void actionPerformed(ActionEvent e) {
                String keyword = keywordTextField.getText(); // מקבל את הטקסט שהוכנס ע"י המשתמש
                if (!keyword.isEmpty()) { //אם הטקסט אינו ריק תבצע את הפעולה
                    searchArticles(keyword);
                }
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    private void searchArticles(String keyword) {
        articlesList = new ArrayList<>(); //תיצור רשימה

        Thread scraperThread = new Thread(new Runnable() { //תיצור תהליכון
            public void run() {
                while (true) {
                    try {
                        // כתוב כאן אתרי החדשות שברצונך לסרוק
                        //הפעולה של חיפוש הכתבות מתבצעת בעזרת תהליך נפרד (Thread)
                        //כדי לאפשר לתוכנית להמשיך לתפקד במקביל.
                        //בתוך התהליך, אנו מבצעים סריקה של אתרי החדשות שצוינו ומנתחים את הדפים שלהם על מנת למצוא את הכתבות הרלוונטיות למילת המפתח שהוזנה.
                        String[] newsSites = {"https://one.co.il", "https://www.ynet.co.il","https://www.walla.co.il","https://www.mako.co.il"};

                        for (String site : newsSites) {
                            Document doc = Jsoup.connect(site).get();// מקבל את כתובת האתר
                            Elements articles = doc.select("a[href]");// לוקח את האלמנטים והקישור של הכתבות הראשיות באתר
                            for (Element article : articles) {// עובר על כל האלמנטים הראשיים
                                String title = article.text();//לוקח את הכותרת
                                String link = article.attr("abs:href");//לוקח את הקישור
                                if (title.toLowerCase().contains(keyword.toLowerCase())) { //אם המלל שווה אחד לשני
                                    String articleInfo = "אתר אינטרנט: " + site + "\n" +
                                            "שם הכתבה: " + title + "\n" +
                                            "קישור: " + link + "\n";
                                    addArticleToList(articleInfo); //תכניס לרשימה
                                }
                            }
                        }

                        Thread.sleep(10000); // זמן המתנה בין סריקות
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        scraperThread.start();
    }

    private void addArticleToList(String articleInfo) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (uniqueArticlesSet.contains(articleInfo)) {
                    return; // הכתבה כבר קיימת ברשימה, נמנע מהוספתה
                }

                articlesList.add(articleInfo);
                uniqueArticlesSet.add(articleInfo);

                if (articlesList.size() > MAX_ARTICLES) {// במידה וגדול מחמש כתבות
                    String removedArticle = articlesList.remove(0);// מוחק איבר אחרון
                    uniqueArticlesSet.remove(removedArticle);
                }

                articlesTextArea.setText("");

                for (String article : articlesList) {
                    articlesTextArea.append(article + "\n\n");
                }
            }
        });
    }


}
