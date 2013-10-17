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
package com.github.heuermh.personalgenome.client.converter;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.github.heuermh.personalgenome.client.AccessDeniedException;
import com.github.heuermh.personalgenome.client.Ancestry;
import com.github.heuermh.personalgenome.client.Carrier;
import com.github.heuermh.personalgenome.client.DrugResponse;
import com.github.heuermh.personalgenome.client.Genome;
import com.github.heuermh.personalgenome.client.Genotype;
import com.github.heuermh.personalgenome.client.Haplogroup;
import com.github.heuermh.personalgenome.client.InvalidClientException;
import com.github.heuermh.personalgenome.client.InvalidRequestException;
import com.github.heuermh.personalgenome.client.InvalidScopeException;
import com.github.heuermh.personalgenome.client.MaternalTerminalSnp;
import com.github.heuermh.personalgenome.client.PaternalTerminalSnp;
import com.github.heuermh.personalgenome.client.PersonalGenomeClientException;
import com.github.heuermh.personalgenome.client.PersonalGenomeConverter;
import com.github.heuermh.personalgenome.client.Profile;
import com.github.heuermh.personalgenome.client.ProfileName;
import com.github.heuermh.personalgenome.client.Relative;
import com.github.heuermh.personalgenome.client.Relationship;
import com.github.heuermh.personalgenome.client.Risk;
import com.github.heuermh.personalgenome.client.Trait;
import com.github.heuermh.personalgenome.client.User;
import com.github.heuermh.personalgenome.client.UserName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converter for <code>application/json</code> mime type implemented using Jackson.
 */
public final class JacksonPersonalGenomeConverter implements PersonalGenomeConverter {
    private final JsonFactory jsonFactory;
    private final Logger logger = LoggerFactory.getLogger(JacksonPersonalGenomeConverter.class);

    //@Inject
    public JacksonPersonalGenomeConverter(final JsonFactory jsonFactory) {
        checkNotNull(jsonFactory);
        this.jsonFactory = jsonFactory;
    }


