package com.study.es_hrset_client;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * 
 * @Description: 批量索引文档，即批量往索引里面放入文档数据.类似于数据库里面批量向表里面插入多行数据，一行数据就是一个文档
 * @author lgs
 * @date 2018年6月23日
 *
 */
public class BulkDemo {
	
	private static Logger logger = LogManager.getRootLogger();  

	public static void main(String[] args) {
		try (RestHighLevelClient client = InitDemo.getClient();) {
			
			// 1、创建批量操作请求
			BulkRequest request = new BulkRequest(); 
			request.add(new IndexRequest("mess", "_doc", "1")  
			        .source(XContentType.JSON,"field", "foo"));
			request.add(new IndexRequest("mess", "_doc", "2")  
			        .source(XContentType.JSON,"field", "bar"));
			request.add(new IndexRequest("mess", "_doc", "3")  
			        .source(XContentType.JSON,"field", "baz"));
			
			/*
			request.add(new DeleteRequest("mess", "_doc", "3")); 
			request.add(new UpdateRequest("mess", "_doc", "2") 
			        .doc(XContentType.JSON,"other", "test"));
			request.add(new IndexRequest("mess", "_doc", "4")  
			        .source(XContentType.JSON,"field", "baz"));
			*/
			
			// 2、可选的设置
			/*
			request.timeout("2m");
			request.setRefreshPolicy("wait_for");  
			request.waitForActiveShards(2);
			*/
			
			
			//3、发送请求		
		
			// 同步请求
			BulkResponse bulkResponse = client.bulk(request);
			
			
			//4、处理响应
			if(bulkResponse != null) {
				for (BulkItemResponse bulkItemResponse : bulkResponse) { 
				    DocWriteResponse itemResponse = bulkItemResponse.getResponse(); 

				    if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
				            || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) { 
				        IndexResponse indexResponse = (IndexResponse) itemResponse;
				        //TODO 新增成功的处理

				    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) { 
				        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
				       //TODO 修改成功的处理

				    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) { 
				        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
				        //TODO 删除成功的处理
				    }
				}
			}
			
			
			//异步方式发送批量操作请求
			/*
			ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
			    @Override
			    public void onResponse(BulkResponse bulkResponse) {
			        
			    }
			
			    @Override
			    public void onFailure(Exception e) {
			        
			    }
			};
			client.bulkAsync(request, listener);
			*/
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
