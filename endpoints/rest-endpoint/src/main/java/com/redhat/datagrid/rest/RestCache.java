/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.redhat.datagrid.rest;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.lifecycle.ComponentStatus;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.util.Base64;
import org.infinispan.util.concurrent.NotifyingFuture;

/**
 * A cache working over REST (only few basic operations)
 *
 * @author Manik Surtani
 * 
 */
class RESTCache<K, V> implements Cache<K, V> {
    String cacheName;
    String basicUrl;

    private static final String GET = "GET";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    RESTCache(String cacheName, String restServerURL) {
       this.cacheName = cacheName;
       this.basicUrl = restServerURL + cacheName;
    }

    private String doOperation(String method, String key, Object value) {
       try {
          URL url = key == null ? new URL(basicUrl) : new URL(basicUrl + "/" + key);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod(method);
          connection.setRequestProperty("Content-Type","text/plain");
          int read = 0;
          byte[] buffer = new byte[1024 * 8];

          if (method.equals(PUT)) {
             connection.setDoOutput(true);
             String payload = Base64.encodeObject((Serializable) value);
             BufferedOutputStream output = new BufferedOutputStream(connection.getOutputStream());
             output.write(payload.getBytes());
             output.close();
          }

          connection.connect();
          InputStream responseBodyStream = connection.getInputStream();
          StringBuffer responseBody = new StringBuffer();
          while ((read = responseBodyStream.read(buffer)) != -1) {
             responseBody.append(new String(buffer, 0, read));
          }
          connection.disconnect();
          String response = responseBody.toString();
          return response;
       } catch (FileNotFoundException fnfe) {
          // Could be that the key being queried does not exist.  Return null.
          return null;
       } catch (Exception e) {
          throw new RuntimeException(e);
       }
    }

    private String toStringKey(Object key) {
       if (key instanceof String) {
          return (String) key;
       } else {
          throw new UnsupportedOperationException("RESTful cache only allows String keys!");
       }
    }

    @Override
    public void putForExternalRead(K k, V v) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void evict(K k) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Configuration getConfiguration() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean startBatch() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void endBatch(boolean b) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public String getName() {
       return cacheName;
    }

    @Override
    public String getVersion() {
       return "1.0";
    }

    @Override
    public EmbeddedCacheManager getCacheManager() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V put(K k, V v, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V putIfAbsent(K k, V v, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V replace(K k, V v, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean replace(K k, V v, V v1, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V put(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V putIfAbsent(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V replace(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean replace(K k, V v, V v1, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> putAsync(K k, V v) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> putAsync(K k, V v, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> putAsync(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<Void> putAllAsync(Map<? extends K, ? extends V> map) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<Void> putAllAsync(Map<? extends K, ? extends V> map, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<Void> putAllAsync(Map<? extends K, ? extends V> map, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<Void> clearAsync() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> putIfAbsentAsync(K k, V v) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> putIfAbsentAsync(K k, V v, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> putIfAbsentAsync(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> removeAsync(Object o) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<Boolean> removeAsync(Object o, Object o1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> replaceAsync(K k, V v) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> replaceAsync(K k, V v, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> replaceAsync(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<Boolean> replaceAsync(K k, V v, V v1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<Boolean> replaceAsync(K k, V v, V v1, long l, TimeUnit timeUnit) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<Boolean> replaceAsync(K k, V v, V v1, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public NotifyingFuture<V> getAsync(K k) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public AdvancedCache<K, V> getAdvancedCache() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void compact() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public ComponentStatus getStatus() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public int size() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean isEmpty() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean containsKey(Object key) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean containsValue(Object value) {
       throw new UnsupportedOperationException("Unsupported");
    }

    private Object decode(String s) {
       if (s == null) {
          return null;
       } else {
          Object o = Base64.decodeToObject(s);
          return o;
       }
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
       String stringKey = toStringKey(key);
       String stringValue = doOperation(GET, stringKey, null);
       return (V) decode(stringValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
       String stringKey = toStringKey(key);
       String stringValue = doOperation(PUT, stringKey, value);

       return (V) decode(stringValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove(Object key) {
       String stringKey = toStringKey(key);
       String stringValue = doOperation(DELETE, stringKey, null);

       return (V) decode(stringValue);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void clear() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Set<K> keySet() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Collection<V> values() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V putIfAbsent(K key, V value) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean remove(Object key, Object value) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V replace(K key, V value) {
       return put(key, value);
    }

    @Override
    public void start() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void stop() {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void addListener(Object o) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void removeListener(Object o) {
       throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Set<Object> getListeners() {
       throw new UnsupportedOperationException("Unsupported");
    }
 }