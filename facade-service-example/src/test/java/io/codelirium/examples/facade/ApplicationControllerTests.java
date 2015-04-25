package io.codelirium.examples.facade;

import io.codelirium.examples.facade.controller.GreetingsController;
import io.codelirium.examples.facade.controller.UrlMappings;
import io.codelirium.examples.facade.service.FacadeService;
import io.codelirium.examples.facade.service.GreetingsFacadeServiceImpl;
import io.codelirium.examples.facade.service.core.EveningGreetingsServiceImpl;
import io.codelirium.examples.facade.service.core.GreetingsService;
import io.codelirium.examples.facade.service.core.MorningGreetingsServiceImpl;
import io.codelirium.examples.facade.service.core.NightGreetingsServiceImpl;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationControllerTests {

	private GreetingsController greetingsController;
	private FacadeService<List<String>> greetingsFacadeService;
	private GreetingsService morningGreetingsService;
	private GreetingsService eveningGreetingsService;
	private GreetingsService nightGreetingsService;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		morningGreetingsService = new MorningGreetingsServiceImpl();
		eveningGreetingsService = new EveningGreetingsServiceImpl();
		nightGreetingsService = new NightGreetingsServiceImpl();

		greetingsFacadeService = new GreetingsFacadeServiceImpl(morningGreetingsService, eveningGreetingsService, nightGreetingsService);
		greetingsController = new GreetingsController(greetingsFacadeService);

		mockMvc = MockMvcBuilders.standaloneSetup(greetingsController).build();
	}

	@Test
	public void testGreetingsControllerGetGreetingsIsOkWithContent() throws Exception {
		mockMvc.perform(get(UrlMappings.API_BASE_MAPPING + UrlMappings.API_GET_GREETINGS)
						.param("name", getGreetingName()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN))
				.andExpect(content().string(containsString(morningGreetingsService.getGreetings(getGreetingName()))))
				.andExpect(content().string(containsString(eveningGreetingsService.getGreetings(getGreetingName()))))
				.andExpect(content().string(containsString(nightGreetingsService.getGreetings(getGreetingName()))));
	}

	private String getGreetingName() {
		return "codelirium";
	}
}
