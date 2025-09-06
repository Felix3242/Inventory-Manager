import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class Main {
    // Index counters for tracking the number of items in each category
    public static int indexFruit = 0, indexVegetable = 0, indexMeat = 0;

    public static void main(String[] args) throws IOException {
        // ArrayList to store items from the inventory
        ArrayList<Item> items = new ArrayList<Item>();

        // Reading the inventory file
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\14163\\Downloads\\U2A3_FelixGao\\inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split each line by commas to extract item details
                String[] itemParts = line.split(",");
                // Increment category-specific counters based on the item category
                if (itemParts[2].equals("FRUIT")) {
                    indexFruit++;
                } else if (itemParts[2].equals("VEGETABLE")) {
                    indexVegetable++;
                } else {
                    indexMeat++;
                }
                // Create a new Item object and add it to the list
                Item item = new Item(
                    itemParts[0],
                    itemParts[1],
                    itemParts[2],
                    Integer.parseInt(itemParts[3]),
                    Integer.parseInt(itemParts[4]),
                    Double.parseDouble(itemParts[5]),
                    Double.parseDouble(itemParts[6]),
                    Double.parseDouble(itemParts[7]),
                    Double.parseDouble(itemParts[8]),
                    Double.parseDouble(itemParts[9])
                );
                items.add(item);
            }
            br.close(); // Close the reader
        }

        // Write back the inventory data after processing (if any updates are made)
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("C:\\Users\\14163\\Downloads\\U2A3_FelixGao\\inventory.txt"))) {
            for (Item item : items) {
                bw.write(item.toString()); // Write each item to the file
                bw.newLine(); // Add a newline for separation
            }
            bw.close(); // Close the writer
        }

        // First frame: Query Inventory
        JFrame frmQuery = new JFrame("Query Inventory");

        // Second frame: Add to Inventory
        JFrame frmAdd = new JFrame("Add to Inventory");

        // Left panel components for querying inventory
        JPanel pnlQueryLeft = new JPanel(new GridLayout(7, 1));
        JLabel lblTitle = new JLabel("Grocery Store Inventory");
        JLabel lblQuery = new JLabel("To query the inventory, enter either the SKU or the name of the product");
        JLabel lblSKU = new JLabel("SKU (\"ABC-1234\"): ");
        JTextField txtSKU = new JTextField();
        JLabel lblQueryName = new JLabel("Name: ");
        JTextField txtQueryName = new JTextField();
        JButton btnSubmitQuery = new JButton("Submit");
        JButton btnExitQuery = new JButton("Exit");

        // Add a KeyListener to limit the SKU input and toggle the Name field
        txtSKU.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent k) {
                String input = txtSKU.getText();
                if (input.length() > 8) {
                    // Limit the SKU input to 8 characters
                    txtSKU.setText(input.substring(0, 8));
                }
                if (!input.isEmpty()) {
                    txtQueryName.setEnabled(false); // Disable Name input if SKU is entered
                } else {
                    txtQueryName.setEnabled(true); // Enable Name input if SKU is empty
                }
            }
        });

        // Add a KeyListener to filter valid characters for the Name input
        txtQueryName.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent k) {
                String input = txtQueryName.getText();
                String validText = "";
                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);
                    // Allow only letters, spaces, '/', and '-'
                    if (Character.isLetter(c) || c == '/' || c == '-' || c == ' ') {
                        validText += c;
                    }
                }
                // Update the text field with valid characters only
                if (!input.equals(validText)) {
                    txtQueryName.setText(validText);
                }
                if (input.length() > 20) {
                    // Limit the Name input to 20 characters
                    txtQueryName.setText(input.substring(0, 20));
                }
                if (!input.isEmpty()) {
                    txtSKU.setEnabled(false); // Disable SKU input if Name is entered
                } else {
                    txtSKU.setEnabled(true); // Enable SKU input if Name is empty
                }
            }
        });

        // Add components to the left panel
        pnlQueryLeft.add(lblTitle);
        pnlQueryLeft.add(lblQuery);
        pnlQueryLeft.add(lblSKU);
        pnlQueryLeft.add(txtSKU);
        pnlQueryLeft.add(lblQueryName);
        pnlQueryLeft.add(txtQueryName);
        pnlQueryLeft.add(btnSubmitQuery);
        pnlQueryLeft.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Right panel components for displaying query results and add option
        JPanel pnlQueryRight = new JPanel(new GridLayout(7, 1));
        JLabel lblQueryItem = new JLabel("Here is the item in the inventory: ");
        JTextField txtQueryItem = new JTextField();
        txtQueryItem.setEditable(false); // Result field should not be editable
        JTextField txtError = new JTextField();
        txtError.setEditable(false); // Error message field should not be editable
        JLabel lblAdd = new JLabel("To add a product to inventory click here: ");
        JButton btnAdd = new JButton("Add");

        // Add components to the right panel
        pnlQueryRight.add(lblQueryItem);
        pnlQueryRight.add(txtQueryItem);
        pnlQueryRight.add(txtError);
        pnlQueryRight.add(lblAdd);
        pnlQueryRight.add(btnAdd);
        pnlQueryRight.add(btnExitQuery);
        pnlQueryRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Split pane to divide left and right panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlQueryLeft, pnlQueryRight);
        splitPane.setDividerSize(0); // Remove visible divider
        splitPane.setDividerLocation(450); // Set divider location

        frmQuery.setVisible(true); // Display the Query frame
        frmQuery.setSize(1100, 300); // Set frame size

        // Submit button action listener for querying inventory
        btnSubmitQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String SKU = txtSKU.getText();
                String name = txtQueryName.getText();
                boolean foundItem = false;

                // Check if both fields are empty
                if (name.isEmpty() && SKU.isEmpty()) {
                    txtError.setText("Please enter a name or SKU");
                    txtQueryItem.setText("");
                    return;
                }
                // Search by SKU
                else if (!SKU.isEmpty() && name.isEmpty()) {
                    // Validate SKU format
                    if (!SKU.matches("^[A-Z]{3}-\\d{4}$")) {
                        txtQueryItem.setText("");
                        txtError.setText("Please enter a proper SKU");
                        return;
                    }
                    // Look for the item in the inventory
                    for (Item item : items) {
                        if (SKU.equals(item.getSku())) {
                            txtQueryItem.setText(item.toString());
                            txtError.setText("This item is in the inventory");
                            foundItem = true;
                            break;
                        }
                    }
                    if (!foundItem) {
                        txtQueryItem.setText("");
                        txtError.setText("This item is not in the inventory");
                    }
                }

                // Search by name
                else if (!name.isEmpty() && SKU.isEmpty()) {
                    // Case-insensitive name search
                    for (Item item : items) {
                        if (name.toLowerCase().equals(item.getName().toLowerCase())) {
                            txtQueryItem.setText(item.toString());
                            txtError.setText("This item is in the inventory");
                            foundItem = true;
                            break;
                        }
                    }
                    if (!foundItem) {
                        txtQueryItem.setText("");
                        txtError.setText("This item is not in the inventory");
                    }
                }
            }
        });

        //button that switches frames from query inventory to add to inventory
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frmQuery.setVisible(false);
                frmAdd.setVisible(true);
            }
        });


        // Exit button action listener to close the application
        btnExitQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Terminate the program
            }
        });

        // Add the split pane to the query frame
        frmQuery.add(splitPane);

        // Set the size of the add frame
        frmAdd.setSize(800, 400);

        // Create the left panel with a grid layout of 9 rows and 2 columns
        JPanel pnlAddLeft = new JPanel(new GridLayout(9, 2));

        // Add a header label for instructions
        JLabel lblHead = new JLabel("To add to the inventory: ");

        // Add a description label for the user
        JLabel lblDescription = new JLabel("Enter all the information needed below");

        // Create and label the text field for entering the name
        JLabel lblName = new JLabel("Name: ");
        JTextField txtName = new JTextField();

        // Create and label the spinner for selecting the category
        JLabel lblCategory = new JLabel("Category: (FRUIT, VEGETABLE, MEAT) ");
        String[] categories = { "FRUIT", "VEGETABLE", "MEAT" };
        JSpinner spnCategory = new JSpinner(new SpinnerListModel(categories));

        // Create and label the spinner for entering the quantity
        JLabel lblQuantity = new JLabel("Quantity: ");
        JSpinner spnQuantity = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        // Create and label the spinner for entering the minimum quantity
        JLabel lblMinimumQuantity = new JLabel("Minimum Quantity: ");
        JSpinner spnMinimumQuantity = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        // Create and label the text field for entering the vendor price
        JLabel lblPrice = new JLabel("Vendor Price $ (0.00): ");
        JTextField txtPrice = new JTextField();

        // Create and label the spinner for entering the markup percentage
        JLabel lblMarkupPercentage = new JLabel("Markup percentage (%): ");
        JSpinner spnMarkeupPercentage = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

        // Create and label the spinner for entering the current discount percentage
        JLabel lblCurrentDiscount = new JLabel("Current Discount Percentage (%): ");
        JSpinner spnCurrentDiscount = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

        // Create a button for submitting the form
        JButton btnSubmitAdd = new JButton("Submit");

        // Add padding to the left panel
        pnlAddLeft.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add all components to the left panel
        pnlAddLeft.add(lblHead);
        pnlAddLeft.add(lblDescription);
        pnlAddLeft.add(lblName);
        pnlAddLeft.add(txtName);
        pnlAddLeft.add(lblCategory);
        pnlAddLeft.add(spnCategory);
        pnlAddLeft.add(lblQuantity);
        pnlAddLeft.add(spnQuantity);
        pnlAddLeft.add(lblMinimumQuantity);
        pnlAddLeft.add(spnMinimumQuantity);
        pnlAddLeft.add(lblPrice);
        pnlAddLeft.add(txtPrice);
        pnlAddLeft.add(lblMarkupPercentage);
        pnlAddLeft.add(spnMarkeupPercentage);
        pnlAddLeft.add(lblCurrentDiscount);
        pnlAddLeft.add(spnCurrentDiscount);
        pnlAddLeft.add(btnSubmitAdd);

        // Create the right panel with a vertical box layout
        JPanel pnlAddRight = new JPanel();
        pnlAddRight.setLayout(new BoxLayout(pnlAddRight, BoxLayout.Y_AXIS));

        // Add padding to the right panel
        pnlAddRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add a label to the right panel for displaying added items
        JLabel lblAddToInventory = new JLabel("Here is the item added to inventory:");
        lblAddToInventory.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlAddRight.add(lblAddToInventory);

        // Add vertical spacing
        pnlAddRight.add(Box.createRigidArea(new Dimension(0, 5)));

        // Add a text field to display the added inventory item
        JTextField txtAddtoInventory = new JTextField();
        txtAddtoInventory.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtAddtoInventory.setPreferredSize(new Dimension(400, 30));
        txtAddtoInventory.setEditable(false);
        pnlAddRight.add(txtAddtoInventory);

        // Add vertical spacing
        pnlAddRight.add(Box.createRigidArea(new Dimension(0, 5)));

        // Add a large text area for output
        JTextArea txtOutput = new JTextArea();
        txtOutput.setLineWrap(true);
        txtOutput.setWrapStyleWord(true);
        txtOutput.setEditable(false);

        // Wrap the text area in a scroll pane
        txtOutput.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        JScrollPane scpOutput = new JScrollPane(txtOutput);
        scpOutput.setPreferredSize(new Dimension(400, 200));
        pnlAddRight.add(scpOutput);

        // Add vertical spacing
        pnlAddRight.add(Box.createRigidArea(new Dimension(0, 5)));

        // Add a text field to display error messages
        JTextField txtErrorMessage = new JTextField();
        txtErrorMessage.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtErrorMessage.setPreferredSize(new Dimension(400, 30));
        txtErrorMessage.setEditable(false);
        pnlAddRight.add(txtErrorMessage);

        // Add vertical spacing before buttons
        pnlAddRight.add(Box.createRigidArea(new Dimension(0, 10)));

        // Create a panel for buttons
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));

        // Create buttons for return and exit
        JButton btnReturn = new JButton("Return");
        JButton btnExit = new JButton("Exit");

        // Add buttons with spacing
        pnlButtons.add(btnReturn);
        pnlButtons.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlButtons.add(btnExit);

        // Center align the button panel
        pnlButtons.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add the button panel to the right panel
        pnlAddRight.add(pnlButtons);

        // Create a split pane to combine the left and right panels
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlAddLeft, pnlAddRight);
        splitPane2.setDividerSize(0); // Remove divider space
        splitPane2.setDividerLocation(500); // Set divider location

        // Add the split pane to the add frame
        frmAdd.add(splitPane2);

        // Set the size of the add frame
        frmAdd.setSize(1200, 400);

        // Add action listener to the submit button
        btnSubmitAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sku = ""; // Initialize SKU
                String name = txtName.getText(); // Get name from the text field
                String category = (String) spnCategory.getValue(); // Get category from the spinner
                int quantity = (int) spnQuantity.getValue(); // Get quantity from the spinner
                int minimumQuantity = (int) spnMinimumQuantity.getValue(); // Get minimum quantity from the spinner

                String price = txtPrice.getText(); // Get price from the text field
                double vendorPrice = 0; // Initialize vendor price

                int markup = (int) spnMarkeupPercentage.getValue(); // Get markup percentage
                double markupPercentage = (double) markup; // Convert to double
                int discount = (int) spnCurrentDiscount.getValue(); // Get current discount percentage
                double currentDiscount = (double) discount; // Convert to double

                // Check if the item already exists in the inventory
                for (Item item : items) {
                    if (name.toLowerCase().equals(item.getName().toLowerCase()) && category.equals(item.getCategory())) {
                        txtErrorMessage.setText("This item already exists");
                        return; // Exit if duplicate found
                    }
                }

                // Validate name field
                if (name.isEmpty()) {
                    txtErrorMessage.setText("Please enter a valid name");
                    return;
                }

                // Validate price format
                if (!price.matches("^\\d+(\\.\\d{0,2})?$")) {
                    txtErrorMessage.setText("Please enter a proper price $ (0.00)");
                    return;
                }

                vendorPrice = Double.parseDouble(price); // Parse vendor price

                // Check if the vendor price is zero
                if (vendorPrice == 0.0) {
                    txtErrorMessage.setText("Price cannot be zero");
                    return;
                } else {
                    // Generate SKU based on category
                    if (category.equals("FRUIT")) {
                        if ((indexFruit + 1) < 100) {
                            sku = "FRU-00" + (indexFruit + 1);
                        } else if ((indexFruit + 1) >= 100 && (indexFruit + 1) < 1000) {
                            sku = "FRU-0" + (indexFruit + 1);
                        } else {
                            sku = "FRU-" + (indexFruit + 1);
                        }
                    } else if (category.equals("VEGETABLE")) {
                        if ((indexVegetable + 1) >= 100 && (indexVegetable + 1) < 1000) {
                            sku = "VEG-0" + (indexVegetable + 1);
                        } else {
                            sku = "VEG-" + (indexVegetable + 1);
                        }
                    } else {
                        if ((indexMeat + 1) < 10) {
                            sku = "MEA-000" + (indexMeat + 1);
                        } else if ((indexMeat + 1) >= 10 && (indexMeat + 1) < 100) {
                            sku = "MEA-00" + (indexMeat + 1);
                        } else if ((indexMeat + 1) >= 100 && (indexMeat + 1) < 1000) {
                            sku = "MEA-0" + (indexMeat + 1);
                        } else {
                            sku = "MEA-" + (indexMeat + 1);
                        }
                    }

                    // Calculate regular and current prices
                    double regularPrice = Double.parseDouble(String.format("%.2f", (vendorPrice * (1 + markupPercentage / 100))));
                    double currentPrice = Double.parseDouble(String.format("%.2f", (regularPrice * (1 - currentDiscount / 100))));

                    // Create a new item
                    Item newItem = new Item(sku, name, category, quantity, minimumQuantity, vendorPrice, markupPercentage,
                            regularPrice, currentDiscount, currentPrice);

                    // Add the item to the inventory based on the category
                    if (category.equals("FRUIT")) {
                        items.add(indexFruit, newItem);
                        indexFruit++;
                    } else if (category.equals("VEGETABLE")) {
                        items.add(indexFruit + indexVegetable, newItem);
                        indexVegetable++;
                    } else {
                        items.add(indexFruit + indexVegetable + indexMeat, newItem);
                        indexMeat++;
                    }

                    // Write inventory data to a file
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(
                            "C:\\Users\\14163\\Downloads\\U2A3_FelixGao\\inventory.txt"))) {
                        for (Item item : items) {
                            bw.write(item.toString() + "\n");
                        }
                        bw.close();
                    } catch (IOException ex) {
                        // Handle any IO exceptions
                    }

                    // Display the added item in the text field
                    txtAddtoInventory.setText(newItem.toString());

                    // Display the full inventory in the text area
                    String output = "";
                    for (Item item : items) {
                        output += item.toString() + "\n";
                    }
                    txtOutput.setText(output);

                    // Display success message
                    txtErrorMessage.setText("Item added successfully");
                }
            }
        });

        // Add action listener to the return button
        btnReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frmAdd.setVisible(false); // Hide add frame
                frmQuery.setVisible(true); // Show query frame
            }
        });

        // Add action listener to the exit button
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program
            }
        });
    }
}