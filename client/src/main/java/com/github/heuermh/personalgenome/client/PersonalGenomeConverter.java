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

import java.io.InputStream;

import java.util.List;

import com.github.heuermh.personalgenome.client.Ancestry;
import com.github.heuermh.personalgenome.client.Carrier;
import com.github.heuermh.personalgenome.client.DrugResponse;
import com.github.heuermh.personalgenome.client.Genome;
import com.github.heuermh.personalgenome.client.Genotype;
import com.github.heuermh.personalgenome.client.Haplogroup;
import com.github.heuermh.personalgenome.client.PersonalGenomeClientException;
import com.github.heuermh.personalgenome.client.Relative;
import com.github.heuermh.personalgenome.client.Risk;
import com.github.heuermh.personalgenome.client.Trait;
import com.github.heuermh.personalgenome.client.User;
import com.github.heuermh.personalgenome.client.UserName;

/**
 * Converter.
 */
public interface PersonalGenomeConverter {

    /**
     * Parse the specified input stream and return a personal genome client exception.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into a personal genome client exception
     */
    PersonalGenomeClientException parseException(InputStream inputStream);

    /**
     * Parse the specified input stream and return a user.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into a user
     */
    User parseUser(InputStream inputStream);

    /**
     * Parse the specified input stream and return user names.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into user names
     */
    UserName parseNames(InputStream inputStream);

    /**
     * Parse the specified input stream and return haplogroups.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into haplogroups
     */
    Haplogroup parseHaplogroups(InputStream inputStream);

    /**
     * Parse the specified input stream and return genotypes.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into genotypes
     */
    Genotype parseGenotypes(InputStream inputStream);
 
    /**
     * Parse the specified input stream and return genomes.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into genomes
     */
    Genome parseGenomes(InputStream inputStream);

    /**
     * Parse the specified input stream and return ancestry.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into ancestry
     */
    Ancestry parseAncestry(InputStream inputStream);

    /**
     * Parse the specified input stream and return the neanderthal proportion.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into the neanderthal proportion
     */
    double parseNeanderthalProportion(InputStream inputStream);

    /**
     * Parse the specified input stream and return zero or more relatives.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into zero or more relatives
     */
    List<Relative> parseRelatives(InputStream inputStream);

    /**
     * Parse the specified input stream and return zero or more risks.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into zero or more risks
     */
    List<Risk> parseRisks(InputStream inputStream);

    /**
     * Parse the specified input stream and return zero or more carriers.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into zero or more carriers
     */
    List<Carrier> parseCarriers(InputStream inputStream);

    /**
     * Parse the specified input stream and return zero or more drug responses.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into zero or more drug responses
     */
    List<DrugResponse> parseDrugResponses(InputStream inputStream);

    /**
     * Parse the specified input stream and return zero or more traits.
     *
     * @param inputStream input stream, must not be null
     * @return the specified input stream parsed into zero or more traits
     */
    List<Trait> parseTraits(InputStream inputStream);
}