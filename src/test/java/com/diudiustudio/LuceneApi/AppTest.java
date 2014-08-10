package com.diudiustudio.LuceneApi;

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
}
