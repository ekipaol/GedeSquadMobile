package com.bsi.gede_squad_mobile.api;

public class UriApi {

    public static class Baseurl {
        public static final String URLDEV = "http://10.1.25.55:8080/MobileBRISIAPI-EKI/webresources/"; //DEV

        public static final String URL_MAPS = "https://api.opencagedata.com/";

        public static String URL = URLDEV;
        public static String URLIKURMA = URLDEV; //ENV BASED URI SELECTOR
    }

    public class general {
        public static final String searchAddress = "NOS_PRAPEN_Services/rest/APIUmum/InqListRegion";

    }
}