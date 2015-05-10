package io.codelirium.examples.upload.controller;

import io.codelirium.examples.upload.service.FileForwardingService;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = UrlMappings.API_BASE_MAPPING)
public class UploadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

	@Value("${baseUrl.fileForwarder}")
	private String baseUrl;
	private final FileForwardingService fileForwardingService;

	public enum ReSTResponse { OK, ERROR };


	@Inject
	public UploadController(final FileForwardingService fileForwardingService) {
		this.fileForwardingService = fileForwardingService;
	}

	/**
	 * Only used by the tests.
	 */
	public UploadController(final String baseUrl, final FileForwardingService fileForwardingService) {
		this.baseUrl = baseUrl;
		this.fileForwardingService = fileForwardingService;
	}

	@RequestMapping(value = UrlMappings.API_UPLOAD_FILE_RESOURCE, method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String doHandleFileUploading(@RequestParam(required = true) final MultipartFile file) {

		try {
			LOGGER.debug("Received upload request for file: <{}>", file.getOriginalFilename());

			ResponseEntity<String> response = fileForwardingService.forwardFileDirectly(baseUrl +
															UrlMappings.API_BASE_MAPPING +
															UrlMappings.API_FORWARD_FILE_RESOURCE, file);

			LOGGER.debug("Response: {}", response.toString());

			return response.getBody();
		} catch (Throwable cause) {
			LOGGER.error(cause.getMessage(), cause);

			return ReSTResponse.ERROR.name();
		}
	}

	@RequestMapping(value = UrlMappings.API_FORWARD_FILE_RESOURCE, method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String doHandleFileForwarding(@RequestParam(required = true) final MultipartFile file) {

		LOGGER.debug("Received forwarded file: <{}> with size: <{}> bytes", file.getOriginalFilename(), file.getSize());

		return ReSTResponse.OK.name();
	}
}
