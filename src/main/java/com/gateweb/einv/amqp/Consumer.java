/**
 * 
 */
package com.gateweb.einv.amqp;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.gateweb.einv.model.Company;
import com.gateweb.einv.model.User;
import com.gateweb.einv.service.EinvFacade;
/**
 * @author mac
 *
 */
public class Consumer implements MessageListener {


	@Autowired
	EinvFacade einvFacade;
	
	/* (non-Javadoc)
	 * @see org.springframework.amqp.core.MessageListener#onMessage(org.springframework.amqp.core.Message)
	 */
	public void onMessage(Message message) {
		
		JsonMessageConverter jmc = new JsonMessageConverter();
		User u = (User)jmc.fromMessage(message);
		System.out.println("Received onMessage  : " + message);
		System.out.println("Received message Properties: "+message.getMessageProperties());
		System.out.println("Received onMessage u : " + u);
		try {
			//Thread.sleep(1000);
			System.out.println("UserID:"+u.getUserId());
			Company comp = einvFacade.findCompanyById(u.getCompanyId());
			System.out.println("User:"+comp.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("Text: " + new String(message.getBody()));
		
	}
    
    public void listen(Message message) {
    	System.out.println("Custom listen: " + message);
    	System.out.println("Received message Properties: "+message.getMessageProperties());
        System.out.println("Received listen m: " + message);
        System.out.println("Text: " + new String(message.getBody()));
    }
    
	
}