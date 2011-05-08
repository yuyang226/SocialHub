/*
 * Created on Mar 15, 2011
 */

package com.googlecode.flickr2twitter.services.rest;

import java.util.Arrays;
import java.util.logging.Logger;

import org.restlet.data.Form;
import org.restlet.resource.ServerResource;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;
import com.googlecode.flickr2twitter.services.rest.models.ISocialHubEbaySellerResource;
import com.googlecode.flickr2twitter.servlet.EbayConfigServlet;

/**
 * @author Emac Shen (shen.bin.1983@gmail.com)
 */
public class SocialHubEbaySellerResource extends ServerResource
        implements ISocialHubEbaySellerResource
{

    private static final Logger log = Logger.getLogger(SocialHubEbaySellerResource.class.getName());

    /**
     * 
     */
    public SocialHubEbaySellerResource()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.flickr2twitter.services.rest.models.ISocialHubEbaySellerResource#register(java.lang.String)
     */
    @Override
    public void register(String data)
    {
        log.info("registering new eBay seller source->" + data);

        if ( data != null )
        {
            try
            {
                String[] values = StringUtils.split(data, "/");
                log.info("Received data->" + Arrays.asList(values));
                if ( values.length == 3 )
                {
                    String userEmail = values[0];
                    String sellerId = values[1];
                    
                    // it seems GAE doesn't support DELETE method, have to use POST to work around
                    boolean following = "follow".equals(values[2]);
                    if ( !following )
                    {
                        unregister(data);
                        return;
                    }

                    new EbayConfigServlet().registerNewSellerSourceServiceConfig(userEmail, sellerId, false);

                    log.info("new eBay seller source registered->" + data);
                }
                else
                {
                    log.info("Unsupported source config->" + data);
                }
            }
            catch (Exception e)
            {
                log.throwing(SocialHubEbaySellerResource.class.getName(), "register", e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.flickr2twitter.services.rest.models.ISocialHubEbaySellerResource#unregister(java.lang.String)
     */
    @Override
    public void unregister(String data)
    {
        log.info("unregistering existing eBay seller source->" + data);

        if ( data != null )
        {
            try
            {
                String[] values = StringUtils.split(data, "/");
                log.info("Received data->" + Arrays.asList(values));
                if ( values.length == 3 )
                {
                    String userEmail = values[0];
                    String sellerId = values[1];

                    User user = MyPersistenceManagerFactory.getUser(userEmail);
                    MyPersistenceManagerFactory.deleteUserService(user, sellerId, 0);

                    log.info("eBay seller source unregistered->" + data);
                }
                else
                {
                    log.info("Unsupported source config->" + data);
                }
            }
            catch (Exception e)
            {
                log.throwing(SocialHubEbaySellerResource.class.getName(), "unregister", e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.flickr2twitter.services.rest.models.ISocialHubEbaySellerResource#find()
     */
    @Override
    public boolean find()
    {
        Form query = getQuery();
        log.info("finding eBay seller source->" + query);

        if ( query != null )
        {
            try
            {
                log.info("Received data->" + query);
                if ( query.size() == 2 )
                {
                    String userEmail = query.getFirstValue("useremail");
                    String sellerId = query.getFirstValue("sellerid");

                    boolean found = false;
                    for (UserSourceServiceConfig config : MyPersistenceManagerFactory.getUserSourceServices(userEmail))
                    {
                        if ( sellerId.equals(config.getServiceAccessToken()) )
                        {
                            found = true;
                            break;
                        }
                    }

                    log.info("eBay seller source found->" + query);

                    return found;
                }
                else
                {
                    log.info("Unsupported source config->" + query);
                }
            }
            catch (Exception e)
            {
                log.throwing(SocialHubEbaySellerResource.class.getName(), "find", e);
            }
        }

        return false;
    }

}
