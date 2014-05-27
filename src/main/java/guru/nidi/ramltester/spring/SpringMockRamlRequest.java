/*
 * Copyright (C) 2014 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.ramltester.spring;

import guru.nidi.ramltester.core.RamlRequest;
import guru.nidi.ramltester.util.Values;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Enumeration;

/**
 *
 */
public class SpringMockRamlRequest implements RamlRequest {
    private final MockHttpServletRequest delegate;

    public SpringMockRamlRequest(MockHttpServletRequest delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getRequestUrl(String baseUri) {
//        if (servletUri != null) {
//            final UriComponents uri = UriComponents.fromHttpUrl(servletUri);
//            final String scheme = uri.getScheme();
//            if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
//                throw new IllegalArgumentException("Servlet URI must start with http(s)://");
//            }
//            delegate.setScheme(scheme);
//            delegate.setServerName(uri.getHost());
//            if (uri.getPort() != null) {
//                delegate.setServerPort(uri.getPort());
//            }
//            delegate.setContextPath(uri.getPath());
//        }
//
//        final StringBuffer requestURL = delegate.getRequestURL();
//        final int pathStart = requestURL.length() - delegate.getRequestURI().length();
//        return requestURL.substring(0, pathStart) + delegate.getContextPath() + requestURL.substring(pathStart);
        return baseUri != null ? baseUri + delegate.getPathInfo() : delegate.getRequestURL().toString();
    }

    @Override
    public String getMethod() {
        return delegate.getMethod();
    }

    @Override
    public Values getQueryValues() {
        return new Values(delegate.getParameterMap());
    }

    @Override
    public Values getHeaderValues() {
        final Values headers = new Values();
        final Enumeration<String> names = delegate.getHeaderNames();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            final Enumeration<String> values = delegate.getHeaders(name);
            while (values.hasMoreElements()) {
                headers.addValue(name, values.nextElement());
            }
        }
        return headers;
    }

    @Override
    public String getContent() {
        return null;
    }
}
