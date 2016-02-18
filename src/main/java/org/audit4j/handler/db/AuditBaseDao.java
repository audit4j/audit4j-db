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

import java.sql.Connection;

/**
 * The Class AuditBaseDao.
 *
 * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
 */
class AuditBaseDao {

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	protected Connection getConnection() {
		ConnectionFactory factory = ConnectionFactory.getInstance();
		return factory.getConnection();
	}
	
	/**
	 * Check if the database is oracle
	 * 
	 * @return true if database is oracle.
	 */
	protected boolean isOracleDatabase() {
		ConnectionFactory factory = ConnectionFactory.getInstance();
		Connection connection = factory.getConnection();
		return connection.getClass().getName().contains("oracle");
	}
}
