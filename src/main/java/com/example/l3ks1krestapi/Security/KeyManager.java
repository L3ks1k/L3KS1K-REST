package com.example.l3ks1krestapi.Security;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class KeyManager {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static PublicKey loadPublicKey(String publicKeyFilename) throws Exception{
        URI uri = ClassLoader.getSystemResource(publicKeyFilename).toURI();
        String pathStr = Paths.get(uri).toString();
        Path path = Paths.get(pathStr);
        File file = new File(pathStr);
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        byte[] publicKeyBytes = new byte[(int)file.length()];
        dis.read(publicKeyBytes);
        dis.close();
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(publicSpec);
    }
    public static PrivateKey loadPrivateKey(String privateKeyFilename) throws Exception{
        URI uri = ClassLoader.getSystemResource(privateKeyFilename).toURI();
        String pathStr = Paths.get(uri).toString();
        File file = new File(pathStr);
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        byte[] privKeyBytes = new byte[(int)file.length()];
        dis.read(privKeyBytes);
        dis.close();
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
        return keyFactory.generatePrivate(privSpec);
    }
}
