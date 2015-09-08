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
package org.jboss.as.quickstarts.cdi.decorator;

/**
 * A simple implementation for the StaffService interface.
 *
 * @author Ievgen Shulga
 */
public class StaffServiceImpl implements StaffService {

    @Override
    public Staff getStaff() {
        Staff staff = new Staff();
        staff.setPosition("Java Developer");
        staff.setBonus(100);
        return staff;
    }

    /**
     * CDI decorator can be abstract class and does not implement all methods of the StaffServiceImpl class.
     * {@link StaffServiceDecorator} are abstract and do not wrap deleteStaff(Staff staff) method.
     *
     */
    @Override
    public void deleteStaff(Staff staff) {
        // Some implementation
    }

}
