package io.jt.autocrawler.http;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Http {
    private static HttpClient client;

    static {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000 * 60)
                .setConnectionRequestTimeout(1000 * 60)
                .setSocketTimeout(1000 * 60)
                .setRedirectsEnabled(true)
                .build();

        SSLConnectionSocketFactory factory = null;
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager xtm = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }
            };
            ctx.init(null, new TrustManager[]{xtm}, null);

            factory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            factory = SSLConnectionSocketFactory.getSocketFactory();
        }
        client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setSSLSocketFactory(
                factory).build();
    }


    static String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, HttpConst.DEFAULT_ENCODING);
        } catch (Exception e) {
        }
        return param;
    }

    private static String buildParamUrl(String url, Map<String, String> params) {
        String[] uriPath = url.split("/");
        String last = uriPath[uriPath.length - 1];
        String firstSymbol = "?";
        if (last.contains("?"))
            firstSymbol = "&";
        StringBuilder paramsUrl = new StringBuilder();
        int index = 0;
        for (Entry<String, String> param : params.entrySet()) {
            String symbol = "&";
            if (index == 0)
                symbol = firstSymbol;
            String name = param.getKey();
            String value = param.getValue();
            paramsUrl.append(symbol);
            paramsUrl.append(name);
            paramsUrl.append("=");
            paramsUrl.append(encodeParam(value));// 长度不控制
            index++;
        }
        return url + paramsUrl.toString();
    }

    private static void setHeaders(HttpUriRequest request, Map<String, String> headers) {
        for (Entry<String, String> header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }
    }

    public static HttpBuilder get(String url) {
        return new HttpBuilder(HttpMethod.GET, url);
    }

    public static HttpBuilder post(String url) {
        return new HttpBuilder(HttpMethod.POST, url);
    }

    public static class HttpBuilder {
        HttpMethod method;
        String url;
        List<Interceptor> interceptors = new ArrayList<Interceptor>();
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String> forms = new HashMap<String, String>();
        Map<String, byte[]> files = new HashMap<String, byte[]>();
        StringBuilder contents = new StringBuilder();

        public HttpBuilder(HttpMethod method, String url) {
            super();
            this.method = method;
            this.url = url;
        }

        public HttpBuilder header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public HttpBuilder param(String name, String value) {
            params.put(name, value);
            return this;
        }

        public HttpBuilder interceptor(Interceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }

        public HttpBuilder form(String name, String value) {
            forms.put(name, value);
            return this;
        }

        public HttpBuilder file(String name, byte[] bytes) {
            files.put(name, bytes);
            return this;
        }

        public HttpBuilder content(String content) {
            contents.append(content);
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }

        public static class Response {
            private String statusLine;
            private String contentType;
            private String contentEncoding;
            private long contentLength;
            private Map<String, String> headers = new HashMap<>();
            byte[] content;

            public String getStatusLine() {
                return statusLine;
            }

            public void setStatusLine(String statusLine) {
                this.statusLine = statusLine;
            }

            public String getContentType() {
                return contentType;
            }

            public void setContentType(String contentType) {
                this.contentType = contentType;
            }

            public String getContentEncoding() {
                return contentEncoding;
            }

            public void setContentEncoding(String contentEncoding) {
                this.contentEncoding = contentEncoding;
            }

            public long getContentLength() {
                return contentLength;
            }

            public void setContentLength(long contentLength) {
                this.contentLength = contentLength;
            }

            public Map<String, String> getHeaders() {
                return headers;
            }

            public void setHeaders(Map<String, String> headers) {
                this.headers = headers;
            }

            public byte[] getContent() {
                return content;
            }

            public void setContent(byte[] content) {
                this.content = content;
            }
        }

        public static class HttpClient {
            HttpBuilder builder;

            public HttpClient(HttpBuilder builder) {
                super();
                this.builder = builder;
            }

            HttpResponse execute() {
                String url = buildParamUrl(builder.url, builder.params);
                HttpUriRequest request = null;
                HttpResponse response = null;
                switch (builder.method) {
                    case GET:
                        request = new HttpGet(url);
                        setHeaders(request, builder.headers);
                        break;
                    case POST:
                        request = new HttpPost(url);
                        setHeaders(request, builder.headers);
                        try {
                            String content = builder.contents.toString();
                            if (StringUtils.isNotBlank(content)) {
                                StringEntity stringEntity = new StringEntity(content, HttpConst.DEFAULT_ENCODING);
                                HttpPost.class.cast(request).setEntity(stringEntity);
                            } else {

                                MultipartEntityBuilder multiBuilder = MultipartEntityBuilder.create();
                                for (Entry<String, String> form : builder.forms.entrySet()) {
                                    multiBuilder.addTextBody(form.getKey(), form.getValue(), ContentType.create("text/plain", HttpConst.DEFAULT_ENCODING));
                                }
                                for (Entry<String, byte[]> file : builder.files.entrySet()) {
                                    multiBuilder.addBinaryBody(file.getKey(), file.getValue());
                                }
                                HttpPost.class.cast(request).setEntity(multiBuilder.build());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                if (request != null) {
                    try {

                        for (Interceptor interceptor : builder.interceptors) {
                            interceptor.handle(url, request);
                        }
                        response = client.execute(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return response;
            }

            public String text(String charset) {
                byte[] bytes = content();
                return new String(bytes, Charset.forName(charset));
            }

            public String text() {
                return text(HttpConst.DEFAULT_ENCODING);
            }

            public JSONObject json() {
                return JSON.parseObject(text());
            }

            public byte[] content() {
                HttpResponse response = null;
                byte[] bytes = {};
                try {
                    response = execute();
                    bytes = IoUtil.readBytes(response.getEntity().getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (response != null && response instanceof CloseableHttpResponse) {
                        try {
                            CloseableHttpResponse.class.cast(response).close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return bytes;
            }

            public Map<String, String> headers() {
                HashMap<String, String> headers = new HashMap<>();
                HttpResponse response = null;
                try {
                    response = execute();
                    for (Header header : response.getAllHeaders()) {
                        headers.put(header.getName(), header.getValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (response != null && response instanceof CloseableHttpResponse) {
                        try {
                            CloseableHttpResponse.class.cast(response).close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return headers;
            }


            public Response response() {
                Response response = new Response();
                HttpResponse httpResponse = null;
                try {
                    httpResponse = execute();
                    for (Header header : httpResponse.getAllHeaders()) {
                        response.getHeaders().put(header.getName(), header.getValue());
                    }

                    response.setStatusLine(StrUtil.toStringOrNull(httpResponse.getStatusLine()));
                    HttpEntity entity = httpResponse.getEntity();
                    response.setContentLength(entity.getContentLength());
                    if (entity.getContentType() != null)
                        response.setContentType(entity.getContentType().getValue());
                    if (entity.getContentEncoding() != null)
                        response.setContentEncoding(entity.getContentEncoding().getValue());
                    response.setContent(IoUtil.readBytes(entity.getContent()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (httpResponse != null && httpResponse instanceof CloseableHttpResponse) {
                        try {
                            CloseableHttpResponse.class.cast(httpResponse).close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return response;
            }
        }
    }
}
