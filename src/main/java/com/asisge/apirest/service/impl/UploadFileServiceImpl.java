package com.asisge.apirest.service.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.asisge.apirest.service.IUploadFileService;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private StorageClient storageClient;

	private void setStorageClient() {
		this.storageClient = getStorageClient();
	}

	@Override
	public StorageClient getStorageClient() {
		return StorageClient.getInstance();
	}

	@Override
	public String crearArchivoProyecto(MultipartFile archivo, Long idProyecto) throws IOException {
		setStorageClient();
		String nombreArchivo = UUID.randomUUID().toString().substring(0, 5) + "_" + archivo.getOriginalFilename().replace(" ", "");
		String filePath = "proyectos/" + idProyecto.toString();
		String blob = filePath + "/" + nombreArchivo;

		Bucket bucket = storageClient.bucket();
		Blob cloudBlob = bucket.create(blob, archivo.getInputStream());
		cloudBlob.createAcl(Acl.of(User.ofAllUsers(), Acl.Role.READER));
		return cloudBlob.getMediaLink();
	}

	@Override
	public String cargarContratoOAnticipo(MultipartFile archivo, Long idProyecto) throws IOException {
		setStorageClient();
		String nombreArchivo = archivo.getOriginalFilename().replace(" ", "");
		String filePath = "proyectos/" + idProyecto.toString();
		String blob = filePath + "/" + nombreArchivo;

		Bucket bucket = storageClient.bucket();
		Blob cloudBlob = bucket.create(blob, archivo.getInputStream());
		cloudBlob.createAcl(Acl.of(User.ofAllUsers(), Acl.Role.READER));
		return cloudBlob.getMediaLink();
	}

}
