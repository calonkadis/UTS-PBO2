import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.print.*;

public class RecipeApp {
    private JFrame frame;
    private JTextField nameField;
    private JTextArea ingredientField, stepsField, recipePreview;
    private JList<String> recipeList;
    private DefaultListModel<String> recipeModel;
    private ArrayList<Recipe> recipes;

    public RecipeApp() {
        // Inisialisasi struktur data
        recipes = new ArrayList<>();
        recipeModel = new DefaultListModel<>();

        // Setup frame utama
        frame = new JFrame("Aplikasi Resep Makanan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Membuat TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Membuat panel untuk input resep dan daftar resep
        JPanel inputPanel = createInputPanel();
        JPanel listPanel = createListPanel();

        // Menambahkan panel ke tabbedPane
        tabbedPane.addTab("Input Resep", inputPanel);
        tabbedPane.addTab("Daftar Resep", listPanel);

        // Membuat panel tombol aksi
        JPanel buttonPanel = createButtonPanel();

        // Menambahkan tabbedPane dan panel tombol ke frame
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Menampilkan frame
        frame.setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input Nama Resep
        JLabel nameLabel = new JLabel("Nama Resep:");
        nameField = new JTextField(30);
        nameField.setPreferredSize(new Dimension(400, 35));
        nameField.setMaximumSize(new Dimension(400, 35));
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);

        // Input Bahan-Bahan
        inputPanel.add(new JLabel("Bahan-Bahan:"));
        ingredientField = createTextArea();
        inputPanel.add(new JScrollPane(ingredientField));

        // Input Langkah-Langkah
        inputPanel.add(new JLabel("Langkah-Langkah:"));
        stepsField = createTextArea();
        inputPanel.add(new JScrollPane(stepsField));

        // Tombol Simpan Resep
        JButton saveRecipeButton = new JButton("Simpan Resep");
        saveRecipeButton.addActionListener(e -> addRecipe());
        inputPanel.add(saveRecipeButton);

        return inputPanel;
    }

    private JPanel createListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());

        // Daftar Resep
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

        // Tombol Edit
        JButton editButton = new JButton("Ubah Resep");
        editButton.addActionListener(e -> editRecipe());
        buttonPanel.add(editButton);

        // Tombol Hapus
        JButton deleteButton = new JButton("Hapus Resep");
        deleteButton.addActionListener(e -> deleteRecipe());
        buttonPanel.add(deleteButton);

        // Tombol Print
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
        String ingredients = ingredientField.getText();
        String steps = stepsField.getText();

        // Memeriksa jika ada kolom input yang kosong
        if (name.isEmpty() || ingredients.isEmpty() || steps.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nama, bahan-bahan, dan langkah-langkah harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Menambahkan resep ke dalam daftar
        Recipe recipe = new Recipe(name, ingredients, steps);
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
        nameField.setText(recipe.getName());
        ingredientField.setText(recipe.getIngredients());
        stepsField.setText(recipe.getSteps());

        int option = JOptionPane.showOptionDialog(frame, new Object[]{
            new JLabel("Nama Resep:"),
            nameField,
            new JLabel("Bahan-Bahan:"),
            ingredientField,
            new JLabel("Langkah-Langkah:"),
            stepsField
        }, "Simpan Perubahan Resep", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String ingredients = ingredientField.getText();
            String steps = stepsField.getText();

            if (name.isEmpty() || ingredients.isEmpty() || steps.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nama, bahan-bahan, dan langkah-langkah harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            recipe.setName(name);
            recipe.setIngredients(ingredients);
            recipe.setSteps(steps);
            recipeModel.set(selectedIndex, recipe.getName());
            clearFields();
        }
    }

    private void deleteRecipe() {
        int selectedIndex = recipeList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih resep untuk dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Menghapus resep dari daftar
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
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        if (printerJob.printDialog()) {
            printerJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
                    if (pageIndex > 0) {
                        return Printable.NO_SUCH_PAGE;
                    }

                    String printText = recipe.toString();
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                    g2d.setFont(new Font("Serif", Font.PLAIN, 12));
                    g2d.drawString(printText, 100, 100);

                    return Printable.PAGE_EXISTS;
                }
            });

            try {
                printerJob.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(frame, "Gagal mencetak: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
        ingredientField.setText("");
        stepsField.setText("");
    }

    public static void main(String[] args) {
        new RecipeApp();
    }
}

class Recipe {
    private String name, steps, ingredients;

    public Recipe(String name, String ingredients, String steps) {
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Nama: " + name + "\nBahan-Bahan: " + ingredients + "\nLangkah-Langkah: " + steps;
    }
}
