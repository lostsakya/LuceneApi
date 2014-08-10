package com.diudiustudio.LuceneApi;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AppTest {
	private App	app;

	@Before
	public void init() {
		app = new App();
	}

	@Test
	public void testCreateIndex() {
		int createIndex = app.createIndex();
		Assert.assertEquals(createIndex, 4);
	}

	@Test
	public void testSearch() throws IOException {
		app.search("content", "hello");
	}
}
