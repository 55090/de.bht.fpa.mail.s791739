package de.bht.fpa.mailApp.s791739.model.data;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

/*
 * This is the component part of a composite pattern.
 * 
 * @author Simone Strippgen
 */
@Entity
@Inheritance
public abstract class Component implements Serializable{
    // absolute directory pathF to this component
    private String pathF;
    // name of the component (without pathF)
    private String name;
    
    @Id
    @GeneratedValue
    private Long id;

    public Component() {
    }

    public Component( final File path ) {
        this.pathF = path.getAbsolutePath();
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
        return this.name;
    }

    public void setName( final String name ) {
        this.name = name;
    }

    public void setPath( final String p ) {
        this.pathF = p;
    }

    public String getPath() {
        return this.pathF;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}