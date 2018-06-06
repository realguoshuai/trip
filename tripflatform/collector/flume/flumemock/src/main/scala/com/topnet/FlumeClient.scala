package com.topnet

import java.nio.charset.Charset

import org.apache.flume.api.RpcClientFactory
import org.apache.flume.event.EventBuilder

/**
  * Created with IDEA  
  * author 郭帅 
  * date 10:07 2018/4/19    
  **/
object FlumeClient {
  //使用客户端模拟流数据  用来测试
  val client = RpcClientFactory.getDefaultInstance("master",8888)

  def sendMsg(msg:String)={
    val event =EventBuilder.withBody(msg,Charset.forName("UTF-8"))
    client.append(event)
  }

  def close()={
    if (client!=null){
      client.close()
    }
  }
}
