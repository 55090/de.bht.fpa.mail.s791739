package de.bht.fpa.mailApp.s791739.model.applicationLogic;

import de.bht.fpa.mailApp.s791739.model.FolderManagerIF;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;

/**
 * Class manages the composite pattern of files and folders
 * @author Marco Kollosche, András Bucsi (FPA Strippgen)
 * @version Aufgabe 3 2014-11-13
 */
public class FileManager implements FolderManagerIF {

    //top Folder of the managed hierarchy
    protected Folder topFolder;

    /**
     * Constructs a new FileManager object which manages a folder hierarchy, 
     * where file contains the path to the top directory. 
     * The contents of the  directory file are loaded into the top folder
     * @param file File which points to the top directory
     */
    public FileManager( final File file ) {
       topFolder = new Folder( file, true );
    }
    
    /**
     * Loads all relevant content in the directory path of a folder
     * object into the folder.
     * @param f the folder into which the content of the corresponding 
     *          directory should be loaded
     */
    @Override
    public void loadContent( final Folder f ) {
        final File folderPath = new File( f.getPath() );
        if ( hasSubFolders( folderPath ) ) {
            f.getComponents().removeAll( f.getComponents() );
            for( final File currentPath : folderPath.listFiles() ){
                if( currentPath.isDirectory() ){
                    f.addComponent( new Folder( currentPath, hasSubFolders( currentPath ) ) );
                } /*else {
                    f.addComponent( new FileElement( currentPath ) );
                }*/
            }
        }
    }
    
    /**
     * Method indicates whether or not the current directory contains more files/folders
     * @param f_path current path of the directory (type File)
     * @return returns boolean 'true' if the directory contains more files/folders, else 'false'
     */
    private boolean hasSubFolders( final File f_path ){
        File[] files = f_path.listFiles();
        if(files != null){
            for(File f : files){
                if (f.isDirectory()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method returns the root Folder of the current tree
     * @return top Folder
     */
    @Override
    public Folder getTopFolder() {
        return topFolder;
    }

    /**
     * Method sets the top folder
     * @param file path of the new folder
     */
    @Override
    public void setTopFolder(final File file) {
        topFolder.getComponents().removeAll(topFolder.getComponents());
        topFolder = new Folder( file, true );
        
    }
}
