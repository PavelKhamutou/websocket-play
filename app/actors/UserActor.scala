package actors


import akka.actor.{ActorLogging, Props, ActorRef, Actor}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import akka.event.LoggingReceive

/**
  * Created by pk on 3/9/16.
  */
case class Message(msg: String)

object UserActor {
  def props(roomId: Int, name: String)(out: ActorRef) = Props(new UserActor(roomId, name, out))
}

class UserActor(roomId: Int, name: String, out: ActorRef) extends Actor with ActorLogging {


  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(roomId.toString, self)
  log.info(s"User [$name] joined chat room [$roomId]")


  override def receive: Receive = LoggingReceive {
    case msg: String =>
      log.info(s"Message [$msg] is received from web socket input chanel. This message will be broadcasted")
      val message = s"[$roomId][$name]: $msg"
      mediator ! Publish(roomId.toString, Message(message))
    case Message(msg) =>
      log.info(s"Message [$msg] is received from mediator. This message will be redirected to web socket output chanel")
      out ! msg
  }
}
