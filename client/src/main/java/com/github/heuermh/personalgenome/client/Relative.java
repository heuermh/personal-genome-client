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

import com.google.common.collect.Sets;

/**
 * Relative.
 */
@Immutable
public final class Relative {
    private final String profileId;
    private final String matchId;
    private final double similarity;
    private final int sharedSegments;
    private final Relationship relationship;
    private final Relationship userRelationship;
    private final Set<Relationship> range;

    public Relative(final String profileId, final String matchId, final double similarity, final int sharedSegments, final Relationship relationship, final Relationship userRelationship, final Set<Relationship> range) {
        checkNotNull(profileId);
        checkNotNull(matchId);
        this.profileId = profileId;
        this.matchId = matchId;
        this.similarity = similarity;
        this.sharedSegments = sharedSegments;
        this.relationship = relationship;
        this.userRelationship = userRelationship;
        // or return empty?
        this.range = range == null ? null : Sets.immutableEnumSet(range);
    }

    public String getProfileId() {
        return profileId;
    }

    public String getMatchId() {
        return matchId;
    }

    public double getSimilarity() {
        return similarity;
    }

    public int getSharedSegments() {
        return sharedSegments;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public Relationship getUserRelationship() {
        return userRelationship;
    }

    public Set<Relationship> getRange() {
        return range;
    }
}