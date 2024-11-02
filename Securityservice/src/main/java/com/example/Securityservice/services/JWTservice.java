package com.example.Securityservice.services;

import java.security.Key;
import java.sql.Date;
import java.util.Base64.Decoder;
import java.util.HashMap;

import org.hibernate.id.insert.GetGeneratedKeysDelegate;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTservice {
	
	private static final String SECERET="5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	

	public static String generatetoken(String username) {
		
		HashMap<String,Object> claims= new HashMap<>();
		
	return createtoken(username,claims);
		
		
	}

	private static String createtoken(String username,HashMap<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setSubject(username).setExpiration(new Date( System.currentTimeMillis()+1000*60*30)).setIssuedAt(new Date(System.currentTimeMillis())).signWith(getkey(),SignatureAlgorithm.HS256).compact();
	}

	private static Key getkey() {
		
		byte[] bytes = Decoders.BASE64.decode(SECERET);
		
		return Keys.hmacShaKeyFor(bytes);
		
	}

	public static boolean validateToken(String token) {
		
		return Jwts.parserBuilder().setSigningKey(getkey()).build().parseClaimsJws(token) != null;
		
	}
	
	
	
	
	
	
	

}
