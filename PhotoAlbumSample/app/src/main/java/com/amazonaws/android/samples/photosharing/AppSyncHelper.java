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

import android.content.Context;
import android.util.Log;

import com.amazonaws.amplify.generated.graphql.CreateAlbumMutation;
import com.amazonaws.amplify.generated.graphql.CreatePhotoMutation;
import com.amazonaws.amplify.generated.graphql.DeleteAlbumMutation;
import com.amazonaws.amplify.generated.graphql.DeletePhotoMutation;
import com.amazonaws.amplify.generated.graphql.ListAlbumsQuery;
import com.amazonaws.amplify.generated.graphql.UpdateAlbumMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.appsync.sigv4.CognitoUserPoolsAuthProvider;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Nonnull;

import type.CreateAlbumInput;
import type.CreatePhotoInput;
import type.DeleteAlbumInput;
import type.DeletePhotoInput;
import type.UpdateAlbumInput;

public class AppSyncHelper {

    private static final String TAG = AppSyncHelper.class.getSimpleName();

    private AWSAppSyncClient mAWSAppSyncClient;

    // Constructor
    public AppSyncHelper(Context context) {

        // Initialize AWSAppSyncClient
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(context)
                .awsConfiguration(new AWSConfiguration(context))
                .cognitoUserPoolsAuthProvider(new CognitoUserPoolsAuthProvider() {
                    @Override
                    public String getLatestAuthToken() {
                        try {
                            return AWSMobileClient.getInstance().getTokens().getIdToken().getTokenString();
                        } catch (Exception e) {
                            Log.e("APPSYNC_ERROR!", e.getLocalizedMessage());
                            return e.getLocalizedMessage();
                        }
                    }
                })
            .build();

    }

    private GraphQLCall.Callback<CreateAlbumMutation.Data> createAlbumCallback = new GraphQLCall.Callback<CreateAlbumMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateAlbumMutation.Data> response) {
            Log.i(TAG, "An album is created successfully in GraphQL.");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            e.printStackTrace();
        }
    };

    private GraphQLCall.Callback<DeleteAlbumMutation.Data> deleteAlbumCallback = new GraphQLCall.Callback<DeleteAlbumMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<DeleteAlbumMutation.Data> response) {
            Log.i(TAG, "An album is deleted successfully in GraphQL.");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            e.printStackTrace();
        }
    };

    private GraphQLCall.Callback<UpdateAlbumMutation.Data> updateAlbumCallback = new GraphQLCall.Callback<UpdateAlbumMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<UpdateAlbumMutation.Data> response) {
            Log.i(TAG, "An album is updated successfully in GraphQL.");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
        }
    };

    public void createAlbum(final String name, final String username, final String accessType) {

        CreateAlbumInput createAlbumInput = CreateAlbumInput.builder()
                .name(name)
                .username(username)
                .accesstype(accessType)
                .build();

        mAWSAppSyncClient.mutate(CreateAlbumMutation.builder().input(createAlbumInput).build())
                .enqueue(createAlbumCallback);
    }

    public void deleteAlbum(final String id) {

        DeleteAlbumInput deleteAlbumInput = DeleteAlbumInput.builder()
                .id(id)
                .build();

        mAWSAppSyncClient.mutate(DeleteAlbumMutation.builder().input(deleteAlbumInput).build())
                .enqueue(deleteAlbumCallback);
    }

    public void updateAlbum(final String name, final String id) {
        UpdateAlbumInput updateAlbumInput = UpdateAlbumInput.builder()
                .name(name)
                .id(id)
                .build();

        mAWSAppSyncClient.mutate(UpdateAlbumMutation.builder().input(updateAlbumInput).build())
                .enqueue(updateAlbumCallback);
    }

    public ArrayList<Album> listAlbums() {
        final ArrayList<Album> lstAlbum = new ArrayList<Album>();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mAWSAppSyncClient.query(ListAlbumsQuery.builder().limit(30).build()).responseFetcher(AppSyncResponseFetchers.NETWORK_ONLY).enqueue(new GraphQLCall.Callback<ListAlbumsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListAlbumsQuery.Data> response) {
                Log.d(TAG, "listAlbums succeeded." + response.data().toString());
                try {
                    List<ListAlbumsQuery.Item> items = response.data().listAlbums().items();
                    for (ListAlbumsQuery.Item item : items) {
                        lstAlbum.add(new Album(item.id(), R.drawable.album1, item.name(), item.accesstype(), new ArrayList<Photo>()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.d(TAG, "listAlbums failed." + e.getLocalizedMessage());
                e.printStackTrace();
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return lstAlbum;
    }

    private GraphQLCall.Callback<CreatePhotoMutation.Data> createPhotoCallback = new GraphQLCall.Callback<CreatePhotoMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreatePhotoMutation.Data> response) {
            Log.i(TAG, "A photo is added successfully.");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            e.printStackTrace();
        }
    };

    private GraphQLCall.Callback<DeletePhotoMutation.Data> deletePhotoCallback = new GraphQLCall.Callback<DeletePhotoMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<DeletePhotoMutation.Data> response) {
            Log.i(TAG, "A photo is deleted successfully.");
            try {

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            e.printStackTrace();
        }
    };

    public void createPhoto(final String name, final String albumID, final String key, final String bucket) {

        CreatePhotoInput createPhotoInput = CreatePhotoInput.builder()
                .photoAlbumId(albumID)
                .name(name)
                .key(key)
                .bucket(bucket)
                .build();

        mAWSAppSyncClient.mutate(CreatePhotoMutation.builder().input(createPhotoInput).build())
                .enqueue(createPhotoCallback);
    }

     public void deletePhoto(final String id) {

        DeletePhotoInput deletePhotoInput = DeletePhotoInput.builder()
                .id(id)
                .build();

        mAWSAppSyncClient.mutate(DeletePhotoMutation.builder().input(deletePhotoInput).build())
                .enqueue(deletePhotoCallback);
    }
}