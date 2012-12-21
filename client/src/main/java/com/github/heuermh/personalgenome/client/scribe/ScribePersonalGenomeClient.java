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
package com.github.heuermh.personalgenome.client.scribe;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.google.common.base.Joiner;

import com.github.heuermh.personalgenome.client.AccessDeniedException;
import com.github.heuermh.personalgenome.client.Ancestry;
import com.github.heuermh.personalgenome.client.Genome;
import com.github.heuermh.personalgenome.client.Genotype;
import com.github.heuermh.personalgenome.client.Haplogroup;
import com.github.heuermh.personalgenome.client.InvalidClientException;
import com.github.heuermh.personalgenome.client.InvalidRequestException;
import com.github.heuermh.personalgenome.client.InvalidScopeException;
import com.github.heuermh.personalgenome.client.MaternalTerminalSnp;
import com.github.heuermh.personalgenome.client.PaternalTerminalSnp;
import com.github.heuermh.personalgenome.client.PersonalGenomeClient;
import com.github.heuermh.personalgenome.client.PersonalGenomeClientException;
import com.github.heuermh.personalgenome.client.Profile;
import com.github.heuermh.personalgenome.client.ProfileName;
import com.github.heuermh.personalgenome.client.User;
import com.github.heuermh.personalgenome.client.UserName;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of PersonalGenomeClient based on Scribe.
 *
 * See <a href="https://github.com/fernandezpablo85/scribe-java">https://github.com/fernandezpablo85/scribe-java</a>
 */
public final class ScribePersonalGenomeClient implements PersonalGenomeClient {
    private final Token accessToken;
    private final OAuthService service;
    private final JsonFactory jsonFactory;
    private final Logger logger = LoggerFactory.getLogger(ScribePersonalGenomeClient.class);
    private static final String USER_URL = "https://api.23andme.com/1/user";
    private static final String NAMES_URL = "https://api.23andme.com/1/names";
    private static final String HAPLOGROUPS_URL = "https://api.23andme.com/1/haplogroups";
    private static final String GENOTYPE_URL = "https://api.23andme.com/1/genotype/?locations=%s";
    private static final String GENOMES_URL = "https://api.23andme.com/1/genomes/%s/";
    private static final String ANCESTRY_URL = "https://api.23andme.com/1/ancestry/?threshold=%f";

    //@Inject
    public ScribePersonalGenomeClient(final Token accessToken, final OAuthService service, final JsonFactory jsonFactory) {
        checkNotNull(accessToken);
        checkNotNull(service);
        checkNotNull(jsonFactory);
        this.accessToken = accessToken;
        this.service = service;
        this.jsonFactory = jsonFactory;
    }

