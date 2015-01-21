package de.bht.fpa.mailApp.s791739.model.data;
import java.io.File;
import java.util.List;

/*
 * This is the component part of a composite pattern.
 * 
 * @author Simone Strippgen
 */
public abstract class Component {
    // absolute directory path to this component
    private String path;
    // name of the component (without path)
    private String name;

    public Component( final File path ) {
        this.path = path.getAbsolutePath();
        this.name = path.getName();
    }

    public void addComponent( final Component comp ) {
        throw new UnsupportedOperationException( "Not supported." );
    }

    public List<Component> getComponents() {
        throw new UnsupportedOperationException( "Not supported." );
    }
    
    /**
     * Abstract method signature that must be implemented when a class is derived from Component
     * @return is the component expandable
     */
    public abstract boolean isExpandable();

    /**
     * 
     * @return 
     */
    public String getName() {
        return name;
    }

    public void setName( final String name ) {
        this.name = name;
    }

    public void setPath( final String p ) {
        path = p;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name;
        // hier (Zahl) einfügen 
        // name wird dadurch nicht verändert...
    }
}