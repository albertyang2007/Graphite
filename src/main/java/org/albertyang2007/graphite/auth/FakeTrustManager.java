package org.albertyang2007.graphite.auth;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class FakeTrustManager implements X509TrustManager {
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public boolean isClientTrusted(X509Certificate[] cert) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] cert) {
        return true;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        //attack: MITM-Attacker can inject arbitrary certificate
        //if you find a customized TrustManager, chances are high it
        //breaks certificate validation

        //certs[0].checkValidity();
        //attack: MITM-Attacker can inject arbitrary certificate as long
        //as it did not expire
        //Usability Problem: name of cert.checkValidity() is misleading!
    }

}
