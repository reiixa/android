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

package no.digipost.android.constants;

public class ApplicationConstants {

	public static final String HMACSHA256 = "HmacSHA256";

	public static final String SCREENLOCK_CHOICE = "SCREENLOCK_CHOICE";
	public static final int SCREENLOCK_CHOICE_HAS_NO_BEEN_TAKEN_YET = 0;
	public static final int SCREENLOCK_CHOICE_NO = 1;
	public static final int SCREENLOCK_CHOICE_YES = 2;

    public static final int OK = 0;
    public static final int BAD_REQUEST = 1;
    public static final int BAD_REQUEST_DELETE = 2;


    public static final boolean FEATURE_SEND_TO_BANK_VISIBLE = true;

	public static final String ENCODING = "UTF-8";
	public static final String MIME = "text/html";
    public static final String NUMBER_OF_TIMES_APP_HAS_RUN = "numberOfTimesAppHasRun";
    public static final int NUMBER_OF_TIMES_DRAWER_SHOULD_OPEN = 1;

    public static final int MAILBOX = 0;
    public static final int RECEIPTS = 1;
    public static final int FOLDERS_LABEL = 2; //MINE MAPPER

    public static final String DRAWER_INBOX = "Postkassen";
    public static final String DRAWER_RECEIPTS = "E-Kvitteringer";

    public static final String DRAWER_MY_FOLDERS = "MINE MAPPER";
    public static final String DRAWER_MY_ACCOUNT = "MIN KONTO";

    public static final String DRAWER_CHANGE_ACCOUNT = "Bytt konto";
    public static final String DRAWER_SETTINGS = "Innstillinger";
    public static final String DRAWER_HELP ="Hjelp";
    public static final String DRAWER_LOGOUT = "Logg ut";

    public static final int numberOfStaticFolders = 3;

}
