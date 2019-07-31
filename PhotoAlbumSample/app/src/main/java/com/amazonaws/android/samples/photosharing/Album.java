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

import java.util.ArrayList;

/**
 * Defines Album class.
 * Each album has a unique album id, resource id, album name, cover image and a list of photos.
 */
public class Album {
    // This id refers to album id in GraphQL
    private String id;

    // This id refers to resource id defined in XML resource file
    private int iId;
    private String name;
    private String accessType;
    private ArrayList<Photo> photos;

    public Album(String id, int iId, String name, String accessType, ArrayList<Photo> photos) {
        this.id = id;
        this.iId = iId;
        this.name = name;
        this.accessType = accessType;
        this.photos = photos;
    }

    public Album(String name, ArrayList<Photo> photos) {
        this.name = name;
        this.photos = photos;
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

    public void setName(String newName) {
        this.name = newName;
    }

    public ArrayList<Photo> getPhotos() {
        return this.photos;
    }

}
