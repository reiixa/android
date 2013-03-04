/**
 * Copyright (C) Posten Norge AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.digipost.android.api;

import no.digipost.android.model.Account;
import no.digipost.android.model.Documents;
import no.digipost.android.model.Letter;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ApiAccess {
	private String json;
	private JSONObject jsonob;
	static int filesize = 0;

	public ApiAccess() {

	}

	public Account getPrimaryAccount(final String access_token) {
		return (Account) JSONConverter.processJackson(Account.class, getApiJsonString(access_token, ApiConstants.URL_API));
	}

	public Documents getDocuments(final String access_token, final String uri) {
		return (Documents) JSONConverter.processJackson(Documents.class, getApiJsonString(access_token, uri));
	}

	public String getApiJsonString(final String access_token, final String uri) {
		Client client = Client.create();

		ClientResponse cr = client
				.resource(uri)
				.header(ApiConstants.ACCEPT, ApiConstants.APPLICATION_VND_DIGIPOST_V2_JSON)
				.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token)
				.get(ClientResponse.class);

		return JSONConverter.getJsonStringFromInputStream(cr.getEntityInputStream());
	}

	public Letter getMovedDocument(final String access_token, final String uri, final JSONObject json) {
		return (Letter) JSONConverter.processJackson(Letter.class, moveLetter(access_token, uri, json));
	}

	public String moveLetter(final String access_token, final String uri, final JSONObject json) {
		jsonob = json;
		// ServiceFinder.setIteratorProvider(new
		// AndroidServiceIteratorProvider());
		Client client = Client.create();
		WebResource r = client.resource(uri);

		ClientResponse cr = r
				.header(ApiConstants.CONTENT_TYPE, ApiConstants.APPLICATION_VND_DIGIPOST_V2_JSON)
				.header(ApiConstants.ACCEPT, ApiConstants.APPLICATION_VND_DIGIPOST_V2_JSON)
				.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token)
				.post(ClientResponse.class, jsonob.toString());

		System.out.println("JSON: " + jsonob.toString());
		System.out.println("URI: " + uri);

		return JSONConverter.getJsonStringFromInputStream(cr.getEntityInputStream());
	}

	public byte[] getDocumentContent(final String access_token, final String uri) {
		Client client = Client.create();

		ClientResponse cr = client
				.resource(uri)
				.header(ApiConstants.ACCEPT, ApiConstants.CONTENT_OCTET_STREAM)
				.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token)
				.get(ClientResponse.class);

		return JSONConverter.inputStreamtoByteArray(filesize, cr.getEntityInputStream());
	}

	public String getDocumentHTML(final String access_token, final String uri) {
		Client client = Client.create();

		ClientResponse cr = client
				.resource(uri)
				.header(ApiConstants.ACCEPT, ApiConstants.CONTENT_OCTET_STREAM)
				.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token)
				.get(ClientResponse.class);

		return JSONConverter.getJsonStringFromInputStream(cr.getEntityInputStream());
	}
}