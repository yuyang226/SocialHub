/*
 * Created on Mar 15, 2011
 */

package com.googlecode.flickr2twitter.services.rest.models;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * @author Emac Shen (shen.bin.1983@gmail.com)
 */
public interface ISocialHubEbaySellerResource
{

    /**
     * Registers an eBay seller source.
     * 
     * @param data format: {userEmail}/{sellerId}
     */
    @Post
    public void register(String data);

    /**
     * Registers the given eBay seller source.
     * 
     * @param data format: {userEmail}/{sellerId}
     */
    @Delete
    public void unregister(String data);

    /**
     * Finds the given eBay seller source.
     */
    @Get
    public boolean find();

}
