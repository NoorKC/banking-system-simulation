import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * constructer 
 */
public class Collection 
{
    private Account currentAccount; 
    private ArrayList<Account> accounts;
    private String bankFile;
    
    /**
     * to read the accounts and add them into the collection
     * @param bankFile the text file to read
     */
    public Collection(String bankFile)
    {
        // initialize arra list 
        this.accounts = new ArrayList<>();
        // store the bankfile name to update it later
        this.bankFile = bankFile;
        // reading in the text file
        Scanner input = null;
        // open the file with the scanner
        try{
            input = new Scanner(new File(bankFile));
        }catch(Exception e){
            // if errors, print them out
            e.printStackTrace();
        }
        // get rid of the header in the file
        input.nextLine();
        // loop until we run out of books
        while(input.hasNext())
        {
            // scan an entire book line
            String bankLine = input.nextLine();
            // break up the info
            String[] bankInfo = bankLine.split(",");
            // put the informations in variable
            int accountNum = Integer.parseInt(bankInfo[0]);
            String password = bankInfo[1];
            String name = bankInfo[2];
            Double cash = Double.parseDouble(bankInfo[3]);
            // make the account
            Account newBank = new Account(accountNum, password, name, cash);
            // add book to arraylist
            this.accounts.add(newBank);
        }
          // close off the Scanner
          input.close();
    }

    /**
     * to varify user number with the account
     * @param account account number of user's bank account
     * @param password password of bank account
     * @return return if user had succesfully logged in or not
     */
    public Boolean logIn(String account, String password)
    {
        String a, p;
        
        for (Account b : accounts) 
        {
            a = String.valueOf(b.getAccountNum());
            p = b.getPassword();
            
            if(account.equals(a) && password.equals(p))
            {
                currentAccount = b;
                    
                return true;
            }
        } 
        
        return false;
    }

    /**
     * return the current balance
     * @return the balance
     */
    public double getBalance()
    {
        return currentAccount.getCash();
    }

    /**
     * return the name of account on
     * @return name
     */
    public String getName()
    {
        return currentAccount.getName();
    }

    /**
     * methode to set user's balance to the amount withdrawn
     * @param cash the amount user wants to withdraw
     * @return return the amount withdrawn
     */
    public Double withdrawCash(Double cash)
    {
        // get costomer money
        Double moneyOwn = currentAccount.getCash() - cash;
        // if the enough money in bank, let user take out the cash
        if(cash <= currentAccount.getCash())
        {
            // set user bank amount to the amount left after withdrawn
            currentAccount.setCash(moneyOwn);
        }
        return moneyOwn;

    }

    /**
     * add the amount deposit to user's balance
     * @param cash the amount user want to deposit
     */
    public void depositFunds(Double cash)
    {
        // add the amount to user account 
        Double moneyOwn = currentAccount.getCash() + cash;
        // set the amount to added
        currentAccount.setCash(moneyOwn);
    }
}


