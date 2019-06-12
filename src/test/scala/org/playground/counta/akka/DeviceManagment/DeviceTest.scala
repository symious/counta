package org.playground.counta.akka.DeviceManagment

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class DeviceTest(_system: ActorSystem) extends TestKit(_system)
      with Matchers
      with WordSpecLike
      with BeforeAndAfterAll{

  def this() = this(ActorSystem("DeviceTest"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "reply to registration requests" in {
    val probe = TestProbe()
    val deviceActor = system.actorOf(Device.props("group", "device"))

    deviceActor.tell(DeviceManager.RequestTrackDevice("group", "device"), probe.ref)
    probe.expectMsg(DeviceManager.DeviceRegistered)
    probe.lastSender should ===(deviceActor)
  }

  "ignore wrong registration requests" in {
    val probe = TestProbe()
    val deviceActor = system.actorOf(Device.props("group", "device"))

    deviceActor.tell(DeviceManager.RequestTrackDevice("wrongGroup", "device"), probe.ref)
    probe.expectNoMessage()

    deviceActor.tell(DeviceManager.RequestTrackDevice("group", "Wrongdevice"), probe.ref)
    probe.expectNoMessage()
  }

}
