package com.example.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.order.model.Order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author dpescatore
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	/**
	 * Get a list of order if present
	 */
	@Test
	public void testGetAllOrders() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/api/v1/orders", HttpMethod.GET, entity,
				String.class);
		Assertions.assertNotNull(response.getBody());
	}

	/**
	 * Create an order
	 */
	@Test
	public void testCreateOrder() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Food);

		String url = getRootUrl() + "/api/v1/orders";
		ResponseEntity<Order> postResponse = restTemplate.postForEntity(url, order, Order.class);
		Assertions.assertTrue(postResponse.getBody().getId() != null);
	}

	/**
	 * Create an order and get it by assigned Id
	 */
	@Test
	public void testGetOrderById() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Food);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();
		Order readOrder = restTemplate.getForObject(getRootUrl() + "/api/v1/orders/" + id, Order.class);
		System.out.println(readOrder.getName());
		Assertions.assertEquals(id, readOrder.getId());
	}

	/**
	 * Create an order in unscheduled state
	 */
	@Test
	public void testUnscheduledState() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setPhotoType(Order.PhotoType.Food);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();
		Order readOrder = restTemplate.getForObject(getRootUrl() + "/api/v1/orders/" + id, Order.class);
		System.out.println(readOrder.getState());
		Assertions.assertEquals(Order.State.UNSCHEDULED, readOrder.getState(), "State should be UNSCHEDULED");
	}

	/**
	 * Create an order and schedule it
	 */
	@Test
	public void testSchedule() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setPhotoType(Order.PhotoType.Events);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();

		String url = getRootUrl() + "/api/v1/orders/" + id + "/schedule";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> param = new HashMap<String, String>();
		HttpEntity<Date> requestEntity = new HttpEntity<Date>(new Date(), headers);
		HttpEntity<Order> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Order.class, param);

		Order saveOrder = response.getBody();

		Assertions.assertEquals(Order.State.PENDING, saveOrder.getState(), "State should be PENDING");
	}

	/**
	 * Assign an order to a photograph
	 */
	@Test
	public void testAssign() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Events);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();

		String url = getRootUrl() + "/api/v1/orders/" + id + "/assign";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> param = new HashMap<String, String>();
		HttpEntity<Order> response = restTemplate.exchange(url, HttpMethod.POST, null, Order.class, param);

		Order saveOrder = response.getBody();

		Assertions.assertTrue(
				(Order.State.ASSIGNED.equals(saveOrder.getState()) && saveOrder.getPhotographerId() != null),
				"State should be ASSIGNED with an ID of a Photographer");

	}

	/**
	 * Cancel an order in ASSIGNED state
	 */
	@Test
	public void testCancel() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Events);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();

		String url = getRootUrl() + "/api/v1/orders/" + id + "/assign";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> param = new HashMap<String, String>();
		restTemplate.exchange(url, HttpMethod.POST, null, Order.class, param);

		url = getRootUrl() + "/api/v1/orders/" + id + "/cancel";

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		param = new HashMap<String, String>();
		HttpEntity<Order> response = restTemplate.exchange(url, HttpMethod.POST, null, Order.class, param);

		Order saveOrder = response.getBody();

		Assertions.assertEquals(Order.State.CANCELED, saveOrder.getState(), "State should be CANCELED");

	}

	/**
	 * Upload photo for an order
	 */
	@Test
	public void testUpload() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Events);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();

		String url = getRootUrl() + "/api/v1/orders/" + id + "/assign";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> param = new HashMap<String, String>();
		restTemplate.exchange(url, HttpMethod.POST, null, Order.class, param);

		url = getRootUrl() + "/api/v1/orders/" + id + "/upload";

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		param = new HashMap<String, String>();
		HttpEntity<String> requestEntity = new HttpEntity<String>("BASE64CONTENTSTRING", headers);

		HttpEntity<Order> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Order.class, param);

		Order saveOrder = response.getBody();

		Assertions.assertEquals(Order.State.UPLOADED, saveOrder.getState(), "State should be UPLOADED");

	}

	/**
	 * Accept photos for an order putting his state to COMPLETED
	 */
	@Test
	public void testAccept() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Events);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();

		String url = getRootUrl() + "/api/v1/orders/" + id + "/assign";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> param = new HashMap<String, String>();
		restTemplate.exchange(url, HttpMethod.POST, null, Order.class, param);

		url = getRootUrl() + "/api/v1/orders/" + id + "/upload";

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		param = new HashMap<String, String>();
		HttpEntity<String> requestEntity = new HttpEntity<String>("BASE64CONTENTSTRING", headers);

		restTemplate.exchange(url, HttpMethod.POST, requestEntity, Order.class, param).getBody();

		url = getRootUrl() + "/api/v1/orders/" + id + "/accept";

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		param = new HashMap<String, String>();
		HttpEntity<Order> response = restTemplate.exchange(url, HttpMethod.POST, null, Order.class, param);

		Order saveOrder = response.getBody();
		Assertions.assertEquals(Order.State.COMPLETED, saveOrder.getState(), "State should be COMPLETED");

	}

	/**
	 * Try to accept a not uploaded order. Server returns a bad request.
	 */
	@Test
	public void testAcceptNotUploaded() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Events);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();

		String url = getRootUrl() + "/api/v1/orders/" + id + "/accept";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> param = new HashMap<String, String>();
		String response = restTemplate.exchange(url, HttpMethod.POST, null, String.class, param).getBody();

		Assertions.assertEquals("Cannot accept an order that is not in UPLOADED state.", response);

	}

	/**
	 * Try to reject a not uploaded order. Server returns a bad request.
	 */
	@Test
	public void testRejectNotUploaded() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Events);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();

		String url = getRootUrl() + "/api/v1/orders/" + id + "/reject";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> param = new HashMap<String, String>();
		String response = restTemplate.exchange(url, HttpMethod.POST, null, String.class, param).getBody();

		Assertions.assertEquals("Cannot reject an order that is not in UPLOADED state.", response);

	}

	/**
	 * Reject an order in UPLOADED state
	 */
	@Test
	public void testReject() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Events);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();

		String url = getRootUrl() + "/api/v1/orders/" + id + "/assign";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> param = new HashMap<String, String>();
		restTemplate.exchange(url, HttpMethod.POST, null, Order.class, param);

		url = getRootUrl() + "/api/v1/orders/" + id + "/upload";

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		param = new HashMap<String, String>();
		HttpEntity<String> requestEntity = new HttpEntity<String>("BASE64CONTENTSTRING", headers);

		restTemplate.exchange(url, HttpMethod.POST, requestEntity, Order.class, param).getBody();

		url = getRootUrl() + "/api/v1/orders/" + id + "/reject";

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		param = new HashMap<String, String>();
		HttpEntity<Order> response = restTemplate.exchange(url, HttpMethod.POST, null, Order.class, param);

		Order saveOrder = response.getBody();
		Assertions.assertEquals(Order.State.ASSIGNED, saveOrder.getState(), "State should be ASSIGNED");

	}

	/**
	 * Create an order in PENDING state
	 */
	@Test
	public void testPendingState() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Food);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();
		Order readOrder = restTemplate.getForObject(getRootUrl() + "/api/v1/orders/" + id, Order.class);
		System.out.println(readOrder.getState());
		Assertions.assertEquals(Order.State.PENDING, readOrder.getState(), "State should be PENDING");
	}

	/**
	 * Create and update an order
	 */
	@Test
	public void testUpdatePut() {

		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Food);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Order saved = postResponse.getBody();
		Long id = saved.getId();

		saved.setName("admin2");
		restTemplate.put(getRootUrl() + "/api/v1/orders/" + id, saved);

		HttpHeaders headers = new HttpHeaders();
		Map<String, String> param = new HashMap<String, String>();
		HttpEntity<Order> requestEntity = new HttpEntity<Order>(saved, headers);
		HttpEntity<Order> response = restTemplate.exchange(getRootUrl() + "/api/v1/orders/" + id, HttpMethod.PUT,
				requestEntity, Order.class, param);

		Order updatedOrder = response.getBody();
		Assertions.assertEquals("admin2", updatedOrder.getName());
	}

	/**
	 * Delete an order
	 */
	@Test
	public void testDelete() {
		Order order = new Order();
		order.setEmail("admin@gmail.com");
		order.setName("admin");
		order.setSurname("admin");
		order.setCellNumber("33312312312");
		order.setDateTime(new Date());
		order.setPhotoType(Order.PhotoType.Food);

		ResponseEntity<Order> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/orders", order,
				Order.class);
		Long id = postResponse.getBody().getId();

		restTemplate.delete(getRootUrl() + "/api/v1/orders/" + id);

		try {
			order = restTemplate.getForObject(getRootUrl() + "/api/v1/orders/" + id, Order.class);
		} catch (final HttpClientErrorException e) {
			Assertions.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
		}
	}

}
