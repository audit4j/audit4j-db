/*
 * Copyright (c) 2014-2015 Janith Bandara, This source is a part of
 * Audit4j - An open source auditing framework.
 * http://audit4j.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.audit4j.handler.db;

import java.io.Serializable;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.audit4j.core.exception.HandlerException;
import org.audit4j.core.exception.InitializationException;
import org.audit4j.core.handler.Handler;

/**
 * The Class GeneralDatabaseAuditHandler.
 * 
 * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
 */
public class DatabaseAuditHandler extends Handler implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4994028889410866952L;

    /** The embeded. */
    private String embedded;

    /** The db_driver. */
    private String db_driver;

    /** The db_url. */
    private String db_url;

    /** The db_user. */
    private String db_user;

    /** The db_password. */
    private String db_password;

    /** The db_connection_type. */
    private String db_connection_type;

    /** The db_jndi_datasource. */
    private String db_jndi_datasource;

    /** The Constant POOLED_CONNECTION. */
    private static final String POOLED_CONNECTION = "pooled";

    /** The Constant JNDI_CONNECTION. */
    private static final String JNDI_CONNECTION = "jndi";

    /** The server. */
    private EmbededDBServer server;

    /** The factory. */
    private ConnectionFactory factory;

    /** The log dao. */
    private final AuditLogDao logDao;

    /** The separate. */
    private boolean separate = false;

    /** The data source. */
    private DataSource dataSource;

    /** The table_prefix. */
    private String table_prefix = "";

    /** The table_suffix. */
    private String table_suffix = "audit";

    public DatabaseAuditHandler() {
        logDao = AuditLogDaoImpl.getInstance();
    }

    /**
     * Initialize database handler.
     * 
     * @throws InitializationException
     *             the initialization exception
     */
    @Override
    public void init() throws InitializationException {
        if (null == embedded || "true".equalsIgnoreCase(embedded)) {
            server = HSQLEmbededDBServer.getInstance();
            db_driver = server.getDriver();
            db_url = server.getNetworkProtocol() + "://localhost/audit4j";
            if (db_user == null) {
                db_user = Utils.EMBEDED_DB_USER;
            }
            if (db_password == null) {
                db_password = Utils.EMBEDED_DB_PASSWORD;
            }
            server.setUname(db_user);
            server.setPassword(db_password);
            server.start();
        }

        factory = ConnectionFactory.getInstance();
        factory.setDataSource(dataSource);
        factory.setDriver(getDb_driver());
        factory.setUrl(getDb_url());
        factory.setUser(getDb_user());
        factory.setPassword(getDb_password());

        if (getDb_connection_type() != null && getDb_connection_type().equals(POOLED_CONNECTION)) {
            factory.setConnectionType(ConnectionType.POOLED);
        } else if (getDb_connection_type() != null && getDb_connection_type().equals(JNDI_CONNECTION)) {
            factory.setConnectionType(ConnectionType.JNDI);
            factory.setJndiDataSource(getDb_jndi_datasource());
        } else {
            factory.setConnectionType(ConnectionType.SINGLE);
        }

        factory.init();

        try {
            logDao.createAuditTableIFNotExist("audit");
        } catch (HandlerException e) {
            throw new InitializationException("Unable to create tables", e);
        }

    }

    /**
     * Handle event.
     * 
     * {@inheritDoc}
     * 
     * @see org.audit4j.core.handler.Handler#handle()
     * 
     */
    @Override
    public void handle() throws HandlerException {
        String tag = getAuditEvent().getTag();
        if (separate && tag != null) {
            try {
                logDao.saveEventWithNewTable(getAuditEvent(), table_prefix + "_" + tag + "_" + table_suffix);
            } catch (SQLException e) {
                throw new HandlerException("SQL exception occured while writing the event", DatabaseAuditHandler.class,
                        e);
            }
        } else {
            try {
                logDao.writeEvent(getAuditEvent());
            } catch (SQLException e) {
                throw new HandlerException("SQL exception occured while writing the event", DatabaseAuditHandler.class,
                        e);
            }
        }
    }

    /**
     * Shutdown database handler.
     */
    @Override
    public void stop() {
        server.shutdown();
        factory.stop();
    }

    /**
     * Gets the db_connection_type.
     * 
     * @return the db_connection_type
     */
    public String getDb_connection_type() {
        return db_connection_type;
    }

    /**
     * Sets the db_connection_type.
     * 
     * @param db_connection_type
     *            the new db_connection_type
     */
    public void setDb_connection_type(String db_connection_type) {
        this.db_connection_type = db_connection_type;
    }

    /**
     * Gets the embedded.
     * 
     * @return the embedded
     */
    public String getEmbedded() {
        return embedded;
    }

    /**
     * Sets the embedded.
     * 
     * @param embedded
     *            the new embedded
     */
    public void setEmbedded(String embedded) {
        this.embedded = embedded;
    }

    /**
     * Gets the db_driver.
     * 
     * @return the db_driver
     */
    public String getDb_driver() {
        return db_driver;
    }

    /**
     * Sets the db_driver.
     * 
     * @param db_driver
     *            the new db_driver
     */
    public void setDb_driver(String db_driver) {
        this.db_driver = db_driver;
    }

    /**
     * Gets the db_url.
     * 
     * @return the db_url
     */
    public String getDb_url() {
        return db_url;
    }

    /**
     * Sets the db_url.
     * 
     * @param db_url
     *            the new db_url
     */
    public void setDb_url(String db_url) {
        this.db_url = db_url;
    }

    /**
     * Gets the db_user.
     * 
     * @return the db_user
     */
    public String getDb_user() {
        return db_user;
    }

    /**
     * Sets the db_user.
     * 
     * @param db_user
     *            the new db_user
     */
    public void setDb_user(String db_user) {
        this.db_user = db_user;
    }

    /**
     * Gets the db_password.
     * 
     * @return the db_password
     */
    public String getDb_password() {
        return db_password;
    }

    /**
     * Sets the db_password.
     * 
     * @param db_password
     *            the new db_password
     */
    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    /**
     * Gets the db_jndi_datasource.
     * 
     * @return the db_jndi_datasource
     */
    public String getDb_jndi_datasource() {
        return db_jndi_datasource;
    }

    /**
     * Sets the db_jndi_datasource.
     * 
     * @param db_jndi_datasource
     *            the new db_jndi_datasource
     */
    public void setDb_jndi_datasource(String db_jndi_datasource) {
        this.db_jndi_datasource = db_jndi_datasource;
    }

    /**
     * Sets the separate.
     * 
     * @param separate
     *            the new separate
     */
    public void setSeparate(boolean separate) {
        this.separate = separate;
    }

    /**
     * Sets the data source.
     * 
     * @param dataSource
     *            the new data source
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Sets the table_prefix.
     * 
     * @param table_prefix
     *            the new table_prefix
     */
    public void setTable_prefix(String table_prefix) {
        this.table_prefix = table_prefix;
    }

    /**
     * Sets the table_suffix.
     * 
     * @param table_suffix
     *            the new table_suffix
     */
    public void setTable_suffix(String table_suffix) {
        this.table_suffix = table_suffix;
    }
}
