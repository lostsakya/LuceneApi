package com.diudiustudio.LuceneApi;

import java.io.IOException;

import org.apache.lucene.util.Version;

/**
 * Hello world!
 *
 */
public class App {
	public static final String	indexPath	= "index";
	public static final String	dataPath	= "data";
	public static final Version	version		= Version.LUCENE_4_9;

	public int createIndex() {
		return Util.getIndex(indexPath, dataPath, version);
	}

	public void search(String field, String key) throws IOException {
		QuaryUtil.search(indexPath, field, key);
	}
}
