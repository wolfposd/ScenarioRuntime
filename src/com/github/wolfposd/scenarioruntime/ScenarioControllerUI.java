/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.wolfposd.scenarioruntime;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

public class ScenarioControllerUI {

    public JFrame frame;
    public JList<ScenarioActor> actors;
    public JButton play;
    public JButton step;
    public JSlider slider;

    public ScenarioControllerUI() {
        frame = new JFrame("Scenario Runtime");
        actors = new JList<>();
        actors.setModel(new DefaultListModel<>());
        actors.setCellRenderer(new ScenarioActorListRenderer());

        JPanel actorSelectionPanel = new JPanel();
        actorSelectionPanel.setBackground(Color.red);
        play = new JButton("Play");
        step = new JButton("Step");
        slider = new JSlider(2, 300);
        slider.setValue(20);

        JLabel sliderValue = new JLabel("Time: 20 seconds");

        frame.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        JScrollPane scroll = new JScrollPane(actors);
        centerPanel.add(scroll);
        centerPanel.add(actorSelectionPanel);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(play, BorderLayout.WEST);
        southPanel.add(step, BorderLayout.EAST);

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(sliderValue, BorderLayout.NORTH);
        sliderPanel.add(slider, BorderLayout.SOUTH);

        southPanel.add(sliderPanel, BorderLayout.NORTH);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setSize(300, 300);
        frame.setLocation(100, 100);

        slider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            int slidervalue = (int) source.getValue();
            sliderValue.setText("Time: " + slidervalue + " seconds");
        });
    }

    public DefaultListModel<ScenarioActor> getListModel() {
        return (DefaultListModel<ScenarioActor>) actors.getModel();
    }

    public void setVisible(boolean vis) {
        frame.setVisible(vis);
    }

    public class ScenarioActorListRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel result = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            ScenarioActor v = (ScenarioActor) value;
            result.setText(v.displayName());
            return result;
        }

    }

}
