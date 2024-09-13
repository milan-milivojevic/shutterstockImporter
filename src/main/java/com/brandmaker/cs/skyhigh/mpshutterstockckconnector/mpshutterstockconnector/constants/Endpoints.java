package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.constants;

public class Endpoints {
    public static final String REST_SSO_AUTH = "/rest/sso/auth";
    public static final String DSE_REST = "/dse/rest";
    public static final String DSE_REST_V1 = DSE_REST + "/v1";

    public static final String RM_REST = "/rm/rest";
    public static final String RM_REST_V1 = RM_REST + "/v1.0";

    public static final String MP_REST = "/rest/mp";
    public static final String MP_REST_V1_0 = MP_REST + "/v1.0";
    public static final String MP_REST_V1 = MP_REST + "/v1.1";
    public static final String MP_REST_V1_2 = MP_REST + "/v1.2";


    public static final String RM_REST_APPROVAL_STATUS = RM_REST_V1 + "/reviews/_find-dse-variable-type-extended";
    public static final String MP_REST_SET_ASSET_METADATA = MP_REST_V1_2 + "/assets";
    public static final String MP_REST_GET_THEMES = MP_REST_V1_2 + "/themes";
    public static final String MP_REST_GET_VDB_ID = MP_REST_V1 + "/virtual-databases";
    public static final String MP_REST_PUBLICATIONS_ASSET = MP_REST_V1_2 + "/assets/{pathParams}/channel-publications";
    public static final String MP_REST_GENERATION_TASK = MP_REST_V1_2 + "/file-generation-task";
    public static final String MP_REST_DOWNLOAD_FILE = MP_REST_V1_2 + "/download/file-generation-task/{pathParams}";
    public static final String MP_REST_ASSET_LOAD = MP_REST_V1_0 + "/assets/load";
    public static final String MP_REST_GET_METADATA = MP_REST_V1_0 + "/assets/{pathParams}/metadata";


    public static final String BASE_URL = "https://api.shutterstock.com/v2";

    public static final String GET_IMAGES = BASE_URL + "/%s/licenses?team_history=true&sort=oldest&download_availability=downloadable&page=%s&per_page=%s";
    public static final String GET_IMAGES_BY_DATE  = BASE_URL + "/%s/licenses?team_history=true&sort=oldest&download_availability=downloadable&page=%s&per_page=%s&start_date=%s";
    public static final String GET_IMAGE = BASE_URL + "/%s/{id}";
    public static final String GET_METADATA = BASE_URL + "/%s?id=%s&language=%s&view=full";
    public static final String LICENSED_IMAGE = BASE_URL + "/%s/licenses/%s/downloads";
    public static final String GET_CONTRIBUTOR = BASE_URL + "/contributors?id=";
}