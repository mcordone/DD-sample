
package com.aol.advertising.dealdiscovery.forecast.lambda.packages;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.aol.advertising.dealdiscovery.forecast.application.AppConfig;
import com.aol.advertising.dealdiscovery.forecast.service.PackageService;
import com.aol.advertising.dealdiscovery.forecast.util.ESQueryUtil;
import io.searchbox.client.JestResult;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * Created by mcordones13 on 1/13/17.
 */
public class ForecastLambdaProd {

    public int handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger lambdaLogger = context.getLogger();
        JestResult jestResult = null;
        int responseCode;

        lambdaLogger.log("****** Starting Package LANA Operation.....\n");

        //Set system property with desired environment profile for proper property environment file to be loaded
        System.setProperty("env", AppConfig.PROFILE_PROD);

        //get spring context
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        //check if spring context has been loaded
        if(applicationContext == null) {
            throw new RuntimeException("Spring Context Loading Error");
        }

        //get service
        PackageService service = applicationContext.getBean(PackageService.class);

        //Start ETL job
        jestResult = service.generateLanaPackageData();
        responseCode = jestResult.getResponseCode();

        if (responseCode == 200) {
            lambdaLogger.log("****** Total count of deals stored: " + ESQueryUtil.getBulkInsertCount(jestResult) + "\n");
        }

        return responseCode;
    }
}
