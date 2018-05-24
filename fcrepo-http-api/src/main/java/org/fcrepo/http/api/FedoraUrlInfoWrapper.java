package org.fcrepo.http.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class FedoraUrlInfoWrapper implements UriInfo {
    
    private UriInfo uriInfo;
    private HttpHeaders headers;
//    private static String ORIGIN = "origin";
    private static String X_FORWARD_PROTO = "x-forwarded-proto";
    private static String X_FORWARD_HOST = "x-forwarded-host";
    private static String FORWARDED = "forwarded";
    private static String FORWARDED_HOST_PARAM = "host";
    private static String FORWARDED_PROTOCOL_PARAM = "proto";
    
    private String protocol = null;
    private String host = null;
    private int port = -2;
    
    FedoraUrlInfoWrapper(UriInfo uriInfo, HttpHeaders headers) {
        this.uriInfo = uriInfo;
        this.headers = headers;
        
        String xForwardProto = getHeader(X_FORWARD_PROTO);
        if( xForwardProto != null ) protocol = xForwardProto;
        
        String xForwardHost = getHeader(X_FORWARD_HOST);
        if( xForwardHost != null ) setHost(xForwardHost);
        
        String forwarded = getHeader(FORWARDED);
        if( forwarded != null ) {
            Map<String, String> values = parseForwardedHeader(forwarded);
            if( values.containsKey(FORWARDED_HOST_PARAM) ) setHost(values.get(FORWARDED_HOST_PARAM));
            if( values.containsKey(FORWARDED_PROTOCOL_PARAM) ) protocol = values.get(FORWARDED_PROTOCOL_PARAM);
        }
    }
    
    private String getHeader(String name) {
        List<String> values = headers.getRequestHeader(name);
        if( values == null ) return null;
        if( values.size() == 0 ) return null;
        return values.get(0);
    }
    
    private Map<String, String> parseForwardedHeader(String forwarded) {
        Map<String, String> valueMap = new HashMap<String, String>();
        
        String[] parts = forwarded.split(";");
        for( String part : parts ) {
            String[] keyValue = part.split("=");
            if( keyValue.length < 2 ) continue;
            valueMap.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
        }
        
        return valueMap;
    }
    
    private String getRelativeHost(String host) {
        if( this.host != null ) return this.host;
        return host;
    }
    
    private String getRelativeProtocol(String protocol) {
        if( this.protocol != null ) return this.protocol;
        return protocol;
    }
    
    private int getRelativePort(int port) {
        if( this.port != -2 ) return this.port;
        return port;
    }
    
    private void setHost(String host) {
        try {
            // just want to split out port and host
            URI uri = new URI("http://"+host);
            this.host = uri.getHost();
            this.port = uri.getPort();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    
    private URI wrapUri(URI uri) {
        try {
            return new URI(
                    getRelativeProtocol(uri.getScheme()), uri.getUserInfo(), getRelativeHost(uri.getHost()),
                    getRelativePort(uri.getPort()), uri.getPath(), uri.getQuery(), uri.getFragment());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return uri;
        }
    }
    
    private UriBuilder wrapUriBuilder(UriBuilder builder) {
        URI uri = builder.build();
        return builder
            .scheme(getRelativeProtocol(uri.getScheme()))
            .host(getRelativeHost(uri.getHost()))
            .port(getRelativePort(uri.getPort()));
    }

    @Override
    public String getPath() {
        return uriInfo.getPath();
    }

    @Override
    public String getPath(boolean decode) {
        return uriInfo.getPath(decode);
    }

    @Override
    public List<PathSegment> getPathSegments() {
        return uriInfo.getPathSegments();
    }

    @Override
    public List<PathSegment> getPathSegments(boolean decode) {
        return uriInfo.getPathSegments();
    }

    @Override
    public URI getRequestUri() {
        return wrapUri(uriInfo.getRequestUri());
    }

    @Override
    public UriBuilder getRequestUriBuilder() {
        return wrapUriBuilder(uriInfo.getRequestUriBuilder());
    }

    @Override
    public URI getAbsolutePath() {
        return wrapUri(uriInfo.getAbsolutePath());
    }

    @Override
    public UriBuilder getAbsolutePathBuilder() {
        return wrapUriBuilder(uriInfo.getAbsolutePathBuilder());
    }

    @Override
    public URI getBaseUri() {
        return wrapUri(uriInfo.getBaseUri());
    }

    @Override
    public UriBuilder getBaseUriBuilder() {
        return wrapUriBuilder(uriInfo.getBaseUriBuilder());
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters() {
        return uriInfo.getPathParameters();
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters(boolean decode) {
        return uriInfo.getPathParameters(decode);
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        return uriInfo.getQueryParameters();
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
        return uriInfo.getQueryParameters(decode);
    }

    @Override
    public List<String> getMatchedURIs() {
        return uriInfo.getMatchedURIs();
    }

    @Override
    public List<String> getMatchedURIs(boolean decode) {
        return uriInfo.getMatchedURIs(decode);
    }

    @Override
    public List<Object> getMatchedResources() {
        return uriInfo.getMatchedResources();
    }

    @Override
    public URI resolve(URI uri) {
        return wrapUri(uriInfo.resolve(uri));
    }

    @Override
    public URI relativize(URI uri) {
        return wrapUri(uriInfo.relativize(uri));
    }

}
