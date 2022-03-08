package com.brailsoft.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Base64;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.ApplicationDecsriptor;

class EmailSenderTest {

	@TempDir
	File rootDirectory;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		ApplicationDecsriptor app = new ApplicationDecsriptor("test");
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationConfiguration.clear();
	}

	@Test
	void testEncodeDecode() {
		byte[] encodedBytes = Base64.getEncoder().encode("Test@".getBytes());
		byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
		assertEquals("Test@", new String(decodedBytes));
	}

}
