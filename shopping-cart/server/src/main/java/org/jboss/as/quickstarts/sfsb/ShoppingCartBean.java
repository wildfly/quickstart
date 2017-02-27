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
package org.jboss.as.quickstarts.sfsb;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.Remove;
import javax.ejb.Stateful;

/**
 * @author Serge Pagop
 */
@Stateful
public class ShoppingCartBean implements ShoppingCart {

    private static final Logger LOGGER = Logger.getLogger(ShoppingCartBean.class.toString());

    private Map<String, Integer> cart = new HashMap<>();

    public void buy(String product, int quantity) {
        if (cart.containsKey(product)) {
            int currentQuantity = cart.get(product);
            currentQuantity += quantity;
            cart.put(product, currentQuantity);
        } else {
            cart.put(product, quantity);
        }
    }

    public Map<String, Integer> getCartContents() {
        return cart;
    }

    @Remove
    public void checkout() {
        LOGGER.info("implementing checkout() left as exercise for the reader!");
    }
}
