
package com.ericsson.oss.mediation.cm.tests.write.node;

import static com.ericsson.cifwk.taf.datasource.TafDataSources.fromCsv;
import static com.ericsson.cifwk.taf.datasource.TafDataSources.shared;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;
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

/**
 * Class used to test different performance scenarios for the Read Non-Persistent Node Data use case.
 */
public class ReadNonPersistentDataFromLteNode extends BaseScenario {

    private static final String READ_10_ATTRIBUTES_ON_1_NODE_DS = "Read10AttributesAcross1Node";
    private static final String READ_10_ATTRIBUTES_ON_160_NODES_DS = "Read10AttributesAcross160Nodes";
    private static final String READ_10_ATTRIBUTES_ON_1440_NODES_WITH_VUSERS_DS = "Read10AttributesAcross1440NodesWithVusers";
    private static final String READ_10_ATTRIBUTES_ON_1440_NODES_DS = "Read10AttributesAcross1440Nodes";
    private static final String READ_10_ATTRIBUTES_ON_ALL_EUTRANCELLS_IN_NETWORK_DS = "Read10AttributesAcrossEntireNetwork";
    private static final String READ_10_ATTRIBUTES_ON_ALL_EUTRANCELLS_IN_NETWORK_WITH_VUSERS_DS = "Read10AttributesAcrossEntireNetworkWithVusers";
    private static final String DATASOURCE_DIR = "data/moRead/";
    private static final String SCENARIO_DESCRIPTION = "Read non persistent attributes";

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
    @TestId(id = "TORF-67762_ReadMo-001", title = "Read non persistent attributes from all EUtranCellFDDs on 1 node - 1 vUser, 1 CLI command")
    public void readNonPersMoAttrsFrom1Node_singleVuser() {
        readNodeScenario(SCENARIO_DESCRIPTION, READ_10_ATTRIBUTES_ON_1_NODE_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_ReadMo-002", title = "Read non persistent attributes from all EUtranCellFDDs on 160 nodes - 1 vUser, 1 CLI command")
    public void readNonPersMoAttrsFrom160Nodes_singleVuser() {
        readNodeScenario(SCENARIO_DESCRIPTION, READ_10_ATTRIBUTES_ON_160_NODES_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_ReadMo-003", title = "Read non persistent attributes from all EUtranCellFDDs on 1440 nodes - 9 vUser, 9 CLI commands")
    public void readANonPersMoAttrsFrom1440Nodes_multipleVusers() {
        readNodeScenario(SCENARIO_DESCRIPTION, READ_10_ATTRIBUTES_ON_1440_NODES_WITH_VUSERS_DS, 9);
    }

    @Test
    @TestId(id = "TORF-67762_ReadMo-004", title = "Read non persistent attributes from all EUtranCellFDDs on 1440 nodes - 1 vUser, 1 CLI command")
    public void readNonPersMoAttrsFrom1440Nodes_singleVuser() {
        readNodeScenario(SCENARIO_DESCRIPTION, READ_10_ATTRIBUTES_ON_1440_NODES_DS, 1);
    }

    @Test
    @TestId(id = "TORF-67762_ReadMo-005", title = "Read non persistent attributes from all EUtranCellFDDs in the network - 25 vUser, 25 CLI command")
    public void readNonPersMoAttrsFromAllEutrancellsInNetwork_multipleVusers() {
        readNodeScenario(SCENARIO_DESCRIPTION, READ_10_ATTRIBUTES_ON_ALL_EUTRANCELLS_IN_NETWORK_WITH_VUSERS_DS, 25);
    }

    @Test
    @TestId(id = "TORF-67762_ReadMo-006", title = "Read non persistent attributes from all EUtranCellFDDs in the network - 1 vUser, 1 CLI command")
    public void readNonPersMoAttrsFromAllEutrancellsInNetwork_singleVuser() {
        readNodeScenario(SCENARIO_DESCRIPTION, READ_10_ATTRIBUTES_ON_ALL_EUTRANCELLS_IN_NETWORK_DS, 1);
    }

    private void setupSingleVUserDataSources() {
        context.addDataSource(READ_10_ATTRIBUTES_ON_1_NODE_DS, fromCsv(DATASOURCE_DIR + READ_10_ATTRIBUTES_ON_1_NODE_DS + CSV_EXTENSION));
        context.addDataSource(READ_10_ATTRIBUTES_ON_160_NODES_DS, fromCsv(DATASOURCE_DIR + READ_10_ATTRIBUTES_ON_160_NODES_DS + CSV_EXTENSION));
        context.addDataSource(READ_10_ATTRIBUTES_ON_1440_NODES_DS, fromCsv(DATASOURCE_DIR + READ_10_ATTRIBUTES_ON_1440_NODES_DS + CSV_EXTENSION));
        context.addDataSource(READ_10_ATTRIBUTES_ON_ALL_EUTRANCELLS_IN_NETWORK_DS, fromCsv(DATASOURCE_DIR
                + READ_10_ATTRIBUTES_ON_ALL_EUTRANCELLS_IN_NETWORK_DS + CSV_EXTENSION));
    }

    private void setupMultipleVUserDataSources() {
        context.addDataSource(READ_10_ATTRIBUTES_ON_1440_NODES_WITH_VUSERS_DS, shared(fromCsv(DATASOURCE_DIR
                + READ_10_ATTRIBUTES_ON_1440_NODES_WITH_VUSERS_DS + CSV_EXTENSION)));
        context.addDataSource(READ_10_ATTRIBUTES_ON_ALL_EUTRANCELLS_IN_NETWORK_WITH_VUSERS_DS, shared(fromCsv(DATASOURCE_DIR
                + READ_10_ATTRIBUTES_ON_ALL_EUTRANCELLS_IN_NETWORK_WITH_VUSERS_DS + CSV_EXTENSION)));
    }

    private void readNodeScenario(final String testDescription, final String dataSourceName, final int numVUsers) {
        final TestScenario scenario = scenario(testDescription)
                .addFlow(commonEnmFlows.login())
                .addFlow(cmMediationFlows.getNumberOfMatchingMos(dataSourceName))
                .addFlow(cmMediationFlows.readAttributeValues(MO_UNDER_TEST))
                .addFlow(commonEnmFlows.logout())
                .withTestStepExceptionHandler(logger.exceptionHandler())
                .withVusers(numVUsers)
                .build();
        executeRunner(scenario);
    }
}
