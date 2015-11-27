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

    /** The initialized. */
    public static boolean initialized = false;

    /** The audit dao. */
    public static AuditLogDao auditDao;

    /**
     * Instantiates a new audit log dao impl.
     */
    private AuditLogDaoImpl() {
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
     *
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
        StringBuffer query = new StringBuffer();
        query.append("insert into audit(uuid, timestamp, actor, origin, action, elements) ").append(
                "values (?, ?, ?, ?, ?, ?)");

        try (Connection conn = getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
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

    /**
     * {@inheritDoc}
     *
     * @see org.audit4j.handler.db.AuditLogDao#createAuditTableIFNotExist(java.lang.String)
     *
     */
    @Override
    public boolean createAuditTableIFNotExist(String tableName) throws HandlerException {
        StringBuffer query = new StringBuffer("create table if not exists " + tableName + " (");
        query.append("uuid varchar(200) NOT NULL,");
        query.append("timestamp varchar(100) NOT NULL,");
        query.append("actor varchar(200) NOT NULL,");
        query.append("origin varchar(200),");
        query.append("action varchar(200) NOT NULL,");
        query.append("elements varchar(20000)");
        query.append(");");

        try (Connection conn = getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
                return statement.execute();
            }
        } catch (SQLException e) {
            throw new HandlerException("SQL Exception", DatabaseAuditHandler.class, e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see org.audit4j.handler.db.AuditLogDao#saveEventWithNewTable(org.audit4j.core.dto.AuditEvent, java.lang.String)
     *
     */
    @Override
    public boolean saveEventWithNewTable(AuditEvent event, String tableName) throws HandlerException {
        createAuditTableIFNotExist(tableName);
        writeEvent(event);
        return true;
    }

    /**
     * Gets the single instance of AuditLogDaoImpl.
     *
     * @return single instance of AuditLogDaoImpl
     */
    public static AuditLogDao getInstance() {
        synchronized (AuditLogDaoImpl.class) {
            if (!initialized) {
                auditDao = new AuditLogDaoImpl();
                initialized = true;
            }
        }
        return auditDao;
    }
}
