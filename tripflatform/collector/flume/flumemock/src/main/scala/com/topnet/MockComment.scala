package com.topnet

import java.util.{Date, UUID}

import scala.util.Random

/**
  * Created with IDEA  
  * author 郭帅 
  * date 9:52 2018/4/19    
  **/
object MockComment {
  val random = new Random()
  val platForms =Array("ctrip","elong","qunar","tongcheng",
  "tuniu","taobao","meituan")

  def getOneComment() = {
    val cDate = new Date() //评论时间
    val cName = s"customer_${random.nextString(random.nextInt(10) + 1)}" //评论者昵称
    val hotelId =random.nextInt(100)+1 //酒店id:1-100之间随机
    val platForm = platForms(random.nextInt(platForms.length)) //随机平台
    val title = random.nextString(5+random.nextInt(10)) //标题
    val content = random.nextString(20+random.nextInt(50)) //内容

    val score =1+random.nextInt(5) //1-5随机 总分
    val score_s =1+random.nextInt(5) //1-5随机 服务
    val score_l =1+random.nextInt(5) //1-5随机 位置
    val score_a =1+random.nextInt(5) //1-5随机 设施
    val score_f =1+random.nextInt(5) //1-5随机 餐椅
    val url =s"https://+${UUID.randomUUID()}" //来源地址
    s"${cDate}|${cName}|${hotelId}|${platForm}|${title}|${content}|${score}|${score_s}|${score_l}|${score_a}|${score_f}"
  }

  def main(args: Array[String]): Unit = {
    while(true){
      val msg =getOneComment()
      println(msg)
      FlumeClient.sendMsg(msg)
      Thread.sleep(1000*(random.nextInt(5))+1)
    }
  }
}
