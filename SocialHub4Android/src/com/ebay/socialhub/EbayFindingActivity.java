/**
 * 
 */
package com.ebay.socialhub;

import java.util.Collections;
import java.util.List;

import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebay.sdk.sample.views.WebImageView;
import com.googlecode.flickr2twitter.impl.ebay.EbayItem;
import com.googlecode.flickr2twitter.impl.ebay.FindItemsDAO;
import com.googlecode.flickr2twitter.services.rest.models.GlobalSourceApplicationServiceModel;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubServicesResource;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class EbayFindingActivity extends Activity {
	public static final String TAG = "SocialHub";
	
	private ListView itemsListView;
	private TextView txtKeywords;
	private Button btnSearch;
	private Button btnSave;

	/**
	 * 
	 */
	public EbayFindingActivity() {
		super();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ebay_finding);
		
		this.btnSearch = (Button) findViewById(R.id.cmd_search);
		this.btnSearch.setEnabled(false);
		this.btnSave = (Button) findViewById(R.id.cmd_save);
		this.btnSave.setEnabled(false);
		this.txtKeywords = (TextView) findViewById(R.id.edit_input);
		this.txtKeywords.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				boolean enable = txtKeywords.getText().toString().trim().length() > 0;
				btnSave.setEnabled(enable);
				btnSearch.setEnabled(enable);
			}
		});
		this.itemsListView = (ListView) this.findViewById(R.id.ebayItemList);
		this.itemsListView.setTextFilterEnabled(true);

		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new EbaySearchTask().execute();
			}
		});
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new EbayKeywordsSaveTask().execute(txtKeywords.getText().toString().trim());
			}
		});
	}
	
	private class EbaySearchTask extends AsyncTask<Void, Void, List<EbayItem>> {
		ProgressDialog authDialog;

		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(EbayFindingActivity.this, "Searching",
					"Searching eBay...", true, // indeterminate
					// duration
					false); // not cancel-able
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<EbayItem> doInBackground(Void... arg0) {
			try {
				String keywords = txtKeywords.getText().toString().trim();
				return new FindItemsDAO().findItemsByKeywordsFromProduction(keywords, 5);
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			
			return Collections.emptyList();
		}

		protected void onPostExecute(List<EbayItem> items) {
			try {
				authDialog.dismiss();
				
		        EbayFindingActivity.this.itemsListView.setAdapter(
		        		new EbayItemAdapter(EbayFindingActivity.this, R.layout.row, items));
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}

	}
	
	private class EbayKeywordsSaveTask extends AsyncTask<String, Void, Boolean> {
		ProgressDialog authDialog;

		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(EbayFindingActivity.this, "Saving",
					"Saving eBay keywords to the server...", true, // indeterminate
					// duration
					false); // not cancel-able
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(String... arg) {
			try {
				String keywords = arg[0];
				if (getIntent().hasExtra(AuthorizeActivity.SERVICE_CONFIG_ID) == false 
						|| getIntent().hasExtra(OAuthActivity.KEY_USER_EMAIL) == false) {
					return Boolean.FALSE;
				}
				GlobalSourceApplicationServiceModel source = (GlobalSourceApplicationServiceModel)
				getIntent().getSerializableExtra(AuthorizeActivity.SERVICE_CONFIG_ID);
				String userEmail = getIntent().getStringExtra(OAuthActivity.KEY_USER_EMAIL);
				
				ClientResource cr = new ClientResource("http://ebaysocialhub.appspot.com/rest/services");
				ISociaHubServicesResource resource = cr.wrap(ISociaHubServicesResource.class);
				//resource.addUserTargetServiceConfig(targetServiceConfig);

				StringBuffer buf = new StringBuffer();
				buf.append(source.getProviderId());
				buf.append("/");
				buf.append(userEmail);
				buf.append("/");
				buf.append(keywords);
				
				resource.addUserServiceConfig(buf.toString());
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
				return Boolean.FALSE;
			}
			
			return Boolean.TRUE;
		}

		protected void onPostExecute(Boolean result) {
			try {
				authDialog.dismiss();
				if (Boolean.TRUE.equals(result)) {
					Toast.makeText(EbayFindingActivity.this, 
							"Successfully saved ebay keywords to the server", Toast.LENGTH_LONG).show();
					Intent uIntent = new Intent(EbayFindingActivity.this,UserProfileActivity.class);
					uIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					uIntent.putExtra(OAuthActivity.KEY_USER_EMAIL, 
							getIntent().getStringExtra(OAuthActivity.KEY_USER_EMAIL));
					EbayFindingActivity.this.startActivity(uIntent);
					finish();
				} else {
					Toast.makeText(EbayFindingActivity.this, 
							"Failed saving ebay keywords to the server", Toast.LENGTH_LONG).show();
				}
		        
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}

	}
	
	private class EbayItemAdapter extends ArrayAdapter<EbayItem> {

		private List<EbayItem> items;

        public EbayItemAdapter(Context context, int textViewResourceId,
                List<EbayItem> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        /*
         * (non-Javadoc)
         * @see android.widget.BaseAdapter#getViewTypeCount()
         */
        @Override
        public int getViewTypeCount() {
            return items != null ? items.size() : 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.ebay_row, null);
            }
            EbayItem ebayItem = items.get(position);
            if (ebayItem != null) {
            	 TextView title = (TextView) v.findViewById(R.id.item_title);
                 
                 if (title != null) {
                 	title.setText(ebayItem.getTitle());                            
                 }
                 
                 WebImageView image = (WebImageView) v.findViewById(R.id.gallery_icon);
                 if (image != null){
                 	if (ebayItem.getGalleryURL() != null) {
                 		image.setImageUrl(ebayItem.getGalleryURL());
                 		image.loadImage();
                 	} else {
                 		image.setNoImageDrawable(R.drawable.placeholder);
                 	}
                 }
                 
                 // once clicked, navigate to item details page
                // v.setOnClickListener(new OnItemClickListener(item.itemId, v.getContext()));
            }
            return v;
        }
    }
	
	

}