    @Override
    public PersonalGenomeClientException parseException(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
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

    /**
     * Parse the specified input stream and return a user.
     *
     * @param inputStream input stream
     * @return the specified input stream parsed into a user
     */
    @Override
    public User parseUser(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
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

    @Override
    public UserName parseNames(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
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

    @Override
    public Haplogroup parseHaplogroups(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
            parser.nextToken();

            String id = null;
            String maternal = null;
            String paternal = null;
            String rsid = null;
            String rcrsPosition = null;
            String snp = null;
            List<PaternalTerminalSnp> paternalTerminalSnps = new ArrayList<PaternalTerminalSnp>();
            List<MaternalTerminalSnp> maternalTerminalSnps = new ArrayList<MaternalTerminalSnp>();

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
            return new Haplogroup(id, paternal, maternal, paternalTerminalSnps, maternalTerminalSnps);
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

    @Override
    public Genotype parseGenotypes(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
            parser.nextToken();

            String id = null;
            String location = null;
            String interpretation = null;
            Map<String, String> values = new HashMap<String, String>();

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
            return new Genotype(id, values);
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

    @Override
    public Genome parseGenomes(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
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

    @Override
    public Ancestry parseAncestry(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
            parser.nextToken();

            String id = null;
            String label = null;
            double proportion = 0.0d;
            double unassigned = 0.0d;
            List<Ancestry> subPopulations = new ArrayList<Ancestry>();
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
            return new Ancestry(id, label, proportion, unassigned, subPopulations);
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

    @Override
    public double parseNeanderthalProportion(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
            parser.nextToken();

            double proportion = -1d;
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();

                if ("neanderthal".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        String neanderthalField = parser.getCurrentName();
                        parser.nextToken();

                        if ("proportion".equals(neanderthalField)) {
                            proportion = Double.parseDouble(parser.getText());
                        }
                    }
                }
            }
            return proportion;
        }
        catch (IOException e) {
            logger.warn("could not parse neanderthal proportion", e);
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
        return -1d;
    }

    @Override
    public List<Relative> parseRelatives(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
            parser.nextToken();

            List<Relative> relatives = new ArrayList<Relative>();

            String profileId = null;
            String matchId = null;
            double similarity = 0.0d;
            int sharedSegments = 0;
            Relationship relationship = null;
            Relationship userRelationship = null;
            Set<Relationship> range = new HashSet<Relationship>();

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();

                if ("id".equals(field)) {
                    profileId = parser.getText();
                }
                else if ("relatives".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String relativeField = parser.getCurrentName();
                            parser.nextToken();

                            if ("match_id".equals(relativeField)) {
                                matchId = parser.getText();
                            }
                            else if ("similarity".equals(relativeField)) {
                                similarity = Double.parseDouble(parser.getText());
                            }
                            else if ("shared_segments".equals(relativeField)) {
                                sharedSegments = parser.getIntValue();
                            }
                            else if ("relationship".equals(relativeField)) {
                                relationship = Relationship.fromDescription(parser.getText());
                            }
                            else if ("user_relationship_code".equals(relativeField)) {
                                String code = parser.getText();
                                userRelationship = code == "null" ? null : Relationship.fromCode(Integer.parseInt(code));
                            }
                            else if ("predicted_relationship_code".equals(relativeField)) {
                                if (relationship == null) {
                                    String code = parser.getText();
                                    relationship = code == "null" ? null : Relationship.fromCode(Integer.parseInt(code));
                                }
                            }
                            else if ("range".equals(relativeField)) {
                                while (parser.nextToken() != JsonToken.END_ARRAY) {
                                    range.add(Relationship.fromDescription(parser.getText()));
                                }
                            }
                            // ignored nested fields
                            else if ("family_locations".equals(relativeField)) {
                                while (parser.nextToken() != JsonToken.END_ARRAY) {
                                    // ignore
                                }
                            }
                            else if ("family_surnames".equals(relativeField)) {
                                while (parser.nextToken() != JsonToken.END_ARRAY) {
                                    // ignore
                                }
                            }
                            else if ("profile_picture_urls".equals(relativeField)) {
                                while (parser.nextToken() != JsonToken.END_OBJECT) {
                                    // ignore
                                }
                            }
                        }
                    }
                    relatives.add(new Relative(profileId, matchId, similarity, sharedSegments, relationship, userRelationship, range));
                    matchId = null;
                    similarity = 0.0d;
                    sharedSegments = 0;
                    relationship = null;
                    userRelationship = null;
                    range.clear();
                }
            }
            return relatives;
        }
        catch (IOException e) {
            logger.warn("could not parse relatives", e);
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

    @Override
    public List<Risk> parseRisks(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
            parser.nextToken();

            String id = null;
            String reportId = null;
            String description = null;
            double risk = 0.0d;
            double populationRisk = 0.0d;
            List<Risk> risks = new ArrayList<Risk>();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();

                if ("id".equals(field)) {
                    id = parser.getText();
                }
                else if ("risks".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String riskField = parser.getCurrentName();
                            parser.nextToken();

                            if ("report_id".equals(riskField)) {
                                reportId = parser.getText();
                            }
                            else if ("description".equals(riskField)) {
                                description = parser.getText();
                            }
                            else if ("risk".equals(riskField)) {
                                risk = Double.parseDouble(parser.getText());
                            }
                            else if ("population_risk".equals(riskField)) {
                                populationRisk = Double.parseDouble(parser.getText());
                            }
                        }
                        risks.add(new Risk(id, reportId, description, risk, populationRisk));
                        reportId = null;
                        description = null;
                        risk = 0.0d;
                        populationRisk = 0.0d;
                    }
                }
            }
            return risks;
        }
        catch (IOException e) {
            logger.warn("could not parse risks", e);
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

    @Override
    public List<Carrier> parseCarriers(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
            parser.nextToken();

            String id = null;
            String reportId = null;
            String description = null;
            int mutations = 0;
            List<Carrier> carriers = new ArrayList<Carrier>();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();

                if ("id".equals(field)) {
                    id = parser.getText();
                }
                else if ("carriers".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String carrierField = parser.getCurrentName();
                            parser.nextToken();

                            if ("report_id".equals(carrierField)) {
                                reportId = parser.getText();
                            }
                            else if ("description".equals(carrierField)) {
                                description = parser.getText();
                            }
                            else if ("mutations".equals(carrierField)) {
                                mutations = Integer.parseInt(parser.getText());
                            }
                        }
                        carriers.add(new Carrier(id, reportId, description, mutations));
                        reportId = null;
                        description = null;
                        mutations = 0;
                    }
                }
            }
            return carriers;
        }
        catch (IOException e) {
            logger.warn("could not parse carriers", e);
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

    @Override
    public List<DrugResponse> parseDrugResponses(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
            parser.nextToken();

            String id = null;
            String reportId = null;
            String description = null;
            String status = null;
            List<DrugResponse> drugResponses = new ArrayList<DrugResponse>();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();

                if ("id".equals(field)) {
                    id = parser.getText();
                }
                else if ("drug_responses".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String drugResponseField = parser.getCurrentName();
                            parser.nextToken();

                            if ("report_id".equals(drugResponseField)) {
                                reportId = parser.getText();
                            }
                            else if ("description".equals(drugResponseField)) {
                                description = parser.getText();
                            }
                            else if ("status".equals(drugResponseField)) {
                                status = parser.getText();
                            }
                        }
                        drugResponses.add(new DrugResponse(id, reportId, description, status));
                        reportId = null;
                        description = null;
                        status = null;
                    }
                }
            }
            return drugResponses;
        }
        catch (IOException e) {
            logger.warn("could not parse drug responses", e);
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

    @Override
    public List<Trait> parseTraits(final InputStream inputStream) {
        checkNotNull(inputStream);
        JsonParser parser = null;
        try {
            parser = jsonFactory.createParser(inputStream);
            parser.nextToken();

            String id = null;
            String reportId = null;
            String description = null;
            String trait = null;
            Set<String> possibleTraits = new HashSet<String>();
            List<Trait> traits = new ArrayList<Trait>();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();

                if ("id".equals(field)) {
                    id = parser.getText();
                }
                else if ("traits".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String traitField = parser.getCurrentName();
                            parser.nextToken();

                            if ("report_id".equals(traitField)) {
                                reportId = parser.getText();
                            }
                            else if ("description".equals(traitField)) {
                                description = parser.getText();
                            }
                            else if ("trait".equals(traitField)) {
                                trait = parser.getText();
                            }
                            else if ("possible_traits".equals(traitField)) {
                                while (parser.nextToken() != JsonToken.END_ARRAY) {
                                    possibleTraits.add(parser.getText());
                                }
                            }
                        }
                        traits.add(new Trait(id, reportId, description, trait, possibleTraits));
                        reportId = null;
                        description = null;
                        trait = null;
                        possibleTraits.clear();
                    }
                }
            }
            return traits;
        }
        catch (IOException e) {
            logger.warn("could not parse traits", e);
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