package br.com.tp.lncr.fooditem.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/br/com/tp/lncr/fooditem/bdd",
        glue = "br.com.tp.lncr.fooditem.bdd",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/all-bdd.html",
                "json:target/cucumber-reports/all-bdd.json",
                "junit:target/cucumber-reports/all-bdd.xml"
        },
        monochrome = true
)
public class AllBddRunner {
}

