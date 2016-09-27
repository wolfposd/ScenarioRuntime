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

public class ScenarioController extends AbstractRuntime {

    private ScenarioControllerUI ui;
    private Thread runtimeThread;

    public ScenarioController() {
        super(FIFTEENMINUTES_IN_SECONDS);
        ui = new ScenarioControllerUI();

        ui.play.addActionListener(e -> playButtonPressed());
        ui.step.addActionListener(e -> stepButtonPressed());

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

    private void startThread() {
        runtimeThread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        int sleepytime = ui.slider.getValue();
                        System.out.println("Next Tick in " + sleepytime + " seconds");
                        Thread.sleep(sleepytime * MILLISECONDS);
                        notifyActorsOfDateTick();
                    }
                } catch (InterruptedException e) {
                }
            }
        });
        runtimeThread.start();
    }

}
