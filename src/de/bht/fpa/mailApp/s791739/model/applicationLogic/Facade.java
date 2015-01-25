package de.bht.fpa.mailApp.s791739.model.applicationLogic;

import de.bht.fpa.mailApp.s791739.model.ApplicationLogicIF;
import de.bht.fpa.mailApp.s791739.model.EmailManagerIF;
import de.bht.fpa.mailApp.s791739.model.FolderManagerIF;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.account.AccountManager;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.account.AccountManagerIF;
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

    private final EmailManagerIF  mailManager;
    private final FolderManagerIF fileManager;
    private final AccountManagerIF accountManager;
    
    /**
     * Contructor for ApplicationLogic facade class
     * @param path the initial directory path
     */
    public Facade(final File path){
        this.fileManager = new FileManager(path);
        this.mailManager = new EmailManager(this.fileManager.getTopFolder());
        this.accountManager = new AccountManager();
        
    }
    
    /**
     * Method delegates tasks to MailManager to retrieve top folder
     * @return the top folder
     */
    @Override
    public Folder getTopFolder() {
        return this.fileManager.getTopFolder();
    }

    /**
     * Method delegates tasks to MailManager to add Email items to a specific folder
     * @param folder the specific folder to add the emails to
     */
    @Override
    public void loadContent(Folder folder) {
        this.fileManager.loadContent(folder);
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
     * Method delegates tasks to FileManager to change top folder
     */
    @Override
    public void changeDirectory(File file) {
        this.fileManager.setTopFolder(file);
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
        Account account = this.accountManager.getAccount(name);
        changeDirectory(new File(account.getTop().getPath()));
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

    @Override
    public Account getAccount( final String name ) {
        return this.accountManager.getAccount(name);
    }

    @Override
    public boolean saveAccount( final Account account ) {
        return this.accountManager.saveAccount( account );
    }

    @Override
    public void updateAccount( final Account account ) {
        boolean updated = this.accountManager.updateAccount(account);
        if(updated){
            System.out.println("Account: "+account.getName()+" was updated successfully!");
        } else {
            System.out.println("Account: "+account.getName()+" could not be updated!");
        }
    }
}