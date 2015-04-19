package io.codelirium.examples.facade.controller;

import io.codelirium.examples.facade.service.FacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

@RestController
@RequestMapping(value = UrlMappings.API_BASE_MAPPING)
public class GreetingsController {

	private static final String PROPERTY_NAME_LINE_SEPARATOR = "line.separator";

	private final FacadeService<List<String>> greetingsFacadeService;

	@Inject
	public GreetingsController(final FacadeService<List<String>> greetingsFacadeService) {
		this.greetingsFacadeService = greetingsFacadeService;
	}

	@RequestMapping(value = UrlMappings.API_GET_GREETINGS, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String doHandleGreetings(@RequestParam(required = true) String name) {
		return greetingsFacadeService.delegate(1, name).stream().collect(Collectors.joining(System.getProperty(PROPERTY_NAME_LINE_SEPARATOR)));
	}
}
