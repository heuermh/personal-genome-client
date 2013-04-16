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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

/**
 * Locations.
 */
@Immutable
public final class Locations {
    private static final Map<String, Location> LOCATIONS;

    static {
        BufferedReader reader = null;
        HashMap<String, Location> map = new HashMap<String, Location>(1200000);
        try {
            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(Locations.class.getResourceAsStream("snps.data.gz"))));
            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] tokens = line.split("\t");
                int index = Integer.parseInt(tokens[0]) * 2;
                String location = tokens[1];
                String chromosome = tokens[2];
                int position = Integer.parseInt(tokens[3]);
                map.put(location, new Location(index, chromosome, position));
            }
        }
        catch (IOException e) {
            // ignore
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
        LOCATIONS = ImmutableMap.copyOf(map);
    }

    public static Iterable<String> locations() {
        return LOCATIONS.keySet();
    }

    public static int index(final String location) {
        checkNotNull(location);
        return LOCATIONS.containsKey(location) ? LOCATIONS.get(location).getIndex() : -1;
    }

    public static String chromosome(final String location) {
        checkNotNull(location);
        return LOCATIONS.containsKey(location) ? LOCATIONS.get(location).getChromosome() : null;
    }

    public static int position(final String location) {
        checkNotNull(location);
        return LOCATIONS.containsKey(location) ? LOCATIONS.get(location).getPosition() : -1;
    }

    /**
     * Location.
     */
    private static class Location {
        private final int index;
        private final String chromosome;
        private final int position;

        Location(final int index, final String chromosome, final int position) {
            this.index = index;
            this.chromosome = chromosome;
            this.position = position;
        }

        int getIndex() {
            return index;
        }

        String getChromosome() {
            return chromosome;
        }

        int getPosition() {
            return position;
        }
    }
}