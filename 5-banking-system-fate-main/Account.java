public class Account {
    private int accountNum;
    private String password;
    private String name;
    private Double cash;

    /**
     * Create account that stores user's information from the file imported
     * @param accountNum number the person uses to log in to their account
     * @param password password for their login
     * @param name the name of the person
     * @param cash how much cash is in their account
     */
    public Account(int accountNum, String password, String name, Double cash){
        this.accountNum = accountNum;
        this.password = password;
        this.name = name;
        this.cash = cash;
    }

    // return their account number
    public int getAccountNum() {
        return accountNum;
    }

    // return their password
    public String getPassword() {
        return password;
    }

    // return their name
    public String getName() {
        return name;
    }

    // return how much cash they have
    public Double getCash() {
        return cash;
    }
    
    /**
     * set cash to the amount given
     * @param cash the amount given
     */
    public void setCash(Double cash)
    {
        this.cash = cash;
    }

    /**
     * to turn informations in string
     */
    public String toString()
    {
        String word = getName() + "," + getAccountNum() + "," + getCash();
        
        return word;
    }

}
