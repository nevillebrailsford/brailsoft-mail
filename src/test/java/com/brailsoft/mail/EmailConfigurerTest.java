package com.brailsoft.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.logging.Level;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.ApplicationDecsriptor;
import com.brailsoft.base.LogConfigurer;

class EmailConfigurerTest {

	@TempDir
	File rootDirectory;

	private File activeDirectory;
	private File mailFile;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		ApplicationDecsriptor app = new ApplicationDecsriptor("test") {
			@Override
			public Level level() {
				return Level.OFF;
			}
		};
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		LogConfigurer.setUp();
		activeDirectory = ApplicationConfiguration.rootDirectory();
		mailFile = new File(activeDirectory, "mail.properties");
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationConfiguration.clear();
		LogConfigurer.shutdown();
	}

	@Test
	void testGetInstance() {
		assertNotNull(EmailConfigurer.getInstance());
	}

	@Test
	void testGetInstanceNoFile() {
		assertFalse(EmailConfigurer.getInstance().isValidConfiguration());
	}

	@Test
	void testGetInstanceEmptyFile() throws Exception {
		createEmptyMailFile();
		assertTrue(mailFile.exists());
		assertFalse(EmailConfigurer.getInstance().isValidConfiguration());
	}

	@Test
	void testGetInstanceValidFile() throws Exception {
		createValidFile();
		assertTrue(EmailConfigurer.getInstance().isValidConfiguration());
	}

	@Test
	void testGetSession() throws Exception {
		createValidFile();
		assertNotNull(EmailConfigurer.getInstance().getSession());
	}

	@Test
	void testInavlidFileContents() throws Exception {
		createInvalidFile();
		assertFalse(EmailConfigurer.getInstance().isValidConfiguration());
	}

	@Test
	void testGetUserName() throws Exception {
		createValidFile();
		assertNotNull(EmailConfigurer.getInstance().userName());
		assertEquals("testname@neville", EmailConfigurer.getInstance().userName());
	}

	private void createEmptyMailFile() throws Exception {
		if (mailFile.exists()) {
			return;
		}
		if (!activeDirectory.exists()) {
			activeDirectory.mkdirs();
		}
		mailFile.createNewFile();
	}

	private void createValidFile() throws Exception {
		createEmptyMailFile();
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(mailFile)));
		writer.println("username=testname@neville");
		writer.println("password=secret");
		writer.flush();
		writer.close();
	}

	private void createInvalidFile() throws Exception {
		createEmptyMailFile();
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(mailFile)));
		writer.println("username=testname@neville");
		writer.flush();
		writer.close();
	}
}
