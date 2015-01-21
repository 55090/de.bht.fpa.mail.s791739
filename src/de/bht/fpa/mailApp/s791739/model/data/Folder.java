package de.bht.fpa.mailApp.s791739.model.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Simone Strippgen
 *
 */
public class Folder extends Component {

    private final boolean expandable;
    private final ArrayList<Component> content;
    private final ArrayList<Email> emails;

    public Folder( final File path, final boolean expandable ) {
        super( path );
        this.expandable = expandable;
        content         = new ArrayList<>();
        emails          = new ArrayList<>();
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
 }