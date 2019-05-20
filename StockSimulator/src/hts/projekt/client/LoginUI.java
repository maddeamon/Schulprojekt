package hts.projekt.client;

import static com.google.gwt.dom.client.Style.Unit.PCT;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class represents the UI responsible for the login page.
 */
public class LoginUI extends LayoutPanel {

	private TextBox username;

	private PasswordTextBox password;

	private Button login;

	private Button signUp;

	private static LoginUI INSTANCE;

	private LoginUI() {
		initializePanel();
	}

	public static LoginUI getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LoginUI();
		}

		return INSTANCE;
	}

	private void initializePanel() {
		VerticalPanel verticalPanel = new VerticalPanel();

		username = new TextBox();
		username.setText("username");
		username.setAlignment(ValueBoxBase.TextAlignment.CENTER);

		password = new PasswordTextBox();
		password.setText("password");
		password.setAlignment(ValueBoxBase.TextAlignment.CENTER);

		login = new Button("login");
		login.setEnabled(true);
		login.addClickHandler(clickEvent -> StockSimulator.login(username.getText(), password.getText()));

		signUp = new Button("sign up");
		signUp.setEnabled(true);
		signUp.addClickHandler(clickEvent -> StockSimulator.signUp(username.getText(), password.getText()));

		verticalPanel.add(username);
		verticalPanel.add(password);
		verticalPanel.add(login);
		verticalPanel.add(signUp);
		verticalPanel.getElement().getStyle().setBackgroundColor("#6CDAE7");

		add(verticalPanel);
		setWidgetTopHeight(verticalPanel, 30, PCT, 30, PCT);
		setWidgetLeftWidth(verticalPanel, 30, PCT, 30, PCT);
		setWidgetRightWidth(verticalPanel, 30, PCT, 30, PCT);
		getElement().getStyle().setBackgroundColor("red");
	}
}
