package de.bht.fpa.mailApp.s791739.model.data;

/**
 * *****************************************************************************
 * Copyright (c) 2011 - 2012 Siamak Haschemi & Benjamin Haupt All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************
 */
import java.io.File;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * This class represents an IMAP account.
 *
 *
 * @author Siamak Haschemi, changed by Simone Strippgen
 */
@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String hostName;

    private String username;
    
    @Column(unique = true)
    private String password;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Folder top;

    public Account() {
        this.top = new Folder(new File(""), true);
    }

    public Account(String name, String host, String username, String password) {
        this.name = name;
        this.hostName = host;
        this.username = username;
        this.password = password;
        this.top = new Folder(new File(name), true);
    }

    public String getName() {
        return name;
    }

    public void setName(String fullName) {
        this.name = fullName;
    }

    public String getHost() {
        return hostName;
    }

    public void setHost(String host) {
        this.hostName = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[Account: ");
        s.append("name=").append(name).append(" ");
        s.append("host=").append(hostName).append(" ");
        s.append("username=").append(username).append(" ");
        s.append("password=").append(password).append(" ");
        s.append("top folder=(").append(top).append(" ");
        s.append(")");
        s.append("]").append(" ");
        return s.toString();
    }

    public Folder getTop() {
        return top;
    }

    public void setTop(Folder top) {
        this.top = top;
    }

}
