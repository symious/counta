package org.playground.counta.akka

import akka.actor.ActorSystem
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.testkit.{ TestKit, TestProbe }



class ActorHierarchyTests2 (_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll{

  def this() = this(ActorSystem("ActorHierarchyTests2"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "reply with empty reading if no temperature is known" in {
    val probe = TestProbe()
    val deviceActor = system.actorOf(Device.props("group", "device"))

    deviceActor.tell(Device.ReadTemperature(requestId = 42), probe.ref)
    val response = probe.expectMsgType[Device.RespondTemperature]
    response.requestId should ===(42L)
    response.value should ===(None)
  }

}