package de.bht.fpa.mailApp.s791739.model;

import de.bht.fpa.mailApp.s791739.model.data.Folder;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import java.io.File;
import java.util.List;

/**
 * Interface for EMail handling
 * @author Marco Kollosche, András Bucsi (FPA Strippgen)
 * @version Aufgabe 4 2014-11-13
 */
public interface EmailManagerIF {

    public void loadEmails( final Folder folder );
    
    public void printMails( );
    
    public void saveEmails( final File file );
    
    public void setCurrentFolder( final Folder folder );
    
    public List<Email> search( final String pattern );
}
