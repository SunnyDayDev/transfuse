/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.integrationTest.observes;

import android.util.Log;
import org.androidtransfuse.annotations.Observes;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class SingletonObserver {

    private boolean observedEventThree = false;
    private boolean observedAll = false;

    public void observeEventThree(@Observes EventThree event) {
        Log.i("Event Test", "event three");
        observedEventThree = true;
    }

    public void observeEverything(@Observes Object event) {
        observedAll = true;
    }

    public boolean isObservedEventThree() {
        return observedEventThree;
    }

    public boolean isObservedAll() {
        return observedAll;
    }

    public void reset() {
        observedAll = false;
    }
}
