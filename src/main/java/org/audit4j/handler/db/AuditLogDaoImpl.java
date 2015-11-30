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

import static org.audit4j.handler.db.Utils.checkNotEmpty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.audit4j.core.dto.AuditEvent;
import org.audit4j.core.dto.Field;
import org.audit4j.core.exception.HandlerException;

/**
 * The Class HSQLAuditLogDao.
 *
 * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
 */
final class AuditLogDaoImpl extends AuditBaseDao implements AuditLogDao {
    private final String tableName;
    private final String insertQuery;

    AuditLogDaoImpl(String tableName) throws HandlerException {
        this.tableName = checkNotEmpty(tableName, "Table name must not be empty");
        this.insertQuery = "insert into " + tableName +
                "(uuid, timestamp, actor, origin, action, elements) " +
                "values (?, ?, ?, ?, ?, ?)";

        createTableIfNotExists();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.audit4j.core.handler.db.AuditLogDao#createEvent(org.audit4j.core.
     * dto.AuditEvent)
     */

    /**
     * {@inheritDoc}
     *
     * @see org.audit4j.handler.db.AuditLogDao#writeEvent(org.audit4j.core.dto.AuditEvent)
     */
    @Override
    public boolean writeEvent(AuditEvent event) throws HandlerException {
        String uuid;
        String timestamp;
        StringBuilder elements = new StringBuilder();

        if (event.getUuid() == null) {
            uuid = String.valueOf(UUID.randomUUID().getMostSignificantBits());
        } else {
            uuid = event.getUuid().toString();
        }

        if (event.getTimestamp() == null) {
            timestamp = new Date().toString();
        } else {
            timestamp = event.getTimestamp().toString();
        }

        for (Field element : event.getFields()) {
            elements.append(element.getName() + " " + element.getType() + ":" + element.getValue() + ", ");
        }

        try (Connection conn = getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
                statement.setString(1, uuid);
                statement.setString(2, timestamp);
                statement.setString(3, event.getActor());
                statement.setString(4, event.getOrigin());
                statement.setString(5, event.getAction());
                statement.setString(6, elements.toString());

                return statement.execute();
            }
        } catch (SQLException e) {
            throw new HandlerException("SQL Exception", DatabaseAuditHandler.class, e);
        }
    }

    private boolean createTableIfNotExists() throws HandlerException {
        try (Connection conn = getConnection()) {
            StringBuffer query = new StringBuffer("create table if not exists ")
                    .append(tableName).append(" (")
                    .append("uuid varchar(200) NOT NULL,")
                    .append("timestamp varchar(100) NOT NULL,")
                    .append("actor varchar(200) NOT NULL,")
                    .append("origin varchar(200),")
                    .append("action varchar(200) NOT NULL,")
                    .append("elements varchar(20000)")
                    .append(");");

            try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
                return statement.execute();
            }
        } catch (SQLException e) {
            throw new HandlerException("SQL Exception", DatabaseAuditHandler.class, e);
        }
    }
}
