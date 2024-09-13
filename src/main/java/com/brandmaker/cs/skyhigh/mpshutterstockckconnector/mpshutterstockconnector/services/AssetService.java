package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services;


import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.constants.ApiEndpointConstants;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.AssetMetadataTO;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.AssetSearchResponseTO;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.AssetSearchTO;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.generated.mediapool.MediaPoolWebServicePort;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.generated.mediapool.UploadMediaResult;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.helpers.WebClientHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AssetService {

    private final WebClientHelper webClientHelper;
    private final MediaPoolWebServicePort mediaPoolWebServicePort;

    public AssetService(final WebClientHelper webClientHelper,
                        final MediaPoolWebServicePort mediaPoolWebServicePort) {
        this.webClientHelper = webClientHelper;
        this.mediaPoolWebServicePort = mediaPoolWebServicePort;
    }

    /**
     * Uploads a file to the media pool.
     *
     * @param file        The file to be uploaded.
     * @return An UploadMediaResult object representing the response from the media pool.
     */
    public UploadMediaResult uploadFileToMediaPool(final File file) {

        final FileDataSource dataSource = new FileDataSource(file);
        final DataHandler dataHandler = new DataHandler(dataSource);

        log.info("name: " + file.getName());
        return mediaPoolWebServicePort.uploadMediaAsStream(file.getName(), dataHandler);
    }

    /**
     * Retrieves the asset ID associated with a given hash code from the media pool.
     *
     * @param hashCode The hash code representing the media asset.
     * @return The asset ID (media GUID) corresponding to the provided hash code.
     */
    public String getAssetIdByHashCode(final String hashCode) {
        return mediaPoolWebServicePort.getMediaGuidByHash(hashCode);
    }

    /**
     * Updates the metadata of an asset.
     *
     * @param assetMetadataTO The AssetMetadataTO object containing the updated metadata.
     */
    public void updateAssetMetadata(final AssetMetadataTO assetMetadataTO) {
        ResponseEntity<?> response = webClientHelper.sendPatchRequest(
                ApiEndpointConstants.REST_MP_ASSETS,
                assetMetadataTO,
                MediaType.APPLICATION_JSON,
                Object.class
        );
        log.info("Update metadata response: " + response.getStatusCode());
    }

    /**
     * Searches for assets based on the provided search criteria.
     *
     * @param assetSearchTO The AssetSearchTO object containing the search criteria.
     * @return An AssetSearchResponseTO object representing the response from the asset search.
     */
    public AssetSearchResponseTO searchAssets(final AssetSearchTO assetSearchTO) {
        return webClientHelper.sendPostRequest(
                ApiEndpointConstants.REST_MP_SEARCH,
                assetSearchTO,
                MediaType.APPLICATION_JSON,
                AssetSearchResponseTO.class
        ).getBody();
    }

}
