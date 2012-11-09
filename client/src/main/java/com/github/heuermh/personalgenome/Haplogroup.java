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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

/**
 * Haplogroup.
 */
@Immutable
public final class Haplogroup {
    private final String profileId;
    private final String paternal; // may be null
    private final String maternal;
    private final List<PaternalTerminalSnp> paternalTerminalSnps; // may be null
    private final List<MaternalTerminalSnp> maternalTerminalSnps;

    public Haplogroup(final String profileId,
                      final String paternal,
                      final String maternal,
                      final List<PaternalTerminalSnp> paternalTerminalSnps,
                      final List<MaternalTerminalSnp> maternalTerminalSnps) {
        checkNotNull(profileId);
        checkNotNull(maternal);
        checkNotNull(maternalTerminalSnps);
        this.profileId = profileId;
        this.paternal = paternal;
        this.maternal = maternal;
        this.paternalTerminalSnps = paternalTerminalSnps == null ? null : ImmutableList.copyOf(paternalTerminalSnps);
        this.maternalTerminalSnps = ImmutableList.copyOf(maternalTerminalSnps);
    }

    public String getProfileId() {
        return profileId;
    }

    public String getPaternal() {
        return paternal;
    }

    public String getMaternal() {
        return maternal;
    }

    public List<PaternalTerminalSnp> getPaternalTerminalSnps() {
        return paternalTerminalSnps;
    }

    public List<MaternalTerminalSnp> getMaternalTerminalSnps() {
        return maternalTerminalSnps;
    }
}