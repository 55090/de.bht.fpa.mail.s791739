package de.bht.fpa.mailApp.s791739.model;

import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;

/*
 * This is the interface for classes that manage
 * folders.
 * 
 * @author Simone Strippgen
 */

public interface FolderManagerIF {

    /**
     * Get current root folder.
     * @return current root folder.
     */
    public Folder getTopFolder();
    
    /**
     * Sets new root folder.
     * @param file 
     */
    public void setTopFolder( final File file );
    
    
    /**
     * Loads all relevant content in the directory path of a folder
     * into the folder.
     * @param folder the folder into which the content of the corresponding 
     *               directory should be loaded
     */
    void loadContent( final Folder folder );     
}
