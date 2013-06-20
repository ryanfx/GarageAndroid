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
package com.blogspot.ryanfx.activity;

import com.blogspot.ryanfx.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class ConfigurationActivity extends PreferenceActivity {

	private ListPreference listPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.server_config);
		listPreference = (ListPreference) findPreference("user");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//When the Activity resumes, refill the list with an updated account list
		fillUserList();
	}

	private void fillUserList() {
		String[] accountList = getAccounts();
		listPreference.setEntries(accountList);
		listPreference.setEntryValues(accountList);
	}

	private String[] getAccounts() {
		AccountManager manager = AccountManager.get(this);
		// Only request the user accounts associated with Google.
		Account[] accounts = manager.getAccountsByType("com.google");
		String[] accountList = new String[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			accountList[i] = accounts[i].name;
		}
		return accountList;
	}

}