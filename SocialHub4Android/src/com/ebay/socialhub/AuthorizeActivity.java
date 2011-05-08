/**
 * 
 */

package com.ebay.socialhub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModel;
import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModelList;
import com.googlecode.flickr2twitter.services.rest.models.GlobalSourceApplicationServiceModel;
import com.googlecode.flickr2twitter.services.rest.models.GlobalTargetApplicationServiceModel;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource;

/**
 * @author yayu
 */
public class AuthorizeActivity extends Activity {
    public static final String TAG = "SocialHub";

    public static final Map<String, Integer> ICON_MAP;
    public static final Collection<String> SUPPORTED_SERVICES;

    private SectionedAdapter servicesAdapter;

    public static final String SERVICES_ID = "serviceProviders";

    public static final String SERVICE_CONFIG_ID = "serviceConfig";

    private ListView authorizeServiceListView;

    static {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("flickr", R.drawable.flickr_64);
        map.put("youtube", R.drawable.youtube_64);
        map.put("facebook", R.drawable.facebook_64);
        map.put("twitter", R.drawable.twitter_64);
        map.put("picasa", R.drawable.picasa_64);
        map.put("ebay", R.drawable.ebay_64);
        map.put("ebay_keywords", R.drawable.ebay_64);
        map.put("ebay_sandbox", R.drawable.ebay_64);
        map.put("ebay_keywords_sandbox", R.drawable.ebay_64);
        map.put("sina", R.drawable.sina_64);
        map.put("email", R.drawable.gmail_64);
        ICON_MAP = Collections.unmodifiableMap(map);
        
        Collection<String> data = new HashSet<String>();
        data.add("ebay_keywords");
        data.add("twitter");
        SUPPORTED_SERVICES = Collections.unmodifiableCollection(data);
    }

