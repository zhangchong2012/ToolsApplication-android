/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.zhangchong.libnetwork.Core.Exception;

import android.content.Intent;

import com.zhangchong.libnetwork.Core.NetworkResponse;

/**
 * Error indicating that there was an authentication failure when performing a Request.
 */
@SuppressWarnings("serial")
public class AuthFailureException extends NetException {
    /** An intent that can be used to resolve this exception. (Brings up the password dialog.) */
    private Intent mResolutionIntent;

    public AuthFailureException() { }

    public AuthFailureException(Intent intent) {
        mResolutionIntent = intent;
    }

    public AuthFailureException(NetworkResponse response) {
        super(response);
    }

    public AuthFailureException(String message) {
        super(message);
    }

    public AuthFailureException(String message, Exception reason) {
        super(message, reason);
    }

    public Intent getResolutionIntent() {
        return mResolutionIntent;
    }

    @Override
    public String getMessage() {
        if (mResolutionIntent != null) {
            return "User needs to (re)enter credentials.";
        }
        return super.getMessage();
    }
}
