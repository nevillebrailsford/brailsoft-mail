package com.brailsoft.mail;

import com.brailsoft.base.NotificationType;

public enum MailNotificationType implements NotificationType {
	Sent("Sent"), Failed("Failed");

	private String type;

	MailNotificationType(String type) {
		this.type = type;
	}

	public String type() {
		return type;
	}

	@Override
	public String category() {
		return "mail";
	}
}
