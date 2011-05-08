/**
 * 
 */
package com.ebay.socialhub;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModel;
import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModelList;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource;
import com.googlecode.flickr2twitter.services.rest.models.UserModel;
import com.googlecode.flickr2twitter.services.rest.models.UserServiceConfigModel;

/**
 * @author yayu
 * 
 */
public class UserProfileActivity extends Activity {
	public static final String TAG = "SocialHub";
	// private static final Map<String, Integer> ICON_MAP;
	public static final String TAG_USER = "user";

	private static final String HEADER_SOURCE = "Authroized Source Services";
	private static final String HEADER_TARGET = "Authroized Target Services";

	private SectionedAdapter sourceAdapter;

	private TextView txtUserName;
	private TextView txtUserEmail;
	private ListView sourceServiceListView;

	private UserModel user = null;
	private boolean selfInit = false;
	private ImageButton btnRefresh;
	private ImageButton btnAddMore;

	@Override
	protected void onResume() {
		super.onResume();
		if (user == null && !selfInit) {
			Intent intent = new Intent(this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		try {
			super.onCreate(savedInstanceState);
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				if (extras.containsKey(TAG_USER)) {
					Object obj = extras.getSerializable(TAG_USER);
					if (obj instanceof UserModel) {
						user = (UserModel) obj;
					}
				} else if (extras.containsKey(OAuthActivity.KEY_USER_EMAIL)) {
					selfInit = true;
					new GetUserProfileTask().execute(extras
							.getString(OAuthActivity.KEY_USER_EMAIL));
				}
			}

			setContentView(R.layout.main);

			this.txtUserName = (TextView) this
					.findViewById(R.id.userScreenName);
			this.txtUserEmail = (TextView) this.findViewById(R.id.userEmail);
			this.btnRefresh = (ImageButton) findViewById(R.id.btnRefreshUserProfile);
			btnRefresh.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					refresh();
				}
			});

