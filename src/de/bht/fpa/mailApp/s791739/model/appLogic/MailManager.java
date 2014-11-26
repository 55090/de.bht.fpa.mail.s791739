/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mailApp.s791739.model.appLogic;

import de.bht.fpa.mailApp.s791739.model.data.Email;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.util.List;
import javax.xml.bind.JAXB;

/**
 *
 * @author Kobe
 */
public class MailManager implements EmailManagerIF{

    @Override
    public Folder loadMails(Folder folder) {
        if (!folder.getEmails().isEmpty()){
            return folder;
        }
        for (final File filePath : new File( folder.getPath()).listFiles() ){
            if(filePath.getName().endsWith(".xml")){
                folder.addEmail(JAXB.unmarshal(filePath, Email.class));
            }
        }
        return folder;
    }

    @Override
    public void printMails(Folder folder) {
        List<Email> mails = folder.getEmails();
        System.out.println("Selected directory" +folder.getPath() );
        System.out.println("Number of details" + mails.size() );
        mails.stream().forEach((Email email_item) ->  {
            System.out.printf("[Email: sender=%s received=%s subject=%s] %n", email_item.getSender() ,
email_item.getReceived() ,
email_item.getSubject() );
});
       
    }
    
}
