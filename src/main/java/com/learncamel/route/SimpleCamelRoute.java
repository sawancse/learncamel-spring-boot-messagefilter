package com.learncamel.route;

import com.learncamel.domain.Item;
import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Created by z001qgd on 1/24/18.
 */
@Component
@Slf4j
public class SimpleCamelRoute  extends RouteBuilder{


    @Autowired
    Environment environment;


    @Override
    public void configure() throws Exception {

        log.info("Starting the Camel Route");

        from("file:data/input?delete=true&readLock=none").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				String body = exchange.getIn().getBody().toString();
				exchange.setProperty("foo", "foo");
				exchange.getIn().setHeader("foo", "bar");
				exchange.getIn().setHeader("param1", "param1value");
				log.info(body);
			}
		}).to("direct:a");
        
        
        from("direct:a") 
    	.filter(simple("${header.foo} == 'bar'"))
    		.to("direct:bar")
    	 .end() 
    	 .to("direct:b") ;

        from("direct:bar").log("calling bar");

        from("direct:b").log("calling b");
        
    }
}
