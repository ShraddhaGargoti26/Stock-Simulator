package myproject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryGUI extends JFrame {
    private InventoryManager inventoryManager;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField productNameField;
    private JTextField quantityField;
    private JTextField newProductNameField;
    private JTextField newProductStockField;
    private JLabel statusLabel;

    public InventoryGUI() {
        inventoryManager = new InventoryManager();
        inventoryManager.addProduct(new Product("Eggs", 100));
        inventoryManager.addProduct(new Product("Meat", 50));

        setTitle("Stock Simulator");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Table setup
        String[] columnNames = {"Product", "Stock", "Sold", "Remaining"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        productTable.setFont(new Font("Arial", Font.BOLD, 16)); // Set font size for table headers and rows
        productTable.setRowHeight(25); // Set row height for data

        // Set font size for table headers
        JTableHeader header = productTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));

        // Center-align table data
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        productTable.setDefaultRenderer(Object.class, centerRenderer);

        updateTable();
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Control panel setup
        JPanel controlPanel = new JPanel(new BorderLayout());

        // Panel for Sell and Restock
        JPanel sellRestockPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        sellRestockPanel.add(new JLabel("Product:"), gbc);

        productNameField = new JTextField(10);
        gbc.gridx = 1;
        sellRestockPanel.add(productNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        sellRestockPanel.add(new JLabel("Quantity:"), gbc);

        quantityField = new JTextField(10);
        gbc.gridx = 1;
        sellRestockPanel.add(quantityField, gbc);

        JButton sellButton = new JButton("Sell");
        styleButton(sellButton, Color.RED);
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellProduct();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        sellRestockPanel.add(sellButton, gbc);

        JButton restockButton = new JButton("Restock");
        styleButton(restockButton, Color.GREEN);
        restockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restockProduct();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 1;
        sellRestockPanel.add(restockButton, gbc);

        // Panel for Add Product
        JPanel addProductPanel = new JPanel(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        addProductPanel.add(new JLabel("New Product Name:"), gbc);

        newProductNameField = new JTextField(10);
        gbc.gridx = 1;
        addProductPanel.add(newProductNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addProductPanel.add(new JLabel("New Product Stock:"), gbc);

        newProductStockField = new JTextField(10);
        gbc.gridx = 1;
        addProductPanel.add(newProductStockField, gbc);

        JButton addProductButton = new JButton("Add Product");
        styleButton(addProductButton, Color.BLUE);
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        addProductPanel.add(addProductButton, gbc);

        // Add Sell/Restock and Add Product panels to controlPanel
        controlPanel.add(sellRestockPanel, BorderLayout.WEST);
        controlPanel.add(addProductPanel, BorderLayout.EAST);

        add(controlPanel, BorderLayout.NORTH);

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.BLUE);
        add(statusLabel, BorderLayout.SOUTH);

        // Display the initial table data
        updateTable();
    }

    private void sellProduct() {
        String productName = productNameField.getText();
        if (productName.isEmpty()) {
            showStatus("Product name cannot be empty!", Color.RED);
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            Product product = inventoryManager.getProduct(productName);
            if (product != null) {
                product.sell(quantity);
                updateTable();
                showStatus("Sold " + quantity + " " + productName, Color.GREEN);
            } else {
                showStatus("Product not found!", Color.RED);
            }
        } catch (NumberFormatException e) {
            showStatus("Quantity must be a number!", Color.RED);
        }
    }

    private void restockProduct() {
        String productName = productNameField.getText();
        if (productName.isEmpty()) {
            showStatus("Product name cannot be empty!", Color.RED);
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            Product product = inventoryManager.getProduct(productName);
            if (product != null) {
                product.restock(quantity);
                updateTable();
                showStatus("Restocked " + quantity + " " + productName, Color.GREEN);
            } else {
                showStatus("Product not found!", Color.RED);
            }
        } catch (NumberFormatException e) {
            showStatus("Quantity must be a number!", Color.RED);
        }
    }

    private void addProduct() {
        String newProductName = newProductNameField.getText();
        if (newProductName.isEmpty()) {
            showStatus("Product name cannot be empty!", Color.RED);
            return;
        }
        try {
            int newProductStock = Integer.parseInt(newProductStockField.getText());
            Product newProduct = new Product(newProductName, newProductStock);
            inventoryManager.addProduct(newProduct);
            updateTable();
            showStatus("Added new product: " + newProductName, Color.GREEN);
        } catch (NumberFormatException e) {
            showStatus("Stock must be a number!", Color.RED);
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Clear the table
        for (Product product : inventoryManager.getProducts()) {
            Object[] rowData = {
                    product.getName(),
                    product.getStock(),
                    product.getSold(),
                    product.getRemaining()
            };
            tableModel.addRow(rowData);
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30)); // Adjust size as needed
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryGUI().setVisible(true);
            }
        });
    }
}
