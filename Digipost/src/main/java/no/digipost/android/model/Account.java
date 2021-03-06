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
package no.digipost.android.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
	@JsonProperty
	private PrimaryAccount primaryAccount;

    @JsonProperty
    private ValidationRules validationRules;

    @JsonProperty
    private String authenticationlevel;

    @JsonProperty
    private String authenticationlevelIdporten;

    @JsonProperty
    private ArrayList<Mailbox> mailbox;

    @JsonProperty
    private String minimumAndroidVersion;

    public ArrayList<Mailbox> getMailbox() {
        return mailbox;
    }

    public Mailbox getMailboxByDigipostAddress(String digipostAddress) {
        for (Mailbox m : mailbox) {
            if (m.getDigipostaddress().equals(digipostAddress)) {
                return m;
            }
        }
        return null;
    }

    public int getMinimumAndroidVersion(){
       return minimumAndroidVersion != null ? Integer.parseInt(minimumAndroidVersion) : 0;
    }

    public int getAuthenticationlevel(){
        return Integer.parseInt(authenticationlevel);
    }

    public int getAuthenticationlevelIdporten(){
        return Integer.parseInt(authenticationlevelIdporten);
    }

    public PrimaryAccount getPrimaryAccount() {
		return primaryAccount;
	}

    public ValidationRules getValidationRules() {
        return validationRules;
    }
}
