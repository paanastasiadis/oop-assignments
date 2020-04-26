package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class FileManagerGui {
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JPanel favoritesPanel;
    private JPanel searchBar;
    private JPanel pathBar;

    private JPanel contentsPanel;
    private JScrollPane scrollingContentsPanel;

    private EditMenu eMenu;
    private BrowserUtilities utilities;


    public FileManagerGui() {
        //Create and set up the window.
        JFrame frame = new JFrame("FileBrowser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setPathBar();
        setFavoritesPanel();
        setContentsPanel();

        setSearchBar();
        setMainPanel();
        setTopBar();

        //initialize browser's functionality
        initServices();

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        frame.pack();
        // show window
        frame.setVisible(true);
    }

    //necessary initialization of services that makes the app function
    private void initServices() {

        utilities = new BrowserUtilities(System.getProperty("user.home"), contentsPanel, pathBar, eMenu, favoritesPanel);

        utilities.initNavigation();


    }

    //the main panel including all the other panels except the topbar
    private void setMainPanel() {

        //right panel: all the content right of favorites panel
        JPanel rightPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbcSearchBar = new GridBagConstraints();
        gbcSearchBar.fill = GridBagConstraints.BOTH;
        gbcSearchBar.weighty = 0.2;
        gbcSearchBar.gridx = 0;
        gbcSearchBar.gridy = 0;
        rightPanel.add(searchBar, gbcSearchBar);

        GridBagConstraints gbcPathPanel = new GridBagConstraints();
        gbcPathPanel.weighty = 0.05;
        gbcPathPanel.fill = GridBagConstraints.BOTH;
        gbcPathPanel.gridx = 0;
        gbcPathPanel.gridy = 1;
        rightPanel.add(pathBar, gbcPathPanel);

        GridBagConstraints gbcBrowser = new GridBagConstraints();
        gbcBrowser.fill = GridBagConstraints.BOTH;
        gbcBrowser.weightx = 1.0;
        gbcBrowser.weighty = 0.75;
        gbcBrowser.gridx = 0;
        gbcBrowser.gridy = 2;
        rightPanel.add(scrollingContentsPanel, gbcBrowser);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(favoritesPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);


    }

    //top bar of the app
    private void setTopBar() {

        menuBar = new JMenuBar();

        // Add file menu with New Window and Exit options
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        // Exit option
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setToolTipText("Exit application");
        exitItem.addActionListener((event) -> System.exit(0));

        // New Window option
        JMenuItem newWindowItem = new JMenuItem("New Window");
        newWindowItem.setMnemonic(KeyEvent.VK_N);
        newWindowItem.setToolTipText("Open a new window");
        newWindowItem.addActionListener(actionEvent -> javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FileManagerGui();
            }
        }));

        //Add the options to 'File' Menu
        fileMenu.add(newWindowItem);
        fileMenu.add(exitItem);

        //Edit Menu
        eMenu = new EditMenu("Edit", mainPanel);

        //Search Button
        JMenuItem searchItem = new JMenuItem("Search");
        searchItem.setMnemonic(KeyEvent.VK_S);
        searchItem.addActionListener(actionEvent -> {
            searchBar.setVisible(!searchBar.isVisible());
            searchBar.revalidate();
            searchBar.repaint();

        });

        //Add all the menus to the top bar
        menuBar.add(fileMenu);
        menuBar.add(eMenu);
        menuBar.add(searchItem);

    }

    //panel containing the favorite directories
    private void setFavoritesPanel() {

        favoritesPanel = new JPanel();
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));

        //the logo of the app as the first element in the box layout
        JLabel logo = new JLabel();
        logo.setBackground(Color.cyan);
        logo.setIcon(new ImageIcon("./logo/logo-icon.png"));

        favoritesPanel.add(logo);
        favoritesPanel.add(Box.createVerticalStrut(40));

    }

    //panel that contains the search utility, including a list with a textfield and a button
    private void setSearchBar() {
        searchBar = new JPanel();
        searchBar.setLayout(new BorderLayout());
        searchBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //initialize search list with an empty list
        JPanel searchList = new JPanel(new BorderLayout());
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Search a file or directory by typing it.");
        searchList.add(new JScrollPane(new JList<String>(model)), BorderLayout.CENTER);
        JTextField textfield = new JTextField(20);

        //trigger searching process with the search button and filter the input value
        JButton searchBarButton = new JButton("Search");
        searchBarButton.addActionListener(actionEvent -> {
            String inputText = textfield.getText();
            String type;
            String fileToSearch;
            int index = inputText.lastIndexOf(" type:");
            if (index != -1) {
                type = inputText.substring(index + 6);
                fileToSearch = inputText.substring(0, index);
            } else {
                type = null;
                fileToSearch = inputText;
            }

            JList<String> jlist = utilities.showSearchResults(fileToSearch.toLowerCase(), type);
            searchList.removeAll();
            searchList.add(new JScrollPane(jlist), BorderLayout.CENTER);
            searchList.revalidate();
            searchList.repaint();

        });
        searchBar.add(searchBarButton, BorderLayout.SOUTH);
        searchBar.add(textfield, BorderLayout.NORTH);
        searchBar.add(searchList, BorderLayout.CENTER);

        //set searchbar hidden by default
        searchBar.setVisible(false);
    }

    //panel that contains the breadcrumb
    private void setPathBar() {

        pathBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pathBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        pathBar.setBackground(Color.GRAY);
    }

    //panel containing the files view
    private void setContentsPanel() {
        contentsPanel = new JPanel();
        contentsPanel.setOpaque(false);
        contentsPanel.setLayout(new WrapLayout(FlowLayout.LEADING));

        scrollingContentsPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollingContentsPanel.setPreferredSize(new Dimension(550, 500));
        scrollingContentsPanel.setViewportView(contentsPanel);

    }
}
