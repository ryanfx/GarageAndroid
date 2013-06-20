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

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import android.util.Log;

/**
 * Why is this needed? https://code.google.com/p/android/issues/detail?id=7058
 * Google's documentation is wrong (as of 6/19/2013).  If you follow the documentation
 * below, your requests whose responses are 401 will infinitely loop
 * until the application or service exits.
 * http://developer.android.com/reference/java/net/HttpURLConnection.html
 *
 */
public class FailingAuthenticator extends Authenticator{

	//Make a ThreadLocal attmpted boolean.  This will let every thread try once before it fails
	private ThreadLocal<Boolean> attempted  = new ThreadLocal<Boolean>();
	private String username;
	private char[] password;
	
	public FailingAuthenticator(String username,  char[] password){
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		//If calling thread has not invoked this method yet, or it is in a "ready" state
		if (attempted.get() == null || !attempted.get()){
			Log.i(getClass().getName(), "First call from thread " + Thread.currentThread().getId());
			//Set attempted to true, so a further call will return null and will not retry
			attempted.set(true);
			return new PasswordAuthentication(username, password); 
		}
		//A previous try has been attempted.  Return null to cancel retries and reset this thread
		Log.i(getClass().getName(), "Second call (now resetting) from thread " + Thread.currentThread().getId());
		attempted.set(false);
		return null;
	}
	
	/**
	 * Call this when a successful connection has been made to reset the attempted variable.
	 */
	public void setSuccessfulConnectionMade(){
		attempted.set(false);
	}
}