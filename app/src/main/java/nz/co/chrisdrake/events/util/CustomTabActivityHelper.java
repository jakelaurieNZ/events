// Derived from Chrome Custom Tabs, which is subject to the following license:
//
// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package nz.co.chrisdrake.events.util;

import android.app.Activity;
import android.content.ComponentName;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

/** A helper class to manage the connection to the Custom Tabs Service. */
public final class CustomTabActivityHelper {
    private CustomTabsSession session;
    private CustomTabsClient client;
    private CustomTabsServiceConnection serviceConnection;

    /**
     * Opens the URL on a Custom Tab if possible.
     *
     * @param activity the host Activity
     * @param customTabsIntent the {@link CustomTabsIntent} to be used if Custom Tabs is available
     * @param uri the URI to be opened
     * @param fallback the {@link CustomTabFallback} to be used if Custom Tabs is not available
     */
    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri,
        CustomTabFallback fallback) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);

        // If we can't find a package name, it means there's no browser installed that
        // supports Chrome Custom Tabs.
        if (packageName == null) {
            if (fallback != null) {
                fallback.openUri(activity, uri);
            }
        } else {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        }
    }

    /** Creates or retrieves an exiting {@link CustomTabsSession}. */
    @Nullable public CustomTabsSession getSession() {
        if (client == null) {
            session = null;
        } else if (session == null) {
            session = client.newSession(null);
        }
        return session;
    }

    /**
     * Binds the Activity to the Custom Tabs Service.
     *
     * @param activity the Activity to be bound to the service
     */
    public void bindCustomTabsService(Activity activity) {
        if (client != null) return;

        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName == null) return;

        serviceConnection = new CustomTabsServiceConnection() {
            @Override public void onServiceDisconnected(ComponentName name) {
                client = null;
                session = null;
            }

            @Override public void onCustomTabsServiceConnected(ComponentName componentName,
                CustomTabsClient client) {
                CustomTabActivityHelper.this.client = client;
                CustomTabActivityHelper.this.client.warmup(0L);
            }
        };
        CustomTabsClient.bindCustomTabsService(activity, packageName, serviceConnection);
    }

    /**
     * Unbinds the Activity from the Custom Tabs Service.
     *
     * @param activity the Activity that is connected to the service
     */
    public void unbindCustomTabsService(Activity activity) {
        if (serviceConnection == null) return;
        activity.unbindService(serviceConnection);
        client = null;
        session = null;
        serviceConnection = null;
    }

    /** To be used as a fallback to open the URI when Custom Tabs is not available. */
    public interface CustomTabFallback {
        /**
         * @param activity the Activity that wants to open the URI
         * @param uri the URI to be opened by the fallback
         */
        void openUri(Activity activity, Uri uri);
    }
}