package co.com.ies.test.pdanyos.cucumber.stepdefs;

import co.com.ies.test.pdanyos.PdanyosApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = PdanyosApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
