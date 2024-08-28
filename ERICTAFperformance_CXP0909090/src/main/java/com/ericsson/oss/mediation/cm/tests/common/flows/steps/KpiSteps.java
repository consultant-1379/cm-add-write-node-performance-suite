/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.mediation.cm.tests.common.flows.steps;

import static org.junit.Assert.assertNotNull;

import static com.ericsson.cifwk.taf.assertions.TafAsserts.assertTrue;

import java.text.MessageFormat;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.TestContext;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestStep;

/**
 * Contains {@code TestStep}s which can be used in {@code TestStepFlow}s to verify Key Performance Indicators (KPI) for the 'AddNode', 'RemoveNode',
 * and 'WriteNode' use cases.
 */
public class KpiSteps {

    public static final String START_TEST = "StartTest";
    public static final String END_TEST = "EndTest";
    public static final String ADD_NODE_KPI_CHECK = "AddNodeKpiCheck";
    public static final String REMOVE_NODE_KPI_CHECK = "RemoveNodeKpiCheck";
    private static final String START_TIME_ATTR_NAME = "startTime";
    private static final String END_TIME_ATTR_NAME = "endTime";

    @Inject
    private TestContext context;

    /**
     * This step can be used to record the start time of a test.
     */
    @TestStep(id = START_TEST)
    public void setStartTime() {
        context.setAttribute(START_TIME_ATTR_NAME, System.nanoTime());
    }

    /**
     * This step can be used to record the start time of a test.
     */
    @TestStep(id = END_TEST)
    public void setEndTime() {
        context.setAttribute(END_TIME_ATTR_NAME, System.nanoTime());
    }

    /**
     * This step can be used to determine if the add node use case has met its KPIs. It requires that the tests steps {@code StarTest} and
     * {@code EndTest} were previously executed to record the start and end of the use case under test.
     *
     * @param internalKpi
     *            Used to check more stringent KPI values based on previous test runs.
     * @param externalKpi
     *            Official KPI for the use case. Based on requirements.
     */
    @TestStep(id = ADD_NODE_KPI_CHECK)
    public void addNodeKpiCheck(@Input("addNodeIntKpi") final long internalKpi, @Input("addNodeExtKpi") final long externalKpi) {
        final long kpiTimeSeconds = getTestDurationInSeconds();
        final String EXT_KPI_FAILURE_MSG =
                MessageFormat.format("Add node use case failed to meet external KPI: {0} secs. Test execution took: {1} secs", externalKpi,
                        kpiTimeSeconds);
        final String INT_KPI_FAILURE_MSG =
                MessageFormat.format("Add node use case failed to meet internal KPI: {0} secs. Test execution took: {1} secs", internalKpi,
                        kpiTimeSeconds);
        assertTrue(EXT_KPI_FAILURE_MSG, kpiTimeSeconds <= externalKpi);
        assertTrue(INT_KPI_FAILURE_MSG, kpiTimeSeconds <= internalKpi);
    }

    /**
     * This step can be used to determine if the remove node use case has met its KPIs. It requires that the tests steps {@code StarTest} and
     * {@code EndTest} were previously executed to record the start and end of the use case under test.
     *
     * @param internalKpi
     *            Used to check more stringent KPI values based on previous test runs.
     * @param externalKpi
     *            Official KPI for the use case. Based on requirements.
     */
    @TestStep(id = REMOVE_NODE_KPI_CHECK)
    public void removeNodeKpiCheck(@Input("removeNodeIntKpi") final long internalKpi, @Input("removeNodeExtKpi") final long externalKpi) {
        final long kpiTimeSeconds = getTestDurationInSeconds();
        final String EXT_KPI_FAILURE_MSG =
                MessageFormat.format("Remove node use case failed to meet external KPI: {0} secs. Test execution took: {1} secs", externalKpi,
                        kpiTimeSeconds);
        final String INT_KPI_FAILURE_MSG =
                MessageFormat.format("Remove node use case failed to meet internal KPI: {0} secs. Test execution took: {1} secs", internalKpi,
                        kpiTimeSeconds);
        assertTrue(EXT_KPI_FAILURE_MSG, kpiTimeSeconds <= externalKpi);
        assertTrue(INT_KPI_FAILURE_MSG, kpiTimeSeconds <= internalKpi);
    }

    private long getTestDurationInSeconds() {
        final long startTime = context.getAttribute(START_TIME_ATTR_NAME);
        assertNotNull("Start time of the test was never recorded using test step with id: ", startTime);
        final long endTime = context.getAttribute(END_TIME_ATTR_NAME);
        assertNotNull("End time of the test was never recorded using test step with id: ", startTime);
        final long testDuration = endTime - startTime;
        final long kpiTimeSeconds = testDuration / 1000000000;
        return kpiTimeSeconds;
    }
}
