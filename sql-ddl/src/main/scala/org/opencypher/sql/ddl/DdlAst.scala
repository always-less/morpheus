/*
 * Copyright (c) 2016-2018 "Neo4j Sweden, AB" [https://neo4j.com]
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
 *
 * Attribution Notice under the terms of the Apache License 2.0
 *
 * This work was created by the collective efforts of the openCypher community.
 * Without limiting the terms of Section 6, any Derivative Work that is not
 * approved by the public consensus process of the openCypher Implementers Group
 * should not be described as “Cypher” (and Cypher® is a registered trademark of
 * Neo4j Inc.) or as "openCypher". Extensions by implementers or prototypes or
 * proposals for change that have been documented or implemented should only be
 * described as "implementation extensions to Cypher" or as "proposed changes to
 * Cypher that are not yet approved by the openCypher community".
 */
package org.opencypher.sql.ddl

import org.opencypher.okapi.api.types.CypherType
import org.opencypher.okapi.trees.AbstractTreeNode
import org.opencypher.sql.ddl.Ddl._

object Ddl {
  type Property = (String, CypherType)

  type EntityDefinition = (String, Map[String, CypherType])

  type KeyDefinition = (String, Set[String])
}

abstract class DdlAst extends AbstractTreeNode[DdlAst]

case class DdlDefinitions(
  labelDefinitions: List[LabelDefinition] = List.empty,
  schemaDefinitions: List[SchemaDefinition] = List.empty,
  graphDefinitions: List[GraphDefinition] = List.empty
) extends DdlAst

case class LabelDefinition(
  name: String,
  properties: Map[String, CypherType] = Map.empty,
  keyDefinition: Option[KeyDefinition] = None
) extends DdlAst

case class SchemaDefinition(
  name: String,
  nodeDefinitions: Set[Set[String]] = Set.empty,
  relDefinitions: Set[String] = Set.empty,
  schemaPatternDefinitions: Set[SchemaPatternDefinition] = Set.empty
) extends DdlAst

case class GraphDefinition(
  name: String,
  schemaName: Option[String]
) extends DdlAst

case class CardinalityConstraint(from: Int, to: Option[Int])

case class SchemaPatternDefinition(
  sourceLabels: Set[String],
  fromConstraint: CardinalityConstraint,
  relTypes: Set[String],
  toConstraint: CardinalityConstraint,
  targetLabels: Set[String]
) extends DdlAst

//case class GraphDeclaration(
//  name: String,
//  labels: List[LabelDeclaration],
//  entityDeclarations: List[EntityDeclaration] = List.empty,
//  patterns: List[BasicPattern] = List.empty
//) extends DdlAst

//case class NodeToTableMapping(
//  labelName: String,
//  tableName: String
//) extends DdlAst
//
//// Simplified version with only column as key per node
//case class IdMapping(
//  nodeLabel: Set[String],
//  nodePropertyName: String,
//  relPropertyName: String
//)
//
//case class RelationshipToTableMapping(
//  relName: String,
//  tableName: String,
//  startNodeIdMappings: List[IdMapping],
//  endNodeIdMappings: List[IdMapping]
//) extends DdlAst
//
//case class LabelsForTablesMapping(
//  nodeMappings: List[NodeToTableMapping],
//  relMappings: List[RelationshipToTableMapping]
//) extends DdlAst
