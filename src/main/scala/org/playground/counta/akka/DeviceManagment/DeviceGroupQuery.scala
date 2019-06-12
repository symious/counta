package org.playground.counta.akka.DeviceManagment

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}

import scala.concurrent.duration.FiniteDuration

object DeviceGroupQuery {
  case object CollectionTimeOut

  def props(actorToDeviceId: Map[ActorRef, String],
            requestId: String,
            requester: ActorRef,
            timeout: FiniteDuration): Props =
     Props(new DeviceGroupQuery(actorToDeviceId, requestId, requester, timeout))

}

class DeviceGroupQuery(actorToDeviceId: Map[ActorRef, String],
                       requestId: String,
                       requester: ActorRef,
                       timeout: FiniteDuration) extends Actor with ActorLogging{
  import DeviceGroupQuery._
  import context.dispatcher

  val queryTimeoutTimer = context.system.scheduler.scheduleOnce(timeout, self, CollectionTimeOut)

  override def preStart(): Unit = {
    actorToDeviceId.keysIterator.foreach { deviceActor =>
      context.watch(deviceActor)
      deviceActor ! Device.ReadTemperature(0)
    }
  }

  override def postStop(): Unit = {
    queryTimeoutTimer.cancel()
  }

  override def receive: Receive = waitingForReplies(Map.empty, actorToDeviceId.keySet)

  def waitingForReplies(repliesSoFar: Map[String, DeviceGroup.TemperatureReading], stillWaiting: Set[ActorRef]): Receive = {
    case Device.RespondTemperature(0, valueOption) =>{
      val deviceActor = sender()
      val reading = valueOption match {
        case Some(value) => DeviceGroup.Temperature(value)
        case None => DeviceGroup.TemperatureNotAvailable
      }
      receivedResponse(deviceActor, reading, stillWaiting, repliesSoFar)
    }
    case Terminated(deviceActor) =>
      receivedResponse(deviceActor, DeviceGroup.TemperatureNotAvailable, stillWaiting, repliesSoFar)
    case CollectionTimeOut =>
      val timeOutReplies = {
        stillWaiting.map{ deviceActor =>
          val deviceId = actorToDeviceId(deviceActor)
          deviceId -> DeviceGroup.DeviceTimeout
        }
      }
      requester ! DeviceGroup.RespondAllTemperatures(requestId, repliesSoFar ++ timeOutReplies)
      context.stop(self)
  }

  def receivedResponse(deviceActor: ActorRef,
                       reading: DeviceGroup.TemperatureReading,
                       stillWaiting: Set[ActorRef],
                       repliesSoFar: Map[String, DeviceGroup.TemperatureReading]) = {
    context.unwatch(deviceActor)
    val deviceId = actorToDeviceId(deviceActor)
    val newStillWaiting = stillWaiting - deviceActor
    val newRepliesSoFar = repliesSoFar + (deviceId -> reading)
    if(newRepliesSoFar.isEmpty){
      requester ! DeviceGroup.RespondAllTemperatures(requestId, newRepliesSoFar)
      context.stop(self)
    }else{
      context.become(waitingForReplies(newRepliesSoFar, newStillWaiting))
    }


  }
}
