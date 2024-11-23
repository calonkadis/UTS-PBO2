import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RecipeApp {
    private JFrame frame; // Frame utama aplikasi
    private JTextField nameField, ingredientField, stepsField; // Input field untuk nama resep, bahan, dan langkah-langkah
    private JList<String> recipeList, ingredientList; // List untuk menampilkan daftar resep dan bahan
    private DefaultListModel<String> recipeModel, ingredientModel; // Model untuk data dalam list
    private ArrayList<Recipe> recipes; // List untuk menyimpan semua resep
    private JTextArea recipePreview; // Area untuk menampilkan detail resep yang dipilih

    public RecipeApp() {
        // Inisialisasi data dan model
        recipes = new ArrayList<>();
        recipeModel = new DefaultListModel<>();
        ingredientModel = new DefaultListModel<>();

        // Setup frame utama
        frame = new JFrame("Aplikasi Resep Makanan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Ukuran frame
        frame.setLayout(new BorderLayout()); // Layout utama menggunakan BorderLayout

        // Membuat TabbedPane untuk navigasi
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel untuk input resep
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); // Menggunakan BoxLayout
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Komponen input untuk nama resep
        JLabel nameLabel = new JLabel("Nama Resep:");
        nameField = new JTextField();
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);

        inputPanel.add(Box.createVerticalStrut(10)); // Spacer

        // Komponen input untuk bahan-bahan
        JLabel ingredientLabel = new JLabel("Bahan-Bahan:");
        ingredientField = new JTextField();
        JButton addIngredientButton = new JButton("Tambah Bahan");
        inputPanel.add(ingredientLabel);
        inputPanel.add(ingredientField);
        inputPanel.add(Box.createVerticalStrut(10)); // Spacer
        inputPanel.add(addIngredientButton);

        inputPanel.add(Box.createVerticalStrut(10));

        // Komponen input untuk langkah-langkah
        JLabel stepsLabel = new JLabel("Langkah-Langkah:");
        stepsField = new JTextField();
        inputPanel.add(stepsLabel);
        inputPanel.add(stepsField);

        inputPanel.add(Box.createVerticalStrut(20));

        // Tombol untuk menyimpan resep
        JButton saveRecipeButton = new JButton("Simpan Resep");
        inputPanel.add(saveRecipeButton);

        // Tambahkan panel input ke tabbedPane
        tabbedPane.addTab("Input Resep", inputPanel);

        // Panel untuk daftar resep
        JPanel listPanel = new JPanel(new BorderLayout());
        recipeList = new JList<>(recipeModel); // List untuk daftar resep
        listPanel.add(new JScrollPane(recipeList), BorderLayout.CENTER);

        // Panel untuk preview resep
        JPanel previewPanel = new JPanel(new BorderLayout());
        recipePreview = new JTextArea(); // Area teks untuk menampilkan detail resep
        recipePreview.setEditable(false);
        recipePreview.setLineWrap(true); // Membungkus teks secara otomatis
        recipePreview.setWrapStyleWord(true);
        previewPanel.add(new JLabel("Detail Resep"), BorderLayout.NORTH);
        previewPanel.add(new JScrollPane(recipePreview), BorderLayout.CENTER);

        listPanel.add(previewPanel, BorderLayout.EAST);

        // Tambahkan panel daftar resep ke tabbedPane
        tabbedPane.addTab("Daftar Resep", listPanel);

        // Panel untuk tombol aksi
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editButton = new JButton("Ubah Resep");
        JButton deleteButton = new JButton("Hapus Resep");
        JButton printButton = new JButton("Cetak Resep");

        // Tambahkan tombol ke panel
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(printButton);

        // Tambahkan tabbedPane dan panel tombol ke frame
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners untuk tombol
        addIngredientButton.addActionListener(e -> addIngredient()); // Tambah bahan
        saveRecipeButton.addActionListener(e -> addRecipe()); // Simpan resep
        editButton.addActionListener(e -> editRecipe()); // Ubah resep
        deleteButton.addActionListener(e -> deleteRecipe()); // Hapus resep
        printButton.addActionListener(e -> printRecipe()); // Cetak resep
        recipeList.addListSelectionListener(e -> showRecipePreview()); // Tampilkan detail saat resep dipilih

        frame.setVisible(true); // Tampilkan frame
    }

    // Menambahkan bahan ke daftar
    private void addIngredient() {
        String ingredient = ingredientField.getText();
        if (ingredient.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Masukkan nama bahan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ingredientModel.addElement(ingredient); // Tambahkan bahan ke model
        ingredientField.setText(""); // Kosongkan input field
    }

    // Menambahkan resep ke daftar
    private void addRecipe() {
        String name = nameField.getText();
        String steps = stepsField.getText();

        if (name.isEmpty() || steps.isEmpty() || ingredientModel.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua bidang harus diisi dan bahan harus ditambahkan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<String> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientModel.getSize(); i++) {
            ingredients.add(ingredientModel.getElementAt(i));
        }

        Recipe recipe = new Recipe(name, ingredients, steps);
        recipes.add(recipe); // Tambahkan resep ke list
        recipeModel.addElement(name); // Tambahkan nama resep ke model
        ingredientModel.clear(); // Kosongkan daftar bahan
        clearFields(); // Reset input field
    }

    // Mengedit resep yang dipilih
    private void editRecipe() {
        int selectedIndex = recipeList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih resep untuk diubah!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Recipe recipe = recipes.get(selectedIndex);
        recipe.setName(nameField.getText());
        recipe.setSteps(stepsField.getText());

        ArrayList<String> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientModel.getSize(); i++) {
            ingredients.add(ingredientModel.getElementAt(i));
        }
        recipe.setIngredients(ingredients);

        recipeModel.set(selectedIndex, recipe.getName()); // Perbarui nama resep di model
        ingredientModel.clear();
        clearFields();
    }

    // Menghapus resep yang dipilih
    private void deleteRecipe() {
        int selectedIndex = recipeList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih resep untuk dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        recipes.remove(selectedIndex); // Hapus resep dari list
        recipeModel.remove(selectedIndex); // Hapus nama resep dari model
        recipePreview.setText(""); // Kosongkan preview
    }

    // Mencetak detail resep yang dipilih
    private void printRecipe() {
        int selectedIndex = recipeList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih resep untuk dicetak!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Recipe recipe = recipes.get(selectedIndex);
        StringBuilder recipeDetails = new StringBuilder();
        recipeDetails.append("Nama Resep: ").append(recipe.getName()).append("\n");
        recipeDetails.append("Bahan-Bahan:\n");
        for (String ingredient : recipe.getIngredients()) {
            recipeDetails.append("- ").append(ingredient).append("\n");
        }
        recipeDetails.append("Langkah-Langkah: ").append(recipe.getSteps());

        JOptionPane.showMessageDialog(frame, recipeDetails.toString(), "Cetak Resep", JOptionPane.INFORMATION_MESSAGE);
    }

    // Menampilkan preview resep yang dipilih
    private void showRecipePreview() {
        int selectedIndex = recipeList.getSelectedIndex();
        if (selectedIndex != -1) {
            Recipe recipe = recipes.get(selectedIndex);
            StringBuilder details = new StringBuilder();
            details.append("Nama: ").append(recipe.getName()).append("\n");
            details.append("Bahan:\n");
            for (String ingredient : recipe.getIngredients()) {
                details.append("- ").append(ingredient).append("\n");
            }
            details.append("Langkah: ").append(recipe.getSteps());
            recipePreview.setText(details.toString());
        } else {
            recipePreview.setText("");
        }
    }

    // Membersihkan input field
    private void clearFields() {
        nameField.setText("");
        stepsField.setText("");
    }

    public static void main(String[] args) {
        new RecipeApp(); // Jalankan aplikasi
    }
}

// Class untuk menyimpan data resep
class Recipe {
    private String name, steps;
    private ArrayList<String> ingredients;

    public Recipe(String name, ArrayList<String> ingredients, String steps) {
        this.name = name;
        this.ingredients = ingredients;
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

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
