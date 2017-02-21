/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.wsba.participantcompletion.simple;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a simple Set collection.
 *
 * It is beyond the scope of this quickstart to provide a full bullet-proof implementation of a Resource Manager. Therefore a
 * simple Mock implementation is provided.
 *
 * You should refer to the XTS demo project, shipped as part of the Narayana project, for a more complete example.
 *
 * @author paul.robinson@redhat.com, 2011-12-21
 */
public class MockSetManager {

    private static final Set<String> set = new HashSet<>();

    /**
     * Add a value to the set
     *
     * @param item Item to add to the set.
     * @throws AlreadyInSetException if the item is already in the set.
     */
    public static void add(String item) throws AlreadyInSetException {
        synchronized (set) {

            if (set.contains(item)) {
                throw new AlreadyInSetException("item '" + item + "' is already in the set.");
            }
            set.add(item);
        }
    }

    /**
     * Persist sufficient data, such that the add operation can be undone or made permanent when told to do so by a call to
     * commit or rollback.
     *
     * As this is a mock implementation, the method does nothing and always returns true.
     *
     * @return true if the SetManager is able to commit and the required state was persisted. False otherwise.
     */
    public static boolean prepare() {
        return true;
    }

    /**
     * Make the outcome of the add operation permanent.
     *
     * As this is a mock implementation, the method does nothing.
     */
    public static void commit() {
        System.out
            .println("[SERVICE] Commit the backend resource (e.g. commit any changes to databases so that they are visible to others)");
    }

    /**
     * Undo any changes made by the add operation.
     *
     * As this is a mock implementation, the method needs to be informed of how to undo the work of the add operation. Typically
     * resource managers will already know this information.
     *
     * @param item The item to remove from the set in order to undo the effects of the add operation.
     */
    public static void rollback(String item) {
        System.out.println("[SERVICE] Compensate the backend resource by removing '" + item
            + "' from the set (e.g. undo any changes to databases that were previously made visible to others)");
        synchronized (set) {

            set.remove(item);
        }

    }

    /**
     * Query the set to see if it contains a particular value.
     *
     * @param value the value to check for.
     * @return true if the value was present, false otherwise.
     */
    public static boolean isInSet(String value) {
        return set.contains(value);
    }

    /**
     * Empty the set
     */
    public static void clear() {
        set.clear();
    }
}
