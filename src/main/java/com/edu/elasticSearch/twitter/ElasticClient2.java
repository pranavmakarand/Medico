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
//public class ElasticClient2 {
//
//	private static final String INDEX_NAME = "plan-index";
//
//	static RestHighLevelClient restClient = new RestHighLevelClient(
//			RestClient.builder(new HttpHost("localhost", 9200, "http")));
//
//	private static LinkedHashMap<String, Map<String, Object>> MapOfDocuments = new LinkedHashMap<String, Map<String, Object>>();
//
//	public static void main(String args[]) throws IOException, ParseException {
//
//		CreateIndexRequest request = new CreateIndexRequest("twitter");
//
//		request.settings(Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 1));
//
//		XContentBuilder builder = XContentFactory.jsonBuilder();
//
//		builder.startObject();
//		{
//		    builder.startObject("properties");
//		    {
//		        builder.startObject("message");
//		        {
//		            builder.field("type", "text");
//		        }
//		        builder.endObject();
//		    }
//		    builder.endObject();
//		}
//
//		builder.endObject();
//
//		request.mapping(builder);
//
//		CreateIndexResponse createIndexResponse = restClient.indices().create(request, RequestOptions.DEFAULT);
//
//		System.out.println(createIndexResponse.isAcknowledged());
//
//		IndexRequest indexRequest = new IndexRequest("twitter");
//
//		indexRequest.id("1");
//
//		XContentBuilder builder1 = XContentFactory.jsonBuilder();
//		
//		builder1.startObject("message");
//		{
//		    builder1.value("hello");
//		    // You can add more fields as needed
//		}
//		builder1.endObject();
//
//		indexRequest.source(builder1, XContentType.JSON);
//
//		IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);
//	}
//}