package com.SitStayCreate.GUI;

import com.SitStayCreate.GUI.ActionListeners.DropActionListener;
import com.SitStayCreate.MidiGrid.MidiGridAdapter;
import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.VirtualGrid.VirtualGridController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevicesTable extends JPanel implements Scrollable {
    private int yConstraint = 0;
    private GridBagLayout bagLayout;
    //Switch to Map
    private Map<String, JComponent[]> tableComponents;

    //TODO: Replace this with a JPanel/GridBagLayout
    public DevicesTable(int FONTSIZE) {

        //Set up and style the table
        bagLayout = new GridBagLayout();
        setLayout(bagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        Font tableFont = new Font(Font.MONOSPACED, Font.BOLD, FONTSIZE - 2);
        Font tableHeaderFont = new Font(Font.MONOSPACED, Font.PLAIN, FONTSIZE - 2);

        setFont(tableFont);
        setBackground(Color.GRAY);
        setForeground(Color.YELLOW);

        tableComponents = new HashMap<>();

        //Add header row
        JLabel[] headers = new JLabel[]{
            new JLabel(Constants.DROPLABEL),
            new JLabel(Constants.IDCOLUMNLABEL),
            new JLabel(Constants.SIZECOLUMNLABEL),
            new JLabel(Constants.PORTINCOLUMNLABEL),
            new JLabel(Constants.INVERTEDCOLUMNLABEL),
            new JLabel(Constants.CHLABEL)
        };

        for(JLabel header : headers){
            constraints.gridx++;
            constraints.gridy = yConstraint;
            bagLayout.setConstraints(header, constraints);
            add(header);
        }
        yConstraint++;
    }

    //Midi Grid
    public void addRow(MidiGridAdapter grid) {
        Dimensions dims = grid.getDimensions();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = yConstraint;

        JComponent[] components = new JComponent[6];
        JButton deleteButton = new JButton(Constants.DELETELABEL);
        deleteButton.setPreferredSize(new Dimension(15, 15));
        deleteButton.addActionListener(new DropActionListener(this, grid));
        components[0] = deleteButton;
        components[1] = new JLabel(grid.getId());
        components[2] = new JLabel(dims.getWidth() + Constants.DEVICEDIMSLABEL + dims.getHeight());
        components[3] = new JLabel(String.valueOf(grid.getDecoratedOSCPortIn().getPortIn()));
        components[4] = new JLabel(String.valueOf(dims.isInverted()));
        components[5] = new JLabel(String.valueOf(grid.getHardwareDevice().getChannel() + 1));


        for(JComponent component : components){
            constraints.gridy = yConstraint;
            bagLayout.setConstraints(component, constraints);
            add(component);
            constraints.gridx++;
        }

        tableComponents.put(grid.getId(), components);
        yConstraint++;
    }

    //VGrid
    public void addRow(VirtualGridController grid) {
        Dimensions dims = grid.getDimensions();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = yConstraint;

        JComponent[] components = new JComponent[6];
        JButton deleteButton = new JButton(Constants.DELETELABEL);
        deleteButton.setPreferredSize(new Dimension(15, 15));
        deleteButton.addActionListener(new DropActionListener(this, grid));
        components[0] = deleteButton;
        components[1] = new JLabel(grid.getId());
        components[2] = new JLabel(dims.getWidth() + Constants.DEVICEDIMSLABEL + dims.getHeight());
        components[3] = new JLabel(String.valueOf(grid.getDecoratedOSCPortIn().getPortIn()));
        components[4] = new JLabel(String.valueOf(dims.isInverted()));
        components[5] = new JLabel(Constants.DELETELABEL);

        for(JComponent component : components){
            constraints.gridy = yConstraint;
            bagLayout.setConstraints(component, constraints);
            add(component);
            constraints.gridx++;
        }
        tableComponents.put(grid.getId(), components);
        yConstraint++;
    }

    public void dropRow(String key){
        //Remove components from Panel
        for (JComponent c : tableComponents.get(key)){
            remove(c);
        }
        //TODO: Change this to Map
        //Remove components from list
        tableComponents.remove(key);
        updateUI();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(Constants.VIEWPORTWIDTH, Constants.VIEWPORTHEIGHT);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
