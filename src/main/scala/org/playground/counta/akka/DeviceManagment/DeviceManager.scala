package org.playground.counta.akka.DeviceManagment

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}

object DeviceManager {

  def props(): Props = Props(new DeviceManager)

  final case class RequestTrackDevice(groupId: String, deviceId: String)
  case object DeviceRegistered
}

class DeviceManager extends Actor with ActorLogging {
  import DeviceManager._
  var groupIdToActor = Map.empty[String, ActorRef]
  var actorToGroupId = Map.empty[ActorRef, String]

  override def preStart(): Unit = log.info("DeviceManager started")

  override def postStop(): Unit = log.info("DeviceManager stopped")

  override def receive: Receive = {
    case trackMsg @ RequestTrackDevice(groupId, _) =>
      groupIdToActor.get(groupId) match{
        case Some(actorRef) =>
          actorRef.forward(trackMsg)
        case None =>
          log.info("Create actor for device group {}", groupId)
          val groupActor = context.actorOf(DeviceGroup.props(groupId), "group-" + groupId)
          context.watch(groupActor)
          groupActor.forward(trackMsg)
          groupIdToActor += groupId -> groupActor
          actorToGroupId += groupActor -> groupId
      }
    case Terminated(groupActor) =>
      var groupId = actorToGroupId(groupActor)
      log.info("Device group actor for {} has been terminated", groupId)
      groupIdToActor -= groupId
      actorToGroupId -= groupActor

  }

}
