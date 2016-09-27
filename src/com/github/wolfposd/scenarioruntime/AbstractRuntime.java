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

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractRuntime {

    public static final int MILLISECONDS = 1000;
    public static final int FIFTEENMINUTES_IN_SECONDS = 900;

    protected Set<ScenarioActor> actors = new LinkedHashSet<>();

    protected Date currentDate;

    protected final long advanceDate;

    public AbstractRuntime(long advanceDatePerSecond) {
        currentDate = setupDate();
        advanceDate = advanceDatePerSecond * MILLISECONDS;
    }

    private Date setupDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public void addActor(ScenarioActor act) {
        actors.add(act);
    }

    public void addActors(ScenarioActor... actors) {
        for (ScenarioActor a : actors) {
            this.actors.add(a);
        }
    }

    protected void notifyActorsOfDateTick() {
        actors.forEach(e -> e.actOnTimeperiod(currentDate));
        advanceDateToNextDate();
    }

    /**
     * ticks the date one step forward
     */
    private void advanceDateToNextDate() {
        currentDate = new Date(currentDate.getTime() + advanceDate);
    }

}
