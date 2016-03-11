package actors


import akka.actor._
import akka.cluster._
import akka.cluster.ClusterEvent._
import akka.event.LoggingReceive

/**
  * Created by pk on 3/10/16.
  */
class ChatRoom extends Actor with ActorLogging {

  val cluster = Cluster(context.system)
  private var nodes = Set.empty[Address]

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }
  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive: Receive = LoggingReceive {
    case MemberUp(member) =>
      nodes += member.address
      log.info("Member is Up: {}. {} nodes in cluster", member.address, nodes.size)
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      nodes -= member.address
      log.info("Member is Removed: {} after {}. {} nodes in cluster",
        member.address, previousStatus, nodes.size)
    case _: MemberEvent => // ignore
  }
}
