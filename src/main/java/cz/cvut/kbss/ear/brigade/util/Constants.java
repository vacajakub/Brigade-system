/**
 * Copyright (C) 2016 Czech Technical University in Prague
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.ear.brigade.util;

import cz.cvut.kbss.ear.brigade.model.Role;

public final class Constants {

    /**
     * UTF-8 encoding identifier.
     */
    public static final String UTF_8_ENCODING = "UTF-8";

    public static final long LIMIT_FOR_SIGNING_OFF_OF_BRIGADE = 1000 * 60 * 60 * 24;

    /**
     * Default user role.
     */
    public static final Role DEFAULT_ROLE = Role.USER;

    private Constants() {
        throw new AssertionError();
    }
}
