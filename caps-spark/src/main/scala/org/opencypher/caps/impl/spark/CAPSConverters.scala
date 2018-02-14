/*
 * Copyright (c) 2016-2018 "Neo4j, Inc." [https://neo4j.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencypher.caps.impl.spark

import org.opencypher.caps.api.CAPSSession
import org.opencypher.caps.api.graph.{CypherQueryPlans, CypherResult, CypherSession, PropertyGraph}
import org.opencypher.caps.api.table.CypherRecords
import org.opencypher.caps.impl.exception.UnsupportedOperationException
import org.opencypher.caps.impl.spark.physical.CAPSQueryPlans

object CAPSConverters {

  implicit class RichSession(val session: CypherSession) extends AnyVal {
    def asCaps: CAPSSession = session match {
      case caps: CAPSSession => caps
      case _ => throw UnsupportedOperationException(s"can only handle CAPS sessions, got $session")
    }
  }

  implicit class RichPropertyGraph(val graph: PropertyGraph) extends AnyVal {
    def asCaps: CAPSGraph = graph match {
      case caps: CAPSGraph => caps
      case _ => throw UnsupportedOperationException(s"can only handle CAPS graphs, got $graph")
    }
  }

  implicit class RichCypherResult(val result: CypherResult) extends AnyVal {
    def asCaps: CAPSResult = result match {
      case caps: CAPSResult => caps
      case _ => throw UnsupportedOperationException(s"can only handle CAPS result, got $result")
    }
  }

  implicit class RichCypherRecords(val records: CypherRecords) extends AnyVal {
    def asCaps: CAPSRecords = records match {
      case caps: CAPSRecords => caps
      case _ => throw UnsupportedOperationException(s"can only handle CAPS records, got $records")
    }
  }

  implicit class RichCypherPlans(val plans: CypherQueryPlans) extends AnyVal {
    def asCaps: CAPSQueryPlans = plans match {
      case caps: CAPSQueryPlans => caps
      case _ => throw UnsupportedOperationException(s"can only handle CAPS plans, got $plans")
    }
  }

}
