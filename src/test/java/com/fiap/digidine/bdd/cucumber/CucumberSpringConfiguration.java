package com.fiap.digidine.bdd.cucumber;

import com.fiap.digidine.DigidineApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = CucumberTest.class)
public class CucumberSpringConfiguration {

}
