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
import org.hsqldb.Server;

/**
 * The Class HSQLEmbededDBServer.
 * 
 * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
 */
class HSQLEmbededDBServer extends EmbededDBServer {

    /** The Constant driver. */
    private static final String driver = "org.hsqldb.jdbcDriver";

    private static final String networkProtol = "jdbc:hsqldb:hsql";
    /** The instance. */
    public static HSQLEmbededDBServer instance;

    /** The hsql server. */
    private Server hsqlServer = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.audit4j.core.handler.db.EmbededDBServer#start()
     */
    @Override
    void start() throws InitializationException {
        if (hsqlServer == null) {
            hsqlServer = new Server();
            hsqlServer.setLogWriter(null);
            hsqlServer.setSilent(true);
            hsqlServer.setDatabaseName(0, EMBEDED_DB_NAME);
            hsqlServer.setDatabasePath(0, "file:" + EMBEDED_DB_FILE_NAME + ";user=" + getUname() + ";password="
                    + getPassword() + "");
            hsqlServer.start();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.audit4j.core.handler.db.EmbededDBServer#shutdown()
     */
    @Override
    void shutdown() {
        hsqlServer.stop();
        hsqlServer.shutdown();
        hsqlServer = null;
    }

    /**
     * Gets the single instance of HSQLEmbededDBServer.
     * 
     * @return single instance of HSQLEmbededDBServer
     */
    static EmbededDBServer getInstance() {
        if (instance == null) {
            instance = new HSQLEmbededDBServer();
            return instance;
        }
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.audit4j.core.handler.db.EmbededDBServer#getDriver()
     */
    @Override
    String getDriver() {
        return driver;
    }

    @Override
    String getNetworkProtocol() {
        return networkProtol;
    }
}
