/**
 * 
 */
package com.gateweb.einv.amqp;
import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.gateweb.einv.service.EinvFacade;
/**
 * @author mac
 *
 */
public class BackConsumer implements ChannelAwareMessageListener {

	@Autowired
	EinvFacade einvFacade;
	
	/* (non-Javadoc)
	 * @see org.springframework.amqp.core.MessageListener#onMessage(org.springframework.amqp.core.Message)
	 */
	public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
		try {
			System.out.println("BackConsumer UserID:"+new Long(new String(message.getBody()))+", priority: "+message.getMessageProperties().getPriority());
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
                Thread.sleep(3000);
                //用ack表示失败，要求重新回到queue，讓下一個consumer來處理。
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                return;
            }
            catch (Exception ee) {
            	ee.printStackTrace();
                System.out.println("BackConsumer 重傳message失敗, message: " + message.getBody());
                System.out.println("-------");
            }
		}
		
		try {
            //回傳給queue，說明這個已經ack了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
        	System.out.println("BackConsumer 傳message ack 失敗, message: " + message.getBody());
        }

        System.out.println("BackConsumer Text: " + new String(message.getBody()));
		
	}
		
    
    public void listen(Message message) {
    	System.out.println("Custom listen: " + message);
    	System.out.println("Received message Properties: "+message.getMessageProperties());
        System.out.println("Received listen m: " + message);
        System.out.println("Text: " + new String(message.getBody()));
    }
    
	
}