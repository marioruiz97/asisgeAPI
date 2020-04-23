package com.asisge.apirest.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asisge.apirest.config.paths.Paths.AuthPath;
import com.asisge.apirest.model.entity.audit.VerificationToken;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.repository.IVerificationTokenDao;
import com.asisge.apirest.service.IEmailSenderService;
import com.asisge.apirest.service.IUsuarioService;

@Controller
public class WebViewsController {
	
	private static final String ADMIN_VIEW = "contacte-administrador";

	@Autowired
	private IUsuarioService service;
	
	@Autowired
	private IEmailSenderService emailService;
	
	@Autowired
	private IVerificationTokenDao tokenDao;

	@GetMapping(AuthPath.CONFIRMAR)
	public String confirmAccount(@RequestParam("token") String token) {
		VerificationToken verificationToken = service.getVerificationToken(token);
		if (verificationToken != null) {
			Usuario user = service.findUsuarioById(verificationToken.getUsuario().getIdUsuario());
			user.setVerificado(true);
			service.saveUsuario(user);
		} else {
			return "confirmacion-fail";
		}
		return "confirmacion-success";
	}

	@PostMapping(AuthPath.CONFIRMAR)
	public String tryAgain(@RequestParam Map<String, Object> modelMap, HttpServletRequest request, Model model) {
		try {
			String email = modelMap.get("email").toString();
			Usuario usuario = service.findUsuarioByCorreo(email);
			if(usuario == null) {
				throw new NullPointerException();
			}
			model.addAttribute("correo", email);
			VerificationToken token = service.validVerificationToken(usuario);
			emailService.sendConfirmationEmail(token);
			return "confirmacion-enviada";
		} catch (Exception ex) {
			return ADMIN_VIEW;
		}
	}
	
	@GetMapping(AuthPath.RECUPERAR)
	public String showResetPassword(@RequestParam("token") String token, Model model) {
		VerificationToken verificationToken = service.getVerificationToken(token);
		if(verificationToken == null) {
			return ADMIN_VIEW;
		}
		model.addAttribute("token",token);
		return "recuperar-contrasena";
	}
	
	@PostMapping(AuthPath.RECUPERAR+"/reset")
	public String resetPassword(@RequestParam Map<String, Object> model, HttpServletRequest request) {
		try {
			String token = model.get("token").toString();
			VerificationToken verify= service.getVerificationToken(token);
			if(verify == null) {
				throw new NullPointerException();
			}
			Long idUsuario = verify.getUsuario().getIdUsuario();
			Usuario usuario = service.findUsuarioById(idUsuario);
			usuario.setContrasena(model.get("contrasena").toString());
			service.changeContrasena(usuario);						
			tokenDao.deleteById(verify.getTokenId());
			return "contrasena-cambiada";
		} catch (Exception ex) {
			return ADMIN_VIEW;
		}
	}
}
