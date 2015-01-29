package de.bht.fpa.mailApp.s791739.model.applicationLogic;

import de.bht.fpa.mailApp.s791739.model.applicationLogic.xml.FolderManager;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.xml.XMLEmailManager;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.account.AccountManager;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.account.AccountManagerIF;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.imap.IMapFolderManager;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.imap.IMapEmailManager;
import de.bht.fpa.mailApp.s791739.model.data.Account;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a facade-pattern for isolation purposes
 * it isolates the applogic algo from the controller as much as needed
 * 
 * @author András Bucsi, Marco Kollosche
 * @version 2014-12-15 Aufgabe 7
 */
public class Facade implements ApplicationLogicIF{

    private       EmailManagerIF    mailManager;
    private       FolderManagerIF   folderManager;
    private final AccountManagerIF  accountManager;
    private       String            type;
    private boolean isRemote;
    
    /**
     * Contructor for ApplicationLogic facade class
     * @param path the initial directory path
     */
    public Facade(final File path){
        this.folderManager    = new FolderManager(path);
        this.mailManager    = new XMLEmailManager();
        this.accountManager = new AccountManager();
        this.isRemote       = false;
        
    }
    
    /**
     * Method returns whether it is remote or local
     * @return 
     */
    public boolean getType(){
        return this.isRemote;
    }
    
    /**
     * Method delegates tasks to MailManager to retrieve top folder
     * @return the top folder
     */
    @Override
    public Folder getTopFolder() {
        return this.folderManager.getTopFolder();
    }

    /**
     * Method delegates tasks to MailManager to add Email items to a specific folder
     * @param folder the specific folder to add the emails to
     */
    @Override
    public void loadContent(Folder folder) {
        this.folderManager.loadContent(folder);
    }

    /**
     * Method delegates tasks to MailManager to retrieve list of emails
     * @return the list with emails
     */
    @Override
    public List<Email> search(String pattern) {
        return this.mailManager.search(pattern);
    }

    /**
     * Method delegates tasks to MailManager to add email to folder
     */
    @Override
    public void loadEmails(Folder folder) {
        this.mailManager.loadEmails(folder);
    }

    /**
     * Method delegates tasks to FolderManager to change top folder
     */
    @Override
    public void changeDirectory(File file) {
        this.folderManager.setTopFolder(file);
        mailManager = new XMLEmailManager();
        isRemote = false;
    }

    /**
     * Method delegates tasks to MailManager to save selected email
     * @param file selected directory path
     */
    @Override
    public void saveEmails(File file) {
        this.mailManager.saveEmails(file);
    }

    @Override
    public void openAccount( final String name ) {
        Account account;
        if (name.contains("Google")){
            account         = this.accountManager.getAccount(name);
            folderManager   = new IMapFolderManager(account);
            mailManager     = new IMapEmailManager(account);
            isRemote        = true;
        } else{
             account = this.accountManager.getAccount(name);
             folderManager = new FolderManager(new File(getAccount(name).getTop().getPath()));
             changeDirectory(new File(account.getTop().getPath()));
        }
    }

    /**
     * Retains all names from the account list and adds it to a String list, which is returned
     * @return String List with account names
     */
    @Override
    public List<String> getAllAccounts() {
        List<String> accountNames = new ArrayList<>();
        accountManager.getAllAccounts().stream().map((Account account) 
                -> account.getName()).filter((String name) 
                        -> (name!=null)).forEach((String name) 
                                -> {accountNames.add(name);});
        return accountNames;
    }

    /**
     * Method gets the account retained by a given String (name)
     * facade delegates the task to the account manager
     * @param name the name to retain the account for
     * @return the account or null if no matches
     */
    @Override
    public Account getAccount( final String name ) {
        return this.accountManager.getAccount(name);
    }

    /**
     * Method saves the current account... it delegates the task to the account manager
     * @param account the current account whis shall be saved
     * @return status of the saving operation (true = success / false = incomplete)
     */
    @Override
    public boolean saveAccount( final Account account ) {
        final boolean saved = this.accountManager.saveAccount( account );
        if(saved){
            System.out.println("Account: "+account.getName()+" was  saved sucessfully!");
        } else {
            System.err.println("Account: "+account.getName()+" could not be saved!");
        }
        return saved;
    }

    /**
     * Method updates a current account with the saved match. It delegates the task 
     * to the account manager
     * @param account the current account, that shall be updated
     */
    @Override
    public void updateAccount( final Account account ) {
        boolean updated = this.accountManager.updateAccount(account);
        if(updated){
            System.out.println("Account: "+account.getName()+" was updated successfully!");
        } else {
            System.err.println("Account: "+account.getName()+" could not be updated!");
        }
    }
}