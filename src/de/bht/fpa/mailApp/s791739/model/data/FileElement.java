package de.bht.fpa.mailApp.s791739.model.data;

import java.io.File;

/*
 * This is the leaf part of a composite pattern.
 * 
 * @author Simone Strippgen
 */
public class FileElement extends Component {

    public FileElement(File path) {
        super(path);
    }

    @Override
    public boolean isExpandable() {
        return false;
    }
    
}
