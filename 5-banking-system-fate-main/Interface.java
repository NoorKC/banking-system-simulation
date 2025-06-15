import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Font;

public class Interface extends JFrame implements KeyListener, ActionListener 
{
    // create instance variables
    private JLabel mainLabel = new JLabel();
    private JButton[] buttons = new JButton[36];
    private JButton checkButton = new JButton("Enter");
    
    // variable used to switch the screen
    private int phase;
    // the current balance of user
    private double balance;

    // set the numbers for screen, the numbers are used to identify which screen is on
    private final int ACCOUNT_SCREEN = 0;
    private final int PASSWORD_SCREEN = 1;
    private final int MENU_SCREEN = 2;
    private final int BALANCE_SCREEN = 3;
    private final int WITHDRAW_SCREEN = 4;
    private final int DEPOSIT_SCREEN = 5;
    private final int EXIT_SCREEN = 6;
    // WRESULT = result of withdraw
    private final int WRESULT_SCREEN = 7;
    // DRESULT = result of deposit
    private final int DRESULT_SCREEN = 8;

    // for pop up screen
    private UIManager um = new UIManager();
    
    // to store inputs from user 
    private String name = new String();
    private String account = new String();
    private String password = new String();
    private String selection = new String();
    private String withdraw = new String();
    private String deposit = new String();
    private String[] mainStr = new String[5];

    // ==========================
    // for integration
    // ==========================
    private Collection accounts;
    	
    /**
     * create panels
     */
    public Interface() 
    {
        // ==========================
        // for integration
        // ==========================
        
        accounts = new Collection("bank.txt");
        resetVariables();

        // ==========================
        // user interface
        // ==========================
        // to use box layout
        Box box = Box.createVerticalBox();
        
        // create panels in box
        // top panel
        JPanel cyan = new JPanel();
        cyan.setPreferredSize( new Dimension(500, 400) );
        cyan.setMinimumSize( cyan.getPreferredSize() );
		cyan.setMaximumSize( cyan.getPreferredSize() );
        // methode called to add label for display to panel
		addLabelToPane(cyan);
        box.add( cyan );

        // lower panel
		JPanel green = new JPanel();
        green.setPreferredSize( new Dimension(500, 300) );
		green.setMinimumSize( green.getPreferredSize() );
        green.setMaximumSize( green.getPreferredSize() );
        // methode called to make keyboard for panel
		addKeyToPane(green);
        box.add( green );

        getContentPane().add(box, BorderLayout.CENTER);
        
        setSize(600, 800);
        setLocationRelativeTo(null); // Center the window on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startListening();
    }

    
    public void keyPressed(KeyEvent e) 
    {
        // Not using
    }
    public void keyReleased(KeyEvent e) 
    {
        // Not needed for this program
    }
    public void keyTyped(KeyEvent e) 
    {
        // Not needed for this program
    }

