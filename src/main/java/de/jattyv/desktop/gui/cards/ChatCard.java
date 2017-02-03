/*
 * Copyright (C) 2016 Dimitrios Diamantidis &lt;Dimitri.dia@ledimi.com&gt;
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.jattyv.desktop.gui.cards;

import de.jattyv.desktop.data.ConfigFileHandler;
import de.jattyv.desktop.gui.Window;
import de.jattyv.desktop.gui.cards.cell.FG;
import de.jattyv.desktop.gui.cards.cell.FGCellRenderer;
import de.jattyv.jcapi.data.jfc.JattyvFileController;
import de.jattyv.jcapi.data.jfc.data.Settings;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Dimitrios Diamantidis &lt;Dimitri.dia@ledimi.com&gt;
 */
public class ChatCard extends JPanel implements KeyListener, MouseListener, ListSelectionListener, ActionListener {

    private JTextArea textAreaInput;
    private JButton btnFriends;
    private JButton btnGroups;
    private JScrollPane scrollPaneFG;
    private JScrollPane scrollPaneMessages;
    private JList listFG;
    private JList<String> listMessages;
    private DefaultListModel modelFG = new DefaultListModel();
    private LinkedList<String> friends = new LinkedList<>();
    private DefaultListModel<String> modelMessages = new DefaultListModel<String>();
    private JMenuBar menuBar;
    private JMenu mnFriends;
    private JMenuItem mntmSFR;
    private JMenu mnGroups;
    private JMenuItem mntmSGR;
    private JMenuItem mntmATG;

    Window window;

    Settings settings;

    public ChatCard(Window window, Settings settings) {
        super();
        this.window = window;
        this.setLayout(new BorderLayout(0, 0));

        JSplitPane splitPane_1 = new JSplitPane();
        splitPane_1.setResizeWeight(0.85);
        splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.add(splitPane_1, BorderLayout.CENTER);

        textAreaInput = new JTextArea();
        textAreaInput.addKeyListener(this);
        splitPane_1.setRightComponent(textAreaInput);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.1);
        splitPane_1.setLeftComponent(splitPane);

        JSplitPane splitPane_2 = new JSplitPane();
        splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_2.setResizeWeight(0.05);
        splitPane.setLeftComponent(splitPane_2);

        JSplitPane splitPane_3 = new JSplitPane();
        splitPane_3.setResizeWeight(0.5);
        splitPane.setResizeWeight(0.2);
        splitPane_2.setLeftComponent(splitPane_3);

        btnFriends = new JButton("Friends");
        splitPane_3.setLeftComponent(btnFriends);

        btnGroups = new JButton("Groups");
        splitPane_3.setRightComponent(btnGroups);

        scrollPaneFG = new JScrollPane();
        splitPane_2.setRightComponent(scrollPaneFG);

        listFG = new JList(modelFG);
        listFG.setCellRenderer(new FGCellRenderer());
        listFG.addListSelectionListener(this);
        listFG.addMouseListener(this);
        scrollPaneFG.setViewportView(listFG);

        scrollPaneMessages = new JScrollPane();
        splitPane.setRightComponent(scrollPaneMessages);

        listMessages = new JList<String>(modelMessages);
        scrollPaneMessages.setViewportView(listMessages);

        menuBar = new JMenuBar();
        this.add(menuBar, BorderLayout.NORTH);

        mnFriends = new JMenu("Friends");
        menuBar.add(mnFriends);

        mnGroups = new JMenu("Groups");
        menuBar.add(mnGroups);

        mntmSFR = new JMenuItem("add Friend");
        mntmSFR.addActionListener(this);
        mnFriends.add(mntmSFR);

        mntmSGR = new JMenuItem("add Group");
        mntmSGR.addActionListener(this);
        mnGroups.add(mntmSGR);

        mntmATG = new JMenuItem("add to Group");
        mntmATG.addActionListener(this);
        mnGroups.add(mntmATG);

        this.settings = settings;

        if (settings.isClientSettingsAvailable()) {
            for (String fname : settings.getClientSettings().getFriends()) {
                FG fg = new FG(fname, FG.FG_TYPE_FRIEND);
                modelFG.addElement(fg);
                friends.add(fname);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            FG fg = (FG) listFG.getSelectedValue();
            if (fg.getType() == FG.FG_TYPE_FRIEND) {
                String toUser = fg.getTitle();
                String message = textAreaInput.getText();
                textAreaInput.setText("");
                window.getHandler().getOutHandler().sendNewMessage(toUser, message);
            }else if (fg.getType() == FG.FG_TYPE_GROUP){
                String toGroup = fg.getTitle();
                String message = textAreaInput.getText();
                textAreaInput.setText("");
                window.getHandler().getOutHandler().sendNewGroupMessage(toGroup, message);
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        modelMessages.clear();
        FG fg = (FG) listFG.getSelectedValue();
        if (fg.getType() == FG.FG_TYPE_FRIEND) {
            LinkedList<String> messages = window.getHandler().getMessages(fg.getTitle());
            for (String message : messages) {
                addMessage(fg.getTitle(), message);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mntmSFR) {
            String fname = JOptionPane.showInputDialog("Enter The Friends Username:", "Friendsname");
            friends.add(fname);
            modelFG.addElement(new FG(fname, FG.FG_TYPE_FRIEND));
            if (settings.isClientSettingsPathAvailable()) {
                new ConfigFileHandler().write(settings.getClientSettingsPath(), JattyvFileController.getFriendsAsJson(friends));
            }
        }
        if (e.getSource() == mntmSGR) {
            String gname = JOptionPane.showInputDialog("Create a new Group:", "GroupName");
            window.getHandler().getOutHandler().createGroup(gname);
        }
        if (e.getSource() == mntmATG) {
            FG fg = (FG) listFG.getSelectedValue();
            if (fg.getType() == FG.FG_TYPE_GROUP) {
                String fname = JOptionPane.showInputDialog("Enter the UserName of you friend you want to add to " + fg.getTitle());
                window.getHandler().getOutHandler().addUserToGroup(fg.getTitle(), fname);
            }
        }
    }

    public void addMessage(String fName, String message) {
        if (!listFG.isSelectionEmpty()) {
            FG fg = (FG) listFG.getSelectedValue();
            if (fg.getType() == FG.FG_TYPE_FRIEND) {
                if (fg.getTitle().equals(fName)) {
                    modelMessages.addElement(message);
                    listMessages.ensureIndexIsVisible(modelMessages.size() - 1);
                }
            }
        }
    }

    public void addGroup(String gName) {
        FG fg = new FG(gName, FG.FG_TYPE_GROUP);
        modelFG.addElement(fg);
    }

    public void addGroupMessage(String gName, String message) {
        if (!listFG.isSelectionEmpty()) {
            FG fg = (FG) listFG.getSelectedValue();
            if (fg.getType() == FG.FG_TYPE_GROUP) {
                if (fg.getTitle().equals(gName)) {
                    modelMessages.addElement(message);
                    listMessages.ensureIndexIsVisible(modelMessages.size() - 1);
                }
            }
        }
    }

}
