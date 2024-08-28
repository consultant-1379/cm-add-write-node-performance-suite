
package com.ericsson.oss.mediation.cm.tests.add.node;

import static com.ericsson.cifwk.taf.datasource.TafDataSources.fromCsv;
import static com.ericsson.cifwk.taf.datasource.TafDataSources.shared;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.ADD_REMOVE_100_NODES_10_VUSERS_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.ADD_REMOVE_100_NODES_1_VUSER_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.ADD_REMOVE_100_NODES_2_VUSERS_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.ADD_REMOVE_100_NODES_4_VUSERS_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.ADD_REMOVE_1_NODE_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.ADD_REMOVE_5K_DUMMY_NETWORK_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.NODE_FILTER_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.REMOVE_20_NODES_10_VUSERS_DS;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.REMOVE_20_NODES_1_VUSER_DS;

import javax.inject.Inject;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.oss.mediation.cm.flows.SyncLteNodeFlows;
import com.ericsson.oss.mediation.cm.tests.common.BaseScenario;
import com.ericsson.oss.mediation.cm.tests.common.flows.AddRemoveLteNodeFlows;
import com.ericsson.oss.mediation.cm.tests.common.flows.LoginFlows;

public class AddRemoveLteNode extends BaseScenario {

    private static final String DATASOURCE_DIR = "data/addRemoveNode/";
    private static final String SCENARIO_1_DESCRIPTION = "Add/Remove LTE ERBS nodes";
    private static final String SCENARIO_2_DESCRIPTION = "Add/Sync/Remove LTE ERBS nodes";
    private static final String SCENARIO_3_DESCRIPTION = "Remove LTE ERBS nodes with wildcard";

    @Inject
    private AddRemoveLteNodeFlows addRemoveLteNodeFlows;
    @Inject
    private LoginFlows commonEnmFlows;
    @SuppressWarnings("deprecation")
    @Inject
    private SyncLteNodeFlows syncLteNodeFlows;

    @BeforeClass
    protected void setupDataSourcesFromCsv() {
        setupSingleVUserDataSources();
        setupMultipleVUserDataSources();
    }

