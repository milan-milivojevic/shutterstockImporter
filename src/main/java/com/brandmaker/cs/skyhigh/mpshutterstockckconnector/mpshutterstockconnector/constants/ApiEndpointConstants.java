package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.constants;

public class ApiEndpointConstants {

    private ApiEndpointConstants() {
        
    }

    public static final String REST_SSO_AUTH = "/rest/sso/auth";
    public static final String REST_SSO_AUTH_TOKEN_VALIDATE = REST_SSO_AUTH + "/universal/token/_validate";

    public static final String DSE_REST = "/dse/rest";
    public static final String DSE_REST_V1 = DSE_REST + "/v1.0";
    public static final String DSE_REST_JOBS = DSE_REST_V1 + "/jobs";
    public static final String DSE_REST_JOBS_SEARCH = DSE_REST_JOBS + "/_search";

    public static final String DSE_REST_INTERNAL = DSE_REST + "/internal";
    public static final String DSE_REST_INTERNAL_JOBS = DSE_REST_INTERNAL + "/jobs";
    public static final String DSE_REST_INTERNAL_DSE_OBJECT_UPDATE = DSE_REST_INTERNAL + "/dse-object/update";
    public static final String DSE_REST_INTERNAL_JOBS_WITH_ID = DSE_REST_INTERNAL_JOBS + "/{jobId}";
    public static final String DSE_REST_INTERNAL_JOBS_WITH_ID_GRIDS = DSE_REST_INTERNAL_JOBS_WITH_ID + "/grids";
    public static final String DSE_REST_INTERNAL_JOBS_WITH_ID_GRIDS_WITH_ID =
            DSE_REST_INTERNAL_JOBS_WITH_ID_GRIDS + "/{gridId}";
    public static final String DSE_REST_INTERNAL_JOBS_WITH_ID_GRIDS_WITH_ID_ROWS =
            DSE_REST_INTERNAL_JOBS_WITH_ID_GRIDS_WITH_ID + "/rows";

    public static final String DSE_REST_JOBS_WITH_ORDINAL_NUMBER = DSE_REST_JOBS + "/{ordinalNumber}";
    public static final String DSE_REST_JOBS_WITH_ORDINAL_NUMBER_GRIDS =
            DSE_REST_JOBS_WITH_ORDINAL_NUMBER + "/grids";
    public static final String DSE_REST_JOBS_WITH_ORDINAL_NUMBER_GRIDS_WITH_ID =
            DSE_REST_JOBS_WITH_ORDINAL_NUMBER_GRIDS + "/{gridId}";
    public static final String DSE_REST_JOBS_WITH_ORDINAL_NUMBER_GRIDS_WITH_ID_ROWS =
            DSE_REST_JOBS_WITH_ORDINAL_NUMBER_GRIDS_WITH_ID + "/rows";
    public static final String DSE_REST_JOBS_WITH_ORDINAL_NUMBER_GRIDS_WITH_ID_ROWS_WITH_ID =
            DSE_REST_JOBS_WITH_ORDINAL_NUMBER_GRIDS_WITH_ID_ROWS + "/{rowId}";
            
    public static final String WEBSERVICES_MEDIA_POOL = "/webservices/MediaPool";
    public static final String REST_MP = "/rest/mp";
    public static final String REST_MP_V12 = REST_MP + "/v1.2";
    public static final String REST_MP_V11 = REST_MP + "/v1.1";
    public static final String REST_MP_ASSETS = REST_MP_V12 + "/assets";
    public static final String REST_MP_SEARCH = REST_MP_V11 + "/search";

}
