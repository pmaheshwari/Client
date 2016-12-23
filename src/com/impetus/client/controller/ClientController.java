package com.impetus.client.controller;

import static com.impetus.client.common.Constants.EMAIL;
import static com.impetus.client.common.Constants.FROM;
import static com.impetus.client.common.Constants.TO;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.impetus.client.dto.EmailInfo;
import com.impetus.client.dto.MetroDetail;

@Controller
public class ClientController {

	@Autowired
	protected RestTemplate restTemplate;

	private Logger logger = LoggerFactory.getLogger(ClientController.class);

	@RequestMapping("/")
	public String getIndex1(Map<String, Object> model) {
		return "index";
	}

	@RequestMapping("/index")
	public String getIndex() {
		return "index";
	}

	@RequestMapping("/dmrcPage")
	public String getDmrcPage(Map<String, Object> model) {
		logger.info("Returning measurement view to render on UI");
		return "dmrcPage";
	}

	

	@RequestMapping(value = "/dmrcFares", produces = "application/json")
	public @ResponseBody MetroDetail metroDetail(
						@ModelAttribute("source") String source, @ModelAttribute("destination") String destination) {
		Map<String, String> parametersMap = new HashMap<String, String>();
		parametersMap.put(FROM, source);
		parametersMap.put(TO, destination);
		
		logger.info("Call service registered on Eureka server");
		return restTemplate
				.getForObject(
						"http://FARE-CALCULATION-INSTANCE/calcFare/{source}/{destination}",
						MetroDetail.class, parametersMap);

	}

	@RequestMapping(value = "/dmrcFaresEmail", produces = "application/json")
	public @ResponseBody void metroDetailOnEmail(
						@ModelAttribute("source") String source, @ModelAttribute("destination") String destination,
						 @ModelAttribute("emailId") String emailId) {
		Map<String, String> parametersMap = new HashMap<String, String>();
		parametersMap.put(FROM, source);
		parametersMap.put(TO, destination);
		parametersMap.put(EMAIL, emailId);
		logger.info("Call service registered on Eureka server");
		 restTemplate
				.getForObject(
						"http://SEND-MAIL-INSTANCE/mail/{source}/{destination}/{emailId}",
						EmailInfo.class, parametersMap);

	}
	
}
