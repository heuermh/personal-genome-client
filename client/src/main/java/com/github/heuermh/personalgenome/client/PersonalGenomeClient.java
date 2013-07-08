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

import java.util.Iterator;
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
     * Return the first and last names of the current user and specified profile.
     *
     * <p>Scope required: <b>names</b></p>
     *
     * @param profileId identifier for profile associated with current user, must not be null
     * @return the first and last names for the user and for each profile associated with that user
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    // or use Profile instead of profileId
    UserName names(String profileId);

    /**
     * Return the maternal and paternal haplogroups and terminal SNPs for the specified profile.
     *
     * <p>Scope required: <b>haplogroups</b></p>
     *
     * @param profileId identifier for profile associated with current user, must not be null
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
    Haplogroup haplogroups(String profileId);

    /**
     * Return the genotype for the specified profile for each of the specified locations.
     * The scope of the bearer token associated with the current user must include each of the specified locations.
     *
     * <p>Scope required: <code>rsXX</code><b>for each location</b>, or <code>genomes</code></p>
     *
     * @param profileId identifier for profile associated with current user, must not be null
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
    Genotype genotypes(String profileId, String... locations);

    /**
     * Return the genotype for the specified profile for each of the specified locations.
     * The scope of the bearer token associated with the current user must include each of the specified locations.
     *
     * <p>Scope required: <code>rsXX</code><b>for each location</b>, or <code>genomes</code></p>
     *
     * @param profileId identifier for profile associated with current user, must not be null
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
    Genotype genotypes(String profileId, Iterable<String> locations);

    /**
     * Return the genome (packed string of SNP typings) for the specified profile.
     *
     * <p>Scope required: <code>genomes</code></p>
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

    /**
     * Return the ancestral background for the specified profile.
     *
     * <p>Scope required: <code>ancestry</code></p>
     *
     * @param profileId identifier for profile associated with current user, must not be null
     * @param threshold threshold, must be in the range <code>(0.5, 1.0)</code>, exclusive
     * @return the ancestral background for the specified profile
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    Ancestry ancestry(String profileId, double threshold);

    /**
     * Return the estimated genome-wide proportion of Neanderthal ancestry for the specified profile.
     * Most users have between 0.01 and 0.04 Neanderthal ancestry; proportion is -1 for un-genotyped (or as-of-yet
     * uncomputed) profiles. 
     *
     * <p>Scope required: <code>ancestry</code></p>
     *
     * @param profileId identifier for profile associated with current user, must not be null
     * @return the estimated genome-wide proportion of Neanderthal ancestry for the specified profile or
     *    <code>-1</code> if the profile is un-genotyped or as-of-yet uncomputed
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    //Neanderthal neanderthal(String profileId); if any attrs are added
    double neanderthalProportion(String profileId);

    /**
     * Return an iterator over the relatives on 23andMe for the specified profile.  The iterator will
     * fetch lazily from 23andMe every 100 relatives.
     *
     * <p>Scope required: <code>relatives:write</code></p>
     *
     * @param profileId identifier for profile associated with current user, must not be null
     * @return an iterator over the relatives on 23andMe for the specified profile
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    Iterator<Relative> relatives(String profileId);

    /**
     * Return up to the specified limit of relatives on 23andMe for the specified profile, starting from the
     * specified offset.
     *
     * <p>Scope required: <code>relatives:write</code></p>
     *
     * @param profileId identifier for profile associated with current user, must not be null
     * @return zero or more relatives on 23andMe for the specified profile
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    List<Relative> relatives(String profileId, int offset, int limit);

    /**
     * Return an analysis of the lifetime risks for certian diseases for the specified profile.
     *
     * <p>Scope required: <code>analysis</code></p>
     *
     * @see https://www.23andme.com/health/ethnicity/#disease-risk
     * @param profileId identifier for profile associated with current user, must not be null
     * @return zero or more risks for the specified profile
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    List<Risk> risks(String profileId);

    /**
     * Return an analysis of whether the specified profile is a carrier for certian diseases.
     *
     * <p>Scope required: <code>analysis</code></p>
     *
     * @see https://www.23andme.com/health/ethnicity/#carrier-status
     * @param profileId identifier for profile associated with current user, must not be null
     * @return zero or more carriers for the specified profile
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    List<Carrier> carriers(String profileId);

    /**
     * Return an analysis of how the specified profile might respond to certian drugs.
     *
     * <p>Scope required: <code>analysis</code></p>
     *
     * @see https://www.23andme.com/health/ethnicity/#drug-response
     * @param profileId identifier for profile associated with current user, must not be null
     * @return zero or more drug responses for the specified profile
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    List<DrugResponse> drugResponses(String profileId);

    /**
     * Return an analysis of certian traits for the specified profile.
     *
     * <p>Scope required: <code>analysis</code></p>
     *
     * @see https://www.23andme.com/health/ethnicity/#traits
     * @param profileId identifier for profile associated with current user, must not be null
     * @return zero or more traits for the specified profile
     *
     * @throws AccessDeniedException if the resource owner or authorization server denied the request
     * @throws InvalidClientException if client authentication failed (e.g. unknown client, no client credentials
     *    included, multiple client credentials included, or unsupported credentials type)
     * @throws InvalidRequestException if request is missing a required parameter, includes an unsupported parameter
     *    or parameter value, or is otherwise malformed
     * @throws InvalidScopeException if the requested scope is invalid, unknown, or malformed
     */
    List<Trait> traits(String profileId);
}