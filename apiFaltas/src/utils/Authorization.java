package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Authorization {
private static final String AUTHENTICATION_SCHEME = "bearer";
	
	public static boolean isAuthorized(String token) {
		try {
			if(!isTokenBasedAuthentication(token)) {
				return false;
			} else {
				token = token.substring(AUTHENTICATION_SCHEME.length()).trim();
				validateToken(token);
				return true;
			}
		} catch(Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unused")
	private static void validateToken(String token) throws Exception {
		Algorithm algorithm = Algorithm.HMAC256(Constants.TOKEN_KEY);
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT jwt = verifier.verify(token);
	}

	private static boolean isTokenBasedAuthentication(String token) {
		return token != null && token.toLowerCase().startsWith(AUTHENTICATION_SCHEME + " ");
	}
}
