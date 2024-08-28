package com.ericsson.oss.mediation.cm.tests.common.flows;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;
import static com.ericsson.enm.data.CommonDataSources.AVAILABLE_USERS;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.oss.clientcommon.login.teststeps.LoginRestTestSteps;

public class LoginFlows {

    @Inject
    private LoginRestTestSteps loginSteps;

    public TestStepFlow login() {
        return flow("Login as ENM user.")
                .withDataSources(dataSource(AVAILABLE_USERS))
                .addTestStep(annotatedMethod(loginSteps, "login"))
                .build();
    }

    public TestStepFlow logout() {
        return flow("Logout as ENM user.")
                .withDataSources(dataSource(AVAILABLE_USERS))
                .addTestStep(annotatedMethod(loginSteps, "logout"))
                .build();
    }

}
