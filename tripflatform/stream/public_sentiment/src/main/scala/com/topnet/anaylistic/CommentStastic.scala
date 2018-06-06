package com.topnet.anaylistic

import java.text.SimpleDateFormat
import java.util.Locale

import com.topnet.GetStreamingFromKafka
import org.apache.spark.streaming.Duration

/**
  * Created with IDEA  
  * author 郭帅 
  * date 12:55 2018/4/19    
  **/
object CommentStastic {
  val streamUtil = new GetStreamingFromKafka("commentanay", "local[*]", Duration(5000))
  //维度:时间(天) ,酒店,平台

  def commentNum() = {
    //计算累计评论数
    val dstream = streamUtil.getKafkaStreaming(List("comment"), "comment_cnum")
    //dstream.print()

    val commentNumDstream = dstream.map(x => {
      val regex = "(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)".r
      val format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
      val format1 = new SimpleDateFormat("yyyyMMdd")
      x match {
        case regex(cDate, cName, hotelId, platForm, title, content, score, score_s, score_l, score_a, score_f, url) => Some(format1.format(format.parse(cDate)), hotelId, platForm)
        case _ => None
      }
    }).filter(_ != None)
      .map(x => (x, 1))
      .updateStateByKey((values: Seq[Int], state: Option[Int]) => {
        val batSum = if (values.size > 0) values.sum else 0
        state match {
          case Some(old) => Some(old + batSum)
          case None => Some(batSum)
        }
      })
    commentNumDstream.print(2000)
    streamUtil.ssc.start()
    streamUtil.ssc.awaitTermination()
  }

  /*  //维度下每天的好评数
  def goodComment()={
    val dstream = streamUtil.getKafkaStreaming(List("comment"), "comment_cnum")

    val goodCommentNumDstream = dstream.map(x => {
      val regex = "(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)".r
      val format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
      val format1 = new SimpleDateFormat("yyyyMMdd")
      x match {
        case regex(cDate, cName, hotelId, platForm, title, content, score, score_s, score_l, score_a, score_f, url) => Some(format1.format(format.parse(cDate)), hotelId, platForm,score)
        case _ => None
      }
    }).filter(_ != None).map(x=>(((x.get._1,x.get._2,x.get._3),x.get._4.toInt),1))
      .updateStateByKey((values: Seq[Int], state: Option[Int]) => {
        val batSum = if (values.size > 0) values.sum else 0
        state match {
          case Some(old) => Some(old + batSum)
          case None => Some(batSum)
        }
      })
    goodCommentNumDstream.print(20)
    streamUtil.ssc.start()
    streamUtil.ssc.awaitTermination()
  }*/
  def main(args: Array[String]): Unit = {
    commentNum()
    //goodComment
  }
}
