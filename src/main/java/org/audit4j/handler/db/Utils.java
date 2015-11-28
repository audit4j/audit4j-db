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

/**
 * The Class Utils.
 *
 * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
 */
public final class Utils {

    /**
     * The Constant EMBEDED_DB_NAME.
     */
    static final String EMBEDED_DB_NAME = "audit4j";

    /**
     * The Constant EMBEDED_DB_USER.
     */
    static final String EMBEDED_DB_USER = "audit4jdbuser";

    /**
     * The Constant EMBEDED_DB_PASSWORD.
     */
    static final String EMBEDED_DB_PASSWORD = "audit4jdbpassword";

    private Utils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the dB name.
     *
     * @param userName the user name
     * @return the dB name
     */
    public static String getDBName(String userName) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(EMBEDED_DB_NAME);
        buffer.append("@");
        buffer.append(userName);
        return buffer.toString();
    }

    /**
     * Throws a NullPointerException if given parameter is <code>null</code>
     *
     * @param t the parameter to check
     * @return the provided parameter
     */
    public static <T> T checkNotNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    /**
     * Throws a IllegalArgumentException if given String is empty
     *
     * @param str     the parameter to check
     * @param message the parameter to check
     * @return the provided parameter
     */
    public static String checkNotEmpty(String str, String message) {
        if (checkNotNull(str).isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return str;
    }
}
