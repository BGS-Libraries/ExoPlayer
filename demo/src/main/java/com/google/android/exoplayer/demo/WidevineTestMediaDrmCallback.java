/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.google.android.exoplayer.demo;

import android.annotation.TargetApi;
import android.text.TextUtils;
import com.google.android.exoplayer.drm.ExoMediaDrm.KeyRequest;
import com.google.android.exoplayer.drm.ExoMediaDrm.ProvisionRequest;
import com.google.android.exoplayer.drm.MediaDrmCallback;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * A {@link MediaDrmCallback} for Widevine test content.
 */
@TargetApi(18)
public class WidevineTestMediaDrmCallback implements MediaDrmCallback {

  private static final String WIDEVINE_GTS_DEFAULT_BASE_URI =
      "https://proxy.uat.widevine.com/proxy";
  private static final Map<String, String> REQUEST_PROPERTIES =
      Collections.singletonMap("Content-Type", "application/octet-stream");

  private final String defaultUri;

  public WidevineTestMediaDrmCallback(String contentId, String provider) {
    if (contentId.equals(Samples.BRIGHTCOVE[0].contentId)) {
      defaultUri = "https://manifest.stage.boltdns.net/license/v1/cenc/widevine/alai/bf18e992-7785-44e0-95cd-35cf74b90f57/e238ea9e-5c97-4ccd-81fd-5247d892aa22?fastly_token=NWIwODIyMTdfYTJjZDAwNTc3N2U3ZGNiZDhmYjNkOTg1MTA4MDYyZDNkZTIyN2FmZWRmMmNkZDllMDVkNTU3OWViYjYzZTNlMQ%3D%3D";
    } else {
      String params = "?video_id=" + contentId + "&provider=" + provider;
      defaultUri = WIDEVINE_GTS_DEFAULT_BASE_URI + params;
    }
  }

  @Override
  public byte[] executeProvisionRequest(UUID uuid, ProvisionRequest request) throws IOException {
    String url = request.getDefaultUrl() + "&signedRequest=" + new String(request.getData());
    return Util.executePost(url, null, null);
  }

  @Override
  public byte[] executeKeyRequest(UUID uuid, KeyRequest request) throws IOException {
    String url = request.getDefaultUrl();
    if (TextUtils.isEmpty(url)) {
      url = defaultUri;
    }
    return Util.executePost(url, request.getData(), REQUEST_PROPERTIES);
  }

}
