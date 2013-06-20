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
package com.blogspot.ryanfx.service;

import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

import com.blogspot.ryanfx.security.GarageSSLSocketFactory;

import android.content.Intent;

public class GarageToggleService extends GarageService{
        
        public GarageToggleService() {
                super("GarageToggleService");
        }
        
        @Override
        protected HttpsURLConnection getRequestFromIntent(String baseString, Intent intent) throws ClientProtocolException, IOException {
                HttpsURLConnection urlConnection = null;
                URL url = null;
                if (intent.getAction().equals(INTENT_TOGGLE)){
                        url = new URL(baseString + "toggle");
                        urlConnection = (HttpsURLConnection) url.openConnection();
                }else if (intent.getAction().equals(INTENT_CLOSE)){
                        url = new URL(baseString + "close");
                        urlConnection = (HttpsURLConnection) url.openConnection();
                }else{
                        throw new IllegalArgumentException("Invalid action passed: " + intent.getAction());
                }
                urlConnection.setRequestMethod(HttpPost.METHOD_NAME);
                setupConnectionProperties(urlConnection);
                urlConnection.setSSLSocketFactory(GarageSSLSocketFactory.getSSLSocketFactory(application));
                return urlConnection;
        }

}