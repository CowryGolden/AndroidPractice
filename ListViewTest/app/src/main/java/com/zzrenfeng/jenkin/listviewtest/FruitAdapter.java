package com.zzrenfeng.jenkin.listviewtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit> {

    private int resourceId;

    public FruitAdapter(@NonNull Context context, int resource, @NonNull List<Fruit> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    /**
     * 获取视图；
     * 性能优化：
     * 1、当ListView滚动时，每次都将布局重新加载一遍，性能低下；将已经加载后的布局缓存到convertView对象中，以便重用，提高效率；
     * 2、每次调用getView()方式时，会调用View的findViewById()方法来获取一次控件的实例；借助ViewHolder来对此性能进行优化；
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit fruit = getItem(position);  //获取当前项的Fruit实例
        View view = null;
        ViewHolder viewHolder = null;
        if(null == convertView) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            if(null == viewHolder) {
                viewHolder = new ViewHolder();
            }
            viewHolder.fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            viewHolder.fruitName = (TextView) view.findViewById(R.id.fruit_name);
            view.setTag(viewHolder);  //将ViewHolder对象存储在View的tag属性中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.fruitImage.setImageResource(fruit.getImageId());
        viewHolder.fruitName.setText(fruit.getName());
        return view;
    }

    /**
     * 内部类：布局控件实例缓存器
     */
    class ViewHolder {
        ImageView fruitImage;
        TextView fruitName;
    }

}
