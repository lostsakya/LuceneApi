package com.diudiustudio.LuceneApi;

import org.apache.lucene.util.Version;

/**
 * Hello world!
 *
 */
public class App {
	public int createIndex() {
		String indexPath = "/home/sakya/workspace/LuceneApi/index";
		String dataPath = "/home/sakya/workspace/LuceneApi/data";
		Version version = Version.LUCENE_4_9;
		return Util.getIndex(indexPath, dataPath, version);
	}
}
