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
 * User name.
 */
@Immutable
public final class UserName {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final List<ProfileName> profileNames;

    public UserName(final String id, final String firstName, final String lastName, final List<ProfileName> profileNames) {
        checkNotNull(id);
        checkNotNull(firstName);
        checkNotNull(lastName);
        checkNotNull(profileNames);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileNames = ImmutableList.copyOf(profileNames);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<ProfileName> getProfileNames() {
        return profileNames;
    }
}