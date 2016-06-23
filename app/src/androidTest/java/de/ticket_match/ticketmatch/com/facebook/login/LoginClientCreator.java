package de.ticket_match.ticketmatch.com.facebook.login;

//
//public class LoginClientCreator {
//
//    public static Request createRequest() {
//        final HashSet<String> permissions = Sets.newHashSet("user_actions.music", "user_friends", "user_likes", "email");
//        return new Request(LoginBehavior.NATIVE_WITH_FALLBACK, permissions, DefaultAudience.EVERYONE, "appId", "authId");
//    }
//
//    public static Result createResult() {
//        final Request request = createRequest();
//        return new Result(request, Result.Code.SUCCESS,  AccessTokenCreator.createToken(request.getPermissions()), null, null);
//    }
//}