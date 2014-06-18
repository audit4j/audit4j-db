/*
 * Copyright 2014 Janith Bandara, This source is a part of Audit4j - 
 * An open-source audit platform for Enterprise java platform.
 * http://mechanizedspace.com/audit4j
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

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.audit4j.core.exception.InitializationException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * A factory for creating Connection objects.
 */
final class ConnectionFactory {

	static final String SINGLE_CONNECTION = "single";
	
	static final String POOLED_CONNECTION = "pooled";
	
	/** The db_driver. */
	private String driver = "org.hsqldb.jdbcDriver";

	/** The db_url. */
	private String url = "jdbc:hsqldb:hsql://localhost/audit4j";

	/** The db_user. */
	private String user = "audit4juser";

	/** The db_password. */
	private String password = "audit4jpassword";
	

	private String connectionType;
	

	/**
	 * Instantiates a new connection factory.
	 */
	private ConnectionFactory() {
	}

	/** The instance. */
	private static ConnectionFactory instance;

	private static ComboPooledDataSource cpds;

	/**
	 * Gets the connection.
	 * 
	 * @return the connection
	 * 
	 * @deprecated please use getPooledConnection() instead.
	 */
	@Deprecated
	Connection getSingleConnection() {
		Connection connection = null;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new InitializationException("Could not initialize the connection", e);
		} catch (ClassNotFoundException e) {
			throw new InitializationException("Could not find the driver class", e);
		}
		return connection;
	}

	Connection getConnection() {
		if (SINGLE_CONNECTION.equals(connectionType)) {
			return getSingleConnection();
		} else {
			try {
				return  cpds.getConnection();
			} catch (SQLException e) {
				throw new InitializationException("Could not obtain the db connection", e);
			}
		}
	}

	

	/**
	 * Inits the.
	 * 
	 * @return true, if successful
	 * @throws SQLException
	 */
	boolean init() {
		try {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass(this.driver);

			cpds.setJdbcUrl(this.url);
			cpds.setUser(this.user);
			cpds.setPassword(this.password);

		} catch (PropertyVetoException e) {
			throw new InitializationException("Couldn't initialize c3p0 object pool", e);
		}
		return Boolean.TRUE;
	}

	void setDriver(String driver) {
		this.driver = driver;
	}

	void setUrl(String url) {
		this.url = url;
	}

	void setUser(String user) {
		this.user = user;
	}

	void setPassword(String password) {
		this.password = password;
	}

	void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	/**
	 * Gets the single instance of ConnectionFactory.
	 * 
	 * @return single instance of ConnectionFactory
	 */
	static ConnectionFactory getInstance() {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		return instance;
	}
}
