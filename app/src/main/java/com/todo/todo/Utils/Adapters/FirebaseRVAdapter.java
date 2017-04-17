package com.todo.todo.Utils.Adapters;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class FirebaseRVAdapter {

    private String type, key;
    private Query mType ;

    public FirebaseRVAdapter(String type, DatabaseReference mMenu, String key) {
        this.type = type;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Todo");
        mType = mDatabase.orderByChild("type").equalTo(type);
        this.key = key;
    }
//
//    public FirebaseRecyclerAdapter<GalleryFormat, ImageViewHolder> showImage() {
//        return new FirebaseRecyclerAdapter<GalleryFormat, ImageViewHolder>(
//
//                GalleryFormat.class,
//                R.layout.layout_fullscreen_image,
//                ImageViewHolder.class,
//                mMenu
//        ) {
//            @Override
//            protected void populateViewHolder(ImageViewHolder viewHolder, GalleryFormat model, int position) {
//                viewHolder.setData(key, type);
//                viewHolder.setDelButton(model.getKey());
//                viewHolder.setImage(model.getImage());
//
//            }
//        };
//    }
}
