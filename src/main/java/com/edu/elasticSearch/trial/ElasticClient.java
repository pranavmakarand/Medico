//package com.edu.elasticSearch.trial;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.HttpHost;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.support.WriteRequest;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.RestHighLevelClientBuilder;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.elasticsearch.client.indices.CreateIndexResponse;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.xcontent.XContentBuilder;
//import org.elasticsearch.xcontent.XContentFactory;
//import org.elasticsearch.xcontent.XContentType;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class ElasticClient {
//
//	private static final String INDEX_NAME = "plan-index";
//
//	static RestHighLevelClient restClient = new RestHighLevelClient(
//		RestClient.builder(new HttpHost("localhost", 9200, "http")));
//
//	private static LinkedHashMap<String, Map<String, Object>> MapOfDocuments = new LinkedHashMap<String, Map<String, Object>>();
//
//	public static void main(String args[]) {
//
//		String filePath = "/Users/pranavdhongade/Documents/AdvancedBigDataIndex/elasticSearchDemo/src/main/resources/input.json";
//		JSONParser parser = new JSONParser();
//		Object obj;
//
//		ObjectMapper objectMapper = new ObjectMapper();
//
//		String jsonString;
//
//		try {
//			obj = parser.parse(new FileReader(filePath));
//			jsonString = objectMapper.writeValueAsString(obj);
//			JSONObject plan = new JSONObject(jsonString);
//			MapOfDocuments = new LinkedHashMap<String, Map<String, Object>>();
//
//			convertMapToDocumentIndex(plan, "", "plan");
//			
//		} catch (IOException | ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		// we need to create an index using the rest client.
//		CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
//
//		request.settings(Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 1));
//
//		XContentBuilder mapping;
//		try {
//			mapping = getMapping();
//			request.mapping(mapping);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		CreateIndexResponse createIndexResponse;
//		
//		try {
//			createIndexResponse = restClient.indices().create(request, RequestOptions.DEFAULT);
//			System.out.println(createIndexResponse.isAcknowledged());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		for (Map.Entry<String, Map<String, Object>> entry : MapOfDocuments.entrySet()) {
//			String parentId = entry.getKey().split(":")[0];
//			String objectId = entry.getKey().split(":")[1];
//			IndexRequest index_request = new IndexRequest(INDEX_NAME);
//			index_request.id(objectId);
//			index_request.source(entry.getValue(), XContentType.JSON);
//			index_request.routing(parentId);
//			index_request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
//			IndexResponse indexResponse;
//			try {
//				indexResponse = restClient.index(index_request, RequestOptions.DEFAULT);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} finally {
////				System.out.println("response id: " + indexResponse.getId() + " parent id: " + parentId);
//			}
//		}
//	}
//
//	private static Map<String, Map<String, Object>> convertMapToDocumentIndex(JSONObject jsonObject, String parentId,
//			String objectName) {
//
//		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
//		Map<String, Object> valueMap = new HashMap<String, Object>();
//		Iterator<String> iterator = jsonObject.keys();
//
//		while (iterator.hasNext()) {
//			String key = iterator.next();
//			String elasticKey = jsonObject.get("objectType") + ":" + parentId;
//			Object value = jsonObject.get(key);
//
//			if (value instanceof JSONObject) {
//
//				convertMapToDocumentIndex((JSONObject) value, jsonObject.get("objectId").toString(), key);
//
//			} else if (value instanceof JSONArray) {
//
//				convertToList((JSONArray) value, jsonObject.get("objectId").toString(), key);
//
//			} else {
//				valueMap.put(key, value);
//				map.put(elasticKey, valueMap);
//			}
//		}
//
//		Map<String, Object> temp = new HashMap<String, Object>();
//
//		if (objectName == "plan") {
//			valueMap.put("plan_join", objectName);
//		} else {
//			temp.put("name", objectName);
//			temp.put("parent", parentId);
//			valueMap.put("plan_join", temp);
//		}
//
//		String id = parentId + ":" + jsonObject.get("objectId").toString();
//		System.out.println(valueMap);
//		MapOfDocuments.put(id, valueMap);
//
//		return map;
//	}
//
//	private static List<Object> convertToList(JSONArray array, String parentId, String objectName) {
//		List<Object> list = new ArrayList<Object>();
//		for (int i = 0; i < array.length(); i++) {
//			Object value = array.get(i);
//			if (value instanceof JSONArray) {
//				value = convertToList((JSONArray) value, parentId, objectName);
//			} else if (value instanceof JSONObject) {
//				value = convertMapToDocumentIndex((JSONObject) value, parentId, objectName);
//			}
//			list.add(value);
//		}
//		return list;
//	}
//
//	private static XContentBuilder getMapping() throws IOException {
//
//		XContentBuilder builder = XContentFactory.jsonBuilder();
//		builder.startObject();
//		{
//			builder.startObject("properties");
//			{
//				builder.startObject("plan");
//				{
//					builder.startObject("properties");
//					{
//						builder.startObject("_org");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//						builder.startObject("objectId");
//						{
//							builder.field("type", "keyword");
//						}
//						builder.endObject();
//						builder.startObject("objectType");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//						builder.startObject("planType");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//						builder.startObject("creationDate");
//						{
//							builder.field("type", "date");
//							builder.field("format", "MM-dd-yyyy");
//						}
//						builder.endObject();
//					}
//					builder.endObject();
//				}
//				builder.endObject();
//				builder.startObject("planCostShares");
//				{
//					builder.startObject("properties");
//					{
//						builder.startObject("copay");
//						{
//							builder.field("type", "long");
//						}
//						builder.endObject();
//						builder.startObject("deductible");
//						{
//							builder.field("type", "long");
//						}
//						builder.endObject();
//						builder.startObject("_org");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//						builder.startObject("objectId");
//						{
//							builder.field("type", "keyword");
//						}
//						builder.endObject();
//						builder.startObject("objectType");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//					}
//					builder.endObject();
//				}
//				builder.endObject();
//				builder.startObject("linkedPlanServices");
//				{
//					// builder.field("type","nested");
//					builder.startObject("properties");
//					{
//						builder.startObject("_org");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//						builder.startObject("objectId");
//						{
//							builder.field("type", "keyword");
//						}
//						builder.endObject();
//						builder.startObject("objectType");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//					}
//					builder.endObject();
//				}
//				builder.endObject();
//				builder.startObject("linkedService");
//				{
//					builder.startObject("properties");
//					{
//						builder.startObject("_org");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//						builder.startObject("name");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//						builder.startObject("objectId");
//						{
//							builder.field("type", "keyword");
//						}
//						builder.endObject();
//						builder.startObject("objectType");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//					}
//					builder.endObject();
//				}
//				builder.endObject();
//				builder.startObject("planserviceCostShares");
//				{
//					builder.startObject("properties");
//					{
//						builder.startObject("copay");
//						{
//							builder.field("type", "long");
//						}
//						builder.endObject();
//						builder.startObject("deductible");
//						{
//							builder.field("type", "long");
//						}
//						builder.endObject();
//						builder.startObject("_org");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//						builder.startObject("objectId");
//						{
//							builder.field("type", "keyword");
//						}
//						builder.endObject();
//						builder.startObject("objectType");
//						{
//							builder.field("type", "text");
//						}
//						builder.endObject();
//					}
//					builder.endObject();
//				}
//				builder.endObject();
//				builder.startObject("plan_join");
//				{
//					builder.field("type", "join");
//					builder.field("eager_global_ordinals", "true");
//					builder.startObject("relations");
//					{
//						builder.array("plan", "planCostShares", "linkedPlanServices");
//						builder.array("linkedPlanServices", "linkedService", "planserviceCostShares");
//					}
//					builder.endObject();
//				}
//				builder.endObject();
//			}
//			builder.endObject();
//		}
//		builder.endObject();
//
//		return builder;
//	}
//}
