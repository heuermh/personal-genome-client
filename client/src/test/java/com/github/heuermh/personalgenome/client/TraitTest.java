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
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import org.junit.Test;

/**
 * Unit test for Trait.
 */
public final class TraitTest {
    private Set<String> possibleTraits = ImmutableSet.of("trait0", "trait1", "trait2");

    @Test(expected=NullPointerException.class)
    public void testConstructorNullProfileId() {
        new Trait(null, "reportId", "description", "trait0", possibleTraits);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullReportId() {
        new Trait("profileId", null, "description", "trait0", possibleTraits);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullDescription() {
        new Trait("profileId", "reportId", null, "trait0", possibleTraits);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullTrait() {
        new Trait("profileId", "reportId", "description", null, possibleTraits);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullPossibleTraits() {
        new Trait(null, "reportId", "description", "trait0", null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorEmptyPossibleTraits() {
        new Trait("profileId", "reportId", "description", "trait0", Collections.<String>emptySet());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorTraitNotContainedInPossibleTraits() {
        new Trait("profileId", "reportId", "description", "invalidTrait", possibleTraits);
    }

    @Test
    public void testConstructor() {
        Trait trait = new Trait("profileId", "reportId", "description", "trait0", possibleTraits);
        assertNotNull(trait);
        assertEquals("profileId", trait.getProfileId());
        assertEquals("reportId", trait.getReportId());
        assertEquals("description", trait.getDescription());
        assertEquals("trait0", trait.getTrait());
        assertEquals(possibleTraits, trait.getPossibleTraits());
        assertTrue(trait.getPossibleTraits().contains(trait.getTrait()));
    }
}