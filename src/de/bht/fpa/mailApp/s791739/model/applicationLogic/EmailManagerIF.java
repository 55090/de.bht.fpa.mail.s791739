package de.bht.fpa.mailApp.s791739.model.applicationLogic;

import de.bht.fpa.mailApp.s791739.model.data.Folder;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import javafx.collections.ObservableList;

/**
 * Interface for EMail handling
 * @author Marco Kollosche, András Bucsi (FPA Strippgen)
 * @version Aufgabe 4 2014-11-13
 */
public interface EmailManagerIF {

    /**
     *
     * @param folder
     * @return
     */
    public void loadMails( final Folder folder );
    
    /**
     *
     * @param folder
     */
    public void printMails( final Folder folder );
    
    /**
     * 
     * @param folder 
     */
    public ObservableList<Email> listMails( final Folder folder );
}
