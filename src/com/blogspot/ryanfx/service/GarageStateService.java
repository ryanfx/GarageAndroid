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

import android.content.Intent;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.client.ClientProtocolException;
import com.blogspot.ryanfx.security.GarageSSLSocketFactory;

public class GarageStateService extends GarageService {

	public GarageStateService() {
		super("GarageStateService");
	}

	protected HttpsURLConnection getRequestFromIntent(String urlAddress, Intent intent)
			throws ClientProtocolException, IOException {
		if (intent.getAction().equals(GarageService.INTENT_STATE)) {
			URL url = new URL(urlAddress + "state");
			HttpsURLConnection httpsurlconnection = (HttpsURLConnection) url.openConnection();
			httpsurlconnection.setRequestMethod("GET");
			setupConnectionProperties(httpsurlconnection);
			httpsurlconnection.setSSLSocketFactory(GarageSSLSocketFactory.getSSLSocketFactory(application));
			return httpsurlconnection;
		} else {
			throw new IllegalArgumentException(
					"Invalid action passed: " + intent.getAction());
		}
	}
}
