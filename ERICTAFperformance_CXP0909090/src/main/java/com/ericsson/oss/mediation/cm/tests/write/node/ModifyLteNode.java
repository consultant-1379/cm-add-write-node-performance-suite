
package com.ericsson.oss.mediation.cm.tests.write.node;

import static com.ericsson.cifwk.taf.datasource.TafDataSources.fromCsv;
import static com.ericsson.cifwk.taf.datasource.TafDataSources.shared;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.LOCK_ALL_EUTRANCELLS_IN_NETWORK_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.LOCK_ALL_EUTRANCELLS_IN_NETWORK_MUTIPLE_USERS_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.LOCK_ALL_EUTRANCELLS_IN_SINGLE_NODE_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.LOCK_ALL_EUTRANCELLS_IN_SINGLE_RNS_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.UNLOCK_ALL_EUTRANCELLS_IN_NETWORK_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.UNLOCK_ALL_EUTRANCELLS_IN_NETWORK_MULTIPLE_USERS_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.UNLOCK_ALL_EUTRANCELLS_IN_SINGLE_NODE_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.UNLOCK_ALL_EUTRANCELLS_IN_SINGLE_RNS_DS;
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

public class ModifyLteNode extends BaseScenario {

    private static final String DATASOURCE_DIR = "data/moModify/";
    private static final String SCENARIO_DESCRIPTION = "Modify Managed Object attributes";

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
    @TestId(id = "TORF-67762_ModifyMo-001", title = "Lock All EutranCells on a Single Node - 1 vUser, 1 CLI command")
    public void lockAllEutranCellsFor1Node_singleVuser() {
        modifyNodeScenario(SCENARIO_DESCRIPTION, LOCK_ALL_EUTRANCELLS_IN_SINGLE_NODE_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_ModifyMo-002", title = "Unlock All EutranCells on a Single Node - 1 vUser, 1 CLI command")
    public void unlockAllEutranCellsFor1Node_singleVuser() {
        modifyNodeScenario(SCENARIO_DESCRIPTION, UNLOCK_ALL_EUTRANCELLS_IN_SINGLE_NODE_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_ModifyMo-003", title = "Lock All EutranCells on 160 Nodes - 1 vUser, 1 CLI command")
    public void lockAllEutranCellsFor1Rns_singleVuser() {
        modifyNodeScenario(SCENARIO_DESCRIPTION, LOCK_ALL_EUTRANCELLS_IN_SINGLE_RNS_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_ModifyMo-004", title = "Unlock All EutranCells on 160 Nodes - 1 vUser, 1 CLI command")
    public void unlockAllEutranCellsFor1Rns_singleVuser() {
        modifyNodeScenario(SCENARIO_DESCRIPTION, UNLOCK_ALL_EUTRANCELLS_IN_SINGLE_RNS_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_ModifyMo-005", title = "Lock All EutranCells on 1600 nodes - 10 vUsers, 10 CLI command")
    public void lockAllEutranCellsFor10Rns_multipleVuser() {
        modifyNodeScenario(SCENARIO_DESCRIPTION, LOCK_ALL_EUTRANCELLS_IN_NETWORK_MUTIPLE_USERS_DS, 10);
    }

    @Test
    @TestId(id = "TORF-67762_ModifyMo-006", title = "Unlock All EutranCells on 1600 nodes - 10 vUsers, 10 CLI commands")
    public void unlockAllEutranCellsFor10Rns_multipleVuser() {
        modifyNodeScenario(SCENARIO_DESCRIPTION, UNLOCK_ALL_EUTRANCELLS_IN_NETWORK_MULTIPLE_USERS_DS, 10);
    }

    @Test
    @TestId(id = "TORF-67762_ModifyMo-007", title = "Lock All EutranCells on 1600 nodes - 1 vUser, 1 CLI command")
    public void lockAllEutranCellsFor10Rns_singleVuser() {
        modifyNodeScenario(SCENARIO_DESCRIPTION, LOCK_ALL_EUTRANCELLS_IN_NETWORK_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_ModifyMo-008", title = "Unlock All EutranCells on 1600 nodes  - 1 vUser, 1 CLI command")
    public void unlockAllEutranCellsFor10Rns_singleVuser() {
        modifyNodeScenario(SCENARIO_DESCRIPTION, UNLOCK_ALL_EUTRANCELLS_IN_NETWORK_DS, 1);
    }

    private void setupSingleVUserDataSources() {
        context.addDataSource(LOCK_ALL_EUTRANCELLS_IN_SINGLE_NODE_DS,
                fromCsv(DATASOURCE_DIR + LOCK_ALL_EUTRANCELLS_IN_SINGLE_NODE_DS + CSV_EXTENSION));
        context.addDataSource(UNLOCK_ALL_EUTRANCELLS_IN_SINGLE_NODE_DS, fromCsv(DATASOURCE_DIR + UNLOCK_ALL_EUTRANCELLS_IN_SINGLE_NODE_DS
                + CSV_EXTENSION));
        context.addDataSource(LOCK_ALL_EUTRANCELLS_IN_SINGLE_RNS_DS, fromCsv(DATASOURCE_DIR + LOCK_ALL_EUTRANCELLS_IN_SINGLE_RNS_DS + CSV_EXTENSION));
        context.addDataSource(UNLOCK_ALL_EUTRANCELLS_IN_SINGLE_RNS_DS, fromCsv(DATASOURCE_DIR + UNLOCK_ALL_EUTRANCELLS_IN_SINGLE_RNS_DS
                + CSV_EXTENSION));
        context.addDataSource(LOCK_ALL_EUTRANCELLS_IN_NETWORK_DS, fromCsv(DATASOURCE_DIR + LOCK_ALL_EUTRANCELLS_IN_NETWORK_DS + CSV_EXTENSION));
        context.addDataSource(UNLOCK_ALL_EUTRANCELLS_IN_NETWORK_DS, fromCsv(DATASOURCE_DIR + UNLOCK_ALL_EUTRANCELLS_IN_NETWORK_DS + CSV_EXTENSION));
    }

    private void setupMultipleVUserDataSources() {
        context.addDataSource(LOCK_ALL_EUTRANCELLS_IN_NETWORK_MUTIPLE_USERS_DS, shared(fromCsv(DATASOURCE_DIR
                + LOCK_ALL_EUTRANCELLS_IN_NETWORK_MUTIPLE_USERS_DS
                + CSV_EXTENSION)));
        context.addDataSource(UNLOCK_ALL_EUTRANCELLS_IN_NETWORK_MULTIPLE_USERS_DS,
                shared(fromCsv(DATASOURCE_DIR + UNLOCK_ALL_EUTRANCELLS_IN_NETWORK_MULTIPLE_USERS_DS + CSV_EXTENSION)));
    }

    private void modifyNodeScenario(final String testDescription, final String lockUnlockDataSource, final int numVUsers) {
        final TestScenario scenario = scenario(testDescription)
                .addFlow(commonEnmFlows.login())
                .addFlow(cmMediationFlows.getNumberOfMatchingMos(lockUnlockDataSource))
                .addFlow(cmMediationFlows.updateAllManagedObjectInstances(lockUnlockDataSource))
                .addFlow(cmMediationFlows.verifyAllManagedObjectInstances(MO_UNDER_TEST))
                .addFlow(commonEnmFlows.logout())
                .withTestStepExceptionHandler(logger.exceptionHandler())
                .withVusers(numVUsers)
                .build();
        executeRunner(scenario);
    }

}
