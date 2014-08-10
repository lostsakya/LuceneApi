package com.diudiustudio.LuceneApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
	/**
	 * logger for system
	 */
	static final Logger	logger	= LoggerFactory.getLogger("lucene");

	/**
	 * @param indexPath
	 * @param dataPath
	 * @param version
	 * @return the number of index
	 */
	public static int getIndex(String indexPath, String dataPath, Version version) {
		File dataFile = new File(dataPath);
		Analyzer analyzer = getAnalyzer(version);
		IndexWriter indexWriter = getIndexWriter(indexPath, version, analyzer);
		try {
			indexWriter.deleteAll();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Fail to delete all original index.");
		}
		indexDirectory(indexWriter, dataFile);
		int numIndexed = indexWriter.numDocs();
		try {
			indexWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Fail to close indexWriter.");
		}
		return numIndexed;
	}

	/**
	 * 
	 * @param version
	 * @return an implement of Analyzer
	 */
	private static Analyzer getAnalyzer(Version version) {
		return new StandardAnalyzer(version);
	}

	/**
	 * 
	 * @param indexPath
	 * @param version
	 * @param analyzer
	 * @return indexWriter
	 */
	public static IndexWriter getIndexWriter(String indexPath, Version version, Analyzer analyzer) {
		File indexFile = new File(indexPath);
		Directory indexDirectory = null;
		try {
			indexDirectory = FSDirectory.open(indexFile);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Fail to open indexFile.");
		}
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(version, analyzer);
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Fail to constract IndexWriter.");
		}
		return indexWriter;
	}

	/**
	 * create index for all files in the directory
	 * 
	 * @param writer
	 * @param dirFile
	 */
	public static void indexDirectory(IndexWriter writer, File dirFile) {
		File[] files = dirFile.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				indexDirectory(writer, file);
			} else {
				indexFile(writer, file);
			}
		}
	}

	/**
	 * create index for a file
	 * 
	 * @param writer
	 * @param file
	 */
	public static void indexFile(IndexWriter writer, File file) {
		if (file.isHidden() || !file.exists() || !file.canRead())
			return;
		String canonicalPath = null;
		try {
			canonicalPath = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		logger.info("Indexing: {}", canonicalPath);
		Document doc = getDocument(file, canonicalPath);
		try {
			writer.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Fail to add document to index.");
		}
	}

	/**
	 * 
	 * @param file
	 * @param canonicalPath
	 * @return Document
	 */
	private static Document getDocument(File file, String canonicalPath) {
		Document doc = new Document();
		String content = null;
		content = getFile(file, "UTF-8");
		FieldType fieldType = getFileType();
		doc.add(new Field("content", content, fieldType));
		doc.add(new Field(file.getName(), canonicalPath, fieldType));
		return doc;
	}

	/**
	 * 
	 * @return FieldType
	 */
	private static FieldType getFileType() {
		FieldType fieldType = new FieldType();
		fieldType.setIndexed(true);
		fieldType.setStored(true);
		return fieldType;
	}

	/**
	 * 
	 * @param file
	 * @param charset
	 * @return file content with the charset
	 */
	public static String getFile(File file, String charset) {
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		try {
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream, charset);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("Fail to find file.");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("File({})not support encoding({}).", new String[] {
					file.getName(), charset });
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String line = new String();
		String result = new String();
		try {
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Fail to read file.");
		}
		return result;
	}
}
