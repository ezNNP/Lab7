import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainWindow {
    private Client client;

    private static String HOLDER = "Language";
    private PropLoader.Language current = PropLoader.Language.RU;
    private int width, height, size;

    public MainWindow (Client client) {
        this.client = client;
        width = 3;
        height = 3;
        size = 20;
    }

    public void createWindow() {
        UIManager.put("Menu.disabledForeground", Color.BLACK);
        JFrame jFrame = new JFrame("LAB8_I_FUCK_THIS_SHIT");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(800, 400);
        jFrame.setVisible(true);
        jFrame.setLayout(new BorderLayout());


        ResourceBundle properties = PropLoader.readProperties(PropLoader.Language.RU);

        // Initializing menubar
        JMenuBar menu = new JMenuBar();
        jFrame.setJMenuBar(menu);

        Adder adder = new Adder(current, false);
        jFrame.add(adder, BorderLayout.EAST);
        Drawer drawer = new Drawer();
        jFrame.add(drawer, BorderLayout.PAGE_START);

        JMenu language = new JMenu(properties.getString("Language"));
        language.setSize(100, 30);
        menu.add(language);
        JMenuItem lRus = new JMenuItem(properties.getString("Russian"));
        JMenuItem lSer = new JMenuItem(properties.getString("Serbian"));
        JMenuItem lIt = new JMenuItem(properties.getString("Italian"));
        JMenuItem lEs = new JMenuItem(properties.getString("Spanish"));
        language.add(lRus);
        language.add(lSer);
        language.add(lIt);
        language.add(lEs);

        lRus.addActionListener(e -> {// TODO: 2019-05-21 Change language to russian
            current = PropLoader.Language.RU;
        });
        lSer.addActionListener(e -> {// TODO: 2019-05-21 Change language to serbian
            current = PropLoader.Language.SB;
        });
        lIt.addActionListener(e -> {// TODO: 2019-05-21 Change language to italian
            current = PropLoader.Language.IT;
        });
        lEs.addActionListener(e -> {// TODO: 2019-05-21 Change language to spanish
            current = PropLoader.Language.ES;
        });

        JMenu command = new JMenu(properties.getString("ExecuteCommand"));
        menu.add(command);
        JMenuItem cAdd = new JMenuItem(properties.getString("Add"));
        JMenuItem cImport = new JMenuItem(properties.getString("Import"));
        JMenuItem cAddIfMin = new JMenuItem(properties.getString("AddIfMin"));
        JMenuItem cShow = new JMenuItem(properties.getString("Show"));
        command.add(cAdd);
        command.add(cImport);
        command.add(cAddIfMin);
        command.add(cShow);
        cAdd.addActionListener(e -> {
            /*adder = new Adder(current, false);

            jFrame.add(adder);
            jFrame.repaint();*/
        });
        cImport.addActionListener(e -> {
            // TODO: 2019-05-22 Импорт
        });
        cAddIfMin.addActionListener(e -> {
            //adder = new Adder(current, true);
        });
        cShow.addActionListener(e -> {
            Shower shower = new Shower();
            shower.showCollection();
        });
        menu.add(Box.createHorizontalGlue());
        JMenu currentUser = new JMenu(properties.getString("Current") + ": " + client.login);
        currentUser.setEnabled(false);
        menu.add(currentUser);

        JMenu exit = new JMenu(properties.getString("Exit"));
        menu.add(exit);
        exit.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                client.finish();
                client.setToken("");
                client.login = "";
                Authorization authorization = new Authorization(client);
                authorization.createWindow();
                jFrame.setVisible(false);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        exit.addActionListener(e -> {
            client.finish();
        });
    }
    
    private class Adder extends JPanel {
        ResourceBundle rb;
        private boolean ifMin;
        Adder(PropLoader.Language current, boolean ifMin) {
            rb = PropLoader.readProperties(current); // TODO: 2019-06-02 Выбор языка
            this.ifMin = ifMin;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            addElement();
            this.repaint();
        }

        public void addElement() {
            JLabel addLabel = null;
            if (ifMin) {
                addLabel = new JLabel(rb.getString("AddIfMin"));
            } else {
                addLabel = new JLabel(rb.getString("Add"));
            }
            JLabel nameLabel = new JLabel(rb.getString("Name"));
            JLabel ageLabel = new JLabel(rb.getString("Age"));
            JLabel xLabel = new JLabel("x");
            JLabel yLabel = new JLabel("y");
            JLabel sizeLabel = new JLabel("Size");

            JTextField nameField = new JTextField();
            JTextField ageField = new JTextField();
            JTextField xField = new JTextField();
            JTextField yField = new JTextField();
            JTextField sizeField = new JTextField();

            JButton backButton = new JButton(rb.getString("Back"));
            JButton addButton = new JButton(rb.getString("Add"));
            GridBagLayout layout = new GridBagLayout();
            this.setLayout(layout);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = SwingConstants.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.1;
            gbc.gridwidth = 2;

            this.add(addLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            this.add(nameLabel, gbc);

            gbc.gridy = 2;
            this.add(ageLabel, gbc);

            gbc.gridy = 3;
            this.add(xLabel, gbc);

            gbc.gridy = 4;
            this.add(yLabel, gbc);

            gbc.gridy = 5;
            this.add(sizeLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            this.add(nameField, gbc);

            gbc.gridy = 2;
            this.add(ageField, gbc);

            gbc.gridy = 3;
            this.add(xField, gbc);

            gbc.gridy = 4;
            this.add(yField, gbc);

            gbc.gridy = 5;
            this.add(sizeField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            this.add(backButton);

            gbc.gridx = 1;
            this.add(addButton);

            backButton.addActionListener(e -> {
                this.setVisible(false);
            });

            addButton.addActionListener(e -> {
                try {
                    Human human = new Human(nameField.getText());
                    human.setAge(Integer.parseInt(ageField.getText()));
                    human.setX(Integer.parseInt(xField.getText()));
                    human.setY(Integer.parseInt(yField.getText()));
                    human.setSize(Integer.parseInt(sizeField.getText()));
                    if (ifMin) {
                        client.sender.sendCommand(client.getToken(), "add_if_min", human);
                    } else {
                        client.sender.sendCommand(client.getToken(), "add", human);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, rb.getString("IllegalField"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    private class Shower extends JPanel {



        public void showElement(Human human) {

        }

        public void showCollection() {

        }
    }

    private class Drawer extends JPanel {
        Drawer() {
            this.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Point point = e.getPoint();
                    int x = (int)point.getX()/size;
                    int y = (int)point.getY()/size;
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < width*size; i+=size) {
                for (int j = 0; j < height*(size+1); j+=size) {
                    g.drawLine(0, j, width*size, j);
                }
                g.drawLine(i, 0, i, height*size);
            }
            g.drawLine(width*size, 0, width*size, height*size);
            this.repaint();
        }
    }
}
