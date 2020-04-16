package com.asisge.apirest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.asisge.apirest.config.paths.Paths.AuthPath;
import com.asisge.apirest.model.entity.audit.VerificationToken;
import com.asisge.apirest.service.IEmailSenderService;

@Service
public class EmailSenderServiceImpl implements IEmailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendConfirmationEmail(VerificationToken verify) {		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(verify.getUsuario().getCorreo());
		mailMessage.setSubject("Registro Completado!");
		mailMessage.setFrom("Servicio Asisge <testAsisgeFirebase@gmail.com>");
		
		StringBuilder content = new StringBuilder("Para confirmar tu cuenta, por favor haz click en el siguiente enlace : ");
		content.append("https://test-bd-elenchos.herokuapp.com/api/v1/").append(AuthPath.CONFIRMAR).append("?token=").append(verify.getToken());
		content.append("\r\n Si no has hecho ningún proceso de registro, por favor ignora este correo");
		mailMessage.setText(content.toString());
		sendEmail(mailMessage);
	}
	
	@Override
	public void sendRecoveryPassword(VerificationToken token) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(token.getUsuario().getCorreo());
		mailMessage.setSubject("Petición de Recuperación de Contraseña!");
		mailMessage.setFrom("Servicio Asisge <testAsisgeFirebase@gmail.com>");
		
		StringBuilder content = new StringBuilder("Para recuperar tu contraseña, por favor haz click en el siguiente enlace : ");
		content.append("https://test-bd-elenchos.herokuapp.com/api/v1/").append(AuthPath.RECUPERAR).append("?token=").append(token.getToken());
		content.append("\r\n Si no has hecho ningún proceso de recuperación, por favor ignora este correo");
		mailMessage.setText(content.toString());
		sendEmail(mailMessage);
	}

	@Async
	public void sendEmail(SimpleMailMessage message) {
		mailSender.send(message);
	}

	

}
