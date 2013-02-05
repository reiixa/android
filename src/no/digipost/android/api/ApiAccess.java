/**
 * Copyright (C) Posten Norge AS
 *	
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.digipost.android.api;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public class ApiAccess {

	public ApiAccess() {
	}

	public Account getPrimaryAccount(final String access_token) {
		return (Account) getApiObject(Account.class, access_token, ApiConstants.URL_API);
	}

	public Documents getDokuments(final String access_token, final String uri) {
		return (Documents) getApiObject(Documents.class, access_token, uri);
	}

	public <T> Object getApiObject(final Class<T> type, final String access_token, final String uri) {
		Client client = Client.create();

		Builder builder = client
				.resource(uri)
				.header(ApiConstants.ACCEPT, ApiConstants.APPLICATION_VND_DIGIPOST_V2_JSON)
				.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token);

		GetApiJsonStringTask apiJsonStringTask = new GetApiJsonStringTask();
		Object apiObject = null;

		try {
			String jsonString = apiJsonStringTask.execute(builder).get();
			apiObject = JSONConverter.processJackson(type, jsonString);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return apiObject;
	}

	private class GetApiJsonStringTask extends AsyncTask<Builder, Void, String> {
		@Override
		protected String doInBackground(final Builder... params) {
			InputStream is = params[0].get(ClientResponse.class).getEntityInputStream();
			return JSONConverter.getJsonStringFromInputStream(is);

		}
	}
}