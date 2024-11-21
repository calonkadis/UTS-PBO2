import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RecipeApp {
    private JFrame frame;
    private JTextField nameField, ingredientField, stepsField;
    private JList<String> recipeList, ingredientList;
    private DefaultListModel<String> recipeModel, ingredientModel;
    private ArrayList<Recipe> recipes;
    private JTextArea recipePreview;

    public RecipeApp() {
        recipes = new ArrayList<>();
        recipeModel = new DefaultListModel<>();
        ingredientModel = new DefaultListModel<>();

        frame = new JFrame("Aplikasi Resep Makanan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // TabbedPane untuk navigasi
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab Input Resep
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Nama Resep:");
        nameField = new JTextField();
        JLabel ingredientLabel = new JLabel("Bahan-Bahan:");
        ingredientField = new JTextField();
        JButton addIngredientButton = new JButton("Tambah Bahan");
        JLabel stepsLabel = new JLabel("Langkah-Langkah:");
        stepsField = new JTextField();
        JButton saveRecipeButton = new JButton("Simpan Resep");

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(Box.createVerticalStrut(10)); // Spacer
        inputPanel.add(ingredientLabel);
        inputPanel.add(ingredientField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(addIngredientButton);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(stepsLabel);
        inputPanel.add(stepsField);
        inputPanel.add(Box.createVerticalStrut(20));
        inputPanel.add(saveRecipeButton);

        tabbedPane.addTab("Input Resep", inputPanel);

        // Tab Daftar Resep
        JPanel listPanel = new JPanel(new BorderLayout());
        recipeList = new JList<>(recipeModel);
        listPanel.add(new JScrollPane(recipeList), BorderLayout.CENTER);

        JPanel previewPanel = new JPanel(new BorderLayout());
        recipePreview = new JTextArea();
        recipePreview.setEditable(false);
        recipePreview.setLineWrap(true);
        recipePreview.setWrapStyleWord(true);
        previewPanel.add(new JLabel("Detail Resep"), BorderLayout.NORTH);
        previewPanel.add(new JScrollPane(recipePreview), BorderLayout.CENTER);

        listPanel.add(previewPanel, BorderLayout.EAST);
        tabbedPane.addTab("Daftar Resep", listPanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editButton = new JButton("Ubah Resep");
        JButton deleteButton = new JButton("Hapus Resep");
        JButton printButton = new JButton("Cetak Resep");

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(printButton);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        addIngredientButton.addActionListener(e -> addIngredient());
        saveRecipeButton.addActionListener(e -> addRecipe());
        editButton.addActionListener(e -> editRecipe());
        deleteButton.addActionListener(e -> deleteRecipe());
        printButton.addActionListener(e -> printRecipe());
        recipeList.addListSelectionListener(e -> showRecipePreview());

        frame.setVisible(true);
    }

    private void addIngredient() {
        String ingredient = ingredientField.getText();
        if (ingredient.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Masukkan nama bahan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ingredientModel.addElement(ingredient);
        ingredientField.setText("");
    }

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
        recipes.add(recipe);
        recipeModel.addElement(name);
        ingredientModel.clear();
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

        ArrayList<String> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientModel.getSize(); i++) {
            ingredients.add(ingredientModel.getElementAt(i));
        }
        recipe.setIngredients(ingredients);

        recipeModel.set(selectedIndex, recipe.getName());
        ingredientModel.clear();
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
        StringBuilder recipeDetails = new StringBuilder();
        recipeDetails.append("Nama Resep: ").append(recipe.getName()).append("\n");
        recipeDetails.append("Bahan-Bahan:\n");
        for (String ingredient : recipe.getIngredients()) {
            recipeDetails.append("- ").append(ingredient).append("\n");
        }
        recipeDetails.append("Langkah-Langkah: ").append(recipe.getSteps());

        JOptionPane.showMessageDialog(frame, recipeDetails.toString(), "Cetak Resep", JOptionPane.INFORMATION_MESSAGE);
    }

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

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}
