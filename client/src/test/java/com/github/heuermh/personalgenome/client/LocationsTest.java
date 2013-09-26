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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Unit test for Locations.
 */
public final class LocationsTest {

    @Test
    public void testLocations() {
        for (String location : Locations.locations()) {
            assertNotNull(location);
        }
    }

    @Test(expected=NullPointerException.class)
    public void testIndexNullLocation() {
        Locations.index(null);
    }

    @Test
    public void testIndexInvalidLocation() {
        assertEquals(-1, Locations.index("invalid location"));
    }

    @Test
    public void testIndex() {
        assertEquals(1047948 * 2, Locations.index("i5053906"));
    }

    @Test(expected=NullPointerException.class)
    public void testChromosomeNullLocation() {
        Locations.chromosome(null);
    }

    @Test
    public void testChromosomeInvalidLocation() {
        assertNull(Locations.chromosome("invalid location"));
    }

    @Test
    public void testChromosome() {
        assertEquals("X", Locations.chromosome("i5053906"));
    }

    @Test(expected=NullPointerException.class)
    public void testPositionNullLocation() {
        Locations.position(null);
    }

    @Test
    public void testPositionInvalidLocation() {
        assertEquals(-1, Locations.position("invalid location"));
    }

    @Test
    public void testPosition() {
        assertEquals(153764245, Locations.position("i5053906"));
    }

    // snps.data updated sometime after 12 dec 2012

    @Test
    public void testIndexNewVersion() {
        assertEquals(582560 * 2, Locations.index("rs4630"));
    }

    @Test
    public void testChromosomeNewVersion() {
        assertEquals("22", Locations.chromosome("rs4630"));
    }

    @Test
    public void testPositionNewVersion() {
        assertEquals(24376322, Locations.position("rs4630"));
    }
}