    /**
     * to perform action of phases according to the button pressed
     */
    public void actionPerformed(ActionEvent e) 
    {
        // get what button is currently clicked by user
        JButton sourceButton = (JButton) e.getSource();
        // turn the button into String
        String buttonText = sourceButton.getText();
    
        // function of each screen/phase
        switch(phase)
        {
            // action to do for account screen
            case ACCOUNT_SCREEN: 
                    // use another switch to seperate the inputs and enter
                    switch (buttonText)
                    {
                        case "Enter":   
                                        // change screen to password screen
                                        phase = PASSWORD_SCREEN;
                                        // keep the original text and add the intruction for password on the other line
                                        mainStr[0] = mainStr[0] + "<br/><br/>" + mainStr[1];
                                        break;
                        // while user still typing
                        default:
                                        // add the button clicked to the input for account
                                        account = account + buttonText;
                                        // add the thing user typed on screen along with intruction
                                        mainStr[0] = mainStr[0] + buttonText;
                    }
                    break;
            
            case PASSWORD_SCREEN:
            
                    switch (buttonText)
                    {
                        case "Enter":   
                                        // if login methode returns true
                                        if(accounts.logIn(account, password))
                                        {
                                            // switch users to user menu
                                            phase = MENU_SCREEN;
                                            // get the balance of user's account
                                            balance = accounts.getBalance();
                                            // get the name of user
                                            name = accounts.getName();
                                        }
                                        else
                                        {
                                            // tell user n account find and switch them back to the login sceen
                                            showMessage("Sorry, We cannot find your account!", Color.ORANGE, Color.BLUE);
                                            phase = ACCOUNT_SCREEN;
                                            resetVariables();
                                        }
                                        break;
                        default:
                                        // store the buttons user clicked into a String
                                        password = password + buttonText;
                                        // don't display the password but show * instead when user click in a button
                                        mainStr[0] = mainStr[0] + "*";
                    }
                    break;

            case MENU_SCREEN:

                    switch (buttonText)
                    {
                        case "Enter":   
                                        // display selection
                                        // 1. View my balance 2. Withdraw 3. Deposit 4. Exit
                                        // corresponding phase: 3, 4, 5, 6
                                        // corresponding string: 3, 4, 5, 6
                                        if (Integer.parseInt(selection) !=0 && Integer.parseInt(selection) < 5)
                                        {
                                            // the screens are 2 more number than the user's click so add 2 to input
                                            phase = Integer.parseInt(selection) + 2;
                                        }
                                        else
                                        {
                                            // if the selection is out of bound
                                            showMessage("Invalid selection!", Color.ORANGE, Color.BLUE);
                                            // keep users in menue screen
                                            phase = MENU_SCREEN;
                                            resetString();
                                        }
                                        break;
                        default:
                                        // get users selection on what they want to view
                                        selection = buttonText;
                                        // add their choic with the intruction
                                        mainStr[2] = mainStr[2] + buttonText;
                    }
                    break;

            case BALANCE_SCREEN:

                    switch (buttonText)
                    {
                        case "Enter":
                                        // switch back to menu screen
                                        phase = MENU_SCREEN; 
                                        // reset string for later to ask user for input again with the past input
                                        resetString();
                                        break;
                        default:
                        // no input need to record
                    }
                    break;

            case WITHDRAW_SCREEN: 

                    switch (buttonText)
                    {
                        case "Enter":
                                        // get the new balance
                                        balance = balance - Double.parseDouble(withdraw);
                                        // set the balance only if user have enough
                                        if (balance < 0)
                                        {
                                            //balance = balance + Double.parseDouble(withdraw);
                                            showMessage("You don't have sufficient funds to withdraw!", Color.ORANGE, Color.BLUE);
                                            balance = accounts.getBalance();
                                        }
                                        else
                                        {
                                            accounts.withdrawCash(Double.parseDouble(withdraw));
                                        }
                                        // change to WRESULT screen for going back to menu page
                                        phase = WRESULT_SCREEN;
                                        break;
                        default:
                                        // record the buttons user click 
                                        withdraw = withdraw + buttonText;
                                        // add the buttons with the intructions on screen
                                        mainStr[3] = mainStr[3] + buttonText;
                    }
                    break;
            
            case DEPOSIT_SCREEN: 

                    switch (buttonText)
                    {
                        case "Enter":
                                        // call the screen to switch back to menue after display balance
                                        phase = DRESULT_SCREEN;
                                        // add the deposit into user's balance
                                        balance = balance + Double.parseDouble(deposit);
                                        // set the deposit in users account 
                                        accounts.depositFunds(Double.parseDouble(deposit));
                                        break;
                        default:
                                        // add user input
                                        deposit = deposit + buttonText;
                                        mainStr[4] = mainStr[4] + buttonText;
                    }
                    break;

            case EXIT_SCREEN: 
                    
                    if (buttonText == "Enter")
                    {
                        // turn back to login screen
                        phase = ACCOUNT_SCREEN;
                        resetVariables();
                    }
                    break;

            // to go back to manue screen
            case WRESULT_SCREEN:
            case DRESULT_SCREEN:
            
                    if (buttonText == "Enter")
                    {
                        phase = MENU_SCREEN; 
                        resetString(); 
                    }
                    break;
            
            default:
        }
        
    }

