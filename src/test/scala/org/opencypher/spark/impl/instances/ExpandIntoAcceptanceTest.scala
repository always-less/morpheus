package org.opencypher.spark.impl.instances

import org.opencypher.spark.SparkCypherTestSuite
import org.opencypher.spark.api.value.CypherMap

import scala.collection.Bag

class ExpandIntoAcceptanceTest extends SparkCypherTestSuite {

  test("test expand into for dangling edge") {
    // Given
    val given = TestGraph(
      """
        |(p1:Person {name: "Alice"}),
        |(p2:Person {name: "Bob"}),
        |(p3:Person {name: "Eve"}),
        |(p4:Person {name: "Carl"}),
        |(p5:Person {name: "Richard"}),
        |(p1)-[:KNOWS]->(p2),
        |(p2)-[:KNOWS]->(p3),
        |(p1)-[:KNOWS]->(p3),
        |(p3)-[:KNOWS]->(p4),
        |(p3)-[:KNOWS]->(p5)
      """.stripMargin)

    // When
    val result = given.cypher(
      """
        |MATCH (p1:Person)-[e1:KNOWS]->(p2:Person),
        |(p2)-[e2:KNOWS]->(p3:Person),
        |(p1)-[e3:KNOWS]->(p3),
        |(p3)-[e4:KNOWS]->(p4)
        |RETURN p1.name, p2.name, p3.name, p4.name
      """.stripMargin)

    // Then
    result.records.toMaps should equal(Bag(
      CypherMap(
        "p1.name" -> "Alice",
        "p2.name" -> "Bob",
        "p3.name" -> "Eve",
        "p4.name" -> "Carl"
      ),
      CypherMap(
        "p1.name" -> "Alice",
        "p2.name" -> "Bob",
        "p3.name" -> "Eve",
        "p4.name" -> "Richard"
      )
    ))

    result.graph shouldMatch given.graph
  }

  test("test expand into for triangle") {
    // Given
    val given = TestGraph(
      """
        |(p1:Person {name: "Alice"}),
        |(p2:Person {name: "Bob"}),
        |(p3:Person {name: "Eve"}),
        |(p1)-[:KNOWS]->(p2),
        |(p2)-[:KNOWS]->(p3),
        |(p1)-[:KNOWS]->(p3),
      """.stripMargin)

    // When
    val result = given.cypher(
      """
        |MATCH (p1:Person)-[e1:KNOWS]->(p2:Person),
        |(p2)-[e2:KNOWS]->(p3:Person),
        |(p1)-[e3:KNOWS]->(p3)
        |RETURN p1.name, p2.name, p3.name
      """.stripMargin)

    // Then
    result.records.toMaps should equal(Bag(
      CypherMap(
        "p1.name" -> "Alice",
        "p2.name" -> "Bob",
        "p3.name" -> "Eve"
      )
    ))

    result.graph shouldMatch given.graph
  }

  test("Expand into after var expand") {
    // Given
    val given = TestGraph(
      """
        |(p1:Person {name: "Alice"}),
        |(p2:Person {name: "Bob"}),
        |(comment:Comment),
        |(post1:Post {content: "asdf"}),
        |(post2:Post {content: "foobar"}),
        |(p1)-[:KNOWS]->(p2),
        |(p2)<-[:HASCREATOR]-(comment),
        |(comment)-[:REPLYOF]->(post1)-[:REPLYOF]->(post2),
        |(post2)-[:HASCREATOR]->(p1)
      """.stripMargin)

    // When
    val result = given.cypher(
      """
        |MATCH (p1:Person)-[e1:KNOWS]->(p2:Person),
        |      (p2)<-[e2:HASCREATOR]-(comment:Comment),
        |      (comment)-[e3:REPLYOF*1..10]->(post:Post),
        |      (p1)<-[:HASCREATOR]-(post)
        |WHERE p1.name = "Alice"
        |RETURN p1.name, p2.name, post.content
      """.stripMargin
    )

    // Then
    result.records.toMaps should equal(Bag(
      CypherMap(
        "p1.name" -> "Alice",
        "p2.name" -> "Bob",
        "post.content" -> "foobar"
      )
    ))
  }
}
