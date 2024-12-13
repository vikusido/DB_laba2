import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.IOException;
import java.util.List;


public class DatabaseGUI extends JFrame {
    private final Database database;
    private final JTable table;
    private final JTable searchResultsTable;
    private JTextField idField, nameField, companyField, emailField;

    public DatabaseGUI(String filename) {
        database = new Database(filename);

        setTitle("Simple File-Based Database");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout()); //arrange in 5 regions - north, south, east, west, center

        table = new JTable(new NonEditableTableModel(new Object[][]{}, new String[]{"ID", "Name", "Company", "Email address"}));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        updateTable();

        JPanel addPanel = createAddPanel();
        add(addPanel, BorderLayout.SOUTH);

        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);

        searchResultsTable = new JTable(new NonEditableTableModel(new Object[][]{}, new String[]{"ID", "Name", "Company", "Email address"}));
        JScrollPane searchScrollPane = new JScrollPane(searchResultsTable);
        add(searchScrollPane, BorderLayout.EAST);

        setVisible(true);
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Id field
        JLabel idLabel = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        idLabel.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(idLabel, gbc);

        idField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        idField.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(idField, gbc);

        // Name field
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        nameLabel.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        nameField.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(nameField, gbc);

        // Company field
        JLabel companyLabel = new JLabel("Company:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        companyLabel.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(companyLabel, gbc);

        companyField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        companyField.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(companyField, gbc);

        // Email field
        JLabel emailLabel = new JLabel("Email address:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        emailLabel.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        emailField.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(emailField, gbc);


        JButton addButton = new JButton("Add Record");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRecord();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(addButton, gbc);

        JButton saveButton = new JButton("Save File");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    database.save();
                    JOptionPane.showMessageDialog(DatabaseGUI.this, "Database saved successfully to mydatabase.csv!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(DatabaseGUI.this, "Error saving database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        saveButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(saveButton, gbc);

        JButton deleteByIdButton = new JButton("Delete Record by ID");
        deleteByIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteById();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        deleteByIdButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(deleteByIdButton, gbc);

        JButton deleteByValueButton = new JButton("Delete Record by Value");
        deleteByValueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteByValue();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        deleteByValueButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(deleteByValueButton, gbc);

        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        clearButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(clearButton, gbc);

        JButton clearDatabaseButton = new JButton("Clear Database");
        clearDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(DatabaseGUI.this, "Are you sure you want to clear the database?", "Confirm Clear", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    database.clear();
                    updateTable();
                }
                JOptionPane.showMessageDialog(DatabaseGUI.this, "Press (save to file) to save changes");
            }
        });
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        clearDatabaseButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(clearDatabaseButton, gbc);

        JButton editRecordsButton = new JButton("Edit Record");
        editRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(DatabaseGUI.this, "Please select a record to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = (int) table.getValueAt(table.getSelectedRow(), 0);
                Record record = database.getRecordById(id);
                idField.setText(String.valueOf(record.id));
                nameField.setText(record.name);
                companyField.setText(record.company);
                emailField.setText(record.email);

                JButton saveChangesButton = new JButton("Save Changes");
                saveChangesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int id = Integer.parseInt(idField.getText());
                            String name = nameField.getText();
                            String company = companyField.getText();
                            String email = emailField.getText();
                            Record updatedRecord = new Record(id, name, company, email);
                            if (database.editRecordByKey(id, updatedRecord)) {
                                updateTable();
                                updateRowInTable(updatedRecord);
                                clearFields();
                                panel.remove(saveChangesButton);
                                panel.revalidate(); //update
                                panel.repaint();
                            } else {
                                JOptionPane.showMessageDialog(DatabaseGUI.this, "Error: Record not found.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(DatabaseGUI.this, "Invalid input. Please enter a valid number for ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                gbc.gridx = 4;
                gbc.gridy = 3;
                gbc.gridwidth = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                saveChangesButton.setFont(new Font("Serif", Font.BOLD, 15));
                panel.add(saveChangesButton, gbc);
                panel.revalidate();
                panel.repaint();
            }
        });
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editRecordsButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(editRecordsButton, gbc);

        JButton backupButton = new JButton("Backup");
        backupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                int returnValue = fileChooser.showSaveDialog(DatabaseGUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String backupFilename = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        database.backup(backupFilename);
                        JOptionPane.showMessageDialog(DatabaseGUI.this, "Backup created successfully.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(DatabaseGUI.this, "Error creating backup: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backupButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(backupButton, gbc);

        JButton restoreButton = new JButton("Restore");
        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                int returnValue = fileChooser.showOpenDialog(DatabaseGUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String backupFilename = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        database.restore(backupFilename);
                        updateTable();
                        JOptionPane.showMessageDialog(DatabaseGUI.this, "Database restored successfully.");
                    } catch (IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(DatabaseGUI.this, "Error restoring database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        restoreButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(restoreButton, gbc);

        JButton importCSVButton = new JButton("Import CSV");
        importCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                int returnValue = fileChooser.showOpenDialog(DatabaseGUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        database.importCSV(filename);
                        updateTable();
                        JOptionPane.showMessageDialog(DatabaseGUI.this, "CSV file imported successfully.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(DatabaseGUI.this, "Error importing CSV file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        importCSVButton.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(importCSVButton, gbc);
        return panel;
    }


    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel fieldLabel = new JLabel("Field:");
        String[] fields = {"ID", "Name", "Company", "Email address"};
        JComboBox<String> fieldComboBox = new JComboBox<>(fields);
        fieldComboBox.setFont(new Font("Serif", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldLabel.setFont(new Font("Serif", Font.BOLD, 15));
        inputPanel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(fieldComboBox, gbc);

        JLabel valueLabel = new JLabel("Value:");
        JTextField valueField = new JTextField(15);
        valueField.setFont(new Font("Serif", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 1;
        valueLabel.setFont(new Font("Serif", Font.BOLD, 15));
        inputPanel.add(valueLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(valueField, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (database.getRecords().isEmpty()) {
                    JOptionPane.showMessageDialog(DatabaseGUI.this, "The database is empty. Please add records first.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String fieldName = (String) fieldComboBox.getSelectedItem();
                    String value = valueField.getText();
                    assert fieldName != null;
                    List<Record> results = database.searchByValue(fieldName, value);
                    updateSearchResultsTable(results);
                    if (results.isEmpty()) {
                        JOptionPane.showMessageDialog(DatabaseGUI.this, "No matching records found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        searchButton.setFont(new Font("Serif", Font.BOLD, 15));
        searchButton.setPreferredSize(new Dimension(100, searchButton.getPreferredSize().height));
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(searchButton, gbc);
        panel.add(buttonPanel);
        return panel;
    }

    // different methods
    private boolean addRecord() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String company = companyField.getText();
            String email = emailField.getText();

            Record existingRecord = database.getRecordById(id);
            if (existingRecord != null) {
                JOptionPane.showMessageDialog(this, "Error: Duplicate ID. Use a unique one.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Record record = new Record(id, name, company, email);
            database.addRecord(record);
            updateTable();
            clearFields();
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid values.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the table
        for (Record record : database.getRecords()) {
            model.addRow(new Object[]{record.id, record.name, record.company, record.email});
        }

    }

    private void deleteById() {
        try {
            int id = Integer.parseInt(idField.getText());
            if (database.deleteRecordById(id)) {
                updateTable();
                clearFields();
                removeRowFromTable(id);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Record not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number for ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeRowFromTable(int id) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (id == Integer.parseInt(model.getValueAt(i, 0).toString())) {
                model.removeRow(i);
                break;
            }
        }
    }

    private void deleteByValue() {
        String fieldName = (String) JOptionPane.showInputDialog(
                this,
                "Select the field to delete by:",
                "Delete by Value",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Name", "Company", "Email address"},
                "Name");
        if (fieldName != null) {
            String value = JOptionPane.showInputDialog(this, "Enter the value to delete:");
            if (value != null && !value.isEmpty()) {
                int count = database.deleteRecordByValue(value, fieldName.toLowerCase());
                updateTable();
                clearFields();
                JOptionPane.showMessageDialog(this, count + " record(s) deleted successfully.");
            }
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        companyField.setText("");
        emailField.setText("");
    }

    private void updateRowInTable(Record updatedRecord) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (updatedRecord.getId() == Integer.parseInt(model.getValueAt(i, 0).toString())) {
                model.setValueAt(updatedRecord.getId(), i, 0);
                model.setValueAt(updatedRecord.getName(), i, 1);
                model.setValueAt(updatedRecord.getCompany(), i, 2);
                model.setValueAt(updatedRecord.getEmail(), i, 3);
                break;
            }
        }
    }

    private void updateSearchResultsTable(List<Record> results) {
        DefaultTableModel model = (DefaultTableModel) searchResultsTable.getModel();
        model.setRowCount(0);
        for (Record record : results) {
            model.addRow(new Object[]{record.id, record.name, record.company, record.email});
        }
    }

    public static void main(String[] args) {
        new DatabaseGUI("mydatabase.csv");
    }
}