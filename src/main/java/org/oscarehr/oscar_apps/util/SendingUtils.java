package org.oscarehr.oscar_apps.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;

public final class SendingUtils
{

	private static final Logger logger = MiscUtils.getLogger();
	private static final Integer CONNECTION_TIME_OUT = 10000;

	public static JSONObject send(AbstractMessage message, String url, String publicOscarKeyString, String publicServiceKeyString, String serviceName) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException, HL7Exception
	{
		PrivateKey publicServiceKey = getPublicServiceKey(publicServiceKeyString);
		PublicKey publicOscarKey = getPublicOscarKey(publicOscarKeyString);

		byte[] dataBytes = OscarToOscarUtils.pipeParser.encode(message).getBytes();

		return (send(dataBytes, url, publicOscarKey, publicServiceKey, serviceName));
	}

	public static JSONObject send(byte[] dataBytes, String url, PublicKey receiverOscarKey, PrivateKey publicServiceKey, String serviceName) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException
	{
		byte[] signature = getSignature(dataBytes, publicServiceKey);
		SecretKey senderSecretKey = createSecretKey();
		byte[] encryptedBytes = encryptData(dataBytes, senderSecretKey);
		byte[] encryptedSecretKey = encryptEncryptionKey(senderSecretKey, receiverOscarKey);

		ActionLog.logAction("Sending:" + new String(dataBytes, MiscUtils.ENCODING));

		return(postData(url, encryptedBytes, encryptedSecretKey, signature, serviceName));
	}

	private static JSONObject postData(String url, byte[] encryptedBytes, byte[] encryptedSecretKey, byte[] signature, String serviceName) throws IOException
	{
		MultipartEntity multipartEntity = new MultipartEntity();

		String filename = serviceName + '_' + System.currentTimeMillis() + ".hl7";
		multipartEntity.addPart("importFile", new ByteArrayBody(encryptedBytes, filename));
		multipartEntity.addPart("key", new StringBody(DataTypeUtils.encodeToBase64String(encryptedSecretKey)));
		multipartEntity.addPart("signature", new StringBody(DataTypeUtils.encodeToBase64String(signature)));
		multipartEntity.addPart("service", new StringBody(serviceName));
		multipartEntity.addPart("use_http_response_code", new StringBody("true"));

		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(multipartEntity);

		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.connection.timeout", CONNECTION_TIME_OUT);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		JSONObject result = new JSONObject();
		result.put("statusCode", statusCode);
		result.put("body", JSONObject.fromObject(EntityUtils.toString(httpResponse.getEntity())));
		logger.debug("result: " + result.toString());
		return result;
	}

	private static byte[] encryptEncryptionKey(SecretKey senderSecretKey, PublicKey receiverOscarKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, receiverOscarKey);
		return(cipher.doFinal(senderSecretKey.getEncoded()));
	}

	private static byte[] encryptData(byte[] dataBytes, SecretKey senderSecretKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
	{
		SecretKeySpec secretKeySpec = new SecretKeySpec(senderSecretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return(cipher.doFinal(dataBytes));
	}

	private static SecretKey createSecretKey() throws NoSuchAlgorithmException
	{
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		return(keyGenerator.generateKey());
	}

	private static byte[] getSignature(byte[] bytes, PrivateKey receiverServiceKey) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException
	{
		Signature signature = Signature.getInstance("MD5WithRSA");
		signature.initSign(receiverServiceKey);
		signature.update(bytes);
		return(signature.sign());
	}

	public static PublicKey getPublicOscarKey(String publicOscarKeyString) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(DataTypeUtils.decodeBase64(publicOscarKeyString));
		KeyFactory pubKeyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicOscarKey = pubKeyFactory.generatePublic(pubKeySpec);
		return publicOscarKey;
	}

	/**
	 * I know it returns a "private key" object but in reality it's a public key
	 * because it's a key we give out to other people.
	 */
	public static PrivateKey getPublicServiceKey(String publicServiceKeyString) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(DataTypeUtils.decodeBase64(publicServiceKeyString));
		KeyFactory privKeyFactory = KeyFactory.getInstance("RSA");
		PrivateKey publicServiceKey = privKeyFactory.generatePrivate(privKeySpec);
		return publicServiceKey;
	}

	public static void main(String... argv) throws Exception
	{
		String url = "http://localhost:49898/oscar/lab/newLabUpload.do";
		String publicOscarKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCYf7j1EXWHdbtZgn7e28Yc4a/0Mznx1irA0NW1yknJU9TScpFUVJ9LKmo3+pqAqaGkWmZgz4bn0XZQ/PJNw9z24dRwaVzOgjJ9h1ci/cmei80UK7uL7soS3c1Hj6lddkZbAJ5+F9amasRsaabFI+Gvevq0EYMIaETFjZiEkDNUwIDAQAB";
		String publicServiceKeyString = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI61yqsLH352cc+Ij3X/WzUKrFD6izRCwRqdS7RobiKmRqp6Ol1fiFJEOGGot1deNpIWJP05SNvx1qhMb9r6g8JamQvORR4QgUfKVOxwauOqm9myvSr8kPjAbQVHG8QcCBs+2qPfQU2whf+dpJFlx2P/Gx42W7S6HhK9awu9ZKeBAgMBAAECgYBRhwmBLZmQZZofNaS/hGJWqwJGQNvFv10SF0pohkBlCxjTy4AMV8dJOC/9mqUjBG+ohX4cK92zyTUYcJJ2Ryd9veIrVKM/3oUAXeHBaAHyaamFb8s6tZMHuJNJipV5igod/7nkRVGFa1RzamnMzrcnBLhqVZacwkN2F+BFzMTAAQJBAPxkQTT46O4DCbuNNxnIvMcpzIT17mhXNE+ZUOL1R2LMFM0bSyItkeiTaWQ1zgK4BPT3iAYiyvUq7fZOVGuGP9ECQQCQwBsubAM8R1STJERefMZRGAUg+UVTXatq9BK1xU9vQQCwKXBf78a+JLONQN/h8F8RXQduyyrNe0qpo7vTVAixAkA2zjJWpWI3JNO9NTns0Gkluk7d5GVjpOQIENu+nNJmgrhVnYKgJlMTtMbi6sgUUQ9KfmG8K1v1BuBrZrDwNFOxAkA9JSlWPsJPIEKVtWg8EbEkaGUiPKoQQS08DMYqiqK3eFn2EEsr+3mUsKQ4MwNfyc4e45FUN/ZovoAXkNayunjBAkAzhYBxLxjJTf7SBCjwcKus/Z0G1+mYWaKQuYWhyXhVJ7w8oNZ0KqoXECDYdeSAMEwGUkLHJjRtIFBHyJzR2vU6";

		send("foo bar was here".getBytes(), url, getPublicOscarKey(publicOscarKeyString), getPublicServiceKey(publicServiceKeyString), "server1");
	}
}
