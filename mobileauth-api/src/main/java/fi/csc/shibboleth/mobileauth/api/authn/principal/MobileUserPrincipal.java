package fi.csc.shibboleth.mobileauth.api.authn.principal;

import javax.annotation.Nonnull;

import com.google.common.base.MoreObjects;

import net.shibboleth.idp.authn.principal.CloneablePrincipal;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

public class MobileUserPrincipal implements CloneablePrincipal {
	
	/** The mobile number */
	@Nonnull @NotEmpty private String mobileNumber;

	/*
	 * Constructor
	 */
	public MobileUserPrincipal(@Nonnull @NotEmpty final String mobile) {
		this.mobileNumber = Constraint.isNotNull(StringSupport.trimOrNull(mobile)
				, "MobileNumber cannot be null or empty");
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
