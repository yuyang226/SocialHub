/**
 * 
 */
package com.googlecode.flickr2twitter.core;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbay;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbayKeywords;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbayKeywordsSandbox;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbaySandbox;
import com.googlecode.flickr2twitter.impl.facebook.TargetServiceProviderFacebook;
import com.googlecode.flickr2twitter.impl.flickr.SourceServiceProviderFlickr;
import com.googlecode.flickr2twitter.impl.sina.TargetServiceProviderSina;
import com.googlecode.flickr2twitter.impl.twitter.TargetServiceProviderTwitter;
import com.googlecode.flickr2twitter.intf.IServiceProvider;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class ServiceFactory {
	private static final Logger log = Logger.getLogger(ServiceFactory.class
			.getName());
	private static final Map<String, ISourceServiceProvider<IItem>> SOURCE_PROVIDERS;
	private static final Map<String, ITargetServiceProvider> TARGET_PROVIDERS;

	private static final Collection<ISourceServiceProvider<IItem>> SOURCE_PROVIDERS_List;
	private static final Collection<ITargetServiceProvider> TARGET_PROVIDERS_List;

	private static final Class<?>[] PROVIDER_CLASSES = {
		SourceServiceProviderEbay.class, SourceServiceProviderEbayKeywords.class, 
		SourceServiceProviderEbaySandbox.class, SourceServiceProviderEbayKeywordsSandbox.class,
		SourceServiceProviderFlickr.class, 
		TargetServiceProviderFacebook.class,
		TargetServiceProviderTwitter.class, TargetServiceProviderSina.class};

	static {
		Map<String, ISourceServiceProvider<IItem>> sourceData = new LinkedHashMap<String, ISourceServiceProvider<IItem>>(
				2);
		Map<String, ITargetServiceProvider> targetData = new LinkedHashMap<String, ITargetServiceProvider>(
				5);

		for (Class<?> _class : PROVIDER_CLASSES) {
			PersistenceManagerFactory pmf = MyPersistenceManagerFactory
					.getInstance();
			PersistenceManager pm = pmf.getPersistenceManager();
			try {
				IServiceProvider<?> provider = (IServiceProvider<?>) _class
						.newInstance();

				if (provider instanceof ISourceServiceProvider<?>) {
					ISourceServiceProvider<IItem> srcProvider = (ISourceServiceProvider<IItem>) provider;
					sourceData.put(provider.getId(), srcProvider);
					if (MyPersistenceManagerFactory
							.getGlobalSourceAppService(srcProvider.getId()) == null) {
						pm.makePersistent(srcProvider
								.createDefaultGlobalApplicationConfig());
					}
				} else if (provider instanceof ITargetServiceProvider) {
					ITargetServiceProvider targetProvider = (ITargetServiceProvider) provider;
					targetData.put(provider.getId(), targetProvider);
					if (MyPersistenceManagerFactory
							.getGlobalTargetAppService(targetProvider.getId()) == null) {
						pm.makePersistent(targetProvider
								.createDefaultGlobalApplicationConfig());
					}
				} else {
					log.warning("Unsupported provider ->" + _class);
				}

			} catch (Exception e) {
				log.throwing(ServiceFactory.class.getName(), "<init>", e);
			} finally {
				pm.close();
			}
		}

		SOURCE_PROVIDERS = Collections.unmodifiableMap(sourceData);
		TARGET_PROVIDERS = Collections.unmodifiableMap(targetData);

		SOURCE_PROVIDERS_List = SOURCE_PROVIDERS.values();
		TARGET_PROVIDERS_List = TARGET_PROVIDERS.values();
	}

	public static Collection<ISourceServiceProvider<IItem>> getAllSourceProviders() {
		return SOURCE_PROVIDERS_List;
	}

	public static Collection<ITargetServiceProvider> getAllTargetProviders() {
		return TARGET_PROVIDERS_List;
	}

	public ServiceFactory() {
		super();
	}

	public static ISourceServiceProvider<IItem> getSourceServiceProvider(
			String sourceServiceProviderId) {
		return SOURCE_PROVIDERS.get(sourceServiceProviderId);
	}

	public static ITargetServiceProvider getTargetServiceProvider(
			String targetServiceProviderId) {
		return TARGET_PROVIDERS.get(targetServiceProviderId);
	}

}