			final OnClickListener clickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					addMore();
				}
			};

			this.btnAddMore = (ImageButton) findViewById(R.id.btnAddMore);
			btnAddMore.setOnClickListener(clickListener);

			this.sourceServiceListView = (ListView) this
					.findViewById(R.id.sourceServiceList);
			populateSourceTargetData();

		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	protected void populateSourceTargetData() {
		if (user == null) {
			return;
		}

		txtUserName.setText(user.getScreenName());
		txtUserEmail.setText(user.getUserId());
		
		if( sourceAdapter != null ) {
			sourceAdapter.refresh();
			return;
		}
		
		this.sourceAdapter = new SectionedAdapter() {

			@Override
			protected View getHeaderView(String caption, int index,
					View convertView, ViewGroup parent) {
				TextView result = (TextView) convertView;

				if (convertView == null) {
					result = (TextView) getLayoutInflater().inflate(
							R.layout.header, null);
				}

				result.setText(caption);
				return (result);
			}
		};

		sourceAdapter.addSection(
				HEADER_SOURCE,
				new ItemAdapter(this, R.layout.row,
						new ArrayList<UserServiceConfigModel>(user
								.getSourceServices())));

		sourceAdapter.addSection(
				HEADER_TARGET,
				new ItemAdapter(this, R.layout.row,
						new ArrayList<UserServiceConfigModel>(user
								.getTargetServices())));
		sourceServiceListView.setAdapter(this.sourceAdapter);
		this.sourceServiceListView.setTextFilterEnabled(true);

		this.sourceServiceListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Object obj = sourceAdapter.getItem(position);
						Toast.makeText(UserProfileActivity.this,
								String.valueOf(obj), Toast.LENGTH_SHORT).show();
					}

				});
	}

	private void refresh() {
		String userEmail = String.valueOf(txtUserEmail.getText());
		if (userEmail == null || userEmail.trim().length() == 0) {
			return;
		}
		new GetUserProfileTask().execute(userEmail);
	}

	private void addMore() {
		Intent i = new Intent(this, AuthorizeActivity.class);
		if (UserProfileActivity.this.getIntent().hasExtra(
				AuthorizeActivity.SERVICES_ID)) {
			i.putExtra(AuthorizeActivity.SERVICES_ID,
					UserProfileActivity.this.getIntent().getExtras()
							.getSerializable(AuthorizeActivity.SERVICES_ID));
		}
		if (this.user != null) {
			i.putExtra(OAuthActivity.KEY_USER_EMAIL, this.user.getUserId());
		}
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.user_profile_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.itemRefresh:
			refresh();
			return true;
		case R.id.itemHelp:
			Toast.makeText(UserProfileActivity.this,
					"Android rocks, iOS sucks!", Toast.LENGTH_LONG).show();
			return true;
		case R.id.itemAddMore:
			addMore();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private class ItemAdapter extends ArrayAdapter<UserServiceConfigModel> {

		private List<UserServiceConfigModel> items;

		public ItemAdapter(Context context, int textViewResourceId,
				List<UserServiceConfigModel> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			UserServiceConfigModel serviceModel = items.get(position);
			if (serviceModel != null) {
				ImageView image = (ImageView) v.findViewById(R.id.serviceIcon);
				String providerId = serviceModel.getServiceProviderId();

				if (providerId != null
						&& AuthorizeActivity.ICON_MAP.containsKey(providerId
								.toLowerCase(Locale.US))) {
					image.setImageResource(AuthorizeActivity.ICON_MAP
							.get(providerId.toLowerCase(Locale.US)));
				}

				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);

				String providerName = serviceModel.getServiceProviderId();
				if (UserProfileActivity.this.getIntent().hasExtra(
						AuthorizeActivity.SERVICES_ID)) {
					GlobalApplicationConfigModelList models = (GlobalApplicationConfigModelList) UserProfileActivity.this
							.getIntent().getExtras()
							.getSerializable(AuthorizeActivity.SERVICES_ID);
					for (GlobalApplicationConfigModel model : models
							.getGlobalAppConfigModels()) {
						if (providerName
								.equalsIgnoreCase(model.getProviderId())) {
							providerName = model.getAppName();
						}
					}
				}

				if (tt != null) {
					tt.setText(providerName);
				}
				if (bt != null) {
					bt.setText(serviceModel.getServiceUserName());
				}
			}
			return v;
		}
	}

	private class GetUserProfileTask extends AsyncTask<String, Void, Data> {
		ProgressDialog authDialog;

		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(UserProfileActivity.this,
					getText(R.string.auth_progress_title),
					getText(R.string.auth_progress_text), true, // indeterminate
																// duration
					false); // not cancel-able
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Data doInBackground(String... params) {
			Data data = null;
			try {
				ClientResource cr = new ClientResource(Login.SERVER_LOCATION);
				ISociaHubResource resource = cr.wrap(ISociaHubResource.class);
				String userEmail = null;
				if (params != null && params.length == 1) {
					userEmail = params[0];
				}

				// Get the remote contact
				if (userEmail != null) {
					UserModel user = resource.openidLogin(userEmail);

					data = new Data(user,
							resource.getSupportedServiceProviders());
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			return data;
		}

		protected void onPostExecute(Data data) {
			try {
				authDialog.dismiss();
				if (data != null) {
					user = data.user;
					populateSourceTargetData();
				} else {
					Toast.makeText(UserProfileActivity.this,
							"Error to get user profile.", Toast.LENGTH_LONG)
							.show();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}

	}

	private class Data {
		private UserModel user;
		private GlobalApplicationConfigModelList serviceProviders;

		/**
		 * @param user
		 * @param serviceProviders
		 */
		public Data(UserModel user,
				GlobalApplicationConfigModelList serviceProviders) {
			super();
			this.user = user;
			this.serviceProviders = serviceProviders;
		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
