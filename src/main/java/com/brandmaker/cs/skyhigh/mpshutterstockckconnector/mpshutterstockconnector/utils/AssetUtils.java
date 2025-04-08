package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.utils;


import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j

public class AssetUtils {

    private static final String ERROR_HANDLING_STRATEGY = "FAIL_ON_ERROR";

    private static final String TYPE_FIELD = "@type";
    private static final String PROPERTY_NAME_FIELD = "propertyName";
    private static final String VDB_ID_FIELD = "vdbId";
    private static final String VALUE_FIELD = "value";
    private static final String VALUES_FIELD = "values";

    private static final String REPLACE_VDB_TYPE = "replace_vdb";
    private static final String ADD_INT_TYPE = "add_int";
    private static final String REPLACE_INT_TYPE = "replace_int";

    private static final String VDB_PROPERTY = "vdb";
    private static final String CATEGORIES_PROPERTY = "categories";
    private static final String ASSET_TYPE_PROPERTY = "assetType";

    private static final Integer DATA_MIGRATION_VDB_ID = 1903;
    private static final Integer DATA_MIGRATION_CATEGORY_ID = 14;
    private static final Integer STANDARD_ASSET_TYPE_ID = 7;

    private AssetUtils() {
    }

    /**
     * Computes the asset metadata for a given media GUID.
     *
     * @param mediaGuid The unique identifier (GUID) of the media asset for which to compute the metadata.
     * @return An {@link AssetMetadataTO} object containing the computed asset metadata.
     * @see AssetMetadataTO
     */
    public static AssetMetadataTO computeAssetMetadata(final String mediaGuid, ImageDownloadDTO image, ShutterstockImageMetadataDto imageMetadataEn, ShutterstockImageMetadataDto imageMetadataDe) {

        try {
            final AssetMetadataTO assetMetadataTO = new AssetMetadataTO();
            final List<String> assetIds = new ArrayList<>(Collections.singletonList(mediaGuid));

            assetMetadataTO.setAssetIds(assetIds);
            assetMetadataTO.setErrorHandlingStrategy(ERROR_HANDLING_STRATEGY);

            final ObjectMapper mapper = new ObjectMapper();
            final ArrayNode updateOperations = mapper.createArrayNode();

            //asset type (file information) operation
            final ObjectNode assetTypeOperation = mapper.createObjectNode();
            assetTypeOperation.put(TYPE_FIELD, REPLACE_INT_TYPE);
            assetTypeOperation.put(PROPERTY_NAME_FIELD, ASSET_TYPE_PROPERTY);
            assetTypeOperation.put(VALUE_FIELD, STANDARD_ASSET_TYPE_ID);
            updateOperations.add(assetTypeOperation);

            //vdb group operation
            final ObjectNode vdbOperation = mapper.createObjectNode();
            vdbOperation.put(TYPE_FIELD, REPLACE_VDB_TYPE);
            vdbOperation.put(PROPERTY_NAME_FIELD, VDB_PROPERTY);

            final ObjectNode vdbValue = mapper.createObjectNode();
            vdbValue.put(VDB_ID_FIELD, DATA_MIGRATION_VDB_ID);
            vdbOperation.set(VALUE_FIELD, vdbValue);
            updateOperations.add(vdbOperation);

            //asset category operation
            final ObjectNode categoryOperation = mapper.createObjectNode();
            categoryOperation.put(TYPE_FIELD, "add_int");
            categoryOperation.put(PROPERTY_NAME_FIELD, "categories");

            final ArrayNode categoryValues = mapper.createArrayNode();
            categoryValues.add("28");
            categoryValues.add("91");
            categoryValues.add("14");
            categoryOperation.set(VALUES_FIELD, categoryValues);

            updateOperations.add(categoryOperation);

            // Asset title operation
            final ObjectNode titleOperation = mapper.createObjectNode();
            titleOperation.put(TYPE_FIELD, "replace_multilang");
            titleOperation.put(PROPERTY_NAME_FIELD, "assetTitle");

            final ObjectNode titleValue = mapper.createObjectNode();
            String enTitle = "Asset name  missing";
            String deTitle = "Asset-Name  fehlt";

            if (!imageMetadataEn.getData().isEmpty()) {

                String shutterstockId = image.getImage().getId();

                String descriptionEn = imageMetadataEn.getData().get(0).getDescription();

                if (!descriptionEn.isEmpty()) {
                    int commaIndex = descriptionEn.indexOf(",");
                    int periodIndex = descriptionEn.indexOf(".");
                    int cutoffIndex;
                    if (commaIndex == -1 && periodIndex == -1) {
                        cutoffIndex = descriptionEn.length();  // No comma or period, use entire description
                    } else if (commaIndex == -1) {
                        cutoffIndex = periodIndex;  // Only period exists
                    } else if (periodIndex == -1) {
                        cutoffIndex = commaIndex;  // Only comma exists
                    } else {
                        cutoffIndex = Math.min(commaIndex, periodIndex);  // Both exist, take the first
                    }
                    descriptionEn = descriptionEn.substring(0, cutoffIndex);
                } else {
                    descriptionEn = "Description missing";
                }

                String descriptionDe = imageMetadataDe.getData().get(0).getDescription();

                if (!descriptionDe.isEmpty()) {
                    int commaIndex = descriptionDe.indexOf(",");
                    int periodIndex = descriptionDe.indexOf(".");
                    int cutoffIndex;
                    if (commaIndex == -1 && periodIndex == -1) {
                        cutoffIndex = descriptionDe.length();
                    } else if (commaIndex == -1) {
                        cutoffIndex = periodIndex;
                    } else if (periodIndex == -1) {
                        cutoffIndex = commaIndex;
                    } else {
                        cutoffIndex = Math.min(commaIndex, periodIndex);
                    }
                    descriptionDe = descriptionDe.substring(0, cutoffIndex);
                } else {
                    descriptionDe = "Beschreibung fehlt";
                }

                int width = imageMetadataEn.getData().get(0).getAssets().getHugeJpg().getWidth();
                int height = imageMetadataEn.getData().get(0).getAssets().getHugeJpg().getHeight();

                String format = width + " x " + height + " px";

                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

                enTitle = String.format("STOCK Image %s %s %s %s", shutterstockId, descriptionEn, format, currentDate);
                deTitle = String.format("STOCK Bild %s %s %s %s", shutterstockId, descriptionDe, format, currentDate);
            }

            titleValue.put("EN", enTitle);
            titleValue.put("DE", deTitle);

            log.info("titleValue: {}", titleValue);
            titleOperation.set(VALUE_FIELD, titleValue);
            log.info("titleOperation: {}", titleOperation);
            updateOperations.add(titleOperation);

            // file name operation
            final ObjectNode fileNameOperation = mapper.createObjectNode();
            fileNameOperation.put(TYPE_FIELD, "replace_string");
            fileNameOperation.put(PROPERTY_NAME_FIELD, "fileName");

            String shutterstockId = image.getImage().getId();

            String description = "Description missing";
            if (!imageMetadataEn.getData().isEmpty()) {
                String descriptionEn = imageMetadataEn.getData().get(0).getDescription();
                if (!descriptionEn.isEmpty()) {
                    // Find the indices of the first comma and period
                    int commaIndex = descriptionEn.indexOf(",");
                    int periodIndex = descriptionEn.indexOf(".");

                    // Determine the cutoff index based on which comes first and exists
                    int cutoffIndex;
                    if (commaIndex == -1 && periodIndex == -1) {
                        cutoffIndex = descriptionEn.length(); // No comma or period, use entire description
                    } else if (commaIndex == -1) {
                        cutoffIndex = periodIndex; // Only period exists
                    } else if (periodIndex == -1) {
                        cutoffIndex = commaIndex; // Only comma exists
                    } else {
                        cutoffIndex = Math.min(commaIndex, periodIndex); // Both exist, take the first
                    }

                    // Set the description up to the cutoff index, replacing spaces with underscores
                    description = descriptionEn.substring(0, cutoffIndex).replace(" ", "_");
                }
            }

            int width = imageMetadataEn.getData().get(0).getAssets().getHugeJpg().getWidth();
            int height = imageMetadataEn.getData().get(0).getAssets().getHugeJpg().getHeight();

            String format = width + "_x_" + height + "_px";

            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

            String finalFileName = String.format(
              "STOCK_IMG_%s_%s_%s_%s",
              shutterstockId,
              description,
              format,
              currentDate
            );

            log.info("finalFileName: {}", finalFileName);
            fileNameOperation.put(VALUE_FIELD, finalFileName);
            log.info("fileNameOperation: {}", fileNameOperation);
            updateOperations.add(fileNameOperation);

            //structured keywords operation
            final ObjectNode keywordsOperation = mapper.createObjectNode();
            keywordsOperation.put(TYPE_FIELD, "add_int");
            keywordsOperation.put(PROPERTY_NAME_FIELD, "keywords");

            final ArrayNode keywordsValues = mapper.createArrayNode();
            keywordsValues.add("10188");
            keywordsOperation.set(VALUES_FIELD, keywordsValues);
            updateOperations.add(keywordsOperation);

            // any keywords operation
            final ObjectNode anyKeywordsOperation = mapper.createObjectNode();
            anyKeywordsOperation.put(TYPE_FIELD, "replace_free_fields");
            anyKeywordsOperation.put(PROPERTY_NAME_FIELD, "freeFields");

            final ObjectNode anyKeywordsValueWrapper = mapper.createObjectNode();
            final ObjectNode anyKeywordsValue = mapper.createObjectNode();

            String enKeywords = "Keywords missing";
            String deKeywords = "Schlüsselwörter fehlen";

            if (!imageMetadataEn.getData().isEmpty() && !imageMetadataEn.getData().get(0).getKeywords().isEmpty()) {
                List<String> keywords = imageMetadataEn.getData().get(0).getKeywords();
                enKeywords = String.join(",\n", keywords);
            }

            if (!imageMetadataDe.getData().isEmpty() && !imageMetadataDe.getData().get(0).getKeywords().isEmpty()) {
                List<String> keywords = imageMetadataDe.getData().get(0).getKeywords();
                deKeywords = String.join(",\n", keywords);
            }

            anyKeywordsValue.put("EN", enKeywords);
            anyKeywordsValue.put("DE", deKeywords);


            anyKeywordsValueWrapper.put("@type", "text");
            anyKeywordsValueWrapper.put("id", "10");
            anyKeywordsValueWrapper.set("value", anyKeywordsValue);

            ArrayNode valuesArray = mapper.createArrayNode();
            valuesArray.add(anyKeywordsValueWrapper);

            anyKeywordsOperation.set("values", valuesArray);

            log.info("valuesArray: {}", valuesArray);
            log.info("anyKeywordsOperation: {}", anyKeywordsOperation);
            updateOperations.add(anyKeywordsOperation);


            //asset description operation
            final ObjectNode assetDescriptionOperation = mapper.createObjectNode();
            assetDescriptionOperation.put(TYPE_FIELD, "replace_multilang");
            assetDescriptionOperation.put(PROPERTY_NAME_FIELD, "description");

            final ObjectNode assetDescriptionValue = mapper.createObjectNode();
            String enDescription = "Description missing";
            String deDescription = "Beschreibung fehlt";

            if (!imageMetadataEn.getData().isEmpty()) {
                String descriptionEn = imageMetadataEn.getData().get(0).getDescription();
                String descriptionDe = imageMetadataDe.getData().get(0).getDescription();

                if (!descriptionEn.isEmpty()) {
                    enDescription = descriptionEn;
                }
                if (!descriptionDe.isEmpty()) {
                    deDescription = descriptionDe;
                }
            }

            assetDescriptionValue.put("EN", enDescription);
            assetDescriptionValue.put("DE", deDescription);

            log.info("assetDescriptionValue: {}", assetDescriptionValue);
            assetDescriptionOperation.set(VALUE_FIELD, assetDescriptionValue);
            log.info("assetDescriptionOperation: {}", assetDescriptionOperation);
            updateOperations.add(assetDescriptionOperation);

            // license operation
            final ObjectNode licenseOperation = mapper.createObjectNode();
            licenseOperation.put(TYPE_FIELD, "replace_int");
            licenseOperation.put(PROPERTY_NAME_FIELD, "license");
            licenseOperation.put(VALUE_FIELD, 123);

            updateOperations.add(licenseOperation);

            // stock ID operation
            final ObjectNode multilangOperation = mapper.createObjectNode();
            multilangOperation.put(TYPE_FIELD, "replace_multilang");
            multilangOperation.put(PROPERTY_NAME_FIELD, "customAttribute_276");

            final ObjectNode valueObject = mapper.createObjectNode();
            valueObject.put("DE", image.getImage().getId());
            valueObject.put("EN", image.getImage().getId());

            multilangOperation.set(VALUE_FIELD, valueObject);

            updateOperations.add(multilangOperation);



            assetMetadataTO.setUpdateOperations(updateOperations);
            return assetMetadataTO;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static AssetMetadataTO computeVideoMetadata(final String mediaGuid, VideoDownloadDTO video, ShutterstockVideoMetadataDto videoMetadataEn, ShutterstockVideoMetadataDto videoMetadataDe) {

        try {
            final AssetMetadataTO assetMetadataTO = new AssetMetadataTO();
            final List<String> assetIds = new ArrayList<>(Collections.singletonList(mediaGuid));

            assetMetadataTO.setAssetIds(assetIds);
            assetMetadataTO.setErrorHandlingStrategy(ERROR_HANDLING_STRATEGY);

            final ObjectMapper mapper = new ObjectMapper();
            final ArrayNode updateOperations = mapper.createArrayNode();

            //asset type (file information) operation
            final ObjectNode assetTypeOperation = mapper.createObjectNode();
            assetTypeOperation.put(TYPE_FIELD, REPLACE_INT_TYPE);
            assetTypeOperation.put(PROPERTY_NAME_FIELD, ASSET_TYPE_PROPERTY);
            assetTypeOperation.put(VALUE_FIELD, STANDARD_ASSET_TYPE_ID);
            updateOperations.add(assetTypeOperation);

            //vdb group operation
            final ObjectNode vdbOperation = mapper.createObjectNode();
            vdbOperation.put(TYPE_FIELD, REPLACE_VDB_TYPE);
            vdbOperation.put(PROPERTY_NAME_FIELD, VDB_PROPERTY);

            final ObjectNode vdbValue = mapper.createObjectNode();
            vdbValue.put(VDB_ID_FIELD, DATA_MIGRATION_VDB_ID);
            vdbOperation.set(VALUE_FIELD, vdbValue);
            updateOperations.add(vdbOperation);

            //asset category operation
            final ObjectNode categoryOperation = mapper.createObjectNode();
            categoryOperation.put(TYPE_FIELD, "add_int");
            categoryOperation.put(PROPERTY_NAME_FIELD, "categories");

            final ArrayNode categoryValues = mapper.createArrayNode();
            categoryValues.add("28");
            categoryValues.add("91");
            categoryValues.add("14");
            categoryOperation.set(VALUES_FIELD, categoryValues);

            updateOperations.add(categoryOperation);


            // Asset title operation
            final ObjectNode titleOperation = mapper.createObjectNode();
            titleOperation.put(TYPE_FIELD, "replace_multilang");
            titleOperation.put(PROPERTY_NAME_FIELD, "assetTitle");

            final ObjectNode titleValue = mapper.createObjectNode();
            String enTitle = "Asset name  missing";
            String deTitle = "Asset-Name  fehlt";

            if (!videoMetadataEn.getData().isEmpty()) {

                String shutterstockId = video.getVideo().getId();

                String descriptionEn = videoMetadataEn.getData().get(0).getDescription();

                if (!descriptionEn.isEmpty()) {
                    int commaIndex = descriptionEn.indexOf(",");
                    int periodIndex = descriptionEn.indexOf(".");
                    int cutoffIndex;
                    if (commaIndex == -1 && periodIndex == -1) {
                        cutoffIndex = descriptionEn.length();  // No comma or period, use entire description
                    } else if (commaIndex == -1) {
                        cutoffIndex = periodIndex;  // Only period exists
                    } else if (periodIndex == -1) {
                        cutoffIndex = commaIndex;  // Only comma exists
                    } else {
                        cutoffIndex = Math.min(commaIndex, periodIndex);  // Both exist, take the first
                    }
                    descriptionEn = descriptionEn.substring(0, cutoffIndex);
                } else {
                    descriptionEn = "Description missing";
                }

                String descriptionDe = videoMetadataDe.getData().get(0).getDescription();

                if (!descriptionDe.isEmpty()) {
                    int commaIndex = descriptionDe.indexOf(",");
                    int periodIndex = descriptionDe.indexOf(".");
                    int cutoffIndex;
                    if (commaIndex == -1 && periodIndex == -1) {
                        cutoffIndex = descriptionDe.length();
                    } else if (commaIndex == -1) {
                        cutoffIndex = periodIndex;
                    } else if (periodIndex == -1) {
                        cutoffIndex = commaIndex;
                    } else {
                        cutoffIndex = Math.min(commaIndex, periodIndex);
                    }
                    descriptionDe = descriptionDe.substring(0, cutoffIndex);
                } else {
                    descriptionDe = "Beschreibung fehlt";
                }

                int fps = videoMetadataEn.getData().get(0).getAssets().getWeb().getFps();

                String format = videoMetadataEn.getData().get(0).getAspectRatio();

                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

                enTitle = String.format("STOCK Video %s %s %sfps %s %s", shutterstockId, descriptionEn, fps, format, currentDate);
                deTitle = String.format("STOCK Video %s %s %sfps %s %s", shutterstockId, descriptionDe, fps, format, currentDate);
            }

            titleValue.put("EN", enTitle);
            titleValue.put("DE", deTitle);

            log.info("titleValue: {}", titleValue);
            titleOperation.set(VALUE_FIELD, titleValue);
            log.info("titleOperation: {}", titleOperation);
            updateOperations.add(titleOperation);

            // file name operation
            final ObjectNode fileNameOperation = mapper.createObjectNode();
            fileNameOperation.put(TYPE_FIELD, "replace_string");
            fileNameOperation.put(PROPERTY_NAME_FIELD, "fileName");

            String shutterstockId = video.getVideo().getId();

            String description = "Description missing";
            if (!videoMetadataEn.getData().isEmpty()) {
                String descriptionEn = videoMetadataEn.getData().get(0).getDescription();
                if (!descriptionEn.isEmpty()) {
                    // Find the indices of the first comma and period
                    int commaIndex = descriptionEn.indexOf(",");
                    int periodIndex = descriptionEn.indexOf(".");

                    // Determine the cutoff index based on which comes first and exists
                    int cutoffIndex;
                    if (commaIndex == -1 && periodIndex == -1) {
                        cutoffIndex = descriptionEn.length(); // No comma or period, use entire description
                    } else if (commaIndex == -1) {
                        cutoffIndex = periodIndex; // Only period exists
                    } else if (periodIndex == -1) {
                        cutoffIndex = commaIndex; // Only comma exists
                    } else {
                        cutoffIndex = Math.min(commaIndex, periodIndex); // Both exist, take the first
                    }

                    // Set the description up to the cutoff index, replacing spaces with underscores
                    description = descriptionEn.substring(0, cutoffIndex).replace(" ", "_");
                }
            }

            int fps = videoMetadataEn.getData().get(0).getAssets().getWeb().getFps();

            String format = videoMetadataEn.getData().get(0).getAspectRatio().replace(":", "-");

            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

            String finalFileName = String.format(
              "STOCK_VID_%s_%s_%sfps_%s_%s",
              shutterstockId,
              description,
              fps,
              format,
              currentDate
            );

            log.info("finalFileName: {}", finalFileName);
            fileNameOperation.put(VALUE_FIELD, finalFileName);
            log.info("fileNameOperation: {}", fileNameOperation);
            updateOperations.add(fileNameOperation);

            //asset description operation
            final ObjectNode assetDescriptionOperation = mapper.createObjectNode();
            assetDescriptionOperation.put(TYPE_FIELD, "replace_multilang");
            assetDescriptionOperation.put(PROPERTY_NAME_FIELD, "description");

            final ObjectNode assetDescriptionValue = mapper.createObjectNode();
            String enDescription = "Description missing";
            String deDescription = "Beschreibung fehlt";

            if (!videoMetadataEn.getData().isEmpty()) {
                String descriptionEn = videoMetadataEn.getData().get(0).getDescription();
                String descriptionDe = videoMetadataDe.getData().get(0).getDescription();

                if (!descriptionEn.isEmpty()) {
                    enDescription = descriptionEn;
                }
                if (!descriptionDe.isEmpty()) {
                    deDescription = descriptionDe;
                }
            }

            assetDescriptionValue.put("EN", enDescription);
            assetDescriptionValue.put("DE", deDescription);

            log.info("assetDescriptionValue: {}", assetDescriptionValue);
            assetDescriptionOperation.set(VALUE_FIELD, assetDescriptionValue);
            log.info("assetDescriptionOperation: {}", assetDescriptionOperation);
            updateOperations.add(assetDescriptionOperation);

            //structured keywords operation
            final ObjectNode keywordsOperation = mapper.createObjectNode();
            keywordsOperation.put(TYPE_FIELD, "add_int");
            keywordsOperation.put(PROPERTY_NAME_FIELD, "keywords");

            final ArrayNode keywordsValues = mapper.createArrayNode();
            keywordsValues.add("10188");
            keywordsOperation.set(VALUES_FIELD, keywordsValues);
            updateOperations.add(keywordsOperation);

            // any keywords operation
            final ObjectNode anyKeywordsOperation = mapper.createObjectNode();
            anyKeywordsOperation.put(TYPE_FIELD, "replace_free_fields");
            anyKeywordsOperation.put(PROPERTY_NAME_FIELD, "freeFields");

            final ObjectNode anyKeywordsValueWrapper = mapper.createObjectNode();
            final ObjectNode anyKeywordsValue = mapper.createObjectNode();

            String enKeywords = "Keywords missing";
            String deKeywords = "Schlüsselwörter fehlen";

            if (!videoMetadataEn.getData().isEmpty() && !videoMetadataEn.getData().get(0).getKeywords().isEmpty()) {
                List<String> keywords = videoMetadataEn.getData().get(0).getKeywords();
                enKeywords = String.join(",\n", keywords);
            }

            if (!videoMetadataDe.getData().isEmpty() && !videoMetadataDe.getData().get(0).getKeywords().isEmpty()) {
                List<String> keywords = videoMetadataDe.getData().get(0).getKeywords();
                deKeywords = String.join(",\n", keywords);
            }

            anyKeywordsValue.put("EN", enKeywords);
            anyKeywordsValue.put("DE", deKeywords);


            anyKeywordsValueWrapper.put("@type", "text");
            anyKeywordsValueWrapper.put("id", "10");
            anyKeywordsValueWrapper.set("value", anyKeywordsValue);

            ArrayNode valuesArray = mapper.createArrayNode();
            valuesArray.add(anyKeywordsValueWrapper);

            anyKeywordsOperation.set("values", valuesArray);

            log.info("valuesArray: {}", valuesArray);
            log.info("anyKeywordsOperation: {}", anyKeywordsOperation);
            updateOperations.add(anyKeywordsOperation);

            // license operation
            final ObjectNode licenseOperation = mapper.createObjectNode();
            licenseOperation.put(TYPE_FIELD, "replace_int");
            licenseOperation.put(PROPERTY_NAME_FIELD, "license");
            licenseOperation.put(VALUE_FIELD, 132);

            log.info("licenseOperation: {}", licenseOperation);
            updateOperations.add(licenseOperation);

            // stock ID operation
            final ObjectNode multilangOperation = mapper.createObjectNode();
            multilangOperation.put(TYPE_FIELD, "replace_multilang");
            multilangOperation.put(PROPERTY_NAME_FIELD, "customAttribute_276");

            final ObjectNode valueObject = mapper.createObjectNode();
            valueObject.put("DE", video.getVideo().getId());
            valueObject.put("EN", video.getVideo().getId());

            log.info("valueObject: {}", valueObject);
            multilangOperation.set(VALUE_FIELD, valueObject);
            log.info("multilangOperation: {}", multilangOperation);
            updateOperations.add(multilangOperation);

            // Custom attribute operation
            final ObjectNode customAttributeOperation = mapper.createObjectNode();
            customAttributeOperation.put(TYPE_FIELD, "replace_multilang");
            customAttributeOperation.put(PROPERTY_NAME_FIELD, "customAttribute_326");

            final ObjectNode customAttributeValue = mapper.createObjectNode();

            String enCustomAttribute = "<p>For further queries</p>\n\n" +
              "<p>Do you need the high-resolution file of this video? Please contact the ZF colleague who is responsible for Shutterstock " +
              "or our hotline at <a aria-label=\"Link help@lsd.de\" href=\"mailto:help@lsd.de\" id=\"menur23e\" " +
              "rel=\"noreferrer noopener\" target=\"_blank\" title=\"mailto:help@lsd.de\">help@lsd.de</a>. " +
              "Please be sure to include the media ID.</p>\n";

            String deCustomAttribute = "<p>Bei Rückfragen</p>\n\n" +
              "<p>Sie benötigen von diesem Video die hochauflösende Datei? Bitte kontaktieren Sie den für Shutterstock " +
              "zuständigen ZF-Kollegen oder unsere Hotline unter <a aria-label=\"Link help@lsd.de\" href=\"mailto:help@lsd.de\" " +
              "id=\"menur23g\" rel=\"noreferrer noopener\" target=\"_blank\" title=\"mailto:help@lsd.de\">help@lsd.de</a>. " +
              "Geben Sie bitte unbedingt die Medien-ID an.</p>\n";

            customAttributeValue.put("EN", enCustomAttribute);
            customAttributeValue.put("DE", deCustomAttribute);

            log.info("customAttributeValue: {}", customAttributeValue);
            customAttributeOperation.set(VALUE_FIELD, customAttributeValue);
            log.info("customAttributeOperation: {}", customAttributeOperation);
            updateOperations.add(customAttributeOperation);



            assetMetadataTO.setUpdateOperations(updateOperations);
            return assetMetadataTO;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    public static AssetMetadataTO computeAudioMetadata(final String mediaGuid, AudioDownloadDTO audio, ShutterstockAudioMetadataDto audioMetadataEn, ShutterstockAudioMetadataDto audioMetadataDe) {

        try {
            final AssetMetadataTO assetMetadataTO = new AssetMetadataTO();
            final List<String> assetIds = new ArrayList<>(Collections.singletonList(mediaGuid));

            assetMetadataTO.setAssetIds(assetIds);
            assetMetadataTO.setErrorHandlingStrategy(ERROR_HANDLING_STRATEGY);

            final ObjectMapper mapper = new ObjectMapper();
            final ArrayNode updateOperations = mapper.createArrayNode();

            //asset type (file information) operation
            final ObjectNode assetTypeOperation = mapper.createObjectNode();
            assetTypeOperation.put(TYPE_FIELD, REPLACE_INT_TYPE);
            assetTypeOperation.put(PROPERTY_NAME_FIELD, ASSET_TYPE_PROPERTY);
            assetTypeOperation.put(VALUE_FIELD, STANDARD_ASSET_TYPE_ID);
            updateOperations.add(assetTypeOperation);

            //vdb group operation
            final ObjectNode vdbOperation = mapper.createObjectNode();
            vdbOperation.put(TYPE_FIELD, REPLACE_VDB_TYPE);
            vdbOperation.put(PROPERTY_NAME_FIELD, VDB_PROPERTY);

            final ObjectNode vdbValue = mapper.createObjectNode();
            vdbValue.put(VDB_ID_FIELD, DATA_MIGRATION_VDB_ID);
            vdbOperation.set(VALUE_FIELD, vdbValue);
            updateOperations.add(vdbOperation);

            //asset category operation
            final ObjectNode categoryOperation = mapper.createObjectNode();
            categoryOperation.put(TYPE_FIELD, "add_int");
            categoryOperation.put(PROPERTY_NAME_FIELD, "categories");

            final ArrayNode categoryValues = mapper.createArrayNode();
            categoryValues.add("28");
            categoryValues.add("91");
            categoryValues.add("14");
            categoryOperation.set(VALUES_FIELD, categoryValues);

            updateOperations.add(categoryOperation);

            // Asset title operation
            final ObjectNode titleOperation = mapper.createObjectNode();
            titleOperation.put(TYPE_FIELD, "replace_multilang");
            titleOperation.put(PROPERTY_NAME_FIELD, "assetTitle");

            final ObjectNode titleValue = mapper.createObjectNode();
            String enTitle = "Asset name missing";
            String deTitle = "Asset-Name fehlt";

            if (!audioMetadataEn.getData().isEmpty()) {
                String originalFilenameEn = audioMetadataEn.getData().get(0).getTitle();
                String originalFilenameDe = audioMetadataDe.getData().get(0).getTitle();

                if (!originalFilenameEn.isEmpty()) {
                    enTitle = originalFilenameEn.replace("_", " ");
                    enTitle = "STOCK " + enTitle;
                }
                if (!originalFilenameDe.isEmpty()) {
                    deTitle = originalFilenameDe.replace("_", " ");
                    deTitle = "STOCK " + deTitle;
                }
            }

            titleValue.put("EN", enTitle);
            titleValue.put("DE", deTitle);

            log.info("titleValue: {}", titleValue);
            titleOperation.set(VALUE_FIELD, titleValue);
            log.info("titleOperation: {}", titleOperation);
            updateOperations.add(titleOperation);

            //file name operation
            final ObjectNode fileNameOperation = mapper.createObjectNode();
            fileNameOperation.put(TYPE_FIELD, "replace_string");
            fileNameOperation.put(PROPERTY_NAME_FIELD, "fileName");
            if (!audioMetadataEn.getData().isEmpty() && !audioMetadataEn.getData().get(0).getTitle().isEmpty()) {
                fileNameOperation.put(VALUE_FIELD, "STOCK_" + audioMetadataEn.getData().get(0).getTitle());
            } else {
                fileNameOperation.put(VALUE_FIELD, "File name missing");
            }

            updateOperations.add(fileNameOperation);
            log.info("fileNameOperation: {}", fileNameOperation);

            updateOperations.add(fileNameOperation);

            //structured keywords operation
            final ObjectNode keywordsOperation = mapper.createObjectNode();
            keywordsOperation.put(TYPE_FIELD, "add_int");
            keywordsOperation.put(PROPERTY_NAME_FIELD, "keywords");

            final ArrayNode keywordsValues = mapper.createArrayNode();
            keywordsValues.add("10188");
            keywordsOperation.set(VALUES_FIELD, keywordsValues);
            updateOperations.add(keywordsOperation);

            // any keywords operation
            final ObjectNode anyKeywordsOperation = mapper.createObjectNode();
            anyKeywordsOperation.put(TYPE_FIELD, "replace_free_fields");
            anyKeywordsOperation.put(PROPERTY_NAME_FIELD, "freeFields");

            final ObjectNode anyKeywordsValueWrapper = mapper.createObjectNode();
            final ObjectNode anyKeywordsValue = mapper.createObjectNode();

            String enKeywords = "Keywords missing";
            String deKeywords = "Schlüsselwörter fehlen";

            if (!audioMetadataEn.getData().isEmpty() && !audioMetadataEn.getData().get(0).getKeywords().isEmpty()) {
                List<String> keywords = audioMetadataEn.getData().get(0).getKeywords();
                enKeywords = String.join(",\n", keywords);
            }

            if (!audioMetadataDe.getData().isEmpty() && !audioMetadataDe.getData().get(0).getKeywords().isEmpty()) {
                List<String> keywords = audioMetadataDe.getData().get(0).getKeywords();
                deKeywords = String.join(",\n", keywords);
            }

            anyKeywordsValue.put("EN", enKeywords);
            anyKeywordsValue.put("DE", deKeywords);


            anyKeywordsValueWrapper.put("@type", "text");
            anyKeywordsValueWrapper.put("id", "10");
            anyKeywordsValueWrapper.set("value", anyKeywordsValue);

            ArrayNode valuesArray = mapper.createArrayNode();
            valuesArray.add(anyKeywordsValueWrapper);

            anyKeywordsOperation.set("values", valuesArray);

            log.info("valuesArray: {}", valuesArray);
            log.info("anyKeywordsOperation: {}", anyKeywordsOperation);
            updateOperations.add(anyKeywordsOperation);


            //asset description operation
            final ObjectNode assetDescriptionOperation = mapper.createObjectNode();
            assetDescriptionOperation.put(TYPE_FIELD, "replace_multilang");
            assetDescriptionOperation.put(PROPERTY_NAME_FIELD, "description");

            final ObjectNode assetDescriptionValue = mapper.createObjectNode();
            String enDescription = "Description missing";
            String deDescription = "Beschreibung fehlt";

            if (!audioMetadataEn.getData().isEmpty()) {
                String descriptionEn = audioMetadataEn.getData().get(0).getDescription();
                String descriptionDe = audioMetadataDe.getData().get(0).getDescription();

                if (!descriptionEn.isEmpty()) {
                    enDescription = descriptionEn;
                }
                if (!descriptionDe.isEmpty()) {
                    deDescription = descriptionDe;
                }
            }

            assetDescriptionValue.put("EN", enDescription);
            assetDescriptionValue.put("DE", deDescription);

            log.info("assetDescriptionValue: {}", assetDescriptionValue);
            assetDescriptionOperation.set(VALUE_FIELD, assetDescriptionValue);
            log.info("assetDescriptionOperation: {}", assetDescriptionOperation);
            updateOperations.add(assetDescriptionOperation);

            // license operation
            final ObjectNode licenseOperation = mapper.createObjectNode();
            licenseOperation.put(TYPE_FIELD, "replace_int");
            licenseOperation.put(PROPERTY_NAME_FIELD, "license");
            licenseOperation.put(VALUE_FIELD, 131);

            updateOperations.add(licenseOperation);

            // stock ID operation
            final ObjectNode multilangOperation = mapper.createObjectNode();
            multilangOperation.put(TYPE_FIELD, "replace_multilang");
            multilangOperation.put(PROPERTY_NAME_FIELD, "customAttribute_276");

            final ObjectNode valueObject = mapper.createObjectNode();
            valueObject.put("DE", audio.getAudio().getId());
            valueObject.put("EN", audio.getAudio().getId());

            multilangOperation.set(VALUE_FIELD, valueObject);

            updateOperations.add(multilangOperation);



            assetMetadataTO.setUpdateOperations(updateOperations);
            return assetMetadataTO;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}