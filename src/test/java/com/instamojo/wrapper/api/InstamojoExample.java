package test.java.com.instamojo.wrapper.api;

import main.java.com.instamojo.wrapper.api.Instamojo;
import main.java.com.instamojo.wrapper.api.InstamojoImpl;
import main.java.com.instamojo.wrapper.exception.ConnectionException;
import main.java.com.instamojo.wrapper.exception.InvalidPaymentOrderException;
import main.java.com.instamojo.wrapper.exception.InvalidRefundException;
import main.java.com.instamojo.wrapper.response.CreatePaymentOrderResponse;
import main.java.com.instamojo.wrapper.response.CreateRefundResponse;
import main.java.com.instamojo.wrapper.response.PaymentOrderDetailsResponse;
import main.java.com.instamojo.wrapper.response.PaymentOrderListResponse;
import main.java.come.instamojo.wrapper.model.PaymentOrder;
import main.java.come.instamojo.wrapper.model.PaymentOrderFilter;
import main.java.come.instamojo.wrapper.model.Refund;

import java.util.logging.Level;
import java.util.logging.Logger;

public class InstamojoExample {

	private static final Logger LOGGER = Logger.getLogger(InstamojoExample.class.getName());

	public static void main(String args[]) throws InterruptedException {

		/*
		 * Create a new payment order **************************************
		 */

		PaymentOrder order = new PaymentOrder();

		order.setName("John Smith");
		order.setEmail("aamirubaid@gmail.com");
		order.setPhone("9810786188");
		order.setCurrency("INR");
		order.setAmount(19D);
		order.setDescription("This is a test transaction.");
		order.setRedirectUrl("http://www.google.com");
		order.setWebhookUrl("http://testDemo.com/");
		order.setTransactionId("00000011");
		//String transacId=order.getTransactionId();

		Instamojo api = null;

		try {
			// gets the reference to the instamojo api
			api = InstamojoImpl.getApi("fMGagopvgZ2vEuh9dujYsbip5ILROVUrVUPxvi1F", "6DwZweZt70Vy8kDHQLjLmFZ2KAwlk31uelInYkLPgdnilEcfS94Z7fT5zKI7kP9LjaKu92XulNzGAZGGr69VANtMp86FOqk2gSbppnmmCopz1qsdZLSmhUuAiKGfSl2s", "https://test.instamojo.com/v2/", "https://test.instamojo.com/oauth2/token/");
			
			System.out.println(api);
		} catch (ConnectionException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

		boolean isOrderValid = order.validate();
		CreatePaymentOrderResponse createPaymentOrderResponse=null;

		if (isOrderValid) {
			try {
				createPaymentOrderResponse = api.createNewPaymentOrder(order);
				// print the status of the payment order.
				System.out.println(createPaymentOrderResponse.getMessage());
			} catch (InvalidPaymentOrderException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);

				if (order.isTransactionIdInvalid()) {
					System.out.println("Transaction id is invalid. This is mostly due to duplicate transaction id.");
				}
				if (order.isCurrencyInvalid()) {
					System.out.println("Currency is invalid.");
				}
			} catch (ConnectionException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
			}
		} else {
			// inform validation errors to the user.
			if (order.isTransactionIdInvalid()) {
				System.out.println("Transaction id is invalid.");
			}
			if (order.isAmountInvalid()) {
				System.out.println("Amount can not be less than 9.00.");
			}
			if (order.isCurrencyInvalid()) {
				System.out.println("Please provide the currency.");
			}
			if (order.isDescriptionInvalid()) {
				System.out.println("Description can not be greater than 255 characters.");
			}
			if (order.isEmailInvalid()) {
				System.out.println("Please provide valid Email Address.");
			}
			if (order.isNameInvalid()) {
				System.out.println("Name can not be greater than 100 characters.");
			}
			if (order.isPhoneInvalid()) {
				System.out.println("Phone is invalid.");
			}
			if (order.isRedirectUrlInvalid()) {
				System.out.println("Please provide valid Redirect url.");
			}

			if (order.isWebhookInvalid()) {
                System.out.println("Provide a valid webhook url");
            }
		}

		/*
		 * Get details of payment order when order id is given
		 */

		/*try {
			Thread.sleep(10000);
			PaymentOrderDetailsResponse paymentOrderDetailsResponse = api.getPaymentOrderDetails(createPaymentOrderResponse.getPaymentOrder().getId());

			if (paymentOrderDetailsResponse.getId() != null) {
				// print the status of the payment order.
				System.out.println(paymentOrderDetailsResponse.getStatus());
			} else {
				System.out.println("Please enter valid order id.");
			}
		} catch (ConnectionException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

		
		 * Get details of payment order when transaction id is given
		 

		try {
			Thread.sleep(10000);
			PaymentOrderDetailsResponse paymentOrderDetailsResponse = api.getPaymentOrderDetailsByTransactionId(transacId);

			if (paymentOrderDetailsResponse.getId() != null) {
				// print the status of the payment order.
				System.out.println(paymentOrderDetailsResponse.getStatus());
			} else {
				System.out.println("Please enter valid transaction id.");
			}
		} catch (ConnectionException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

		
		 * Get list of all payment orders
		 

		try {
			PaymentOrderFilter paymentOrderFilter = new PaymentOrderFilter();

			PaymentOrderListResponse paymentOrderListResponse = api.getPaymentOrderList(paymentOrderFilter);

			// Loop over all of the payment orders and print status of each
			// payment order.
			for (PaymentOrder paymentOrder : paymentOrderListResponse.getPaymentOrders()) {
				System.out.println("Result = " + paymentOrder.getStatus());
			}
			System.out.println(paymentOrderListResponse.getPaymentOrders());
		} catch (ConnectionException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}


		
		 Create a new refund **************************************
		 

		Refund refund = new Refund();
		refund.setPaymentId(createPaymentOrderResponse.getPaymentOrder().getId());
		refund.setStatus("refunded");
		refund.setType("RFD");
		refund.setBody("This is a test refund.");
		refund.setRefundAmount(9D);
		refund.setTotalAmount(10D);

		boolean isRefundValid = refund.validate();

		if (isRefundValid) {
			try {
				Thread.sleep(1000);
				CreateRefundResponse createRefundResponse = api.createNewRefund(refund);
				// print the status of the refund.
				System.out.println(createRefundResponse.getRefund().getStatus());
			} catch (InvalidRefundException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);

				if (refund.isTypeInvalid()) {
					System.out.println("type is invalid.");
				}
			} catch (ConnectionException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
			}
		} else {
			// inform validation errors to the user.
			if (refund.isPaymentIdInvalid()) {
				System.out.println("Payment id is invalid.");
			}
			if (refund.isTypeInvalid()) {
				System.out.println("Type is invalid.");
			}
			if (refund.isBodyInvalid()) {
				System.out.println("Body is invalid.");
			}
			if (refund.isRefundAmountInvalid()) {
				System.out.println("Refund amount is invalid.");
			}
		}
*/	}
}
