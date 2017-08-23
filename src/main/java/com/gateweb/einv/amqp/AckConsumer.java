/**
 * 
 */
package com.gateweb.einv.amqp;
import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.gateweb.einv.model.Company;
import com.gateweb.einv.model.User;
import com.gateweb.einv.service.EinvFacade;
/**
 * @author mac
 *
 */
public class AckConsumer implements ChannelAwareMessageListener {


	@Autowired
	EinvFacade einvFacade;
	
	/* (non-Javadoc)
	 * @see org.springframework.amqp.rabbit.core.ChannelAwareMessageListener#onMessage(org.springframework.amqp.core.Message, com.rabbitmq.client.Channel)
	 */
	public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
		try {
			JsonMessageConverter jmc = new JsonMessageConverter();
			User u = (User)jmc.fromMessage(message);
			//System.out.println("Received onMessage  : " + message);
			//System.out.println("Received message Properties: "+message.getMessageProperties());
			//System.out.println("Received onMessage u : " + u);
			
			//Thread.sleep(1000);
			//System.out.println("AckConsumer onMessage UserID:"+u.getUserId());
			System.out.println("AckConsumer onMessage companyID:"+u.getCompanyId());
			Company comp = einvFacade.findCompanyById(u.getCompanyId());
			System.out.println("AckConsumer onMessage company name:"+comp.getName());
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
                System.out.println("重傳message失敗, message: " + message.getBody());
                System.out.println("-------");
            }
		}
		
		try {
            //回傳給queue，說明這個已經ack了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
        	System.out.println("傳message ack 失敗, message: " + message.getBody());
        }

        //System.out.println("Text: " + new String(message.getBody()));
		
	}
    
    public void listen(Message message) {
    	System.out.println("Custom listen: " + message);
    	System.out.println("Received message Properties: "+message.getMessageProperties());
        System.out.println("Received listen m: " + message);
        System.out.println("Text: " + new String(message.getBody()));
    }

	
    
	
}