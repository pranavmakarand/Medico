//package com.edu.elasticSearch.twitter;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
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
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class ElasticClient {
//
//	private static final String INDEX_NAME = "plan-index";
//
//	static RestHighLevelClient restClient = new RestHighLevelClient(
//			RestClient.builder(new HttpHost("localhost", 9200, "http")));
//
//	private static LinkedHashMap<String, Map<String, Object>> MapOfDocuments = new LinkedHashMap<String, Map<String, Object>>();
//
//	public static void main(String args[]) {
//
//		IndexRequest request = new IndexRequest("posts"); 
//		request.id("1"); 
//		
//		XContentBuilder builder;
//		
//		try {
//			
//			builder = XContentFactory.jsonBuilder();
//			builder.startObject();
//			{
//			    builder.field("user", "kimchy");
//			    builder.timeField("postDate", new Date());
//			    builder.field("message", "trying out Elasticsearch");
//			}
//			builder.endObject();
//			request.source(builder);
//			request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL); 
//			request.setRefreshPolicy("wait_for");    
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			IndexResponse indexResponse = restClient.index(request, RequestOptions.DEFAULT);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}