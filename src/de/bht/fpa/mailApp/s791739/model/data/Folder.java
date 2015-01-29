package de.bht.fpa.mailApp.s791739.model.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 *
 * @author Simone Strippgen
 *
 */
@Entity
public class Folder extends Component implements Serializable{

    private boolean expandable;
    
    @Transient
    private transient ArrayList<Component> content;
    
    @Transient
    private ArrayList<Email> emails;
    

    public Folder( final File path, final boolean expandable ) {
        super( path );
        this.expandable = expandable;
        content         = new ArrayList<>();
        emails          = new ArrayList<>();
    }
    
    /**
     * serialization requires a standard constructor
     */
    public Folder(){
    }

    @Override
    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void addComponent( final Component comp ) {
        content.add( comp );
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void addEmail( final Email message ) {
        emails.add( message );
    }
    
    @Override
    public String toString(){
        if(this.emails.size()>0){
            return super.toString()+" ("+emails.size()+")";
        } else {
            return super.toString();
        }
        
    }
 }