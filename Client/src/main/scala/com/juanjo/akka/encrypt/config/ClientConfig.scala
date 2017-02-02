/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.juanjo.akka.encrypt.config

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger

object ClientConfig {

  val ClientConfigDefault = "client-reference.conf"
  val ParentConfigName = "client"
  val ClientConfigResource = "external.config.resource"
  val ClientConfigFile = "external.config.filename"
  val ServerNode ="akka.cluster.seed-nodes"

}

trait ClientConfig {

  import ClientConfig._

  lazy val logger: Logger = ???

  val config: Config = {

    val defaultConfig = ConfigFactory.load(ClientConfigDefault).getConfig(ParentConfigName)
    val configFile = defaultConfig.getString(ClientConfigFile)
    val configResource = defaultConfig.getString(ClientConfigResource)

    //Get the Client-application.conf properties if exists in resources
    val configWithResource: Config = {
      val resource = ClientConfig.getClass.getClassLoader.getResource(ClientConfigResource)
      Option(resource).fold {
        logger.warn("User resource (" + configResource + ") haven't been found")
        val file = new File(configResource)
        if (file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(ParentConfigName)
          userConfig.withFallback(defaultConfig)
        } else {
          logger.warn("User file (" + configResource + ") haven't been found in classpath")
          defaultConfig
        }
      } { resTemp =>
        val userConfig = ConfigFactory.parseResources(ClientConfigResource).getConfig(ParentConfigName)
        userConfig.withFallback(defaultConfig)
      }
    }

    //Get the user external Client-application.conf properties if exists
    val finalConfig: Config = {
      if(configFile.isEmpty){
        configWithResource
      }else{
        val file = new File(configFile)
        if (file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(ParentConfigName)
          userConfig.withFallback(configWithResource)
        } else {
          logger.error("User file (" + configFile + ") haven't been found")
          configWithResource
        }
      }
    }
    ConfigFactory.load(finalConfig)
  }

}