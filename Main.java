package databaseGui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, nimField, lineIdField, programField, colorField;
    private JButton addButton, refreshButton, deleteButton, updateButton;

    public Main() {
        setTitle("Daftar Biodata Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(106, 90, 205)); // Menambahkan warna latar belakang biru dan ungu

        tableModel = new DefaultTableModel(new String[]{"NO", "NIM", "Name", "ID Line", "Program Studi", "Warna Favorit"}, 0);
        table = new JTable(tableModel);

        // Mengatur warna sel menjadi biru muda
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(173, 216, 230)); // Warna biru muda untuk sel
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 740, 300);
        add(scrollPane);

        Color labelColor = new Color(123, 104, 238); // Warna ungu untuk label

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(20, 340, 100, 25);
        nameLabel.setForeground(new Color(255, 216, 230));
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(120, 340, 200, 25);
        nameField.setBackground(new Color(173, 216, 230)); // Warna biru muda untuk field
        add(nameField);

        JLabel nimLabel = new JLabel("NIM");
        nimLabel.setBounds(20, 380, 100, 25);
        nimLabel.setForeground(new Color(255, 216, 230));
        add(nimLabel);

        nimField = new JTextField();
        nimField.setBounds(120, 380, 200, 25);
        nimField.setBackground(new Color(173, 216, 230)); // Warna biru muda untuk field
        add(nimField);

        JLabel lineIdLabel = new JLabel("ID Line");
        lineIdLabel.setBounds(20, 420, 100, 25);
        lineIdLabel.setForeground(new Color(255, 216, 230));
        add(lineIdLabel);

        lineIdField = new JTextField();
        lineIdField.setBounds(120, 420, 200, 25);
        lineIdField.setBackground(new Color(173, 216, 230)); // Warna biru muda untuk field
        add(lineIdField);

        JLabel programLabel = new JLabel("Program Studi");
        programLabel.setBounds(20, 460, 100, 25);
        programLabel.setForeground(new Color(255, 216, 230));
        add(programLabel);

        programField = new JTextField();
        programField.setBounds(120, 460, 200, 25);
        programField.setBackground(new Color(173, 216, 230)); // Warna biru muda untuk field
        add(programField);

        JLabel colorLabel = new JLabel("Warna Favorit");
        colorLabel.setBounds(20, 500, 100, 25);
        colorLabel.setForeground(new Color(255, 216, 230));
        add(colorLabel);

        colorField = new JTextField();
        colorField.setBounds(120, 500, 200, 25);
        colorField.setBackground(new Color(173, 216, 230)); // Warna biru muda untuk field
        add(colorField);

        addButton = new JButton("Add");
        addButton.setBounds(350, 340, 100, 25);
        addButton.setBackground(new Color(123, 104, 238)); // Warna ungu untuk tombol
        addButton.setForeground(Color.WHITE);
        add(addButton);

        refreshButton = new JButton("Refresh");
        refreshButton.setBounds(350, 380, 100, 25);
        refreshButton.setBackground(new Color(123, 104, 238)); // Warna ungu untuk tombol
        refreshButton.setForeground(Color.WHITE);
        add(refreshButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(350, 420, 100, 25);
        deleteButton.setBackground(new Color(123, 104, 238)); // Warna ungu untuk tombol
        deleteButton.setForeground(Color.WHITE);
        add(deleteButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(350, 460, 100, 25);
        updateButton.setBackground(new Color(123, 104, 238)); // Warna ungu untuk tombol
        updateButton.setForeground(Color.WHITE);
        add(updateButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addData();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();
            }
        });

        loadData();
    }

    private void loadData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users"; // Ganti dengan nama tabel Anda
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            tableModel.setRowCount(0);  // Clear the table

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nim = resultSet.getString("nim");
                String name = resultSet.getString("name");
                String lineId = resultSet.getString("line_id");
                String program = resultSet.getString("program");
                String color = resultSet.getString("color");
                tableModel.addRow(new Object[]{id, nim, name, lineId, program, color});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addData() {
        String name = nameField.getText();
        String nim = nimField.getText();
        String lineId = lineIdField.getText();
        String program = programField.getText();
        String color = colorField.getText();

        if (name.isEmpty() || nim.isEmpty() || lineId.isEmpty() || program.isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to add this data?", "Confirm Add", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (nim, name, line_id, program, color) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nim);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, lineId);
            preparedStatement.setString(4, program);
            preparedStatement.setString(5, color);
            preparedStatement.executeUpdate();
            loadData();  // Refresh the table data
            nameField.setText("");
            nimField.setText("");
            lineIdField.setText("");
            programField.setText("");
            colorField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this data?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            loadData();  // Refresh the table data
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String nim = nimField.getText();
        String lineId = lineIdField.getText();
        String program = programField.getText();
        String color = colorField.getText();

        if (name.isEmpty() || nim.isEmpty() || lineId.isEmpty() || program.isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this data?", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE users SET nim = ?, name = ?, line_id = ?, program = ?, color = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nim);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, lineId);
            preparedStatement.setString(4, program);
            preparedStatement.setString(5, color);
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
            loadData();  // Refresh the table data
            nameField.setText("");
            nimField.setText("");
            lineIdField.setText("");
            programField.setText("");
            colorField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
