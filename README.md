# CommuteStream Android Native Ads SDK

This SDK allows you to add CommuteStream Native Ads to your app.

These instructions assume you have already followed the instructions at: [https://commutestream.com/sdkinstructions](https://commutestream.com/sdkinstructions).


## Requirements

- API 16 (Android 4.1)
- Most likely requires multidex enabled for any significantly large application

## Adding the SDK 
Add the SDK as a gradle dependency in the build.gradle dependencies block, as an example:

**Gradle**
```
dependencies {
    implementation 'com.commutestream.sdk:native-sdk:1.2.2'
}
```

## Overview
CommuteStream Native Ads were built specifically for transit apps. Our SDK is packed with flexible components that allow publishers to seamlessly display advertising alongside public transit information.

**Available Components**

Components are meant to be used where the publisher sees fit, but should be placed with the understanding that the ad data they display, will be relevant to the transit data surrounding them. The following components are available and listed in order of usage priority:

<img width="500px" style="margin: 40px auto; display: block;" src="https://s3.amazonaws.com/commutestream-cdn/native-ad-sdk-readme-assets/components_example.png">

- Headline - Displays a strong call-to-action or phrase that relates the value of the offer in as few words as possible –e.g., *"Free 12 oz. Coffee"*


- Body - Provides a more detailed description of the offer –e.g., *"With purchase any breakfast item."*


- Logo - Shows the brand of the company being advertised. In most cases, logos are meant to be displayed with other components such as a Headline, Body or Advertiser, but can be placed alone as an embeleshment to a list item for a transit stop or route.

**Action Cards**

The Action Card is shown anytime a Logo, Body, Headline, Advertiser, or any grouping thereof is tapped on. It is the ultimate native ad, floating above the contents of the app, it keeps the user experience and related transit information in context. The action card includes the following components by default:

<img width="550px" style="margin: 40px auto; display: block;" src="https://s3.amazonaws.com/commutestream-cdn/native-ad-sdk-readme-assets/action_card_example.png">

- Title - Explains the transit context of the ad. For example, if the user tapped on an icon that was placed on a train stop list, it might read *"Near Belmont Red Line Stop."*


- Subtitle - Further explains the transit context with location information such as a specific address, cross streets, or how far away it is to walk.


- Hero Image - A large image that reenforces the advertiser's brand or message.


- Headline, Body, Logo, and Advertiser - the same compenents used by the publisher to entice users to tap and reveal the Action Card.


- Action Buttons - A maximum of three buttons that perform link-based actions specified by the advertiser. –e.g., *"Download App"*, *"Apply Now"*, or *"Learn More"***


- Close Button - An easy way for the user to quickly opt out.

## Layout

Our SDK requires that the layout for your ad components match their expected types

- Logo - ImageView
- Headline - TextView
- Body - TextView

Your Native Ad layout may be in an existing view layout or in its own. We recommend you create a specific layout
for your Native Ad.

#### ViewBinding

You may then tell the SDK which Views are to be used for which components using the ViewBinder class

**Java**
```java
ViewBinder viewBinder = new ViewBinder(R.layout.my_cs_native_layout);
viewBinder.setLogo(R.id.my_cs_native_logo);
viewBinder.setHeadline(R.id.my_cs_native_headline);
viewBinder.setBody(R.id.my_cs_native_body);
```

This ViewBinder instance is then used when building an Ad view later.

## SDK Usage
### AdRequest
Once you've create your ad layout and a view binder to tell our SDK which views match each component, you'll need to request a list of native ads to populate them. For this you will create an individual **AdRequest**, packaged with transit information that matches what's showing on the screen.

The AdRequest contains all the context we use to find the best matching Ad. There are many ways you may use this to
display ads in different locati @Override
    public void onResume() {
        super.onResume();
        this.adsController.resume();

    }

    @Override
    public void onPause() {
        super.onPause();
        this.adsController.pause();
    }ons in your application. We support matching an Ad for a transit agency, transit route, and a transit stop. We use GTFS route and stop ids to target ads along with a CommuteStream supplied agency id.

An example of a single stop matched AdRequest would be:

#### Single Stop Target AdRequest
**Java**
```java
AdRequest stopAdRequest = new AdRequest().addStop("cta", "red", "1240");
```

Only ads that target that particular stop will be returned for that request.

#### Multiple Target AdRequest

An example of an AdRequest to fill a wider Agency or Route level request would be:
**Java**
```java
AdRequest adRequest = new AdRequest().addAgency("cta").addRoute("cta", "red");
```

Only ads that target the provided agency ("cta") and/or provided route ("cta","red"**.

### AdRequest List

For each Ad you wish to display a corresponding AdRequest should be created and added to a list of requests.

For example if we have two Ads we might want to show on a particular screen we would do something like:

**Java**
```java
AdRequest stopAdRequest = new AdRequest().addStop("cta", "red", "1240");
AdRequest agencyRouteAdRequest = new AdRequest().addAgency("cta").addRoute("cta", "red");
ArrayList<AdRequest> adRequests = new ArrayList<AdRequest();
adRequests.append(stopAdRequest);
adRequests.append(agencyRouteAdRequest);
```

### AdsController
In order to fetch ads with the list of **AdRequest**s, you must call the ``fetchAds`` method on an instance of **AdsController**. This will invoke a callback that returns an **ads** list.

The **ads** list has the same object count and order as the adRequests list provided in the call. This makes it easy to map to the transit context you made the requests for.

All indexes in the list have a **Ad**, and each one is either a null when an ad could not be found or an instance of the Ad class when one is found. You will use this list along with an instance of the ViewBinder to display ad content in the components you added to your ad layout. The AdsController should be a member variable of each Activity you use in your application and instantiated in the onCreate Activity method. It is important to ensure your app correctly reports information and pauses background tasks to call appropriate methods in your Activity onPause and onResume methods as well.

**Java**
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    adsController = new AdsController(this, UUID.fromString(myAdUnit));
    ViewBinder viewBinder = new ViewBinder(R.layout.my_cs_native_layout);
    viewBinder.setLogo(R.id.my_cs_native_logo);
    AdRequest stopAdRequest = new AdRequest().addStop("cta", "red", "1240");
    ArrayList<AdRequest> adReqeusts = new ArrayList();
    adRequests.append(stopAdRequest);
    adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
        @Override
        public void onAds(List<Ad> ads) {
            Ad ad = ads.get(0);
            if(ad != null) {
                mainLayout.addView(adsController.renderAd(null, viewBinder, ad, true));
            }
        }
    });
}

@Override
public void onResume() {
    super.onResume();
    adsController.resume();

}

@Override
public void onPause() {
    super.onPause();
    adsController.pause();
}
```
