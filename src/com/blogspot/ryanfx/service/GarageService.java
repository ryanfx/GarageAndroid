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
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import com.blogspot.ryanfx.application.GarageApplication;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public abstract class GarageService extends IntentService{
	public static final String AUTH_TOKEN = "AUTH_TOKEN";
	public static final String INTENT_TOGGLE = "com.blogspot.ryanfx.garage-toggle-result";
	public static final String INTENT_CLOSE  = "com.blogspot.ryanfx.garage-close-result";
	public static final String INTENT_STATE  = "com.blogspot.ryanfx.garage-state-result";
	public static final String INTENT_ERROR  = "com.blogspot.ryanfx.garage-error-result";
	public static final String EXTRA_HTTP_RESPONSE_CODE = "EXTRA_HTTP_RESPONSE_CODE";
	public static final String EXTRA_HTTP_RESPONSE_TEXT = "EXTRA_HTTP_RESPONSE_TEXT";
	private static final int TIMEOUT = 3000;
	private final boolean SECURE = true;

	protected  GarageApplication application;

	public GarageService(String name) {
		super(name);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = (GarageApplication) getApplication();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String host = application.getServerHost();
		int port = application.getServerPort();
		Intent broadcast = new Intent(intent.getAction());
		try {
			String protocol = SECURE ? "https" : "http";
			String baseString = protocol + "://" + host + ":" + port + "/GarageDoor/Garage/";
			HttpsURLConnection urlConnection = getRequestFromIntent(baseString, intent);
			int responseCode = urlConnection.getResponseCode();
			application.getAuthenticator().setSuccessfulConnectionMade();
			//getResponseCode or getInputStream signals to actually make the request
			if (responseCode == HttpStatus.SC_OK){
				String responseBody = convertStreamToString(urlConnection.getInputStream());
				urlConnection.getInputStream().close();
				broadcast.putExtra(EXTRA_HTTP_RESPONSE_TEXT, responseBody);
			}else {
				broadcast.putExtra(EXTRA_HTTP_RESPONSE_TEXT, "");
			}
			Log.i(GarageService.class.getName(), "Response Code: " + responseCode);
			broadcast.putExtra(EXTRA_HTTP_RESPONSE_CODE, responseCode);
		} catch (Exception e) {
			Log.e(getClass().getName(), "Exception", e);
			broadcast.putExtra(EXTRA_HTTP_RESPONSE_CODE, -1);
			broadcast.putExtra(EXTRA_HTTP_RESPONSE_TEXT, e.getMessage());
		}finally{
			sendBroadcast(broadcast);
		}
	}
	
	protected void setupConnectionProperties(HttpsURLConnection urlConnection) {
		urlConnection.setConnectTimeout(TIMEOUT);
	}

	protected static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	protected abstract HttpsURLConnection getRequestFromIntent(String baseString, Intent intent) throws ClientProtocolException, IOException;

}
