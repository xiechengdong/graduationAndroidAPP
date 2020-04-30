package com.xcd.graduation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xcd.graduation.R;
import com.xcd.graduation.bean.Message;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //填充视图
        View view = View.inflate(context, R.layout.show_message,null);

        //查找控件
        TextView username = view.findViewById(R.id.message_username);
        TextView message_content = view.findViewById(R.id.message_content);
        TextView message_time = view.findViewById(R.id.message_time);
        TextView message_id = view.findViewById(R.id.message_id);

        //获取数据值
        Message message = messages.get(position);

        //把数据展示到控件
        username.setText(message.getUsername());
        message_content.setText(message.getContent());
        message_time.setText(message.getCreate_time());
        message_id.setText(message.getId());

        //返回
        return view;
    }
}
