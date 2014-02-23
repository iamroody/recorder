package com.example.recorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.recorder.BaseActivity;
import com.example.recorder.R;
import com.example.recorder.model.Message;
import com.example.recorder.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: gongming
 * Date: 2/23/14
 * Time: 11:22 上午
 * Email:gongmingqm10@foxmail.com
 */
public class ListMessageAdapter extends BaseAdapter {

    public BaseActivity context;
    public List<Message> messages;
    private Map<String, View> viewContainer;

    public ListMessageAdapter(BaseActivity context, List<Message> messages) {
        this.context = context;
        if(messages == null) messages = new ArrayList<Message>();
        this.messages = messages;
        viewContainer = new HashMap<String, View>();

    }
    @Override
    public int getCount() {
        return messages.size();
    }

    public void insertMessage(Message message) {
        messages.add(message);
        this.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Message message = messages.get(position);
        boolean isSender = message.isSender();
        if(isSender) {
            convertView = viewContainer.get(Constants.MESSAGE_OUT_KEY);
        }else{
            convertView = viewContainer.get(Constants.MESSAGE_IN_KEY);
        }
        if(convertView == null) {
            holder = new ViewHolder();
            if(isSender){
                convertView = context.getLayoutInflater().inflate(R.layout.list_message_out_item, null);
            }else{
                convertView = context.getLayoutInflater().inflate(R.layout.list_message_in_item, null);
            }
            holder.contentText = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.contentText.setText(message.getContent());

        return convertView;
    }

    private class ViewHolder {
        public TextView contentText;
    }

}
