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
 * Unit test for UserName.
 */
public final class UserNameTest {

    @Test(expected=NullPointerException.class)
    public void testConstructorNullId() {
        new UserName(null, "First", "Last", ImmutableList.of(new ProfileName("id", "First", "Last")));
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullFirstName() {
        new UserName("id", null, "Last", ImmutableList.of(new ProfileName("id", "First", "Last")));
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullLastName() {
        new UserName("id", "First", null, ImmutableList.of(new ProfileName("id", "First", "Last")));
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullProfileNames() {
        new UserName("id", "First", "Last", null);
    }

    @Test
    public void testConstructor() {
        UserName userName = new UserName("id", "First", "Last", ImmutableList.of(new ProfileName("id", "First", "Last")));;
        assertNotNull(userName);
        assertEquals("id", userName.getId());
        assertEquals("First", userName.getFirstName());
        assertEquals("Last", userName.getLastName());
        assertEquals("id", userName.getProfileNames().get(0).getId());
        assertEquals("First", userName.getProfileNames().get(0).getFirstName());
        assertEquals("Last", userName.getProfileNames().get(0).getLastName());
    }
}