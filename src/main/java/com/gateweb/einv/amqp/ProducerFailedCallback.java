/**
 * 
 */
package com.gateweb.einv.amqp;

import org.apache.poi.util.StringUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;

/**
 * @author mac
 *
 */
public class ProducerFailedCallback implements ReturnCallback {

	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        if (null != message) {
           System.out.println("message can't send to Queue: "+message.getBody());
        }else {
        	System.out.println("message can't send to Queue because message is null");
        }
    }
}
