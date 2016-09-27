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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScenarioController extends AbstractRuntime {

    private ScenarioControllerUI ui;
    private Thread runtimeThread;
    private int currentDateIndex = -1;

    private IMessageDetail messageDetail;

    private List<Date> dates = new ArrayList<>();

    public ScenarioController() {
        super(FIFTEENMINUTES_IN_SECONDS);
        ui = new ScenarioControllerUI();

        ui.play.addActionListener(e -> playButtonPressed());
        ui.step.addActionListener(e -> stepButtonPressed());
        ui.prevDate.addActionListener(e -> previousDateSelected());
        ui.nextDate.addActionListener(e -> nextDateSelected());

        ui.setVisible(true);
    }

    @Override
    public void addActor(ScenarioActor act) {
        super.addActor(act);
        ui.getActorsListModel().addElement(act);
    }

    @Override
    public void addActors(ScenarioActor... actors) {
        for (ScenarioActor act : actors) {
            this.addActor(act);
        }
    }

    public void setMessageDetail(IMessageDetail imd) {
        messageDetail = imd;
        messageDetail.setCallBack(() -> updateMessageBoard());
    }

    private void playButtonPressed() {
        if (runtimeThread == null) {
            startThread();
            ui.play.setText("Pause");
        } else {
            runtimeThread.stop();
            runtimeThread = null;
            ui.play.setText("Play");
        }
    }

    private void stepButtonPressed() {
        notifyActorsOfDateTick();
    }

    private void previousDateSelected() {
        currentDateIndex--;
        if (currentDateIndex < 0)
            currentDateIndex = 0;
        if (messageDetail != null)
            updateMessageBoard();
    }

    private void nextDateSelected() {
        currentDateIndex++;
        if (currentDateIndex >= dates.size())
            currentDateIndex = dates.size() - 1;
        if (messageDetail != null)
            updateMessageBoard();
    }

    private void updateMessageBoard() {
        Date d = dates.get(currentDateIndex);
        ui.messageBoard.setText("Date: " + d + "\n");
        if (messageDetail != null) {
            List<SRMessage> msgs = messageDetail.getMessagesForDate(d);
            if (msgs != null) {
                for (SRMessage m : msgs) {
                    ui.messageBoard.append(m.getDisplayMessage());
                    ui.messageBoard.append("\n");
                }
            }
        }
    }

    private void startThread() {
        runtimeThread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        int sleepytime = ui.slider.getValue();
                        Thread.sleep(sleepytime * MILLISECONDS);
                        notifyActorsOfDateTick();
                    }
                } catch (InterruptedException e) {
                }
            }
        });
        runtimeThread.start();
    }

    @Override
    protected void notifyActorsOfDateTick() {
        dates.add(super.currentDate);
        currentDateIndex++;
        super.notifyActorsOfDateTick();
        updateMessageBoard();
    }

}
