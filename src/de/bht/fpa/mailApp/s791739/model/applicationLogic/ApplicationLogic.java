/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mailApp.s791739.model.applicationLogic;

import de.bht.fpa.mailApp.s791739.model.ApplicationLogicIF;
import de.bht.fpa.mailApp.s791739.model.EmailManagerIF;
import de.bht.fpa.mailApp.s791739.model.FolderManagerIF;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.util.List;
import javafx.collections.ObservableList;

/**
 * Class represents a facade-pattern for isolation purposes
 * it isolates the applogic algo from the controller as much as needed
 * 
 * @author András Bucsi, Marco Kollosche
 * @version 2014-12-15 Aufgabe 7
 */
public class ApplicationLogic implements ApplicationLogicIF, EmailManagerIF{

    private final EmailManagerIF mailManager;
    private final FolderManagerIF fileManager;
    
    /**
     * Contructor for ApplicationLogic facade class
     * @param path the initial directory path
     */
    public ApplicationLogic(final File path){
        fileManager = new FileManager(path);
        mailManager = new XMLEmailManager(fileManager.getTopFolder());
        
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
        mailManager.updateSearchList(pattern);
        return mailManager.getSearchList();
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

    // Interface EmaiManagerIF implementations
    /**
     * Method delegates tasks to MailManager to update list with found emails in current folder
     * @param pattern to search for in list
     */
    @Override
    public void updateSearchList(String pattern) {
        mailManager.updateSearchList(pattern);
    }

    /**
     * Method delegates tasks to MailManager to update list with emails in current folder
     * @param folder new folder to retreive emails from
     */
    @Override
    public void updateEmailList(Folder folder) {
        mailManager.updateEmailList(folder);
    }
    
    /**
     * Method delegates tasks to MailManager to retrieve searchList
     * @return the observable list
     */
    @Override
    public ObservableList<Email> getSearchList() {
        return mailManager.getSearchList();
    }
    
    /**
     * Method delegates tasks to MailManager to retrieve full emailList
     * @return the observable list
     */
    @Override
    public ObservableList<Email> getEmailList() {
        return mailManager.getEmailList();
    }
    
    /**
     * Method implementation at this point is unused
     * @param folder unused
     */
    @Override
    public void printMails(Folder folder) {
        // unused
    }

    /**
     * Method delegates tasks to MailManager to set the currently selected email from table view
     * @param selectedItem currently selected email
     */
    @Override
    public void setSelectedTableViewItem(Email selectedItem) {
        mailManager.setSelectedTableViewItem(selectedItem);
    }

    /**
     * Method delegates tasks to MailManager to return the currently selected email
     * @return currently selected email
     */
    @Override
    public Email getSelectedTableViewItem() {
        return mailManager.getSelectedTableViewItem();
    }
}