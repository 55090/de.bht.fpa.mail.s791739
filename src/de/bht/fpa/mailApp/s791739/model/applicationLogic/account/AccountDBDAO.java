package de.bht.fpa.mailApp.s791739.model.applicationLogic.account;


import de.bht.fpa.mailApp.s791739.model.data.Account;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Class for Account Handling DB
 * @author Marco Kollosche, András Bucsi (FPA Strippgen) Gruppe 4
 * @version Aufgabe 9 2015-01-29
 */
public class AccountDBDAO implements AccountDAOIF {
    private final EntityManagerFactory entityManagerFactory;

    public AccountDBDAO() {
        TestDBDataProvider.createAccounts();
        entityManagerFactory = Persistence.createEntityManagerFactory("fpa");
    }

    /**
     * Method starts query via the persistence unit and retains all accounts 
     * stored in the database
     * 
     * @return a list of all retained accounts 
     */
    @Override
    public List<Account> getAllAccounts() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final List<Account> accounts = (entityManager.createQuery("SELECT accounts FROM Account accounts")).getResultList();
        entityManager.close();
        return accounts;
    }

    /**
     * Method saves the current account to the database via the PU
     * 
     * @param account current account to be saved
     * @return the saved account (weird but yes...)
     */
    @Override
    public Account saveAccount( final Account account ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            entityManager.persist( account );
            transaction.  commit();
        } catch ( final IllegalArgumentException iae ) {
            Logger.getLogger(AccountDBDAO.class.getName()).log(Level.SEVERE, null, iae);
        } finally {
            entityManager.close();
        }
        return account;
    }

    /**
     * Method updates an altered account to the database via PU
     * @param acc the current altered account
     * @return boolean value (true = successful / false = incomplete)
     */
    @Override
    public boolean updateAccount( final Account acc ) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            entityManager.merge( acc );
            transaction.commit();
        } catch ( final IllegalArgumentException iae ) {
            Logger.getLogger(AccountDBDAO.class.getName()).log(Level.SEVERE, null, iae);
            return false;
        } finally {
            entityManager.close();
        }
        return true;
    }
}

