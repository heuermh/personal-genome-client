/*

    personal-genome-client  Java client for the 23andMe Personal Genome API.
    Copyright (c) 2012-2013 held jointly by the individual authors.

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

*/
package com.github.heuermh.personalgenome.client;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

/**
 * Relationship.
 */
@Immutable
public enum Relationship {

    YOU(0, "You"),
    IDENTICAL_TWIN(1, "Identical Twin"),
    FATHER(2, "Father"),
    MOTHER(3, "Mother"),
    SON(4, "Son"),
    DAUGHTER(5, "Daughter"),
    BROTHER(6, "Brother"),
    SISTER(7, "Sister"),
    HALF_BROTHER(8, "Half Brother"),
    HALF_SISTER(9, "Half Sister"),
    GRANDFATHER(10, "Grandfather"),
    GRANDMOTHER(11, "Grandmother"),
    GRANDSON(12, "Grandson"),
    GRANDDAUGHTER(13, "Granddaughter"),
    UNCLE(14, "Uncle"),
    AUNT(15, "Aunt"),
    NEPHEW(16, "Nephew"),
    NIECE(17, "Niece"),
    GREAT_GRANDFATHER(18, "Great Grandfather"),
    GREAT_GRANDSON(19, "Great Grandson"),
    GREAT_GRANDMOTHER(20, "Great Grandmother"),
    GREAT_GRANDDAUGHTER(21, "Great Granddaughter"),
    GREAT_UNCLE(22, "Great Uncle"),
    GREAT_AUNT(23, "Great Aunt"),
    GREAT_NEPHEW(24, "Great Nephew"),
    GREAT_NIECE(25, "Great Niece"),
    FIRST_COUSIN(26, "1st Cousin"),
    FIRST_COUSIN_ONCE_REMOVED(27, "1st Cousin, Once Removed"),
    FIRST_COUSIN_TWICE_REMOVED(28, "1st Cousin, Twice Removed"),
    SECOND_COUSIN(29, "2nd Cousin"),
    SECOND_COUSIN_ONCE_REMOVED(30, "2nd Cousin, Once Removed"),
    SECOND_COUSIN_TWICE_REMOVED(31, "2nd Cousin, Twice Removed"),
    THIRD_COUSIN(32, "3rd Cousin"),
    THIRD_COUSIN_ONCE_REMOVED(33, "3rd Cousin, Once Removed"),
    THIRD_COUSIN_TWICE_REMOVED(34, "3rd Cousin, Twice Removed"),
    FOURTH_COUSIN(35, "4th Cousin"),
    FIFTH_COUSIN(38, "5th Cousin"),
    SIXTH_COUSIN(41, "6th Cousin"),
    DISTANT_COUSIN(44, "Distant Cousin");

    private final int code;
    private final String description;
    private static final Map<Integer, Relationship> KEYED_BY_CODE;
    private static final Map<String, Relationship> KEYED_BY_DESCRIPTION;

    static
    {
        Map<Integer, Relationship> keyedByCode = new HashMap<Integer, Relationship>();
        for (Relationship relationship : values()) {
            keyedByCode.put(relationship.getCode(), relationship);
        }
        KEYED_BY_CODE = ImmutableMap.copyOf(keyedByCode);

        Map<String, Relationship> keyedByDescription = new HashMap<String, Relationship>();
        for (Relationship relationship : values()) {
            keyedByDescription.put(relationship.getDescription(), relationship);
        }
        KEYED_BY_DESCRIPTION = ImmutableMap.copyOf(keyedByDescription);
    }

    private Relationship(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    public static Relationship fromCode(final int code) {
        return KEYED_BY_CODE.get(code);
    }

    public static Relationship fromDescription(final String description) {
        return KEYED_BY_DESCRIPTION.get(description);
    }
}