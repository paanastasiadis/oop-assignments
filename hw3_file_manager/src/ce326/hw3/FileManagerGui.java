package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class FileManagerGui {
    private JFrame frame;
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JPanel favoritesPanel;
    private JPanel searchBar;
    private JPanel pathBar;

    private JPanel contentsPanel;
    private JScrollPane scrollingContentsPanel;

    private EditMenu eMenu;
    private FavoritesInXML xmlfavorites;
    private ContentsPanelUtilities contents;


    public FileManagerGui() {
        //Create and set up the window.
        frame = new JFrame("FileManager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 150);
        frame.setLayout(new BorderLayout());

        setTopBar();
        setSearchBar();
        setPathBar();
        setFavoritesPanel();
        setContentsPanel();
        setMainPanel();

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        // set window size
        frame.pack();
        // show window
        frame.setVisible(true);
    }

    private void setMainPanel() {

        JPanel rightPanel = new JPanel();

        GridBagLayout gbl_rightPanel = new GridBagLayout();

        rightPanel.setLayout(gbl_rightPanel);

        GridBagConstraints gbcSearchBar = new GridBagConstraints();
        gbcSearchBar.fill = GridBagConstraints.BOTH;
//        gbcSearchBar.insets = new Insets(0, 0, 5, 0);
//        gbcSearchBar.weightx = 1.0;
        gbcSearchBar.weighty = 0.2;
        gbcSearchBar.gridx = 0;
        gbcSearchBar.gridy = 0;
        rightPanel.add(searchBar, gbcSearchBar);

        GridBagConstraints gbcPathPanel = new GridBagConstraints();
//        gbcPathPanel.weightx = 1.0;
        gbcPathPanel.weighty = 0.05;
        gbcPathPanel.fill = GridBagConstraints.BOTH;
//        gbcPathPanel.insets = new Insets(0, 0, 5, 0);
        gbcPathPanel.gridx = 0;
        gbcPathPanel.gridy = 1;
        rightPanel.add(pathBar, gbcPathPanel);

        GridBagConstraints gbcBrowser = new GridBagConstraints();
        gbcBrowser.fill = GridBagConstraints.BOTH;
//        gbcBrowser.insets = new Insets(0, 0, 5, 0);
        gbcBrowser.weightx = 1.0;
        gbcBrowser.weighty = 0.75;
        gbcBrowser.gridx = 0;
        gbcBrowser.gridy = 2;

        rightPanel.add(scrollingContentsPanel, gbcBrowser);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(favoritesPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        xmlfavorites = new FavoritesInXML(System.getProperty("user.home"));
        contents = new ContentsPanelUtilities(contentsPanel, pathBar, eMenu, xmlfavorites, favoritesPanel);
        contents.browseDirectory(System.getProperty("user.home"));

        File initFile = new File(System.getProperty("user.home"));
        contents.setBreadCrumb(initFile);


        File[] existingFiles = xmlfavorites.readAllEntries();
        contents.initFavorites(existingFiles, System.getProperty("user.home"));


        JPanel searchList = new JPanel(new BorderLayout());
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("no entry yet");
        searchList.add(new JScrollPane(new JList<String>(model)), BorderLayout.CENTER);
        JTextField textfield = new JTextField(20);

        JButton searchBarButton = new JButton("Search");
        searchBarButton.addActionListener(actionEvent -> {
            String inputText = textfield.getText();
            String type;
            String fileToSearch;
            int index = inputText.lastIndexOf(" type:");
            if ( index != -1) {
                type = inputText.substring(index + 6);
                fileToSearch = inputText.substring(0, index);
            }
            else {
                type = null;
                fileToSearch = inputText;
            }
            JList<String> jlist = contents.showSearchResults(fileToSearch.toLowerCase(), type);
            searchList.removeAll();
            searchList.add(new JScrollPane(jlist), BorderLayout.CENTER);
            searchList.revalidate();
            searchList.repaint();

        });
        searchBar.add(searchBarButton, BorderLayout.SOUTH);
        searchBar.add(textfield, BorderLayout.NORTH);
        searchBar.add(searchList, BorderLayout.CENTER);

        searchBar.setVisible(false);

    }

    private void setTopBar() {
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        eMenu = new EditMenu("Edit", mainPanel);

        JMenuItem searchItem = new JMenuItem("Search");
        searchItem.addActionListener(actionEvent -> {
            searchBar.setVisible(!searchBar.isVisible());
            searchBar.revalidate();
            searchBar.repaint();

        });
        searchItem.setMnemonic(KeyEvent.VK_S);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setToolTipText("Exit application");
        exitItem.addActionListener((event) -> System.exit(0));

        JMenuItem newWindowItem = new JMenuItem("New Window");
        newWindowItem.setMnemonic(KeyEvent.VK_N);
        newWindowItem.setToolTipText("Open a new window");
        newWindowItem.addActionListener(actionEvent -> javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FileManagerGui();
            }
        }));


        fileMenu.add(newWindowItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(eMenu);
        menuBar.add(searchItem);

    }

    private void setFavoritesPanel() {
        favoritesPanel = new JPanel();
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel();
        logo.setBackground(Color.cyan);
        logo.setIcon(new ImageIcon("./logo-icon.png"));
        favoritesPanel.add(logo);

    }

    private void setSearchBar() {
        searchBar = new JPanel();
        searchBar.setLayout(new BorderLayout());
        searchBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //add components to search panel
//        JTextField searchField = new JTextField(20);
//
//        JButton searchBarButton = new JButton("Search");

    }

    private void setPathBar() {

        pathBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pathBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        pathBar.setBackground(Color.GRAY);//for demonstration purpose
    }

    private void setContentsPanel() {
        contentsPanel = new JPanel();
        contentsPanel.setOpaque(false);
        contentsPanel.setLayout(new WrapLayout(FlowLayout.LEADING));

        scrollingContentsPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollingContentsPanel.setPreferredSize(new Dimension(550, 500));
        scrollingContentsPanel.setViewportView(contentsPanel);

    }
}
