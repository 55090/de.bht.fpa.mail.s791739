package de.bht.fpa.mailApp.s791739.model.applicationLogic.account;


import de.bht.fpa.mailApp.s791739.model.data.Account;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class defines a dao class, that stores Account objects as
 * serialized data in a given file.
 * @author Simone Strippgen
 */
public class AccountFileDAO implements AccountDAOIF {

    public static final File ACCOUNT_FILE = new File(TestAccountProvider.TESTDATA_HOME, "Account.ser");

    public AccountFileDAO() {
        //for testing: serializes test accounts
        if (!ACCOUNT_FILE.exists()) {
            saveAccounts(TestAccountProvider.createAccounts());
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        try {
            Account acc;
            FileInputStream fileInput = new FileInputStream(ACCOUNT_FILE);
            try (ObjectInputStream is = new ObjectInputStream(fileInput)) {
                do {
                   acc = (Account) is.readObject();
                   list.add(acc);
                }while(acc != null && acc.getName()!=null);
            }
        } catch (IOException | ClassNotFoundException ex) {
            //System.err.println(ex.getMessage());
        }
        return list;
    }

    @Override
    public Account saveAccount(Account acc) {
        if (acc != null) {
            List<Account> accounts = getAllAccounts();
            accounts.add(acc);
            saveAccounts(accounts);
        }
        return acc;
    }

    @Override
    public boolean updateAccount(Account acc) {
        if (acc != null) {
            List<Account> accounts = getAllAccounts();
            remove(acc.getName(), accounts);
            accounts.add(acc);
            saveAccounts(accounts);
        }
        return true;
    }

    private void saveAccounts(List<Account> accList) {
        try {
            File accountFile = ACCOUNT_FILE;
            boolean deleted = accountFile.delete();
            FileOutputStream fileOutput = new FileOutputStream(accountFile);
            try (ObjectOutputStream os = new ObjectOutputStream(fileOutput)) {
                for (Account a : accList) {
                    os.writeObject(a);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void remove(String name, List<Account> list) {
        Iterator<Account> it = list.iterator();
        while (it.hasNext()) {
            Account acc = it.next();
            if (acc.getName().equals(name)) {
                it.remove();
                return;
            }
        }
    }
}