    @Test
    @TestId(id = "TORF-67762_AddRemoveNode-001", title = "Add/Remove 1 node - 1 vUser")
    public void addRemoveSingleNode() {
        addRemoveNodesScenario(SCENARIO_1_DESCRIPTION, ADD_REMOVE_1_NODE_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_AddRemoveNode-002", title = "Add/Remove 100 nodes - 1 vUser")
    public void addRemoveBatchNodes1VUser() {
        addRemoveNodesScenario(SCENARIO_1_DESCRIPTION, ADD_REMOVE_100_NODES_1_VUSER_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_AddRemoveNode-003", title = "Add/Remove 100 nodes - 2 vUsers")
    public void addRemoveBatchNodes2VUser() {
        addRemoveNodesScenario(SCENARIO_1_DESCRIPTION, ADD_REMOVE_100_NODES_2_VUSERS_DS, 2);
    }

    @Test
    @TestId(id = "TORF-67762_AddRemoveNode-004", title = "Add/Remove 100 nodes - 4 vUsers")
    public void addRemoveBatchNodes4VUser() {
        addRemoveNodesScenario(SCENARIO_1_DESCRIPTION, ADD_REMOVE_100_NODES_4_VUSERS_DS, 4);
    }

    @Test
    @TestId(id = "TORF-67762_AddNode-005", title = "Add/Remove 100 nodes - 10 vUsers")
    public void addRemoveBatchNodes10VUser() {
        addRemoveNodesScenario(SCENARIO_1_DESCRIPTION, ADD_REMOVE_100_NODES_10_VUSERS_DS, 10);
    }

    @Test
    @TestId(id = "TORF-67762_AddRemoveNode-006", title = "Add/Remove 5K network - 7 vUsers")
    public void addRemove5KNetwork() {
        addRemoveNodesScenario(SCENARIO_1_DESCRIPTION, ADD_REMOVE_5K_DUMMY_NETWORK_DS, 7);
    }

    @Test
    @TestId(id = "TORF-67762_AddRemoveSyncedNode-007", title = "Delete 1 synced node - 1 vUser")
    public void addRemoveSyncedSingleNode() {
        addRemoveSyncedNodesScenario(SCENARIO_2_DESCRIPTION, ADD_REMOVE_1_NODE_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_AddRemoveSyncedNode-008", title = "Delete 20 synced nodes - 1 vUser")
    public void addRemoveSyncedBatchNodes1VUser() {
        addRemoveSyncedNodesScenario(SCENARIO_2_DESCRIPTION, REMOVE_20_NODES_1_VUSER_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_AddRemoveSyncedNode-009", title = "Delete 20 synced nodes - 10 vUsers")
    public void addRemoveSyncedBatchNodes10VUser() {
        addRemoveSyncedNodesScenario(SCENARIO_2_DESCRIPTION, REMOVE_20_NODES_10_VUSERS_DS, 10);
    }

    @Test
    @TestId(id = "AddRemoveNode-Performance-010", title = "Add 100 nodes then delete using wildcards with 1 vUser")
    public void removeBatchOfNodesWithWildCard1VUser() {
        removeNodesWithWildCardScenario(SCENARIO_3_DESCRIPTION, ADD_REMOVE_100_NODES_1_VUSER_DS, NODE_FILTER_DS);
    }

    private void setupSingleVUserDataSources() {
        context.addDataSource(ADD_REMOVE_1_NODE_DS, fromCsv(DATASOURCE_DIR + ADD_REMOVE_1_NODE_DS + CSV_EXTENSION));
        context.addDataSource(ADD_REMOVE_100_NODES_1_VUSER_DS, fromCsv(DATASOURCE_DIR + ADD_REMOVE_100_NODES_1_VUSER_DS + CSV_EXTENSION));
        context.addDataSource(REMOVE_20_NODES_1_VUSER_DS, fromCsv(DATASOURCE_DIR + REMOVE_20_NODES_1_VUSER_DS + CSV_EXTENSION));
        context.addDataSource(NODE_FILTER_DS, fromCsv(DATASOURCE_DIR + NODE_FILTER_DS + CSV_EXTENSION));
    }

    private void setupMultipleVUserDataSources() {
        context.addDataSource(ADD_REMOVE_100_NODES_2_VUSERS_DS, shared(fromCsv(DATASOURCE_DIR + ADD_REMOVE_100_NODES_2_VUSERS_DS + CSV_EXTENSION)));
        context.addDataSource(ADD_REMOVE_100_NODES_4_VUSERS_DS, shared(fromCsv(DATASOURCE_DIR + ADD_REMOVE_100_NODES_4_VUSERS_DS + CSV_EXTENSION)));
        context.addDataSource(ADD_REMOVE_100_NODES_10_VUSERS_DS, shared(fromCsv(DATASOURCE_DIR + ADD_REMOVE_100_NODES_10_VUSERS_DS + CSV_EXTENSION)));
        context.addDataSource(ADD_REMOVE_5K_DUMMY_NETWORK_DS, shared(fromCsv(DATASOURCE_DIR + ADD_REMOVE_5K_DUMMY_NETWORK_DS + CSV_EXTENSION)));
        context.addDataSource(REMOVE_20_NODES_10_VUSERS_DS, shared(fromCsv(DATASOURCE_DIR + REMOVE_20_NODES_10_VUSERS_DS + CSV_EXTENSION)));
    }

    private void addRemoveNodesScenario(final String testDescription, final String dataSource,
            final int numVUsers) {
        final TestScenario scenario = scenario(testDescription)
                .addFlow(commonEnmFlows.login())
                .addFlow(addRemoveLteNodeFlows.addBatchNodes(dataSource))
                .addFlow(addRemoveLteNodeFlows.verifyBatchOfNodesAdded(dataSource))
                .addFlow(addRemoveLteNodeFlows.removeBatchNodes(dataSource))
                .addFlow(commonEnmFlows.logout())
                .withTestStepExceptionHandler(logger.exceptionHandler())
                .withVusers(numVUsers)
                .build();
        executeRunner(scenario);
    }

    private void addRemoveSyncedNodesScenario(final String testDescription, final String dataSource,
            final int numVUsers) {
        @SuppressWarnings("deprecation")
        final TestScenario scenario = scenario(testDescription)
                .addFlow(commonEnmFlows.login())
                .addFlow(addRemoveLteNodeFlows.addBatchNodes(dataSource))
                .addFlow(addRemoveLteNodeFlows.verifyBatchOfNodesAdded(dataSource))
                .addFlow(syncLteNodeFlows.syncBatchNodes(dataSource))
                .addFlow(syncLteNodeFlows.verifyBatchNodesSynced(dataSource))
                .addFlow(addRemoveLteNodeFlows.removeBatchNodes(dataSource))
                .addFlow(commonEnmFlows.logout())
                .withTestStepExceptionHandler(logger.exceptionHandler())
                .withVusers(numVUsers)
                .build();
        executeRunner(scenario);
    }

    private void removeNodesWithWildCardScenario(final String testDescription, final String addNodeDataSource,
            final String removeNodeDataSource) {
        final TestScenario scenario = scenario(testDescription)
                .addFlow(commonEnmFlows.login())
                .addFlow(addRemoveLteNodeFlows.addBatchNodes(addNodeDataSource))
                .addFlow(addRemoveLteNodeFlows.verifyBatchOfNodesAdded(addNodeDataSource))
                .addFlow(addRemoveLteNodeFlows.removeFilteredNodes(removeNodeDataSource))
                .addFlow(commonEnmFlows.logout())
                .withTestStepExceptionHandler(logger.exceptionHandler())
                .withVusers(1)
                .build();
        executeRunner(scenario);
    }
}
