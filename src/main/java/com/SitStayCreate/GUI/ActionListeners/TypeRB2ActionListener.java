package com.SitStayCreate.GUI.ActionListeners;

import com.SitStayCreate.GUI.GridPanel;
import com.SitStayCreate.GUI.MidiPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TypeRB2ActionListener implements ActionListener {
    MidiPanel midiPanel;
    public TypeRB2ActionListener(MidiPanel midiPanel, GridPanel gridPanel) {
        this.midiPanel = midiPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        midiPanel.setEnabled(false);
    }
}
