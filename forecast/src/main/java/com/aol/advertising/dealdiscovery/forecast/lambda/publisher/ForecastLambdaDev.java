
package com.aol.advertising.dealdiscovery.forecast.lambda.publisher;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.aol.advertising.dealdiscovery.forecast.application.AppConfig;
import com.aol.advertising.dealdiscovery.forecast.service.PublisherService;
import com.aol.advertising.dealdiscovery.forecast.util.ESQueryUtil;
import io.searchbox.client.JestResult;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * Created by mcordones13 on 10/9/16.
 */
public class ForecastLambdaDev {
    /**
     * Handles a Lambda Function request
     *
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    public int handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger lambdaLogger = context.getLogger();
        JestResult jestResult = null;
        int responseCode = 0;

        lambdaLogger.log("****** Starting LANA operation.....\n");

        //Set system property with desired environment profile for proper property environment file to be loaded
        System.setProperty("env", AppConfig.PROFILE_DEV);

        //get spring context
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        //check if spring context has been loaded
        if(applicationContext == null) {
            throw new RuntimeException("Spring Context Loading Error");
        }

        //get service
        PublisherService service = applicationContext.getBean(PublisherService.class);

        //Start ETL job
        jestResult = service.generateLanaPublisherData();
        responseCode = jestResult.getResponseCode();

        if (responseCode == 200) {
            lambdaLogger.log("****** Total count of publishers stored: " + ESQueryUtil.getBulkInsertCount(jestResult) + "\n");
        }

        return responseCode;
    }
}
