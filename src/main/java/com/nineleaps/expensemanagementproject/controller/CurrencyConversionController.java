//package com.nineleaps.expensemanagementproject.controller;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import com.nineleaps.expensemanagementproject.entity.CurrencyConversion;
//
//@RestController
//public class CurrencyConversionController {
//	
//	@GetMapping("currency-conversion/{from}/{to}/{quantity}")
////	public BigDecimal getConversionDetails(@PathVariable("from") String from, @PathVariable("to") String to,@PathVariable("quantity") BigDecimal quantity) {
//    public CurrencyConversion getConversionDetails(@PathVariable("from") String from, @PathVariable("to") String to,@PathVariable("quantity") BigDecimal quantity) {
//		
//		Map<String, String> uriVariables = new HashMap<>();
//		uriVariables.put("from", from);
//		uriVariables.put("to", to);
//		
//		ResponseEntity<CurrencyConversion> responseEntity = null;
//		
//		try {
//			responseEntity = new RestTemplate().getForEntity("http://localhost:8080/currency-conversion/{from}/INR", CurrencyConversion.class, uriVariables);
//		} catch (RestClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		CurrencyConversion response = responseEntity.getBody(); 
//		
//		 return new CurrencyConversion(response.getId(), from , to, quantity,response.getExchangeRate(), quantity.multiply(response.getExchangeRate()));
////		return quantity.multiply(response.getExchangeRate());
//	} 
//
//}
