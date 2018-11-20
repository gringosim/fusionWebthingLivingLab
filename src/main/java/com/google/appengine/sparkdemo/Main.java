/*
 * Copyright (c) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.appengine.sparkdemo;

import static spark.Spark.port;
import com.google.cloud.datastore.DatastoreOptions;

import java.io.FileNotFoundException;
/*
//Run locally
canaima:TestSpark Eugenio$ echo $JAVA_HOME
/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home
canaima:TestSpark Eugenio$ /Users/Eugenio/Downloads/apache-maven-3.3.9/bin/mvn clean package exec:java
 
//Steps for deploy
canaima:TestSpark Eugenio$ /Users/Eugenio/Downloads/apache-maven-3.3.9/bin/mvn appengine:deploy
Error: JAVA_HOME is not defined correctly.
  We cannot execute /Library/Java/Home/bin/java
canaima:TestSpark Eugenio$  JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home
canaima:TestSpark Eugenio$ export JAVA_HOME
canaima:TestSpark Eugenio$ /Users/Eugenio/Downloads/apache-maven-3.3.9/bin/mvn appengine:deploy
*/

public class Main {

  /**
   * Starts the webapp on localhost:8080.
   */
  public static void main(String[] args) throws FileNotFoundException {
    port(8080);  
    
    String kind = "DemoUser";
    
    if (args != null) {
      for (String arg : args) {
        if (arg.startsWith("kind=")) {
          kind = arg.substring("kind=".length());
        }
      }
    }
    //this class manage users on Google Cloud if available
    /*
    UserService service = new UserService(DatastoreOptions.getDefaultInstance().getService(), kind);
    UserController userController = new UserController(service);
    es.upm.SparkLivingLab.setUserUtils(service);
    */
    //SparkLivingLab is the main class, it defines autenthication and routes
    es.upm.SparkLivingLab.main(args);
   
  }
  
}
