import com.formdev.flatlaf.FlatDarkLaf;
import okhttp3.OkHttpClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class jIntel {

    public static void main(String[] args) {
        // Set the look and feel to Dark FlatLaf
        FlatDarkLaf.setup();

        // Create and set up the window
        JFrame frame = new JFrame("jIntel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLayout(new BorderLayout());

        // UI Elements
        JTextField characterNameField = new JTextField();
        characterNameField.setPreferredSize(new Dimension(280, 30));
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        // Top Panel (Search Bar)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        topPanel.add(characterNameField);

        // Result Area
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Add Components to Frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Action when Enter is pressed
        characterNameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String characterName = characterNameField.getText();
                    OkHttpClient httpClient = new OkHttpClient();
                    ToonRetriever retriever = new ToonRetriever(httpClient);
                    zKillFetcher zKillFetcher = new zKillFetcher(retriever);

                    try {
                        Long characterId = retriever.getCharacterId(characterName);
                        if (characterId != null) {
                            String characterDetails = retriever.fetchCharacterDetails(characterId);
                            String zKillDetails = zKillFetcher.fetchZKillInfo(characterId);
                            resultArea.setText(characterDetails + zKillDetails);
                        } else {
                            resultArea.setText("Character not found: " + characterName);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        resultArea.setText("An error occurred: " + ex.getMessage());
                    }
                }
            }
        });

        // Display the window
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
