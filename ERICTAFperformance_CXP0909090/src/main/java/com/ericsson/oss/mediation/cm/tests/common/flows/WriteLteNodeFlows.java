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

package com.ericsson.oss.mediation.cm.tests.common.flows;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;
import static com.ericsson.oss.mediation.cm.teststeps.WriteLteNodeTestSteps.CONFIRM_UPDATE_OF_ALL_MANAGED_OBJECT_INSTANCES;
import static com.ericsson.oss.mediation.cm.teststeps.WriteLteNodeTestSteps.CREATE_SINGLE_MO_MULTIPLE_NE;
import static com.ericsson.oss.mediation.cm.teststeps.WriteLteNodeTestSteps.CREATE_MULTIPLE_MO_SINGLE_NE;
import static com.ericsson.oss.mediation.cm.teststeps.WriteLteNodeTestSteps.DELETE_SINGLE_MO_MULTIPLE_NE;
import static com.ericsson.oss.mediation.cm.teststeps.WriteLteNodeTestSteps.GET_NUM_MATCHING_MOS;
import static com.ericsson.oss.mediation.cm.teststeps.WriteLteNodeTestSteps.READ_BATCH_OF_MO;
import static com.ericsson.oss.mediation.cm.teststeps.WriteLteNodeTestSteps.UPDATE_ALL_MANAGED_OBJECT_INSTANCES;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.oss.mediation.cm.teststeps.WriteLteNodeTestSteps;

/**
 * This class contains TAF flows that can be used with TAF scenarios to perform MO Create/Read/Update/Action operations.
 */
public class WriteLteNodeFlows {

    @Inject
    private WriteLteNodeTestSteps writeLteNodeTestSteps;

    /**
     * Updates all Managed Object instances of the specified MO type for the node identified by the filter.
     *
     * @param dataSourceName
     *            The data source that provides details of the MOs (nodeFilter, moType, attributes)
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow updateAllManagedObjectInstances(final String dataSourceName) {
        return flow("Update Batch of Managed Objects")
                .withDataSources(dataSource(dataSourceName))
                .addTestStep(annotatedMethod(writeLteNodeTestSteps, UPDATE_ALL_MANAGED_OBJECT_INSTANCES))
                .build();
    }

    /**
     * Performs a query on the ENM system to determine the number of FDNs matching the filter criteria specified by the parameters. Results are added
     * to the data source {@code MO_UNDER_TEST}
     *
     * @param dataSourceName
     *            The data source that provides details of the MOs (nodeFilter, moType, attributes)
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow getNumberOfMatchingMos(final String dataSourceName) {
        return flow("Get Number of Matching Managed Objects under test")
                .withDataSources(dataSource(dataSourceName))
                .addTestStep(annotatedMethod(writeLteNodeTestSteps, GET_NUM_MATCHING_MOS))
                .build();
    }

    /**
     * Verifies all MOs updated of the specified MO type for the node identified by the filter.
     *
     * @param dataSourceName
     *            The data source that provides details of the MOs (nodeFilter, moType, attributes)
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow verifyAllManagedObjectInstances(final String dataSourceName) {
        return flow("Verify Batch of Managed Objects Updated")
                .withDataSources(dataSource(dataSourceName))
                .addTestStep(annotatedMethod(writeLteNodeTestSteps, CONFIRM_UPDATE_OF_ALL_MANAGED_OBJECT_INSTANCES))
                .build();
    }

    /**
     * Creates a specified MO type across the number of nodes on the SUT that match the filter.
     *
     * @param dataSourceName
     *            The data source that provides details of the type of MO to create, the attributes of that MO and the filter on which nodes to create
     *            them on.
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow createSingleMoOnMultipleNodes(final String dataSourceName) {
        return flow("Create Batch of Managed Objects")
                .withDataSources(dataSource(dataSourceName))
                .addTestStep(annotatedMethod(writeLteNodeTestSteps, CREATE_SINGLE_MO_MULTIPLE_NE))
                .build();
    }

    /**
     * Deletes the MO listed by FDN in the dataSource.
     *
     * @param dataSourceName
     *            The data source that provides details of the type of MO that was created and on which nodes it was created.
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow deleteSingleMoFromMultipleNodes(final String dataSourceName) {
        return flow("Delete Batch of Managed Objects")
                .withDataSources(dataSource(dataSourceName))
                .addTestStep(annotatedMethod(writeLteNodeTestSteps, DELETE_SINGLE_MO_MULTIPLE_NE))
                .build();
    }

    /**
     * Creates a specified MO type across the number of nodes on the SUT that match the filter.
     *
     * @param dataSourceName
     *            The data source that provides details of the type of MO to create, the attributes of that MO and the filter on which nodes to create
     *            them on.
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow createMultipleMosSingleNode(final String dataSourceName) {
        return flow("Create Batch of Managed Objects")
                .withDataSources(dataSource(dataSourceName))
                .addTestStep(annotatedMethod(writeLteNodeTestSteps, CREATE_MULTIPLE_MO_SINGLE_NE))
                .build();
    }

    /**
     * Reads the attribute value(s) of a specified MO for nodes that meet the specified criteria, e.g. NodeFilter
     *
     * @param dataSourceName
     *            The data source that provides details of the type of MO and the attribute that you wish to retrieve.
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow readAttributeValues(final String dataSourceName) {
        return flow("Read MO attribute values")
                .withDataSources(dataSource(dataSourceName))
                .addTestStep(annotatedMethod(writeLteNodeTestSteps, READ_BATCH_OF_MO))
                .build();
    }
}
