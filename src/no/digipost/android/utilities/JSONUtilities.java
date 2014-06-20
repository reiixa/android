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

package no.digipost.android.utilities;

import android.content.Context;

import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import no.digipost.android.R;
import no.digipost.android.api.exception.DigipostClientException;
import no.digipost.android.model.Document;
import no.digipost.android.model.Folder;

public class JSONUtilities {
	public static String getJsonStringFromInputStream(final InputStream inputStream) {
		String content = "";

		if (inputStream != null) {
			Writer writer = new StringWriter();
			int buffer_size = 1024;
			char[] buffer = new char[buffer_size];

			try {
				Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), buffer_size);
				int n;

				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}

				inputStream.close();
				reader.close();
				writer.close();
			} catch (Exception e) {
			}
			content = writer.toString();
		}
		return content;
	}

	public static <T> Object processJackson(final Class<T> type, final String data) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory fact = new JsonFactory();
		Object jacksonObject = null;

		try {
			JsonParser jp = fact.createJsonParser(data);
			jacksonObject = objectMapper.readValue(jp, type);
		} catch (JsonParseException e) {
			// Ignore
		} catch (IOException e) {
			// Ignore
		} catch (Exception e) {
			// Ignore
		}

		return jacksonObject;
	}

	public static <T> Object processJackson(final Class<T> type, final InputStream data) {
		return processJackson(type, getJsonStringFromInputStream(data));
	}

	public static StringEntity createJsonFromJackson(final Object object) {
		// ignore-test

        String[] ignore = { "link","folderId","contentUri", "documents","changeUri","deleteUri", "updateUri", "organizationLogo", "attachment", "openingReceiptUri",
                "selfUri", "uploadUri","settingsUri" };

        if (object instanceof Document) {
            if (((Document) object).getFolderId() != null) {
                ignore = new String[]{"link", "contentUri", "changeUri","deleteUri", "updateUri", "organizationLogo", "attachment", "openingReceiptUri",
                        "selfUri", "settingsUri"};
            }
        }else if(object instanceof Folder){
            ignore = new String[]{"link","contentUri", "documents","changeUri","deleteUri", "updateUri", "organizationLogo", "attachment", "openingReceiptUri",
                    "selfUri", "uploadUri","settingsUri" };

        }

        ObjectMapper objectMapper = new ObjectMapper();
        FilterProvider filters = new SimpleFilterProvider().addFilter("toJSON", SimpleBeanPropertyFilter.serializeAllExcept(ignore));

        StringEntity output = null;
        try {
            output = new StringEntity(objectMapper.writer(filters).writeValueAsString(object));
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            // Ignore
        } catch (JsonMappingException e) {
            e.printStackTrace();
            // Ignore
        } catch (IOException e) {
            e.printStackTrace();
            // Ignore
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
	}

	public static byte[] inputStreamtoByteArray(Context context, final int size, final InputStream data) throws DigipostClientException {
		InputStream is = data;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;

		try {
			byte[] byteArray = new byte[size];
			while ((nRead = is.read(byteArray, 0, byteArray.length)) != -1) {
				buffer.write(byteArray, 0, nRead);
			}
			buffer.flush();
		} catch (IOException e) {
			// Ignore
		} catch (OutOfMemoryError e) {
			throw new DigipostClientException(context.getString(R.string.error_inputstreamtobyarray));
		}
		return buffer.toByteArray();
	}
}
