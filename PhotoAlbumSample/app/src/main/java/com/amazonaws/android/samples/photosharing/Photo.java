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

//  Created by Chang Xu on 6/25/19.
//  Copyright © 2019 AWSMobile. All rights reserved.

public class Photo {
    private String id;
    private int iId; // resource id
    private String name;
    private String bucket;
    private String key;
    private boolean backedUp; // whether it's uploaded to s3 or not

    public Photo(String id, int iId, String name, String bucket, String key) {
        this.id = id;
        this.iId = iId;
        this.name = name;
        this.bucket = bucket;
        this.key = key;
    }

    public String getId() {
        return this.id;
    }

    public int getiId() {
        return this.iId;
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

    public boolean getBackedUp() {
        return this.backedUp;
    }

    public void setBackedUp(boolean backedUp) {
        this.backedUp = backedUp;
    }

}
