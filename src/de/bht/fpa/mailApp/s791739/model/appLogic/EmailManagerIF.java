package de.bht.fpa.mailApp.s791739.model.appLogic;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
/**
* Interface for EMail handling
* @author Marco Kollosche, András Bucsi (FPA Strippgen)
* @version Aufgabe 4 2014-11-13
*/
public interface EmailManagerIF {
/**
*
* @param folder
* @return
*/
public Folder loadMails(final Folder folder);
/**
*
* @param folder
*/
public void printMails(final Folder folder);
}