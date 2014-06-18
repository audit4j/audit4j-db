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

import org.audit4j.core.exception.InitializationException;

/**
 * The Class EmbededDBServer.
 *
 * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
 */
public abstract class EmbededDBServer {

	/** The Constant EMBEDED_DB_NAME. */
	static final String EMBEDED_DB_NAME = "audit4j";

	/** The Constant EMBEDED_DB_FILE_NAME. */
	static final String EMBEDED_DB_FILE_NAME = "audit4jdb";
	
	/** The uname. */
	private String uname;

	/** The password. */
	private String password;
	
	/**
	 * Gets the uname.
	 *
	 * @return the uname
	 */
	public String getUname() {
		return uname;
	}

	/**
	 * Sets the uname.
	 *
	 * @param uname the new uname
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Start.
	 *
	 * @throws InitializationException the initialization exception
	 */
	abstract void start() throws InitializationException;
	
	/**
	 * Shutdown.
	 */
	abstract void shutdown();
	
	/**
	 * Gets the driver.
	 *
	 * @return the driver
	 */
	abstract String getDriver();
	
	/**
	 * Gets the network protocol.
	 *
	 * @return the network protocol
	 */
	abstract String getNetworkProtocol();
	
}
