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

import java.util.Set;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;

/**
 * Trait.
 */
@Immutable
public final class Trait {
    private final String profileId;
    private final String reportId;
    private final String description;

    // todo:  trait is a value in possible_traits, or null if the profile's not analyzed at those markers
    private final String trait;

    private final Set<String> possibleTraits;

    public Trait(final String profileId, final String reportId, final String description, final String trait, final Set<String> possibleTraits) {
        checkNotNull(profileId);
        checkNotNull(reportId);
        checkNotNull(description);
        checkNotNull(trait);
        checkNotNull(possibleTraits);
        if (!possibleTraits.contains(trait)) {
            throw new IllegalArgumentException("trait must be contained in possible traits");
        }
        this.profileId = profileId;
        this.reportId = reportId;
        this.description = description;
        this.trait = trait;
        this.possibleTraits = ImmutableSet.copyOf(possibleTraits);
    }

    public String getProfileId() {
        return profileId;
    }

    public String getReportId() {
        return reportId;
    }

    public String getDescription() {
        return description;
    }

    public String getTrait() {
        return trait;
    }

    public Set<String> getPossibleTraits() {
        return possibleTraits;
    }
}