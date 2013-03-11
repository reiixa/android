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
package no.digipost.android.gui;

//import android.R;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import no.digipost.android.R;
import no.digipost.android.model.Receipt;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ReceiptListAdapter extends ArrayAdapter<Receipt> {
	private final Context con;
	private final ArrayList<Receipt> receipts;
	public static boolean showboxes = false;
	public boolean[] checked;
	CheckBox checkbox;

	public ReceiptListAdapter(final Context context, final int textViewResourceId, final ArrayList<Receipt> objects) {
		super(context, textViewResourceId, objects);
		con = context;
		receipts = objects;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.mailbox_list_item, parent, false);

		Drawable even = con.getResources().getDrawable(R.drawable.list_selector_even);
		Drawable odd = con.getResources().getDrawable(R.drawable.list_selector_odd);

		row.setBackgroundDrawable((position % 2 == 0) ? even : odd);

		TextView subject = (TextView) row.findViewById(R.id.mail_subject);
		subject.setText(receipts.get(position).getStoreName());
		TextView date = (TextView) row.findViewById(R.id.mail_date);
		date.setText(getDateFormatted(receipts.get(position).getTimeOfPurchase()));
		TextView price = (TextView) row.findViewById(R.id.mail_size_price);
		price.setTextColor(con.getResources().getColor(R.color.green_price));
		String currency = receipts.get(position).getCurrency();
		if (currency.equals("NOK")) {
			currency = "kr.";
		}
		String amount = receipts.get(position).getAmount();
		Double number = Double.valueOf(amount);
		DecimalFormat dec = new DecimalFormat("#.00");
		amount = dec.format(number);

		price.setText(amount + " " + currency);

		checkbox = (CheckBox) row.findViewById(R.id.mailbox_checkbox);

		if (checked != null) {

			if (checked[position]) {
				checkbox.setChecked(true);
			}

			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(final CompoundButton arg0, final boolean state) {
					// TODO Auto-generated method stub
					checked[position] = state;
				}
			});

			if (showboxes) {
				checkbox.setVisibility(View.VISIBLE);
			}
		}
		return row;
	}

	@Override
	public Receipt getItem(final int position) {
		return receipts.get(position);
	}

	public void updateList(final ArrayList<Receipt> list) {
		receipts.addAll(list);
		notifyDataSetChanged();
	}

	public void setInitialcheck(final int position) {
		checked = new boolean[receipts.size()];
		checked[position] = true;
	}

	public void clearCheckboxes() {
		checked = null;
	}

	public boolean[] getCheckedDocuments() {
		return checked;
	}

	private String getDateFormatted(final String date) {
		String date_substring = date.substring(0, 10);
		SimpleDateFormat fromApi = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat guiFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
		String formatted = null;
		try {
			formatted = guiFormat.format(fromApi.parse(date_substring));
		} catch (ParseException e) {
			// Ignore
		}
		return formatted;
	}
}
