package org.albertyang2007.graphite.auth;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

//refer to: 
//http://www.coderanch.com/t/207318/sockets/java/hold-Java-default-SSL-Trust
//http://blog.chenxiaosheng.com/posts/2013-12-26/java-use-self_signed_certificate.html
public class MyTrustManager implements javax.net.ssl.X509TrustManager {

    private X509TrustManager instance;

    public MyTrustManager() {
        KeyStore ks;
        try {
            ks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream("c:/users/albertyang/desktop/temp/trustedCerts"), "changeit".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            TrustManager tms[] = tmf.getTrustManagers();

            for (int i = 0; i < tms.length; i++) {
                if (tms[i] instanceof X509TrustManager) {
                    instance = (X509TrustManager) tms[i];
                    return;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return instance.getAcceptedIssuers();
    }

    public boolean isClientTrusted(X509Certificate[] cert) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] cert) {
        return true;
    }

    //Delegate to the default trust manager. 
    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
        try {
            instance.checkClientTrusted(certs, authType);
        } catch (CertificateException excep) {
            // do any special handling here, or rethrow exception.  
        }
    }

    //Delegate to the default trust manager. 
    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        try {
            instance.checkServerTrusted(certs, authType);
        } catch (CertificateException excep) {
            /*
             * Possibly pop up a dialog box asking whether to trust
             * the cert chain.
             */
        }
    }

}
