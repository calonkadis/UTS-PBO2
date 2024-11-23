import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RecipeApp {
    private JFrame frame;
    private JTextField nameField;
    private JTextArea ingredientField, stepsField, recipePreview;
    private JList<String> recipeList;
    private DefaultListModel<String> recipeModel, ingredientModel;
    private ArrayList<Recipe> recipes;

    public RecipeApp() {
        // Inisialisasi data
        recipes = new ArrayList<>();
        recipeModel = new DefaultListModel<>();
        ingredientModel = new DefaultListModel<>();

        // Setup frame utama
        frame = new JFrame("Aplikasi Resep Makanan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Membuat TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel Input Resep
        JPanel inputPanel = createInputPanel();

        // Panel Daftar Resep
        JPanel listPanel = createListPanel();

        // Tambahkan panel ke tabbedPane
        tabbedPane.addTab("Input Resep", inputPanel);
        tabbedPane.addTab("Daftar Resep", listPanel);

        // Panel Tombol Aksi
        JPanel buttonPanel = createButtonPanel();

        // Tambahkan ke frame
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input Nama Resep
JLabel nameLabel = new JLabel("Nama Resep:");
nameField = new JTextField(30); // Panjang kolom teks sekitar 15 karakter
nameField.setPreferredSize(new Dimension(400, 35)); // Lebar 200px, tinggi 25px
nameField.setMaximumSize(new Dimension(400, 35)); // Pastikan ukurannya tidak berubah
inputPanel.add(nameLabel);
inputPanel.add(nameField);


        // Input Bahan-Bahan
        inputPanel.add(new JLabel("Bahan-Bahan:"));
        ingredientField = createTextArea();
        inputPanel.add(new JScrollPane(ingredientField));
        inputPanel.add(Box.createVerticalStrut(10));

        // Input Langkah-Langkah
        inputPanel.add(new JLabel("Langkah-Langkah:"));
        stepsField = createTextArea();
        inputPanel.add(new JScrollPane(stepsField));
        inputPanel.add(Box.createVerticalStrut(10));

        // Tombol Simpan Resep
        JButton saveRecipeButton = new JButton("Simpan Resep");
        saveRecipeButton.addActionListener(e -> addRecipe());
        inputPanel.add(saveRecipeButton);

        return inputPanel;
    }

    private JPanel createListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());

        // List Daftar Resep
        recipeList = new JList<>(recipeModel);
        recipeList.addListSelectionListener(e -> showRecipePreview());
        listPanel.add(new JScrollPane(recipeList), BorderLayout.CENTER);

        // Preview Resep
        JPanel previewPanel = new JPanel(new BorderLayout());
        recipePreview = createTextArea();
        recipePreview.setEditable(false);
        previewPanel.add(new JLabel("Detail Resep"), BorderLayout.NORTH);
        previewPanel.add(new JScrollPane(recipePreview), BorderLayout.CENTER);

        listPanel.add(previewPanel, BorderLayout.EAST);
        return listPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton editButton = new JButton("Ubah Resep");
        editButton.addActionListener(e -> editRecipe());
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Hapus Resep");
        deleteButton.addActionListener(e -> deleteRecipe());
        buttonPanel.add(deleteButton);

        JButton printButton = new JButton("Cetak Resep");
        printButton.addActionListener(e -> printRecipe());
        buttonPanel.add(printButton);

        return buttonPanel;
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    private void addRecipe() {
        String name = nameField.getText();
        String steps = stepsField.getText();

        if (name.isEmpty() || steps.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nama dan langkah-langkah harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Recipe recipe = new Recipe(name, steps);
        recipes.add(recipe);
        recipeModel.addElement(name);

        clearFields();
    }

    private void editRecipe() {
        int selectedIndex = recipeList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih resep untuk diubah!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Recipe recipe = recipes.get(selectedIndex);
        recipe.setName(nameField.getText());
        recipe.setSteps(stepsField.getText());
        recipeModel.set(selectedIndex, recipe.getName());
        clearFields();
    }

    private void deleteRecipe() {
        int selectedIndex = recipeList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih resep untuk dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        recipes.remove(selectedIndex);
        recipeModel.remove(selectedIndex);
        recipePreview.setText("");
    }

    private void printRecipe() {
        int selectedIndex = recipeList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih resep untuk dicetak!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Recipe recipe = recipes.get(selectedIndex);
        JOptionPane.showMessageDialog(frame, recipe.toString(), "Cetak Resep", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRecipePreview() {
        int selectedIndex = recipeList.getSelectedIndex();
        if (selectedIndex != -1) {
            Recipe recipe = recipes.get(selectedIndex);
            recipePreview.setText(recipe.toString());
        } else {
            recipePreview.setText("");
        }
    }

    private void clearFields() {
        nameField.setText("");
        stepsField.setText("");
    }

    public static void main(String[] args) {
        new RecipeApp();
    }
}

class Recipe {
    private String name, steps;

    public Recipe(String name, String steps) {
        this.name = name;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Nama Resep: " + name + "\nLangkah-Langkah:\n" + steps;
    }
}
