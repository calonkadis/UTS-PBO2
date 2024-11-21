import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AddressBookApp {
    private JFrame frame;
    private JTextField nameField, phoneField, emailField;
    private JList<String> contactList;
    private DefaultListModel<String> listModel;
    private ArrayList<Contact> contacts;

    public AddressBookApp() {
        contacts = new ArrayList<>();
        listModel = new DefaultListModel<>();

        frame = new JFrame("Aplikasi Buku Alamat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Nama:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Telepon:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        frame.add(inputPanel, BorderLayout.NORTH);

        // List Panel
        contactList = new JList<>(listModel);
        frame.add(new JScrollPane(contactList), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Tambah");
        JButton editButton = new JButton("Ubah");
        JButton deleteButton = new JButton("Hapus");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        addButton.addActionListener(e -> addContact());
        editButton.addActionListener(e -> editContact());
        deleteButton.addActionListener(e -> deleteContact());

        frame.setVisible(true);
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua bidang harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contact = new Contact(name, phone, email);
        contacts.add(contact);
        listModel.addElement(name);
        clearFields();
    }

    private void editContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih kontak untuk diubah!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contact = contacts.get(selectedIndex);
        contact.setName(nameField.getText());
        contact.setPhone(phoneField.getText());
        contact.setEmail(emailField.getText());
        listModel.set(selectedIndex, contact.getName());
        clearFields();
    }

    private void deleteContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih kontak untuk dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        contacts.remove(selectedIndex);
        listModel.remove(selectedIndex);
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        new AddressBookApp();
    }
}

class Contact {
    private String name, phone, email;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
