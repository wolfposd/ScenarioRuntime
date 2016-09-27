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
import java.awt.Component;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

public class ScenarioControllerUI {

    public JFrame frame;
    public JList<ScenarioActor> actors;
    public JButton play;
    public JButton step;
    public JSlider slider;
    public JList<String> actorSelectionList;
    public JButton prevDate;
    public JButton nextDate;
    public JTextArea messageBoard;

    public ScenarioControllerUI() {
        frame = new JFrame("Scenario Runtime");

        frame.add(setupScenarioView(), BorderLayout.WEST);
        frame.add(setupMessageDetailView(), BorderLayout.EAST);

        frame.pack();
        frame.setLocation(100, 100);
    }

    private JPanel setupScenarioView() {
        actors = new JList<>();
        actors.setModel(new DefaultListModel<>());
        actors.setCellRenderer(new ScenarioActorListRenderer());
        actors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        actorSelectionList = new JList<>(new DefaultListModel<String>());
        play = new JButton("Play");
        step = new JButton("Step");
        slider = new JSlider(2, 300);
        slider.setValue(20);

        JLabel sliderValue = new JLabel("Time: 20 seconds");

        frame.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        JScrollPane scroll = new JScrollPane(actors);
        JScrollPane scroll2 = new JScrollPane(actorSelectionList);
        scroll.setBorder(BorderFactory.createTitledBorder("Actors"));
        scroll2.setBorder(BorderFactory.createTitledBorder("Details"));
        centerPanel.add(scroll);
        centerPanel.add(scroll2);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(play, BorderLayout.WEST);
        southPanel.add(step, BorderLayout.EAST);

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(sliderValue, BorderLayout.NORTH);
        sliderPanel.add(slider, BorderLayout.SOUTH);

        southPanel.add(sliderPanel, BorderLayout.NORTH);

        JPanel mainpanel = new JPanel(new BorderLayout());

        slider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            int slidervalue = (int) source.getValue();
            sliderValue.setText("Time: " + slidervalue + " seconds");
        });

        actors.addListSelectionListener(e -> actorsListSelectionChanged(e));

        mainpanel.add(centerPanel, BorderLayout.CENTER);
        mainpanel.add(southPanel, BorderLayout.SOUTH);
        return mainpanel;

    }

    private JPanel setupMessageDetailView() {
        JPanel mainpanel = new JPanel(new BorderLayout());

        prevDate = new JButton("<-");
        nextDate = new JButton("->");

        messageBoard = new JTextArea();

        JPanel north = new JPanel();
        north.add(prevDate);
        north.add(nextDate);

        mainpanel.add(north, BorderLayout.NORTH);
        mainpanel.add(messageBoard, BorderLayout.CENTER);

        return mainpanel;
    }

    private void actorsListSelectionChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && actors.getSelectedValue() != null) {
            ScenarioActor selected = actors.getSelectedValue();

            DefaultListModel<String> model = (DefaultListModel<String>) actorSelectionList.getModel();
            model.removeAllElements();

            ArrayList<Field> myFields = new ArrayList<>();
            getAllFields(selected.getClass(), myFields);
            for (Field f : myFields) {
                try {
                    f.setAccessible(true);
                    Object value = f.get(selected);
                    String name = f.getName();
                    model.addElement(name + ": " + (value == null ? "null" : value.toString()));
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                }
            }
        }
    }

    private void getAllFields(Class<?> o, Collection<Field> fields) {
        if (o != Object.class && o.getSuperclass() != null) {
            getAllFields(o.getSuperclass(), fields);
            for (Field f : o.getDeclaredFields()) {
                fields.add(f);
            }
        }
    }

    public DefaultListModel<ScenarioActor> getActorsListModel() {
        return (DefaultListModel<ScenarioActor>) actors.getModel();
    }

    /**
     * Sets this frame visible/invisible
     * 
     * @param visible
     */
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public class ScenarioActorListRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel result = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof ScenarioActor) {
                ScenarioActor v = (ScenarioActor) value;
                result.setText(v.displayName());
            }
            return result;
        }

    }

}
