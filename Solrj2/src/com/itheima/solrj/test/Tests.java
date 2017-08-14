/**
 * 
 */
package com.itheima.solrj.test;

import static org.junit.Assert.*;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Molly
 *
 */
public class Tests {
	private HttpSolrServer hSolrServer;
	@Before
	public void init() throws Exception {
		String baseURL = "http://localhost:8080/solr";
		hSolrServer = new HttpSolrServer(baseURL);
	}
	@Test
	public void testAddAndUpdate() throws Exception {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "1003");
		// doc.addField("name", "张三");
		doc.addField("name", "李四");
		hSolrServer.add(doc);
		hSolrServer.commit();

	}

	@Test
	public void testDelete() throws Exception {
		//hSolrServer.deleteById("1003");
		hSolrServer.deleteByQuery("*:*");
		hSolrServer.commit();
	}
	@Test
	public void testQuery() throws Exception {
		SolrParams params=new SolrQuery("*:*");
		QueryResponse response = hSolrServer.query(params);
		SolrDocumentList results = response.getResults();
		System.out.println("搜到的结果总数"+results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println(""+solrDocument.get("id"));
			System.out.println(""+solrDocument.get("name"));
		}
	}
	@Test
	public void testComplexQuery() throws Exception {
		SolrQuery query = new SolrQuery();
		//query.set("q", "钻石");
		query.setQuery("钻石");
		query.set("fq", "product_price:{* TO 5}");
		//query.setFilterQueries("product_price:{* TO 5}");
		query.setSort("product_price ", ORDER.desc);
		query.setStart(0);
		query.setRows(10);
		query.setFields("id","description","product_name","product_price");
		query.set("df", "product_name");
		query.setHighlight(true);
		//添加高亮显示的域
		query.addHighlightField("product_name");
		//设置高亮的前缀
		query.setHighlightSimplePre("<font colr='red'>");
		//设置高亮的后缀
		query.setHighlightSimplePost("</font>");
		QueryResponse response = hSolrServer.query(query);
		SolrDocumentList results = response.getResults();
		System.out.println("搜到的结果总数"+results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println(""+solrDocument.get("id"));
			System.out.println(""+solrDocument.get("product_name"));
			System.out.println(""+solrDocument.get("product_price"));
		}
	}
}
