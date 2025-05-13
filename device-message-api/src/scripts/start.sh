#!/bin/bash


#################################################################################
# /********************************************************************************
#
#  * Copyright (c) 2023-24 Harman International
#
#  *
#
#  * Licensed under the Apache License, Version 2.0 (the "License");
#
#  * you may not use this file except in compliance with the License.
#
#  * You may obtain a copy of the License at
#
#  *
#
#  *  http://www.apache.org/licenses/LICENSE-2.0
#
#  *
#
#  * Unless required by applicable law or agreed to in writing, software
#
#  * distributed under the License is distributed on an "AS IS" BASIS,
#
#  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
#  * See the License for the specific language governing permissions and
#
#  * limitations under the License.
#
#  *
#
#  * SPDX-License-Identifier: Apache-2.0
#
#  ********************************************************************************/
#################################################################################

sed -i  's/protocol=\"HTTP\/1.1\"/protocol=\"HTTP\/1.1\" compression=\"on\" compressionMinSize=\"1024\" compressableMimeType=\"application\/json\" maxThreads=\"17000\" maxConnections=\"5000\" acceptorThreadCount=\"12\" acceptCount=\"100\" keepAliveTimeout=\"10000\" maxKeepAliveRequests=\"500\" processorCache=\"17000\" /g' /usr/local/tomcat/conf/server.xml
sed -i 's/connectionTimeout=\"20000\"/connectionTimeout=\"60000\" /g' /usr/local/tomcat/conf/server.xml
/bin/sh /usr/local/tomcat/bin/catalina.sh run



