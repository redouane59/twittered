package com.github.redouane59.twitter.signature;
/*
 * Copyright (C) 2015 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Oauth1SigningInterceptor /* implements Interceptor*/ {
/*
  private static final Function<String, String> ESCAPER_FORM= form -> URLEncoder.encode(form, StandardCharsets.UTF_8);
  private static final String  OAUTH_CONSUMER_KEY           = "oauth_consumer_key";
  private static final String  OAUTH_NONCE                  = "oauth_nonce";
  private static final String  OAUTH_SIGNATURE              = "oauth_signature";
  private static final String  OAUTH_SIGNATURE_METHOD       = "oauth_signature_method";
  private static final String  OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
  private static final String  OAUTH_TIMESTAMP              = "oauth_timestamp";
  private static final String  OAUTH_ACCESS_TOKEN           = "oauth_token";
  private static final String  OAUTH_VERSION                = "oauth_version";
  private static final String  OAUTH_VERSION_VALUE          = "1.0";

  private final String consumerKey;
  private final String consumerSecret;
  private final String accessToken;
  private final String accessSecret;
  private final Random random;
  private final Clock  clock;

  private Oauth1SigningInterceptor(String consumerKey, String consumerSecret, String accessToken,
                                   String accessSecret, Random random, Clock clock) {
    this.consumerKey    = consumerKey;
    this.consumerSecret = consumerSecret;
    this.accessToken    = accessToken;
    this.accessSecret   = accessSecret;
    this.random         = random;
    this.clock          = clock;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    return chain.proceed(signRequest(chain.request()));
  }

  public Request signRequest(Request request) {
    byte[] nonce = new byte[32];
    random.nextBytes(nonce);
    String oauthNonce     = ByteString.of(nonce).base64().replaceAll("\\W", "");
    String oauthTimestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()).substring(0, 10);

    String consumerKeyValue = ESCAPER_FORM.apply(consumerKey);
    String accessTokenValue = ESCAPER_FORM.apply(accessToken);

    SortedMap<String, String> parameters = new TreeMap<>();
    parameters.put(OAUTH_CONSUMER_KEY, consumerKeyValue);
    parameters.put(OAUTH_ACCESS_TOKEN, accessTokenValue);
    parameters.put(OAUTH_NONCE, oauthNonce);
    parameters.put(OAUTH_TIMESTAMP, oauthTimestamp);
    parameters.put(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE);
    parameters.put(OAUTH_VERSION, OAUTH_VERSION_VALUE);

    HttpUrl url = request.url();
    for (int i = 0; i < url.querySize(); i++) {
      parameters.put(ESCAPER_FORM.apply(url.queryParameterName(i)),
                     ESCAPER_FORM.apply(url.queryParameterValue(i)));
    }

    RequestBody requestBody = request.body();
    Buffer      body        = new Buffer();
    try {
      if (requestBody != null) {
        requestBody.writeTo(body);
      }

      if (requestBody != null && requestBody.contentLength() > 2) {
        while (!body.exhausted()) {
          long keyEnd = body.indexOf((byte) '=');
          if (keyEnd == -1) {
            break;//throw new IllegalStateException("Key with no value: " + body.readUtf8());
          }
          String key = body.readUtf8(keyEnd);
          body.skip(1); // Equals.

          long   valueEnd = body.indexOf((byte) '&');
          String value    = valueEnd == -1 ? body.readUtf8() : body.readUtf8(valueEnd);
          if (valueEnd != -1) {
            body.skip(1); // Ampersand.
          }

          parameters.put(key, value);
        }
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    } finally {
      body.close();
    }

    Buffer base = new Buffer();
    try {
      String method = request.method();
      base.writeUtf8(method);
      base.writeByte('&');
      base.writeUtf8(ESCAPER_FORM.apply(request.url().newBuilder().query(null).build().toString()));
      base.writeByte('&');

      boolean first = true;
      for (Map.Entry<String, String> entry : parameters.entrySet()) {
        if (!first) {
          base.writeUtf8(ESCAPER_FORM.apply("&"));
        }
        first = false;
        base.writeUtf8(ESCAPER_FORM.apply(entry.getKey()));
        base.writeUtf8(ESCAPER_FORM.apply("="));
        base.writeUtf8(ESCAPER_FORM.apply(entry.getValue().replace("+", "%20")));
      }

      String signingKey =
          ESCAPER_FORM.apply(consumerSecret) + "&" + ESCAPER_FORM.apply(accessSecret);

      SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
      Mac           mac;
      try {
        mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
      } catch (NoSuchAlgorithmException | InvalidKeyException e) {
        throw new IllegalStateException(e);
      }
      byte[] result    = mac.doFinal(base.readByteArray());
      String signature = ByteString.of(result).base64();

      String authorization = "OAuth "
                             + OAUTH_CONSUMER_KEY + "=\"" + consumerKeyValue + "\", "
                             + OAUTH_NONCE + "=\"" + oauthNonce + "\", "
                             + OAUTH_SIGNATURE + "=\"" + ESCAPER_FORM.apply(signature) + "\", "
                             + OAUTH_SIGNATURE_METHOD + "=\"" + OAUTH_SIGNATURE_METHOD_VALUE + "\", "
                             + OAUTH_TIMESTAMP + "=\"" + oauthTimestamp + "\", "
                             + OAUTH_ACCESS_TOKEN + "=\"" + accessTokenValue + "\", "
                             + OAUTH_VERSION + "=\"" + OAUTH_VERSION_VALUE + "\"";
      return request.newBuilder()
                    .addHeader("Authorization", authorization)
                    .build();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    } finally {
      base.close();
    }
    return null;
  }

  public static final class Builder {

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessSecret;
    private Random random = new SecureRandom();
    private Clock  clock  = Clock.systemUTC();

    public Builder consumerKey(String consumerKey) {
      if (consumerKey == null) {
        throw new NullPointerException("consumerKey = null");
      }
      this.consumerKey = consumerKey;
      return this;
    }

    public Builder consumerSecret(String consumerSecret) {
      if (consumerSecret == null) {
        throw new NullPointerException("consumerSecret = null");
      }
      this.consumerSecret = consumerSecret;
      return this;
    }

    public Builder accessToken(String accessToken) {
      if (accessToken == null) {
        throw new NullPointerException("accessToken == null");
      }
      this.accessToken = accessToken;
      return this;
    }

    public Builder accessSecret(String accessSecret) {
      if (accessSecret == null) {
        throw new NullPointerException("accessSecret == null");
      }
      this.accessSecret = accessSecret;
      return this;
    }

    public Builder random(Random random) {
      if (random == null) {
        throw new NullPointerException("random == null");
      }
      this.random = random;
      return this;
    }

    public Builder clock(Clock clock) {
      if (clock == null) {
        throw new NullPointerException("clock == null");
      }
      this.clock = clock;
      return this;
    }

    public Oauth1SigningInterceptor build() {
      if (consumerKey == null) {
        throw new IllegalStateException("consumerKey not set");
      }
      if (consumerSecret == null) {
        throw new IllegalStateException("consumerSecret not set");
      }
      if (accessToken == null) {
        throw new IllegalStateException("accessToken not set");
      }
      if (accessSecret == null) {
        throw new IllegalStateException("accessSecret not set");
      }
      return new Oauth1SigningInterceptor(consumerKey, consumerSecret, accessToken, accessSecret, random,
                                          clock);
    }
  } */
}