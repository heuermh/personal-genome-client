/*

    personal-genome-client  Java client for the 23andMe Personal Genome API.
    Copyright (c) 2012 held jointly by the individual authors.

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit test for User.
 */
public final class UserTest {

    @Test(expected=NullPointerException.class)
    public void testConstructorNullId() {
        new User(null, ImmutableList.of(new Profile("profileId", false)));
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullProfiles() {
        new User("userId", null);
    }

    @Test
    public void testConstructor() {
        User user = new User("userId", ImmutableList.of(new Profile("profileId0", false), new Profile("profileId1", true)));
        assertNotNull(user);
        assertEquals("userId", user.getId());
        assertNotNull(user.getProfiles());
        assertEquals(2, user.getProfiles().size());
        assertEquals("profileId0", user.getProfiles().get(0).getId());
        assertEquals("profileId1", user.getProfiles().get(1).getId());
    }
}