    private void startListening() 
    {
        setFocusable(true);
        addKeyListener(this);
    }

    /**
     * make the visual keyboard 
     * @param pane the panel given to add
     */
    private void addKeyToPane(Container pane) 
    {
        // set grid into 6x5 for engough space
        pane.setLayout(new GridLayout(6, 5));

        // digit keys, from 0-9
        for (int i=0; i<10; i++)
        {
            // switch char to string 
            buttons[i] = new JButton(Character.toString((char) (48+i)));
            buttons[i].setFocusable(false);
            // add button on panel 
            pane.add(buttons[i]);
            buttons[i].addActionListener(this);
        }

        // A~Z keys
        for (int i=0; i<26; i++)
        {
            // turn characters into string, button start from 10 since integer keys added
            buttons[10+i] = new JButton(Character.toString((char) (97+i)));
            buttons[10+i].setFocusable(false);
            // add button in panel
            pane.add(buttons[10+i]);
            buttons[10+i].addActionListener(this);
        }

        // set enter button
        checkButton.setFocusable(false);
        pane.add(checkButton);
        checkButton.addActionListener(this);
    }
	    
    /**
     * adding the words diplay on screen to panel
     * @param pane the panel to add label on 
     */
    private void addLabelToPane(Container pane) 
    {
        // set grid into one single screen
        pane.setLayout(new GridLayout(1, 1));

        // initialize label 
        mainLabel = new JLabel();
        // set aligmen to the top of the screen
        mainLabel.setVerticalAlignment(JLabel.TOP);
        // set visibility
        mainLabel.setOpaque(true); 
        mainLabel.setForeground(Color.WHITE);
        mainLabel.setBackground(Color.BLACK);
        mainLabel.setFont(new Font("Arial",Font.BOLD,16));
        mainLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        pane.add(mainLabel);
    }
    
