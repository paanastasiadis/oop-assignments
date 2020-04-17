package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class FileManagerGui {
    private JFrame frame;
    private JPanel mainPanel;
    //    private JPanel topBar;
    private JMenuBar menuBar;
    private JPanel favoritesPanel;
    private JPanel searchBar;
    private JPanel pathBar;

    private JPanel contentsPanel;
    private JScrollPane scrollingContentsPanel;

    private EditMenu eMenu;


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
        gbcSearchBar.fill = GridBagConstraints.HORIZONTAL;
//        gbcSearchBar.insets = new Insets(0, 0, 5, 0);
//        gbcSearchBar.weightx = 1.0;
//        gbcSearchBar.weighty = 0.1;
        gbcSearchBar.gridx = 0;
        gbcSearchBar.gridy = 0;
        rightPanel.add(searchBar, gbcSearchBar);

        GridBagConstraints gbcPathPanel = new GridBagConstraints();
//        gbcPathPanel.weightx = 1.0;
//        gbcPathPanel.weighty = 0.1;
        gbcPathPanel.fill = GridBagConstraints.HORIZONTAL;
//        gbcPathPanel.insets = new Insets(0, 0, 5, 0);
        gbcPathPanel.gridx = 0;
        gbcPathPanel.gridy = 1;
        rightPanel.add(pathBar, gbcPathPanel);

        GridBagConstraints gbcBrowser = new GridBagConstraints();
        gbcBrowser.fill = GridBagConstraints.BOTH;
//        gbcBrowser.insets = new Insets(0, 0, 5, 0);
        gbcBrowser.weightx = 1.0;
        gbcBrowser.weighty = 1.0;
        gbcBrowser.gridx = 0;
        gbcBrowser.gridy = 2;

        rightPanel.add(scrollingContentsPanel, gbcBrowser);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(favoritesPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);


        ContentsPanelUtilities contents = new ContentsPanelUtilities(contentsPanel, pathBar,eMenu, frame);
        contents.browseDirectory(System.getProperty("user.home"));

        File initFile = new File(System.getProperty("user.home"));
        contents.setBreadCrumb(initFile);


    }

    private void setTopBar() {
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

         eMenu = new EditMenu("Edit", mainPanel);
//        editMenu.setMnemonic(KeyEvent.VK_E);
//
//        JMenuItem renameItem = new JMenuItem("Rename");
//        renameItem.setMnemonic(KeyEvent.VK_R);
//        renameItem.setToolTipText("Rename a file or directory");
//
//        editMenu.setEnabled(false);
//        editMenu.add(renameItem);

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setMnemonic(KeyEvent.VK_S);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> System.exit(0));

        JMenuItem newWindowItem = new JMenuItem("New Window");
        newWindowItem.setMnemonic(KeyEvent.VK_N);
        newWindowItem.setToolTipText("Open a new window");
        newWindowItem.addActionListener(actionEvent -> javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FileManagerGui();
            }
        }));



        fileMenu.add(newWindowItem);
        fileMenu.add(eMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(eMenu);
        menuBar.add(searchMenu);

    }

    private void setFavoritesPanel() {
        favoritesPanel = new JPanel();
        favoritesPanel.setLayout(new GridLayout(6, 1));
        //add components (buttons) to buttons panel
//        JPanel insertionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        insertionPanel.add(insertionButton);
        JButton insertionButton = new JButton("Insertion Sort");
        JButton selectionButton = new JButton("Selection Sort");
        JButton quickButton = new JButton("Quick Sort");
        JButton mergeButton = new JButton("Merge Sort");
        JButton heapButton = new JButton("Heap Sort");
        JButton radixButton = new JButton("Radix Sort");

        favoritesPanel.add(insertionButton);
        favoritesPanel.add(selectionButton);
        favoritesPanel.add(quickButton);
        favoritesPanel.add(mergeButton);
        favoritesPanel.add(heapButton);
        favoritesPanel.add(radixButton);
    }

    private void setSearchBar() {
        searchBar = new JPanel();
        searchBar.setLayout(new BorderLayout());
        searchBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //add components to search panel
        JTextField searchField = new JTextField();
        JButton searchBarButton = new JButton("Search");
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBarButton, BorderLayout.EAST);
    }

    private void setPathBar() {
        //properties
        pathBar = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 0));
        pathBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
//        pathBar.setOpaque(false);
        pathBar.setBackground(Color.YELLOW);//for demonstration purpose
    }

    private void setContentsPanel() {
        contentsPanel = new JPanel();
        contentsPanel.setOpaque(false);
        contentsPanel.setLayout(new GridLayout(0,4));

        scrollingContentsPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollingContentsPanel.setViewportView(contentsPanel);

    }
}
