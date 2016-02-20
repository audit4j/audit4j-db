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
import java.sql.SQLException;

import org.audit4j.core.exception.HandlerException;

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
     * Determine database type using database metadata product name.
     *
     * @return the string
     * @throws HandlerException
     *             the handler exception
     */
    protected String determineDatabaseType() throws HandlerException {
        String dbName = null;
        try (Connection conn = getConnection()) {
            dbName = conn.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw new HandlerException("Exception occured while getting DB Name",
                    DatabaseAuditHandler.class, e);
        }
        return dbName;
    }

    /**
     * Checks if is oracle database.
     *
     * @return true, if is oracle database
     * @throws HandlerException
     *             the handler exception
     */
    protected boolean isOracleDatabase() throws HandlerException {
        String dbName = determineDatabaseType();
        if (dbName == null) {
            return false;
        }
        return "Oracle".equalsIgnoreCase(dbName);
    }

    /**
     * Checks if is HSQL database.
     *
     * @return true, if is HSQL database
     * @throws HandlerException
     *             the handler exception
     */
    protected boolean isHSQLDatabase() throws HandlerException {
        String dbName = determineDatabaseType();
        if (dbName == null) {
            return false;
        }
        return "HSQL Database Engine".equalsIgnoreCase(dbName);
    }
}
