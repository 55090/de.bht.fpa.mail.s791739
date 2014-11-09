package de.bht.fpa.mailApp.s791739.model.appLogic;

import de.bht.fpa.mailApp.s791739.model.data.FileElement;
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
        if ( isExpandable( folderPath ) ) {
            f.getComponents().removeAll( f.getComponents() );
            for( final File currentPath : folderPath.listFiles() ){
                if( currentPath.isDirectory() ){
                    f.addComponent( new Folder( currentPath, isExpandable( currentPath ) ) );
                } else {
                    f.addComponent( new FileElement( currentPath ) );
                }
            }
        }
    }
    
    /**
     * Method indicates whether or not the current directory contains more files/folders
     * @param f_path current path of the directory (type File)
     * @return returns boolean 'true' if the directory contains more files/folders, else 'false'
     */
    private boolean isExpandable( final File f_path ){
        return f_path.listFiles() != null;
    }

    /**
     * Method returns the root Folder of the current tree
     * @return top Folder
     */
    @Override
    public Folder getTopFolder() {
        return topFolder;
    }
}
