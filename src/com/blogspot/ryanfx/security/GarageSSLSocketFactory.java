/**
 * Copyright 2013 Ryan Shaw (ryanfx1@gmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.blogspot.ryanfx.security;

import java.io.InputStream;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import com.blogspot.ryanfx.R;
import android.content.Context;

public class GarageSSLSocketFactory {
        
        private static SSLSocketFactory instance;
        private static final String PASSWORD = "Password!";
        
        public static SSLSocketFactory getSSLSocketFactory(Context context){
                if (instance != null)
                        return instance;
                try {
                        KeyStore trusted = KeyStore.getInstance("BKS");
                        InputStream in = context.getResources().openRawResource(R.raw.mystore);
                        SSLContext sslContext  = null;
                        try {
                                trusted.load(in, PASSWORD.toCharArray());
                                TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                                tmf.init(trusted);
                                sslContext = SSLContext.getInstance("TLS");
                                sslContext.init(null, tmf.getTrustManagers(), null);
                        } finally {
                                in.close();
                        }
                        instance = sslContext.getSocketFactory();
                        return instance;
                } catch (Exception e) {
                        throw new AssertionError(e);
                }
        }
}