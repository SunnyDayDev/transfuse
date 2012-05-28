package org.androidtransfuse.gen.variableBuilder;

import android.view.View;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

public class ViewVariableBuilder extends ConsistentTypeVariableBuilder {

    private static final String FIND_VIEW = "findViewById";

    private JType viewType;
    private int viewId;
    private InjectionNode activityInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private RResourceReferenceBuilder rResourceReferenceBuilder;
    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private InvocationBuilder injectionInvocationBuilder;

    @Inject
    public ViewVariableBuilder(@Assisted int viewId,
                               @Assisted InjectionNode activityInjectionNode,
                               @Assisted JType viewType,
                               InjectionExpressionBuilder injectionExpressionBuilder,
                               RResourceReferenceBuilder rResourceReferenceBuilder,
                               JCodeModel codeModel,
                               InvocationBuilder injectionInvocationBuilder,
                               UniqueVariableNamer variableNamer) {
        super(View.class);
        this.viewId = viewId;
        this.activityInjectionNode = activityInjectionNode;
        this.viewType = viewType;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.codeModel = codeModel;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.variableNamer = variableNamer;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JVar variableRef;
        try {
            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, injectionNode);

            JType nodeType = codeModel.parseType(injectionNode.getClassName());

            TypedExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

            ResourceIdentifier viewResourceIdentifier = injectionBuilderContext.getRResource().getResourceIdentifier(viewId);
            JExpression viewIdRef = rResourceReferenceBuilder.buildReference(viewResourceIdentifier);

            JExpression viewExpression = contextVar.getExpression().invoke(FIND_VIEW).arg(viewIdRef);

            ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
            if (injectionAspect == null) {
                return viewExpression;
            } else {
                if (injectionAspect.getAssignmentType().equals(ASTInjectionAspect.InjectionAssignmentType.LOCAL)) {
                    variableRef = injectionBuilderContext.getBlock().decl(nodeType, variableNamer.generateName(injectionNode.getClassName()));
                } else {
                    variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(injectionNode.getClassName()));
                }
                JBlock block = injectionBuilderContext.getBlock();


                block.assign(variableRef, JExpr.cast(viewType, viewExpression));

                //field injection
                for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
                    block.add(
                            injectionInvocationBuilder.buildFieldSet(
                                    injectionBuilderContext.getVariableMap().get(fieldInjectionPoint.getInjectionNode()),
                                    fieldInjectionPoint,
                                    variableRef));
                }

                //method injection
                for (MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
                    block.add(
                            injectionInvocationBuilder.buildMethodCall(
                                    Object.class.getName(),
                                    injectionBuilderContext.getVariableMap(),
                                    methodInjectionPoint,
                                    variableRef));
                }
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse class: " + injectionNode.getClassName(), e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while generating injection: " + injectionNode.getClassName(), e);
        }
        return variableRef;
    }
}