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
package org.jboss.as.quickstarts.kitchensink.spring.matrixvariables.controller;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.jboss.as.quickstarts.kitchensink.spring.matrixvariables.data.MemberDao;
import org.jboss.as.quickstarts.kitchensink.spring.matrixvariables.data.MemberDaoImpl;
import org.jboss.as.quickstarts.kitchensink.spring.matrixvariables.model.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class MemberController {
    private static final Logger log = Logger.getLogger(MemberDaoImpl.class.getName());

    @Autowired
    private MemberDao memberDao;

    /*
     * When you go to http://localhost:8080/jboss-spring-kitchensink-matrixvariables/ this method is fired.  This is
     *   because of the class level @RequestMapping(value = "/").  This method does not specify the path and therefore it
     *   runs when you access the root level of the URI.
     * This method will access DAO to query the database and return all the Members.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String displaySortedMembers(Model model) {
        // "newMember" is the form commandName and it is being linked with the Member POJO that backs the forms attributes.
        model.addAttribute("newMember", new Member());
        // The list of Members are now available on the index.jsp as "members" to be iterated over and displayed.
        model.addAttribute("members", memberDao.findAllOrderedByName());
        return "index";
    }

    // Note that to enable the use of matrix variables, you must set the removeSemicolonContent property of RequestMappingHandlerMapping
    //  to false. By default it is set to true with the exception of the MVC namespace and the MVC Java config both of which automatically
    //  enable the use of matrix variables.
    // This will allow the Matrix Variables to be passed in because they use semicolons to separate each one.
    // This was done by creating a Configuration class and pointing to it in the jboss-as-spring-mvc-context.xml.
    @RequestMapping(value = "/mv/{filter}", method = RequestMethod.GET)
    public ModelAndView filteredMembers(@MatrixVariable(value = "n", pathVar = "filter", required = false, defaultValue = "") String n,
        @MatrixVariable(value = "e", pathVar = "filter", required = false, defaultValue = "") String e) {
        log.fine("filteredMembers @MatrixVariable n = " + n + ", e = " + e);
        ModelAndView model = new ModelAndView("index");
        model.addObject("newMember", new Member());
        List<Member> members = memberDao.findByNameAndEmail(n, e);
        // The list of Members are now available on the index.jsp as "members" to be iterated over and displayed.
        model.addObject("members", members);
        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerNewMember(@Valid @ModelAttribute("newMember") Member newMember, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            try {
                memberDao.register(newMember);
                return "redirect:/";
            } catch (UnexpectedRollbackException e) {
                model.addAttribute("members", memberDao.findAllOrderedByName());
                // Check the uniqueness of the email address
                if (emailAlreadyExists(newMember.getEmail())) {
                    model.addAttribute("error", "Unique Email Violation");
                } else {
                    model.addAttribute("error", e.getCause().getCause());
                }
                return "index";
            }
        } else {
            model.addAttribute("members", memberDao.findAllOrderedByName());
            return "index";
        }
    }

    /**
     * Checks if a member with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
     *
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean emailAlreadyExists(String email) {
        Member member = null;
        try {
            member = memberDao.findByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }
        return member != null;
    }

}
