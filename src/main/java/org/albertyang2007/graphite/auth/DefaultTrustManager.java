package org.albertyang2007.graphite.auth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class DefaultTrustManager {
    public static X509TrustManager getDefaultTrustManager() {
        TrustManagerFactory trustManagerFactory;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("c:/users/albertyang/desktop/temp/trustedCerts"), "changeit".toCharArray());
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(ks);

            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {

                if (trustManager instanceof X509TrustManager) {
                    X509TrustManager x509TrustManager = (X509TrustManager) trustManager;
                    return x509TrustManager;
                }
            }

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
