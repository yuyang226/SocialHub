package com.googlecode.flickr2twitter.com.aetrion.flickr.photos;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.googlecode.flickr2twitter.com.aetrion.flickr.tags.Tag;
import com.googlecode.flickr2twitter.com.aetrion.flickr.util.XMLUtilities;

/**
 * Utilitiy-methods to transfer requested XML to Photo-objects.
 *
 * @author till, x-mago
 * @version $Id: PhotoUtils.java,v 1.20 2009/07/23 21:49:35 x-mago Exp $
 */
public final class PhotoUtils {
	private static final long serialVersionUID = 12L;

    private PhotoUtils() {
    }

    /**
     * Try to get an attribute value from two elements.
     *
     * @param firstElement
     * @param secondElement
     * @return attribute value
     */
    private static String getAttribute(String name, Element firstElement,
            Element secondElement) {
        String val = firstElement.getAttribute(name);
        if (val.length() == 0 && secondElement != null) {
            val = secondElement.getAttribute(name);
        }
        return val;
    }

    /**
     * Transfer the Information of a photo from a DOM-object
     * to a Photo-object.
     *
     * @param photoElement
     * @return Photo
     */
    public static final Photo createPhoto(Element photoElement) {
        return createPhoto(photoElement, null);
    }

    /**
     * Transfer the Information of a photo from a DOM-object
     * to a Photo-object.
     *
     * @param photoElement
     * @param defaultElement
     * @return Photo
     */
     public static final Photo createPhoto(Element photoElement,
        Element defaultElement) {
        Photo photo = new Photo();
        photo.setId(photoElement.getAttribute("id"));
        photo.setDateTaken(photoElement.getAttribute("datetaken"));
        photo.setDatePosted(photoElement.getAttribute("dateupload"));
        photo.setLastUpdate(photoElement.getAttribute("lastupdate"));
        // flickr.groups.pools.getPhotos provides this value!
        photo.setDateAdded(photoElement.getAttribute("dateadded"));

        try {
            photo.setTitle(XMLUtilities.getChildValue(photoElement, "title"));
            if (photo.getTitle() == null) {
                photo.setTitle(photoElement.getAttribute("title"));
            }
        } catch (IndexOutOfBoundsException e) {
            photo.setTitle(photoElement.getAttribute("title"));
        }
        
        try {
            // here the flags are set, if the photo is read by getInfo().
            Element visibilityElement = (Element) photoElement.getElementsByTagName("visibility").item(0);
            photo.setPublicFlag("1".equals(visibilityElement.getAttribute("ispublic")));
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
            // these flags are set here, if photos read from a list.
            photo.setPublicFlag("1".equals(photoElement.getAttribute("ispublic")));
        }

        try {
            photo.setDescription(XMLUtilities.getChildValue(photoElement, "description"));
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            Element ownerElement = (Element) photoElement.getElementsByTagName("owner").item(0);
            if (ownerElement == null) {
                photo.setUrl("http://flickr.com/photos/" + getAttribute("owner", photoElement, defaultElement) + "/" + photo.getId());
            } else {
                String ownerId = ownerElement.getAttribute("nsid");
                photo.setUrl("http://flickr.com/photos/" + ownerId + "/" + photo.getId());
            }
        } catch (IndexOutOfBoundsException e) {
            photo.setUrl("http://flickr.com/photos/" + photoElement.getAttribute("owner") + "/" + photo.getId());
        }

        // Parse either photo by getInfo, or from list
        try {
            Element datesElement = XMLUtilities.getChild(photoElement, "dates");
            photo.setDatePosted(datesElement.getAttribute("posted"));
            photo.setDateTaken(datesElement.getAttribute("taken"));
            photo.setLastUpdate(datesElement.getAttribute("lastupdate"));
        } catch (IndexOutOfBoundsException e) {
            photo.setDateTaken(photoElement.getAttribute("datetaken"));
        } catch (NullPointerException e) {
            photo.setDateTaken(photoElement.getAttribute("datetaken"));
        }

        NodeList permissionsNodes = photoElement.getElementsByTagName("permissions");
        if (permissionsNodes.getLength() > 0) {
            Element permissionsElement = (Element) permissionsNodes.item(0);
            Permissions permissions = new Permissions();
            permissions.setComment(permissionsElement.getAttribute("permcomment"));
            permissions.setAddmeta(permissionsElement.getAttribute("permaddmeta"));
        }

        String longitude = null;
        String latitude = null;
        String accuracy = null;
        try {
            Element geoElement = (Element) photoElement.getElementsByTagName("location").item(0);
            longitude = geoElement.getAttribute("longitude");
            latitude = geoElement.getAttribute("latitude");
            accuracy = geoElement.getAttribute("accuracy");
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        	// Geodata may be available as attributes in the photo-tag itself!
            try {
                longitude = photoElement.getAttribute("longitude");
                latitude = photoElement.getAttribute("latitude");
                accuracy = photoElement.getAttribute("accuracy");
            } catch (NullPointerException e2) {
                // no geodata at all
            }
        }
        if (longitude != null && latitude != null) {
            if (longitude.length() > 0 && latitude.length() > 0
                && !("0".equals(longitude) && "0".equals(latitude))) {
                photo.setGeoData(new GeoData(longitude, latitude, accuracy));
            }
        }
        
        // Tags coming as space-seperated attribute calling
        // InterestingnessInterface#getList().
        // Through PhotoInterface#getInfo() the Photo has a list of
        // Elements.
        try {
            List<Tag> tags = new ArrayList<Tag>();
            String tagsAttr = photoElement.getAttribute("tags");
            if (!tagsAttr.equals("")) {
                String[] values = tagsAttr.split("\\s+");
                for (int i = 0; i < values.length; i++) {
                    Tag tag = new Tag();
                    tag.setValue(values[i]);
                    tags.add(tag);
                }
            } else {
                 try {
                    Element tagsElement = (Element) photoElement.getElementsByTagName("tags").item(0);
                    NodeList tagNodes = tagsElement.getElementsByTagName("tag");
                    for (int i = 0; i < tagNodes.getLength(); i++) {
                        Element tagElement = (Element) tagNodes.item(i);
                        Tag tag = new Tag();
                        tag.setId(tagElement.getAttribute("id"));
                        tag.setAuthor(tagElement.getAttribute("author"));
                        tag.setRaw(tagElement.getAttribute("raw"));
                        tag.setValue(((Text) tagElement.getFirstChild()).getData());
                        tags.add(tag);
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            }
            photo.setTags(tags);
        } catch (NullPointerException e) {
            photo.setTags(new ArrayList<Tag>());
        }

        return photo;
    }

    /**
     * Parse a list of Photos from given Element.
     *
     * @param photosElement
     * @return PhotoList
     */
    public static final PhotoList createPhotoList(Element photosElement) {
        PhotoList photos = new PhotoList();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));

        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

}
