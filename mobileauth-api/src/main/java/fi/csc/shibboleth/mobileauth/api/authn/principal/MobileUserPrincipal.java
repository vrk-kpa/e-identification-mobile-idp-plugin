/*
 * The MIT License
 * Copyright (c) 2016 CSC - IT Center for Science, http://www.csc.fi
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package fi.csc.shibboleth.mobileauth.api.authn.principal;

import javax.annotation.Nonnull;

import com.google.common.base.MoreObjects;

import net.shibboleth.idp.authn.principal.CloneablePrincipal;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

public class MobileUserPrincipal implements CloneablePrincipal {

    /** The mobile number */
    @Nonnull
    @NotEmpty
    private String mobileNumber;

    /*
     * Constructor
     */
    public MobileUserPrincipal(@Nonnull @NotEmpty final String mobile) {
        this.mobileNumber = Constraint.isNotNull(StringSupport.trimOrNull(mobile),
                "MobileNumber cannot be null or empty");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return mobileNumber;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return mobileNumber.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (other instanceof MobileUserPrincipal) {
            return mobileNumber.equals(((MobileUserPrincipal) other).getName());
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("mobileNumber", mobileNumber).toString();
    }

    /** {@inheritDoc} */
    @Override
    public MobileUserPrincipal clone() throws CloneNotSupportedException {
        MobileUserPrincipal copy = (MobileUserPrincipal) super.clone();
        copy.mobileNumber = mobileNumber;
        return copy;
    }

}
