package de.bht.fpa.mailApp.s791739.controller;

import de.bht.fpa.mailApp.s791739.model.data.Account;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.Stage;

/**
 * Controller Class for FXMLDocument
 * @author Marco Kollosche, András Bucsi (FPA Strippgen) Gruppe 4
 * @version Aufgabe 8 2015-01-29
 */
public class CreateAccountController implements Initializable{
    private final MainViewController mVCtrl;
    /**
     * Indicates Mode of Create/Edit
     * true  -> create account
     * false -> edit account
     */
    private final boolean newAccount;
    
    private Account account;

    @FXML
    TextField ac_tf_name,
              ac_tf_host,
              ac_tf_username;
    
    @FXML
    PasswordField ac_pf_password,
                  ac_pf_passwordRepeat;
    
    @FXML
    Label ac_l_error_message;
    
    @FXML
    Button ac_b_saveAccount, 
           ac_b_cancel;
    
    /**
     * Contructor for new account setups
     * @param mVCtrl MainViewController (because it wants to know everything)
     */
    public CreateAccountController( final  MainViewController mVCtrl ){
        this.mVCtrl = mVCtrl; 
        account     = null;
        newAccount  = true;
    }
    
    /**
     * Constructor for update actions
     * @param mVCtrl MainViewController (because it wants to know everything)
     * @param account account that needs to be updated
     */
    public CreateAccountController( final MainViewController mVCtrl, final Account account ){
        this.mVCtrl     = mVCtrl; 
        this.account    = account;
        newAccount      = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ac_b_saveAccount.setOnAction((ActionEvent event) -> handleButtonEvent(event));
        if (!newAccount){
            ac_b_saveAccount.setText("update");
        } else {
            ac_b_saveAccount.setText("save");
        }
        ac_b_cancel.     setOnAction((ActionEvent event) -> handleButtonEvent(event));
        if(!newAccount){
            ac_tf_name.          setText(account.getName());
            ac_tf_name.setEditable(false);
            ac_tf_host.          setText(account.getHost());
            ac_tf_username.      setText(account.getUsername());
            ac_pf_password.      setText(account.getPassword());
            ac_pf_passwordRepeat.setText(account.getPassword());
        }
    }

    /**
     * Method is a callback method for the button events from the view
     * @param event event from the button click
     */
    private void handleButtonEvent(ActionEvent event) {
        switch( ( (Button) event.getSource() ).getId() ){
            case "save":    
                if(validateForm()){
                    if(newAccount){
                        account = new Account();
                    }
                    final String name     = ac_tf_name.getText(); 
                    final String host     = ac_tf_host.getText(); 
                    final String username = ac_tf_username.getText(); 
                    final String password = ac_pf_password.getText();
                    account.setName(name);
                    account.setHost(host);
                    account.setUsername(username);
                    account.setPassword(password);
                    if(newAccount){
                        mVCtrl.saveAccount(account);
                    } else {
                        mVCtrl.updateAccount(account);
                    }
                    close( ac_b_saveAccount.getScene().getWindow() );
                }
                            break;
            case "cancel":  close( ac_b_cancel.getScene().getWindow() );
                            break;
            default:        break;
        }
    }

    /**
     * Method validates all fields, as well as it checks whether the name entered 
     * is already taken by another account
     * @return boolean value (true = valid / false = invalid) 
     */
    private boolean validateForm() {
        String message ="";
        if(ac_tf_name.getText().length()<1){
            message = message + "Name is missing."; 
            ac_tf_name.setStyle("-fx-background-color: #ffdddd;");
        } else {
            ac_tf_name.setStyle("-fx-background-color: #fff;");
        }
        if(ac_tf_host.getText().length()<1){
            message = message + " Host is missing.";
            ac_tf_host.setStyle("-fx-background-color: #ffdddd;");
        } else {
            ac_tf_host.setStyle("-fx-background-color: #fff;");
        }
        if(ac_tf_username.getText().length()<1){
            message = message + " Username is missing.";
            ac_tf_username.setStyle("-fx-background-color: #ffdddd;");
        } else {
            ac_tf_username.setStyle("-fx-background-color: #fff;");
        }
        if(ac_pf_password.getText().length()<1){
            message = message + " Password is missing.";
            ac_pf_password.setStyle("-fx-background-color: #ffdddd;");
        } else {
            ac_pf_password.      setStyle("-fx-background-color: #fff;");
        }
        if(ac_pf_passwordRepeat.getText().length()<1){
            message = message + " Repeated password is missing.";
            ac_pf_passwordRepeat.setStyle("-fx-background-color: #ffdddd;");
        } else {
            ac_pf_passwordRepeat.setStyle("-fx-background-color: #fff;");
        }
        if( ( ac_pf_passwordRepeat.getText().length()>0 || ac_pf_password.getText().length()>0 ) && ( ac_pf_passwordRepeat.getText().equals( ac_pf_password.getText() ) ) ){
            ac_pf_passwordRepeat.setStyle("-fx-background-color: #fff;");
            ac_pf_password.      setStyle("-fx-background-color: #fff;");
        } else {
            message = message + " Passwords not matching.";
            ac_pf_password.      setStyle("-fx-background-color: #ffdddd;");
            ac_pf_passwordRepeat.setStyle("-fx-background-color: #ffdddd;");
        }
        if (message.length()<1){
            if( mVCtrl.accountNameTaken( ac_tf_name.getText() ) &&  account == null ){
                message = message + "Name already taken. Choose different name.";
                ac_l_error_message.setText(message);
                return false;
            } else {
                return true;
            }
        } else {
            ac_l_error_message.setText(message);
            return false;
        }
    }
    
    /**
     * Method closes the View
     * @param window origin view
     */
    private void close( final Window window ) {
        ( (Stage) window ).close();
    }
}
