package de.bht.fpa.mailApp.s791739.model.applicationLogic;

import de.bht.fpa.mailApp.s791739.model.ApplicationLogicIF;
import de.bht.fpa.mailApp.s791739.model.EmailManagerIF;
import de.bht.fpa.mailApp.s791739.model.FolderManagerIF;
import de.bht.fpa.mailApp.s791739.model.data.Account;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
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
    
    /**
     * Contructor for ApplicationLogic facade class
     * @param path the initial directory path
     */
    public Facade(final File path){
        fileManager = new FileManager(path);
        mailManager = new EmailManager(fileManager.getTopFolder());
        
    }
    
    /**
     * Method delegates tasks to MailManager to retrieve top folder
     * @return the top folder
     */
    @Override
    public Folder getTopFolder() {
        return fileManager.getTopFolder();
    }

    /**
     * Method delegates tasks to MailManager to add Email items to a specific folder
     * @param folder the specific folder to add the emails to
     */
    @Override
    public void loadContent(Folder folder) {
        fileManager.loadContent(folder);
    }

    /**
     * Method delegates tasks to MailManager to retrieve list of emails
     * @return the list with emails
     */
    @Override
    public List<Email> search(String pattern) {
        return mailManager.search(pattern);
    }

    /**
     * Method delegates tasks to MailManager to add email to folder
     */
    @Override
    public void loadEmails(Folder folder) {
        mailManager.loadEmails(folder);
    }

    /**
     * Method delegates tasks to FileManager to change top folder
     */
    @Override
    public void changeDirectory(File file) {
        fileManager.setTopFolder(file);
    }

    /**
     * Method delegates tasks to MailManager to save selected email
     * @param file selected directory path
     */
    @Override
    public void saveEmails(File file) {
        mailManager.saveEmails(file);
    }

    @Override
    public void openAccount(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getAllAccounts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Account getAccount(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean saveAccount(Account account) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateAccount(Account account) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}