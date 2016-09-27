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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Provides a "Runtime" for a scenario, that ticks every given period and
 * advances the date by a specified amount. <br>
 * All Dates start at "Today,12.00"
 * 
 * @author @wolfposd
 *
 */
public class ScenarioRuntime extends AbstractRuntime {

    private long period;
    private TimeUnit timeunit;

    /**
     * Create a Scenario that ticks every period and advances the date by 15
     * minutes and starts today at 12:00
     * 
     * @param period
     * @param timeunit
     */
    public ScenarioRuntime(long period, TimeUnit timeunit) {
        this(period, timeunit, FIFTEENMINUTES_IN_SECONDS);
    }

    /**
     * Create a Scenario that ticks every period and advances the date by given
     * seconds and starts today at 12:00
     * 
     * @param period
     * @param timeunit
     * @param advanceDatebySeconds
     */
    public ScenarioRuntime(long period, TimeUnit timeunit, long advanceDatebySeconds) {
        super(advanceDatebySeconds);
        this.period = period;
        this.timeunit = timeunit;
    }

    /**
     * Start the execution of this scenario
     */
    public void startScenario() {
        ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
        s.scheduleAtFixedRate(() -> {
            notifyActorsOfDateTick();
        }, period, period, timeunit);
    }

}
