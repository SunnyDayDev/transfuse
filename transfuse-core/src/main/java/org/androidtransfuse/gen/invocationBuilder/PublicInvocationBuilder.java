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
package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTConstructor;
import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.List;

/**
 * Invocation Builder for building publicly scoped elements.
 *
 * @author John Ericksen
 */
public class PublicInvocationBuilder implements ModifiedInvocationBuilder {

    private final TypeInvocationHelper invocationHelper;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public PublicInvocationBuilder(TypeInvocationHelper invocationHelper, ClassGenerationUtil generationUtil) {
        this.invocationHelper = invocationHelper;
        this.generationUtil = generationUtil;
    }

    @Override
    public JExpression buildConstructorCall(ASTConstructor constructor, ASTType type, List<? extends JExpression> parameters) {
        JInvocation constructorInvocation = JExpr._new(generationUtil.ref(type));

        for (JExpression parameter : parameters) {
            constructorInvocation.arg(parameter);
        }

        return constructorInvocation;
    }

    @Override
    public JInvocation buildMethodCall(boolean cast, ASTMethod method, List<? extends JExpression> parameters, TypedExpression expression) {
        JInvocation methodInvocation = conditionalCast(cast, generationUtil.ref(expression.getType()), expression.getExpression()).invoke(method.getName());

        for (JExpression parameter : parameters) {
            methodInvocation.arg(parameter);
        }

        return methodInvocation;
    }

    @Override
    public JExpression buildFieldGet(boolean cast, ASTField field, TypedExpression variable) {
        return conditionalCast(cast, generationUtil.ref(variable.getType()), variable.getExpression()).ref(field.getName());
    }

    @Override
    public JStatement buildFieldSet(boolean cast, ASTField field, TypedExpression expression, TypedExpression containingExpression) {
        JBlock assignmentBlock = new JBlock(false, false);

        assignmentBlock.assign(conditionalCast(cast, generationUtil.ref(containingExpression.getType()), containingExpression.getExpression()).ref(field.getName()), invocationHelper.coerceType(field.getASTType(), expression));

        return assignmentBlock;
    }

    private JExpression conditionalCast(boolean cast, JType type, JExpression expr){
        if(cast){
            return JExpr.cast(type, expr);
        }
        return expr;
    }
}
