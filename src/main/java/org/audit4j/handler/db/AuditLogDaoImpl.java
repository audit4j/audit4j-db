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
import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.audit4j.core.dto.AuditEvent;
import org.audit4j.core.dto.Field;
import org.audit4j.core.exception.HandlerException;

/**
 * This class is used to create audit tables and submit audit events to JDBC
 * supported data stores.
 *
 * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
 * @author Thebora Kompanioni https://github.com/theborakompanioni
 * @author Kettner, MA (ITOPT2) - KLM <Mark.Kettner@klm.com> - For Oracle
 *         Support
 */
final class AuditLogDaoImpl extends AuditBaseDao implements AuditLogDao {

  /** The table name. */
  private final String tableName;

  /** The schema name. */
  private final String schemaName;

  /** The insert query. */
  private final String insertQuery;

  /**
   * Instantiates a new audit log dao.
   *
   * @param tableName
   *            given through the constructor to create table.
   * @throws HandlerException
   *             the handler exception
   */
  AuditLogDaoImpl(String tableName, String schema) throws HandlerException {
    this.tableName = checkNotEmpty(tableName, "Table name must not be empty");
    this.schemaName = schema;
    StringBuilder insertQueryBuilder = new StringBuilder();
    insertQueryBuilder.append("insert into ");
    if(!StringUtils.isEmpty(schema)){
      insertQueryBuilder.append(schema).append(".");
    }
    insertQueryBuilder.append(tableName)
      .append("(identifier, timestamp, actor, origin, action, elements) values (?, ?, ?, ?, ?, ?)");
    this.insertQuery = insertQueryBuilder.toString();

    createTableIfNotExists();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.audit4j.handler.db.AuditLogDao#writeEvent(org.audit4j.core.dto.AuditEvent)
   */
  @Override
  public boolean writeEvent(AuditEvent event) throws HandlerException {
    StringBuilder elements = new StringBuilder();
    for (Field element : event.getFields()) {
      elements.append(
        element.getName() + " " + element.getType() + ":" + element.getValue() + ", ");
    }

    try (Connection conn = getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
        statement.setString(1, event.getUuid().toString());
        statement.setTimestamp(2, new Timestamp(event.getTimestamp().getTime()));
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
   * Creates the table in the database based on the table name given through
   * constructor. This supports to different databases including Oracle,
   * MySQL, Postgress and HSQLDB.
   *
   * @return true, if successful
   * @throws HandlerException
   *             the handler exception
   */
  private boolean createTableIfNotExists() throws HandlerException {
    boolean result = false;
    try (Connection conn = getConnection()) {
      StringBuilder query = new StringBuilder();
      String tableNameWithSchema = (!StringUtils.isEmpty(schemaName)) ? (schemaName + "." + tableName) : tableName;

      if (isOracleDatabase()) {
        // Create table if Oracle Database
        //String values[] = tableName.split("\\.");
        query.append("select count(*) from all_tables where table_name = upper('")
          .append(tableName).append("')");
        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
          result = statement.execute();
        }
        if (result == false) {
          query.append("create table ").append(tableNameWithSchema).append(" (")
            .append("identifier VARCHAR2(200) NOT NULL,")
            .append("timestamp TIMESTAMP NOT NULL,")
            .append("actor VARCHAR2(200) NOT NULL,").append("origin VARCHAR2(200),")
            .append("action VARCHAR2(200) NOT NULL,").append("elements CLOB")
            .append(");");
        }
      } else if (isHSQLDatabase()) {
        // Create Table if HSQLDB database
        query.append("create table if not exists ").append(tableNameWithSchema).append(" (")
          .append("identifier VARCHAR(200) NOT NULL,")
          .append("timestamp TIMESTAMP NOT NULL,")
          .append("actor VARCHAR(200) NOT NULL,").append("origin VARCHAR(200),")
          .append("action VARCHAR(200) NOT NULL,").append("elements LONGVARCHAR")
          .append(");");
        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
          result = statement.execute();
        }
      } else if (isMySQLDatabase()) {
        // Create table if MySQL database
        query.append("create table if not exists ").append(tableNameWithSchema).append(" (")
          .append("identifier VARCHAR(200) NOT NULL,")
          .append("timestamp TIMESTAMP NOT NULL,")
          .append("actor VARCHAR(200) NOT NULL,").append("origin VARCHAR(200),")
          .append("action VARCHAR(200) NOT NULL,").append("elements TEXT")
          .append(");");
        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
          result = statement.execute();
        }
      } else if (isSQLServerDatabase()) {
        // Create table if SQLServer database
        query.append(" IF OBJECT_ID(N'"+tableNameWithSchema+"', N'U') IS NULL BEGIN ");
        query.append("create table ").append(tableNameWithSchema).append(" (")
          .append("identifier VARCHAR(200) NOT NULL,")
          .append("timestamp DATETIME NOT NULL,")
          .append("actor VARCHAR(200) NOT NULL,").append("origin VARCHAR(200),")
          .append("action VARCHAR(200) NOT NULL,").append("elements TEXT")
          .append(");");
        query.append(" END ");
        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
          result = statement.execute();
        }
      } else {
        query.append("create table if not exists ").append(tableNameWithSchema).append(" (")
          .append("identifier VARCHAR(200) NOT NULL,")
          .append("timestamp TIMESTAMP NOT NULL,")
          .append("actor VARCHAR(200) NOT NULL,").append("origin VARCHAR(200),")
          .append("action VARCHAR(200) NOT NULL,").append("elements VARCHAR(70000)")
          .append(");");
        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
          result = statement.execute();
        }
      }
      return result;
    } catch (SQLException e) {
      throw new HandlerException("SQL Exception", DatabaseAuditHandler.class, e);
    }
  }
}
