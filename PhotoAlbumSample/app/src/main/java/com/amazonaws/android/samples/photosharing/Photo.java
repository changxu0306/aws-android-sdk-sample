/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.android.samples.photosharing;

import android.graphics.Bitmap;

public class Photo {
    private String id;
    private String name;
    private String bucket;
    private String key;
    private Bitmap file;

    public Photo(String id, String name, String bucket, String key, Bitmap file) {
        this.id = id;
        this.name = name;
        this.bucket = bucket;
        this.key = key;
        this.file = file;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getBucket() {
        return this.bucket;
    }

    public String getKey() {
        return this.key;
    }

    public Bitmap getPhoto(){ return this.file; }

}
