package com.edu.elasticSearch.service;


import java.net.MalformedURLException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth0.jwk.JwkException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Service
public class JwtUtilsService {

	private static KeyPairGenerator keyPairGenerator;
	private static KeyPair keyPair;

	public JwtUtilsService() throws NoSuchAlgorithmException {
		keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		keyPair = keyPairGenerator.generateKeyPair();
	}

	private String generateJwt(Map<String, String> payload) throws Exception {

		Builder tokenBuilder = JWT.create().withIssuer(null).withClaim("jti", UUID.randomUUID().toString())
			.withExpiresAt(Date.from(Instant.now().plusSeconds(300))).withIssuedAt(Date.from(Instant.now()));

		payload.entrySet().forEach(action -> tokenBuilder.withClaim(action.getKey(), action.getValue()));

		return tokenBuilder
			.sign(Algorithm.RSA256(((RSAPublicKey) keyPair.getPublic()), ((RSAPrivateKey) keyPair.getPrivate())));
	}

	private static RSAPublicKey loadPublicKey(DecodedJWT token) throws JwkException, MalformedURLException {

		return (RSAPublicKey) keyPair.getPublic();
	}

	public String generateToken(String userName) throws Exception {

		Map<String, String> claims = new HashMap<String, String>();

		claims.put("action", "all");
		claims.put("sub", userName);
		claims.put("email", "dhongade.p@northeastern.edu");
		claims.put("aud", "*");

		return generateJwt(claims);
	}

	public DecodedJWT validate(String token) {
		
		try {
			
			final DecodedJWT jwt = JWT.decode(token);

			RSAPublicKey publicKey = loadPublicKey(jwt);

			Algorithm algorithm = Algorithm.RSA256(publicKey, null);

			JWTVerifier verifier = JWT.require(algorithm).withIssuer(jwt.getIssuer()).build();

			DecodedJWT decodedJWT = verifier.verify(token);

			return decodedJWT;

		} catch (Exception e) {
			throw new InvalidParameterException("JWT validation failed: " + e.getMessage());
		}
	}

	public String getUsername(DecodedJWT decodedJWT) {
		return decodedJWT.getSubject();
	}
	
	public static void main(String[] args) {
		try {

 			JwtUtilsService service = new JwtUtilsService();

			Map<String, String> claims = new HashMap<String, String>();
		
			claims.put("action", "read");
			claims.put("sub", "dhongade.p");
			claims.put("email", "dhongade.p@northeastern.edu");
			claims.put("aud", "*");
			
			String token = service.generateToken("dhongade.p");

//			String token = service.generateJwt(claims);
			
			System.out.println(token);

			DecodedJWT jwt = JWT.decode(token);

			RSAPublicKey publicKey = loadPublicKey(jwt);

			Algorithm algorithm = Algorithm.RSA256(publicKey, null);
			
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(jwt.getIssuer()).build();

			DecodedJWT verifiedToken = verifier.verify(token);
			
			System.out.println(verifiedToken);

		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
