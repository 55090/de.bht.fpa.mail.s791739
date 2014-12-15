package de.bht.fpa.mailApp.s791739.model;

import de.bht.fpa.mailApp.s791739.model.data.Folder;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import java.io.File;
import javafx.collections.ObservableList;

/**
 * Interface for EMail handling
 * @author Marco Kollosche, András Bucsi (FPA Strippgen)
 * @version Aufgabe 4 2014-11-13
 */
public interface EmailManagerIF {

    public void loadEmails( final Folder folder );
    
    public void printMails( final Folder folder );
    
    public void updateEmailList( final Folder folder );
    
    public void updateSearchList( final String pattern );
    
    public ObservableList<Email> getEmailList();
    
    public ObservableList<Email> getSearchList();
    
    public void saveEmails( final File file );
    
    public void setSelectedTableViewItem(final Email selectedItem);
    
    public Email getSelectedTableViewItem();
}
