package io.codelirium.examples.upload.service;

import io.codelirium.examples.upload.client.RestfulHTTPClientHolder;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileForwardingServiceImpl implements FileForwardingService, RestfulHTTPClientHolder<RestTemplate> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileForwardingServiceImpl.class);

	private RestTemplate restClient;


	@Inject
	public FileForwardingServiceImpl(final RestTemplate restClient) {
		this.restClient = restClient;
	}


	public ResponseEntity<String> forwardFileDirectly(final String absoluteUrl, final MultipartFile file) throws Throwable {

		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();

		ByteArrayResource contentsAsResource = new ByteArrayResource(file.getBytes()) {
			@Override
			public String getFilename() {
				return file.getOriginalFilename();
			}
		};

		parts.add("file", contentsAsResource);

		LOGGER.debug("Forwarding file without persisting it...");

		return restClient.exchange(absoluteUrl, HttpMethod.POST, new HttpEntity<>(parts), new ParameterizedTypeReference<String>() {});
	}

	@Override
	public RestTemplate getRestfulHTTPClient() {
		return restClient;
	}
}
