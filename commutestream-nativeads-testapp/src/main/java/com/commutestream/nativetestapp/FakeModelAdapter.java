package com.commutestream.nativetestapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.AdsController;
import com.commutestream.nativeads.ViewBinder;

import java.util.List;

public class FakeModelAdapter extends RecyclerView.Adapter<FakeModelAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView rowIdxView;
        public ImageView logoView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            rowIdxView = (TextView) itemView.findViewById(R.id.row_idx);
            logoView = (ImageView) itemView.findViewById(R.id.native_ad_logo);
        }
    }

    // Store a member variable for the FakeModels
    private List<FakeModel> mFakeModels;
    // Store the context for easy access
    private Context mContext;
    private AdsController mAdsController;
    private ViewBinder mViewBinder;


    // Pass in the FakeModel array into the constructor
    public FakeModelAdapter(Context context, List<FakeModel> fakeModels, AdsController adsController) {
        mFakeModels = fakeModels;
        mContext = context;
        mAdsController = adsController;
        mViewBinder = new ViewBinder(R.layout.row_layout)
                .setLogo(R.id.logoView);
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public FakeModelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View fakeModelView = inflater.inflate(R.layout.row_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(fakeModelView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(FakeModelAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        FakeModel fakeModel = mFakeModels.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.rowIdxView;
        textView.setText("Index: " + fakeModel.getIdx());
        Ad ad = fakeModel.getAd();
        if(ad != null) {
            Log.v("TESTING", "ad is not null " + fakeModel.getIdx());
            mAdsController.renderAdInto(viewHolder.itemView, mViewBinder, ad, true);
        } else {
            Log.v("TESTING", "ad is null " + fakeModel.getIdx());
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mFakeModels.size();
    }
}

