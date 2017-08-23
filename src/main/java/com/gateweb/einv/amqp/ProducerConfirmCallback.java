/**
 * 
 */
package com.gateweb.einv.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @author mac
 *
 */
public class ProducerConfirmCallback implements ConfirmCallback {

	/* (non-Javadoc)
	 * @see org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback#confirm(org.springframework.amqp.rabbit.support.CorrelationData, boolean, java.lang.String)
	 */

	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		
		System.out.println("correlationData :"  + correlationData +", ack:   "+ ack+" , cause:  "+cause);
		 /*if (ack) {
			 System.out.println("message can send to Queue success : "+correlationData.getId()+"    cause:"+cause);
		 } else {
			 System.out.println("message can send to Queue fail : "+correlationData.getId()+"    cause:"+cause);
		 }*/
		
	}
}
