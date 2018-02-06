package com.vise.xsnow.net;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class UnSafeHostnameVerifier implements HostnameVerifier {
        private String host;

        public UnSafeHostnameVerifier(String host) {
            this.host = host;
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
}