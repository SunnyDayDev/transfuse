package org.androidtransfuse.integrationTest;

import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.BindInterceptor;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.integrationTest.aop.AOPInterceptor;
import org.androidtransfuse.integrationTest.aop.InterceptorRecorder;
import org.androidtransfuse.integrationTest.inject.LoopThree;
import org.androidtransfuse.integrationTest.inject.LoopThreeImpl;

/**
 * @author John Ericksen
 */
@TransfuseModule
public interface IntegrationModule {

    @Bind(LoopThreeImpl.class)
    LoopThree getThree();

    @BindInterceptor(AOPInterceptor.class)
    InterceptorRecorder getInterceptor();
}
