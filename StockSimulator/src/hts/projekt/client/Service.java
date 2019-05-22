package hts.projekt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import hts.projekt.shared.Equity;
import hts.projekt.shared.User;
import hts.projekt.shared.Wallet;

@RemoteServiceRelativePath("service")
public interface Service extends RemoteService {
	Wallet login(String username, String password) throws Exception;

	Wallet signUp(String username, String password) throws Exception;

	Wallet getWallet(User user);

	List<Equity> getAllEquities();

	Wallet buyEquity(Wallet wallet, String equityId) throws Exception;

	Wallet sellEquity(Wallet wallet, String equityId);

	Boolean triggerUpdatePrices();

}
