import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class YuGiOhTournament {
    private static final int MAX_PLAYERS = 32;
    private List<Player> players = new ArrayList<>();
    private int totalRounds;
    private int currentRound = 0;
    private String tournamentName;
    private JTable playerTable;
    private DefaultTableModel tableModel;
    private List<Player[]> matchups = new ArrayList<>();

    public YuGiOhTournament() {
        JFrame frame = new JFrame("Yu-Gi-Oh Tournament");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLayout(new BorderLayout());

        // Gornji panel za unos podataka i sliku
        JPanel topPanel = new JPanel(new BorderLayout());

        // Panel za unos podataka o turniru
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Usmjerenje lijevo
        JLabel tournamentLabel = new JLabel("Tournament Name:");
        JTextField tournamentField = new JTextField(15);
        JLabel playersLabel = new JLabel("Number of Players:");
        JTextField playersField = new JTextField(5);
        JLabel roundsLabel = new JLabel("Number of Rounds:");
        JTextField roundsField = new JTextField(5);
        JButton startButton = new JButton("Start Tournament");

        inputPanel.add(tournamentLabel);
        inputPanel.add(tournamentField);
        inputPanel.add(playersLabel);
        inputPanel.add(playersField);
        inputPanel.add(roundsLabel);
        inputPanel.add(roundsField);
        inputPanel.add(startButton);

        // Dodavanje slike u gornji lijevi kut
        try {
            ImageIcon logoIcon = new ImageIcon("yugioh_logo.png");
            Image image = logoIcon.getImage(); // Uzimanje originalne slike
            Image scaledImage = image.getScaledInstance(200, 150, Image.SCALE_SMOOTH); // Skaliranje slike
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            topPanel.add(logoLabel, BorderLayout.WEST); // Dodaj sliku u gornji lijevi kut
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        topPanel.add(inputPanel, BorderLayout.CENTER); // Unos podataka ide u centar

        frame.add(topPanel, BorderLayout.NORTH); // Gornji panel sa slikom i unosom podataka ide na vrh

        // Tabela za prikaz igrača
        tableModel = new DefaultTableModel(new Object[]{"Player Name", "Wins", "Losses", "Draws", "Points"}, 0);
        playerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(playerTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tournamentName = tournamentField.getText();
                int numPlayers = Integer.parseInt(playersField.getText());
                totalRounds = Integer.parseInt(roundsField.getText());

                if (numPlayers > 0 && numPlayers <= MAX_PLAYERS) {
                    addPlayers(numPlayers);
                    updateTable();
                    startButton.setVisible(false);
                    frame.setTitle(tournamentName + " - Players Added");
                    createNewRound(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid number of players.");
                }
            }
        });

        frame.setVisible(true);
    }

    private void addPlayers(int numPlayers) {
        Set<String> names = new HashSet<>();
        for (int i = 0; i < numPlayers; i++) {
            String playerName = JOptionPane.showInputDialog("Enter Player " + (i + 1) + " Name:");
            if (playerName != null && !playerName.trim().isEmpty() && names.add(playerName)) {
                players.add(new Player(playerName));
            } else {
                JOptionPane.showMessageDialog(null, "Invalid or duplicate player name. Please try again.");
                i--;
            }
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Player player : players) {
            tableModel.addRow(new Object[]{player.name, player.wins, player.losses, player.draws, player.points});
        }
        sortTable();
    }

    private void sortTable() {
        List<Player> sortedPlayers = new ArrayList<>(players);
        Collections.sort(sortedPlayers, Comparator.comparingInt((Player p) -> -p.points)
                .thenComparingInt(p -> -p.wins)
                .thenComparingInt(p -> p.losses));
        tableModel.setRowCount(0);
        for (Player player : sortedPlayers) {
            tableModel.addRow(new Object[]{player.name, player.wins, player.losses, player.draws, player.points});
        }
    }

    private void createNewRound(JFrame frame) {
        if (currentRound < totalRounds) {
            matchups.clear(); // Clear previous matchups for the new round
            JFrame roundFrame = new JFrame("Round " + (currentRound + 1));
            roundFrame.setSize(400, 300);
            roundFrame.setLayout(new BorderLayout());

            List<Player> playersToPair = new ArrayList<>(players);
            if (playersToPair.size() % 2 != 0) {
                Player byePlayer = playersToPair.remove(playersToPair.size() - 1);
                byePlayer.updateScore("Win"); // BYE player gets a win
                JOptionPane.showMessageDialog(roundFrame, byePlayer.name + " gets a BYE and wins this round!");
            }

            // Sort players before pairing them for the current round
            Collections.sort(playersToPair, Comparator.comparingInt((Player p) -> -p.points)
                    .thenComparingInt(p -> -p.wins)
                    .thenComparingInt(p -> p.losses));

            for (int i = 0; i < playersToPair.size(); i += 2) {
                Player player1 = playersToPair.get(i);
                Player player2 = playersToPair.get(i + 1);
                matchups.add(new Player[]{player1, player2});
            }

            JPanel matchesPanel = new JPanel();
            matchesPanel.setLayout(new GridLayout(matchups.size(), 1));

            List<JComboBox<String>> resultCombos = new ArrayList<>();

            for (Player[] matchup : matchups) {
                JPanel matchPanel = new JPanel();
                matchPanel.setLayout(new FlowLayout());
                matchPanel.add(new JLabel(matchup[0].name + " vs " + matchup[1].name));

                String[] results = {matchup[0].name + " pobjeda", matchup[1].name + " pobjeda", "Neriješeno"};
                JComboBox<String> resultCombo = new JComboBox<>(results);
                resultCombos.add(resultCombo);
                matchPanel.add(resultCombo);
                matchesPanel.add(matchPanel);
            }

            JButton submitResults = new JButton("Submit Results");
            submitResults.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean allSelected = true;

                    // Check if all results are selected
                    for (JComboBox<String> combo : resultCombos) {
                        if (combo.getSelectedItem() == null) {
                            allSelected = false;
                            break;
                        }
                    }

                    if (allSelected) {
                        for (int i = 0; i < matchups.size(); i++) {
                            String result = (String) resultCombos.get(i).getSelectedItem();
                            Player[] matchup = matchups.get(i);
                            if (result.equals(matchup[0].name + " pobjeda")) {
                                matchup[0].updateScore("Win");
                                matchup[1].updateScore("Loss");
                            } else if (result.equals(matchup[1].name + " pobjeda")) {
                                matchup[0].updateScore("Loss");
                                matchup[1].updateScore("Win");
                            } else if (result.equals("Neriješeno")) {
                                matchup[0].updateScore("Draw");
                                matchup[1].updateScore("Draw");
                            }
                        }
                        updateTable();
                        roundFrame.dispose();
                        currentRound++;
                        createNewRound(frame);
                    } else {
                        JOptionPane.showMessageDialog(roundFrame, "Please select results for all matches before submitting.");
                    }
                }
            });

            roundFrame.add(matchesPanel, BorderLayout.CENTER);
            roundFrame.add(submitResults, BorderLayout.SOUTH);
            roundFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(frame, "Tournament has ended! Final rankings:");
            sortTable();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(YuGiOhTournament::new);
    }
}
