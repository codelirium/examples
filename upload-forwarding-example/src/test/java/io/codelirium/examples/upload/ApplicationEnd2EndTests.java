package io.codelirium.examples.upload;

import io.codelirium.examples.upload.controller.UploadController;
import io.codelirium.examples.upload.controller.UploadController.ReSTResponse;
import io.codelirium.examples.upload.controller.UrlMappings;
import io.codelirium.examples.upload.service.FileForwardingService;
import io.codelirium.examples.upload.service.FileForwardingServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationEnd2EndTests {

	private static final String baseUrl = "http://localhost:31337";

	private RestTemplate restClient;
	private FileForwardingService fileForwardingService;
	private UploadController uploadController;

	private MockRestServiceServer mockServer;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		restClient = new RestTemplate();
		fileForwardingService = new FileForwardingServiceImpl(restClient);
		uploadController = new UploadController(baseUrl, fileForwardingService);

		mockMvc = MockMvcBuilders.standaloneSetup(uploadController).build();
		mockServer = MockRestServiceServer.createServer(restClient);
	}

	@Test
	public void testFileUploadWithInMemoryForwarding() throws Throwable {

		mockServer.expect(requestTo(baseUrl + UrlMappings.API_BASE_MAPPING + UrlMappings.API_FORWARD_FILE_RESOURCE))
			.andExpect(method(HttpMethod.POST))
			.andRespond(withSuccess(ReSTResponse.OK.name(), MediaType.TEXT_PLAIN));

		mockMvc.perform(fileUpload(UrlMappings.API_BASE_MAPPING + UrlMappings.API_UPLOAD_FILE_RESOURCE)
						.file(getMultipartFile())
						.contentType(MediaType.MULTIPART_FORM_DATA))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.TEXT_PLAIN))
		.andExpect(content().string(containsString("OK")));
	}

	private MockMultipartFile getMultipartFile() {
		return new MockMultipartFile("file", "foo.txt", MediaType.TEXT_PLAIN_VALUE, "foo".getBytes());
	}
}
