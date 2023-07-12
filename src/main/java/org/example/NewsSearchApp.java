package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//מחפש מילה שהכניס המשתמש בכל האתר ומחזיר כמה פעמים מופעיה
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NewsSearchApp {
    private JFrame frame;
    private JTextField searchField;
    private JTextArea resultArea;

    public NewsSearchApp() {
        frame = new JFrame("News Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        searchField = new JTextField();
        searchField.setColumns(5);//מגדיל את הגודל של התיבת טקסט
        JButton searchButton = new JButton("Search");
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().trim();
                if (!searchTerm.isEmpty()) {
                    try {
                        int count = searchNews(searchTerm);
                        resultArea.setText("Occurrences of \"" + searchTerm + "\": " + count);
                    } catch (IOException ex) {
                        resultArea.setText("Error occurred during search.");
                        ex.printStackTrace();
                    }
                }
            }
        });

        frame.setVisible(true);
    }

    private int searchNews(String searchTerm) throws IOException {
        String url = "https://www.mako.co.il/";
        Document doc = Jsoup.connect(url).get();
        String content = doc.text();
        int count = 0;
        int index = content.indexOf(searchTerm);
        while (index != -1) {
            count++;
            content = content.substring(index + searchTerm.length());
            index = content.indexOf(searchTerm);
        }
        return count;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NewsSearchApp();
            }
        });
    }
}
