# Elasticsearch-Java-client-api
Elasticsearch的java客户端使用示例  

工程说明：  
一. es-hrest-client-src工程：   
Elasticsearch的高级别的REST客户端使用示例 

二、es-java-client-src工程：
Elasticsearch的TransportClient客户端使用示例  
注意：TransPort客户端的使用和RESTful风格的使用基本一致，除了获取客户端不一样，还有发送请求有的不一样外  

三、es-hrest-client-src工程和es-java-client-src工程里面包含有如下功能  
1. Create index 创建索引  
CreateIndexDemo.java  
2. index  document  
索引文档，即往索引里面放入文档数据.类似于数据库里面向表里面插入一行数据，一行数据就是一个文档  
IndexDocumentDemo.java  
3. get  document  
获取文档数据  
GetDocumentDemo.java  
4. Bulk   
批量索引文档，即批量往索引里面放入文档数据.类似于数据库里面批量向表里面插入多行数据，一行数据就是一个文档  
BulkDemo.java  
5. search  
搜索数据  
SearchDemo.java  
6. highlight 高亮  
HighlightDemo.java  
7. suggest 查询建议  
SuggestDemo.java    
8. aggregation 聚合分析  
AggregationDemo.java  

四、博客学习地址  
https://www.cnblogs.com/leeSmall/p/9218779.html
