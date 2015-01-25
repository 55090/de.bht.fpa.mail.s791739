package de.bht.fpa.mailApp.s791739.controller;

import de.bht.fpa.mailApp.s791739.model.ApplicationLogicIF;
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

public class CreateAccountController implements Initializable{
    private final ApplicationLogicIF facade;
    /**
     * Indicates Mode 
     * true  -> create account
     * false -> edit account
     */
    private final boolean mode;
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
    
    public CreateAccountController( final ApplicationLogicIF facade ){
        this.facade = facade; 
        account = null;
        mode = false;
    }
    
    public CreateAccountController( final ApplicationLogicIF facade, final Account account ){
        this.facade = facade; 
        this.account = account;
        mode = ( account != null );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ac_b_saveAccount.setOnAction((ActionEvent event) -> handleButtonEvent(event));
        ac_b_cancel.     setOnAction((ActionEvent event) -> handleButtonEvent(event));
        if(mode){
            ac_tf_name.          setText(account.getName());
            ac_tf_host.          setText(account.getHost());
            ac_tf_username.      setText(account.getUsername());
            ac_pf_password.      setText(account.getPassword());
            ac_pf_passwordRepeat.setText(account.getPassword());
        }
    }

    private void handleButtonEvent(ActionEvent event) {
        switch( ( (Button) event.getSource() ).getId() ){
            case "save":    
                if(validateForm()){
                    if(!mode){
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
                    facade.saveAccount(account);
                    close( ac_b_saveAccount.getScene().getWindow() );
                }
                            break;
            case "cancel":  close( ac_b_cancel.getScene().getWindow() );
                            break;
            default:        break;
        }
    }
    
        /**
     * Method closes the View
     * @param window origin view
     */
    private void close( final Window window ) {
        ( (Stage) window ).close();
    }

    /*private void setHeading() {
        ((Stage)(ac_b_cancel.getScene().getWindow())).setTitle(mode?"New Account":"Edit Account");
    }*/

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
            ac_l_error_message.setText(message);
            return true;
        } else {
            ac_l_error_message.setText(message);
            return false;
        }
    }
}
