package br.com.tp.lncr.fooditem.bdd.datasources.storage;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/br/com/tp/lncr/fooditem/bdd/datasources/storage",
        glue = "br.com.tp.lncr.fooditem.bdd.datasources.storage",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/storage.html",
                "json:target/cucumber-reports/storage.json"
        },
        monochrome = true
)
public class AllStorageRunner {
}

