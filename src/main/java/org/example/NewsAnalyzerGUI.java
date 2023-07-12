package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsAnalyzerGUI extends JFrame {
    private Set<String> exclusions;
    private JTextArea keywordsTextArea;
    private JTextArea resultsTextArea;
    private JButton startButton;

    public NewsAnalyzerGUI() {
        exclusions = new HashSet<>();
        initializeGUI();
    }

    private void initializeGUI() {


        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.NORTH);

        JPanel resultPanel = createResultPanel();
        add(resultPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());

        JLabel keywordsLabel = new JLabel("Excluded Keywords/Phrases:");
        inputPanel.add(keywordsLabel, BorderLayout.NORTH);

        keywordsTextArea = new JTextArea(5, 20);
        JScrollPane keywordsScrollPane = new JScrollPane(keywordsTextArea);
        inputPanel.add(keywordsScrollPane, BorderLayout.CENTER);

        startButton = new JButton("Start Scanning");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                exclusions.clear();
                String[] keywords = keywordsTextArea.getText().split("\n");
                Collections.addAll(exclusions, keywords);
                performScan();
            }
        });
        inputPanel.add(startButton, BorderLayout.SOUTH);

        return inputPanel;
    }

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());

        JLabel resultsLabel = new JLabel("Results:");
        resultPanel.add(resultsLabel, BorderLayout.NORTH);

        resultsTextArea = new JTextArea(10, 30);
        resultsTextArea.setEditable(false);
        JScrollPane resultsScrollPane = new JScrollPane(resultsTextArea);
        resultPanel.add(resultsScrollPane, BorderLayout.CENTER);

        return resultPanel;
    }

    private void performScan() {
        while (true) {
            try {
                // Scrape news websites
                Map<String, Integer> keywordCounts = new HashMap<>();
                for (String website : getNewsWebsites()) {
                    Document document = Jsoup.connect(website).get();
                    Elements articles = document.select("article");

                    for (Element article : articles) {
                        String text = article.text();
                        String[] words = text.split("\\s+");

                        for (String word : words) {
                            word = word.toLowerCase();
                            if (!exclusions.contains(word)) {
                                keywordCounts.put(word, keywordCounts.getOrDefault(word, 0) + 1);
                            }
                        }
                    }
                }

                // Sort keywords by frequency
                List<Map.Entry<String, Integer>> sortedKeywords = new ArrayList<>(keywordCounts.entrySet());
                sortedKeywords.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

                // Display results
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        resultsTextArea.setText("");
                        resultsTextArea.append("Most Popular Keywords:\n");
                        int rank = 1;
                        for (Map.Entry<String, Integer> entry : sortedKeywords) {
                            resultsTextArea.append(rank + ". " + entry.getKey() + " (" + entry.getValue() + " occurrences)\n");
                            rank++;
                        }
                        resultsTextArea.append("\nPopular Keywords by Website:\n");
                        for (String website : getNewsWebsites()) {
                            resultsTextArea.append(website + ":\n");
                            for (Map.Entry<String, Integer> entry : sortedKeywords) {
                                if (entry.getKey().equalsIgnoreCase(website)) {
                                    resultsTextArea.append("\t" + entry.getKey() + " (" + entry.getValue() + " occurrences)\n");
                                    break;
                                }
                            }
                        }
                    }
                });

                Thread.sleep(10 * 60 * 1000); // Wait for 10 minutes before the next scan
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> getNewsWebsites() {
        return Arrays.asList("https://www.ynet.co.il");
    }


}
