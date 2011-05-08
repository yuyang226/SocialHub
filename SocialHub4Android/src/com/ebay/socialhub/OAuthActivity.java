/**
 * 
 */
package com.ebay.socialhub;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.RequestParameters;

import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;
import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.flickr2twitter.services.rest.models.GlobalTargetApplicationServiceModel;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubServicesResource;
import com.googlecode.flickr2twitter.services.rest.models.UserModel;
import com.googlecode.flickr2twitter.services.rest.models.UserTargetServiceConfigModel;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class OAuthActivity extends Activity {
	public static final String TAG = "SocialHub";
	public static final String ID_GOOGLE = "Google";
	public static final String ID_YAHOO = "Yahoo";
	public static final String ID_TWITTER = "Twitter";

	public static final String ID_PROVIDER = "providerId";

	private OpenIdManager manager;

	private static final String ID_SCHEME = "socialhub-app";
	private static final Uri GOOGLE_CALLBACK_URI = Uri.parse(ID_SCHEME + "://google");
	private static final Uri YAHOO_CALLBACK_URI = Uri.parse(ID_SCHEME + "://yahoo");
	private static final Uri TWITTER_CALLBACK_URI = Uri.parse(ID_SCHEME + "://twitter");

	private static final String GAE_CALLBACK_URL = "http://ebaysocialhub.appspot.com/openid_callback.jsp";
	public static final String KEY_USER_EMAIL = "userEmail";


	//twitter variables
	public static final String USER_TOKEN = "user_token";
	public static final String USER_SECRET = "user_secret";
	public static final String CONSUMER_ID = "consumer_id";
	public static final String CONSUMER_SECRET = "consumer_secret";
	public static final String REQUEST_TOKEN = "request_token";
	public static final String REQUEST_SECRET = "request_secret";

	public static final String TWITTER_REQUEST_TOKEN_URL = "http://twitter.com/oauth/request_token";
	public static final String TWITTER_ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
	public static final String TWITTER_AUTHORIZE_URL = "http://twitter.com/oauth/authorize";

	public static final String PREFS = "MyPrefsFile";

	//	private OAuthConsumer mConsumer = null;
	//	private OAuthProvider mProvider = null;
	SharedPreferences mSettings;

	/**
	 * 
	 */
	public OAuthActivity() {
		super();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			if (getIntent().hasExtra(ID_PROVIDER)) {
				String providerId = getIntent().getExtras().getString(ID_PROVIDER);
				if (ID_GOOGLE.equalsIgnoreCase(providerId)) {
					new AuthGoogleOpenIDTask().execute();
				} else if (ID_TWITTER.equalsIgnoreCase(providerId)) {
					new AuthTwitterOpenIDTask().execute();
				}  else if (ID_YAHOO.equalsIgnoreCase(providerId)) {
					new AuthYahooOpenIDTask().execute();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		try {
			super.onResume();
			Uri uri = getIntent().getData();

			if (uri != null && ID_SCHEME.equals(uri.getScheme())) {
				if (ID_GOOGLE.equalsIgnoreCase(uri.getHost())) {
					//google open id oauth
					String query = uri.getQuery();
					if (query.startsWith(KEY_USER_EMAIL)) {
						String userEmail = query.substring(KEY_USER_EMAIL.length() + 1, query.length());
						Intent intent = new Intent(this, UserProfileActivity.class);
						//loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
						intent.putExtra(KEY_USER_EMAIL, userEmail);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						this.startActivity(intent);
						finish();
					}
				} else if (ID_TWITTER.equalsIgnoreCase(uri.getHost())) {
					/*GlobalTargetApplicationServiceModel target;
					if (getIntent().hasExtra(AuthorizeActivity.SERVICE_CONFIG_ID)) {
						target = (GlobalTargetApplicationServiceModel)getIntent().getExtras().get(AuthorizeActivity.SERVICE_CONFIG_ID);
					} else {
						return;
					}*/
					mSettings = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
					String token = mSettings.getString(REQUEST_TOKEN, null);
					String secret = mSettings.getString(REQUEST_SECRET, null);
					String consumerId = mSettings.getString(CONSUMER_ID, null);
					String consumerSecret = mSettings.getString(CONSUMER_SECRET, null);
					String userEmail = mSettings.getString(KEY_USER_EMAIL, null);
					try {
						OAuthConsumer mConsumer = new CommonsHttpOAuthConsumer(
								consumerId, 
								consumerSecret);

						OAuthProvider mProvider = new CommonsHttpOAuthProvider (
								TWITTER_REQUEST_TOKEN_URL, 
								TWITTER_ACCESS_TOKEN_URL,
								TWITTER_AUTHORIZE_URL);

						// It turns out this was the missing thing to making standard Activity launch mode work
						mProvider.setOAuth10a(true);

						mConsumer.setTokenWithSecret(token, secret);

						String otoken = uri.getQueryParameter(OAuth.OAUTH_TOKEN);
						String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

						// We send out and save the request token, but the secret is not the same as the verifier
						// Apparently, the verifier is decoded to get the secret, which is then compared - crafty
						// This is a sanity check which should never fail - hence the assertion
						Assert.assertEquals(otoken, mConsumer.getToken());

						// This is the moment of truth - we could throw here
						mProvider.retrieveAccessToken(mConsumer, verifier);
						// Now we can retrieve the goodies
						token = mConsumer.getToken();
						secret = mConsumer.getTokenSecret();
						RequestParameters params = mConsumer.getRequestParameters();
						Map<String, String> data = mProvider.getResponseParameters();

						UserTargetServiceConfigModel model = new UserTargetServiceConfigModel();
						model.setServiceProviderId(ID_TWITTER.toLowerCase(Locale.US));
						model.setServiceAccessToken(token);
						model.setServiceTokenSecret(secret);
						model.setUserEmail(userEmail);
						model.setServiceUserName(data.get("screen_name"));
						//user service id for storing the twitter verifier
						model.setServiceUserId(data.get("user_id"));
						model.setAdditionalParameters(new HashMap<String, String>(0));
						model.setUserSiteUrl("http://twitter.com");

						new SaveTwitterTokenTask().execute(model);

						//OAuthActivity.saveAuthInformation(mSettings, token, secret);
						// Clear the request stuff, now that we have the real thing
						OAuthActivity.saveRequestInformation(mSettings, null, null, null, null, null);
						//i.putExtra(USER_TOKEN, token);
						//i.putExtra(USER_SECRET, secret);
					} catch (OAuthMessageSignerException e) {
						e.printStackTrace();
					} catch (OAuthNotAuthorizedException e) {
						e.printStackTrace();
					} catch (OAuthExpectationFailedException e) {
						e.printStackTrace();
					} catch (OAuthCommunicationException e) {
						e.printStackTrace();
					} finally {
						//startActivity(i); // we either authenticated and have the extras or not, but we're going back
					   /* Intent uIntent = new Intent(OAuthActivity.this,UserProfileActivity.class);
					    uIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						OAuthActivity.this.startActivity(uIntent);
						finish();*/
					}
				} else if (ID_YAHOO.equalsIgnoreCase(uri.getHost())) {
					//yahoo open id oauth
					String query = uri.getQuery();
					if (query.startsWith(KEY_USER_EMAIL)) {
						String userEmail = query.substring(KEY_USER_EMAIL.length() + 1, query.length());
						Intent intent = new Intent(this, UserProfileActivity.class);
						//loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
						intent.putExtra(KEY_USER_EMAIL, userEmail);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						this.startActivity(intent);
						finish();
					}
				} 
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	public static void saveRequestInformation(SharedPreferences settings, String userEmail, 
			String token, String secret, 
			String consumerId, String consumerSecret) {
		// null means to clear the old values
		SharedPreferences.Editor editor = settings.edit();
		if(userEmail == null) {
			editor.remove(KEY_USER_EMAIL);
			Log.d(TAG, "Clearing user email");
		}
		else {
			editor.putString(KEY_USER_EMAIL, userEmail);
			Log.d(TAG, "Saving user email: " + userEmail);
		}

		if(token == null) {
			editor.remove(OAuthActivity.REQUEST_TOKEN);
			Log.d(TAG, "Clearing Request Token");
		}
		else {
			editor.putString(OAuthActivity.REQUEST_TOKEN, token);
			Log.d(TAG, "Saving Request Token: " + token);
		}
		if (secret == null) {
			editor.remove(OAuthActivity.REQUEST_SECRET);
			Log.d(TAG, "Clearing Request Secret");
		}
		else {
			editor.putString(OAuthActivity.REQUEST_SECRET, secret);
			Log.d(TAG, "Saving Request Secret: " + secret);
		}

		if(consumerId == null) {
			editor.remove(OAuthActivity.CONSUMER_ID);
			Log.d(TAG, "Clearing Consumer Id");
		}
		else {
			editor.putString(OAuthActivity.CONSUMER_ID, consumerId);
			Log.d(TAG, "Saving Consumer Id: " + consumerId);
		}

		if(consumerSecret == null) {
			editor.remove(OAuthActivity.CONSUMER_SECRET);
			Log.d(TAG, "Clearing Consumer Secret");
		}
		else {
			editor.putString(OAuthActivity.CONSUMER_SECRET, consumerSecret);
			Log.d(TAG, "Saving Consumer Secret: " + consumerSecret);
		}

		editor.commit();

	}

	public static void saveAuthInformation(SharedPreferences settings, String token, String secret) {
		// null means to clear the old values
		SharedPreferences.Editor editor = settings.edit();
		if(token == null) {
			editor.remove(OAuthActivity.USER_TOKEN);
			Log.d(TAG, "Clearing OAuth Token");
		}
		else {
			editor.putString(OAuthActivity.USER_TOKEN, token);
			Log.d(TAG, "Saving OAuth Token: " + token);
		}
		if (secret == null) {
			editor.remove(OAuthActivity.USER_SECRET);
			Log.d(TAG, "Clearing OAuth Secret");
		}
		else {
			editor.putString(OAuthActivity.USER_SECRET, secret);
			Log.d(TAG, "Saving OAuth Secret: " + secret);
		}
		editor.commit();

	}

	private class AuthGoogleOpenIDTask extends AsyncTask<Void, Void, String> {
		ProgressDialog authDialog;

		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(OAuthActivity.this, 
					getText(R.string.auth_progress_title), 
					getText(R.string.google_openid_oauth_message), 
					true,	// indeterminate duration
					false); // not cancel-able
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			String message = null;
			try {
				manager = new OpenIdManager();
				manager.setReturnTo(GAE_CALLBACK_URL);

				Intent i = OAuthActivity.this.getIntent();
				if (i.getData() == null) {
					Endpoint endpoint = manager.lookupEndpoint(ID_GOOGLE);
					Association association = manager.lookupAssociation(endpoint);
					String authUrl = manager.getAuthenticationUrl(endpoint, association);
					Log.i(TAG, "Google OpenID AuthURL: " + authUrl);
					OAuthActivity.this.startActivity(
							new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
				message = e.toString();
			}
			return message;
		}

		protected void onPostExecute(String result) {
			authDialog.dismiss();
			if (result != null) {
				Toast.makeText(OAuthActivity.this, 
						"Google OpenID OAuth - " + result,Toast.LENGTH_LONG).show();
			}
			finish();
		}

	}
	
	private class AuthYahooOpenIDTask extends AsyncTask<Void, Void, String> {
		ProgressDialog authDialog;

		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(OAuthActivity.this, 
					getText(R.string.auth_progress_title), 
					getText(R.string.yahoo_openid_oauth_message), 
					true,	// indeterminate duration
					false); // not cancel-able
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			String message = null;
			try {
				manager = new OpenIdManager();
				manager.setReturnTo(GAE_CALLBACK_URL);

				Intent i = OAuthActivity.this.getIntent();
				if (i.getData() == null) {
					Endpoint endpoint = manager.lookupEndpoint(ID_YAHOO);
					Association association = manager.lookupAssociation(endpoint);
					String authUrl = manager.getAuthenticationUrl(endpoint, association);
					Log.i(TAG, "Yahoo OpenID AuthURL: " + authUrl);
					OAuthActivity.this.startActivity(
							new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
				message = e.toString();
			}
			return message;
		}

		protected void onPostExecute(String result) {
			authDialog.dismiss();
			if (result != null) {
				Toast.makeText(OAuthActivity.this, 
						"Google OpenID OAuth - " + result,Toast.LENGTH_LONG).show();
			}
			finish();
		}

	}
	
	private class AuthTwitterOpenIDTask extends AsyncTask<Void, Void, String> {
		ProgressDialog authDialog;

		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(OAuthActivity.this, 
					getText(R.string.auth_progress_title), 
					"Redirecting to twitter for oauth...", 
					true,	// indeterminate duration
					false); // not cancel-able
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			GlobalTargetApplicationServiceModel target = null;
			if (getIntent().hasExtra(AuthorizeActivity.SERVICE_CONFIG_ID)) {
				target = (GlobalTargetApplicationServiceModel)getIntent().getExtras().get(AuthorizeActivity.SERVICE_CONFIG_ID);
			} else {
				finish();
			}
			// We don't need to worry about any saved states: we can reconstruct the state
			OAuthConsumer mConsumer = new CommonsHttpOAuthConsumer(
					target.getTargetAppConsumerId(), 
					target.getTargetAppConsumerSecret());

			OAuthProvider mProvider = new CommonsHttpOAuthProvider (
					TWITTER_REQUEST_TOKEN_URL, 
					TWITTER_ACCESS_TOKEN_URL,
					TWITTER_AUTHORIZE_URL);

			// It turns out this was the missing thing to making standard Activity launch mode work
			mProvider.setOAuth10a(true);

			mSettings = OAuthActivity.this.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

			Intent i = OAuthActivity.this.getIntent();
			if (i.getData() == null) {
				try {
					// This is really important. If you were able to register your real callback Uri with Twitter, and not some fake Uri
					// like I registered when I wrote this example, you need to send null as the callback Uri in this function call. Then
					// Twitter will correctly process your callback redirection
					String authUrl = mProvider.retrieveRequestToken(mConsumer, TWITTER_CALLBACK_URI.toString());
					saveRequestInformation(mSettings, getIntent().getExtras().getString(KEY_USER_EMAIL), 
							mConsumer.getToken(), mConsumer.getTokenSecret(), 
							target.getTargetAppConsumerId(), 
							target.getTargetAppConsumerSecret());
					OAuthActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				} catch (Exception e) {
					Log.e(TAG, e.toString(), e);
					return e.toString();
				}
			}
			return null;
		}

		protected void onPostExecute(String result) {
			authDialog.dismiss();
			if (result != null) {
				Toast.makeText(OAuthActivity.this, 
						"Twitter OAuth request failed-> " + result,Toast.LENGTH_LONG).show();
			}
			finish();
		}

	}

	private class SaveTwitterTokenTask extends AsyncTask<UserTargetServiceConfigModel, Void, UserModel> {
		ProgressDialog twitterAuthDialog;

		@Override
		protected void onPreExecute() {
			twitterAuthDialog = ProgressDialog.show(OAuthActivity.this, 
				"Saving", 
				"Saving twitter oauth token to the server...", 
				true,	// indeterminate duration
				false); // not cancel-able
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected UserModel doInBackground(UserTargetServiceConfigModel... params) {
			try {
				UserTargetServiceConfigModel targetServiceConfig = params[0];
				ClientResource cr = new ClientResource("http://ebaysocialhub.appspot.com/rest/services");
				ISociaHubServicesResource resource = cr.wrap(ISociaHubServicesResource.class);
				//resource.addUserTargetServiceConfig(targetServiceConfig);

				StringBuffer buf = new StringBuffer();
				buf.append(targetServiceConfig.getServiceProviderId());
				buf.append("/");
				buf.append(targetServiceConfig.getUserEmail());
				buf.append("/");
				buf.append(targetServiceConfig.getServiceAccessToken());
				buf.append("/");
				buf.append(targetServiceConfig.getServiceTokenSecret());
				buf.append("/");
				buf.append(targetServiceConfig.getServiceUserId());
				buf.append("/");
				buf.append(targetServiceConfig.getServiceUserName());
				
				resource.addUserServiceConfig(buf.toString());

				//				resource.addTwitterTargetServiceConfig(targetServiceConfig.getUserEmail(), 
				//						targetServiceConfig.getServiceAccessToken(), targetServiceConfig.getServiceTokenSecret());
				
				cr = new ClientResource(Login.SERVER_LOCATION);
				ISociaHubResource service = cr.wrap(ISociaHubResource.class);
				return service.retrieve(targetServiceConfig.getUserEmail());
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			return null;
		}

		protected void onPostExecute(UserModel user) {
			if (twitterAuthDialog != null)
				twitterAuthDialog.dismiss();
			if (user != null) {
				Toast.makeText(OAuthActivity.this, 
						"Successfully saved twitter oauth token to the server", Toast.LENGTH_LONG).show();
				Intent uIntent = new Intent(OAuthActivity.this,UserProfileActivity.class);
				uIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				uIntent.putExtra(UserProfileActivity.TAG_USER, user);
				OAuthActivity.this.startActivity(uIntent);
				finish();
			} else {
				Toast.makeText(OAuthActivity.this, 
						"Failed saving twitter oauth token to the server", Toast.LENGTH_LONG).show();
			}
		}

	}

}
