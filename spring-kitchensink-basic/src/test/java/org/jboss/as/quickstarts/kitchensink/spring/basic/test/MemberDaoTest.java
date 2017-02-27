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
package org.jboss.as.quickstarts.kitchensink.spring.basic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.jboss.as.quickstarts.kitchensink.spring.basic.data.MemberDao;
import org.jboss.as.quickstarts.kitchensink.spring.basic.model.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml",
        "classpath:/META-INF/spring/applicationContext.xml"})
@Transactional
@Rollback
public class MemberDaoTest {
    @Autowired
    private MemberDao memberDao;

    @Test
    public void testFindById() {
        Member member = memberDao.findById(0L);

        assertEquals("John Smith", member.getName());
        assertEquals("john.smith@mailinator.com", member.getEmail());
        assertEquals("2125551212", member.getPhoneNumber());
        return;
    }

    @Test
    public void testFindByEmail() {
        Member member = memberDao.findByEmail("john.smith@mailinator.com");

        assertEquals("John Smith", member.getName());
        assertEquals("john.smith@mailinator.com", member.getEmail());
        assertEquals("2125551212", member.getPhoneNumber());
        return;
    }

    @Test
    public void testRegister() {
        Member member = new Member();
        member.setEmail("jane.doe@mailinator.com");
        member.setName("Jane Doe");
        member.setPhoneNumber("2125552121");

        memberDao.register(member);
        Long id = member.getId();
        assertNotNull(id);

        assertEquals(2, memberDao.findAllOrderedByName().size());
        Member newMember = memberDao.findById(id);

        assertEquals("Jane Doe", newMember.getName());
        assertEquals("jane.doe@mailinator.com", newMember.getEmail());
        assertEquals("2125552121", newMember.getPhoneNumber());
        return;
    }

    @Test
    public void testFindAllOrderedByName() {
        Member member = new Member();
        member.setEmail("jane.doe@mailinator.com");
        member.setName("Jane Doe");
        member.setPhoneNumber("2125552121");
        memberDao.register(member);

        List<Member> members = memberDao.findAllOrderedByName();
        assertEquals(2, members.size());
        Member newMember = members.get(0);

        assertEquals("Jane Doe", newMember.getName());
        assertEquals("jane.doe@mailinator.com", newMember.getEmail());
        assertEquals("2125552121", newMember.getPhoneNumber());
        return;
    }
}
