package com.terragene.baxen.configuration;



import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;

import java.net.URI;

public class HttpGetWithBody extends HttpEntityEnclosingRequestBase {

        public HttpGetWithBody() {
            super();
        }

        public HttpGetWithBody(URI uri) {
            super();
            setURI(uri);
        }

        public HttpGetWithBody(String uri) {
            super();
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return HttpGet.METHOD_NAME;
        }
    }
