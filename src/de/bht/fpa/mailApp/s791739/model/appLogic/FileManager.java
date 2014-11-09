package de.bht.fpa.mailApp.s791739.model.appLogic;

import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;

public class FileManager implements FolderManagerIF {

    //top Folder of the managed hierarchy
    Folder topFolder;

    /**
     * Constructs a new FileManager object which manages a folder hierarchy, 
     * where file contains the path to the top directory. 
     * The contents of the  directory file are loaded into the top folder
     * @param file File which points to the top directory
     */
    public FileManager(File file) {
       topFolder = new Folder(file, true);
    }
    
    /**
     * Loads all relevant content in the directory path of a folder
     * object into the folder.
     * @param f the folder into which the content of the corresponding 
     *          directory should be loaded
     */
    @Override
    public void loadContent(Folder f) {
        // hier kommt Ihr Code hin.
    }

    @Override
    public Folder getTopFolder() {
        return topFolder;
    }
}
