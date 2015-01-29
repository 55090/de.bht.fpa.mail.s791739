package de.bht.fpa.mailApp.s791739.model.applicationLogic.account;

import de.bht.fpa.mailApp.s791739.model.data.Account;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * This class gets test accounts from TestAccountProvider and saves them
 * into the DB which is specified in the PersistenceUnit. 
 * It can be used to fill a local DB with the test accounts matching the 
 * corresponding account folders.
 * @author Simone Strippgen
 */

public class TestDBDataProvider {

    private static final String TESTDATA_PU = "fpa";
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(TESTDATA_PU);


    public static void createAccounts() {
        final EntityManager em = emf.createEntityManager();
        final List<Account> accs = TestAccountProvider.createAccounts();
        final EntityTransaction trans = em.getTransaction();
        trans.begin();
        for (final Account a : accs) {
            em.persist(a);
        }
        trans.commit();
        em.close();
    }
}