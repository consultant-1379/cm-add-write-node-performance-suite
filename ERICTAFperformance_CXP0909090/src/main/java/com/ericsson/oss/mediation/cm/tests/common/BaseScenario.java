/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.mediation.cm.tests.common;

import static com.ericsson.cifwk.taf.datasource.TafDataSources.fromCsv;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.enm.data.CommonDataSources.AVAILABLE_USERS;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

import com.ericsson.cifwk.taf.TestContext;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.enm.data.CommonDataSources;
import com.ericsson.enm.scenarios.FailingSummaryLogger;

public class BaseScenario extends TorTestCaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseScenario.class);
    protected final FailingSummaryLogger logger = new FailingSummaryLogger();
    protected static final String CSV_EXTENSION = ".csv";
    protected TestScenarioRunner runner;

    @Inject
    protected TestContext context;

    @BeforeClass
    protected void setDataSources() {
        try {
            CommonDataSources.initializeDataSources();
            addUserToDataSource();
        } catch (final Exception e) {
            LOGGER.error("ERROR during setup : {} ", e);
        }
    }

    private void addUserToDataSource() {        
        context.addDataSource(AVAILABLE_USERS, fromCsv("data/user.csv"));
    }

    protected void executeRunner(final TestScenario scenario) {
        runner = runner()
                .withListener(logger)
                .withExceptionHandler(ExceptionHandler.LOGONLY)
                .build();
        runner.start(scenario);
    }
}
