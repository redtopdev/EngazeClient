package com.redtop.engaze;

public interface Config {

	// used to share GCM regId with application server - using php app server
	static final String APP_SERVER_URL = "http://125.63.68.121:4459/GCM-App-Server/GCMNotification?shareRegId=1";
//	static final String APP_SERVER_URL = "http://10.0.10.39.8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// GCM server using java
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "111568034099";
	static final String MESSAGE_KEY = "ASHISH";
	static final String IMAGE_DATA = "image_data";
	static final String BROWSER_URL = "browser_url";

}
