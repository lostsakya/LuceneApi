package com.diudiustudio.LuceneApi;

import org.apache.lucene.util.Version;

/**
 * Hello world!
 *
 */
public class App {
	public int createIndex() {
		String indexPath = "index";
		String dataPath = "data";
		Version version = Version.LUCENE_4_9;
		return Util.getIndex(indexPath, dataPath, version);
	}
}
