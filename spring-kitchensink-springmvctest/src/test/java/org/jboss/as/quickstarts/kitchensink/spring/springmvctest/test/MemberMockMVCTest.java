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
package org.jboss.as.quickstarts.kitchensink.spring.springmvctest.test;

import static org.junit.Assert.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.jboss.as.quickstarts.kitchensink.spring.springmvctest.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:test-context.xml",
        "classpath:/META-INF/spring/applicationContext.xml" })
public class MemberMockMVCTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private RestTemplate template;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.template = new RestTemplate(new MockMvcClientHttpRequestFactory(mockMvc));
    }

    @Test
    public void getAccountClient() throws Exception {
        Member member = new Member();
        member.setEmail("john.smith@mailinator.com");
        member.setId((long) 0);
        member.setName("John Smith");
        member.setPhoneNumber("2125551212");
        Member returnedMember = this.template.getForObject("/rest/members/0", Member.class);
        assertEquals(member.getEmail(), returnedMember.getEmail());
        assertEquals(member.getId(), returnedMember.getId());
        assertEquals(member.getName(), returnedMember.getName());
        assertEquals(member.getPhoneNumber(), returnedMember.getPhoneNumber());
    }

    @Test
    public void getAccount() throws Exception {
        this.mockMvc.perform(get("/rest/members/0").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith("application/json"))
            .andExpect(jsonPath("$.name").value("John Smith"));
    }
}
