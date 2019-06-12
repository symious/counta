package org.playground.counta.akka.DeviceManagment

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.duration._




object DeviceGroup{

  sealed trait TemperatureReading
  case class Temperature(value: Double) extends  TemperatureReading
  case object TemperatureNotAvailable extends TemperatureReading
  case object DeviceTimeout extends TemperatureReading

  case class RequestAllTemperatures(requestId: String)
  case class RespondAllTemperatures(requestId: String, replies: Map[String, TemperatureReading])

  def props(groupId: String): Props = Props(new DeviceGroup(groupId))
}

class DeviceGroup(groupId: String) extends Actor with ActorLogging {
  import DeviceManager._
  import DeviceGroup._
  var deviceIdToActor = Map.empty[String, ActorRef]
  var actorToDeviceId = Map.empty[ActorRef, String]

  override def preStart(): Unit = log.info("DeviceGroup {} started", groupId)
  override def postStop(): Unit = log.info("DeviceGroup {} stoped", groupId)

  override def receive: Receive = {
    case trackMsg @ RequestTrackDevice(`groupId`, _ ) =>
      deviceIdToActor.get(trackMsg.deviceId) match{
        case Some(deviceActor) =>
          deviceActor.forward(trackMsg)
        case None =>
          log.info("Creating device actor for {}", trackMsg.deviceId)
          val deviceActor = context.actorOf(Device.props(groupId, trackMsg.deviceId), "device-" + trackMsg.deviceId)
          deviceIdToActor += trackMsg.deviceId -> deviceActor
          deviceActor.forward(trackMsg)
      }
    case RequestTrackDevice(groupId, deviceId) =>
      log.warning("Ignoring TrackDevice request for {}. This actor is responsible for {}.", groupId, this.groupId)
    case RequestAllTemperatures(requestId) =>
      context.actorOf(DeviceGroupQuery.props(actorToDeviceId, requestId, sender(), 3.seconds))
  }

}