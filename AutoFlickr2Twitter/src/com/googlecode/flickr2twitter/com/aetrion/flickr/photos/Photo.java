/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickr2twitter.com.aetrion.flickr.photos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import com.googlecode.flickr2twitter.com.aetrion.flickr.people.User;
import com.googlecode.flickr2twitter.com.aetrion.flickr.tags.Tag;
import com.googlecode.flickr2twitter.com.aetrion.flickr.util.StringUtilities;
import com.googlecode.flickr2twitter.impl.flickr.FlickrBaseEncoder;
import com.googlecode.flickr2twitter.model.IGeoItem;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.model.IShortUrl;
import com.googlecode.flickr2twitter.model.Item;
import com.googlecode.flickr2twitter.model.ItemGeoData;

/**
 * Class representing metadata about a Flickr photo. Instances do not actually
 * contain the photo data, however you can obtain the photo data by calling
 * {@link PhotosInterface#getImage(Photo, int)} or
 * {@link PhotosInterface#getImageAsStream(Photo, int)}.
 *
 * @author Anthony Eden
 * @version $Id: Photo.java,v 1.28 2009/07/23 21:49:35 x-mago Exp $
 */
public class Photo extends Item implements IPhoto, IGeoItem, IShortUrl{
    private static final long serialVersionUID = 12L;

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATS = new ThreadLocal<SimpleDateFormat>() {
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private boolean publicFlag;
    private Date dateAdded;
    private Date datePosted;
    private Date dateTaken;
    private Date lastUpdate;
    private String url;
    private ItemGeoData geoData;
    private Collection<Tag> tags;

    public Photo() {
    }
    
    public boolean isPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(boolean publicFlag) {
        this.publicFlag = publicFlag;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        setDateAdded(new Date(dateAdded));
    }

    public void setDateAdded(String dateAdded) {
        if (dateAdded == null || "".equals(dateAdded)) return;
        setDateAdded(Long.parseLong(dateAdded) * (long) 1000);
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public void setDatePosted(long datePosted) {
        setDatePosted(new Date(datePosted));
    }

    public void setDatePosted(String datePosted) {
        if (datePosted == null || "".equals(datePosted)) return;
        setDatePosted(Long.parseLong(datePosted) * (long) 1000);
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        if (dateTaken == null || "".equals(dateTaken)) return;
        try {
            setDateTaken(DATE_FORMATS.get().parse(dateTaken));
        } catch (ParseException e) {
            // TODO: figure out what to do with this error
            e.printStackTrace();
        }
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdate(String lastUpdateStr) {
        if (lastUpdateStr == null || "".equals(lastUpdateStr)) return;
        long unixTime = Long.parseLong(lastUpdateStr);
        setLastUpdate(new Date(unixTime * 1000L));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ItemGeoData getGeoData() {
        return geoData;
    }

    public void setGeoData(ItemGeoData geoData) {
        this.geoData = geoData;
    }

    public boolean hasGeoData() {
        return geoData != null;
    }

    /**
	 * @return the tags
	 */
	public Collection<Tag> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Collection<Tag> tags) {
		this.tags = tags;
	}

	/**
     * @see java.lang.Object#equals(java.lang.Object)
     * @see <a href="http://www.ibm.com/developerworks/library/j-dyn0603/">http://www.ibm.com/developerworks/library/j-dyn0603/</a>
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        // object must be Photo at this point
        Photo test = (Photo) obj;
        Class<?> cl = this.getClass();
        Method[] method = cl.getMethods();
        for (int i = 0; i < method.length; i++) {
            Matcher m = StringUtilities.getterPattern.matcher(method[i].getName());
            if (m.find() && !method[i].getName().equals("getClass")) {
                try {
                    Object res = method[i].invoke(this, (Object[])null);
                    Object resTest = method[i].invoke(test, (Object[])null);
                    String retType = method[i].getReturnType().toString();
                    if (retType.indexOf("class") == 0) {
                        if (res != null && resTest != null) {
                            //System.out.println("class: " + method[i].getName());
                            if (!res.equals(resTest)) return false;
                        } else {
                            if (res == null && resTest == null) {
                                // nop
                            } else if (res == null || resTest == null) {
                                // one is set and one is null
                                return false;
                            }
                        }
                    } else if (retType.equals("int")) {
                        if (!((Integer) res).equals(((Integer)resTest))) return false;
                    } else if (retType.equals("boolean")) {
                        if (!((Boolean) res).equals(((Boolean)resTest))) return false;
                    } else if (retType.equals("interface java.util.Collection")) {
                        if (res != null && resTest != null) {
                            List<?> col = (List<?>) res;
                            List<?> colTest = (List<?>) resTest;
                            if (col.size() != colTest.size()) return false;
                            for (int j = 0; j < col.size(); j++) {
                                Object tobj1 = col.get(j);
                                Object tobj2 = colTest.get(j);
                                if (tobj1 instanceof PhotoUrl) {
                                    PhotoUrl url1 = (PhotoUrl) tobj1;
                                    PhotoUrl url2 = (PhotoUrl) tobj2;
                                    if (!url1.equals(url2)) return false;
                                } else {
                                    System.out.println("Photo unhandled object: " + tobj1.getClass().getName());
                               }
                            }
                        } else {
                            if (res == null && resTest != null) return false;
                            if (res != null && resTest == null) return false;
                        }
                    } else {
                        System.out.println("Photo#equals() missing type " + method[i].getName() + "|" +
                            method[i].getReturnType().toString());
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (InvocationTargetException ex) {
                    //System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (Exception ex) {
                    System.out.println("equals " + method[i].getName() + " " + ex);
                }
            }
        }
        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 1;
        Class<?> cl = this.getClass();
        Method[] method = cl.getMethods();
        for (int i = 0; i < method.length; i++) {
            Matcher m = StringUtilities.getterPattern.matcher(method[i].getName());
            if (m.find() && !method[i].getName().equals("getClass")) {
                Object res = null;
                try {
                    res = method[i].invoke(this, (Object[])null);
                } catch (IllegalAccessException ex) {
                    System.out.println("Photo hashCode " + method[i].getName() + " " + ex);
                } catch (InvocationTargetException ex) {
                    //System.out.println("hashCode " + method[i].getName() + " " + ex);
                }
                if (res != null) {
                    if (res instanceof Boolean) {
                        Boolean bool = (Boolean) res;
                        hash += bool.hashCode();
                    } else if (res instanceof Integer) {
                        Integer inte = (Integer) res;
                        hash += inte.hashCode();
                    } else if (res instanceof String) {
                        String str = (String) res;
                        hash += str.hashCode();
                    } else if (res instanceof GeoData) {
                        GeoData edit = (GeoData) res;
                        hash += edit.hashCode();
                    } else if (res instanceof Permissions) {
                        Permissions perm = (Permissions) res;
                        hash += perm.hashCode();
                    } else if (res instanceof User) {
                        User user = (User) res;
                        hash += user.hashCode();
                    } else if (res instanceof ArrayList<?>) {
                        ArrayList<?> list = (ArrayList<?>) res;
                        hash += list.hashCode();
                    } else {
                        System.out.println("Photo hashCode unrecognised object: " + res.getClass().getName());
                    }
                }
            }
        }
        return hash;
    }

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IShortUrl#getShortUrl()
	 */
	@Override
	public String getShortUrl() {
		return FlickrBaseEncoder.getShortUrl(this);
	}

}
