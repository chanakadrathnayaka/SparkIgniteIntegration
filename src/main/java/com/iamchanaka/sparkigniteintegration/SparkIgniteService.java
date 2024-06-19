package com.iamchanaka.sparkigniteintegration;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spark.JavaIgniteContext;
import org.apache.ignite.spark.IgniteRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.Arrays;

@Service
public class SparkIgniteService {

  private final Ignite ignite;
  private final JavaSparkContext sparkContext;

  public SparkIgniteService() {
    // Spark configuration
    SparkConf sparkConf = new SparkConf()
        .setAppName("SparkIgniteIntegration")
        .setMaster("spark://<hostname>:7077");

    this.sparkContext = new JavaSparkContext(sparkConf);

    // Ignite configuration
    IgniteConfiguration igniteConfig = new IgniteConfiguration();
    igniteConfig.setClientMode(true);
    this.ignite = Ignition.start(igniteConfig);
  }

  public void processDataAndSaveToIgnite() {
    // Ignite context
    JavaIgniteContext<Integer, String> igniteContext = new JavaIgniteContext<>(sparkContext, ignite.configuration(), true);

    // Create an IgniteRDD
    IgniteRDD<Integer, String> igniteRDD = igniteContext.<Integer, String>fromCache("sparkCache");

    // Process and save data to Ignite
    JavaRDD<String> data = sparkContext.parallelize(Arrays.asList("one", "two", "three"));
    igniteRDD.savePairs(data.mapToPair(value -> new Tuple2<>(value.hashCode(), value)));

    igniteContext.close();
  }

  public String getDataFromIgnite(Integer key) {
    return ignite.cache("sparkCache").get(key);
  }

  public void shutdown() {
    sparkContext.close();
    ignite.close();
  }
}

