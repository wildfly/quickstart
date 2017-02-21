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
package org.jboss.as.quickstarts.kitchensink.spring.controlleradvice.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jboss.as.quickstarts.kitchensink.spring.controlleradvice.data.MemberDao;
import org.jboss.as.quickstarts.kitchensink.spring.controlleradvice.model.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class MemberControllerAdvice {

    @Autowired
    private MemberDao memberDao;

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    //Global Exception Handler, removing the need for an extra Exception Handler Controller
    @ExceptionHandler(value = IOException.class)
    public ModelAndView exception(IOException e) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("error", getStackTrace(e));
        return model;
    }

    //Globally adding a date editor for all @RequestMapping methods
    @InitBinder
    public void binder(WebDataBinder dataBinder, WebRequest request) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        CustomDateEditor editor = new CustomDateEditor(format, true);
        dataBinder.registerCustomEditor(Date.class, editor);
    }

    //Global Attribute adder for all @RequestMapping methods
    @ModelAttribute("members")
    public List<Member> getMembers() {
        return memberDao.findAllOrderedByName();
    }
}
