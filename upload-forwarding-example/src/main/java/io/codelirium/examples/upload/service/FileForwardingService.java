package io.codelirium.examples.upload.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileForwardingService {

	ResponseEntity<String> forwardFileDirectly(final String absoluteUrl, final MultipartFile file) throws Throwable;
}
