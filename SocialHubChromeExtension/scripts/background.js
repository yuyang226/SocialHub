/**
 * @author: Emac Shen (shen.bin.1983@gmail.com)
 */
var SITE_URL = "http://ebaysocialhub.appspot.com/rest/eBaySeller";
var METHOD_QUERY = "QUERY";
var METHOD_FOLLOW = "FOLLOW";
var METHOD_UNFOLLOW = "UNFOLLOW";

var oauth = ChromeExOAuth.initBackgroundPage({
    'request_url': 'https://www.google.com/accounts/OAuthGetRequestToken',
    'authorize_url': 'https://www.google.com/accounts/OAuthAuthorizeToken',
    'access_url': 'https://www.google.com/accounts/OAuthGetAccessToken',
    'consumer_key': 'anonymous',
    'consumer_secret': 'anonymous',
    'scope': 'http://www.google.com/m8/feeds/',
    'app_name': 'SocialHub - eBay (Chrome Extension)'
});

var userEmail = "";

handleRequest.method = "";
handleRequest.sellerId = "";
handleRequest.sendResponseCallback = null;

doSubmit.xhr = null;

chrome.extension.onRequest.addListener(function(request, sender, sendResponse){
    console.log(sender.tab ? "from a content script: " + sender.tab.url : "from the extension");
    
    if (request.SELLER_ID) {
        handleRequest(request.METHOD, request.SELLER_ID, sendResponse);
    }
    else {
        sendResponse({
            STATUS: "ERROR",
            MESSAGE: "Error: Empty seller ID."
        });
    }
});

chrome.cookies.get({
    url: "http://ebaysocialhub.appspot.com/chromeextension/",
    name: "userEmail"
}, function(cookie){
    if (cookie) {
        userEmail = cookie.value;
        console.log("Found user email address in cookies: " + userEmail);
    }
});

function greet(){
    console.log("[Func] greet");
    
    chrome.tabs.create({
        'url': 'http://ebaysocialhub.appspot.com/'
    });
}

function handleRequest(method, sellerId, sendResponse){
    console.log("[Func] handleRequest");
    
    // cache sellerId and sendResponse
    handleRequest.method = method;
    handleRequest.sellerId = sellerId;
    handleRequest.sendResponseCallback = sendResponse;
    
    // already bound
    if (userEmail) {
        submitRequest();
        
        return;
    }
    else 
        if (method == METHOD_QUERY) {
            // shouldn't do Oauth for query request
            sendResponse({
                FOLLOWED: false
            });
            
            return;
        }
    
    // request via Oauth
    console.log("Request user email address via Oauth ..");
    requestUserEmailAndSubmit();
}

function submitRequest(){
    console.log("[Func] submitRequest");
    
    if (!userEmail) {
        reportError("Failed to get user email address.");
        
        return;
    }
    
    doSubmit();
}

function doSubmit(){
    console.log("[Func] doSubmit");
    
    var method = "";
    var url = SITE_URL;
    var data = userEmail + "/" + handleRequest.sellerId;
    if (METHOD_QUERY == handleRequest.method) {
        method = "GET";
        url = SITE_URL + "?useremail=" + userEmail + "&sellerid=" + handleRequest.sellerId;
    }
    else 
        if (METHOD_FOLLOW == handleRequest.method) {
            method = "POST";
			data += "/follow";
        }
        else 
            if (METHOD_UNFOLLOW == handleRequest.method) {
				// it seems GAE doesn't support DELETE method, have to use POST to work around
                method = "POST";
				data += "/unfollow";
            }
            else {
                reportError("Unsupported method: " + handleRequest.method);
                
                return;
            }
    
    doSubmit.xhr = new XMLHttpRequest();
    doSubmit.xhr.onreadystatechange = onDoSubmit;
    doSubmit.xhr.open(method, url, true);
    doSubmit.xhr.send(data);
}

function onDoSubmit(){
    console.log("[Func] onDoSubmit");
    
    console.log(doSubmit.xhr);
    
    var followed = false;
    var status = "";
    var message = "";
    if (doSubmit.xhr.readyState == 4) {
        if (doSubmit.xhr.status == 200) {
            status = "OK";
            if (METHOD_QUERY == handleRequest.method) {
                followed = (doSubmit.xhr.responseText == "true");
            }
            else 
                if (METHOD_FOLLOW == handleRequest.method) {
                    message = "Followed!";
                }
                else 
                    if (METHOD_UNFOLLOW == handleRequest.method) {
                        message = "Unfollowed!";
                    }
        }
        else {
            status = "ERROR";
            message = "Error: Problem retrieving data."
        }
        
        handleRequest.sendResponseCallback({
            STATUS: status,
            FOLLOWED: followed,
            MESSAGE: message
        });
    }
}


function reportError(error){
    console.error(error);
    
    if (handleRequest.sendResponseCallback != null) {
        handleRequest.sendResponseCallback({});
        clearCache();
    }
}

function clearCache(){
    handleRequest.method = "";
    handleRequest.sellerId = "";
    handleRequest.sendResponseCallback = null;
}

function requestUserEmailAndSubmit(){
    console.log("[Func] requestUserEmailAndSubmit");
    
    oauth.authorize(onAuthorized);
}

function onAuthorized(){
    console.log("[Func] onAuthorized");
    
    var url = "http://www.google.com/m8/feeds/contacts/default/full";
    oauth.sendSignedRequest(url, onRequestUserId, {
        'parameters': {
            'alt': 'json',
            'max-results': 0
        }
    });
}

function onRequestUserId(text, xhr){
    console.log("[Func] onRequestUserId");
    
    var data = JSON.parse(text);
    userEmail = data.feed.author[0].email.$t;
    console.log("Got user email address: " + userEmail);
    
    console.log("Saving user email address in cookies ..");
    chrome.cookies.set({
        url: "http://ebaysocialhub.appspot.com/chromeextension/",
        name: "userEmail",
        value: userEmail,
        expirationDate: (new Date().getTime() + 86400 * 7)
    });
    
    // submit request right after we got user email address
    submitRequest();
}
