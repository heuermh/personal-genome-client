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
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

/**
 * Unit test for Genome.
 */
public final class GenomeTest {

    @Test(expected=NullPointerException.class)
    public void testConstructorNullProfileId() {
        new Genome(null, "ACGT__");
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullValues() {
        new Genome("profileId", null);
    }

    @Test
    public void testConstructor() {
        Genome genome = new Genome("profileId", "ACGT__");
        assertNotNull(genome);
        assertEquals("profileId", genome.getProfileId());
        assertEquals("ACGT__", genome.getValues());
    }

    @Test(expected=NullPointerException.class)
    public void testAsGenotypeNullLocation() {
        Genome genome = new Genome("profileId", "ACGT__");
        genome.asGenotype((String) null);
    }

    @Test(expected=NullPointerException.class)
    public void testAsGenotypeNullLocations() {
        Genome genome = new Genome("profileId", "ACGT__");
        genome.asGenotype((String[]) null);
    }

    @Test(expected=NullPointerException.class)
    public void testAsGenotypeNullIterableLocations() {
        Genome genome = new Genome("profileId", "ACGT__");
        genome.asGenotype((Iterable<String>) null);
    }

    @Test(expected=NullPointerException.class)
    public void testAsGenotypeNullLocationInIterableLocations() {
        Genome genome = new Genome("profileId", "ACGT__");
        genome.asGenotype(Arrays.asList(new String[] { null }));
    }

    @Test
    public void testAsGenotype() {
        Genome genome = new Genome("profileId", "ACGT__");
        Genotype genotype = genome.asGenotype("rs41362547", "rs28358280", "rs3915952");
        assertEquals(genome.getProfileId(), genotype.getProfileId());
        assertEquals("AC", genotype.getValues().get("rs41362547"));
        assertEquals("GT", genotype.getValues().get("rs28358280"));
        assertEquals("__", genotype.getValues().get("rs3915952"));
    }

    @Test
    public void testAsGenotypeNoValueForLocation() {
        Genome genome = new Genome("profileId", "ACGT__");
        Genotype genotype = genome.asGenotype("rs41333444");
        assertNull(genotype.getValues().get("rs41333444"));
    }
}