package commonDataStructures

import twitter4j.Status
import java.util.concurrent.LinkedBlockingQueue
import twitter4j._
import twitter4j.StatusDeletionNotice
import commonDataStructures._

/**
 * Created by benkio on 02/08/15.
 */
class MyStatusListener(queue: LinkedBlockingQueue[Tweet]) extends StatusListener{
  override def onStatus(status:Status) {
    queue.offer(Tweet(Author(status.getUser.getScreenName), status.getCreatedAt.getTime, status.getText))
  }

  override def onDeletionNotice(sdn: StatusDeletionNotice) {}
  override def onTrackLimitationNotice(i: Int) {}
  override def onScrubGeo(l: Long, l1:Long) {}
  override def onException(ex: Exception) {}
  override def onStallWarning(arg0: StallWarning) {}
}
