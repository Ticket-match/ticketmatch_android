package de.ticket_match.ticketmatch.com.facebook;


        import com.facebook.AccessToken;
        import com.facebook.AccessTokenSource;

        import java.util.ArrayList;
        import java.util.Collection;

public class AccessTokenCreator {

    public static AccessToken createToken(Collection<String> grantedPermissions) {
        return new AccessToken("token", "appId", "userId", grantedPermissions,
                new ArrayList<String>(), AccessTokenSource.WEB_VIEW, null, null);
    }
}
