/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mailApp.s791739.model.applicationLogic.account;

import de.bht.fpa.mailApp.s791739.model.data.Account;
import java.util.List;

/**
 *
 * @author Kobe
 */
public class AccountManager implements AccountManagerIF{
    private final AccountDAOIF dao;
    
    public AccountManager(){
        this.dao = new AccountFileDAO();
    }

    @Override
    public Account getAccount( final String name ) {
        for( final Account account : this.dao.getAllAccounts() ){
            if( account.getName().equals( name ) ){
                return account;
            }
        }
        return null;
    }

    @Override
    public List<Account> getAllAccounts() {
        return this.dao.getAllAccounts();
    }

    @Override
    public boolean saveAccount( final Account acc ) {
        return acc.equals( this.dao.saveAccount( acc ) );
    }

    @Override
    public boolean updateAccount( final Account account ) {
        return this.dao.updateAccount( account );
    }
    
}
