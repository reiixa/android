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

package no.digipost.android.utilities;

import android.content.Context;
import android.os.AsyncTask;
import no.digipost.android.R;
import no.digipost.android.api.exception.DigipostApiException;
import no.digipost.android.api.exception.DigipostAuthenticationException;
import no.digipost.android.api.exception.DigipostInvalidTokenException;
import no.digipost.android.authentication.TokenEncryption;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class NetworkUtilities {
    public static final int HTTP_STATUS_SUCCESS = 200;
    public static final int HTTP_STATUS_CREATE = 201;
    public static final int HTTP_STATUS_NO_CONTENT = 204;
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

    public static final int SUCCESS = 0;
    public static final int BAD_REQUEST = 1;
    public static final int BAD_REQUEST_DELETE = 2;
    public static final String SUCCESS_NO_CONTENT = "SUCCESS_NO_CONTENT";

    public static void checkHttpStatusCode(Context context, final int statusCode) throws DigipostApiException, DigipostInvalidTokenException,
            DigipostAuthenticationException {

        if (statusCode == HTTP_STATUS_UNAUTHORIZED) {
            if (TokenEncryption.canUseRefreshTokens(context)) {
                throw new DigipostInvalidTokenException();
            } else {
                throw new DigipostAuthenticationException(context.getString(R.string.error_invalid_token));
            }
        } else if (statusCode == HTTP_STATUS_BAD_REQUEST) {
            throw new DigipostAuthenticationException(context.getString(R.string.error_bad_request));
        } else if (statusCode == HTTP_STATUS_INTERNAL_SERVER_ERROR) {
            throw new DigipostApiException(context.getString(R.string.error_digipost_api));
        } else if (statusCode == HTTP_STATUS_SUCCESS || statusCode == HTTP_STATUS_CREATE || statusCode == HTTP_STATUS_NO_CONTENT) {
            // OK, Ignore
        } else {
            throw new DigipostApiException(context.getString(R.string.error_your_network));
        }
    }

    public static boolean isOnline() {
        IsOnlineTask task = new IsOnlineTask();
        try {
            return task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class IsOnlineTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(final Void... params) {
            try {
                URL url = new URL("https://www.digipost.no/post/api/session");
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(3000);
                connection.connect();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
