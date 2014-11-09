package de.bht.fpa.mailApp.s791739.model.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Simone Strippgen
 */
public class Folder extends Component {

    private final ArrayList<Component>    content;
    private final boolean		    expandable;
    
    public Folder(final File path, final boolean expandable) {
        super(path);
        content		= new ArrayList<>();
	this.expandable = expandable;
    }

    @Override
    public void addComponent(final Component comp) {
        content.add(comp);
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    @Override
    public boolean isExpandable() {
        return expandable;
    }    
}