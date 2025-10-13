package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetSearchTO {

    @JsonValue
    private final ObjectNode root;

    private AssetSearchTO(ObjectNode root) {
        this.root = root;
    }

    private static final String TEMPLATE =
      "{\n" +
        "  \"searchSchemaId\":\"asset\",\n" +
        "  \"lang\":\"EN\",\n" +
        "  \"output\":{\n" +
        "    \"items\":{\n" +
        "      \"fields\":[\n" +
        "        \"id\",\"title\",\"title_multi\",\"actualVersionNumber\",\"currentVersion\",\"validFrom\",\"validTo\"," +
        "        \"keywords\",\"keywords_multi\",\"affiliateNumber\",\"owner\",\"description\",\"description_multi\"," +
        "        \"division\",\"editState\",\"masterAsset\",\"lastUpdatedTime\",\"popularity\",\"suitableForW2P\"," +
        "        \"hiRes\",\"isins\",\"relationsCount\",\"downloadApprovalData\",\"themes\",\"watchers\",\"variants\"," +
        "        \"variantsCount\",\"alternativeImage\",\"hideIfNotValid\",\"versionCount\",\"platform\",\"license\"," +
        "        \"uploadDate\",\"programVersion\",\"versions\",\"channelPublications\",\"persons\",\"relations\"," +
        "        \"language\",\"countries\",\"structuredKeywords\",\"uploadApprovalData\",\"itemNumber\",\"itemDescription\"," +
        "        \"itemDescription_multi\",\"vdb\",\"w2pClassification\",\"assetType\",\"freeField11\",\"freeField20\"," +
        "        \"freeField19\",\"freeField18\",\"freeField16\",\"freeField14\",\"freeField1\",\"freeField10\",\"freeField6\"," +
        "        \"freeField2\",\"freeField3\",\"freeField4\",\"freeField5\",\"freeField7\",\"freeField8\",\"freeField9\"," +
        "        \"freeField12\",\"freeField13\",\"freeField15\",\"freeField17\",\"customAttribute_26\",\"customAttribute_27\"," +
        "        \"customAttribute_176\",\"customAttribute_177\",\"customAttribute_276\",\"customAttribute_326\"," +
        "        \"customAttribute_426\",\"customAttribute_427\",\"customAttribute_428\",\"customAttribute_430\"," +
        "        \"customAttribute_431\",\"customAttribute_432\",\"customAttribute_433\",\"customAttribute_434\"," +
        "        \"customAttribute_435\",\"customAttribute_437\",\"customAttribute_439\",\"customAttribute_440\"," +
        "        \"customAttribute_441\",\"customAttribute_442\",\"customAttribute_443\",\"customAttribute_444\"," +
        "        \"customAttribute_445\",\"customAttribute_446\",\"customAttribute_447\",\"customAttribute_448\"," +
        "        \"customAttribute_449\",\"customAttribute_450\",\"customAttribute_451\",\"customAttribute_452\"," +
        "        \"customAttribute_453\",\"customAttribute_454\",\"customAttribute_455\",\"customAttribute_456\"," +
        "        \"customAttribute_457\",\"customAttribute_458\",\"customAttribute_459\",\"customAttribute_460\"," +
        "        \"customAttribute_461\",\"customAttribute_462\",\"customAttribute_463\",\"customAttribute_464\"," +
        "        \"customAttribute_465\",\"customAttribute_466\",\"customAttribute_467\",\"customAttribute_468\"," +
        "        \"customAttribute_469\",\"customAttribute_470\",\"customAttribute_471\",\"customAttribute_472\"," +
        "        \"customAttribute_473\",\"customAttribute_474\",\"customAttribute_475\"\n" +
        "      ]\n" +
        "    },\n" +
        "    \"paging\":{\"@type\":\"offset\",\"offset\":0,\"limit\":25},\n" +
        "    \"sorting\":[\n" +
        "      {\"@type\":\"field\",\"field\":\"uploadDate\",\"asc\":false},\n" +
        "      {\"@type\":\"relevance\",\"asc\":false}\n" +
        "    ]\n" +
        "  },\n" +
        "  \"criteria\":{\n" +
        "    \"@type\":\"and\",\n" +
        "    \"subs\":[\n" +
        "      {\"@type\":\"or\",\"subs\":[\n" +
        "        {\"@type\":\"match\",\"fields\":[\n" +
        "          \"description_multi\",\"itemDescription_multi\",\"originalName\",\"assetType.name\",\"customAttribute_26\"," +
        "          \"customAttribute_276\",\"customAttribute_427.value\",\"customAttribute_430\",\"customAttribute_433.value\"," +
        "          \"customAttribute_434\",\"customAttribute_435\",\"customAttribute_437\",\"ff_20_multi\",\"ff_19_multi\"," +
        "          \"ff_18_multi\",\"ff_16_multi\",\"ff_14_multi\",\"ff_1_multi\",\"ff_10_multi\",\"ff_6_multi\",\"ff_4_multi\"," +
        "          \"ff_5_multi\",\"ff_7_multi\",\"ff_8_multi\",\"ff_12_multi\",\"ff_13_multi\",\"colorSpace.type\"," +
        "          \"countries.id\",\"countries.name_multi\",\"documentDimensions.unit\",\"itemNumber\",\"keywords_multi\"," +
        "          \"persons\",\"extension\",\"structuredKeywords.name_multi\",\"affiliateNumber\",\"id\",\"licenseHolderName\"," +
        "          \"licenseData.license\",\"channelPublications.channelId\",\"division.name\",\"owner.name\",\"themes.name_multi\"," +
        "          \"themes.pathIds\",\"title_multi\",\"vdb.name_multi\",\"w2pClassification\"],\"value\":\"%s\"},\n" +
        "        {\"@type\":\"exact_match\",\"fields\":[\"containedText\",\"exifIptcXmpData\"],\"value\":\"%s\"}\n" +
        "      ]},\n" +
        "      {\"@type\":\"in\",\"fields\":[\"extension\"],\"text_value\":[\n" +
        "        \"ai\",\"bmp\",\"eps\",\"gif\",\"ico\",\"jpg/jpeg\",\"png\",\"psb\",\"psd\",\"svg\",\"tif/tiff\",\"wmf\"]},\n" +
        "      {\"@type\":\"and\",\"subs\":[\n" +
        "        {\"@type\":false,\"fields\":[\"isVariant\"]},\n" +
        "        {\"@type\":\"not\",\"criteria\":{\"@type\":\"eq\",\"fields\":[\"vdb.id\"],\"long_value\":3}},\n" +
        "        {\"@type\":\"and\",\"subs\":[\n" +
        "          {\"@type\":\"eq\",\"fields\":[\"owner.id\"],\"long_value\":95714},\n" +
        "          {\"@type\":\"in\",\"fields\":[\"vdb.id\"],\"long_value\":[1903],\"any\":true}\n" +
        "        ]}\n" +
        "      ]}\n" +
        "    ]\n" +
        "  }\n" +
        "}";

    public static AssetSearchTO createPayloadWithShutterstockImageId(String imageId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = String.format(TEMPLATE, imageId, imageId);
            ObjectNode node = (ObjectNode) mapper.readTree(json);
            return new AssetSearchTO(node);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build AssetSearchTO payload", e);
        }
    }
}
