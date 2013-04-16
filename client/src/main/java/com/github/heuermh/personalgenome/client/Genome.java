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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

/**
 * Genome.
 */
@Immutable
public final class Genome {
    private final String profileId;
    private final String values;

    public Genome(final String profileId, final String values) {
        checkNotNull(profileId);
        checkNotNull(values);
        this.profileId = profileId;
        this.values = values;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getValues() {
        return values;
    }

    public Genotype asGenotype() {
        return asGenotype(Locations.locations());
    }

    public Genotype asGenotype(final String... locations) {
        checkNotNull(locations);
        Map<String, String> genotypeValues = new HashMap<String, String>(locations.length);
        for (String location : locations) {
            checkNotNull(location);
            int index = Locations.index(location);
            if (index >= 0 && index < values.length() - 1) {
                genotypeValues.put(location, values.substring(index, index + 2));
            }
        }
        return new Genotype(profileId, genotypeValues);
    }

    public Genotype asGenotype(final Iterable<String> locations) {
        checkNotNull(locations);
        Map<String, String> genotypeValues = new HashMap<String, String>();
        for (String location : locations) {
            checkNotNull(location);
            int index = Locations.index(location);
            if (index >= 0 && index < values.length() - 1) {
                genotypeValues.put(location, values.substring(index, index + 2));
            }
        }
        return new Genotype(profileId, genotypeValues);
    }
}