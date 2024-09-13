package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.constants.ApiEndpointConstants;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.AssetMetadataTO;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.helpers.WebClientHelper;
import org.springframework.http.MediaType;

public class MediaPoolService {

    private final WebClientHelper webClientHelper;

    public MediaPoolService(WebClientHelper webClientHelper) {
        this.webClientHelper = webClientHelper;
    }

    /**
     * Updates the metadata of an asset.
     *
     * @param assetMetadataTO The AssetMetadataTO object containing the updated metadata.
     */
    public void updateAssetMetadata(final AssetMetadataTO assetMetadataTO) {
        webClientHelper.sendPatchRequest(
                ApiEndpointConstants.REST_MP_ASSETS,
                assetMetadataTO,
                MediaType.APPLICATION_JSON,
                Object.class
        ).getBody();
    }
}
