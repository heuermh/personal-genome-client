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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Abstract unit test for implementations of PersonalGenomeClient.
 */
public abstract class AbstractPersonalGenomeClientTest {
    protected PersonalGenomeClient client;
    protected abstract PersonalGenomeClient createPersonalGenomeClient();

    @Before
    public void setUp() {
        client = createPersonalGenomeClient();
    }

    @Test
    public void testCreatePersonalGenomeClient() {
        assertNotNull(client);
    }

    @Test(expected=NullPointerException.class)
    public void testGenotypesNullLocations() {
        client.genotypes((String) null);
    }

    @Test(expected=NullPointerException.class)
    public void testGenotypesNullIterableLocations() {
        client.genotypes((Iterable<String>) null);
    }

    @Test(expected=NullPointerException.class)
    public void testGenotypesNullLocation() {
        client.genotypes("rs1234", null, "rs2345");
    }

    @Test(expected=NullPointerException.class)
    public void testGenotypesNullInIterableLocations() {
        List<String> locations = new ArrayList<String>();
        locations.add("rs1234");
        locations.add(null);
        locations.add("rs2345");
        client.genotypes(locations);
    }
}