/**
 * 
 */
package com.gateweb.einv.amqp;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.gateweb.einv.model.User;
import com.gateweb.einv.service.EinvFacade;
/**
 * @author mac
 *
 */
public class PriorityConsumer implements MessageListener {


	@Autowired
	EinvFacade einvFacade;
	
	public void onMessage(Message message) {
		System.out.println("PriorityConsumer Received onMessage: " + message);
		try {
			//Thread.sleep(1000);
			System.out.println("PriorityConsumer UserID:"+new Long(new String(message.getBody()))+", priority: "+message.getMessageProperties().getPriority());
			//System.out.println("PriorityConsumer Received message Properties: "+message.getMessageProperties());
			User user = einvFacade.findUserById(new Long(new String(message.getBody())));
			System.out.println("User:"+user.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
        //System.out.println("Text: " + new String(message.getBody()));
		
	}
    
    public void listen(Message message) {
		System.out.println("UserID:"+new Long(new String(message.getBody())));

    	System.out.println("Received message Properties: "+message.getMessageProperties());
        System.out.println("Received listen m: " + message);
        System.out.println("Text: " + new String(message.getBody()));
    }
    
    public void listen(String message) {
        System.out.println("Received listen s: " + message);
        //System.out.println("Text: " + message);
    }

	
}