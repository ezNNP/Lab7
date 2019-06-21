import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class Authorization {

    private Client client;

    public Authorization(Client client) {
        this.client = client;
    }

    public void createWindow() {
        // TODO: 2019-05-20 Создать отдельный класс, в котором будем подгружать properties по енаму
        ResourceBundle properties = PropLoader.readProperties(PropLoader.Language.RU);

        // Initializing strings from property file
        String titleString = properties.getString("AuthorizeTitle");
        String enterString = properties.getString("Enter");
        String registerString = properties.getString("Register");
        String loginString = properties.getString("Login");
        String passwordString = properties.getString("Password");
        String emailString = properties.getString("Email");
        String tokenString = properties.getString("Token");
        String confirmRegistrationString = properties.getString("ConfirmRegistration");
        String back = properties.getString("Back");

        // Initializing labels and buttons for login tab
        JButton enterButton = new JButton(enterString);
        JLabel loginLabel = new JLabel(loginString);
        JLabel passwordLabel = new JLabel(passwordString);
        JLabel registerLabel = new JLabel(registerString);
        JLabel enterLabel = new JLabel(enterString);

        JTextField loginField = new JTextField(50);
        JTextField emailField = new JTextField(50);
        JPasswordField passwordField = new JPasswordField(50);

        // Initializing labels and buttons for register tab
        JButton registerButton2 = new JButton(registerString);
        JButton confirmRegisterButton2 = new JButton(confirmRegistrationString);
        JButton backToMenuButton2 = new JButton(back);
        JLabel emailLabel2 = new JLabel(emailString);
        JLabel loginLabel2 = new JLabel(loginString);
        JLabel passwordLabel2 = new JLabel(passwordString);
        JLabel tokenLabel2 = new JLabel(tokenString);

        JTextField emailField2 = new JTextField(50);
        JTextField loginField2 = new JTextField(50);
        JPasswordField passwordField2 = new JPasswordField(50);
        JTextField tokenField2 = new JTextField(50);




        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        emailLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        loginLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        passwordLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        tokenLabel2.setHorizontalAlignment(SwingConstants.RIGHT);



        // Initializing jframe
        JFrame jFrame = new JFrame(titleString);
        jFrame.setSize(600, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        JTabbedPane tab = new JTabbedPane();



        JPanel loginPanel = new JPanel();
        jFrame.add(tab);

        GridBagLayout layout = new GridBagLayout();
        loginPanel.setLayout(layout);
        tab.addTab(loginString, loginPanel);

        Panel registerPanel = new Panel();
        registerPanel.removeAll();
        registerPanel.repaint();
        GridBagLayout layout2 = new GridBagLayout();
        registerPanel.setLayout(layout2);
        tab.addTab(registerString, registerPanel);

        // Creating elements and adding them to the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 15,15, 15);
        gbc.anchor = GridBagConstraints.NORTH;
        enterLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        enterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(enterLabel, gbc);

        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(loginLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        loginPanel.add(loginField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        loginPanel.add(enterButton, gbc);


        // Init register panel
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        registerPanel.add(registerLabel, gbc);

        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(emailLabel2, gbc);

        gbc.gridy = 2;
        registerPanel.add(loginLabel2, gbc);

        gbc.gridy = 3;
        registerPanel.add(passwordLabel2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        registerPanel.add(emailField2, gbc);

        gbc.gridy = 2;
        registerPanel.add(loginField2, gbc);

        gbc.gridy = 3;
        registerPanel.add(passwordField2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        registerButton2.setHorizontalAlignment(SwingConstants.CENTER);
        registerPanel.add(registerButton2, gbc);



        enterButton.addActionListener(e -> {
            // TODO: 2019-05-20 Вход
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());
            if (validateField(loginField) && validateField(passwordField)) {
                String data = login + "\uE000" + password;
                System.out.println(data);
                client.sender.sendCommand("login", data);
                Response response = client.plannedReceiver.listenServer();
                if (response.getStatus() == Status.OK) {
                    client.setToken(Response.getStringFromResponse(response.getResponse()));
                    client.login = login;
                    MainWindow window = new MainWindow(client);
                    window.createWindow();
                    jFrame.setVisible(false);
                } else if (response.getStatus() == Status.USER_NOT_FOUND) {
                    JOptionPane.showMessageDialog(jFrame, properties.getString("USER_NOT_FOUND"), properties.getString("Error"), JOptionPane.ERROR_MESSAGE);
                } else if (response.getStatus() == Status.USER_IN_SYSTEM) {
                    JOptionPane.showMessageDialog(jFrame, properties.getString("USER_IN_SYSTEM"), properties.getString("Error"), JOptionPane.ERROR_MESSAGE);
                } else if (response.getStatus() == Status.WRONG_PASSWORD) {
                    JOptionPane.showMessageDialog(jFrame, properties.getString("WRONG_PASSWORD"), properties.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(jFrame, properties.getString("FillFields"), properties.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton2.addActionListener(e -> {
            String email = emailField2.getText();
            String login = loginField2.getText();
            String password = new String(passwordField2.getPassword());
            String data = email + "\uE000" + login + "\uE000" + password;
            client.sender.sendCommand("register", data);
            Response response = client.plannedReceiver.listenServer();
            final String token;
            if (response.getStatus() == Status.OK) {
                token = Response.getStringFromResponse(response.getResponse());
                JOptionPane.showMessageDialog(jFrame, properties.getString("CheckEmail"), properties.getString("CompleteRegistration"), JOptionPane.QUESTION_MESSAGE);
            } else if (response.getStatus() == Status.USER_EXIST) {
                token = "";
                JOptionPane.showMessageDialog(jFrame, properties.getString("UserExist"), properties.getString("Error"), JOptionPane.ERROR_MESSAGE);
            } else {
                token = "";
            }

            if (response.getStatus() == Status.OK) {
                registerPanel.removeAll();
                registerPanel.repaint();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 0;
                registerPanel.add(tokenLabel2, gbc);
                gbc.gridx = 1;
                gbc.weightx = 0.4;
                gbc.gridwidth = 2;
                registerPanel.add(tokenField2, gbc);
                gbc.gridy = 1;
                gbc.gridwidth = 1;
                gbc.weightx = 0;
                registerPanel.add(confirmRegisterButton2, gbc);
                gbc.gridx = 2;
                registerPanel.add(backToMenuButton2, gbc);
                confirmRegisterButton2.addActionListener(t -> {
                    if (token.equals(tokenField2.getText())) {
                        JOptionPane.showMessageDialog(jFrame, properties.getString("CorrectToken"), properties.getString("Complete"), JOptionPane.PLAIN_MESSAGE);
                        client.sender.sendCommand("acceptregister", data + "\uE000" + tokenField2.getText());
                        createWindow();
                    } else {
                        JOptionPane.showMessageDialog(jFrame, properties.getString("IncorrectToken"), properties.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    }
                });

                backToMenuButton2.addActionListener(t -> {
                    createWindow();
                });

            }
        });
        registerPanel.revalidate();
        loginPanel.revalidate();
    }

    private boolean validateField(JTextField field) {
        if (field.getText().trim().equals(""))
            return false;
        else
            return true;
    }

}