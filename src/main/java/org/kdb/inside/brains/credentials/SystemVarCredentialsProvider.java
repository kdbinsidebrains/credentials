package org.kdb.inside.brains.credentials;

import org.kdb.inside.brains.core.credentials.CredentialEditor;
import org.kdb.inside.brains.core.credentials.CredentialProvider;
import org.kdb.inside.brains.core.credentials.CredentialsResolvingException;

public class SystemVarCredentialsProvider implements CredentialProvider {
    public static final String SPLITTER = ":env:";

    @Override
    public String getName() {
        return "System Variable Credentials";
    }

    @Override
    public String getVersion() {
        return "2.0";
    }

    @Override
    public String getDescription() {
        return "Example of CredentialsPlugin that takes password from environment variables";
    }

    @Override
    public boolean isSupported(String credentials) {
        if (credentials == null) {
            return false;
        }
        return credentials.contains(SPLITTER);
    }

    @Override
    public CredentialEditor createEditor() {
        return new SystemVarCredentialsEditor();
    }

    @Override
    public String resolveCredentials(String host, int port, String credentials) throws CredentialsResolvingException {
        final String[] v = split(credentials);
        return v[0] + ":" + System.getenv().get(v[1]);
    }

    public static String join(String u, String p) {
        return u + SPLITTER + p;
    }

    public static String[] split(String credentials) throws CredentialsResolvingException {
        int i = credentials.indexOf(SPLITTER);
        if (i < 0) {
            throw new CredentialsResolvingException("Incorrect credentials format, no :env: splitter");
        }
        return new String[]{credentials.substring(0, i), credentials.substring(i + SPLITTER.length())};
    }
}