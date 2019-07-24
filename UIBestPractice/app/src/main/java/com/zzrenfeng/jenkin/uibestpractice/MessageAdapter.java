package com.zzrenfeng.jenkin.uibestpractice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 消息适配器
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMsgList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            this.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            this.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            this.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            this.rightMsg = (TextView) view.findViewById(R.id.right_msg);
        }
    }

    public MessageAdapter(List<Message> msgList) {
        this.mMsgList = msgList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Message message = mMsgList.get(position);
        if(message.getType() == Message.TYPE_RECEIVED) {
            //如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(message.getContent());
        } else if(message.getType() == Message.TYPE_SEND) {
            //如果是发送的消息，则显示右边的消息布局，将左边的消息布局隐藏
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

}