    @Override
    public User user() {
        OAuthRequest request = createAndSignRequest(USER_URL);
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("user call ok");
            return parseUser(response.getStream());
        }
        logger.warn("could not call user, response code " + code);
        throw parseException(response.getStream());
    }

    @Override
    public UserName names() {
        OAuthRequest request = createAndSignRequest(NAMES_URL);
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("names call ok");
            return parseNames(response.getStream());
        }
        logger.warn("could not call names, response code " + code);
        throw parseException(response.getStream());
    }

    @Override
    public List<Haplogroup> haplogroups() {
        OAuthRequest request = createAndSignRequest(HAPLOGROUPS_URL);
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("haplogroups call ok");
            return parseHaplogroups(response.getStream());
        }
        logger.warn("could not call haplogroups, response code " + code);
        throw parseException(response.getStream());
    }

    @Override
    public List<Genotype> genotypes(final String... locations) {
        checkNotNull(locations);
        return genotypesWithScope(Joiner.on(" ").join(locations));
    }

    @Override
    public List<Genotype> genotypes(final Iterable<String> locations) {
        checkNotNull(locations);
        return genotypesWithScope(Joiner.on(" ").join(locations));
    }

    @Override
    public Genome genome(final String profileId) {
        checkNotNull(profileId);
        // todo: URL encode and/or otherwise sanitize profileId?
        OAuthRequest request = createAndSignRequest(String.format(GENOMES_URL, profileId));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("genomes call ok");
            return parseGenomes(response.getStream());
        }
        logger.warn("could not call genomes, response code " + code);
        throw parseException(response.getStream());
    }

    @Override
    public List<Ancestry> ancestry(final double threshold) {
        if (threshold <= 0.5 || threshold >= 1.0) {
            throw new IllegalArgumentException("threshold must be in the range (0.5, 1.0), exclusive");
        }
        OAuthRequest request = createAndSignRequest(String.format(ANCESTRY_URL, threshold));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("ancestry call ok");
            return parseAncestry(response.getStream());
        }
        logger.warn("could not call ancestry, response code " + code);
        throw parseException(response.getStream());
    }

    OAuthRequest createAndSignRequest(final String url) {
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        request.addHeader("Authorization", String.format("Bearer %s", accessToken.getToken()));
        service.signRequest(accessToken, request);
        return request;
    }

    PersonalGenomeClientException parseException(final InputStream inputStream) {
        JsonParser parser = null;
        try {
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();

            String error = null;
            String errorDescription = null;
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();

                if ("error".equals(field)) {
                    error = parser.getText();
                }
                else if ("error_description".equals(field)) {
                    errorDescription = parser.getText();
                }
            }
            if ("access_denied".equals(error)) {
                return new AccessDeniedException(errorDescription);
            }
            else if ("invalid_client".equals(error)) {
                return new InvalidClientException(errorDescription);
            }
            else if ("invalid_request".equals(error)) {
                return new InvalidRequestException(errorDescription);
            }
            else if ("invalid_scope".equals(error)) {
                return new InvalidScopeException(errorDescription);
            }
            return new PersonalGenomeClientException(errorDescription);
        }
        catch (IOException e) {
            logger.warn("could not parse exception", e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return new PersonalGenomeClientException("unknown error");
    }

    User parseUser(final InputStream inputStream) {
        JsonParser parser = null;
        try {
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();

            String id = null;
            String profileId = null;
            boolean genotyped = false;
            List<Profile> profiles = new ArrayList<Profile>();

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("id".equals(field)) {
                    id = parser.getText();
                }
                else if ("profiles".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String profileField = parser.getCurrentName();
                            parser.nextToken();
                            if ("id".equals(profileField)) {
                                profileId = parser.getText();
                            }
                            else if ("genotyped".equals(profileField)) {
                                genotyped = parser.getBooleanValue();
                            }
                        }
                        profiles.add(new Profile(profileId, genotyped));
                    }
                }
            }
            return new User(id, profiles);
        }
        catch (IOException e) {
            logger.warn("could not parse user", e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    UserName parseNames(final InputStream inputStream) {
        JsonParser parser = null;
        try {
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();

            String id = null;
            String firstName = null;
            String lastName = null;
            String profileId = null;
            String profileFirstName = null;
            String profileLastName = null;
            List<ProfileName> profileNames = new ArrayList<ProfileName>();

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("id".equals(field)) {
                    id = parser.getText();
                }
                else if ("first_name".equals(field)) {
                    firstName = parser.getText();
                }
                else if ("last_name".equals(field)) {
                    lastName = parser.getText();
                }
                else if ("profiles".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String profileNameField = parser.getCurrentName();
                            parser.nextToken();
                            if ("id".equals(profileNameField)) {
                                profileId = parser.getText();
                            }
                            else if ("first_name".equals(profileNameField)) {
                                profileFirstName = parser.getText();
                            }
                            else if ("last_name".equals(profileNameField)) {
                                profileLastName = parser.getText();
                            }
                        }
                        profileNames.add(new ProfileName(profileId, profileFirstName, profileLastName));
                    }
                }
            }
            return new UserName(id, firstName, lastName, profileNames);
        }
        catch (IOException e) {
            logger.warn("could not parse names", e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    List<Haplogroup> parseHaplogroups(final InputStream inputStream) {
        JsonParser parser = null;
        try {
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();

            String id = null;
            String maternal = null;
            String paternal = null;
            String rsid = null;
            String rcrsPosition = null;
            String snp = null;
            List<Haplogroup> haplogroups = new ArrayList<Haplogroup>();
            List<PaternalTerminalSnp> paternalTerminalSnps = new ArrayList<PaternalTerminalSnp>();
            List<MaternalTerminalSnp> maternalTerminalSnps = new ArrayList<MaternalTerminalSnp>();

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String field = parser.getCurrentName();
                    parser.nextToken();

                    if ("id".equals(field)) {
                        id = parser.getText();
                    }
                    else if ("maternal".equals(field)) {
                        maternal = parser.getText();
                    }
                    else if ("paternal".equals(field)) {
                        paternal = "null" == parser.getText() ? null : parser.getText();
                    }
                    else if ("maternal_terminal_snps".equals(field)) {
                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            while (parser.nextToken() != JsonToken.END_OBJECT) {
                                String maternalTerminalSnpsField = parser.getCurrentName();
                                parser.nextToken();

                                if ("rsid".equals(maternalTerminalSnpsField)) {
                                    rsid = parser.getText();
                                }
                                else if ("rcrs_position".equals(maternalTerminalSnpsField)) {
                                    rcrsPosition = parser.getText();
                                }
                            }
                            maternalTerminalSnps.add(new MaternalTerminalSnp(rsid, rcrsPosition));
                        }
                    }
                    else if ("paternal_terminal_snps".equals(field)) {
                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            while (parser.nextToken() != JsonToken.END_OBJECT) {
                                String paternalTerminalSnpsField = parser.getCurrentName();
                                parser.nextToken();

                                if ("rsid".equals(paternalTerminalSnpsField)) {
                                    rsid = parser.getText();
                                }
                                else if ("snp".equals(paternalTerminalSnpsField)) {
                                    snp = parser.getText();
                                }
                            }
                            paternalTerminalSnps.add(new PaternalTerminalSnp(rsid, snp));
                        }
                    }
                }
                haplogroups.add(new Haplogroup(id, paternal, maternal, paternalTerminalSnps, maternalTerminalSnps));
                paternalTerminalSnps.clear();
                maternalTerminalSnps.clear();
            }
            return haplogroups;
        }
        catch (IOException e) {
            logger.warn("could not parse haplogroups", e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    List<Genotype> genotypesWithScope(final String scope) {
        // todo: URL encode and/or otherwise sanitize scope?
        OAuthRequest request = createAndSignRequest(String.format(GENOTYPE_URL, scope));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("genotype call ok");
            return parseGenotypes(response.getStream());
        }
        logger.warn("could not call genotype, response code " + code);
        throw parseException(response.getStream());
    }

    List<Genotype> parseGenotypes(final InputStream inputStream) {
        JsonParser parser = null;
        try {
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();

            String id = null;
            String location = null;
            String interpretation = null;
            Map<String, String> values = new HashMap<String, String>();
            List<Genotype> genotypes = new ArrayList<Genotype>();

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String field = parser.getCurrentName();
                    parser.nextToken();

                    if ("id".equals(field)) {
                        id = parser.getText();
                    }
                    else {
                        location = field;
                        interpretation = parser.getText();
                        values.put(location, interpretation);
                    }
                }
                genotypes.add(new Genotype(id, values));
                values.clear();
            }
            return genotypes;
        }
        catch (IOException e) {
            logger.warn("could not parse genotypes");
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    Genome parseGenomes(final InputStream inputStream) {
        JsonParser parser = null;
        try {
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();

            String id = null;
            String values = null;
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();

                if ("id".equals(field)) {
                    id = parser.getText();
                }
                else if ("genome".equals(field)) {
                    values = parser.getText();
                }
            }
            return new Genome(id, values);
        }
        catch (IOException e) {
            logger.warn("could not parse genomes", e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    List<Ancestry> parseSubPopulation(final String id, final List<Ancestry> ancestries, final JsonParser parser) throws IOException {
        String label = null;
        double proportion = 0.0d;
        double unassigned = 0.0d;
        List<Ancestry> subPopulations = new ArrayList<Ancestry>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();

                if ("label".equals(field)) {
                    label = parser.getText();
                }
                else if ("proportion".equals(field)) {
                    proportion = Double.parseDouble(parser.getText());
                }
                else if ("unassigned".equals(field)) {
                    unassigned = Double.parseDouble(parser.getText());
                }
                else if ("sub_populations".equals(field)) {
                    subPopulations = parseSubPopulation(id, subPopulations, parser);
                }
            }
            Ancestry ancestry = new Ancestry(id, label, proportion, unassigned, subPopulations);
            ancestries.add(ancestry);
            label = null;
            proportion = 0.0d;
            unassigned = 0.0d;
            subPopulations.clear();
        }
        return ancestries;
    }

    List<Ancestry> parseAncestry(final InputStream inputStream) {
        JsonParser parser = null;
        try {
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();

            String id = null;
            String label = null;
            double proportion = 0.0d;
            double unassigned = 0.0d;
            List<Ancestry> ancestries = new ArrayList<Ancestry>();
            List<Ancestry> subPopulations = new ArrayList<Ancestry>();
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String field = parser.getCurrentName();
                    parser.nextToken();

                    if ("id".equals(field)) {
                        id = parser.getText();
                    }
                    else if ("ancestry".equals(field)) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String ancestryField = parser.getCurrentName();
                            parser.nextToken();

                            if ("label".equals(ancestryField)) {
                                label = parser.getText();
                            }
                            else if ("proportion".equals(ancestryField)) {
                                proportion = Double.parseDouble(parser.getText());
                            }
                            else if ("unassigned".equals(ancestryField)) {
                                unassigned = Double.parseDouble(parser.getText());
                            }
                            else if ("sub_populations".equals(ancestryField)) {
                                subPopulations = parseSubPopulation(id, subPopulations, parser);
                            }
                        }
                    }
                }
                Ancestry ancestry = new Ancestry(id, label, proportion, unassigned, subPopulations);
                ancestries.add(ancestry);
                label = null;
                proportion = 0.0d;
                unassigned = 0.0d;
                subPopulations.clear();
            }
            return ancestries;
        }
        catch (IOException e) {
            logger.warn("could not parse ancestry", e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }
}