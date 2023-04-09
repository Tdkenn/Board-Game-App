package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Game;
import model.Review;
import model.UserDataManager;

import java.awt.*;
import java.net.URL;

public class GameDataView extends JPanel {

    private DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private Game currentGameOnScreen;
    private JPanel gameDataPanel;
    private JLabel gameName;
    private JScrollPane scrollPane;
    private JLabel imageLabel;
    private JTextPane infoTextPane;
    private JTable reviewTable;
    private JSlider ratingSlider;
    private JTextField ratingTextBox;
    private JButton ratingSubmitButton;

    public GameDataView() {

        tableModel.addColumn("Rating");
        tableModel.addColumn("Review");
        tableModel.addColumn("User");

        reviewTable.setModel(tableModel);

        reviewTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        reviewTable.setRowHeight(30);
        reviewTable.getTableHeader().setReorderingAllowed(false);

        ratingSubmitButton.addActionListener(e -> {
            int rating = ratingSlider.getValue();
            String reviewText = ratingTextBox.getText();

            ratingSlider.setValue(10);
            ratingTextBox.setText("");

            Review review = new Review(rating, reviewText, currentGameOnScreen.getID(), UserDataManager.currentUser.getUserName());
            //Add review to game object
            currentGameOnScreen.addReview(review);

            //Add review to screen
            tableModel.addRow(new Object[]{rating, reviewText, UserDataManager.currentUser.getUserName()});

            //Save review to XML
            UserDataManager.saveReview(review);
        });
    }

    public JPanel getPanel() {
        return gameDataPanel;
    }

    public void setGame(Game g) {

        tableModel.setRowCount(0);

        currentGameOnScreen = g;

        gameName.setText(g.getName());

        ImageIcon imageIcon = null;
        try {
            URL url = new URL(g.getFullSizeImage());
            Image image = ImageIO.read(url).getScaledInstance(-1, 450, Image.SCALE_FAST);
            imageIcon = new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        infoTextPane.setText("Player Count: " + g.getMinPlayers() + " - " + g.getMaxPlayers() + "\nPlaying Time: " + g.getPlayingTime() + " min" + "\nYear Published: " + g.getYearPublished());

        imageLabel.setIcon(imageIcon);

        for (Review r : g.getReviewList()) {
            tableModel.addRow(new Object[]{r.getRating(), r.getReviewText(), r.getUserName()});
        }
    }
}
