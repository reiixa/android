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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import no.digipost.android.authentication.OAuth2;
import no.digipost.android.authentication.Secret;
import no.digipost.android.gui.NetworkConnection;
import no.digipost.android.model.Account;
import no.digipost.android.model.Documents;
import no.digipost.android.model.Letter;
import no.digipost.android.model.Receipts;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jettison.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

public class ApiAccess {
	private String json;
	private JSONObject jsonob;
	static int filesize = 0;
	private final Context context;
	private final NetworkConnection networkConnection;

	public ApiAccess(final Context context) {
		this.context = context;
		networkConnection = new NetworkConnection(context);
	}

	public Account getPrimaryAccount(final String access_token) throws NetworkErrorException {
		return (Account) JSONConverter.processJackson(Account.class, getApiJsonString(access_token, ApiConstants.URL_API));
	}

	public Documents getDocuments(final String access_token, final String uri) throws NetworkErrorException {
		return (Documents) JSONConverter.processJackson(Documents.class, getApiJsonString(access_token, uri));
	}

	public Receipts getReceipts(final String access_token, final String uri) throws NetworkErrorException {
		return (Receipts) JSONConverter.processJackson(Receipts.class, getApiJsonString(access_token, uri));
	}

	public String getApiJsonString(final String access_token, final String uri) throws NetworkErrorException {

		Client client = Client.create();

		ClientResponse cr = client
				.resource(uri)
				.header(ApiConstants.ACCEPT, ApiConstants.APPLICATION_VND_DIGIPOST_V2_JSON)
				.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token)
				.get(ClientResponse.class);

		try {
			networkConnection.checkHttpStatusCode(cr.getStatus());
		} catch (IllegalStateException e) {
			OAuth2.updateRefreshTokenSuccess(context);
			return getApiJsonString(Secret.ACCESS_TOKEN, uri);
		}

		return JSONConverter.getJsonStringFromInputStream(cr.getEntityInputStream());
	}

	public Letter getMovedDocument(final String access_token, final String uri, final StringEntity json) throws ClientProtocolException,
			IOException, UniformInterfaceException, ClientHandlerException, URISyntaxException, IllegalStateException,
			NetworkErrorException {
		return (Letter) JSONConverter.processJackson(Letter.class, moveLetter(access_token, uri, json));
	}

	public String moveLetter(final String access_token, final String uri, final StringEntity json) throws ClientProtocolException,
			IOException, UniformInterfaceException, ClientHandlerException, URISyntaxException, IllegalStateException,
			NetworkErrorException {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost();
		post.setURI(new URI(uri));
		post.addHeader(ApiConstants.CONTENT_TYPE, ApiConstants.APPLICATION_VND_DIGIPOST_V2_JSON);
		post.addHeader(ApiConstants.ACCEPT, ApiConstants.APPLICATION_VND_DIGIPOST_V2_JSON);
		post.addHeader(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token);
		post.setEntity(json);

		HttpResponse response = httpClient.execute(post);

		try {
			networkConnection.checkHttpStatusCode(response.getStatusLine().getStatusCode());
		} catch (IllegalStateException e) {
			OAuth2.updateRefreshTokenSuccess(context);
			return moveLetter(Secret.ACCESS_TOKEN, uri, json);
		}

		return JSONConverter.getJsonStringFromInputStream(response.getEntity().getContent());

	}

	public byte[] getDocumentContent(final String access_token, final String uri) throws NetworkErrorException {
		Client client = Client.create();

		ClientResponse cr = client
				.resource(uri)
				.header(ApiConstants.ACCEPT, ApiConstants.CONTENT_OCTET_STREAM)
				.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token)
				.get(ClientResponse.class);

		try {
			networkConnection.checkHttpStatusCode(cr.getStatus());
		} catch (IllegalStateException e) {
			OAuth2.updateRefreshTokenSuccess(context);
			return getDocumentContent(Secret.ACCESS_TOKEN, uri);
		}

		return JSONConverter.inputStreamtoByteArray(filesize, cr.getEntityInputStream());
	}

	public String getDocumentHTML(final String access_token, final String uri) throws NetworkErrorException {
		Client client = Client.create();

		ClientResponse cr = client
				.resource(uri)
				.header(ApiConstants.ACCEPT, ApiConstants.CONTENT_OCTET_STREAM)
				.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token)
				.get(ClientResponse.class);

		try {
			networkConnection.checkHttpStatusCode(cr.getStatus());
		} catch (IllegalStateException e) {
			OAuth2.updateRefreshTokenSuccess(context);
			return getDocumentHTML(Secret.ACCESS_TOKEN, uri);
		}

		return JSONConverter.getJsonStringFromInputStream(cr.getEntityInputStream());
	}

	public String getReceiptHTML(final String access_token, final String uri) throws NetworkErrorException {
		Client client = Client.create();

		ClientResponse cr = client
				.resource(uri)
				.header(ApiConstants.ACCEPT, ApiConstants.TEXT_HTML)
				.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + access_token)
				.get(ClientResponse.class);

		try {
			networkConnection.checkHttpStatusCode(cr.getStatus());
		} catch (IllegalStateException e) {
			OAuth2.updateRefreshTokenSuccess(context);
			return getDocumentHTML(Secret.ACCESS_TOKEN, uri);
		}

		return JSONConverter.getJsonStringFromInputStream(cr.getEntityInputStream());
	}
}