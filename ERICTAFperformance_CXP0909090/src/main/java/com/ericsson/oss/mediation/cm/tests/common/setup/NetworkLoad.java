
package com.ericsson.oss.mediation.cm.tests.common.setup;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;
import static com.ericsson.oss.mediation.cm.tests.common.constants.TestCaseConstants.ADD_REMOVE_1_NODE_DS;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.oss.mediation.cm.tests.common.BaseScenario;
import com.ericsson.oss.mediation.cm.tests.common.flows.AddRemoveLteNodeFlows;
import com.ericsson.oss.mediation.cm.tests.common.flows.LoginFlows;

public class NetworkLoad extends BaseScenario {

    @Inject
    private AddRemoveLteNodeFlows addRemoveLteNodeFlows;
    @Inject
    private LoginFlows commonEnmFlows;

    final Integer numVUsers = DataHandler.getConfiguration().getProperty("num.vusers", 1, Integer.class);
    final String dataSource = DataHandler.getConfiguration().getProperty("csv.name", ADD_REMOVE_1_NODE_DS, String.class);

    @Test
    @TestId(id = "Setup", title = "Add batch of network elements")
    public void addLargeNetwork() {
        final TestScenario scenario = scenario("Setup network element load on a server")
                .addFlow(commonEnmFlows.login())
                .addFlow(addRemoveLteNodeFlows.addBatchNodesWithSecurity(dataSource))
                .addFlow(commonEnmFlows.logout())
                .withVusers(numVUsers)
                .build();
        executeRunner(scenario);
    }

    @Test
    @TestId(id = "Teardown", title = "Remove batch of network elements")
    public void removeLargeNetwork() {
        final TestScenario scenario = scenario("Remove network element load from a server")
                .addFlow(commonEnmFlows.login())
                .addFlow(addRemoveLteNodeFlows.removeBatchNodes(dataSource))
                .addFlow(commonEnmFlows.logout())
                .withVusers(numVUsers)
                .build();
        executeRunner(scenario);
    }
}
