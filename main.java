
import java.util.*;
import java.text.SimpleDateFormat;

class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;
    private ArrayList<Transaction> transactions;
    private double dailyWithdrawal;
    private Date lastWithdrawalDate;

    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        this.dailyWithdrawal = 0;
        this.lastWithdrawalDate = new Date();
        transactions.add(new Transaction("ACCOUNT CREATED", initialBalance, initialBalance));
    }

    // Getters and other methods
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }
    
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add(new Transaction("DEPOSIT", amount, balance));
            return true;
        }
        return false;
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance && checkDailyLimit(amount)) {
            balance -= amount;
            dailyWithdrawal += amount;
            transactions.add(new Transaction("WITHDRAWAL", amount, balance));
            return true;
        }
        return false;
    }
    
    private boolean checkDailyLimit(double amount) {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        
        if (!sdf.format(today).equals(sdf.format(lastWithdrawalDate))) {
            dailyWithdrawal = 0;
            lastWithdrawalDate = today;
        }
        
        return (dailyWithdrawal + amount) <= 50000; // Daily limit: 50,000
    }
    
    public void displayAccountInfo() {
        System.out.println("\n=== ACCOUNT INFORMATION ===");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Current Balance: ₹" + balance);
        System.out.println("Daily Withdrawal: ₹" + dailyWithdrawal);
    }
    
    public void printPassbook(Date fromDate, Date toDate) {
        System.out.println("\n=== PASSBOOK STATEMENT ===");
        System.out.println("Account: " + accountNumber + " - " + accountHolder);
        System.out.println("Period: " + fromDate + " to " + toDate);
        System.out.println("\nDate\t\tType\t\tAmount\tBalance");
        System.out.println("==============================================");
        
        for (Transaction t : transactions) {
            if (!t.getDate().before(fromDate) && !t.getDate().after(toDate)) {
                System.out.println(t);
            }
        }
    }
    
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}

class Transaction {
    private Date date;
    private String type;
    private double amount;
    private double balanceAfter;
    
    public Transaction(String type, double amount, double balanceAfter) {
        this.date = new Date();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }
    
    public Date getDate() { return date; }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date) + "\t" + type + "\t\t₹" + amount + "\t₹" + balanceAfter;
    }
}

class BankingSystem {
    private HashMap<String, BankAccount> accounts;
    private int accountCounter;
    
    public BankingSystem() {
        accounts = new HashMap<>();
        accountCounter = 1001;
    }
    
    public void createAccount(String accountHolder, double initialDeposit) {
        String accountNumber = "ACC" + accountCounter++;
        BankAccount account = new BankAccount(accountNumber, accountHolder, initialDeposit);
        accounts.put(accountNumber, account);
        System.out.println("Account created successfully!");
        System.out.println("Account Number: " + accountNumber);
    }
    
    public BankAccount findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    
    public void displayAllAccounts() {
        System.out.println("\n=== ALL ACCOUNTS ===");
        for (BankAccount account : accounts.values()) {
            account.displayAccountInfo();
            System.out.println("----------------------------");
        }
    }
}

public class Main {
    private static BankingSystem bank = new BankingSystem();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1: createAccount(); break;
                case 2: depositMoney(); break;
                case 3: withdrawMoney(); break;
                case 4: checkBalance(); break;
                case 5: displayAccountInfo(); break;
                case 6: printPassbook(); break;
                case 7: bank.displayAllAccounts(); break;
                case 8: 
                    System.out.println("Thank you for using our Banking System!");
                    return;
                default: 
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n=== BANKING SYSTEM ===");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Check Balance");
        System.out.println("5. Display Account Information");
        System.out.println("6. Print Passbook");
        System.out.println("7. Display All Accounts");
        System.out.println("8. Exit");
    }
    
    private static void createAccount() {
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();
        double deposit = getDoubleInput("Enter Initial Deposit: ");
        bank.createAccount(name, deposit);
    }
    
    private static void depositMoney() {
        String accNo = getAccountNumber();
        BankAccount account = bank.findAccount(accNo);
        if (account != null) {
            double amount = getDoubleInput("Enter deposit amount: ");
            if (account.deposit(amount)) {
                System.out.println("Deposit successful!");
            } else {
                System.out.println("Invalid deposit amount!");
            }
        } else {
            System.out.println("Account not found!");
        }
    }
    
    private static void withdrawMoney() {
        String accNo = getAccountNumber();
        BankAccount account = bank.findAccount(accNo);
        if (account != null) {
            double amount = getDoubleInput("Enter withdrawal amount: ");
            if (account.withdraw(amount)) {
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Withdrawal failed! Check balance or daily limit.");
            }
        } else {
            System.out.println("Account not found!");
        }
    }
    
    private static void checkBalance() {
        String accNo = getAccountNumber();
        BankAccount account = bank.findAccount(accNo);
        if (account != null) {
            System.out.println("Current Balance: ₹" + account.getBalance());
        } else {
            System.out.println("Account not found!");
        }
    }
    
    private static void displayAccountInfo() {
        String accNo = getAccountNumber();
        BankAccount account = bank.findAccount(accNo);
        if (account != null) {
            account.displayAccountInfo();
        } else {
            System.out.println("Account not found!");
        }
    }
    
    private static void printPassbook() {
        String accNo = getAccountNumber();
        BankAccount account = bank.findAccount(accNo);
        if (account != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                System.out.print("Enter From Date (dd/MM/yyyy): ");
                Date fromDate = sdf.parse(scanner.nextLine());
                System.out.print("Enter To Date (dd/MM/yyyy): ");
                Date toDate = sdf.parse(scanner.nextLine());
                account.printPassbook(fromDate, toDate);
            } catch (Exception e) {
                System.out.println("Invalid date format!");
            }
        } else {
            System.out.println("Account not found!");
        }
    }
    
    private static String getAccountNumber() {
        System.out.print("Enter Account Number: ");
        return scanner.nextLine();
    }
    
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input! " + prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return value;
    }
    
    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input! " + prompt);
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        return value;
    }
}
