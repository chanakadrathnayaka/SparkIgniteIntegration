package com.iamchanaka.sparkigniteintegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

  private final SparkIgniteService sparkIgniteService;

  @Autowired
  public DataController(SparkIgniteService sparkIgniteService) {
    this.sparkIgniteService = sparkIgniteService;
  }

  @PostMapping("/processData")
  public String processData() {
    sparkIgniteService.processDataAndSaveToIgnite();
    return "Data processed and saved to Ignite.";
  }

  @GetMapping("/data/{key}")
  public String getData(@PathVariable Integer key) {
    return sparkIgniteService.getDataFromIgnite(key);
  }
}
//mvn spring-boot:run
