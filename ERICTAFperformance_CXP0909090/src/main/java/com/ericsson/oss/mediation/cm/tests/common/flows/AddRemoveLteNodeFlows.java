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
import static com.ericsson.enm.data.CommonDataSources.AVAILABLE_USERS;
import static com.ericsson.oss.mediation.cm.teststeps.AddRemoveLteNodeTestSteps.ADD_BATCH_LTE_NODES;
import static com.ericsson.oss.mediation.cm.teststeps.AddRemoveLteNodeTestSteps.ADD_BATCH_SECURE_LTE_NODES;
import static com.ericsson.oss.mediation.cm.teststeps.AddRemoveLteNodeTestSteps.CONFIRM_BATCH_OF_LTE_NODES_ADDED;
import static com.ericsson.oss.mediation.cm.teststeps.AddRemoveLteNodeTestSteps.DELETE_SUBNETWORK;
import static com.ericsson.oss.mediation.cm.teststeps.AddRemoveLteNodeTestSteps.REMOVE_BATCH_LTE_NODES;
import static com.ericsson.oss.mediation.cm.teststeps.AddRemoveLteNodeTestSteps.REMOVE_FILTERED_LTE_NODES;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.oss.mediation.cm.tests.common.flows.steps.KpiSteps;
import com.ericsson.oss.mediation.cm.teststeps.AddRemoveLteNodeTestSteps;

public class AddRemoveLteNodeFlows {

    @Inject
    private AddRemoveLteNodeTestSteps addRemoveLteNodeTestSteps;
    @Inject
    private KpiSteps kpiSteps;

    /**
     * Flow to add a batch of nodes.
     *
     * @param dataSourceName
     *            The nodes data source that will be passed to add batch nodes test step
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow addBatchNodes(final String dataSourceName) {
        return flow("Add Batch Lte Nodes")
                .addTestStep(annotatedMethod(kpiSteps, "StartTest"))
                .addTestStep(annotatedMethod(addRemoveLteNodeTestSteps, ADD_BATCH_LTE_NODES))
                .addTestStep(annotatedMethod(kpiSteps, "EndTest"))
                .addTestStep(annotatedMethod(kpiSteps, "AddNodeKpiCheck"))
                .withDataSources(dataSource(dataSourceName))
                .build();
    }

    /**
     * Flow to add a batch of nodes with security credentials created for each network element.
     *
     * @param dataSourceName
     *            The nodes data source that will be passed to add batch nodes test step
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow addBatchNodesWithSecurity(final String dataSourceName) {
        return flow("Add Batch Secure Lte Nodes")
                .addTestStep(annotatedMethod(addRemoveLteNodeTestSteps, ADD_BATCH_SECURE_LTE_NODES))
                .withDataSources(dataSource(dataSourceName))
                .build();
    }

    /**
     * Flow to verify batch of nodes added.
     *
     * @param dataSourceName
     *            The nodes data source that will be verified as part of verify batch of nodes added test step.
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow verifyBatchOfNodesAdded(final String dataSourceName) {
        return flow("Verify Batch Of Lte Nodes Added")
                .addTestStep(annotatedMethod(addRemoveLteNodeTestSteps, CONFIRM_BATCH_OF_LTE_NODES_ADDED))
                .withDataSources(dataSource(dataSourceName))
                .build();
    }

    /**
     * Flow that removes a batch of nodes
     *
     * @param dataSourceName
     *            The nodes data source that will be deleted as part of remove batch nodes test step. Users data source to pass in to login test step
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow removeBatchNodes(final String dataSourceName) {
        return flow("Remove Batch Lte Nodes")
                .addTestStep(annotatedMethod(addRemoveLteNodeTestSteps, REMOVE_BATCH_LTE_NODES))
                .addTestStep(annotatedMethod(addRemoveLteNodeTestSteps, DELETE_SUBNETWORK))
                .withDataSources(dataSource(dataSourceName), dataSource(AVAILABLE_USERS))
                .build();
    }

    /**
     * Flow that deletes all nodes from the SUT using the CLIs wildcard option.
     *
     * @param dataSourceName
     *            The data file that contains a filter to be used. The filter will be combined with the CLI wildcard to determine the nodes to be
     *            deleted.
     *            Example: Specifying filter "LTE01" all nodes with MeContextId starting with LTE01 will be deleted
     *            Example Command: cmedit delete LTE01* NetworkElement -ALL
     * @return the {@code flow} to be executed.
     */
    public TestStepFlow removeFilteredNodes(final String dataSourceName) {
        return flow("Remove Batch Lte Nodes")
                .addTestStep(annotatedMethod(addRemoveLteNodeTestSteps, REMOVE_FILTERED_LTE_NODES))
                .addTestStep(annotatedMethod(addRemoveLteNodeTestSteps, DELETE_SUBNETWORK))
                .withDataSources(dataSource(dataSourceName), dataSource(AVAILABLE_USERS))
                .build();
    }

}
