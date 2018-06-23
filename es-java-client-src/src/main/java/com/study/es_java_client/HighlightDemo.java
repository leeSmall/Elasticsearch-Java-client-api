package com.study.es_java_client;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

public class HighlightDemo {
	
	private static Logger logger = LogManager.getRootLogger();  

	public static void main(String[] args) {
		try (TransportClient client = InitDemo.getClient();) {
			
			// 1、创建search请求
			SearchRequest searchRequest = new SearchRequest("hl_test"); 
			
			// 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
			
			//构造QueryBuilder
			QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "lucene solr");
			sourceBuilder.query(matchQueryBuilder);
			
			//分页设置
			/*sourceBuilder.from(0); 
			sourceBuilder.size(5); ;*/ 
			
					
			// 高亮设置
			HighlightBuilder highlightBuilder = new HighlightBuilder(); 
			highlightBuilder.requireFieldMatch(false).field("title").field("content")
				.preTags("<strong>").postTags("</strong>");
			//不同字段可有不同设置，如不同标签
			/*HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title"); 
			highlightTitle.preTags("<strong>").postTags("</strong>");
			highlightBuilder.field(highlightTitle);  
			HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
			highlightContent.preTags("<b>").postTags("</b>");
			highlightBuilder.field(highlightContent).requireFieldMatch(false);*/
			
			sourceBuilder.highlighter(highlightBuilder);
			
			searchRequest.source(sourceBuilder);
			
			//3、发送请求		
			SearchResponse searchResponse = client.search(searchRequest).get();
			
			
			//4、处理响应
			if(RestStatus.OK.equals(searchResponse.status())) {
				//处理搜索命中文档结果
				SearchHits hits = searchResponse.getHits();
				long totalHits = hits.getTotalHits();
				
				SearchHit[] searchHits = hits.getHits();
				for (SearchHit hit : searchHits) {		
					String index = hit.getIndex();
					String type = hit.getType();
					String id = hit.getId();
					float score = hit.getScore();
					
					//取_source字段值
					//String sourceAsString = hit.getSourceAsString(); //取成json串
					Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
					//从map中取字段值
					/*String title = (String) sourceAsMap.get("title"); 
					String content  = (String) sourceAsMap.get("content"); */
					logger.info("index:" + index + "  type:" + type + "  id:" + id);
				    logger.info("sourceMap : " +  sourceAsMap);
					//取高亮结果
					Map<String, HighlightField> highlightFields = hit.getHighlightFields();
				    HighlightField highlight = highlightFields.get("title"); 
				    if(highlight != null) {
					    Text[] fragments = highlight.fragments();  //多值的字段会有多个值
					    if(fragments != null) {
					    	String fragmentString = fragments[0].string();
					    	logger.info("title highlight : " +  fragmentString);
					    	//可用高亮字符串替换上面sourceAsMap中的对应字段返回到上一级调用
					    	//sourceAsMap.put("title", fragmentString);
					    }
				    }
				    
				    highlight = highlightFields.get("content"); 
				    if(highlight != null) {
					    Text[] fragments = highlight.fragments();  //多值的字段会有多个值
					    if(fragments != null) {
					    	String fragmentString = fragments[0].string();
					    	logger.info("content highlight : " +  fragmentString);
					    	//可用高亮字符串替换上面sourceAsMap中的对应字段返回到上一级调用
					    	//sourceAsMap.put("content", fragmentString);
					    }
				    }
				}
			}
			
		} catch (IOException | InterruptedException | ExecutionException e) {
			logger.error(e);
		}
	}
}
