package com.vedi.vedi_box.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vedi.vedi_box.databinding.SliderItemBinding;
import com.vedi.vedi_box.models.Product;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderListAdapter extends SliderViewAdapter<SliderListAdapter.MyHolder> {
    private final List<Product> mlist;
    private final Context mcontext;
    private final LayoutInflater minflater;


    public SliderListAdapter(Context context, List<Product> list) {
        mcontext = context;
        mlist = list;
        minflater = LayoutInflater.from(context);


    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent) {
        SliderItemBinding sliderItemBinding = SliderItemBinding.inflate(minflater, parent, false);
        return new MyHolder(sliderItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Product model = mlist.get(position);
        if (!model.getBanner_imageurl().equals("")) {
            Glide.with(mcontext).onLowMemory();
            Glide.with(mcontext).asBitmap().load(model.getBanner_imageurl()).apply(new RequestOptions().override(600, 400)).into(holder.sliderItemBinding.sliderImage);
        }


    }


    @Override
    public int getCount() {

        return mlist.size();
    }


    public class MyHolder extends SliderViewAdapter.ViewHolder {

        SliderItemBinding sliderItemBinding;

        public MyHolder(@NonNull SliderItemBinding binding) {
            super(binding.getRoot());
            this.sliderItemBinding = binding;

        }
    }
}

