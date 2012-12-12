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

import java.util.List;

/**
 * Personal genome API client.
 */
public interface PersonalGenomeClient {

    /**
     * Return the current user, a list of profiles associated with that user, and whether or not each
     * profile has been genotyped.
     *
     * <p>Scope required: <b>basic</b></p>
     *
     * @return the user, a list of profiles associated with that user, and whether or not each
     *    profile has been genotyped
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    User user();

    /**
     * Return the first and last names of the current user and for each profile associated with that user.
     *
     * <p>Scope required: <b>names</b></p>
     *
     * @return the first and last names for the user and for each profile associated with that user
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    UserName names();

    /**
     * Return the maternal and paternal haplogrounds and terminal SNPs for each profile associated with
     * the current user.
     *
     * <p>Scope required: <b>haplogroups</b></p>
     *
     * @return the maternal and paternal haplogrounds and terminal SNPs for each profile associated with
     *    the current user
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    List<Haplogroup> haplogroups();

    /**
     * Return the genotype for each profile associated with the current user for each of the specified locations.
     * The scope of the bearer token associated with the current user must include each of the specified locations.
     *
     * <p>Scope required: <code>rsXX</code><b>for each location</b>, or <code>genomes</code></p>
     *
     * @param locations variable number of locations, must not be null
     * @return the genotype for each of the specified locations
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    List<Genotype> genotypes(String... locations);

    /**
     * Return the genotype for each profile associated with the current user for each of the specified locations.
     * The scope of the bearer token associated with the current user must include each of the specified locations.
     *
     * <p>Scope required: <code>rsXX</code><b>for each location</b>, or <code>genomes</code></p>
     *
     * @param locations zero or more locations, must not be null
     * @return the genotype for each of the specified locations
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    List<Genotype> genotypes(Iterable<String> locations);

    /**
     * Return the genome (packed string of SNP typings) for the specified profile.
     *
     * <p>Scope required: <code>genomes</scope></p>
     *
     * @param profileId identifier for profile associated with current user, must not be null
     * @return the genome for the specified profile
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    Genome genome(String profileId);
}