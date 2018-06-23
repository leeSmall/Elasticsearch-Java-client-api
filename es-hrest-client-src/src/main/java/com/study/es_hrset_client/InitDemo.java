package com.study.es_hrset_client;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * 
 * @Description: 获取Java High Level REST Client客户端
 * @author lgs
 * @date 2018年6月23日
 *
 */
public class InitDemo {

	public static RestHighLevelClient getClient() {

		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")));

		return client;
	}
}