    /**
	 * 
	 */
    public AuthorizeActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.authorize);

            final OnLongClickListener longClickListener = new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (v instanceof TextView) {
                        TextView txtV = (TextView) v;
                        Toast.makeText(AuthorizeActivity.this, txtV.getText(), Toast.LENGTH_SHORT)
                                .show();
                        return true;
                    }
                    return false;
                }
            };
            this.servicesAdapter = new SectionedAdapter() {

                @Override
                protected View getHeaderView(String caption, int index, View convertView,
                        ViewGroup parent) {
                    TextView result = (TextView) convertView;

                    if (convertView == null) {
                        result = (TextView) getLayoutInflater().inflate(R.layout.header, null);
                    }

                    result.setText(caption);
                    result.setOnLongClickListener(longClickListener);
                    return (result);
                }
            };

            this.authorizeServiceListView = (ListView) this.findViewById(R.id.authorizeServiceList);
            this.authorizeServiceListView.setTextFilterEnabled(true);

            if (getIntent().hasExtra(SERVICES_ID)) {
                setListData((GlobalApplicationConfigModelList) getIntent().getExtras()
                        .getSerializable(SERVICES_ID));
            } else {
                new GetAvailableServicesTask().execute();
            }

            final String userEmail = getIntent().getExtras()
                    .getString(OAuthActivity.KEY_USER_EMAIL);

            this.authorizeServiceListView
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
                            Object obj = servicesAdapter.getItem(position);
                            if (obj instanceof GlobalTargetApplicationServiceModel) {
                                GlobalTargetApplicationServiceModel target = (GlobalTargetApplicationServiceModel) obj;
                                if ("twitter".equalsIgnoreCase(target.getProviderId())) {
                                    // we only support twitter for now
                                    Intent intent = new Intent(AuthorizeActivity.this,
                                            OAuthActivity.class);
                                    intent.putExtra(SERVICE_CONFIG_ID, target);
                                    intent.putExtra(OAuthActivity.ID_PROVIDER,
                                            OAuthActivity.ID_TWITTER);
                                    intent.putExtra(OAuthActivity.KEY_USER_EMAIL, userEmail);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    AuthorizeActivity.this.startActivity(intent);
                                }
                            } else {
                            	GlobalSourceApplicationServiceModel source = (GlobalSourceApplicationServiceModel) obj;
                            	 if ("ebay".equalsIgnoreCase(source.getProviderId()) 
                                 		|| "ebay_keywords".equalsIgnoreCase(source.getProviderId())) {
                                 	Intent intent = new Intent(AuthorizeActivity.this,
                                 			EbayFindingActivity.class);
                                 	intent.putExtra(SERVICE_CONFIG_ID, source);
                                 	intent.putExtra(OAuthActivity.KEY_USER_EMAIL, userEmail);
                                 	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                 	AuthorizeActivity.this.startActivity(intent);
                                 } else {
                                	 Toast.makeText(AuthorizeActivity.this, String.valueOf(obj),
                                             Toast.LENGTH_SHORT).show();
                                 }
                            }
                        }

                    });

        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }
    }

    private class ItemAdapter extends ArrayAdapter<GlobalApplicationConfigModel> {
        private List<GlobalApplicationConfigModel> items;

        public ItemAdapter(Context context, int textViewResourceId,
                List<GlobalApplicationConfigModel> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.authorize_row, null);
            }
            GlobalApplicationConfigModel serviceModel = items.get(position);
            if (serviceModel != null) {
                ImageView image = (ImageView) v.findViewById(R.id.authorizeServiceIcon);
                String providerId = serviceModel.getProviderId();

                if (providerId != null && ICON_MAP.containsKey(providerId.toLowerCase(Locale.US))) {
                    image.setImageResource(ICON_MAP.get(providerId.toLowerCase(Locale.US)));
                }

                TextView tt = (TextView) v.findViewById(R.id.txtAuthAppName);
                TextView bt = (TextView) v.findViewById(R.id.txtAuthDescription);
                if (tt != null) {
                    tt.setText(serviceModel.getAppName());
                }
                if (bt != null) {
                    bt.setText(serviceModel.getDescription());
                }
            }
            return v;
        }
    }

    private void setListData(GlobalApplicationConfigModelList models) {
        if (models != null && models.getGlobalAppConfigModels() != null) {
            List<GlobalApplicationConfigModel> sources = new ArrayList<GlobalApplicationConfigModel>();
            List<GlobalApplicationConfigModel> targets = new ArrayList<GlobalApplicationConfigModel>();

            for (GlobalApplicationConfigModel model : models.getGlobalAppConfigModels()) {
            	if (SUPPORTED_SERVICES.contains(model.getProviderId()) == false) {
            		//only show those that could be supported by this mobile app
            		continue;
            	}
                if (model instanceof GlobalSourceApplicationServiceModel) {
                    sources.add(model);
                } else {
                    targets.add(model);
                }
            }
            AuthorizeActivity.this.servicesAdapter.addSection("Source Services", new ItemAdapter(
                    AuthorizeActivity.this, R.layout.row, sources));

            AuthorizeActivity.this.servicesAdapter.addSection("Target Services", new ItemAdapter(
                    AuthorizeActivity.this, R.layout.row, targets));
            authorizeServiceListView.setAdapter(AuthorizeActivity.this.servicesAdapter);
            AuthorizeActivity.this.getIntent().putExtra(SERVICES_ID, models);
        }
    }

    private class GetAvailableServicesTask extends
            AsyncTask<Void, Void, GlobalApplicationConfigModelList> {
        ProgressDialog authDialog;

        @Override
        protected void onPreExecute() {
            authDialog = ProgressDialog.show(AuthorizeActivity.this, "Contacting Server",
                    "Retrieving supported services from server...", true, // indeterminate
                                                                          // duration
                    false); // not cancel-able
        }

        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected GlobalApplicationConfigModelList doInBackground(Void... arg0) {
            try {
                ClientResource cr = new ClientResource(Login.SERVER_LOCATION);
                ISociaHubResource resource = cr.wrap(ISociaHubResource.class);
                return resource.getSupportedServiceProviders();
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }
            return null;
        }

        protected void onPostExecute(GlobalApplicationConfigModelList models) {
            try {
                authDialog.dismiss();
                setListData(models);
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }
        }

    }

}
