package utils

import commonDataStructures._

/**
 * @author parallels
 */
object StormTweetStepManager {
  def AddStormStep(source: StormTweet, componentId: String, taskIndex: Int): StormTweet = {
    StormTweet(source.tweet,source.stormSteps :+ StormStep(componentId,taskIndex))
  }
}