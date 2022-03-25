package com.gateweb.charge.service;

import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public interface FileStorageService {

    FileOutputStream getDownloadFileOutputStream(String subPath) throws FileNotFoundException;

    Resource loadDownloadFileAsResource(String fileName) throws FileNotFoundException;
}
