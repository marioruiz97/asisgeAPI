package com.asisge.apirest.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.cloud.StorageClient;

public interface IUploadFileService {

	StorageClient getStorageClient();

	String cargarContratoOAnticipo(MultipartFile archivo, Long idProyecto) throws IOException;

	String crearArchivoProyecto(MultipartFile archivo, Long idProyecto) throws IOException;
}
