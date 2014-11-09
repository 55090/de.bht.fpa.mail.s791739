package de.bht.fpa.mail.s791739.model.appLogic;
import de.bht.fpa.mail.s791739.model.appLogic.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Simone Strippgen
 */
public class Folder extends Component {

    private ArrayList<Component> content;
    private boolean expandable;
    
    public Folder(File path, boolean expandable) {
        super(path);
        content = new ArrayList<Component>();
	this.expandable = expandable;
    }

    @Override
    public void addComponent(Component comp) {
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
