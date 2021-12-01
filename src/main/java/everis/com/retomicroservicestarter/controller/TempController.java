package everis.com.retomicroservicestarter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import everis.com.startereto.Temp;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
public class TempController {

	private final static Logger logger= LoggerFactory.getLogger(TempController.class);
	private Counter counterD;
	private Counter counterConverter;
	
	@Autowired
	private Temp temp;
	
	@Value("${some.valueCels}")
	private Integer c;
	@Value("${some.valueF}")
	private Integer f;
	
	public TempController(MeterRegistry mRegistry) {
		
		this.counterD = Counter.builder("degrees.invokations").description("Total degrees invokations").register(mRegistry);
		this.counterConverter = Counter.builder("converter.invokations").description("Total converter invokations").register(mRegistry);
	}
	
	@GetMapping("/")
	public Integer index(){
		counterD.increment();
		logger.info("Llamada al endpoint de grados");
		Integer result = 0;
		if (temp.getTemp().equals("Fº")) {
			result = f;
		}else {
			result = c;
		}
		return result;
		
	}
	
	@GetMapping("/convertCToF/{tempC}")
	public Integer cToF(@PathVariable Integer tempC) {
		counterConverter.increment();
		logger.info("Llamada al endpoint de conversion de celsius a fahrenheit");
		return temp.conversor(tempC);
	}
}

