/**
 * @author: Emac Shen (shen.bin.1983@gmail.com)
 */
var URL_PREFIX_ITEM = "cgi.ebay.com";
var URL_PREFIX_SEARCH = "shop.ebay.com";

var DEFAULT_BACKGROUND_COLOR = "#FEBE43";

var METHOD_QUERY = "QUERY";
var METHOD_FOLLOW = "FOLLOW";
var METHOD_UNFOLLOW = "UNFOLLOW";

var FOLLOW_SELLER_BTN_ID = "socialhub.seller";
var FOLLOW = "Follow";
var UNFOLLOW = "Unfollow";
var DEFAULT_VALUE_SELLER = FOLLOW;

var SAVE_SEARCH_BTN_ID = "socialhub.search";
var SAVE = "Save to SocialHub";
var DEFAULT_VALUE_SAVE = SAVE;

var sellerId = "";
var sellerBtn = null;
var searchBtn = null;

init();

function init(){
    console.log("[Func] init");
    
    if (document.getElementsByClassName("mbg").length > 0) {
        // item page
        initSellerSection();
    }
    else 
        if (document.getElementsByClassName("saveSearch").length > 0) {
            // search page
            initSearchSection();
        }
}

function initSellerSection(){
    console.log("[Func] initSellerSection");
    
    // check follow status
    doQueryFollowingStatus();
}

function doQueryFollowingStatus(){
    console.log("[Func] doQueryFollowingStatus");
    
    var sellerId = getSellerId();
    console.log("Seller ID: " + sellerId);
    
    chrome.extension.sendRequest({
        METHOD: METHOD_QUERY,
        SELLER_ID: sellerId
    }, function(response){
        handleQueryFollowingStatusResponse(response);
    });
}

function handleQueryFollowingStatusResponse(response){
    console.log("[Func] handleQueryFollowingStatusResponse");
    
    // update button status
    updateFollowSellerButton(response.FOLLOWED);
}

function updateFollowSellerButton(followed){
    console.log("[Func] updateFollowSellerButton");
    
    var btn = getFollowSellerButton();
    if (!btn) {
        console.error("Error: unable to create/find button to follow seller.");
        return;
    }
    
    if (followed) {
        btn.value = UNFOLLOW;
        btn.removeEventListener("click", doFollowSeller);
        btn.addEventListener("click", undoFollowSeller);
    }
    else {
        btn.value = FOLLOW;
        btn.removeEventListener("click", undoFollowSeller);
        btn.addEventListener("click", doFollowSeller);
    }
}

function getFollowSellerButton(){
    console.log("[Func] getFollowSellerButton");
    
    if (sellerBtn) {
        return sellerBtn;
    }
    
    sellerBtn = createFollowSellerButton();
    
    return sellerBtn;
}

function createFollowSellerButton(){
    console.log("[Func] createFollowSellerButton");
    
    var elms = document.getElementsByClassName("mbg");
    for (i in elms) {
        if (elms[i].nodeName == "DIV") {
            console.log("<div> found: " + elms[i]);
            
            var span = document.createElement("span");
            span.className = "bn-b psb-b psb-S";
            
            var input = document.createElement("input");
            input.id = FOLLOW_SELLER_BTN_ID;
            input.value = DEFAULT_VALUE_SELLER;
            input.type = "button";
            
            span.appendChild(input);
            elms[i].appendChild(span);
            
            return input;
        }
    }
}

function doFollowSeller(){
    console.log("[Func] doFollowSeller");
    
    var sellerId = getSellerId();
    console.log("Seller ID: " + sellerId);
    
    chrome.extension.sendRequest({
        METHOD: METHOD_FOLLOW,
        SELLER_ID: sellerId
    }, function(response){
        handleFollowSellerResponse(response);
    });
}

function handleResponse(response){
    console.log("[Func] handleResponse");
    
    if (response.MESSAGE) {
        alert(response.MESSAGE);
    }
}

function handleFollowSellerResponse(response){
    console.log("[Func] handleFollowSellerResponse");
    
    handleResponse(response);
    
    if (response.STATUS == "OK") {
        updateFollowSellerButton(true);
    }
}

function undoFollowSeller(){
    console.log("[Func] undoFollowSeller");
    
    var sellerId = getSellerId();
    console.log("Seller ID: " + sellerId);
    
    chrome.extension.sendRequest({
        METHOD: METHOD_UNFOLLOW,
        SELLER_ID: sellerId
    }, function(response){
        handleUnfollowSellerResponse(response);
    });
}

function handleUnfollowSellerResponse(response){
    console.log("[Func] handleUnfollowSellerResponse");
    
    handleResponse(response);
    
    if (response.STATUS == "OK") {
        updateFollowSellerButton(false);
    }
}

function getSellerId(){
    console.log("[Func] getSellerId");
    
    if (sellerId) {
        return sellerId;
    }
    
    var elms = document.getElementsByClassName("mbg-nw");
    for (i in elms) {
        try {
            if (elms[i].nodeName == "SPAN") {
                console.log("<span> found: " + elms[i]);
                
                sellerId = elms[i].innerHTML;
                break;
            }
        } 
        catch (err) {
            console.error(err);
        }
    }
    
    return sellerId;
}

function initSearchSection(){
    console.log("[Func] initSearchSection");
    
    searchBtn = createSaveSearchButton();
}

function createSaveSearchButton(){
    console.log("[Func] createSaveSearchButton");
    
    var elms = document.getElementsByClassName("saveSearch");
    for (i in elms) {
        if (elms[i].nodeName == "SPAN") {
            console.log("<span> found: " + elms[i]);
            
            var span = document.createElement("span");
            span.className = "fsbr";
            span.style.marginLeft = "10px";
            
            var input = document.createElement("input");
            input.id = SAVE_SEARCH_BTN_ID;
            input.value = DEFAULT_VALUE_SAVE;
            input.type = "button";
            input.className = "srch";
            input.addEventListener("click", doSaveSearch);
            
            span.appendChild(input);
            elms[i].parentNode.appendChild(span);
            
            return input;
        }
    }
}

function doSaveSearch(){
    console.log("[Func] doSaveSearch");
    
    handleResponse({
        MESSAGE: "Under construction.."
    });
}
