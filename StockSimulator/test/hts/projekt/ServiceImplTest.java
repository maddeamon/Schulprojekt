package hts.projekt;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hts.projekt.server.ServiceImpl;
import hts.projekt.shared.Equity;
import hts.projekt.shared.User;

public class ServiceImplTest {

	private ServiceImpl service;

	@Before
	public void setUp() {
		service = new ServiceImpl();
		service.signUp("test", "test");
	}

	@After
	public void tearDown() {
		service.removeUser("test");
	}

	@Test
	public void test_login_Succesful() throws Exception {
		service.login("test", "test");
	}

	@Test(expected = Exception.class)
	public void test_login_Failure_WrongPassword() throws Exception {
		service.login("test", "test1");
	}

	public void test_getWallet_succesful() {
		service.getWallet(new User("test", null));
	}

	@Test
	public void test_getAllEquities_successful() {

		// when
		List<Equity> equities = service.getAllEquities();

		// then
		assertFalse(equities.isEmpty());
	}

	@Test
	public void test_buyEquity_successful() {

	}

	@Test
	public void test_sellEquity_successful() {

	}

}
