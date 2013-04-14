/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.scope;

import org.junit.Test;

import javax.inject.Provider;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ScopeTest {

    @Test
    public void verifyScopeMethod() throws NoSuchMethodException {
        Method getScopedByKeyMethod = Scope.class.getMethod(Scope.GET_SCOPED_OBJECT, ScopeKey.class, Provider.class);
        assertNotNull(getScopedByKeyMethod);
    }
}
