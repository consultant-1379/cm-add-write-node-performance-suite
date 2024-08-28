
package com.ericsson.oss.mediation.cm.tests.write.node;

import static com.ericsson.cifwk.taf.datasource.TafDataSources.fromCsv;
import static com.ericsson.cifwk.taf.datasource.TafDataSources.shared;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.CREATE_DELETE_SINGLE_MO_160_NODES_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.CREATE_DELETE_SINGLE_MO_960_NODES_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.CREATE_RADIO_DATA_ONE_NODE_DS;
import static com.ericsson.oss.mediation.cm.teststeps.WriteLteNodeTestSteps.MO_UNDER_TEST;

import javax.inject.Inject;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.datasource.DataPoolDataSource;
import com.ericsson.cifwk.taf.datasource.DataRecord;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.oss.mediation.cm.tests.common.BaseScenario;
import com.ericsson.oss.mediation.cm.tests.common.flows.LoginFlows;
import com.ericsson.oss.mediation.cm.tests.common.flows.WriteLteNodeFlows;

public class CreateMoOnLteNode extends BaseScenario {

    private static final String DATASOURCE_DIR = "data/moCreateAndDelete/";
    private static final String SCENARIO_1_DESCRIPTION = "Create batch of Managed Objects";
    private static final String SCENARIO_2_DESCRIPTION = "Delete batch of Managed Objects";

    @Inject
    private WriteLteNodeFlows cmMediationFlows;
    @Inject
    private LoginFlows commonEnmFlows;

    @BeforeClass
    protected void setupDataSourcesFromCsv() {
        setupSingleVUserDataSources();
        setupMultipleVUserDataSources();
    }

    @BeforeMethod
    protected void setupEmptyDataSource() {
        context.addDataSource(MO_UNDER_TEST, DataPoolDataSource.createSharedEmpty(DataRecord.class));
    }

    @Test
    @TestId(id = "TORF-67762_CreateAndDeleteMo-001", title = "Create 1 MO per node across 160 nodes - 1 vUser")
    public void createSingleMoAcross160Nodes_singleVuser() {
        createBatchOfMosScenario(SCENARIO_1_DESCRIPTION, CREATE_DELETE_SINGLE_MO_160_NODES_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_CreateAndDeleteMo-002", title = "Delete 1 MO per node across 160 nodes - 1 vUser")
    public void deleteSingleMoAcross160Nodes_singleVuser() {
        deleteBatchOfMosScenario(SCENARIO_2_DESCRIPTION, CREATE_DELETE_SINGLE_MO_160_NODES_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_CreateAndDeleteMo-003", title = "Create 1 MO per node across 960 nodes - 1 vUser")
    public void createSingleMoAcross960Nodes_singleVuser() {
        createBatchOfMosScenario(SCENARIO_1_DESCRIPTION, CREATE_DELETE_SINGLE_MO_960_NODES_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_CreateAndDeleteMo-004", title = "Delete 1 MO per node across 960 nodes - 1 vUser")
    public void deleteSingleMoAcross960Nodes_singleVuser() {
        deleteBatchOfMosScenario(SCENARIO_2_DESCRIPTION, CREATE_DELETE_SINGLE_MO_960_NODES_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_CreateAndDeleteMo-005", title = "Create 1 MO per node across 960 nodes - 6 vUsers")
    public void createSingleMoAcross960Nodes_multipleVuser() {
        createBatchOfMosScenario(SCENARIO_1_DESCRIPTION, CREATE_DELETE_SINGLE_MO_960_NODES_DS, 6);
    }

    @Test
    @TestId(id = "TORF-67762_CreateAndDeleteMo-006", title = "Delete 1 MO per node across 960 nodes - 6 vUsers")
    public void deleteSingleMoAcross960Nodes_multipleVuser() {
        deleteBatchOfMosScenario(SCENARIO_2_DESCRIPTION, CREATE_DELETE_SINGLE_MO_960_NODES_DS, 6);

    }

    @Test(enabled = true)
    @TestId(id = "TORF-67762_CreateRadioData-007", title = "Create multiple radio MOs on a single node - 1 vUser")
    public void createRadioDataOnASingleNode_singleVuser() {
        final TestScenario scenario = scenario("")
                .addFlow(commonEnmFlows.login())
                .addFlow(cmMediationFlows.createMultipleMosSingleNode(CREATE_RADIO_DATA_ONE_NODE_DS))
                .addFlow(commonEnmFlows.logout())
                .withTestStepExceptionHandler(logger.exceptionHandler())
                .withVusers(1)
                .build();
        executeRunner(scenario);
    }

    private void setupSingleVUserDataSources() {
        context.addDataSource(CREATE_DELETE_SINGLE_MO_160_NODES_DS, fromCsv(DATASOURCE_DIR + CREATE_DELETE_SINGLE_MO_160_NODES_DS + CSV_EXTENSION));
        context.addDataSource(CREATE_RADIO_DATA_ONE_NODE_DS, fromCsv(DATASOURCE_DIR + CREATE_RADIO_DATA_ONE_NODE_DS + CSV_EXTENSION));
    }

    private void setupMultipleVUserDataSources() {
        context.addDataSource(CREATE_DELETE_SINGLE_MO_960_NODES_DS, shared(fromCsv(DATASOURCE_DIR + CREATE_DELETE_SINGLE_MO_960_NODES_DS
                + CSV_EXTENSION)));
    }

    private void createBatchOfMosScenario(final String testDescription, final String dataSource, final int numVUsers) {
        final TestScenario scenario = scenario(testDescription)
                .addFlow(commonEnmFlows.login())
                .addFlow(cmMediationFlows.createSingleMoOnMultipleNodes(dataSource))
                .addFlow(commonEnmFlows.logout())
                .withTestStepExceptionHandler(logger.exceptionHandler())
                .withVusers(numVUsers)
                .build();
        executeRunner(scenario);
    }

    private void deleteBatchOfMosScenario(final String testDescription, final String dataSource, final int numVUsers) {
        final TestScenario scenario = scenario(testDescription)
                .addFlow(commonEnmFlows.login())
                .addFlow(cmMediationFlows.deleteSingleMoFromMultipleNodes(dataSource))
                .addFlow(commonEnmFlows.logout())
                .withTestStepExceptionHandler(logger.exceptionHandler())
                .withVusers(numVUsers)
                .build();
        executeRunner(scenario);
    }
}
