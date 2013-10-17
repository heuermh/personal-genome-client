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

import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

/**
 * Ancestry.
 */
@Immutable
public final class Ancestry {
    private final String profileId;
    private final String label;
    private final double proportion;
    private final double unassigned;
    private final List<Ancestry> subPopulations;

    public Ancestry(final String profileId, final String label, final double proportion, final double unassigned, final List<Ancestry> subPopulations)
    {
        checkNotNull(profileId);
        checkNotNull(label);
        checkNotNull(subPopulations);
        this.profileId = profileId;
        this.label = label;
        this.proportion = proportion;
        this.unassigned = unassigned;
        this.subPopulations = ImmutableList.copyOf(subPopulations);
    }

    public String getProfileId() {
        return profileId;
    }

    public String getLabel() {
        return label;
    }

    public double getProportion() {
        return proportion;
    }

    public double getUnassigned() {
        return unassigned;
    }

    public List<Ancestry> getSubPopulations() {
        return subPopulations;
    }
}