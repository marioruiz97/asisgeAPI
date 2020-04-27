package com.asisge.apirest.service;

import com.asisge.apirest.model.entity.audit.VerificationToken;

public interface IEmailSenderService {

	void sendConfirmationEmail(VerificationToken verificationToken);
	
	void sendRecoveryPassword(VerificationToken verificationToken);
	
	void sendNotification(String to, String subject, String message);
	
	void sendNotifications(String[] to, String subject, String message);

}
