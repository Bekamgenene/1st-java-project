import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*  Name            ID_No.
    Endrias Eshetu  UGR/30469/15
    Bekam Genene    UGR/30253/15


*/
record Transaction(String type, double amount, String date, double balanceAfter) {
}

class BankStatement {
    private final List<Transaction> transactions;

    public BankStatement() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public String displayStatement() {
        StringBuilder statement = new StringBuilder("Bank Statement Summary:\n");
        statement.append("----------------------------\n");
        for (Transaction transaction : transactions) {
            statement.append("Date: ").append(transaction.date()).append("\n");
            statement.append("Type: ").append(transaction.type()).append("\n");
            statement.append("Amount: ").append(transaction.amount()).append("\n");
            statement.append("Balance After: ").append(transaction.balanceAfter()).append("\n");
            statement.append("----------------------------\n");
        }
        return statement.toString();
    }
}


class Machine {
    private double balance = 0;
    static final int PIN = 1122; // Use final for constants
    private final BankStatement bankStatement = new BankStatement();

    public boolean checkPIN(int pin) {
        return pin == PIN;
    }

    public double getBalance() {
        return balance;
    }

    public void depositMoney(double amount) {
        balance += amount;
        String date = java.time.LocalDate.now().toString();
        bankStatement.addTransaction(new Transaction("deposit", amount, date, balance));
    }

    public void withdrawMoney(double amount) {
        if (amount <= balance) {
            balance -= amount;
            String date = java.time.LocalDate.now().toString();
            bankStatement.addTransaction(new Transaction("withdrawal", amount, date, balance));
        } else {
            throw new IllegalArgumentException("Insufficient Balance!");
        }
    }

    public void billPayment(double amount) {
        if (amount <= balance) {
            balance -= amount;
            String date = java.time.LocalDate.now().toString();
            bankStatement.addTransaction(new Transaction("bill payment", amount, date, balance));
        } else {
            throw new IllegalArgumentException("Insufficient funds!");
        }
    }

    public void fundTransfer(double amount) {
        if (amount <= balance) {
            balance -= amount;
            String date = java.time.LocalDate.now().toString();
            bankStatement.addTransaction(new Transaction("fund transfer", amount, date, balance));
        } else {
            throw new IllegalArgumentException("Insufficient funds!");
        }
    }

    public String printStatement() {
        return bankStatement.displayStatement();
    }
}


public class ATM {
    private final Machine atm = new Machine();
    private final JFrame frame;
    private final JTextArea outputArea;
    private final JTextField inputField;
    private final JLabel balanceLabel;
    private final JPanel mainPanel;

    public ATM() {
        frame = new JFrame("ATM Machine");
        outputArea = new JTextArea(20, 30);
        inputField = new JTextField(10);
        balanceLabel = new JLabel("Current Balance: 0.0");
        mainPanel = new JPanel();

        // Initialize the PIN entry panel
        showPinEntryPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void showPinEntryPanel() {
        JPanel pinPanel = new JPanel();
        pinPanel.setLayout(new FlowLayout());
        pinPanel.add(new JLabel("Enter PIN:"));
        pinPanel.add(inputField);
        JButton pinButton = new JButton("Submit");

        pinPanel.add(pinButton);
        frame.getContentPane().add(pinPanel);

        pinButton.addActionListener(_ -> {
            try {
                int pin = Integer.parseInt(inputField.getText());
                if (atm.checkPIN(pin)) {
                    outputArea.append("PIN accepted.\n");
                    balanceLabel.setText("Current Balance: " + atm.getBalance());
                    showMainInterface(); // Show main ATM functionalities
                } else {
                    outputArea.append("Invalid PIN.\n");
                }
            } catch (NumberFormatException ex) {
                outputArea.append("Please enter a valid PIN.\n");
            }
            inputField.setText(""); // Clear input field
        });

        frame.getContentPane().add(pinPanel);
    }

    private void showMainInterface() {
        // Remove the PIN panel and set up the main panel
        frame.getContentPane().removeAll();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton billButton = new JButton("Pay Bill");
        JButton transferButton = new JButton("Transfer Funds");
        JButton statementButton = new JButton("Print Statement");
        JButton exitButton = new JButton("Exit");

        mainPanel.add(new JLabel("Input:"));
        mainPanel.add(inputField);
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing
        mainPanel.add(depositButton);
        mainPanel.add(withdrawButton);
        mainPanel.add(billButton);
        mainPanel.add(transferButton);
        mainPanel.add(statementButton);
        mainPanel.add(exitButton);
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing
        mainPanel.add(balanceLabel);
        mainPanel.add(new JScrollPane(outputArea));

        // Add action listeners for buttons
        depositButton.addActionListener(_ -> {
            try {
                double amount = Double.parseDouble(inputField.getText());
                atm.depositMoney(amount);
                outputArea.append("Deposited: " + amount + "\n");
                balanceLabel.setText("Current Balance: " + atm.getBalance());
            } catch (NumberFormatException ex) {
                outputArea.append("Please enter a valid amount.\n");
            }
            inputField.setText(""); // Clear input field
        });


        withdrawButton.addActionListener(_ -> {
            try {
                double amount = Double.parseDouble(inputField.getText());
                atm.withdrawMoney(amount);
                outputArea.append("Withdrew: " + amount + "\n");
                balanceLabel.setText("Current Balance: " + atm.getBalance());
            } catch (NumberFormatException ex) {
                outputArea.append("Please enter a valid amount.\n");
            } catch (IllegalArgumentException ex) {
                outputArea.append(ex.getMessage() + "\n");
            }
            inputField.setText(""); // Clear input field
        });

        billButton.addActionListener(_ -> {
            try {
                double amount = Double.parseDouble(inputField.getText());
                atm.billPayment(amount);
                outputArea.append("Bill paid: " + amount + "\n");
                balanceLabel.setText("Current Balance: " + atm.getBalance());
            } catch (NumberFormatException ex) {
                outputArea.append("Please enter a valid amount.\n");
            } catch (IllegalArgumentException ex) {
                outputArea.append(ex.getMessage() + "\n");
            }
            inputField.setText(""); // Clear input field
        });

        transferButton.addActionListener(_ -> {
            try {
                double amount = Double.parseDouble(inputField.getText());
                atm.fundTransfer(amount);
                outputArea.append("Transferred: " + amount + "\n");
                balanceLabel.setText("Current Balance: " + atm.getBalance());
            } catch (NumberFormatException ex) {
                outputArea.append("Please enter a valid amount.\n");
            } catch (IllegalArgumentException ex) {
                outputArea.append(ex.getMessage() + "\n");
            }
            inputField.setText(""); // Clear input field
        });

        statementButton.addActionListener(_ -> outputArea.append(atm.printStatement() + "\n"));

        exitButton.addActionListener(_ -> System.exit(0));

        frame.getContentPane().add(mainPanel);
        frame.revalidate(); // Refresh the frame to show the new panel
        frame.repaint();
    }

    public static void main(String[] args) {
        new ATM();
    }
}