    // pop up screen
    private void showMessage(String s, Color b, Color f)
    {
        // set font
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
        // set colors
        UIManager.put("OptionPane.messageForeground", f);
        UIManager.put("OptionPane.background", b);
        UIManager.put("Panel.background", b);
        UIManager.put("InternalFrame.activeTitleBackground", Color.pink);
        // set the message
        JOptionPane.showOptionDialog(null, s, null, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
    }

    /**
     * reset variables back to starting value
     */
    private void resetVariables()
    {
        phase = 0;
        // clear all user inputs
        account = "";
        password = "";
        selection = "";
        withdraw = "";
        deposit = "";
        
        resetString();
    }
    
    /**
     * set the strings back to original, without input added
     */
    private void resetString()
    {
        selection = "";
        withdraw = "";
        deposit = "";
        mainStr[0] = "Please input your account number and press Enter: <br/><br/>>> ";
        mainStr[1] = "Please input your password and press Enter: <br/><br/>>> ";
        mainStr[2] = ">> ";
        mainStr[3] = ">> ";
        mainStr[4] = ">> ";
    }

    /**
     * set labels & displays (all screens)
     */
    private void loginScreen()
    {
        // set color
        mainLabel.setForeground(Color.BLUE);
        mainLabel.setBackground(Color.cyan);
        
        // set texts to screen
        mainLabel.setText("<html>" 
        + "<br/><br/>"
        + "Welcome!<br/><br/>"
        + mainStr[0] 
        + "</html>");
    }
    
    private void menuScreen()
    {
        mainLabel.setForeground(Color.BLUE);
        mainLabel.setBackground(Color.cyan);

        mainLabel.setText("<html>" 
        + "<br/><br/>"
        + "Enter a choice"
        + "<br/><br/>"
        + "1. View my balance<br/>2. Withdraw<br/>3. Deposit<br/>4. Exit"
        + "<br/><br/>"
        + mainStr[2] 
        + "</html>");
    }

    private void balanceScreen()
    {
        mainLabel.setForeground(Color.BLUE);
        mainLabel.setBackground(Color.cyan);

        mainLabel.setText("<html>" 
        + "<br/><br/>"
        + "Here is your account information: " 
        + "<br/><br/>"
        + "Name: " + name 
        + "<br/><br/>"
        + "Balance: $" + balance
        + "<br/><br/>"
        + "Please press Enter to continue." 
        + "</html>");
    }

    private void withdrawScreen()
    {
        mainLabel.setForeground(Color.BLUE);
        mainLabel.setBackground(Color.green);

        mainLabel.setText("<html>" 
        + "<br/><br/>"
        + "Please input an amout to withdraw and press Enter:"
        + "<br/><br/>"
        + mainStr[3]
        + "</html>");
    }

    private void depositScreen()
    {
        mainLabel.setForeground(Color.BLUE);
        mainLabel.setBackground(Color.green);

        mainLabel.setText("<html>" 
        + "<br/><br/>"
        + "Please input an amout to deposit and press Enter:" 
        + "<br/><br/>"
        + mainStr[4]
        + "</html>");
    }

    private void exitScreen()
    {
        mainLabel.setForeground(Color.BLUE);
        mainLabel.setBackground(Color.cyan);
        
        mainLabel.setText("<html>" 
        + "<br/><br/>"
        + "Thank you for using our service!" 
        + "<br/><br/>"
        + "Press Enter to continue the service." 
        + "</html>");
    }

    private void wresultScreen()
    {
        if (balance<0)
        {
            mainLabel.setText("<html>" 
            + "<br/><br/>"
            + "Hello, " 
            + name + ", "
            + "Your current balance is "
            + balance
            + "<br/><br/>"
            + "You have Withdrawn: " 
            + withdraw 
            + "<br/><br/>" 
            + "Please press Enter to continue." 
            + "</html>");
        }
        else
        {
            mainLabel.setText("<html>" 
            + "<br/><br/>"
            + "Hello, " 
            + name + ", "
            + "Your current balance is "
            + accounts.getBalance()
            + "<br/><br/>"
            + "You can not Withdraw: " 
            + withdraw 
            + "<br/><br/>" 
            + "Please press Enter to continue." 
            + "</html>");

        }
        
    }

    private void dresultScreen()
    {
        mainLabel.setText("<html>" 
        + "<br/><br/>"
        + "Hello, " 
        + name + ", "
        + "Your current balance is "
        + balance
        + "<br/><br/>"
        + "You have deposit: " 
        + deposit 
        + "<br/><br/>" 
        + "Please press Enter to continue." 
        + "</html>");
    }

    /**
     * show/refesh the labels of each screen according to the screen user is on
     */
    public void run() 
    {
        while (true)
        {
            switch (phase)
            {
                case ACCOUNT_SCREEN:
                case PASSWORD_SCREEN:
                                        loginScreen();
                                        break;
                case MENU_SCREEN:
                                        menuScreen();
                                        break;
                case BALANCE_SCREEN:
                                        balanceScreen();
                                        break;
                case WITHDRAW_SCREEN:
                                        withdrawScreen();
                                        break;
                case DEPOSIT_SCREEN:
                                        depositScreen();
                                        break;
                case EXIT_SCREEN:
                                        exitScreen();
                                        break;
                case WRESULT_SCREEN:
                                        wresultScreen();
                                        break;
                case DRESULT_SCREEN:
                                        dresultScreen();
                                        break;
                default:
            }
        }
    }
}
