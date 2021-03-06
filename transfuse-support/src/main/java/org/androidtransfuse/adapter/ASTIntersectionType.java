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
package org.androidtransfuse.adapter;

import com.google.common.base.Joiner;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTIntersectionType extends ASTEmptyType {

    private List<ASTType> intersection;

    public ASTIntersectionType(List<ASTType> intersection){
        super(Joiner.on("&").join(intersection));
        this.intersection = intersection;
    }

    @Override
    public boolean inherits(ASTType type) {
        for (ASTType astType : intersection) {
            if(astType.inherits(type)){
                return true;
            }
        }
        return false;
    }

    @Override
    public PackageClass getPackageClass() {
        return intersection.iterator().next().getPackageClass();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTIntersectionType)) {
            return false;
        }

        ASTIntersectionType that = (ASTIntersectionType) o;
        return new EqualsBuilder().append(intersection, that.intersection).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(intersection).hashCode();
    }
}
