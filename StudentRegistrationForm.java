import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentRegistrationForm extends JFrame implements ActionListener {

    JTextField nameField, ageField, emailField;
    JComboBox<String> courseBox;
    JButton addButton, updateButton, deleteButton, refreshButton;
    JTable table;
    DefaultTableModel model;
    JLabel messageLabel;

    static final String DB_URL = "jdbc:mysql://localhost:3306/studentdb";
    static final String USER = "root";
    static final String PASS = "your_password";

    public StudentRegistrationForm() {
        setTitle("Student Registration Form");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Form"));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        formPanel.add(ageField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Course:"));
        String[] courses = {
            "1 - Mathematics",
            "2 - Computer Science",
            "3 - Physics",
            "4 - Chemistry",
            "5 - English Literature"
        };
        courseBox = new JComboBox<>(courses);
        formPanel.add(courseBox);

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.BLUE);
        formPanel.add(messageLabel);

        add(formPanel, BorderLayout.NORTH);

        // Table for displaying students
        model = new DefaultTableModel(new String[]{"Name", "Age", "Email", "Course"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        refreshButton.addActionListener(this);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();
                nameField.setText(model.getValueAt(i, 0).toString());
                ageField.setText(model.getValueAt(i, 1).toString());
                emailField.setText(model.getValueAt(i, 2).toString());
                courseBox.setSelectedItem(model.getValueAt(i, 3).toString());
            }
        });

        loadData();
        setVisible(true);
    }

    // Load data into table
    void loadData() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, age, email, course FROM students")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("email"),
                    rs.getString("course")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    // Validate inputs
    boolean validateInputs() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return false;
        }
        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age!");
            return false;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!");
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            if (!validateInputs()) return;
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "INSERT INTO students (name, age, email, course) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nameField.getText().trim());
                ps.setInt(2, Integer.parseInt(ageField.getText().trim()));
                ps.setString(3, emailField.getText().trim());
                ps.setString(4, courseBox.getSelectedItem().toString());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                loadData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }

        } else if (e.getSource() == updateButton) {
            int i = table.getSelectedRow();
            if (i == -1) {
                JOptionPane.showMessageDialog(this, "Select a record to update!");
                return;
            }
            if (!validateInputs()) return;
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "UPDATE students SET name=?, age=?, email=?, course=? WHERE email=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nameField.getText().trim());
                ps.setInt(2, Integer.parseInt(ageField.getText().trim()));
                ps.setString(3, emailField.getText().trim());
                ps.setString(4, courseBox.getSelectedItem().toString());
                ps.setString(5, model.getValueAt(i, 2).toString());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record updated successfully!");
                loadData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }

        } else if (e.getSource() == deleteButton) {
            int i = table.getSelectedRow();
            if (i == -1) {
                JOptionPane.showMessageDialog(this, "Select a record to delete!");
                return;
            }
            String email = model.getValueAt(i, 2).toString();
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "DELETE FROM students WHERE email=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                loadData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }

        } else if (e.getSource() == refreshButton) {
            loadData();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRegistrationForm::new);
    }
}
