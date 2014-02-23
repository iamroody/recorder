package com.example.recorder.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.recorder.BaseActivity;
import com.example.recorder.model.Message;

import java.util.List;

/**
 * User: gongming
 * Date: 2/23/14
 * Time: 11:22 上午
 * Email:gongmingqm10@foxmail.com
 */
public class ListMessageAdapter extends BaseAdapter {

    public BaseActivity context;
    public List<Message> messages;

    public ListMessageAdapter(BaseActivity context, List<Message> messages) {
        this.context = context;
        this.messages = messages;

    }
    @Override
    public int getCount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getItem(int position) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int position) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
