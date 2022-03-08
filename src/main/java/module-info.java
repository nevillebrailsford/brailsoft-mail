module brailsoft.mail {
	exports com.brailsoft.mail;

	requires brailsoft.base;
	requires java.logging;
	requires transitive java.mail;
	requires java.prefs;

	opens com.brailsoft.mail;
}