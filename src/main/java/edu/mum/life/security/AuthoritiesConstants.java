package edu.mum.life.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String STUDENT = "ROLE_STUDENT";

    public static final String ORGANIZER = "ROLE_ORGANIZER";

    public static final String LENDER = "ROLE_LENDER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
