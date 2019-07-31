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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AlbumAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Album> albumList;
    private boolean editable;
    private static final String TAG = AlbumAdapter.class.getSimpleName();
    private static String albumName;

    public AlbumAdapter(Activity activity, ArrayList<Album> albums, boolean editable) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.albumList = albums;
        this.editable = editable;
    }

    @Override
    public int getCount() {
        return albumList != null ? albumList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        ViewHolder holder = null;
        if (null == view) {
            view = inflater.inflate(R.layout.grid_album_item, null);
            holder = new ViewHolder();
            holder.album_image =  view.findViewById(R.id.album_image);
            holder.album_image.setImageResource(albumList.get(position).getiId());

            holder.delete_album = view.findViewById(R.id.delete_album);

            holder.album_name = view.findViewById(R.id.album_name);
            albumName = albumList.get(position).getName();
            holder.album_name.setText(albumName);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (editable) {
            holder.delete_album.setVisibility(View.VISIBLE);
            holder.album_name.setEnabled(true);

            holder.delete_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, AlbumActivity.class);
                    intent.putExtra("index", position);
                    // Request code 1 for delete
                    intent.putExtra("ReqCode", 1);
                    activity.startActivity(intent);
                }
            });

            final ViewHolder finalHolder = holder;
            holder.album_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText albumEditText = new EditText(activity);
                    AlertDialog dialog = new AlertDialog.Builder(activity)
                            .setTitle("Enter a new name for this album:")
                            .setView(albumEditText)
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    albumName = String.valueOf(albumEditText.getText());
                                    finalHolder.album_name.setText(albumName);

                                    // Pass the data to AlbumActivity
                                    Intent intent = new Intent(activity, AlbumActivity.class);
                                    intent.putExtra("index", position);
                                    intent.putExtra("newAlbumName", albumName);
                                    // Request code 2 for update
                                    intent.putExtra("ReqCode", 2);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog.show();
                    finalHolder.album_name.setText(albumName);

                }
            });

        } else {
            holder.album_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PhotoActivity.class);
                    intent.putExtra("AlbumIndex", position);
                    activity.startActivity(intent);
                }
            });
            holder.delete_album.setVisibility(View.GONE);
            holder.album_name.setEnabled(false);
        }

        return view;
    }

    protected class ViewHolder {

        protected ImageButton album_image;

        protected Button delete_album;

        protected TextView album_name;
    }
}